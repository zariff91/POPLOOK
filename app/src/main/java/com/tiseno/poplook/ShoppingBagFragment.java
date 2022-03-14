package com.tiseno.poplook;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.shoppingBagAdapter;
import com.tiseno.poplook.functions.shoppingBagItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingBagFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    String UserID, CartID, LanguageID,totalPriceWt,totalPrice="",taxAmount;
    String CartMessages , CartMessageContent;
    String totalItemInBag = "0";

    JSONObject cartResultJObj,cartResultJObj1;

    JSONArray cartTopMessage;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter sbAdapter,sb2Adapter;
    Boolean IsGiftOptionsEnabled = false;
    Boolean IsLeaveMSGEnabled = false;
    Boolean refreshCallApi = false;

    int pos=0;
    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<shoppingBagItem>();
    ArrayList<JSONObject> listArray_outofstockitem = new ArrayList<JSONObject>();

    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();
    ArrayList<voucherItem> listArray_store_credit = new ArrayList<voucherItem>();
    ArrayList<addressItem> listArray_address = new ArrayList<addressItem>();
    RelativeLayout giftBarRL, leaveMsgBarRL, giftOptionsPopOutRL;
    ImageView giftOptionsNOIV,giftOptionsYESIV, leaveMsgNOIV,leaveMsgYESIV, giftOptionsPopOutCloseIV, giftOptionsIV;
    TextView giftOptionsNOTV,bottomNext, giftOptionsYESTV, giftOptionsNoOfCharTV, leaveMsgNOTV, leaveMsgYESTV;
    EditText giftOptionsET, leaveMsgET;

    TextView codeTV,codeApplyTV,giftOptionsTV,leaveMsgTV,shoppingBarNextBtnTV;
    TextView totalPriceTV,totalPayableTV,totalPayableRMTV,shippingTVCart,shippingRMTVCart;
    TextView shoppingBagEmptyTV,additionalTopViewText,additionalTopViewText2,additionalTopViewText3,totalPriceRMTV;
    TextView bottomMessage;

    ImageButton shoppingBarNextBtnIB, codeApplyIB;

    EditText codeET;
    String NeedToGoBackToCart;
    float totalAllProductPrice;
    float totalVoucherPrice = 0;

    String oriGiftOptionsText = "";
    String oriLeaveMessageText = "";
    String SelectedShopID,SelectedCountryCurrency;

    String bottomText = "";

    String sale_notice_state;

    String shippingPrice;

    String couponCode;

    Boolean haveOutOfStock = false;

    public ShoppingBagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Create a RecyclerView with a linear layout manager
        View contentView = inflater.inflate(R.layout.fragment_shopping_bag, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Shopping Bag");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).showBottomBar(false);



        shoppingBagEmptyTV = (TextView) contentView.findViewById(R.id.shoppingBagEmptyTV);
        shoppingBagEmptyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.shoppingBagRV);
        bottomNext = (TextView) contentView.findViewById(R.id.cartBottomView);
        bottomNext.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));



        bottomNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalItemInBag.equals("0"))
                {

                }
                else
                {
                    goToNextStepWS(giftOptionsET.getText().toString(),leaveMsgET.getText().toString());

                }


            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "");
        LanguageID = pref.getString("LanguageID", "");
        NeedToGoBackToCart = pref.getString("NeedToGoBackToCart","");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");

        Insider.Instance.tagEvent("cart_visited").build();

        //

        getUserCartList();
        getUserCartList();

        if(NeedToGoBackToCart.equals("1")){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("NeedToGoBackToCart", "0");
            editor.apply();
            goToNextStepWS("","");

        }else{
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("NeedToGoBackToCart", "0");
            editor.apply();

        }
        return contentView;

    }

    public void refreshRecyclerView()
    {
        // Make Bookends
        sbAdapter = new shoppingBagAdapter(getActivity(),listArray_shoppingBag,ShoppingBagFragment.this, true);
        final Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(sbAdapter);

        // Inflate footer view
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

        RelativeLayout footerEarlyView = (RelativeLayout) inflater1.inflate(R.layout.shopping_bar_footer_first_view, mRecyclerView, false);
//        final RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view, mRecyclerView, false);
        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.shopping_bar_footer_view, mRecyclerView, false);

        LinearLayout header = (LinearLayout) inflater1.inflate(R.layout.shopping_bar_header, mRecyclerView, false);

        additionalTopViewText = (TextView) header.findViewById(R.id.additionalTopViewTextheader);
        additionalTopViewText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        additionalTopViewText2 = (TextView) header.findViewById(R.id.additionalTopViewText2header);
        additionalTopViewText2.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        additionalTopViewText3 = (TextView) header.findViewById(R.id.additionalTopViewText3header);
        additionalTopViewText3.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        totalPriceRMTV = (TextView)footerEarlyView.findViewById(R.id.totalPriceRMTV);
//        totalPriceRM1TV = (TextView)footerEarlyView.findViewById(R.id.totalPriceRM1TV);
        totalPriceTV = (TextView)footerEarlyView.findViewById(R.id.totalPriceTV);
        shippingRMTVCart = (TextView)footerEarlyView.findViewById(R.id.shippingMethodTVlblCart);
        shippingTVCart = (TextView)footerEarlyView.findViewById(R.id.shippingMethodTVCart);

        totalPayableTV = (TextView)footerEarlyView.findViewById(R.id.totalPayableTVCart);
        totalPayableRMTV = (TextView)footerEarlyView.findViewById(R.id.totalPayableRMTVCart);

        totalPayableTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalPayableRMTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));


        bottomMessage = (TextView)footerEarlyView.findViewById(R.id.bottomMessage);
        giftBarRL = (RelativeLayout)footer.findViewById(R.id.giftBarRL);
        giftOptionsNOIV = (ImageView)footer.findViewById(R.id.giftOptionsNOIV);
        giftOptionsYESIV = (ImageView)footer.findViewById(R.id.giftOptionsYESIV);
        giftOptionsNOTV = (TextView)footer.findViewById(R.id.giftOptionsNOTV);
        giftOptionsYESTV = (TextView)footer.findViewById(R.id.giftOptionsYESTV);
        giftOptionsET = (EditText)footer.findViewById(R.id.giftOptionsET);
        giftOptionsNoOfCharTV = (TextView)footer.findViewById(R.id.giftOptionsNoOfCharTV);

        leaveMsgBarRL = (RelativeLayout)footer.findViewById(R.id.leaveMsgRL);
        leaveMsgNOIV = (ImageView)footer.findViewById(R.id.leaveMsgNOIV);
        leaveMsgYESIV = (ImageView)footer.findViewById(R.id.leaveMsgYESIV);
        leaveMsgNOTV = (TextView)footer.findViewById(R.id.leaveMsgNOTV);
        leaveMsgYESTV = (TextView)footer.findViewById(R.id.leaveMsgYESTV);
        leaveMsgET = (EditText)footer.findViewById(R.id.leaveMsgET);

        giftOptionsIV = (ImageView)footer.findViewById(R.id.giftOptionsIV);
        giftOptionsPopOutRL = (RelativeLayout) footer.findViewById(R.id.giftOptionsPopOutRL);
        giftOptionsPopOutCloseIV = (ImageView) footer.findViewById(R.id.giftOptionsPopOutCloseIV);

//        shoppingBarNextBtnIB = (ImageButton) footer.findViewById(R.id.shoppingBarNextBtnIB);

        codeApplyIB = (ImageButton) footer.findViewById(R.id.codeApplyIB);

        codeTV = (TextView) footer.findViewById(R.id.codeTV);
        codeApplyTV = (TextView) footer.findViewById(R.id.codeApplyTV);
        giftOptionsTV = (TextView) footer.findViewById(R.id.giftOptionsTV);
        leaveMsgTV = (TextView) footer.findViewById(R.id.leaveMsgTV);
        shoppingBarNextBtnTV = (TextView) footer.findViewById(R.id.shoppingBarNextBtnTV);

        codeET = (EditText) footer.findViewById(R.id.codeET);

        totalPriceRMTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        bottomMessage.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        totalPriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        shippingTVCart.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        shippingRMTVCart.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        if(CartMessages.equalsIgnoreCase("alert-success"))
        {

            try {

                if(cartTopMessage.length() == 1)
                {

                    String firstText = cartTopMessage.getString(0);
                    additionalTopViewText.setVisibility(View.VISIBLE);
                    additionalTopViewText.setText(firstText);

                    additionalTopViewText2.setVisibility(View.GONE);
                    additionalTopViewText3.setVisibility(View.GONE);
                }

                if(cartTopMessage.length() == 2)
                {

                    String firstText = cartTopMessage.getString(0);
                    String secondText = cartTopMessage.getString(1);

                    additionalTopViewText.setVisibility(View.VISIBLE);
                    additionalTopViewText.setText(firstText);

                    additionalTopViewText2.setVisibility(View.VISIBLE);
                    additionalTopViewText2.setText(secondText);

                    additionalTopViewText3.setVisibility(View.GONE);
                }

                if(cartTopMessage.length() == 3)
                {

                    String firstText = cartTopMessage.getString(0);
                    String scnText = cartTopMessage.getString(1);
                    String thirdText = cartTopMessage.getString(2);


                    additionalTopViewText.setVisibility(View.VISIBLE);
                    additionalTopViewText.setText(firstText);
                    additionalTopViewText2.setVisibility(View.VISIBLE);
                    additionalTopViewText2.setText(scnText);
                    additionalTopViewText3.setVisibility(View.VISIBLE);
                    additionalTopViewText3.setText(thirdText);


                }

            }
           catch (Exception e)
           {

           }

//            String cartMessage = CartMessageContent.replace("[", "").replace("\"", "").replace("]", "");
//            additionalTopViewText.setVisibility(View.VISIBLE);
//            additionalTopViewText.setText(cartMessage);

        }


        mBookends.addHeader(header);

        if(SelectedShopID.equals("1")){
            totalPriceRMTV.setText("Subtotal");
            totalPriceTV.setText(": RM " + totalPriceWt);
        }else{
            totalPriceRMTV.setText("Subtotal");
            totalPriceTV.setText(": " +SelectedCountryCurrency+" "+ totalPriceWt);
        }

        if(shippingPrice.equals("0.00")){
            shippingTVCart.setText("Free Shipping");

        }else{
            shippingTVCart.setText(": " +SelectedCountryCurrency+" "+ shippingPrice);
        }


        if (totalPrice.equalsIgnoreCase("0.00")){
            totalPayableTV.setText(": " +SelectedCountryCurrency+" "+ "0.00");
        }else if(!totalPrice.contains(".")) {
            totalPayableTV.setText(": " +SelectedCountryCurrency+" " +totalPrice+".00");

            float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
            System.out.print("GST" + rounded);
        }else if(Float.parseFloat(totalPrice)<0.00){
            totalPayableTV.setText(": " +SelectedCountryCurrency+" "+ "0.00");
        }else{
            totalPrice=String.format("%.2f", Double.parseDouble(totalPrice));
            totalPayableTV.setText(": " +SelectedCountryCurrency+" "+ totalPrice);
        }

        if(sale_notice_state.equals("1"))
        {
            bottomMessage.setText(bottomText);
            bottomMessage.setVisibility(View.VISIBLE);
        }

        else {
            bottomMessage.setVisibility(View.GONE);
        }

        giftOptionsNoOfCharTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        codeTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        codeApplyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        giftOptionsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        leaveMsgTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shoppingBarNextBtnTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        codeET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        giftOptionsET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        leaveMsgET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
            String leaveMessage = pref.getString("LeaveMessage", "");
            String giftMessage = pref.getString("giftMessage", "");

            if(!leaveMessage.equals("")){
                IsLeaveMSGEnabled = true;
                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
                leaveMsgNOIV.setImageResource(R.drawable.btn_check);
                leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
                leaveMsgET.setVisibility(View.VISIBLE);
                leaveMsgET.setText(leaveMessage);
            }else{
                IsLeaveMSGEnabled = false;
                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
                leaveMsgNOIV.setImageResource(R.drawable.btn_check_active);
                leaveMsgYESIV.setImageResource(R.drawable.btn_check);
                leaveMsgET.setVisibility(View.GONE);
            }




        giftOptionsNOTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsGiftOptionsEnabled = false;
                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
                giftOptionsNOIV.setImageResource(R.drawable.btn_check_active);
                giftOptionsYESIV.setImageResource(R.drawable.btn_check);
                giftOptionsET.setVisibility(View.GONE);
                giftOptionsNoOfCharTV.setVisibility(View.GONE);
            }
        });

        giftOptionsYESTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsGiftOptionsEnabled = true;
                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
                giftOptionsNOIV.setImageResource(R.drawable.btn_check);
                giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
                giftOptionsET.setVisibility(View.VISIBLE);
                giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);
            }
        });

        giftOptionsNOIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsGiftOptionsEnabled = false;
                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
                giftOptionsNOIV.setImageResource(R.drawable.btn_check_active);
                giftOptionsYESIV.setImageResource(R.drawable.btn_check);
                giftOptionsET.setVisibility(View.GONE);
                giftOptionsNoOfCharTV.setVisibility(View.GONE);
            }
        });

        giftOptionsYESIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsGiftOptionsEnabled = true;
                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
                giftOptionsNOIV.setImageResource(R.drawable.btn_check);
                giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
                giftOptionsET.setVisibility(View.VISIBLE);
                giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);
            }
        });

        if(!giftMessage.equals("")){
            IsGiftOptionsEnabled = true;
            giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
            giftOptionsNOIV.setImageResource(R.drawable.btn_check);
            giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
            giftOptionsET.setVisibility(View.VISIBLE);
            giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);
            giftOptionsET.setText(giftMessage);
        }else{
            IsGiftOptionsEnabled = false;
            giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
            giftOptionsNOIV.setImageResource(R.drawable.btn_check_active);
            giftOptionsYESIV.setImageResource(R.drawable.btn_check);
            giftOptionsET.setVisibility(View.GONE);
            giftOptionsNoOfCharTV.setVisibility(View.GONE);
        }


        leaveMsgNOTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsLeaveMSGEnabled = false;
                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
                leaveMsgNOIV.setImageResource(R.drawable.btn_check_active);
                leaveMsgYESIV.setImageResource(R.drawable.btn_check);
                leaveMsgET.setVisibility(View.GONE);
            }
        });

        leaveMsgYESTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsLeaveMSGEnabled = true;
                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
                leaveMsgNOIV.setImageResource(R.drawable.btn_check);
                leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
                leaveMsgET.setVisibility(View.VISIBLE);
            }
        });

        leaveMsgNOIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsLeaveMSGEnabled = false;
                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
                leaveMsgNOIV.setImageResource(R.drawable.btn_check_active);
                leaveMsgYESIV.setImageResource(R.drawable.btn_check);
                leaveMsgET.setVisibility(View.GONE);
            }
        });

        leaveMsgYESIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsLeaveMSGEnabled = true;
                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
                leaveMsgNOIV.setImageResource(R.drawable.btn_check);
                leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
                leaveMsgET.setVisibility(View.VISIBLE);
            }
        });

        giftOptionsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftOptionsPopOutRL.setVisibility(View.VISIBLE);
            }
        });

        giftOptionsPopOutCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftOptionsPopOutRL.setVisibility(View.GONE);
            }
        });
        String NeedToGoBackToCart = pref.getString("NeedToGoBackToCart","0");

        // Attach footer view
        mBookends.addFooter(footerEarlyView);

        for(int i = 0; i <listArray_voucher.size(); i++)
        {   pos=i;
            final voucherItem item=listArray_voucher.get(i);
            RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view_shoppingbag, mRecyclerView, false);

            TextView voucherCodeNameTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV);
            TextView voucherCodeNameTV1 = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV1);
            TextView voucherCodePriceLblTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceLblTV);
            TextView voucherCodePriceTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceTV);
            ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode.findViewById(R.id.voucherCodeRemove);

            voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeNameTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeRemove.setVisibility(View.VISIBLE);

            voucherCodePriceLblTV.setText(SelectedCountryCurrency);
            voucherCodeNameTV.setText("Code - " + listArray_voucher.get(pos).getvoucherCode());
            voucherCodeNameTV1.setText(listArray_voucher.get(pos).getvoucherName());
            voucherCodePriceTV.setText(listArray_voucher.get(pos).getvoucherReduceAmount());

            voucherCodeRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Remove Voucher Code?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteVoucherFromCart(item.getvoucherID(), CartID);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .show();


                }
            });
//            mBookends.addFooter(footerVoucherCode);
        }


        for(int i = 0; i <listArray_store_credit.size(); i++)
        {   pos=i;
            final voucherItem item=listArray_store_credit.get(i);
            RelativeLayout footerVoucherCode1 = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view_shoppingbag, mRecyclerView, false);

            TextView voucherCodeNameTV = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodeNameTV);
            TextView voucherCodeNameTV1 = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodeNameTV1);
            TextView voucherCodePriceLblTV = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodePriceLblTV);
            TextView voucherCodePriceTV = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodePriceTV);
            ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode1.findViewById(R.id.voucherCodeRemove);


//            TextView totalPayableTVsc = (TextView)footerVoucherCode1.findViewById(R.id.totalPayableTVCart);
//            TextView totalPayableRMTVsc = (TextView)footerVoucherCode1.findViewById(R.id.totalPayableRMTVCart);

//            totalPayableTVsc.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//            totalPayableRMTVsc.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//
            voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeNameTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeRemove.setVisibility(View.VISIBLE);

            voucherCodePriceLblTV.setText(SelectedCountryCurrency);
            voucherCodeNameTV.setText(listArray_store_credit.get(pos).getvoucherCode());
            voucherCodeNameTV1.setText(listArray_store_credit.get(pos).getvoucherName());
            voucherCodePriceTV.setText(listArray_store_credit.get(pos).getvoucherReduceAmount());
//
//            if (totalPrice.equalsIgnoreCase("0.00")){
//                totalPayableTVsc.setText(": RM 0.00");
//            }else if(!totalPrice.contains(".")) {
//                totalPayableTVsc.setText(": RM " +totalPrice+".00");
//
//                float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
//                System.out.print("GST" + rounded);
//            }else if(Float.parseFloat(totalPrice)<0.00){
//                totalPayableTVsc.setText(": RM 0.00");
//            }else{
//                totalPrice=String.format("%.2f", Double.parseDouble(totalPrice));
//                totalPayableTVsc.setText(": RM " + totalPrice);
//            }

            voucherCodeRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Remove Store Credit?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteVoucherFromCart(item.getvoucherID(), CartID);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .show();


                }
            });


//            mBookends.addFooter(footerVoucherCode1);
        }

//        mBookends.addFooter(footer);

        if(oriGiftOptionsText.length() > 0)
        {
            IsGiftOptionsEnabled = true;
            giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
            giftOptionsNOIV.setImageResource(R.drawable.btn_check);
            giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
            giftOptionsET.setVisibility(View.VISIBLE);
            giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);

            giftOptionsET.setText(oriGiftOptionsText);
        }

        if(oriLeaveMessageText.length() > 0)
        {
            IsLeaveMSGEnabled = true;
            leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
            leaveMsgNOIV.setImageResource(R.drawable.btn_check);
            leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
            leaveMsgET.setVisibility(View.VISIBLE);

            leaveMsgET.setText(oriLeaveMessageText);
        }


        mRecyclerView.setAdapter(mBookends);

        codeApplyIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalPrice.equals("0.00")){
                    Toast.makeText(getActivity(), "Your order has been fully paid", Toast.LENGTH_LONG).show();
                }else if(codeET.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You must enter a voucher code", Toast.LENGTH_LONG).show();
                }else{
                    applyVoucherWS(codeET.getText().toString(), UserID, CartID);
                }


                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
//                refreshAll();

//                oriGiftOptionsText = giftOptionsET.getText().toString();
//                oriLeaveMessageText = leaveMsgET.getText().toString();
//
//                refreshRecyclerView();

            }
        });

        leaveMsgET.setMovementMethod(new ScrollingMovementMethod());

        leaveMsgET.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                leaveMsgET.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        giftOptionsET.setMovementMethod(new ScrollingMovementMethod());

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                giftOptionsET.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });

        giftOptionsET.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                giftOptionsET.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        giftOptionsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                giftOptionsNoOfCharTV.setText(giftOptionsET.getText().length() + "/350");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getUserCartList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String loggedin= "0";
        if(UserID.equalsIgnoreCase("")){
            loggedin = "0";
        }
        else{
            loggedin = "1";
        }
        System.out.println("LOGGEDIN " +loggedin);
        String apikey =pref.getString("apikey","");
        String cartID="0";
        if (CartID.equalsIgnoreCase("")){
            cartID="0";
        }else{
            cartID=CartID;
        }
        String userID="0";
        if (UserID.equalsIgnoreCase("")){
            userID="0";
        }else{
            userID=UserID;
        }
        String action="Carts/cart/id/"+cartID+"/customer/"+userID+"/login/"+loggedin+"?apikey="+apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void applyVoucherWS(String voucherCode, String userID, String cartID){

        couponCode = voucherCode;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID+"&mobile=1";
//        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void deleteVoucherFromCart(String voucherID,String cartID){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Carts/removeVoucher";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_cart",cartID)
                .add("id_cart_rule",voucherID)
                .build();
        WebServiceAccessDelete callws = new WebServiceAccessDelete(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);

    }


    private void goToNextStepWS(String giftMessage, String leaveMessage)
    {

        final String leaveMessageFinal = leaveMessage;
        final String giftMessageFinal = giftMessage;

        if(haveOutOfStock)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setTitle("Out of stock");
            builder1.setMessage("Some of the items have sold out. These items will be removed from cart.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Proceed",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            removeOutOfStockItem(giftMessageFinal, leaveMessageFinal);


                        }
                    });

            builder1.setNegativeButton(
                    "Dismiss",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
        else {

            proceedToNextStep(giftMessageFinal, leaveMessageFinal);

        }


    }


    @Override
    public void onTaskComplete(JSONObject result) {

        if(result!=null){
            try{
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                listArray_shoppingBag.clear();
                totalAllProductPrice = 0;
                int totalquantity=0;
                String action = result.getString("action");

                if(action.equals("Carts_cart"))
                {
                    if(result.getBoolean("status"))
                    {

                        cartResultJObj = result;

                        JSONObject data = new JSONObject();
                        data=result.getJSONObject("data");
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = data.getJSONArray("product_list");
                        CartID = data.getString("id_cart");

                        sale_notice_state = data.getString("sale_delay_notice");

                        if(sale_notice_state.equals("1")){
                            if(data.has("bottom_message"))
                            {
                                bottomText = data.getString("bottom_message");
                            }
                        }




                        CartMessages = data.getString("message_type");

                        if(CartMessages.equals("alert-success"))
                        {

                            if(data.getString("cart_messages").equals(""))
                            {
                            }
                            else {

                                cartTopMessage = data.getJSONArray("cart_messages");

                            }

                        }

                        totalItemInBag = data.getString("totalItemInCart");

                        shippingPrice = data.getString("shipping_price");

                        editor.putString("CartID", CartID);
                        totalPriceWt = data.getString("totalProductsWt");
                        totalPrice = data.getString("totalPrice");
                        taxAmount = data.getString("taxCost");
                        System.out.println("siniini = " + jsonArr.length());

                        for(int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jObj = jsonArr.getJSONObject(i);
                            String productAttributeID = jObj.getString("id_product_attribute");
                            String productID = jObj.getString("id_product");
                            String productName = jObj.getString("name");
                            String productSize = jObj.getString("attributes_small");
                            if(productSize.equals("")){
                                productSize="-";
                            }
                            String price = jObj.getString("price");
                            String discountedPrice = jObj.getString("price");
                            String imageURL = jObj.getString("image_url");
                            String timeEnd = jObj.getString("time_end");
                            String timeRem = jObj.getString("time_remainder");
                            String productRef = jObj.getString("reference");
                            String quantity = jObj.getString("quantity");
                            String item_total = jObj.getString("total");
                            Integer quantity_available = jObj.getInt("quantity_available");
//
                            if(quantity_available == 0 || quantity_available < 1)
                            {
                                haveOutOfStock = true;
                                listArray_outofstockitem.add(jObj);
                            }

                            int tot=Integer.parseInt(quantity);
                            totalquantity+=tot;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            Date d1 = format.parse(current_date);
                            Date d2 = format.parse(timeEnd);
                            System.out.println("current date is " + current_date);
                            System.out.println("current timeEnd is " + timeEnd);


                            System.out.println("current timeRem is " + timeRem);
                            //in milliseconds
                            long diff = d2.getTime() - d1.getTime();
                            System.out.println("current diff is " + diff);

//                            if(diff > 0)
//                            {
                                if(!discountedPrice.equals("0"))
                                {
                                    price = discountedPrice;
                                }

                                price = String.format("%.2f", Float.parseFloat(price));


                                String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                                totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);


                                listArray_shoppingBag.add(new shoppingBagItem(CartID,productAttributeID,productID,imageURL,productName,productSize,productRef,price,item_total,quantity,timeRem,quantity_available));

//                            }


                        }
//                        ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(totalquantity));

                        editor.putString("cartItem", String.valueOf(totalquantity));
                        editor.apply();
                        listArray_voucher.clear();

                        try {
                            JSONArray voucherArray = data.getJSONArray("voucher_list");
                            for (int j = 0; j < voucherArray.length(); j++) {
                                JSONObject jObj2 = voucherArray.getJSONObject(j);
                                String voucherID = jObj2.getString("id_discount");
                                String voucher_name= jObj2.getString("name");
                                String voucherCode = jObj2.getString("code");
                                String voucherPercentage = jObj2.getString("reduction_percent");
                                String voucherAmount = jObj2.getString("value_tax_exc");

//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                                listArray_voucher.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                            }
                        }catch (Exception e)
                        {

                        }

                        listArray_store_credit.clear();

                        try {
                            JSONArray voucherArray1 = data.getJSONArray("store_credit_list");
                            for (int j = 0; j < voucherArray1.length(); j++) {
                                JSONObject jObj2 = voucherArray1.getJSONObject(j);
                                String voucherID = jObj2.getString("id_discount");
                                String voucher_name= jObj2.getString("name");
                                String voucherCode = jObj2.getString("code");
                                String voucherPercentage = jObj2.getString("reduction_percent");
                                String voucherAmount = jObj2.getString("value_real");

//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                                listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                            }
                        }catch (Exception e)
                        {

                        }
                    }
                    else
                    {
//                        ((MainActivity) getActivity()).changeToolBarBagNotiText("0");


                        totalItemInBag = "0";

                        editor.putString("cartItem", "0");
                        editor.apply();
                    }

                    refreshAll();


                }

                else if(result.getString("action").equals("Carts_removeProduct"))
                {
                    listArray_outofstockitem.clear();
                }


                else if(action.equals("Vouchers_validate"))
                {
                    if(result.getBoolean("status"))
                    {
                        String message=result.getString("message");

                        if (!message.equals("This voucher does not exists")) {
                            Toast.makeText(getActivity(), "Voucher Accepted", Toast.LENGTH_LONG).show();
                            Insider.Instance.tagEvent("coupon_used").addParameterWithString("coupon_code",couponCode).build();
                            getUserCartList();
                            getUserCartList();

                        }
                        else  {
                            Toast.makeText(getActivity(), "Invalid Voucher Code", Toast.LENGTH_LONG).show();
                            getUserCartList();
                            getUserCartList();


//                            new AlertDialog.Builder(getActivity())
//                                    .setTitle("Message")
//                                    .setMessage("Invalid Voucher Code")
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    }).show();
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        getUserCartList();
                        getUserCartList();


                    }
                    else
                    {
                        String message=result.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        getUserCartList();
                        getUserCartList();

                    }

                }
                else if(action.equals("Carts_removeVoucher"))
                {
                    if(result.getBoolean("status"))
                    {
                        String message=result.getString("message");

                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            getUserCartList();
                        getUserCartList();


                    }
                    else
                    {

                        Toast.makeText(getActivity(), "Unable to remove voucher please try again", Toast.LENGTH_LONG).show();

                    }

                }
                else if(action.equals("Carts_OrderStep1"))
                {
                    if(result.getBoolean("status"))
                    {
                        cartResultJObj1 = result;
                        JSONObject data = new JSONObject();
                        data=result.getJSONObject("data");
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = data.getJSONArray("product_list");
                        totalPriceWt = data.getString("totalProductsWt");
                        totalPrice = data.getString("totalPrice");
                        taxAmount = data.getString("taxCost");

                        Insider.Instance.tagEvent("order_start").addParameterWithString("total_price",totalPriceWt).build();

                        String nextPage = data.getString("next_page");

                        if(nextPage.equals("addressPage"))
                        {
//                            Fragment fragment = new AddressBillingFragment();
//                            FragmentManager fragmentManager = getActivity().getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                            fragmentTransaction.replace(R.id.fragmentContainer, fragment,"AddressBillingFragment");
//                            fragmentTransaction.addToBackStack(null);
//
//                            Bundle args=new Bundle();
//                            String cartResultJObjString=cartResultJObj1.toString();
//                            System.out.println("cartResultObj" + cartResultJObjString);
//                            args.putString("cartResultJObj", cartResultJObjString);
//                            fragment.setArguments(args);
//
//                            fragmentTransaction.commit();
//                            editor = pref.edit();
//                            editor.putString("NeedToGoBackToCart", "0");
//                            editor.apply();

                            if (UserID.length() == 0) {
                                Fragment fragment = new CheckOutMethodFragment();

                                Bundle args = new Bundle();
                                String cartResultJObjString1 = cartResultJObj1.toString();

                                args.putString("cartResultJObj", cartResultJObjString1);
                                fragment.setArguments(args);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

                            else {

                                Fragment fragment = new NewOrderConfirmationFragment();
                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "NewOrderConfirmationFragment");
                                fragmentTransaction.addToBackStack(null);

                                Bundle args = new Bundle();
                                String cartResultJObjString = cartResultJObj1.toString();
                                System.out.println("cartResultObj" + cartResultJObjString);
                                args.putString("cartResultJObj", cartResultJObjString);
                                args.putString("FOR_BILLING", "0");
                                fragment.setArguments(args);

                                fragmentTransaction.commit();

                                editor = pref.edit();
                                editor.putString("NeedToGoBackToCart", "0");
                                editor.apply();
                            }

                        }
                        else if(nextPage.equals("addAddressPage"))
                        {
                            Fragment fragment = new AddAdressFragment();
                            Bundle bundle = new Bundle();
                            String cartResultJObjString=cartResultJObj1.toString();
                            bundle.putString("cartResultJObj", cartResultJObjString);
                            bundle.putInt("COME_FROM_WHERE", 3);
                            bundle.putBoolean("EDIT_ADDRESS", false);
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else if(nextPage.equals("cart"))
                        {
                            // Reload current fragment
                            Fragment frg = null;
                            frg = getFragmentManager().findFragmentByTag("ShoppingBagFragment");
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                        }
                    }
                    else
                    {
                        String message = result.getString("message");
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Message")
                                .setMessage(message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
//                                        Fragment fragment = new ShoppingBagFragment();
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
//                                        fragmentTransaction.addToBackStack(null);
//                                        fragmentTransaction.commit();
                                        getUserCartList();
                                        getUserCartList();

                                    }
                                }).show();
                    }
                }

            }
            catch (Exception e){

                e.printStackTrace();

                // Prints what exception has been thrown
                System.out.println("error here"+e);
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

    private void proceedToNextStep(String giftMessage, String leaveMessage)
    {

        if(totalItemInBag.equals("0"))
        {

            System.out.println("Stay in page");

        }

        else {

            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
            String apikey =pref.getString("apikey","");
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("LeaveMessage", leaveMessage);
            editor.putString("giftMessage", giftMessage);
            editor.apply();

            String action="Carts/OrderStep1?apikey="+apikey+"&id_cart="+CartID+"&gift_message="+giftMessage;

            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

            callws.execute(action);

        }
    }

    private void removeOutOfStockItem(String giftMessage, String leaveMessage)
    {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");

        for(int i=0;i<listArray_outofstockitem.size();i++)
        {

            try {
                JSONObject obj = listArray_outofstockitem.get(i);

                String productAttributeID = obj.getString("id_product_attribute");
                String productID = obj.getString("id_product");

                String action="Carts/removeProduct";
                RequestBody formBody = new FormBody.Builder()
                        .add("apikey",apikey)
                        .add("id_cart",CartID)
                        .add("id_product", productID)
                        .add("id_product_attribute",productAttributeID)
                        .build();
                WebServiceAccessDelete callws = new WebServiceAccessDelete(getActivity(), this);
                callws.setAction(action);
                callws.execute(formBody);

            } catch (Exception e) {
                Log.e("Error", "unexpected JSON exception", e);
                // Do something to recover ... or kill the app.
            }



        }

        getUserCartList();

        proceedToNextStep(giftMessage, leaveMessage);


    }

    public void refreshAll()
    {
        try {
            oriGiftOptionsText = giftOptionsET.getText().toString();
            oriLeaveMessageText = leaveMsgET.getText().toString();
        }catch (Exception e) {
            System.out.println("prob "+e);
        }

        System.out.println("totalInBag = " + totalItemInBag);

        if(totalItemInBag.equals("0"))
        {

            shoppingBagEmptyTV.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);

            additionalTopViewText.setVisibility(View.GONE);
            additionalTopViewText2.setVisibility(View.GONE);
            additionalTopViewText3.setVisibility(View.GONE);
            bottomNext.setVisibility(View.GONE);


            refreshRecyclerView();


        }
        else
        {
            shoppingBagEmptyTV.setVisibility(View.INVISIBLE);
            bottomNext.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            refreshRecyclerView();

        }


    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void logInitiatedCheckoutEvent (String contentId, String contentType, int numItems, boolean paymentInfoAvailable, String currency, double totalPrice) {
        Bundle params = new Bundle();

        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());


        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, numItems);
        params.putInt(AppEventsConstants.EVENT_PARAM_PAYMENT_INFO_AVAILABLE, paymentInfoAvailable ? 1 : 0);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, totalPrice, params);
    }
}
