package com.tiseno.poplook;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.tiseno.poplook.functions.EndlessScrollListenerLinearLayout;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.MyLinearLayoutManager;
import com.tiseno.poplook.functions.MyVoucherAdapter;
import com.tiseno.poplook.functions.loyaltyPointsAdapter;
import com.tiseno.poplook.functions.loyaltyPointsOrderItem;
import com.tiseno.poplook.functions.loyaltyPointsVoucherItem;
import com.tiseno.poplook.functions.voucherGeneratedFromRVAdapter;
import com.tiseno.poplook.functions.voucherGeneratedFromRVItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPut;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoyaltyPointsFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{
    public static int index = -1;
    public static int top = -1;

    int numberPages=1,loyalty_page;

    String UserID, CartID, LanguageID,pointValue="0.00";

    RecyclerView mRecyclerView,voucherGeneratedFromRV;
    LinearLayoutManager mLayoutManager,mLayoutManager1;
    RecyclerView.Adapter RVAdapter;

    RelativeLayout voucherGeneratedFromRL,voucherGeneratedFromRL1,paymentNextRL;
    TextView convertBtnTextTV,voucherGeneratedFromTV1;
    ImageButton loyaltyPointsOrderCloseIV,paymentNextBtnIB;
    private Handler mHandler = new Handler();
    ArrayList<loyaltyPointsOrderItem> listArray = new ArrayList<loyaltyPointsOrderItem>();
    ArrayList<loyaltyPointsVoucherItem> listArray2 = new ArrayList<loyaltyPointsVoucherItem>();

    boolean atMyVoucher=true;
    public LoyaltyPointsFragment() {
        // Required empty public constructor
    }

    String SelectedCountryCurrency;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_loyalty_points, container, false);

        ((MainActivity) getActivity()).changeToolBarText("My Vouchers");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "");
        LanguageID = pref.getString("LanguageID", "");
        atMyVoucher = pref.getBoolean("atMyVoucher", true);
        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.loyaltyPointsOrderRV);
        voucherGeneratedFromRV  = (RecyclerView) contentView.findViewById(R.id.voucherGeneratedFromRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager1 = new MyLinearLayoutManager(getActivity(),getResources().getConfiguration().orientation,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        voucherGeneratedFromRV.setLayoutManager(mLayoutManager1);

        voucherGeneratedFromRL = (RelativeLayout) contentView.findViewById(R.id.voucherGeneratedFromRL);
        voucherGeneratedFromRL1  = (RelativeLayout) contentView.findViewById(R.id.voucherGeneratedFromRL1);
        paymentNextRL = (RelativeLayout) contentView.findViewById(R.id.paymentNextRL);
        voucherGeneratedFromTV1 = (TextView) contentView.findViewById(R.id.voucherGeneratedFromTV1);
        convertBtnTextTV  = (TextView) contentView.findViewById(R.id.convertBtnTextTV);
        convertBtnTextTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        loyaltyPointsOrderCloseIV = (ImageButton) contentView.findViewById(R.id.loyaltyPointsOrderCloseIV);
        paymentNextBtnIB = (ImageButton) contentView.findViewById(R.id.paymentNextBtnIB);

        voucherGeneratedFromTV1.setPaintFlags(voucherGeneratedFromTV1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        getLoyaltyPoints();
        mRecyclerView.addOnScrollListener(new EndlessScrollListenerLinearLayout(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                System.out.println("Heloooaskdoskd" + current_page);
                if(numberPages>loyalty_page){

                }else {
                    getLoyaltyPoints();
                }

//                    numberPages++;

            }
        });
        return contentView;
    }

    private void getLoyaltyPoints()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="LoyaltyPoints/list/customer/"+UserID+"/lang/"+LanguageID+"/n/10/p/"+numberPages+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
        numberPages++;

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                String Action = result.getString("action");
                if(Action.equals("LoyaltyPoints_list"))
                {
                    if(result.getBoolean("status")) {
//                        listArray.clear();
//                        listArray2.clear();

                        JSONArray jsonArr = new JSONArray();
                        JSONArray jsonArr2 = new JSONArray();
                        JSONObject data=result.getJSONObject("data");
                        loyalty_page =Integer.parseInt(data.getString("loyalty_max_page"))+1;
                        jsonArr = data.getJSONArray("loyalty_points");
                        jsonArr2 = data.getJSONArray("discount_lists");
                        Double AvailableForConvert = data.getDouble("loyalty_availableForConvert");
                        if (!atMyVoucher) {
                            try {
                                if (AvailableForConvert==0||AvailableForConvert==0.0||AvailableForConvert==0.00) {
                                    final String text = "YOU HAVE NO LOYALTY POINTS TO CONVERT AT THE MOMENT";
                                    showPaymentNextBtnIB(text);
                                } else {
                                    final String text = "CONVERT MY POINTS INTO A VOUCHER WORTH "+SelectedCountryCurrency+" "+ AvailableForConvert;
                                    showPaymentNextBtnIB(text);
                                }
//                                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
//                                SharedPreferences.Editor editor = pref.edit();
//
//                                editor.putString("pointValue", AvailableForConvert);
//                                editor.commit();
                                pointValue=String.valueOf(AvailableForConvert);
                            } catch (Exception e) {
                            }
                        } else {

                        }
                        if (jsonArr.length() == 0 || jsonArr.isNull(0)) {
                            listArray.clear();
                        } else {
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jObj = jsonArr.getJSONObject(i);

                                String orderID = jObj.getString("id");
                                String orderDate = jObj.getString("date");
                                int points = jObj.getInt("points");
                                String loyaltyIdState = jObj.getString("id_loyalty_state");
                                String orderStatus = jObj.getString("state");

                                listArray.add(new loyaltyPointsOrderItem(orderID, orderDate, orderStatus, String.valueOf(points), String.valueOf(AvailableForConvert)));
                            }
                        }

                        if(numberPages>2){


                        }else {
                            for (int j = 0; j < jsonArr2.length(); j++) {
                                JSONObject jObj = jsonArr2.getJSONObject(j);
                                JSONArray jsonArr3 = new JSONArray();

                                try {
                                    String code = jObj.getString("code");
                                    String date_from = parseDate(jObj.getString("date_from"));
                                    String date_to = parseDate(jObj.getString("date_to"));
                                    String reduction_amount = jObj.getString("reduction_amount");
                                    String availability = jObj.getString("voucher_available");
                                    jsonArr3 = jObj.getJSONArray("orders");

                                    String date = date_from + " - " + date_to;
                                    if(availability.equalsIgnoreCase("1")) {
                                        listArray2.add(new loyaltyPointsVoucherItem(code, date, reduction_amount, availability, jsonArr3));
                                    }

                                } catch (Exception e) {
                                    System.out.println("errorrrrr " + e);

                                }
                            }
                        }

                        if(!atMyVoucher) {
                            refreshRecyclerView();
                        }else{
                            refreshRecyclerView1();
                        }
                    }
                    else
                    {
                        if(!atMyVoucher) {
                            refreshRecyclerView();
                        }else{
                            refreshRecyclerView1();
                        }
                    }

                }else if(Action.equals("LoyaltyPoints_covertPoints"))
                {
                    String message = result.getString("message");

                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    atMyVoucher=true;
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    pointValue="0.00";
                    editor.putBoolean("atMyVoucher", atMyVoucher);
                    editor.apply();
                    reloadFragment();
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

    private void refreshRecyclerView()
    {
        RVAdapter = new loyaltyPointsAdapter(getActivity(),listArray, LoyaltyPointsFragment.this);

        // Make Bookends
        Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

        index = mLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - mRecyclerView.getPaddingTop());
//            mRecyclerView.scrollToPosition(index);
        mLayoutManager.scrollToPositionWithOffset(index, top);

        // Inflate footer view
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

        RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.loyalty_points_header_view, mRecyclerView, false);
        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.loyalty_points_footer_view, mRecyclerView, false);
        TextView step1TV = (TextView)header.findViewById(R.id.step1TV);
        TextView step2TV = (TextView)header.findViewById(R.id.step2TV);

        step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        Button myVoucherIB = (Button)header.findViewById(R.id.myVoucherIB);
        Button loyaltyPointIB = (Button)header.findViewById(R.id.loyaltyPointIB);
        loyaltyPointIB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        myVoucherIB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        myVoucherIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atMyVoucher=true;
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putBoolean("atMyVoucher", atMyVoucher);
                editor.apply();
                refreshRecyclerView1();
                voucherGeneratedFromRL.setVisibility(View.GONE);
                voucherGeneratedFromRL.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                voucherGeneratedFromRL1.setVisibility(View.GONE);
                paymentNextRL.setVisibility(View.GONE);
            }
        });

        mBookends.addHeader(header);
        mBookends.addFooter(footer);

        mRecyclerView.setAdapter(mBookends);
    }

    private void refreshRecyclerView1()
    {
        RVAdapter = new MyVoucherAdapter(getActivity(),listArray2, LoyaltyPointsFragment.this);

        // Make Bookends
        Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

        index = mLayoutManager1.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - mRecyclerView.getPaddingTop());
//            mRecyclerView.scrollToPosition(index);
        mLayoutManager1.scrollToPositionWithOffset(index, top);

        // Inflate footer view
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

        RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.loyalty_points_header_view2, mRecyclerView, false);
        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.loyalty_points_footer_view, mRecyclerView, false);
        TextView step1TV = (TextView)header.findViewById(R.id.step1TV);
        TextView step2TV = (TextView)header.findViewById(R.id.step2TV);

        step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        Button myVoucherIB = (Button)header.findViewById(R.id.myVoucherIB);
        Button loyaltyPointIB = (Button)header.findViewById(R.id.loyaltyPointIB);
        loyaltyPointIB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        myVoucherIB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        loyaltyPointIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atMyVoucher = false;
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putBoolean("atMyVoucher", atMyVoucher);
                editor.commit();
                refreshRecyclerView();

                voucherGeneratedFromRL.setVisibility(View.GONE);
                voucherGeneratedFromRL.setBackgroundResource(R.drawable.message_orders);
                voucherGeneratedFromRL1.setVisibility(View.GONE);
                paymentNextRL.setVisibility(View.GONE);
                if (!pointValue.equals("") && !pointValue.equals("0.00") &&!pointValue.equals("0") &&!pointValue.equals("0.0")) {
                    final String text = "CONVERT MY POINTS INTO A VOUCHER WORTH "+SelectedCountryCurrency+" "+ pointValue;
                    showPaymentNextBtnIB(text);
                } else if (pointValue.equals("0.00")||pointValue.equals("0")||pointValue.equals("0.0")) {
                    final String text = "YOU HAVE NO LOYALTY POINTS TO CONVERT AT THE MOMENT";
                    showPaymentNextBtnIB(text);
                }
            }
        });

        mBookends.addHeader(header);
        mBookends.addFooter(footer);

        mRecyclerView.setAdapter(mBookends);
    }

    public void showGeneratedFromView(ArrayList<voucherGeneratedFromRVItem> list1)
    {
        System.out.println("List 1" + list1);
        voucherGeneratedFromRL.setVisibility(View.VISIBLE);
        voucherGeneratedFromRL.setBackgroundResource(R.drawable.message_orders);
        voucherGeneratedFromRL1.setVisibility(View.VISIBLE);
        paymentNextRL.setVisibility(View.GONE);
        voucherGeneratedFromRV.setAdapter(new voucherGeneratedFromRVAdapter(list1,getActivity()));
        loyaltyPointsOrderCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voucherGeneratedFromRL.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void showPaymentNextBtnIB(String text)
    {
        voucherGeneratedFromRL.setVisibility(View.VISIBLE);
        voucherGeneratedFromRL.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
        voucherGeneratedFromRL1.setVisibility(View.GONE);
        paymentNextRL.setVisibility(View.VISIBLE);
        convertBtnTextTV.setText(text);
        if(text.equals("YOU HAVE NO LOYALTY POINTS TO CONVERT AT THE MOMENT")){
            convertBtnTextTV.setTextColor(getResources().getColor(R.color.grey));
        }else{
            convertBtnTextTV.setTextColor(getResources().getColor(R.color.white));
            paymentNextBtnIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Are you sure you want to convert your points into voucher?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ConvertPointsIntoVoucher();
                                }

                            })
                            .setNegativeButton("Cancel", null)
                            .show();


                }
            });
        }


    }

    public void reloadFragment()
    {
        // Reload current fragment
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("LoyaltyPointsFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void ConvertPointsIntoVoucher()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        LanguageID = pref.getString("LanguageID", "1");
        String apikey =pref.getString("apikey","");
        String action="LoyaltyPoints/covertPoints";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_customer",UserID)
                .build();
        WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            // Do some stuff that you want to do here
            reloadFragment();
            // You could do this call if you wanted it to be periodic:
            mHandler.postDelayed(this, 2000 );

        }
    };
}
