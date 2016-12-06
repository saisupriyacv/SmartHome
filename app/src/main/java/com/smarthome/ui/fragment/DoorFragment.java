package com.smarthome.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarthome.R;
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.ui.activity.SecureMainActivity;

/**
 * Created by Naveen on 12/3/16.
 */

public class DoorFragment extends BaseFragment {
    TextView text1,text2,text3;
    Object obj;

    //AlaramFragmentDialogueListner mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View view = inflater.inflate(R.layout.doorfragment, container, false);
        text1 = (TextView) view.findViewById(R.id.FrontDoor);
        ImageView img = (ImageView) view.findViewById(R.id.doorimage);
        text2 = (TextView) view.findViewById(R.id.BackDoor);
        text3 = (TextView) view.findViewById(R.id.SideDoor);
        //Please add this in your Fragment in order to access doors
        Activity activity = getActivity();
        if(activity instanceof SecureMainActivity){
            SecureMainActivity myactivity = (SecureMainActivity) activity;
            obj = myactivity.GetObject();
        }
        text1.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getFrontDoor());
        text2.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getBackDoor());
        text2.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getSideDoor());

        return view;
    }

    @Override
    public void updateFragment(Object object) {

        if (object instanceof SmartHomeStatus) {
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;
            text1.setText(smartHomeStatus.getState().getReported().getDoors().getFrontDoor());
            text2.setText(smartHomeStatus.getState().getReported().getDoors().getBackDoor());
            text2.setText(smartHomeStatus.getState().getReported().getDoors().getSideDoor());
        }
    }
}