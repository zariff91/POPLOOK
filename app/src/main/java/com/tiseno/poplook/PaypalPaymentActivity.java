package com.tiseno.poplook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class PaypalPaymentActivity extends Activity implements AsyncTaskCompleteListener<JSONObject>{
    private static final String TAG = "PaypalPayment";
    public FirebaseAnalytics mFirebaseAnalytics;

    String ItemDetails, ItemPrice, OrderID, TransactionStatus,CART_ID;
    String Message,SelectedShopID,SelectedCountryCurrency,TransID="";
    String appVersion="1.0.0";

    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AQ3JRKXKiOL98Q3v9neQZ0mvcjSmilmUw9ss2SkftupK1u0N5ozKigjuwH8voWy3TksMaJ8nsU49skxJ";

    private static final int REQUEST_CODE_PAYMENT = 1;


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("POPLOOK Sdn Bhd")
            .merchantPrivacyPolicyUri(Uri.parse("https://poplook.com/en/content/6-privacy-policy"))
            .merchantUserAgreementUri(Uri.parse("https://poplook.com/en/content/3-terms-and-conditions-of-use"))
            .acceptCreditCards(false);

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
        setContentView(R.layout.activity_paypal_payment);
        mFirebaseAnalytics =FirebaseAnalytics.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ItemDetails = extras.getString("ITEM_DETAIL");
            ItemPrice = extras.getString("ITEM_PRICE");
            OrderID = extras.getString("ORDER_ID");
            CART_ID = extras.getString("CART_ID");
        }

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        BeginProceedToPayPal();
    }

    private void BeginProceedToPayPal()
    {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        thingToBuy.custom(CART_ID);
        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(PaypalPaymentActivity.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

//    public void onBuyPressed(View pressed) {
//        /*
//         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
//         * Change PAYMENT_INTENT_SALE to
//         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
//         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
//         *     later via calls from your server.
//         *
//         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
//         */
//        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//
//        /*
//         * See getStuffToBuy(..) for examples of some available payment options.
//         */
//
//        Intent intent = new Intent(PaypalPaymentActivity.this, PaymentActivity.class);
//
//        // send the same configuration for restart resiliency
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
//    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0);
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");
        if(SelectedShopID.equals("1")) {
            return new PayPalPayment(new BigDecimal(ItemPrice), "MYR", ItemDetails,
                    paymentIntent);
        }else{
            return new PayPalPayment(new BigDecimal(ItemPrice), SelectedCountryCurrency, ItemDetails,
                    paymentIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {

            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */

                        String PaymentId = confirm.toJSONObject().getJSONObject("response").getString("id");
                        String PaymentAmount = confirm.getPayment().toJSONObject().getString("amount");
                        Message = "Payment Success";
                        TransactionStatus = "1";

                        FinalizingPayment(OrderID, PaymentAmount, PaymentId, TransactionStatus);


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                try {

                    PaymentConfirmation confirm =
                            data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                    try {
                        String PaymentId = confirm.toJSONObject().getJSONObject("response").getString("id");
                        String PaymentAmount = confirm.getPayment().toJSONObject().getString("amount");
                        Message = "Payment Cancelled";
                        TransactionStatus = "2";

                        FinalizingPayment(OrderID, PaymentAmount, PaymentId, TransactionStatus);

                    } catch (Exception e) {
                    }

                    Log.i(TAG, "The user canceled.");
                }catch (Exception e){}

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                try {
                    String PaymentId = confirm.toJSONObject().getJSONObject("response").getString("id");
                    String PaymentAmount = confirm.getPayment().toJSONObject().getString("amount");
                    Message = "Payment Failed";
                    TransactionStatus = "0";

                    FinalizingPayment(OrderID, PaymentAmount, PaymentId, TransactionStatus);

                }catch (Exception e){}

                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
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
        TransID=TransactionID;
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
                        Toast toast = Toast.makeText(PaypalPaymentActivity.this,
                                Message, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                        if(TransactionStatus.equals("1")) {
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
                            bundle.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(ItemPrice));
                            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, TransID);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);

//                            Insider.Instance.trackPurchasedItems(this, "POPLOOKSALES", TransID, "POPLOOK SALES", "POPLOOK SALES", "POPLOOK SALES", Double.parseDouble(ItemPrice), "USD");

                        }
                        SharedPreferences pref = PaypalPaymentActivity.this.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("FromPaypalSuccess", "1");
                        editor.apply();

                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                        if(TransactionStatus.equals("1")) {

                            String[] taxonomy = {"POPLOOKSALES"};

//                            Insider.Instance.trackSales(this, TransID, Integer.parseInt(ItemPrice), "USD");
                            InsiderProduct insiderProduct = Insider.Instance.createNewProduct("POPLOOKSALES","POPLOOKSALES",taxonomy,"POPLOOKSALES",Double.parseDouble(ItemPrice),"USD");
                            Insider.Instance.itemPurchased(TransID,insiderProduct);
                            Insider.Instance.tagEvent("purchase_made").build();
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(PaypalPaymentActivity.this,
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

            new androidx.appcompat.app.AlertDialog.Builder(this)
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
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(PaypalPaymentActivity.this,
                "Payment Pending", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.show();

        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
    }
}