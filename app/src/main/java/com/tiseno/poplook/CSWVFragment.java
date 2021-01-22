package com.tiseno.poplook;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CSWVFragment extends Fragment {


    public CSWVFragment() {
        // Required empty public constructor
    }
    WebView wv;
    ProgressBar progressBar2;
    String title,content;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_cswv, container, false);
        Bundle bundle = this.getArguments();
        title = bundle.getString("title");
        content = bundle.getString("content");

        ((MainActivity) getActivity()).changeToolBarText(title);
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        progressBar2 = (ProgressBar) v.findViewById(R.id.progressBar2);
        wv = (WebView) v.findViewById(R.id.wv);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setSupportZoom(false);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(false);

        wv.loadDataWithBaseURL("", "<style>img{display: inline; height: auto; max-width: 100%;}</style>"+content, "text/html", "UTF-8", "");

        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                wv.setVisibility(View.VISIBLE);
                progressBar2.setVisibility(View.GONE);
            }

        });

        return v;
    }

}
