package com.tiseno.poplook;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.facebook.internal.Utility;
import com.tiseno.poplook.functions.PaymentSplitInterface;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.net.URI;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SplitWebView extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    String callbackSuccessURL;
    Handler mHandler = new Handler();

    String apiKey,secretKey;
    String sign;

    String orderID, price, currency;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("JavascriptInterface")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.split_view,container,false);

        Bundle bundle = this.getArguments();
        orderID = bundle.getString("orderID", "");
        price = bundle.getString("priceTotal", "");
        currency = bundle.getString("currency", "");

        apiKey = "hg55f5fm5v7jbece8ld75sx2ssiv7x7c";
        secretKey = "m6vb5eze8l8lkgizsa32533xcpv0k8p8";
        String payload = "apiKey"+apiKey+"currency"+currency+"orderId"+orderID+"price"+price+"";
        String signature = null;

        try {
            signature = encode(secretKey,payload);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebView webView = view.findViewById(R.id.webView_split);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        sign = signature;
        webView.loadUrl("https://sandbox.paywithsplit.co/process/welcome?currency=MYR&price="+price+"&orderId="+orderID+"&apiKey="+apiKey+"&signature="+signature+"");

//        webView.postUrl("https://sandbox.paywithsplit.co/process/welcome?price="+price+"&orderId="+orderID+"&apiKey"+apiKey+"&signature="+signature+"", EncodingUtils.getBytes(url,"base64"));
//        webView.postUrl();
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                String urld = url;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }


        });

        return view;

    }

    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    public void refreshPaymentStatus(){
        String action="api/v1/webhooks/check-payment-status?apiKey="+apiKey+"&signature="+sign+"&orderId="+orderID;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {

    }
}
