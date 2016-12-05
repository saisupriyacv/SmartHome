package com.smarthome.network;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import com.smarthome.data.Authenticate;
import com.smarthome.ui.listener.NetworkListener;
import com.smarthome.network.model.SmartHomeControl;
import com.smarthome.network.model.SmartHomeStatus;
import com.google.gson.Gson;

/**
 * Created by saisu on 11/30/2016.
 */

public class GetShadowTask extends AsyncTask<Void, Void, AsyncTaskResult<String>> {

    private final String thingName;
    private NetworkListener mNetworkListener;
    private static final String LOG_TAG = GetShadowTask.class.getSimpleName();

    public GetShadowTask(String name, NetworkListener networkListener) {
        thingName = name;
        mNetworkListener = networkListener;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... voids) {
        try {
            GetThingShadowRequest getThingShadowRequest = new GetThingShadowRequest()
                    .withThingName("SmartHome");
            GetThingShadowResult result = Authenticate.getInstance().getIotDataClient().getThingShadow(getThingShadowRequest);
            byte[] bytes = new byte[result.getPayload().remaining()];
            result.getPayload().get(bytes);
            String resultString = new String(bytes);
            Log.d(LOG_TAG,"Get thing shadow request");
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
                Log.d(LOG_TAG,"Smart home control status call back");
                smarthomeControlUpdated(result.getResult());
            } else if ("SmartHome".equals(thingName)) {
                smarthomeStatusUpdated(result.getResult());
                Log.d(LOG_TAG,"Smart home control status updated call back");
            }

        } else {
            Log.e(GetShadowTask.class.getCanonicalName(), "getShadowTask", result.getError());
        }
    }

    private void smarthomeControlUpdated(String smarthomeControlState) {
        Gson gson = new Gson();
        SmartHomeControl tc = gson.fromJson(smarthomeControlState, SmartHomeControl.class);
        if (mNetworkListener != null) {
            mNetworkListener.onSuccess(tc);
            Log.d(LOG_TAG,"Call back from mNetworkListner to main activity on success");

        }
        else{
            mNetworkListener.onFailure();
            Log.d(LOG_TAG,"Call back from mNetworkListner to main activity on failure");

        }

    }

    private void smarthomeStatusUpdated(String smarthomeStatusState) {
        Gson gson = new Gson();
        SmartHomeStatus ts = gson.fromJson(smarthomeStatusState, SmartHomeStatus.class);
        if (mNetworkListener != null) {
            mNetworkListener.onSuccess(ts);
        }
        else{
            mNetworkListener.onFailure();
            Log.d(LOG_TAG,"Call back from mNetworkListner to main activity on failure");

        }

    }

}