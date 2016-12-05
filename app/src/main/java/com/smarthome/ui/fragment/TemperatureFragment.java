package com.smarthome.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
 * Created by saisu on 12/4/2016.
 */

public class TemperatureFragment extends BaseFragment {
    Object obj;
    TextView mtextview;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temperature, container, false);
        mtextview = (TextView) view.findViewById(R.id.tmptxt);
        Activity activity = getActivity();
        if(activity instanceof SecureMainActivity){
            SecureMainActivity myactivity = (SecureMainActivity) activity;
            obj = myactivity.GetObject();
        }

        mtextview.setText((((SmartHomeStatus) obj).getState().getReported().getTemperature().getTemperature()));
        Log.d( "" , "temperature" + (((SmartHomeStatus) obj).getState().getReported().getTemperature().getTemperature()));
        return view;
    }

    @Override
    public void updateFragment(Object object) {

        if (object instanceof SmartHomeStatus) {
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;
            mtextview.setText(smartHomeStatus.getState().getReported().getTemperature().getTemperature());

        }

    }
}
