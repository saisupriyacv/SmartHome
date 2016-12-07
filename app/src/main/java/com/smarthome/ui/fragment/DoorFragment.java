package com.smarthome.ui.fragment;

import android.app.Activity;
import android.media.Image;
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

import static com.smarthome.R.drawable.dooropen;

/**
 * Created by Naveen on 12/3/16.
 */

public class DoorFragment extends BaseFragment {
    ImageView Frontimage,Backimage;
    Object obj;

    //AlaramFragmentDialogueListner mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View view = inflater.inflate(R.layout.doorfragment, container, false);
        Frontimage = (ImageView) view.findViewById(R.id.FrontDoor1) ;
        Backimage = (ImageView) view.findViewById(R.id.BackDoor1) ;


        //Please add this in your Fragment in order to access doors
        Activity activity = getActivity();
        if(activity instanceof SecureMainActivity){
            SecureMainActivity myactivity = (SecureMainActivity) activity;
            obj = myactivity.GetObject();
        }

        if((((SmartHomeStatus) obj).getState().getReported().getDoors().getFrontDoor()).equalsIgnoreCase("open"))
        {
            Frontimage.setImageResource(R.drawable.dooropen);
        }
        else if ((((SmartHomeStatus) obj).getState().getReported().getDoors().getFrontDoor()).equalsIgnoreCase("closed")){
            Frontimage.setImageResource(R.drawable.doorclose);

        }
        if((((SmartHomeStatus) obj).getState().getReported().getDoors().getBackDoor()).equalsIgnoreCase("open"))
        {
            Backimage.setImageResource(R.drawable.dooropen);
        }
        else if ((((SmartHomeStatus) obj).getState().getReported().getDoors().getBackDoor()).equalsIgnoreCase("closed")){
            Backimage.setImageResource(R.drawable.doorclose);

        }


       // text1.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getFrontDoor());
       // text2.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getBackDoor());
       // text2.setText(((SmartHomeStatus) obj).getState().getReported().getDoors().getSideDoor());

        return view;
    }

    @Override
    public void updateFragment(Object object) {

        if (object instanceof SmartHomeStatus) {
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;
            if((((SmartHomeStatus) obj).getState().getReported().getDoors().getFrontDoor()).equalsIgnoreCase("open"))
            {
                Frontimage.setImageResource(R.drawable.dooropen);
            }
            else if ((((SmartHomeStatus) obj).getState().getReported().getDoors().getFrontDoor()).equalsIgnoreCase("closed")){
                Frontimage.setImageResource(R.drawable.doorclose);

            }
            if((((SmartHomeStatus) obj).getState().getReported().getDoors().getBackDoor()).equalsIgnoreCase("open"))
            {
                Backimage.setImageResource(R.drawable.dooropen);
            }
            else if ((((SmartHomeStatus) obj).getState().getReported().getDoors().getBackDoor()).equalsIgnoreCase("closed")){
                Backimage.setImageResource(R.drawable.doorclose);

            }
        }
    }
}