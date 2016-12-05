package com.smarthome.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smarthome.R;
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.ui.activity.SecureMainActivity;

/**
 * Created by Naveen on 12/3/16.
 */

public class DoorFragment extends android.support.v4.app.Fragment {
    TextView text1,text2,text3;
    Object obj;

    //AlaramFragmentDialogueListner mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View view = inflater.inflate(R.layout.doorfragment, container, false);
        text1 = (TextView) view.findViewById(R.id.FrontDoor);
        text2 = (TextView) view.findViewById(R.id.BackDoor);
        text3 = (TextView) view.findViewById(R.id.SideDoor);
        //Please add this in your Fragment in order to access doors
        Activity activity = getActivity();
        if(activity instanceof SecureMainActivity){
            SecureMainActivity myactivity = (SecureMainActivity) activity;
            obj = myactivity.GetObject();
        }


        SmartHomeStatus smartHomeStatus = (SmartHomeStatus) obj;
        text1.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getFrontDoor());
        text2.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getBackDoor());
        text2.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getSideDoor());
        return view;
    }
}