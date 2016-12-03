package com.example.saisu.smarthome;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iotdata.AWSIotDataClient;

/**
 * Created by saisu on 11/30/2016.
 */
public class Authnticate {

    // Customer specific IoT endpoint
    // AWS Iot CLI describe-endpoint call returns: XXXXXXXXXX.iot.<region>.amazonaws.com
    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a2kc9la4cp40qj.iot.us-east-1.amazonaws.com";
    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // AWS IoT permissions.
    private static final String COGNITO_POOL_ID = "us-east-1:63486043-e999-4cc0-8d1c-c6b6423e1f3a";
    // Region of AWS IoT
    private static final Regions MY_REGION = Regions.US_EAST_1;



    CognitoCachingCredentialsProvider credentialsProvider;

    private AWSIotDataClient iotDataClient;

    private static Authnticate ourInstance;

    public static Authnticate getInstance() {
        if (ourInstance == null) {
            ourInstance = new Authnticate();
        }
        return ourInstance;
    }

    public void createConnection(Context context) {
        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                COGNITO_POOL_ID, // Identity Pool ID
                MY_REGION // Region
        );

      /*  if (TextUtils.isEmpty(credentialsProvider.getIdentityId())) {
            // TODO: Callback for not established connection
            Log.e("","Cnnection not established.");
            return;
        }*/

        iotDataClient = new AWSIotDataClient(credentialsProvider);
        String iotDataEndpoint = CUSTOMER_SPECIFIC_ENDPOINT;
        iotDataClient.setEndpoint(iotDataEndpoint);
        setIotDataClient(iotDataClient);

        // crete connection
    }



    public AWSIotDataClient getIotDataClient() {
        return iotDataClient;
    }

    public void setIotDataClient(AWSIotDataClient iotDataClient) {
        this.iotDataClient = iotDataClient;
    }

    public CognitoCachingCredentialsProvider getCredentialProvider() {
        return credentialsProvider;
    }
}
