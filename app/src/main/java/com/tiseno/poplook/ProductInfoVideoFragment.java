package com.tiseno.poplook;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.tiseno.poplook.functions.FontUtil;

public class ProductInfoVideoFragment extends Fragment {

    WebView videoView;

    TextView close;

    ProgressBar centerBar;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product_info_video_view, container, false);
        videoView = (WebView) view.findViewById(R.id.videoProduct);
        close = (TextView) view.findViewById(R.id.closeTxt);
        centerBar = (ProgressBar)view.findViewById(R.id.progressBarVideo);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        Bundle bundle = this.getArguments();
        String urlVideo = bundle.getString("videoURL");

        System.out.println("get video url = " + urlVideo);

        videoView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.setWebChromeClient(new WebChromeClient());
        videoView.setWebViewClient(new WebViewClient());

        videoView.loadUrl(urlVideo);

        videoView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                centerBar.setVisibility(View.GONE);
            }
        });

//        if(videoView.getProgress() != 100){
//            Toast toast = Toast.makeText(getActivity(),
//                    "Loading...Video may take a few seconds to show", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.BOTTOM, 0, 50);
//            toast.show();
//        }


        return view;
    }
}
