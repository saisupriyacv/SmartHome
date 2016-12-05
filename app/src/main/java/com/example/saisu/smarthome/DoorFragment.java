package com.example.saisu.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        if(activity instanceof Main_Secure){
            Main_Secure myactivity = (Main_Secure) activity;
            obj = myactivity.GetObject();
        }


        SmartHomeStatus smartHomeStatus = (SmartHomeStatus) obj;
        text1.setText(((SmartHomeStatus) obj).state.reported.Doors.getFrontDoor());
        text2.setText(((SmartHomeStatus) obj).state.reported.Doors.getBackDoor());
        text2.setText(((SmartHomeStatus) obj).state.reported.Doors.getSideDoor());
        return view;

    }
}