
package com.example.saisu.smarthome;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttLastWillAndTestament;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.UUID;


public class Main_Secure extends FragmentActivity implements NetworkListener, ClientMqttStatusListener {

    private static final String LOG_TAG = Main_Secure.class.getCanonicalName();
    ImageView mAlaram;
    TextView mAlaramTxt;
    Button btn;


    final Context context = Main_Secure.this;

    AWSIotClient mIotAndroidClient;
    AWSIotMqttManager mqttManager;
    String clientId;
    String keystorePath;
    String keystoreName;
    String keystorePassword;

    KeyStore clientKeyStore = null;
    String certificateId;

    TextView mJsonTxt;


    CognitoCachingCredentialsProvider credentialsProvider;

    String result;
    String SelectedText;
    final String topic = "$aws/things/SmartHome/shadow/update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__secure);
        SmartHomeStatus smartHomeStatus;
        //Cognito Pool Authentications
        Authnticate.getInstance().createConnection(getApplicationContext());

        Button btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.frame_container);

                if(fragment == null)
                {

                    fragment = new AlaramFragment();
                    fm.beginTransaction()
                            .add(R.id.frame_container, fragment)
                            .commit();

                }


            }
        });

        // To load cert/key from keystore on filesystem
        try {
            if (AWSIotKeystoreHelper.isKeystorePresent(Constants.KEY_STORE_PATH, Constants.KEYSTORE_NAME)) {
                if (AWSIotKeystoreHelper.keystoreContainsAlias(Constants.CERTIFICATE_ID, Constants.KEY_STORE_PATH,
                        Constants.KEYSTORE_NAME, Constants.KEYSTORE_PASSWORD)) {
                    Log.i(LOG_TAG, "Certificate " + Constants.CERTIFICATE_ID
                            + " found in keystore - using for MQTT.");
                    // load keystore from file into memory to pass on connection
                    clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(Constants.CERTIFICATE_ID,
                            Constants.KEY_STORE_PATH, Constants.KEYSTORE_NAME, Constants.KEYSTORE_PASSWORD);

                } else {
                    Log.i(LOG_TAG, "Key/cert " + certificateId + " not found in keystore.");
                }
            } else {
                Log.i(LOG_TAG, "Keystore " + keystorePath + "/" + keystoreName + " not found.");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "An error occurred retrieving cert/key from keystore.", e);
        }

        if (clientKeyStore == null) {
            Log.i(LOG_TAG, "Cert/key was not found in keystore - creating new key and certificate.");
            new VerifyCertificateTask(this).execute();
        } else {

           ShadowApplication.getInstance().getIotManager().connect(clientKeyStore, new AWSIotMqttClientStatusCallback() {
             @Override
            public void onStatusChanged(AWSIotMqttClientStatus status, Throwable throwable) {

                    String clientStatus = "";
                    onStatusUpdate(clientStatus);
                    Log.i("", "Client status : " + status.name());
                    if (status == AWSIotMqttClientStatus.Connected) {
                        clientStatus = Constants.CONNECTED;
                        try {
                            ShadowApplication.getInstance().getIotManager().subscribeToTopic(topic, AWSIotMqttQos.QOS0,
                                    new AWSIotMqttNewMessageCallback() {
                                        @Override
                                        public void onMessageArrived(final String topic, final byte[] data) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        String message = new String(data, "UTF-8");
                                                        Log.d(LOG_TAG, "Message arrived:");
                                                        Log.d(LOG_TAG, "   Topic: " + topic);
                                                        Log.d(LOG_TAG, " Message: " + message);

                                                    } catch (UnsupportedEncodingException e) {
                                                        Log.e(LOG_TAG, "Message encoding error.", e);
                                                    }
                                                }
                                            });
                                        }
                                    });
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Subscription error.", e);
                        }
                    } else {
                        Log.d(LOG_TAG, "Not Subscribed as client not connected");
                    }



               }
            });
        }

        mAlaram = (ImageView) findViewById(R.id.Alaram);
        executeShadowTask();


        mAlaram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);


                dialog.setContentView(R.layout.alrammode);
                dialog.setTitle(R.string.title);

                final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.group);
               final RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.mode1);
               final RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.mode2);

                //  System.out.println("alaram in text field" + mAlaramTxt.getText().toString());
                if (mAlaramTxt.getText().toString().equalsIgnoreCase("arm away")) {

                    rd1.setText("Arm Home");
                    rd2.setText("Disarm");

                } else if (mAlaramTxt.getText().toString().equalsIgnoreCase("arm home")) {

                    rd1.setText("Arm Away");
                    rd2.setText("Disarm");

                } else if (mAlaramTxt.getText().toString().equalsIgnoreCase("disarm")) {

                    rd1.setText("Arm Home");
                    rd2.setText("Arm Away");

                }
                final SmartHomeStatus update = (SmartHomeStatus) GetObject();

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        int id = rg.getCheckedRadioButtonId();
                        if(rd1.isChecked()) {
                            update.state.reported.Controls.setAlaram(rd1.getText().toString());
                            SelectedText = rd1.getText().toString();
                            Log.d(LOG_TAG, "button selected " + rd1.getText());
                        }
                        else if(rd2.isChecked()){
                            update.state.reported.Controls.setAlaram(rd2.getText().toString());
                            SelectedText = rd2.getText().toString();
                            Log.d(LOG_TAG, "button selected " + rd2.getText());

                        }
                           // updtae id.gettext to shadow.
                    }
                });
                Button ok = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mAlaramTxt.setText(SelectedText+" " +  "Setting "+"......");
                        Log.d(LOG_TAG, "Text changed on Ok " + rd2.getText());
                        String newState = String.format("{\"state\":{\"desired\":{\"Controls\":{\"Alaram\":\"%s\"}}}}", SelectedText);
                        Log.i(LOG_TAG, "Subscribed result: " + newState);
                        executeUpdateShadowTask(newState);

                    /*    ProgressDialog dialog1 = ProgressDialog.show(
                                context,
                                "Experimental",
                                "Progress dialog test");
                        dialog1.setProgress(1);*/


                    }
                });

                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();


            }
        });



    }
    public void getShadow(View view) {
        executeShadowTask();
    }

    public void executeShadowTask() {
        GetShadowTask getStatusShadowTask = new GetShadowTask("SmartHome", this);
        getStatusShadowTask.execute();
    }
    public void executeUpdateShadowTask(String newState){
        UpdateShadowTask updateShadowTask = new UpdateShadowTask(this);
        updateShadowTask.setState(newState);
        updateShadowTask.execute();


    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof SmartHomeControl)
        {
            SmartHomeControl smartHomeControl = (SmartHomeControl) object;

        }
        if (object instanceof SmartHomeStatus) {
            SetObject(object);
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;

            System.out.println(smartHomeStatus.state.reported.Controls.Alaram);

            mAlaramTxt = (TextView) findViewById(R.id.AlaramTxt);
            mAlaramTxt.setText(smartHomeStatus.state.reported.Controls.getAlaram());
            // System.out.println(smartHomeStatus.state.reported.Doors.getFrontDoor());

            System.out.println("i am in onsuccess");
        }
    }

    Object mObject;
    public  void SetObject(Object object)
    {
        mObject = object;

    }

    public  Object GetObject()

    {
        return mObject;

    }
    @Override
    public void onFailure() {
        System.out.println("i am in failure");
    }


    @Override
    public void onStatusUpdate(String status) {

        if (status == Constants.CONNECTED) {
            //

        }
    }
}


