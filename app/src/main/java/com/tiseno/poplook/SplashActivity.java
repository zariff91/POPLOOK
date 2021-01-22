package com.tiseno.poplook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.view.WindowManager;

import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity implements AsyncTaskCompleteListener<JSONObject> {
    boolean notFirstTime = false;
    String apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here

            Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.lightgrey));


            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

// set an exit transition
            window.setExitTransition(new Explode());
            window.setEnterTransition(new Explode());

        }

//        setContentView(R.layout.activity_splash);
        apikey = "PL:@KrAk!fA9RpGDcnIfDKzljGkEqW48yU4M6Y2GckgawSVbEg62FHKHBU7awnidFZ4wxVxUdcTAvkxT1GrlVhuZ1dKlqzl9zlsedD66G";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("apikey", apikey);
        editor.apply();

        getAppVersion();


    }

    public void getAppVersion() {
        String action = "Devices/versionInfo?apikey=" + apikey;
        WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(this, this);
        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {

        if(result!=null) {

            try {
                if (result.getBoolean("status"))
                {
                    if (result.getString("action").equals("Devices_versionInfo")) {

                        JSONObject jsonObject = null;
                        jsonObject = result.getJSONObject("data");

                        String versionNumberAndroid = jsonObject.getString("android_version");
                        PackageInfo pInfo = null;
                        try {
                            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        //get the app version Name for display
                        String version = pInfo.versionName;
                        //get the app version Code for checking
                        int versionCode = pInfo.versionCode;
                        System.out.println("VERSION FROM API " + versionNumberAndroid);
                        System.out.println("VERSION FROM APP " + version);

                        int compare = version.compareToIgnoreCase(versionNumberAndroid);

                        if (compare > 0 || compare == 0) {


                            System.out.println("no need update");

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

                            notFirstTime = pref.getBoolean("notFirstTime", false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!notFirstTime) {
                                        Intent intent = new Intent(SplashActivity.this, ChooseCountryActivity.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);

                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
//                    Intent intent1 = new Intent(SplashActivity.this, Main2Activity.class);
//                    startActivity(intent1);
//                    finish();
                                    }
                                }
                            }, 1000);
                        } else {

//                            if (!version.equalsIgnoreCase(versionNumberAndroid)) {


                                new android.app.AlertDialog.Builder(SplashActivity.this)
                                        .setTitle("Message")
                                        .setMessage("A new update is available, Please update app to continue browsing our products")
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                finish();
                                                System.exit(0);
                                            }
                                        })
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        }).show();
//                            } else {
//                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//
//                                notFirstTime = pref.getBoolean("notFirstTime", false);
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (!notFirstTime) {
//                                            Intent intent = new Intent(SplashActivity.this, ChooseCountryActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                            overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
//
//                                        } else {
//                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                            overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
////                    Intent intent1 = new Intent(SplashActivity.this, Main2Activity.class);
////                    startActivity(intent1);
////                    finish();
//                                        }
//                                    }
//                                }, 1000);
//
//                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
                        new AlertDialog.Builder(this)
                    .setTitle("Internet Connection Problem")
                    .setMessage("We are unable to connect to the server due to connection problem. Please check your connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    }).show();

        }


    }
}
