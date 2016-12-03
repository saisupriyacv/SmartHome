package com.example.saisu.smarthome;

import android.app.Application;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;

import java.util.UUID;

/**
 * Created by saisu on 12/3/2016.
 */

public class ShadowApplication extends Application {

    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a2kc9la4cp40qj.iot.us-east-1.amazonaws.com";
    public static ShadowApplication mInstance;

    public AWSIotMqttManager mqttManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mqttManager = new AWSIotMqttManager(UUID.randomUUID().toString(),CUSTOMER_SPECIFIC_ENDPOINT);
        mqttManager.setKeepAlive(10);
    }

    public static ShadowApplication getInstance() {;
        return mInstance;
    }

    public AWSIotMqttManager getIotManager() {
        return mqttManager;
    }
}
