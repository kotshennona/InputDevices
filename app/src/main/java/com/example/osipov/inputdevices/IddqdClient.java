package com.example.osipov.inputdevices;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by osipov on 12.02.16.
 */
public class IddqdClient {

    private static final String SOCKET_NAME = "inputdevinfo_socket";

    private static final String COMMAND_GET_DEVICE_INFO = "_get_input_device_info ";

    LocalSocket lSocket;
    LocalSocketAddress serverAddress;
    OutputStreamWriter outputStreamWriter;

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
        lSocket.close();
    }

    public String getInputDeviceInfo(int deviceNo) throws IOException {
        String result;
        String completeCommand;

        // Dummy implementation
        if (lSocket.isConnected()) {
            result = "Device no is " + Integer.toString(deviceNo);
            completeCommand = COMMAND_GET_DEVICE_INFO + Integer.toString(deviceNo) + "\n";

            if (outputStreamWriter == null) {
                outputStreamWriter = new OutputStreamWriter(lSocket.getOutputStream(), "utf-8");
            }

            outputStreamWriter.write(completeCommand);
            outputStreamWriter.flush();
        } else {
            result = "Error. Failed to connect";
        }
        return result;
    }

}
