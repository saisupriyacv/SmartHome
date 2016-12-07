package com.smarthome.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.smarthome.R;
import com.smarthome.ui.fragment.HistoryFragment;
import com.smarthome.ui.fragment.ProfileFragment;

public class InfoActivity extends AppCompatActivity {
final String Extra = "com.smarthome.ui.activity.itemid";
    String Tag;
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//replacing the fragment
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        int id = getIntent().getIntExtra(Extra,0);
        if(id == 1)
        {
            ProfileFragment fragment =new ProfileFragment();

        } else if(id ==2 ) {

            HistoryFragment fragment = new HistoryFragment();
        }
        else if(id == 3){


        }

           //replacing the fragment
           if (fragment != null) {
                FragmentManager manager = getSupportFragmentManager();
               FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_container1, fragment);
               transaction.addToBackStack(null);
               transaction.commit();
            }

        }
    }

