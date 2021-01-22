package com.tiseno.poplook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
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


public class PrivacyPolicyFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    WebView PPTV;
    String fromSignUp;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        Bundle bundle = this.getArguments();
        fromSignUp = bundle.getString("fromSignUp");
        if (fromSignUp.equals("Nope")) {
            ((MainActivity) getActivity()).changeToolBarText("Privacy Policy");
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
            ((MainActivity) getActivity()).changeToolBarText("Privacy Policy");
            ((MainActivity) getActivity()).changeToolBarTextView(true);
            ((MainActivity) getActivity()).changeBtnBackView(false);
            ((MainActivity) getActivity()).changeToolBarImageView(false);
            ((MainActivity) getActivity()).changeBtnSearchView(false);
            ((MainActivity) getActivity()).changeBtnBagView(false);
            ((MainActivity) getActivity()).changeBtnWishlistView(false);
            ((MainActivity) getActivity()).changeBtnCloseXView(true);
            ((MainActivity) getActivity()).setDrawerState(false);
        }
        PPTV=(WebView)view.findViewById(R.id.PPTV);
        PPTV.getSettings().setJavaScriptEnabled(true);
        PPTV.getSettings().setSupportZoom(false);
        PPTV.getSettings().setBuiltInZoomControls(true);
        PPTV.getSettings().setDisplayZoomControls(false);
        PPTV.getSettings().setLoadWithOverviewMode(true);
        PPTV.getSettings().setUseWideViewPort(false);
        getPrivacyPolicy();
        return view;
    }

    private void getPrivacyPolicy(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Infos/privacypolicy/lang/1?apikey="+apikey;
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
                    if (result.getString("action").equals("Infos_privacypolicy"))
                    {
                        JSONArray data=result.getJSONArray("data");
                        String content = data.getJSONObject(0).getString("content");
                        PPTV.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
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
