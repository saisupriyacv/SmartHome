package com.smarthome.network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttLastWillAndTestament;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.smarthome.data.Authenticate;
import com.smarthome.ui.SecureAWSIotMqttClientManager;
import com.smarthome.ui.activity.SecureMainActivity;
import com.smarthome.ui.listener.ClientMqttStatusListener;
import com.smarthome.Constants;
import com.smarthome.ShadowApplication;

import java.security.KeyStore;

/**
 * Created by saisu on 12/3/2016.
 */
public class VerifyCertificateTask extends AsyncTask<Void, Void, KeyStore> {

    private Activity mActivity;

    private static final String TAG = VerifyCertificateTask.class.getSimpleName();

    public VerifyCertificateTask(Activity activity) {
        AWSIotMqttLastWillAndTestament lwt = new AWSIotMqttLastWillAndTestament("my/lwt/topic",
                "Android client lost connection", AWSIotMqttQos.QOS0);
        ShadowApplication.getInstance().getIotManager().setMqttLastWillAndTestament(lwt);
        mActivity = activity;
    }

    @Override
    protected KeyStore doInBackground(Void... params) {
        AWSIotClient mIotAndroidClient = new AWSIotClient(Authenticate.getInstance().getCredentialProvider());
            // Create a new private key and certificate. This call
            // creates both on the server and returns them to the
            // device.
            CreateKeysAndCertificateRequest createKeysAndCertificateRequest =
                    new CreateKeysAndCertificateRequest();
            createKeysAndCertificateRequest.setSetAsActive(true);
            final CreateKeysAndCertificateResult createKeysAndCertificateResult;
            createKeysAndCertificateResult =
                    mIotAndroidClient.createKeysAndCertificate(createKeysAndCertificateRequest);
            Log.i("",
                    "Cert ID: " +
                            createKeysAndCertificateResult.getCertificateId() +
                            " created.");

            // store in keystore for use in MQTT client
            // saved as alias "default" so a new certificate isn't
            // generated each run of this application
            AWSIotKeystoreHelper.saveCertificateAndPrivateKey(Constants.CERTIFICATE_ID,
                    createKeysAndCertificateResult.getCertificatePem(),
                    createKeysAndCertificateResult.getKeyPair().getPrivateKey(),
                    Constants.KEY_STORE_PATH, Constants.KEYSTORE_NAME, Constants.KEYSTORE_PASSWORD);

            // load keystore from file into memory to pass on
            // connection
            KeyStore clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(Constants.CERTIFICATE_ID,
                    Constants.KEY_STORE_PATH, Constants.KEYSTORE_NAME, Constants.KEYSTORE_PASSWORD);

            // Attach a policy to the newly created certificate.
            // This flow assumes the policy was already created in
            // AWS IoT and we are now just attaching it to the
            // certificate.
            AttachPrincipalPolicyRequest policyAttachRequest =
                    new AttachPrincipalPolicyRequest();
            policyAttachRequest.setPolicyName(Constants.AWS_IOT_POLICY_NAME);
            policyAttachRequest.setPrincipal(createKeysAndCertificateResult
                    .getCertificateArn());
            mIotAndroidClient.attachPrincipalPolicy(policyAttachRequest);
         Log.d(TAG,"Key store Created");

        return clientKeyStore;
    }

    @Override
    protected void onPostExecute(KeyStore keyStore) {
        if (mActivity instanceof SecureMainActivity) {
            ((SecureMainActivity) mActivity).connectIotManager(keyStore);
        }
    }
}
