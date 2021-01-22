package com.tiseno.poplook.functions;

import android.util.Log;

import com.ipay.IPayIHResultDelegate;
import com.tiseno.poplook.IPay88PaymentActivity;

import java.io.Serializable;
import java.sql.Ref;

/**
 * Created by billygoh on 9/25/15.
 */
public class IPay88ResultDelegate implements IPayIHResultDelegate, Serializable {

    private static final long serialVersionUID = 10001L;
    private static final String TAG = IPay88ResultDelegate.class.getSimpleName();

    @Override
    public void onPaymentSucceeded(String TransId, String RefNo, String Amount,
                                   String Remark, String AuthCode) {

        IPay88PaymentActivity.resultTitle	= "SUCCESS";
        IPay88PaymentActivity.resultInfo 	= "You have successfully completed your transaction.";

        IPay88PaymentActivity.transactionID = TransId;
        IPay88PaymentActivity.referenceNo = RefNo;
        IPay88PaymentActivity.totalAmount = Amount;
        IPay88PaymentActivity.transactionStatus = "1";
        Log.d(TAG, "successss: " + RefNo);

    }

    @Override
    public void onPaymentFailed(String TransId, String RefNo, String Amount,
                                String Remark, String ErrDesc) {

        IPay88PaymentActivity.resultTitle	= "FAILURE";
        IPay88PaymentActivity.resultInfo 	= ErrDesc;

        IPay88PaymentActivity.transactionID = TransId;
        IPay88PaymentActivity.referenceNo = RefNo;
        IPay88PaymentActivity.totalAmount = Amount;
        IPay88PaymentActivity.transactionStatus = "0";
        Log.d(TAG, "failll: " + RefNo);


    }

    @Override
    public void onPaymentCanceled(String TransId, String RefNo, String Amount,
                                  String Remark, String ErrDesc) {

        IPay88PaymentActivity.resultTitle	= "CANCELED";
        IPay88PaymentActivity.resultInfo 	= "The transaction has been cancelled.";

        IPay88PaymentActivity.transactionID = TransId;
        IPay88PaymentActivity.referenceNo = RefNo;
        IPay88PaymentActivity.totalAmount = Amount;
        IPay88PaymentActivity.transactionStatus = "2";
        Log.d(TAG, "cancelll: " + RefNo);

    }

    @Override
    public void onRequeryResult(String MerchantCode, String RefNo,
                                String Amount, String Result) {
        IPay88PaymentActivity.resultTitle	= "Requery Result";
        IPay88PaymentActivity.resultInfo 	= "";


    }
    @Override
    public void onConnectionError(String merchantCode, String merchantKey,
                                  String RefNo, String Amount, String Remark, String lang, String country) {
        IPay88PaymentActivity.resultTitle = "CONNECTION ERROR";
        IPay88PaymentActivity.resultInfo = "The transaction has been cancelled.";

    }

}