package com.smarthome.ui.activity;




import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ImageView;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.smarthome.data.SubscribeToTopic;
import com.smarthome.network.GetShadowTask;
import com.smarthome.network.UpdateShadowTask;
import com.smarthome.ui.SecureAWSIotMqttClientManager;
import com.smarthome.data.Authenticate;
import com.smarthome.ui.fragment.BaseFragment;
import com.smarthome.ui.fragment.DoorFragment;
import com.smarthome.ui.fragment.HistoryFragment;
import com.smarthome.ui.fragment.ProfileFragment;
import com.smarthome.ui.fragment.SwitchFragment;
import com.smarthome.ui.fragment.TemperatureFragment;
import com.smarthome.ui.listener.ClientMqttStatusListener;
import com.smarthome.Constants;
import com.smarthome.ui.listener.NetworkListener;
import com.smarthome.R;
import com.smarthome.ShadowApplication;
import com.smarthome.network.model.SmartHomeControl;
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.network.VerifyCertificateTask;

import java.security.KeyStore;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SecureMainActivity extends ActionBarActivity implements NetworkListener, ClientMqttStatusListener ,NavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = SecureMainActivity.class.getSimpleName();
    public final String Extra = "com.smarthome.ui.activity.itemid";

    private ImageView mAlaram;
    private TextView mAlaramTxt;
    private Button btn;

    final Context context = SecureMainActivity.this;

    private KeyStore clientKeyStore = null;

    private String SelectedText;

    NotificationManager manager;
    Notification myNotication;

    private SubscribeToTopic subscribeToTopic;
    private final String topic = "$aws/things/SmartHome/shadow/update";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__secure);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        ValueAnimator skyAnim = ObjectAnimator.ofInt
                (findViewById(R.id.car_layout), "backgroundColor",
                        Color.rgb(0x66, 0xcc, 0xff), Color.rgb(0x00, 0x66, 0x99));
        skyAnim.setDuration(3000);
        skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();

        ObjectAnimator cloudAnim6 = ObjectAnimator.ofFloat(findViewById(R.id.cloud6), "x", -300);
        cloudAnim6.setDuration(7000);
        cloudAnim6.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim6.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim6.start();

        ObjectAnimator cloudAnim0 = ObjectAnimator.ofFloat(findViewById(R.id.cloud0), "x", -300);
        cloudAnim0.setDuration(2000);
        cloudAnim0.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim0.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim0.start();
        ObjectAnimator cloudAnim = ObjectAnimator.ofFloat(findViewById(R.id.cloud1), "x", -270);
        cloudAnim.setDuration(4000);
        cloudAnim.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim.start();
        ObjectAnimator cloudAnim2 = ObjectAnimator.ofFloat(findViewById(R.id.cloud2), "x", -250);
        cloudAnim2.setDuration(6000);
        cloudAnim2.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim2.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim2.start();
        ObjectAnimator cloudAnim3 = ObjectAnimator.ofFloat(findViewById(R.id.cloud3), "x", -220);
        cloudAnim3.setDuration(3000);
        cloudAnim3.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim3.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim3.start();
        ObjectAnimator cloudAnim4 = ObjectAnimator.ofFloat(findViewById(R.id.cloud4), "x", -220);
        cloudAnim4.setDuration(5000);
        cloudAnim4.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim4.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim4.start();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager manager=this.getSupportFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Cognito Pool Authentications
        Authenticate.getInstance().createConnection(getApplicationContext());



        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {
                Fragment fragment = null;
                if(view.getId() == R.id.button1){
                    fragment = new DoorFragment();
                } else if(view.getId() == R.id.button2) {
                    fragment = new SwitchFragment();
                }
                else if(view.getId() == R.id.tempbtn){
                    fragment =new TemperatureFragment();
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };

        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(listener);
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(listener);
        Button btn3 = (Button)findViewById(R.id.tempbtn);
        btn3.setOnClickListener(listener);



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

                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "An error occurred retrieving cert/key from keystore.", e);
        }

        if (clientKeyStore == null) {
            Log.i(LOG_TAG, "Cert/key was not found in keystore - creating new key and certificate.");
            new VerifyCertificateTask(this).execute();
        } else {
            connectIotManager();
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
                        if (rd1.isChecked()) {
                            update.getState().getReported().getControls().setAlaram(rd1.getText().toString());
                            SelectedText = rd1.getText().toString();
                            Log.d(LOG_TAG, "button selected " + rd1.getText());
                        } else if (rd2.isChecked()) {
                            update.getState().getReported().getControls().setAlaram(rd2.getText().toString());
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
                        mAlaramTxt.setText(SelectedText + " " + "Setting " + "......");
                        Log.d(LOG_TAG, "Text changed on Ok " + rd2.getText());
                        String newState = String.format("{\"state\":{\"desired\":{\"Controls\":{\"Alaram\":\"%s\"}}}}", SelectedText);
                        Log.i(LOG_TAG, "Sending data to shadow: " + newState);
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

    private void launchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commitAllowingStateLoss();

    }

    public void getShadow(View view) {
        executeShadowTask();
    }

    public void executeShadowTask() {
        GetShadowTask getStatusShadowTask = new GetShadowTask("SmartHome", this);
        getStatusShadowTask.execute();
    }

    public void executeUpdateShadowTask(String newState) {
        UpdateShadowTask updateShadowTask = new UpdateShadowTask(newState, this);
        updateShadowTask.execute();
    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof SmartHomeControl) {
            SmartHomeControl smartHomeControl = (SmartHomeControl) object;
            //
        }
        if (object instanceof SmartHomeStatus) {
            SetObject(object);
            final SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;

            System.out.println(smartHomeStatus.getState().getReported().getControls().getAlaram());

            mAlaramTxt = (TextView) findViewById(R.id.AlaramTxt);
            mAlaramTxt.setText(smartHomeStatus.getState().getReported().getControls().getAlaram());

            BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
            if (fragment != null && fragment.isVisible()) {
                fragment.updateFragment(object);
            }
            // System.out.println(smartHomeStatus.state.reported.Doors.getFrontDoor());

            System.out.println("i am in onsuccess");
        }
    }

    Object mObject;

    public void SetObject(Object object) {
        mObject = object;
    }

    public Object GetObject() {
        return mObject;
    }

    @Override
    public void onFailure() {
        System.out.println("i am in failure");
    }


    @Override
    public void onStatusUpdate(String status) {
        if (status.equals(Constants.CONNECTED)) {
            subscribeToTopic = new SubscribeToTopic(this, this);
            ShadowApplication.getInstance().getIotManager().subscribeToTopic(
                    topic, AWSIotMqttQos.QOS0, subscribeToTopic);
            Log.d(LOG_TAG, "Subscribed to Topic");
        }
    }

    public void connectIotManager() {
        ShadowApplication.getInstance().getIotManager().connect(clientKeyStore,
                new SecureAWSIotMqttClientManager(this));
    }
    final static String GROUP_KEY_GUEST = "group_key_guest";


    public void Notify(String notificationTitle, String notificationMessage){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent intent = new Intent(this, SecureMainActivity.class);
       PendingIntent pIntent =
               PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification noti = new Notification.Builder(this)
                               .setContentTitle("Smart Home Alert message " )
                                .setContentText(notificationTitle)
                                .setSmallIcon(R.drawable.alarm)
                               //.setContentIntent(pIntent)
                                .setFullScreenIntent(pIntent, false)
                               .setOnlyAlertOnce(true)
                                .setVisibility(1)
                                .setSound(alarmSound)
                                .setGroup(GROUP_KEY_GUEST)
                                .setAutoCancel(true)


                                .build();


                // Notification bui  ld

                       // NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(NOTIFICATION_SERVICE);
                        noti.flags |= Notification.FLAG_AUTO_CANCEL;

                        manager.notify((int) System.currentTimeMillis(), noti);
                         Log.d(LOG_TAG, "Notifying Message");

    }






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        //creating fragment object
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.Signout) {
//            Intent intent = new Intent(this, InfoActivity.class);
//            intent.putExtra(Extra,id);
//            startActivity(intent);


        } else if (id == R.id.history) {
               fragment = new HistoryFragment();

        }
        //replacing the fragment
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}


