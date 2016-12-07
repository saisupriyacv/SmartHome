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

    //Save User Preference for login
    private static final String PreferID = "prefSetting";
    private static final String PreferUser = "username";
    private static final String PreferPass = "password";
    // UI references
    private EditText layoutUserName;
    private EditText layoutPassword;
    Button layoutSignInButton, layoutRequestButton;
    CheckBox layoutSaveCheck,layoutAutoLogin;
    UserDatabase mydb;

    //class field members
    private String userName, password;

    //FireBase CLoud masseging members
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(msg,"Oncreate called");

        layoutUserName = (EditText) findViewById(R.id.UserNameID);
        layoutPassword = (EditText) findViewById(R.id.PasswordID);
        layoutSignInButton = (Button) findViewById(R.id.LoginID);
        layoutRequestButton = (Button) findViewById(R.id.RequestID);
        layoutSaveCheck = (CheckBox) findViewById(R.id.SaveCheckBoxID);
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
        layoutUserName.setText(userName);
        layoutPassword.setText(password);


        //autologin checking
        if (userName != null )
        {
            mydb.setUserName(userName);
            if (mydb.getAutoLogin()!=null){
                if (mydb.getAutoLogin().equals("true"))
                    displayOnScreen("Checing autologin");
                displayOnScreen(mydb.getAutoLogin());
                startNewActivity();
            }
        }



        //OnClick Listener for SignInButton & SignUpButton
        layoutSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Code below gets the User name and Password
                userName = layoutUserName.getText().toString();
                password = layoutPassword.getText().toString();

                Log.d(msg,"Login button press");

                if (password.equals(mydb.getPassword(userName)))
                {
                    mydb.setUserName(userName);
                    //displayOnScreen ("username or password matched");
                    //set Username and Password for next time.
                    if (isCheckBox(1))
                    {
                        getSharedPreferences(PreferID,MODE_PRIVATE)
                                .edit()
                                .putString(PreferUser, userName)
                                .putString(PreferPass,password)
                                .commit();
                        //      .apply();
                    }

                    if (isCheckBox(2)){
                        mydb.setAutoLogin("true");
                        displayOnScreen("autologin is true");

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

        }

        Log.d(msg, "OnCreate called is over");

    }

    private boolean isCheckBox(int i) {
        boolean box = false;
        switch (i){
            case 1:
                box = layoutSaveCheck.isChecked();
                break;
            case 2:
                box= layoutAutoLogin.isChecked();
                break;
        }
        return box;
    }


    public void displayOnScreen (String displayStringLong){
        Toast.makeText(getApplicationContext(), displayStringLong ,Toast.LENGTH_LONG).show();
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
