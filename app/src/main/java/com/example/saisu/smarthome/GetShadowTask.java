package com.example.saisu.smarthome;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import com.google.gson.Gson;

/**
 * Created by saisu on 11/30/2016.
 */

class GetShadowTask extends AsyncTask<Void, Void, AsyncTaskResult<String>> {

    private final String thingName;
    private NetworkListener mNetworkListener;
    String LOG_TAG = GetShadowTask.class.getSimpleName();

    public GetShadowTask(String name, NetworkListener networkListener) {
        thingName = name;
        mNetworkListener = networkListener;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... voids) {
        try {
            GetThingShadowRequest getThingShadowRequest = new GetThingShadowRequest()
                    .withThingName("SmartHome");
            GetThingShadowResult result = Authnticate.getInstance().getIotDataClient().getThingShadow(getThingShadowRequest);
            byte[] bytes = new byte[result.getPayload().remaining()];
            result.getPayload().get(bytes);
            String resultString = new String(bytes);


            return new AsyncTaskResult<String>(resultString);
        } catch (Exception e) {
            Log.e("E", "getShadowTask", e);
            return new AsyncTaskResult<String>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        if (result.getError() == null) {
            Log.i(GetShadowTask.class.getSimpleName(), result.getResult());
            if ("SmartHomeControl".equals(thingName)) {
                smarthomeControlUpdated(result.getResult());
            } else if ("SmartHome".equals(thingName)) {
                smarthomeStatusUpdated(result.getResult());
            }
            System.out.println(result.getResult());
        } else {
            Log.e(GetShadowTask.class.getCanonicalName(), "getShadowTask", result.getError());
        }
    }

    private void smarthomeControlUpdated(String smarthomeControlState) {
        Gson gson = new Gson();
        SmartHomeControl tc = gson.fromJson(smarthomeControlState, SmartHomeControl.class);
        if (mNetworkListener != null) {
            mNetworkListener.onSuccess(tc);
        }
        // mNetwo.onSuccess(tc)

        //// Log.i(LOG_TAG, String.format("setPoint: %d", tc.state.desired.setPoint));
        //Log.i(LOG_TAG, String.format("enabled: %b", tc.state.desired.enabled));

        /*NumberPicker np = (NumberPicker) findViewById(R.id.setpoint);
        np.setValue(tc.state.desired.setPoint);

        ToggleButton tb = (ToggleButton) findViewById(R.id.enableButton);
        tb.setChecked(tc.state.desired.enabled);*/
    }



    private void smarthomeStatusUpdated(String smarthomeStatusState) {
        Gson gson = new Gson();
        SmartHomeStatus ts = gson.fromJson(smarthomeStatusState, SmartHomeStatus.class);
        Log.i(LOG_TAG,  ts.state.reported.Doors.FrontDoor);
        Log.i(LOG_TAG,  ts.state.reported.Doors.BackDoor);
        Log.i(LOG_TAG,  ts.state.reported.Doors.SideDoor);
        Log.i(LOG_TAG,  ts.state.reported.Controls.Alaram);
        Log.i(LOG_TAG,  ts.state.reported.Controls.Switch);

    }

}