package com.tiseno.poplook;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;

public class NewCheckoutPage extends Fragment {

    TextView shippingTxt;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){

        View contentView = inflater.inflate(R.layout.new_checkout_page, container, false);

        shippingTxt = (TextView)contentView.findViewById(R.id.shippingText);
        shippingTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));


        return contentView;

    }

}
