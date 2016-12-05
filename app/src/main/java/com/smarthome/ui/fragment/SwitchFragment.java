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
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.ui.activity.SecureMainActivity;

/**
 * Created by Naveen on 12/3/16.
 */

public class SwitchFragment extends BaseFragment {


    public ToggleButton toggleButton;
    public TextView stateOnOff;
    Object obj;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.switch_fragment, container, false);
        toggleButton = (ToggleButton) view.findViewById(R.id.toggle);
        stateOnOff=(TextView) view.findViewById(R.id.tvstate);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    String newState = String.format("{\"state\":{\"desired\":{\"Controls\":{\"Switch\":\"%s\"}}}}", "on");
                    ((SecureMainActivity)getActivity()).executeUpdateShadowTask(newState);
                   // getActivity().
                }else{

                    String newState = String.format("{\"state\":{\"desired\":{\"Controls\":{\"Switch\":\"%s\"}}}}", "off");
                    ((SecureMainActivity)getActivity()).executeUpdateShadowTask(newState);

                }

                stateOnOff.setText(((SmartHomeStatus) obj).getState().getReported().getControls().getSwitch());

            }
        });
        return view;
    }

    @Override
    public void updateFragment(Object object) {

        if (object instanceof SmartHomeStatus) {
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;
            stateOnOff.setText(smartHomeStatus.getState().getReported().getControls().getSwitch());
        }


    }
}