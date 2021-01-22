package com.tiseno.poplook.functions;

import android.util.Log;

import java.io.Serializable;

//import my.com.ipay88.androidsdkmerchanthosted.pay.IPayMHResultDelegate;
//import my.com.ipay88.androidsdkmerchanthosted.pay.mt.MCTokenizationRet;
//import com.ipay.obj.MCTokenizationRet;

import com.tiseno.poplook.IPay88PaymentActivity;


/**
 * Created by pnduy on 3/9/2016.
 */
public class ResultDelegate implements Serializable {
    private static final long serialVersionUID = 10001L;

//    @Override
//    public void onPaymentSucceeded(String TransId, String RefNo,
//                                   String Amount, String Remark, String AuthCode,
//                                   MCTokenizationRet mcTokenizationRet) {
//
//        IPay88PaymentActivity.resultTitle = "SUCCESS";
//        IPay88PaymentActivity.resultInfo = "You have successfully completed your transaction.";
//
//        String extra = "";
//        extra = extra + "TransId\t= " + TransId + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Remark\t= " + Remark + "\n";
//        extra = extra + "AuthCode\t= " + AuthCode;
//        IPay88PaymentActivity.resultExtra = extra;
//        IPay88PaymentActivity.mcTokenizationRet = mcTokenizationRet;
//        Log.d("HuyLV", "TransId: " + TransId);
//    }
//
//    @Override
//    public void onPaymentFailed(String TransId, String RefNo, String Amount,
//                                String Remark, String ErrDesc,
//                                MCTokenizationRet mcTokenizationRet) {
//
//        IPay88PaymentActivity.resultTitle = "FAILURE";
//        IPay88PaymentActivity.resultInfo = ErrDesc;
//
//        String extra = "";
//        extra = extra + "TransId\t= " + TransId + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Remark\t= " + Remark + "\n";
//        extra = extra + "ErrDesc\t= " + ErrDesc;
//        IPay88PaymentActivity.resultExtra = extra;
//        IPay88PaymentActivity.mcTokenizationRet = mcTokenizationRet;
//
//    }
//
//    @Override
//    public void onPaymentCanceled(String TransId, String RefNo, String Amount,
//                                  String Remark, String ErrDesc,
//                                  MCTokenizationRet mcTokenizationRet) {
//
//        IPay88PaymentActivity.resultTitle = "CANCELED";
//        IPay88PaymentActivity.resultInfo = "The transaction has been cancelled.";
//
//        String extra = "";
//        extra = extra + "TransId\t= " + TransId + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Remark\t= " + Remark + "\n";
//        extra = extra + "ErrDesc\t= " + ErrDesc;
//        IPay88PaymentActivity.resultExtra = extra;
//        IPay88PaymentActivity.mcTokenizationRet = mcTokenizationRet;
//
//    }
//
//    @Override
//    public void onRequeryResult(String MerchantCode, String RefNo,
//                                String Amount, String Result) {
//        IPay88PaymentActivity.resultTitle = "Requery Result";
//        IPay88PaymentActivity.resultInfo = "";
//        String extra = "";
//
//        extra = extra + "MerchantCode\t= " + MerchantCode + "\n";
//        extra = extra + "RefNo\t\t= " + RefNo + "\n";
//        extra = extra + "Amount\t= " + Amount + "\n";
//        extra = extra + "Result\t= " + Result;
//        IPay88PaymentActivity.resultExtra = extra;
//
//    }
}

