package com.tiseno.poplook;


import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.EndlessScrollListenerLinearLayout;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.orderHistoryAdapter;
import com.tiseno.poplook.functions.orderHistoryItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{
    public static int index = -1;
    public static int top = -1;

    int numberPages=1;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;
    TextView orderHistoryEmptyTV;
    String UserID,LanguageID;

    ArrayList<orderHistoryItem> listArray = new ArrayList<orderHistoryItem>();


    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_order_history, container, false);
        listArray.clear();
        numberPages=1;

        ((MainActivity) getActivity()).changeToolBarText("Order History");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.orderHistroyRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        orderHistoryEmptyTV = (TextView) contentView.findViewById(R.id.orderHistoryEmptyTV);

        orderHistoryEmptyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        LanguageID = pref.getString("LanguageID", "");

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PaymentDone", "0");
        editor.putString("FromPaypalSuccess", "");
        editor.apply();

        getOrderHistory();
        mRecyclerView.addOnScrollListener(new EndlessScrollListenerLinearLayout(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                System.out.println("Heloooaskdoskd" + current_page);

                getOrderHistory();


//                    numberPages++;

            }
        });
        try{
            String FromPaypalSuccess = pref.getString("FromPaypalSuccess", "");

            if(FromPaypalSuccess.equals("1"))
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                      //Do something after 100ms
//                    }
//                }, 2000);
                reloadFragment();

            }
        }catch (Exception e){}

        return contentView;
    }

    private void getOrderHistory()
    {
        System.out.println("LALUSINIEGHLSUCCSES2");
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Orders/histories/customer/"+UserID+"/n/20/p/"+numberPages+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
        numberPages++;
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                boolean status = result.getBoolean("status");
                String Action = result.getString("action");
                if(Action.equals("Orders_histories"))
                {
                    if(status)
                    {
                        orderHistoryEmptyTV.setVisibility(View.INVISIBLE);
//                        listArray.clear();
                        JSONObject data = result.getJSONObject("data");
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = data.getJSONArray("order_histories");
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            String orderID = jObj.getString("id_order");
                            String orderDate = jObj.getString("date_add");
                            String orderStatus = jObj.getString("order_state");
                            String deliveryNo = jObj.getString("delivery_number");

                            String trackNum = jObj.getString("tracking_number");
                            String deliveryinfo = jObj.getString("carrier");


                            if(deliveryNo.equals("null")||deliveryNo.equals("0"))
                            {
                                deliveryNo = "-";
                            }

                            String cartID = jObj.getString("id_cart");

                            listArray.add(new orderHistoryItem(orderID,orderDate,orderStatus,deliveryNo,"-",cartID, trackNum, deliveryinfo));
                        }

                        RVAdapter = new orderHistoryAdapter(getActivity(),listArray,OrderHistoryFragment.this);
                        // Make Bookends
                        Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

                        index = mLayoutManager.findFirstVisibleItemPosition();
                        View v = mRecyclerView.getChildAt(0);
                        top = (v == null) ? 0 : (v.getTop() - mRecyclerView.getPaddingTop());
//            mRecyclerView.scrollToPosition(index);
                        mLayoutManager.scrollToPositionWithOffset(index, top);
                        // Inflate footer view
                        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                        LinearLayout header = (LinearLayout) inflater1.inflate(R.layout.order_history_header_view, mRecyclerView, false);
                        TextView step1TV = (TextView)header.findViewById(R.id.step1TV);
                        TextView step2TV = (TextView)header.findViewById(R.id.step2TV);

                        step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                        step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


                        mBookends.addHeader(header);
                        mRecyclerView.setAdapter(mBookends);
                    }else{
                        orderHistoryEmptyTV.setVisibility(View.VISIBLE);
                    }
                }

            }
            catch (Exception e){
                System.out.println("errorrrr "+e);
                orderHistoryEmptyTV.setVisibility(View.VISIBLE);

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

    public void reloadFragment()
    {
        // Reload current fragment
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("OrderHistoryFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        try{        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

            String FromPaypalSuccess = pref.getString("FromPaypalSuccess", "");

            if(FromPaypalSuccess.equals("1"))
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                      //Do something after 100ms
//                    }
//                }, 2000);
                getOrderHistory();

            }
        }catch (Exception e){}
    }
}
