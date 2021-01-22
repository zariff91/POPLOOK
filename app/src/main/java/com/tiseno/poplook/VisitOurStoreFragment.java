package com.tiseno.poplook;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiseno.poplook.functions.HackyViewPager;
import com.tiseno.poplook.functions.ViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisitOurStoreFragment extends Fragment {

    ViewPager VP;
    PagerAdapter adapter;
    ArrayList<Uri> imgList = new ArrayList<Uri>();
    Uri[] imageListProduct;
    CirclePageIndicator imageIndicator;

    private static final String ISLOCKED_ARG = "isLocked";

    public VisitOurStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) VP).setLocked(isLocked);
        }
        View v= inflater.inflate(R.layout.fragment_visit_our_store, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Visit Our Store");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        VP = (HackyViewPager) v.findViewById(R.id.VP);
        imageIndicator = (CirclePageIndicator)v.findViewById(R.id.welcomePageIndicator);

        final float density = getResources().getDisplayMetrics().density;
//        imageIndicator.setBackgroundColor(Color.parseColor("#888888"));
        imageIndicator.setRadius(8 * density - 10);
        imageIndicator.setPageColor(Color.parseColor("#C2C2C2"));
        imageIndicator.setFillColor(Color.parseColor("#000000"));
//        imageIndicator.setStrokeColor(Color.parseColor("#000000"));
        imageIndicator.setStrokeWidth((float) (0.3 * density));
        imageIndicator.setCentered(true);
        Uri introURI,introURI1,introURI2,introURI3,introURI4;
        introURI = Uri.parse("drawable://" + R.drawable.setia_city_mall);
        introURI1 = Uri.parse("drawable://" + R.drawable.the_curve);
        introURI2 = Uri.parse("drawable://" + R.drawable.sogo);
        introURI3 = Uri.parse("drawable://" + R.drawable.pl_ioi_v2);
        introURI4 = Uri.parse("drawable://" + R.drawable.pl_kleast);


        imgList.add(introURI);
        imgList.add(introURI1);
        imgList.add(introURI2);
        imgList.add(introURI3);
        imgList.add(introURI4);
        imageListProduct = new Uri[imgList.size()];
        Arrays.fill(imageListProduct, null);

        imageListProduct = imgList.toArray(imageListProduct);
        adapter = new ViewPagerAdapter(getActivity(), imageListProduct, false);
        VP.setTag("http://poplook.com/webapi/VideoEmbed/player?video=");
        VP.setAdapter(adapter);
        VP.setOffscreenPageLimit(4);


        imageIndicator.setViewPager(VP);

        return v;
    }
    private boolean isViewPagerActive() {
        return (VP != null && VP instanceof HackyViewPager);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) VP).isLocked());
        }
        super.onSaveInstanceState(outState);
    }
}
