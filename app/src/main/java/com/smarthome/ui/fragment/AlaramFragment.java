package com.smarthome.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smarthome.R;
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.ui.activity.SecureMainActivity;


/**
 * Created by saisu on 11/18/2016.
 */

public class AlaramFragment extends BaseFragment {

    TextView text;
    Object obj;

    //AlaramFragmentDialogueListner mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        Toast.makeText(getContext(), "Launch Fragment" , Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.alaramfragment_layout, container, false);
        text = (TextView) view.findViewById(R.id.doors);
        return view;

    }

    @Override
    public void updateFragment(Object object) {
        if (object instanceof SmartHomeStatus) {
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;
            text.setText(smartHomeStatus.getState().getReported().getDoors().getFrontDoor());
        }
    }
}











