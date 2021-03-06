package com.smarthome;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.smarthome.ui.activity.SecureMainActivity;

import java.util.UUID;

/**
 * Created by saisu on 12/3/2016.
 */

public class ShadowApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a2kc9la4cp40qj.iot.us-east-1.amazonaws.com";
    public static ShadowApplication mInstance;
    final String TAG = ShadowApplication.class.getSimpleName();


    public AWSIotMqttManager mqttManager;
    private boolean misActivitypassed;

    @Override
    public void onCreate() {
        registerActivityLifecycleCallbacks(this);
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

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG, "on create");


    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "on Start");

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "on Resumed");
        misActivitypassed = false;

    }

    @Override
    public void onActivityPaused(Activity activity) {
        misActivitypassed = true;
        Log.d(TAG, "on pause");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "on activity stopped");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "on Destroyed");

    }


    public boolean isMisActivitypassed()
    {
        return misActivitypassed;
    }
}
