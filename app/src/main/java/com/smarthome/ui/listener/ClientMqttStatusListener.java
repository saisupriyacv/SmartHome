package com.smarthome.ui.listener;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;

/**
 * Created by saisu on 12/3/2016.
 */

public interface ClientMqttStatusListener {
    void onStatusUpdate(String status);
}
