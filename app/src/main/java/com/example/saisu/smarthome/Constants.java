package com.example.saisu.smarthome;

/**
 * Created by saisu on 12/3/2016.
 */

public class Constants {

    public static final String CONNECTED = "Connected";
    public static final String INVALID = "invalid";
    // Filename of KeyStore file on the filesystem
    public static final String KEYSTORE_NAME = "iot_keystore";
    // Password for the private key in the KeyStore
    public static final String KEYSTORE_PASSWORD = "password";
    // Certificate and key aliases in the KeyStore
    public static final String CERTIFICATE_ID = "default";
    public static final String KEY_STORE_PATH = ShadowApplication.getInstance().getFilesDir().getPath();
    public static final String AWS_IOT_POLICY_NAME = "SmartHome-Policy";
}
