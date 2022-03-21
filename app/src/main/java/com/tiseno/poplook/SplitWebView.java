package com.tiseno.poplook;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import org.apache.commons.codec.binary.Hex;

import java.net.URI;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SplitWebView extends Fragment {

    String orderID, price, currency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.split_view,container,false);

        Bundle bundle = this.getArguments();
        orderID = bundle.getString("orderID", "");
        price = bundle.getString("priceTotal", "");
        currency = bundle.getString("currency", "");

        String apiKey = "hg55f5fm5v7jbece8ld75sx2ssiv7x7c";
        String secretKey = "m6vb5eze8l8lkgizsa32533xcpv0k8p8";
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
        webView.loadUrl("https://sandbox.paywithsplit.co/process/welcome?currency=MYR&price="+price+"&orderId="+orderID+"&apiKey="+apiKey+"&signature="+signature+"");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                String urld = url;

                System.out.println("split finish load = " + urld);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(request.getUrl().toString().contains("process/finish")){
                    return false;
                }
                else {
                    return true;
                }
            }


        });

        return view;    }

    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }
}