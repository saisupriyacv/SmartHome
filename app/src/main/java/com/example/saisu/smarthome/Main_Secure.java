
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


public class Main_Secure extends FragmentActivity implements NetworkListener {

    private static final String LOG_TAG = Main_Secure.class.getCanonicalName();
    ImageView mAlaram;
    TextView mAlaramTxt;
    Button btn;


    // --- Constants to modify per your configuration ---

    // IoT endpoint
    // AWS Iot CLI describe-endpoint call returns: XXXXXXXXXX.iot.<region>.amazonaws.com
    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a2kc9la4cp40qj.iot.us-east-1.amazonaws.com";
    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // AWS IoT permissions.
    private static final String COGNITO_POOL_ID = "us-east-1:63486043-e999-4cc0-8d1c-c6b6423e1f3a";
    // Name of the AWS IoT policy to attach to a newly created certificate
    private static final String AWS_IOT_POLICY_NAME = "SmartHome-Policy";

    // Region of AWS IoT
    private static final Regions MY_REGION = Regions.US_EAST_1;
    // Filename of KeyStore file on the filesystem
    private static final String KEYSTORE_NAME = "iot_keystore";
    // Password for the private key in the KeyStore
    private static final String KEYSTORE_PASSWORD = "password";
    // Certificate and key aliases in the KeyStore
    private static final String CERTIFICATE_ID = "default";


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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__secure);
        SmartHomeStatus smartHomeStatus;

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


        // MQTT client IDs are required to be unique per AWS IoT account.
        // This UUID is "practically unique" but does not _guarantee_
        // uniqueness.
        clientId = UUID.randomUUID().toString();
        // tvClientId.setText(clientId);
        Log.i(LOG_TAG, "ClientId " + clientId);

        // Initialize the AWS Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // context
                COGNITO_POOL_ID, // Identity Pool ID
                MY_REGION // Region
        );

        Region region = Region.getRegion(MY_REGION);

        // MQTT Client
        mqttManager = new AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_ENDPOINT);

        // Set keepalive to 10 seconds.  Will recognize disconnects more quickly but will also send
        // MQTT pings every 10 seconds.
        mqttManager.setKeepAlive(10);

        // Set Last Will and Testament for MQTT.  On an unclean disconnect (loss of connection)
        // AWS IoT will publish this message to alert other clients.
        AWSIotMqttLastWillAndTestament lwt = new AWSIotMqttLastWillAndTestament("my/lwt/topic",
                "Android client lost connection", AWSIotMqttQos.QOS0);
        mqttManager.setMqttLastWillAndTestament(lwt);

        // IoT Client (for creation of certificate if needed)
        mIotAndroidClient = new AWSIotClient(credentialsProvider);
        mIotAndroidClient.setRegion(region);

        keystorePath = getFilesDir().getPath();
        keystoreName = KEYSTORE_NAME;
        keystorePassword = KEYSTORE_PASSWORD;
        certificateId = CERTIFICATE_ID;


        // To load cert/key from keystore on filesystem
        try {
            if (AWSIotKeystoreHelper.isKeystorePresent(keystorePath, keystoreName)) {
                if (AWSIotKeystoreHelper.keystoreContainsAlias(certificateId, keystorePath,
                        keystoreName, keystorePassword)) {
                    Log.i(LOG_TAG, "Certificate " + certificateId
                            + " found in keystore - using for MQTT.");
                    // load keystore from file into memory to pass on connection
                    clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId,
                            keystorePath, keystoreName, keystorePassword);

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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Create a new private key and certificate. This call
                        // creates both on the server and returns them to the
                        // device.
                        CreateKeysAndCertificateRequest createKeysAndCertificateRequest =
                                new CreateKeysAndCertificateRequest();
                        createKeysAndCertificateRequest.setSetAsActive(true);
                        final CreateKeysAndCertificateResult createKeysAndCertificateResult;
                        createKeysAndCertificateResult =
                                mIotAndroidClient.createKeysAndCertificate(createKeysAndCertificateRequest);
                        Log.i(LOG_TAG,
                                "Cert ID: " +
                                        createKeysAndCertificateResult.getCertificateId() +
                                        " created.");

                        // store in keystore for use in MQTT client
                        // saved as alias "default" so a new certificate isn't
                        // generated each run of this application
                        AWSIotKeystoreHelper.saveCertificateAndPrivateKey(certificateId,
                                createKeysAndCertificateResult.getCertificatePem(),
                                createKeysAndCertificateResult.getKeyPair().getPrivateKey(),
                                keystorePath, keystoreName, keystorePassword);

                        // load keystore from file into memory to pass on
                        // connection
                        clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId,
                                keystorePath, keystoreName, keystorePassword);

                        // Attach a policy to the newly created certificate.
                        // This flow assumes the policy was already created in
                        // AWS IoT and we are now just attaching it to the
                        // certificate.
                        AttachPrincipalPolicyRequest policyAttachRequest =
                                new AttachPrincipalPolicyRequest();
                        policyAttachRequest.setPolicyName(AWS_IOT_POLICY_NAME);
                        policyAttachRequest.setPrincipal(createKeysAndCertificateResult
                                .getCertificateArn());
                        mIotAndroidClient.attachPrincipalPolicy(policyAttachRequest);


                    } catch (Exception e) {
                        Log.e(LOG_TAG,
                                "Exception occurred when generating new private key and certificate.",
                                e);
                    }
                }
            }).start();
        }


        ///// Connecting to Server creating a UI Thread ///////////////////////////
        Log.d(LOG_TAG, "clientId = " + clientId);

        try {
            mqttManager.connect(clientKeyStore, new AWSIotMqttClientStatusCallback() {
                @Override
                public void onStatusChanged(final AWSIotMqttClientStatus status,
                                            final Throwable throwable) {
                    Log.d(LOG_TAG, "Status = " + String.valueOf(status));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status == AWSIotMqttClientStatus.Connecting) {

                                Log.d(LOG_TAG, "Connecting....");

                            } else if (status == AWSIotMqttClientStatus.Connected) {

                                Log.d(LOG_TAG, "Connected");
                                // mJsonTxt = (TextView) findViewById(R.id.textView2);

//////////////////Subscribing to the topic//////////////////////////
                                final String topic = "$aws/things/SmartHome/shadow/delta";

                                Log.d(LOG_TAG, "topic = " + topic);

                                Toast.makeText(Main_Secure.this,
                                        "to be done",
                                        Toast.LENGTH_SHORT).show();

                                try {
                                    mqttManager.subscribeToTopic(topic, AWSIotMqttQos.QOS0,
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


                            } else if (status == AWSIotMqttClientStatus.Reconnecting) {
                                if (throwable != null) {
                                    Log.e(LOG_TAG, "Connection error.", throwable);
                                }
                                // tvStatus.setText("Reconnecting");
                            } else if (status == AWSIotMqttClientStatus.ConnectionLost) {
                                if (throwable != null) {
                                    Log.e(LOG_TAG, "Connection error.", throwable);
                                }

                                Log.d(LOG_TAG, "Disconnected");
                            } else {

                                Log.d(LOG_TAG, "Disconnected");
                                result = "Disconnected";

                            }
                        }
                    });
                }
            });
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Connection error." + e.getMessage());

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
                if (mAlaramTxt.getText().toString().equalsIgnoreCase("armaway")) {

                    rd1.setText("Arm Home");
                    rd2.setText("Disaram");

                } else if (mAlaramTxt.getText().toString().equalsIgnoreCase("armhome")) {

                    rd1.setText("Arm Away");
                    rd2.setText("Disaram");

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
                        Log.i(LOG_TAG, newState);
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



}


