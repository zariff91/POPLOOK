package com.tiseno.poplook;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;


public class TermsCondFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    WebView TCTV;
    String fromPayment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_terms_cond, container, false);

        Bundle bundle = this.getArguments();
        fromPayment = bundle.getString("fromPayment");
        if (fromPayment.equals("Nope")) {
            ((MainActivity) getActivity()).changeToolBarText("Terms And Conditions");
            ((MainActivity) getActivity()).changeToolBarTextView(true);
            ((MainActivity) getActivity()).changeBtnBackView(true);
            ((MainActivity) getActivity()).changeToolBarImageView(false);
            ((MainActivity) getActivity()).changeBtnSearchView(true);
            ((MainActivity) getActivity()).changeBtnBagView(true);
            ((MainActivity) getActivity()).changeBtnWishlistView(true);
            ((MainActivity) getActivity()).changeBtnCloseXView(false);
            ((MainActivity) getActivity()).setDrawerState(true);
        }
        else{
            ((MainActivity) getActivity()).changeToolBarText("Terms And Conditions");
            ((MainActivity) getActivity()).changeToolBarTextView(true);
            ((MainActivity) getActivity()).changeBtnBackView(false);
            ((MainActivity) getActivity()).changeToolBarImageView(false);
            ((MainActivity) getActivity()).changeBtnSearchView(false);
            ((MainActivity) getActivity()).changeBtnBagView(false);
            ((MainActivity) getActivity()).changeBtnWishlistView(false);
            ((MainActivity) getActivity()).changeBtnCloseXView(true);
            ((MainActivity) getActivity()).setDrawerState(false);
        }

        TCTV = (WebView)v.findViewById(R.id.TCTV);
        TCTV.getSettings().setJavaScriptEnabled(true);
        TCTV.getSettings().setSupportZoom(false);
        TCTV.getSettings().setBuiltInZoomControls(true);
        TCTV.getSettings().setDisplayZoomControls(false);
        TCTV.getSettings().setLoadWithOverviewMode(true);
        TCTV.getSettings().setUseWideViewPort(false);
        getTerms();
        return v;
    }


    private void getTerms(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Infos/term/lang/1?apikey="+apikey;
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
                    if (result.getString("action").equals("Infos_term"))
                    {
                        JSONArray data=result.getJSONArray("data");
                        String content = data.getJSONObject(0).getString("content");
                        TCTV.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
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
