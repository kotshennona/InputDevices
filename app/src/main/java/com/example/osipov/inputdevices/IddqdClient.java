package com.example.osipov.inputdevices;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by osipov on 12.02.16.
 */
public class IddqdClient {

    private static final String SOCKET_NAME = "inputdevinfo_socket";

    LocalSocket lSocket;
    LocalSocketAddress serverAddress;
    InputStream ioStream;

    public IddqdClient() {
        serverAddress = new LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
        lSocket = new LocalSocket();
    }

    public void connect() throws IOException {
        lSocket.connect(serverAddress);
    }

    public void disconnect() {
        if (!lSocket.isConnected()) {
            return;
        }

        try {
            lSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getInputDeviceInfo(int deviceNo) {
        String result;
        // Dummy implementation
        if (lSocket.isConnected()) {
            result = "Device no is " + Integer.toString(deviceNo);
        } else {
            result = "Error. Failed to connect";
        }
        return result;
    }

}
