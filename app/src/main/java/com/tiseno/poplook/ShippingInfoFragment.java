package com.tiseno.poplook;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.text.Spannable;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tiseno.poplook.functions.URLSpanNoUnderline;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;


public class ShippingInfoFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {
    WebView localDeliveryTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping_info, container, false);
        ((MainActivity) getActivity()).changeToolBarText("Shipping Information");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        localDeliveryTV = (WebView)view.findViewById(R.id.localDeliveryTV);
//        localDeliveryTV.getSettings().setLoadWithOverviewMode(true);
//        localDeliveryTV.getSettings().setUseWideViewPort(true);
//        localDeliveryTV.getSettings().setLoadsImagesAutomatically(true);
//        localDeliveryTV.getSettings().setBlockNetworkImage(true);
//        localDeliveryTV.getSettings().setLoadsImagesAutomatically(true);
//        localDeliveryTV.getSettings().setGeolocationEnabled(false);
//        localDeliveryTV.getSettings().setNeedInitialFocus(false);
//        localDeliveryTV.getSettings().setSaveFormData(false);
////        localDeliveryTV.getSettings().setDefaultFontSize(fontSize);
////        localDeliveryTV.getSettings().setTextZoom(measurementsWV.getSettings().getTextZoom() + 150);
//        localDeliveryTV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        localDeliveryTV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        localDeliveryTV.getSettings().setJavaScriptEnabled(true);
        localDeliveryTV.getSettings().setSupportZoom(false);
        localDeliveryTV.getSettings().setBuiltInZoomControls(true);
        localDeliveryTV.getSettings().setDisplayZoomControls(false);
        localDeliveryTV.getSettings().setLoadWithOverviewMode(true);
        localDeliveryTV.getSettings().setUseWideViewPort(false);



        getTerms();
        return view;
    }

    private void getTerms(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Infos/shipping/lang/1?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){

            try {
                if (result.getBoolean("status"))
                {
                    System.out.println("hahaaaaaaab");
                    if (result.getString("action").equals("Infos_shipping"))
                    {
                        JSONArray data=result.getJSONArray("data");
                        String content = data.getJSONObject(0).getString("content");
                        localDeliveryTV.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
                    }





                }
            }
            catch (Exception e){

            }

        }
        else{

            new AlertDialog.Builder(getActivity())
                    .setTitle("Message")
                    .setMessage("Please connect to the Internet")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();

        }
    }

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }
}
