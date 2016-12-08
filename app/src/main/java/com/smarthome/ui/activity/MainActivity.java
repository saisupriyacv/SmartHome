package com.smarthome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.smarthome.R;
import com.smarthome.data.UserDatabase;

//import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends Activity {

    private static final String msg = "MainActivity : ";
    //Varibales save lost data while Rotation
    public static String EXTRA_USERNAME =
            "com.example.yogesh.homeautomation.userName";
    public static String EXTRA_PASSWORD =
            "com.example.yogesh.homeautomation.password";
    public static String EXTRA_PASSCODE =
            "com.example.yogesh.homeautomation.password";

    //Save User Preference for login
    private static final String PreferID = "prefSetting";
    private static final String PreferUser = "username";
    private static final String PreferPass = "password";
    private static final String PreferAutoLogin = "AutoLogin";
    private static final String PreferOneTimePass = "OnTimePass";
    // UI references
    private EditText layoutUserName;
    private EditText layoutPassword;
    private EditText layoutPassCode;
    Button layoutSignInButton, layoutRequestButton;
    CheckBox layoutAutoLogin;
    UserDatabase mydb;

    //class field members
    private String userName, password,autoLogin,OneTimePass;

    //FireBase CLoud masseging members
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(msg,"Oncreate called");

        layoutUserName = (EditText) findViewById(R.id.UserNameID);
        layoutPassword = (EditText) findViewById(R.id.PasswordID);
        layoutPassCode = (EditText) findViewById(R.id.PasscodeID);
        layoutSignInButton = (Button) findViewById(R.id.LoginID);
        layoutRequestButton = (Button) findViewById(R.id.RequestID);
        layoutAutoLogin = (CheckBox) findViewById(R.id.AutoLoginID);



        //getting user database for managing data
        mydb = new UserDatabase(getApplicationContext());
        mydb= mydb.open();


        //setting logoff condition
        mydb.setLogOff("false");

        //Save preference
        SharedPreferences sharePref = getSharedPreferences(PreferID,MODE_PRIVATE);
        userName = sharePref.getString(PreferUser,null);
        password = sharePref.getString(PreferPass,null);
        autoLogin = sharePref.getString(PreferAutoLogin,null);
        OneTimePass =  sharePref.getString(PreferOneTimePass,null);
        layoutUserName.setText(userName);
        layoutPassword.setText(password);

        if(autoLogin == null && OneTimePass == null)
        {
            layoutPassCode.setVisibility(View.VISIBLE);

        }
        else {
            layoutPassCode.setVisibility(View.INVISIBLE);

        }


        if (autoLogin!=null){
            if (autoLogin.equals("true")) {
                //displayOnScreen("Checking autologin");
                //displayOnScreen(mydb.getAutoLogin());
                startNewActivity();
            }
        }





/*
        //autologin checking
        if (userName != null )
        {
            mydb.setUserName(userName);
            if (mydb.getAutoLogin()!=null){
                if (mydb.getAutoLogin().equals("true")) {

                    displayOnScreen("Checking autologin");
                    displayOnScreen(mydb.getAutoLogin());
                    startNewActivity();
                }
            }
        }
       */




        //OnClick Listener for SignInButton & SignUpButton
        layoutSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Code below gets the User name and Password
                userName = layoutUserName.getText().toString();
                password = layoutPassword.getText().toString();

                Log.d(msg,"Login button press");


                if (password.equals(mydb.getPassword(userName))) {

                    if(Checking_condition(layoutPassCode.isShown()))
                    {
                        //displayOnScreen("one timme");

                        mydb.setUserName(userName);
                        mydb.setPasscode("1234");
                        //displayOnScreen ("username or password matched");
                        //set Username and Password for next time.
                        if (isCheckBox())

                        {
                            displayOnScreen("true check box");
                            getSharedPreferences(PreferID,MODE_PRIVATE)
                                    .edit()
                                    .putString(PreferUser, userName)
                                    .putString(PreferPass,password)
                                    .putString(PreferAutoLogin,"true")
                                    .putString(PreferOneTimePass,"1234")
                                    .commit();
                            //      .apply();
                        }
                        else
                        {
                            displayOnScreen("false check box");
                            getSharedPreferences(PreferID,MODE_PRIVATE)
                                    .edit()
                                    .putString(PreferAutoLogin,"false")
                                    .putString(PreferOneTimePass,"1234")
                                    .commit();
                            mydb.setAutoLogin("false");

                        }

                   /*
                    //FireBase Cloud massaging
                    token= FirebaseInstanceId.getInstance().getToken();
                    Log.d(msg,"Token is: "+token);
                    displayOnScreen(token);
                    */

                        //System Start
                        startNewActivity();

                    }
                    else{
                        //displayOnScreen("one timme");
                    }


                }
                else
                {
                    displayOnScreen ("username or password does not match, Enter again");
                }

            }
        });

        layoutRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(msg,"Request button press");
                Intent intent = new Intent(MainActivity.this,RequestAccess.class);
                intent.putExtra("HINT",1);
                startActivity(intent);

            }
        });

        // restore the input strings after a rotation
        if (savedInstanceState != null) {
            Log.d(msg, "Restoring input strings");
            layoutUserName.setText(savedInstanceState.getString(EXTRA_USERNAME));
            layoutPassword.setText(savedInstanceState.getString(EXTRA_PASSWORD));
            layoutPassCode.setText(savedInstanceState.getString(EXTRA_PASSCODE));

        }

        Log.d(msg, "OnCreate called is over");

    }


    private boolean Checking_condition(boolean shown) {
        boolean condtion =false;
        if (shown){
            if (layoutPassCode.getText().toString().equals("1234")){
                condtion = true;
            }
            else{
                condtion = false;
            }
        }
        else {
            condtion = true;
        }
        return condtion;
    }

    private boolean isCheckBox() {

        return layoutAutoLogin.isChecked();
    }


    public void displayOnScreen (String displayStringLong){
      //  Toast.makeText(getApplicationContext(), displayStringLong ,Toast.LENGTH_LONG).show();
    }

    public void startNewActivity(){
        Intent intent = new Intent(MainActivity.this,SecureMainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(EXTRA_USERNAME,
                layoutUserName.getText().toString());
        savedInstanceState.putString(EXTRA_PASSWORD,
                layoutPassword.getText().toString());
        savedInstanceState.putString(EXTRA_PASSCODE,
                layoutPassCode.getText().toString());
        Log.d(msg, "Saving input strings");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mydb.close();
        Log.d(msg, "The onDestroy() event called");
    }
}
