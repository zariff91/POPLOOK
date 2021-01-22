package com.tiseno.poplook;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by billygoh on 10/2/15.
 */
public class WebAppInterface {
    Context mContext;
    Activity activity;

    /** Instantiate the interface and set the context */
    WebAppInterface(Activity a,Context c) {
        mContext = c;
        activity = a;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    public void getData(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void closeActivity() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        });

    }
}
