package com.tiseno.poplook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Base64;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ipay.IPayIH;
import com.ipay.IPayIHPayment;
import com.ipay.constants.ConnectAddress;
import com.tiseno.poplook.functions.IPay88ResultDelegate;
import com.tiseno.poplook.functions.ResultDelegate;

import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderProduct;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

//import my.com.ipay88.androidsdkmerchanthosted.pay.mt.MCTokenizationRet;

import static okio.ByteString.decodeBase64;

public class IPay88PaymentActivity extends Activity implements AsyncTaskCompleteListener<JSONObject>{

    public static String resultExtra;
//    public static MCTokenizationRet mcTokenizationRet;

    public static String resultTitle;
    public static String resultInfo;
    public static String transactionID;
    public static String referenceNo;
    public static String totalAmount;
    public static String transactionStatus;
    String appVersion="1.0.0";
    public FirebaseAnalytics mFirebaseAnalytics;

    String CREDITCARD_PAYMENT,SelectedCountryCurrency="MYR", PAYMENT_ID, ORDER_ID,ITEM_PRICE,USER_NAME,USER_EMAIL,USER_CONTACT,CART_ID,BACKEND_URL="https://poplook.com/modules/ipay88induxive/backend_response.php";

    String eWalletPayment;

    int method=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipay888_payment);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CREDITCARD_PAYMENT = extras.getString("CREDITCARD_PAYMENT");
            eWalletPayment = extras.getString("eWallet");
            ORDER_ID = extras.getString("ORDER_ID");
            System.out.println("ORDERID" + ORDER_ID);
            ITEM_PRICE = extras.getString("ITEM_PRICE");
            CART_ID = extras.getString("CART_ID");
            System.out.println("Cart_ID Ipay " + CART_ID);
        }


        SharedPreferences pref = this.getSharedPreferences("MyPref", 0);
        USER_NAME = pref.getString("Name", "") + " " + pref.getString("LastName", "");
        USER_EMAIL = pref.getString("Email", "");
        //multishop
        SelectedCountryCurrency = pref.getString("SelectedCountryCurrency", "MYR");
        String SelectedCountryIsoCode = pref.getString("SelectedCountryIsoCode", "MY");
        //
        USER_CONTACT = pref.getString("userContact", "");
        try {
            IPayIHPayment payment = new IPayIHPayment();

            if (CREDITCARD_PAYMENT.equals("1")) {
                method = IPayIH.PAY_METHOD_CREDIT_CARD;
//                payment.setMerchantKey("s8EXcXnvqK");
                payment.setMerchantCode("M01333_S0001");
                if (SelectedCountryCurrency.equals("RM")) {
                    PAYMENT_ID = "2";
                } else {
                    PAYMENT_ID = "25";
                }
            } else if (CREDITCARD_PAYMENT.equals("0")) {
                method = IPayIH.PAY_METHOD_CREDIT_CARD;
//                method = IPayIH.PAY_METHOD_EWALLET; ipay88SDK error
//                payment.setMerchantKey("s8EXcXnvqK");
                payment.setMerchantCode("M01333_S0001");
                PAYMENT_ID = extras.getString("PAYMENT_ID");
            } else if (CREDITCARD_PAYMENT.equals("4")) {

                method = IPayIH.PAY_METHOD_CREDIT_CARD;
//                method = IPayIH.PAY_METHOD_EWALLET; ipay88SDK error
//                payment.setMerchantKey("s8EXcXnvqK");
                payment.setMerchantCode("M01333_S0001");
                PAYMENT_ID = eWalletPayment;

            }


            if (SelectedCountryCurrency.equals("RM")) {
                payment.setCurrency("MYR");
            BACKEND_URL = "https://poplook.com/modules/ipay88induxive/backend_response.php";
//                BACKEND_URL = "https://dev3.poplook.com/modules/ipay88induxive/backend_response.php";

            } else {
                payment.setCurrency(SelectedCountryCurrency);
            BACKEND_URL = "https://poplook.com/modules/usdcreditcard/backend_response.php";
//                BACKEND_URL = "https://dev3.poplook.com/modules/usdcreditcard/backend_response.php";

            }
//            BACKEND_URL = decodeBase64(ConnectAddress.getBackendPostAddr());
            System.out.println("payment id is " + PAYMENT_ID);

            payment.setPaymentId(PAYMENT_ID);
            payment.setRefNo(CART_ID);
            payment.setAmount(ITEM_PRICE);//change with real price
            payment.setProdDesc("Poplook Purchases");
            payment.setUserName(USER_NAME);
            payment.setUserEmail(USER_EMAIL);
//            payment.setUserContact(USER_CONTACT);
            payment.setRemark("Modest Fashion");
            payment.setLang("ISO-8859-1");
            payment.setCountry("MY");
            payment.setBackendPostURL(BACKEND_URL);
            payment.setResponseURL("");
//            payment.setTID("");
//            payment.setXfield1("");
//            payment.setENV(IPayIH.ENV_PRODUCTION);

        Intent selectionIntent = IPayIH.getInstance().checkout(payment
                , this, new IPay88ResultDelegate(), method);
        startActivityForResult(selectionIntent, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private String decodeBase64(String text) {
//        String result = "";
//        byte[] data = Base64.decode(text, Base64.DEFAULT);
//        try {
//            result = new String(data, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode != 1)
            return;

        if(resultInfo != null)
        {
            FinalizingPayment(ORDER_ID,totalAmount,transactionID,transactionStatus);
        }

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

        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        String PaymentType = pref.getString("PaymentType", "");
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

                if(action.equals("Carts_OrderStep5"))
                {
                    if(result.getBoolean("status"))
                    {

                        String[] taxonomy = {"POPLOOKSALES"};

                        InsiderProduct insiderProduct = Insider.Instance.createNewProduct("POPLOOKSALES","POPLOOKSALES",taxonomy,"POPLOOKSALES",Double.parseDouble(ITEM_PRICE),"MYR");
                        Insider.Instance.itemPurchased(transactionID,insiderProduct);
//                            Insider.Instance.trackSales(this, TransID, Integer.parseInt(ITEM_PRICE), "SGD");
                        Insider.Instance.tagEvent("purchase_made").build();

                        Toast toast = Toast.makeText(IPay88PaymentActivity.this,
                                resultInfo, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                        if(transactionStatus.equals("1")) {
                            if(SelectedCountryCurrency.equalsIgnoreCase("RM")){
                                SelectedCountryCurrency="MYR";
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.CURRENCY, SelectedCountryCurrency);
                            bundle.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(ITEM_PRICE));
                            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionID);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);

//                            InsiderProduct insiderProduct = Insider.Instance.createNewProduct("POPLOOKSALES","POPLOOKSALES","POPLOOKSALES","POPLOOKSALES",Double.parseDouble(ITEM_PRICE),SelectedCountryCurrency);
//                            Insider.Instance.itemPurchased(transactionID,insiderProduct);
////                            Insider.Instance.trackSales(this, TransID, Integer.parseInt(ITEM_PRICE), "SGD");
//                            Insider.Instance.tagEvent("purchase_made");

//                            InsiderProduct product = Insider.Instance.createNewProduct("");

//                          Insider.Instance.trackSales(this, transactionID, Integer.parseInt(ITEM_PRICE), SelectedCountryCurrency);
//                          Insider.Instance.tagEvent(this,"purchase_made");
//                            Insider.Instance.trackPurchasedItems(this, "POPLOOKSALES", transactionID, "POPLOOK SALES", "POPLOOK SALES", "POPLOOK SALES", Double.parseDouble(ITEM_PRICE), SelectedCountryCurrency);

                        }

                        SharedPreferences pref = IPay88PaymentActivity.this.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        if(transactionStatus.equals("1")) {

                            editor.putString("payment_success","1");
                            editor.putString("order_success_id",ORDER_ID);

                        }

                        else {

                            editor.putString("payment_success","0");
                            editor.putString("order_success_id",ORDER_ID);



                        }

                        editor.putString("FromPaypalSuccess", "1");
                        editor.apply();

                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();

                    }
                    else
                    {
                        Toast toast = Toast.makeText(IPay88PaymentActivity.this,
                                "Payment Failed", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
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
        FinalizingPayment(ORDER_ID,ITEM_PRICE,ORDER_ID,"0");

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
    }
}
