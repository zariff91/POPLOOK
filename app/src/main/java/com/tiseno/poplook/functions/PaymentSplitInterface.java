package com.tiseno.poplook.functions;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.tiseno.poplook.SplitWebView;

public class PaymentSplitInterface {

    Context mContext;

    public PaymentSplitInterface(Context c) {
        mContext = c;
    }


    @JavascriptInterface
    public void status(String data){

        System.out.println("get data 1 = " +data);

    }


}
