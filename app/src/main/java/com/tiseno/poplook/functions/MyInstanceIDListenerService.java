//package com.tiseno.poplook.functions;
//
//import android.app.Service;
//import android.content.Intent;
//import android.util.Log;
//
import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//import com.useinsider.insider.Insider;
//
///**
// * Created by billygoh on 10/1/15.
// */
//public class MyInstanceIDListenerService extends Service {
//
//    private static final String TAG = "MyInstanceIDLS";
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is also called
//     * when the InstanceID token is initially generated, so this is where
//     * you retrieve the token.
//     */
//    // [START refresh_token]
//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        super.onTokenRefresh();
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        Insider.Instance.refreshDeviceToken();
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
////        sendRegistrationToServer(refreshedToken);
//    }
//    // [END refresh_token]
//}
