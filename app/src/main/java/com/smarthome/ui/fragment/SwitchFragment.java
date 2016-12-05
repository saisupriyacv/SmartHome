package com.smarthome.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.smarthome.R;

/**
 * Created by Naveen on 12/3/16.
 */

public class SwitchFragment extends android.support.v4.app.Fragment {

    public ToggleButton toggleButton;
    public TextView stateOnOff;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.switch_fragment, container, false);
        toggleButton = (ToggleButton) view.findViewById(R.id.toggle);
        stateOnOff=(TextView) view.findViewById(R.id.tvstate);
        stateOnOff.setText("Light Switch");
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    stateOnOff.setText("Switch is On");
                }else{
                    stateOnOff.setText("Switch is Off");
                }
            }
        });
        return view;
    }
}