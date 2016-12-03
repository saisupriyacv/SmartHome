package com.example.saisu.smarthome;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import com.amazonaws.services.iotdata.model.UpdateThingShadowRequest;
import com.amazonaws.services.iotdata.model.UpdateThingShadowResult;
import com.google.gson.Gson;

import java.nio.ByteBuffer;

/**
 * Created by saisu on 12/3/2016.
 */
class UpdateShadowTask extends AsyncTask<Void, Void, AsyncTaskResult<String>> {

    private String thingName;
    private String updateState;
    private NetworkListener mNetworkListener;

    public void setThingName(String name) {
        thingName = name;
    }

    public void setState(String state) {
        updateState = state;
    }


    public UpdateShadowTask( NetworkListener networkListener) {

        mNetworkListener = networkListener;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... voids) {
        try {
            UpdateThingShadowRequest request = new UpdateThingShadowRequest();
            request.setThingName("SmartHome");

            ByteBuffer payloadBuffer = ByteBuffer.wrap(updateState.getBytes());
            request.setPayload(payloadBuffer);

            UpdateThingShadowResult result = Authnticate.getInstance().getIotDataClient().updateThingShadow(request);

            byte[] bytes = new byte[result.getPayload().remaining()];
            result.getPayload().get(bytes);
            String resultString = new String(bytes);
            Log.d(UpdateShadowTask.class.getCanonicalName(), "result of update " + resultString);
            smarthomeUpdated(resultString);
            return new AsyncTaskResult<String>(resultString);
        } catch (Exception e) {
            Log.e(UpdateShadowTask.class.getCanonicalName(), "updateShadowTask", e);
            return new AsyncTaskResult<String>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        if (result.getError() == null) {
            Log.i(UpdateShadowTask.class.getCanonicalName(), result.getResult());
        } else {
            Log.e(UpdateShadowTask.class.getCanonicalName(), "Error in Update Shadow",
                    result.getError());
        }
    }


    private void smarthomeUpdated(String smarthomeStatusState) {
        Gson gson = new Gson();
        SmartHomeControl ts = gson.fromJson(smarthomeStatusState, SmartHomeControl.class);
        if (mNetworkListener != null) {
            mNetworkListener.onSuccess(ts);
        }


    }
}

