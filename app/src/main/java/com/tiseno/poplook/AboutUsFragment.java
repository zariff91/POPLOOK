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


public class AboutUsFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    WebView AUTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        ((MainActivity) getActivity()).changeToolBarText("About Us");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);


        AUTV = (WebView) view.findViewById(R.id.AUTV);
//        AUTV.getSettings().setLoadWithOverviewMode(true);
//        AUTV.getSettings().setUseWideViewPort(true);
//        AUTV.getSettings().setLoadsImagesAutomatically(true);
//        AUTV.getSettings().setAppCacheEnabled(false);
//        AUTV.getSettings().setBlockNetworkImage(true);
//        AUTV.getSettings().setLoadsImagesAutomatically(true);
//        AUTV.getSettings().setGeolocationEnabled(false);
//        AUTV.getSettings().setNeedInitialFocus(false);
//        AUTV.getSettings().setSaveFormData(false);
////        AUTV.getSettings().setDefaultFontSize(fontSize);
////        AUTV.getSettings().setTextZoom(measurementsWV.getSettings().getTextZoom() + 150);
//        AUTV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        AUTV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        AUTV.getSettings().setJavaScriptEnabled(true);
        AUTV.getSettings().setSupportZoom(false);
        AUTV.getSettings().setBuiltInZoomControls(true);
        AUTV.getSettings().setDisplayZoomControls(false);
        AUTV.getSettings().setLoadWithOverviewMode(true);
        AUTV.getSettings().setUseWideViewPort(false);
        getAboutUs();
        return view;
    }

    private void getAboutUs(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Infos/about?apikey="+apikey+"&lang=1";
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
                    if (result.getString("action").equals("Infos_about"))
                    {
                        JSONArray data=result.getJSONArray("data");
                        String content = data.getJSONObject(0).getString("content");
                        AUTV.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
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
