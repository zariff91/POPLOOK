package com.tiseno.poplook;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderCallback;
import com.useinsider.insider.InsiderCallbackType;

import org.json.JSONException;
import org.json.JSONObject;
//import com.useinsider.insider.config.Geofence;
//import com.useinsider.insider.config.Push;

/**
 * Created by rahnrazamai on 07/09/16.
 */
public class
POPLOOK extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);

        Insider.Instance.init(this,"poplook");
        Insider.Instance.setSplashActivity(SplashActivity.class);

        Insider.Instance.registerInsiderCallback((data, insiderCallbackType) -> {

            switch (insiderCallbackType) {
                case NOTIFICATION_OPEN:
                    Log.d("[INSIDER]", "[NOTIFICATION_OPEN]: " + data);
                    break;
                case INAPP_BUTTON_CLICK:
                    Log.d("[INSIDER]", "[INAPP_BUTTON_CLICK]: " + data);

                    String cartResultJObjString=data.toString();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("comeFromNotification", "1");
                    System.out.println("ppppppppp = " + cartResultJObjString);
                    editor.putString("signup_page_open", cartResultJObjString);
                    editor.apply();

                    break;
                case TEMP_STORE_PURCHASE:
                    Log.d("[INSIDER]", "[TEMP_STORE_PURCHASE]: " + data);
                    break;
                case TEMP_STORE_ADDED_TO_CART:
                    Log.d("[INSIDER]", "[TEMP_STORE_ADDED_TO_CART]: " + data);
                    break;
                case TEMP_STORE_CUSTOM_ACTION:
                    Log.d("[INSIDER]", "[TEMP_STORE_CUSTOM_ACTION]: " + data);
                    break;
            }
        });

//        Insider.Instance.init(this,
//                /* Insider Partner Name (Should be lowercase) */
//                "poplook",
//                /* Sender ID */
//                "549922517099",
//                /* Your Landing Class Path */
//                MainActivity.class,
//                /* Push Collapsing Option */
//                /* WILL_NOT_COLLAPSE, WILL_COLLAPSE */
//                Push.WILL_COLLAPSE,
//         /* Geofence Options:
//            NOT_ENABLED, EVERY_10_SECONDS,
//            EVERY_30_SECONDS, EVERY_60_SECONDS,EVERY_90_SECONDS,
//            EVERY_120_SECONDS, EVERY_180_SECONDS,
//            EVERY_240_SECONDS ,EVERY_300_SECONDS
//           We recommend choosing 60 seconds.
//         */
//                Geofence.EVERY_60_SECONDS
//        );

    }
}
