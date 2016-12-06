package com.smarthome.ui;

import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.smarthome.Constants;
import com.smarthome.ui.listener.ClientMqttStatusListener;



public class SecureAWSIotMqttClientManager implements AWSIotMqttClientStatusCallback {

    private static final String TAG = SecureAWSIotMqttClientManager.class.getSimpleName();
    private ClientMqttStatusListener clientMqttStatusListener;

    public SecureAWSIotMqttClientManager(ClientMqttStatusListener clientMqttStatusListener) {
        this.clientMqttStatusListener = clientMqttStatusListener;
    }

    @Override
    public void onStatusChanged(AWSIotMqttClientStatus status, Throwable throwable) {
        Log.d(TAG, "Client Status : " + status.name());
        String clientStatus;
        if (status == AWSIotMqttClientStatus.Connected) {
            clientStatus = Constants.CONNECTED;
        } else {
            clientStatus = Constants.INVALID;
        }
        if (clientMqttStatusListener != null) {
            clientMqttStatusListener.onStatusUpdate(clientStatus);
        }
    }
}
