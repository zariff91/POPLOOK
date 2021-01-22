package com.tiseno.poplook;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;


public class PersonalDataProtectionFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {
        WebView PDPTV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_data_protection, container, false);


        ((MainActivity) getActivity()).changeToolBarText("Personal Data Protection");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(true);
        ((MainActivity) getActivity()).setDrawerState(false);


        PDPTV=(WebView)view.findViewById(R.id.PDPTV);
        PDPTV.getSettings().setLoadWithOverviewMode(true);
        PDPTV.getSettings().setUseWideViewPort(true);
        PDPTV.getSettings().setLoadsImagesAutomatically(true);
        PDPTV.getSettings().setAppCacheEnabled(false);
        PDPTV.getSettings().setBlockNetworkImage(true);
        PDPTV.getSettings().setLoadsImagesAutomatically(true);
        PDPTV.getSettings().setGeolocationEnabled(false);
        PDPTV.getSettings().setNeedInitialFocus(false);
        PDPTV.getSettings().setSaveFormData(false);
//        PDPTV.getSettings().setDefaultFontSize(fontSize);
//        PDPTV.getSettings().setTextZoom(measurementsWV.getSettings().getTextZoom() + 150);
        PDPTV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        PDPTV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getPersonalDataProtection();
        return view;
    }

    private void getPersonalDataProtection(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Infos/dataprotection/lang/1?apikey="+apikey;
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
                    if (result.getString("action").equals("Infos_dataprotection"))
                    {

                        JSONArray data=result.getJSONArray("data");
                        String content = data.getJSONObject(0).getString("content");
                        PDPTV.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
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

}

