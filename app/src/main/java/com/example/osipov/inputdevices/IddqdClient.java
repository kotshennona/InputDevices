package com.example.osipov.inputdevices;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by osipov on 12.02.16.
 */
public class IddqdClient {

    private static final String SOCKET_NAME = "inputdevinfo_socket";

    private static final String COMMAND_GET_DEVICE_INFO = "get_input_device_info ";
    private static final int READ_BUFFER_SIZE = 1024;
    private static final long TIMEOUT_MILLIS = 100;

    LocalSocket lSocket;
    LocalSocketAddress serverAddress;
    OutputStreamWriter outputStreamWriter;
    InputStreamReader inputStreamReader;

    public IddqdClient() {
        serverAddress = new LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
        lSocket = new LocalSocket();
    }

    public void connect() throws IOException {
        lSocket.connect(serverAddress);
    }

    public boolean isConnected() {
        return lSocket.isConnected();
    }

    public void disconnect() throws IOException {
        outputStreamWriter.close();
        outputStreamWriter = null;
        inputStreamReader.close();
        inputStreamReader = null;
        lSocket.close();
    }

    public String getInputDeviceInfo(int deviceNo) throws IOException {
        String result;
        String completeCommand;
        char[] readBuffer;
        readBuffer = new char[READ_BUFFER_SIZE];

        // Dummy implementation
        if (lSocket.isConnected()) {
            completeCommand = COMMAND_GET_DEVICE_INFO + Integer.toString(deviceNo) + "-\n";

            if (outputStreamWriter == null) {
                outputStreamWriter = new OutputStreamWriter(lSocket.getOutputStream(), "utf-8");
            }

            outputStreamWriter.write(completeCommand);
            outputStreamWriter.flush();

            if (inputStreamReader == null) {
                inputStreamReader = new InputStreamReader(lSocket.getInputStream(), "utf-8");
            }

            while (!inputStreamReader.ready()) {
                //ToDo: limit number of attempts
                try {
                    Thread.sleep(TIMEOUT_MILLIS);
                } catch (InterruptedException e) {
                    //Who did it? I didn't.
                    ;
                }
            }

            inputStreamReader.read(readBuffer, 0, READ_BUFFER_SIZE);
            result = new String(readBuffer);

        } else {
            result = "Error. Failed to connect";
        }
        return result;
    }

}
