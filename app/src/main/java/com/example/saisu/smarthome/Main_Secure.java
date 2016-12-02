
package com.example.saisu.smarthome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class Main_Secure extends Activity implements NetworkListener  {

    private static final String LOG_TAG = Main_Secure.class.getCanonicalName();
    ImageView mAlaram;
    TextView mAlaramTxt;

    final Context context = this;



    CharSequence[] items;
    // Customer specific IoT endpoint
    // AWS Iot CLI describe-endpoint call returns: XXXXXXXXXX.iot.<region>.amazonaws.com
   /* private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a2kc9la4cp40qj.iot.us-east-1.amazonaws.com";
    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // AWS IoT permissions.
    private static final String COGNITO_POOL_ID = "us-east-1:63486043-e999-4cc0-8d1c-c6b6423e1f3a";
    // Region of AWS IoT
    private static final Regions MY_REGION = Regions.US_EAST_1;

    CognitoCachingCredentialsProvider credentialsProvider;

    AWSIotDataClient iotDataClient;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__secure);
        SmartHomeStatus smartHomeStatus;

//        // Initialize the Amazon Cognito credentials provider
//        credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                COGNITO_POOL_ID, // Identity Pool ID
//                MY_REGION // Region
//        );
//
//        iotDataClient = new AWSIotDataClient(credentialsProvider);
//        String iotDataEndpoint = CUSTOMER_SPECIFIC_ENDPOINT;
//        iotDataClient.setEndpoint(iotDataEndpoint);

        Authnticate.getInstance().createConnection(getApplicationContext());

        mAlaram = (ImageView) findViewById(R.id.Alaram);
        executeShadowTask();


        mAlaram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);


                dialog.setContentView(R.layout.alrammode);
                dialog.setTitle(R.string.title);

                final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.group);
                RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.mode1);
                RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.mode2);

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

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int id = rg.getCheckedRadioButtonId();
                        // updtae id.gettext to shadow.
                    }
                });
                Button ok = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

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

    @Override
    public void onSuccess(Object object) {
        if (object instanceof SmartHomeControl) {
            SmartHomeControl smartHomeControl = (SmartHomeControl) object;

        } else if (object instanceof SmartHomeStatus) {
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;
           // System.out.println(smartHomeStatus.state.reported.Doors.getFrontDoor());
            mAlaramTxt = (TextView) findViewById(R.id.AlaramTxt);
            mAlaramTxt.setText(smartHomeStatus.state.reported.Controls.Alaram);
            System.out.println("i am in onsuccess");
        }
    }

    @Override
    public void onFailure() {
        System.out.println("i am in failure");
    }




}


