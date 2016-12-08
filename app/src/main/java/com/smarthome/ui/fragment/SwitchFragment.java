package com.smarthome.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.smarthome.R;
import com.smarthome.network.model.SmartHomeStatus;
import com.smarthome.ui.activity.SecureMainActivity;

/**
 * Created by Naveen on 12/3/16.
 */

public class SwitchFragment extends BaseFragment {


    ImageView imgview;
    Switch sButton;
    Object obj;

   @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.switch_fragment, container, false);

       sButton = (Switch) view.findViewById(R.id.switchbtn);
       imgview =(ImageView) view.findViewById(R.id.bulbimg);


        //Please add this in your Fragment in order to access doors
        Activity activity = getActivity();
        if(activity instanceof SecureMainActivity){
            SecureMainActivity myactivity = (SecureMainActivity) activity;
            obj = myactivity.GetObject();
        }

        sButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){

                    String newState =  String.format("{\"state\":{\"desired\":{\"Controls\":{\"Alaram\":\"nop\",\"Switch\":\"on\"}}}}");
                    imgview.setImageResource(R.drawable.bulbon);
                    ((SecureMainActivity)getActivity()).executeUpdateShadowTask(newState);
                   // getActivity().
                }else{

                    String newState =  String.format("{\"state\":{\"desired\":{\"Controls\":{\"Alaram\":\"nop\",\"Switch\":\"off\"}}}}");
                    imgview.setImageResource(R.drawable.bulboff);
                    ((SecureMainActivity)getActivity()).executeUpdateShadowTask(newState);

                }



            }
        });
        return view;
    }

    @Override
    public void updateFragment(Object object) {

       if (object instanceof SmartHomeStatus) {
            SmartHomeStatus smartHomeStatus = (SmartHomeStatus) object;
            if((smartHomeStatus.getState().getReported().getControls().getSwitch()).equalsIgnoreCase("on"))
           {
               imgview.setImageResource(R.drawable.bulbon);

           }
           else
           {
               imgview.setImageResource(R.drawable.bulboff);

           }
        }


    }
}