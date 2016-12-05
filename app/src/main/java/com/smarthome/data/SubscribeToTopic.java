package com.smarthome.data;

import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;

import java.io.UnsupportedEncodingException;

/**
 * Created by sukesh.venkata on 12/4/16.
 */

public class SubscribeToTopic implements AWSIotMqttNewMessageCallback {

    private static final String TAG = SubscribeToTopic.class.getSimpleName();
    public String getMessage() {
        return message;
    }

    public String getTopic() {
        return topic;
    }

    private String message;

    private String topic;

    @Override
    public void onMessageArrived(String topic, byte[] data) {
        this.topic = topic;
        try {
            message = new String(data, "UTF-8");
            Log.d(TAG, " Message arrived:");
            Log.d(TAG, " Topic: " + this.topic);
            Log.d(TAG, " Message: " + message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
