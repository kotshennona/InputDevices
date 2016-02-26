package com.example.osipov.inputdevices;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String RESULT = "saved_result";
    private static final String DEVICE_NUMBER = "saved_device_number";

    IddqdClient iddqdClient;
    TextView tvResult;
    EditText etDeviceNumber;
    Button btnSendQuery;
    AsyncTask<Integer, Void, String> aTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.tvResult);
        etDeviceNumber = (EditText) findViewById(R.id.etDeviceNumber);
        btnSendQuery = (Button) findViewById(R.id.btnSendQuery);
        btnSendQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDeviceInfo();
            }
        });

        iddqdClient = new IddqdClient();
        try {
            iddqdClient.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedInstanceState != null) {
            tvResult.setText(savedInstanceState.getString(RESULT));
            etDeviceNumber.setText(Integer.toString(savedInstanceState.getInt(DEVICE_NUMBER)));
        }

    }

    private void queryDeviceInfo() {
        int argument;
        //This approach was chosen for simplicity's sake. Namely to avoid task queue.
        if (aTask != null && (aTask.getStatus() == AsyncTask.Status.PENDING
                || aTask.getStatus() == AsyncTask.Status.RUNNING)) {
            return;
        }

        aTask = new DeviceInfoQueryAsyncTask();

        if (!iddqdClient.isConnected()) {
            try {
                iddqdClient.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO: Extract into a method
        String devNumbers = etDeviceNumber.getText().toString();
        devNumbers = devNumbers.equals("") ? "0" : devNumbers;
        argument = Integer.parseInt(devNumbers);
        aTask.execute(argument);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT, tvResult.getText().toString());

        String devNumbers = etDeviceNumber.getText().toString();
        devNumbers = devNumbers.equals("") ? "0" : devNumbers;
        int devNumber = Integer.parseInt(devNumbers);
        outState.putInt(DEVICE_NUMBER, devNumber);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class DeviceInfoQueryAsyncTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int deviceNumber;
            String result = "";

            if (params.length < 1) {
                //What about exceptions?
                return "Error. No arguments provided.";
            }
            deviceNumber = params[0];

            try {
                result = iddqdClient.getInputDeviceInfo(deviceNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            tvResult.setText(s);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvResult.setText(s);
        }
    }

    @Override
    protected void onDestroy() {
        if (aTask != null) {
            aTask.cancel(true);
        }

        try {
            iddqdClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
