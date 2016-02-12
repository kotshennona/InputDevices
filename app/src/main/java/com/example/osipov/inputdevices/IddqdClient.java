package com.example.osipov.inputdevices;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by osipov on 12.02.16.
 */
public class IddqdClient {

    private static final String SOCKET_NAME =  "inputdevinfo_socket";
    private static final byte MAX_ATTEMPTS_COUNT = 5;
    private static final String LOG_TAG = "IddqdClient";

    LocalSocket lSocket;
    LocalSocketAddress serverAddress;
    InputStream ioStream;

    public IddqdClient() {
        serverAddress = new LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
        lSocket = new LocalSocket();
    }

    private void connect() {

        byte attemptsCount = 0;

        // Currently results in Permission Denied error
        while (!lSocket.isConnected() && attemptsCount < MAX_ATTEMPTS_COUNT) {
            try {
                lSocket.connect(serverAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attemptsCount++;
        }
        Log.i(LOG_TAG, "Conection attempt no " + Integer.toString(attemptsCount));
    }

    private void disconnect() {
         if (!lSocket.isConnected()){
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
        connect();
        // Dummy implementation
        if (lSocket.isConnected()) {
            result = "Device no is " + Integer.toString(deviceNo);
        } else {
            result = "Error. Failed to connect";
        }
        disconnect();
        return result;
    }

}
