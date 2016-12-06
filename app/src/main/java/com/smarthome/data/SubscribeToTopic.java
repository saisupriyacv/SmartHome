package com.smarthome.data;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.google.gson.Gson;
import com.smarthome.ShadowApplication;
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.ui.activity.SecureMainActivity;
import com.smarthome.ui.listener.NetworkListener;

import java.io.UnsupportedEncodingException;



public class SubscribeToTopic implements AWSIotMqttNewMessageCallback  {

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
       final Object obj = (((SecureMainActivity)mActivity).GetObject());
       final SmartHomeStatus Status = (SmartHomeStatus) obj;


       mActivity.runOnUiThread(new Runnable() {
           @Override
    public void run() {
               Gson gson = new Gson();
                try {
                    message = new String(mData, "UTF-8");


                    Log.d(TAG, " Message arrived:");
                    Log.d(TAG, " Topic: " + mTopic);
                    Log.d(TAG, " Message: " + message);




                        if(!message.contains("desired")){
                            Log.d(TAG, " Message in if: " + message);
                            SmartHomeStatus ts = gson.fromJson(message, SmartHomeStatus.class);
                            CompareObjects(ts, (SmartHomeStatus) obj);
                            ((SecureMainActivity) mActivity).onSuccess(obj);
                        }





                    if(ShadowApplication.getInstance().isMisActivitypassed()){

                        if(!(Status.getState().getReported().getControls().getAlaram().equalsIgnoreCase("disarm"))) {

                            ((SecureMainActivity) mActivity).Notify(Result, "hello");
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    String Result;
    public void CompareObjects(SmartHomeStatus desired, SmartHomeStatus reported){
        Result = "";
        if(desired.equals(reported)){
            Log.d(TAG,"Both equal");
        }
        else
        {
            if(!(desired.getState().getReported().getDoors().getFrontDoor().equals( reported.getState().getReported().getDoors().getFrontDoor() ))){
                Log.d(TAG,"front Door not equal");
                Result  += "Front Door " + desired.getState().getReported().getDoors().getFrontDoor();
                reported.getState().getReported().getDoors().setFrontDoor(desired.getState().getReported().getDoors().getFrontDoor());
            }
            if(!(desired.getState().getReported().getDoors().getBackDoor().equals( reported.getState().getReported().getDoors().getBackDoor() ))){
                Log.d(TAG,"Back Door not equal");
                Result += "Back Door " + desired.getState().getReported().getDoors().getBackDoor();
                reported.getState().getReported().getDoors().setBackDoor(desired.getState().getReported().getDoors().getBackDoor());
            }

            if(!(desired.getState().getReported().getControls().getAlaram().equals( reported.getState().getReported().getControls().getAlaram() ))){
                Log.d(TAG,"Alaram  not equal");
                Result += "Alaram is now " + desired.getState().getReported().getControls().getAlaram();
                reported.getState().getReported().getControls().setAlaram(desired.getState().getReported().getControls().getAlaram());

            }

            if(!(desired.getState().getReported().getControls().getSwitch().equals( reported.getState().getReported().getControls().getSwitch() ))){
                Log.d(TAG,"Switch  not equal");
                Result += "Switch is Switched " + desired.getState().getReported().getControls().getSwitch();
                reported.getState().getReported().getControls().setSwitch(desired.getState().getReported().getControls().getSwitch());
            }
            if(!(desired.getState().getReported().getTemperature().getTemperature().equals( reported.getState().getReported().getTemperature().getTemperature() ))) {
                Log.d(TAG, "Temperature  not equal");
                Result += "Temperature is changed to " + desired.getState().getReported().getTemperature().getTemperature();
                reported.getState().getReported().getTemperature().setTemperature(desired.getState().getReported().getTemperature().getTemperature());

            }

            Log.d(TAG,Result);


        }



    }

}
