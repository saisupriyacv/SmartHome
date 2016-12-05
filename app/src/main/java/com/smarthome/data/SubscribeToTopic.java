package com.smarthome.data;

import android.app.Activity;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.google.gson.Gson;
import com.smarthome.ShadowApplication;
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.ui.activity.SecureMainActivity;
import com.smarthome.ui.listener.NetworkListener;

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

    private NetworkListener mnetworkListner;

    private Activity mActivity;

    public SubscribeToTopic(Activity activity, NetworkListener networkListner) {

        mnetworkListner = networkListner;
        mActivity = activity;
    }

    @Override
    public void onMessageArrived(String topic, byte[] data) {
        this.topic = topic;

        final String mTopic = topic;
        final byte[] mData = data;
       mActivity.runOnUiThread(new Runnable() {
           @Override
    public void run() {
               Gson gson = new Gson();
                try {
                    message = new String(mData, "UTF-8");
                   // SmartHomeStatus ts = gson.fromJson(message, SmartHomeStatus.class);
                    Log.d(TAG, " Message arrived:");
                    Log.d(TAG, " Topic: " + mTopic);
                    Log.d(TAG, " Message: " + message);
                    ((SecureMainActivity)mActivity).executeShadowTask();
//                   if (mnetworkListner != null) {
//                        mnetworkListner.onSuccess(ts);
//                    }
                    if(ShadowApplication.getInstance().isMisActivitypassed()){
                        // Notification bui ld
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
