package com.example.saisu.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * Created by saisu on 11/18/2016.
 */

public class AlaramFragment extends Fragment {

   TextView text;
    Object obj;

    //AlaramFragmentDialogueListner mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View view = inflater.inflate(R.layout.alaramfragment_layout, container, false);
        text = (TextView) view.findViewById(R.id.doors);
        //Please add this in your Fragment in order to access doors
        Activity activity = getActivity();
        if(activity instanceof Main_Secure){
            Main_Secure myactivity = (Main_Secure) activity;
            obj = myactivity.GetObject();
        }


        SmartHomeStatus smartHomeStatus = (SmartHomeStatus) obj;
        text.setText(((SmartHomeStatus) obj).state.reported.Doors.getFrontDoor());
        return view;

    }


}











