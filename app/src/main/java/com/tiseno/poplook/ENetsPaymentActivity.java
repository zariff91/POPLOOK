package com.tiseno.poplook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

//import com.eghl.EGHLAPI;
import com.eghl.sdk.*;
//import com.epay.eghl.EGHLInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tiseno.poplook.functions.EGHLStart;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.useinsider.insider.Insider;

import com.eghl.sdk.EGHL;
import com.eghl.sdk.ELogger;
import com.eghl.sdk.interfaces.CaptureCallback;
import com.eghl.sdk.interfaces.MasterpassCallback;
import com.eghl.sdk.interfaces.QueryCallback;
import com.eghl.sdk.params.CaptureParams;
import com.eghl.sdk.params.LightboxParams;
import com.eghl.sdk.params.MasterpassParams;
import com.eghl.sdk.params.Params;
import com.eghl.sdk.params.PaymentParams;
import com.eghl.sdk.params.QueryParams;
import com.eghl.sdk.response.CaptureResponse;
import com.eghl.sdk.response.QueryResponse;
import com.useinsider.insider.InsiderProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import java.util.HashMap;


import static com.tiseno.poplook.functions.EndlessScrollListener.TAG;

@SuppressLint("HandlerLeak")
public class ENetsPaymentActivity extends Activity implements AsyncTaskCompleteListener<JSONObject>
{
    public FirebaseAnalytics mFirebaseAnalytics;

    String ITEM_PRICE, ORDER_ID, ITEM_DETAIL, CART_ID,CREDITCARD_PAYMENT="0",PaymentMethod="DD",TransID="";
    String Message,TransactionID,TransactionStatus,ReturnURL="https://poplook.com/modules/sgcreditcard/callback_mobile.php?return_url=1",CallbackURL="http://poplook.com/modules/sgcreditcard/callback_mobile.php";
    String appVersion="1.0.0";
    boolean passStep5=false;
    private String transType = null, transPymt = null;
    private boolean saleFlag = false;
//new sdk params 2.8.1
    public static final String PROD_HOST = "https://securepay.e-ghl.com/IPG/Payment.aspx";
    public static final String TEST_HOST = "https://test2pay.ghl.com/IPGSG/Payment.aspx";
    private PaymentParams.Builder params;
    private EGHL eghl;
    private String paymentGateway = PROD_HOST;
    private Intent lastPaymentData;


    private EGHL EGHLCallback = null;
    @Override
    public void onCreate(Bundle  instance)
    {
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
        super.onCreate(instance);
        this.setContentView(R.layout.activity_enets_payment);
        mFirebaseAnalytics =FirebaseAnalytics.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ITEM_PRICE = extras.getString("ITEM_PRICE");
            ORDER_ID = extras.getString("ORDER_ID");
            ITEM_DETAIL = extras.getString("ITEM_DETAIL");
            CART_ID = extras.getString("CART_ID");
            CREDITCARD_PAYMENT = extras.getString("CREDITCARD_PAYMENT");
        }
        if(CREDITCARD_PAYMENT.equals("1")){
            CallbackURL="https://poplook.com/modules/sgcreditcard/callback_mobile.php";
            ReturnURL="https://poplook.com/modules/sgcreditcard/callback_mobile.php?return_url=1";
            PaymentMethod="CC";
        }else{
            CallbackURL="https://poplook.com/modules/enets/callback_mobile.php";
            ReturnURL="https://poplook.com/modules/enets/callback_mobile.php?return_url=1";
            PaymentMethod="DD";
        }
        eghl = EGHL.getInstance();
        ELogger.setLoggable(true);

        SharedPreferences pref = this.getSharedPreferences("MyPref", 0);
        String Name  = pref.getString("Name", "");
        String Email  = pref.getString("Email", "");
        String cnasit = eghl.generateId("CNASIT");
        params = new PaymentParams.Builder()
                .setMerchantReturnUrl(ReturnURL)
                .setMerchantCallbackUrl(CallbackURL)
                .setPaymentDesc(ITEM_DETAIL)
                .setCustPhone("+6062532750")
                .setLanguageCode("EN")
                .setPageTimeout("720")
                .setServiceId("POP")
                .setAmount(ITEM_PRICE)
                .setCustName(Name)
                .setCustEmail(Email)
                .setMerchantName("POPLOOK Sdn Bhd")
                .setCurrencyCode("SGD")
                .setTransactionType("SALE")
                .setPaymentMethod(PaymentMethod)
                .setPaymentGateway(paymentGateway)
                .setTriggerReturnURL(true)
                .setPassword("Po1kM5L7")
//                .setPassword("pop12345")
                .setPaymentId(CART_ID)
                .setOrderNumber(ORDER_ID);

        Bundle paymentParams = params.build();
        eghl.executePayment(paymentParams, ENetsPaymentActivity.this);

        saleFlag = true;
    }
    private void FinalizingPayment(String orderID, String TotalAmount, String TransactionID, String TransactionStatus)
    {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //get the app version Name for display
        appVersion = pInfo.versionName;

        TransID=TransactionID;
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        String PaymentType = pref.getString("PaymentType", "");
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Carts/OrderStep5?apikey="+apikey+"&device_type=android&id_order="+orderID+"&transaction_status="+TransactionStatus+"&payment_type="+PaymentType+"&total_paid="+TotalAmount+"&transaction_id="+TransactionID+"&app_version="+appVersion;

        WebServiceAccessGet callws = new WebServiceAccessGet(this, this);

        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{
                String action = result.getString("action");
                System.out.println("LALUSINISDKGHL");

                if(action.equals("Carts_OrderStep5"))
                {
                    System.out.println("LALUSINISDKGH1L");

                    if(result.getBoolean("status"))
                    {
                        System.out.println("LALUSINISDKGH2L");

//                        Toast toast = Toast.makeText(ENetsPaymentActivity.this,
//                                "Payment Success", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.BOTTOM, 0, 50);
//                        toast.show();
                        if(TransactionStatus.equals("1")) {

                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "SGD");
                            bundle.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(ITEM_PRICE));
                            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, TransID);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);

//                            Insider.Instance.trackPurchasedItems(this, "POPLOOKSALES", TransID, "POPLOOK SALES", "POPLOOK SALES", "POPLOOK SALES", Double.parseDouble(ITEM_PRICE), "SGD");

                        }
                        SharedPreferences pref = ENetsPaymentActivity.this.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("TransactionID", "");
                        editor.putString("TransactionStatus", "");
                        editor.putString("FromPaypalSuccess", "1");
                        editor.apply();
                        System.out.println("LALUSINISDKGH3L");
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        passStep5=true;
                        finish();
                        if(TransactionStatus.equals("1")) {

                            String[] taxonomy = {"POPLOOKSALES"};

                            InsiderProduct insiderProduct = Insider.Instance.createNewProduct("POPLOOKSALES","POPLOOKSALES",taxonomy,"POPLOOKSALES",Double.parseDouble(ITEM_PRICE),"SGD");
                            Insider.Instance.itemPurchased(TransID,insiderProduct);
//                            Insider.Instance.trackSales(this, TransID, Integer.parseInt(ITEM_PRICE), "SGD");
                            Insider.Instance.tagEvent("purchase_made").build();

                        }


                    }
                    else
                    {
                        SharedPreferences pref = ENetsPaymentActivity.this.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("TransactionID", "");
                        editor.putString("TransactionStatus", "");
                        editor.apply();
//                        Toast toast = Toast.makeText(ENetsPaymentActivity.this,
//                                "Payment Failed", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.BOTTOM, 0, 50);
//                        toast.show();

                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        passStep5=true;
                        finish();
                    }
                }

            }
            catch (Exception e){

            }

        }
        else{

            new AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage("Please connect to the Internet")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();

        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences pref =ENetsPaymentActivity.this.getSharedPreferences("MyPref", 0);
        if(pref.getString("TransactionStatus", "2").equals("1")){
            Toast toast = Toast.makeText(ENetsPaymentActivity.this,
                    "Payment Success", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();
        }else if(pref.getString("TransactionStatus", "2").equals("0")){
            Toast toast = Toast.makeText(ENetsPaymentActivity.this,
                    "Payment Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();

        }
        else if(pref.getString("TransactionStatus", "2").equals("2")){
            Toast toast = Toast.makeText(ENetsPaymentActivity.this,
                    "Payment Pending", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();
        }


        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        if(!passStep5) {
            SharedPreferences pref = ENetsPaymentActivity.this.getSharedPreferences("MyPref", 0);
            TransactionID = pref.getString("TransactionID", "");
            TransactionStatus = pref.getString("TransactionStatus", "2");
            FinalizingPayment(ORDER_ID, ITEM_PRICE, TransactionID, TransactionStatus);
        }
        overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EGHL.REQUEST_PAYMENT && data != null) {
            lastPaymentData = data;
            if (!TextUtils.isEmpty(data.getStringExtra(EGHL.RAW_RESPONSE))) {

//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                if (data.getStringExtra(EGHL.TXN_MESSAGE) != null && !TextUtils.isEmpty(data.getStringExtra(EGHL.TXN_MESSAGE))) {
//                    builder.setTitle(data.getStringExtra(EGHL.TXN_MESSAGE));
//                } else {
//                    builder.setTitle(data.getStringExtra(QueryResponse.QUERY_DESC));
//
//                }
//                String message = "TxnStatus = " + data.getIntExtra(EGHL.TXN_STATUS, EGHL.TRANSACTION_NO_STATUS) + "\n" + "TxnMessage = " + data.getStringExtra(EGHL.TXN_MESSAGE) + "\nRaw Response:\n" + data.getStringExtra(EGHL.RAW_RESPONSE);
//                builder.setMessage(message);
//                builder.setPositiveButton("OK", null);
//                AlertDialog dialog = builder.create();
//                dialog.show();

                SharedPreferences pref = ENetsPaymentActivity.this.getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                try {
                    JSONObject rawresponse = new JSONObject(data.getStringExtra(EGHL.RAW_RESPONSE));
                    editor.putString("TransactionID", rawresponse.getString("TxnID"));

//                    System.out.println("TXN  "+ URLEncoder.encode(rawresponse.toString()));

                }catch (Exception e){
                    Log.d(TAG, "RawResponseErr");
                }
                if(data.getIntExtra(EGHL.TXN_STATUS, EGHL.TRANSACTION_NO_STATUS)==0) {
                    editor.putString("TransactionStatus", "1");
                    System.out.println("TXN 1 "+EGHL.TXN_STATUS);

                }else if(data.getIntExtra(EGHL.TXN_STATUS, EGHL.TRANSACTION_NO_STATUS)==1){
                    editor.putString("TransactionStatus", "0");
                    System.out.println("TXN 2 "+EGHL.TXN_STATUS);

                }else{
                    editor.putString("TransactionStatus", "2");
                    System.out.println("TXN 3 "+EGHL.TXN_STATUS);
                }
                editor.apply();
                TransactionID = pref.getString("TransactionID", "");

                TransactionStatus = pref.getString("TransactionStatus", "2");
            }
            switch (resultCode) {
                case EGHL.TRANSACTION_SUCCESS:
                    Log.d(TAG, "onActivityResult: payment successful");
                    FinalizingPayment(ORDER_ID, ITEM_PRICE, TransactionID, TransactionStatus);
                    break;
                case EGHL.TRANSACTION_FAILED:
                    Log.d(TAG, "onActivityResult: payment failure");
                    FinalizingPayment(ORDER_ID, ITEM_PRICE, TransactionID, TransactionStatus);

                    break;
                case EGHL.TRANSACTION_CANCELLED:
                    Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
                    FinalizingPayment(ORDER_ID, ITEM_PRICE, TransactionID, TransactionStatus);
                    break;
                case EGHL.TRANSACTION_AUTHORIZED:
                    Toast.makeText(this, "Payment Authorized", Toast.LENGTH_SHORT).show();
                    FinalizingPayment(ORDER_ID, ITEM_PRICE, TransactionID, TransactionStatus);
                    break;
                default:
                    Log.d(TAG, "onActivityResult: " + resultCode);
                    FinalizingPayment(ORDER_ID, ITEM_PRICE, TransactionID, TransactionStatus);

                    break;
            }
        }

    }

}
