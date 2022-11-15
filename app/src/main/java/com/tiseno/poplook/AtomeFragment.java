package com.tiseno.poplook;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONException;
import org.json.JSONObject;

public class AtomeFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    String URL;
    String orderID;
    String appVersion="1.0.0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_atome, container, false);

        Bundle bundle = this.getArguments();
        URL = bundle.getString("paymentURL");
        orderID = bundle.getString("referenceID");

        WebView webView = view.findViewById(R.id.webViewAtome);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);
        webView.setWebViewClient(new WebViewClient()
        {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(url.contains("orderStep5")){

                    System.out.println("atome payment url = " + url);


                    finalzePayment();
                }

                return true;
            }
        }
        );

        return view;
    }

    public void finalzePayment(){
        String urlAtome = "https://api.apaylater.net/v2/payments/"+orderID;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(urlAtome);
    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("atome payment result = " + result);

        if (result != null) {
            try {

                if(result.has("action")){

                    System.out.println("atome payment result 2 = " + result);

                    String action = result.getString("action");
                    if (action.equals("Carts_OrderStep5")) {
                        if (result.getBoolean("status")) {
                            Fragment fragment = new OrderHistoryFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Payment Failed", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                            Fragment fragment = new OrderHistoryFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                }

                else {

                    System.out.println("atome payment result 3 = " + result);


                    String paymentStatus = result.getString("status");
                    int totalAmount = result.getInt("amount");

                    if (paymentStatus.equals("PAID")) {

                        PackageInfo pInfo = null;

                        try {
                            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        //get the app version Name for display
                        appVersion = pInfo.versionName;

                        System.out.println("atome payment result 7 = " + totalAmount);


                        double finalAmount = totalAmount / 100;

                        System.out.println("atome payment result 6 = " + finalAmount);

                        String totalAmountStr = String.valueOf(finalAmount);

                        System.out.println("atome payment result 5 = " + totalAmountStr);

                        JSONObject paymentDetail = result.getJSONObject("paymentTransaction");
                        String transactionID = paymentDetail.getString("transactionId");

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        String apikey = pref.getString("apikey", "");
                        String action = "Carts/OrderStep5?apikey="+apikey +"&device_type=android&id_order="+orderID+"&transaction_status=1&payment_type=atome&total_paid=" + totalAmountStr + "&transaction_id=" + transactionID + "&app_version=" + appVersion;
                        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                        callws.execute(action);
                    } else {
                        PackageInfo pInfo = null;

                        try {
                            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        //get the app version Name for display
                        appVersion = pInfo.versionName;

                        double finalAmount = totalAmount / 100;
                        String totalAmountStr = String.valueOf(finalAmount);

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        String apikey = pref.getString("apikey", "");
                        String action = "Carts/OrderStep5?apikey=" + apikey + "&device_type=android&id_order=" + orderID + "&transaction_status=0&payment_type=atome&total_paid=" + totalAmountStr + "&transaction_id&app_version=" + appVersion;
                        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                        callws.execute(action);
                    }
                }

            } catch (JSONException e) {

                System.out.println("atome payment result 4 = " + result);

                e.printStackTrace();
            }
        }
    }
}