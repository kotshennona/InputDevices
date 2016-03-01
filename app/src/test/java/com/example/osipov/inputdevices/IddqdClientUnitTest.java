package com.example.osipov.inputdevices;

import android.net.LocalSocket;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.Mockito.when;

/**
 * Created by osipov on 01.03.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class IddqdClientUnitTest {

    private IddqdClient mIddqdClient;

    @Mock
    LocalSocket mockedLocalSocket;

    @Mock
    InputStream mockedInputStream;

    @Mock
    OutputStream mockedOutputStream;

    @Before
    public void initMocks() throws IOException {
        when(mockedLocalSocket.getOutputStream()).thenReturn(mockedOutputStream);
        when(mockedLocalSocket.getInputStream()).thenReturn(mockedInputStream);

        mIddqdClient = new IddqdClient(mockedLocalSocket);
        try {
            mIddqdClient.connect();
        } catch (IOException e) {
            fail("IddqdClient should not have thrown this exception.");
        }

        when(mockedLocalSocket.isConnected()).thenReturn(true);

    }

    @Test
    public void iddqdClient_isConnected() {
        assertTrue("Client is connected", mIddqdClient.isConnected());
    }

    @After
    public void cleanUp() throws IOException {
        mIddqdClient.disconnect();
    }
}
