package com.smarthome.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.smarthome.R;

public class InfoActivity extends AppCompatActivity {
final String Extra = "com.smarthome.ui.activity.itemid";
    String Tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        int id = getIntent().getIntExtra(Extra,0);
        Log.d(Tag,"in activity"+ id);
    }
}
