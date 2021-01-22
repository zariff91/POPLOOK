package com.tiseno.poplook.functions;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.OrderDetailsFragment;
import com.tiseno.poplook.OrderHistoryFragment;
import com.tiseno.poplook.PaymentFragment;
import com.tiseno.poplook.R;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPost;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by billygoh on 9/10/15.
 */
public class orderHistoryAdapter extends RecyclerView.Adapter<orderHistoryAdapter.ViewHolder> implements AsyncTaskCompleteListener<JSONObject>{

    ArrayList<orderHistoryItem> data ;
    Context mContext;
    String cartID,apikey;
    OrderHistoryFragment mFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView orderhistoryGoToIV;
        TextView OrderNoLabel,orderhistoryOrderNoTV;
        TextView DateLabel,orderhistoryDateTV;
        TextView StatusLabel,orderhistoryStatusTV;
        TextView TrackingLabel,orderhistoryTrackingNoTV;
        TextView DeliveryLabel,orderhistoryDeliveryInfoTV;
        TextView payNowTV,cancelTV;
        RelativeLayout orderhistoryIncompleteRL;
        ImageButton orderhistoryPayNowBtnIV;
        ImageButton orderhistoryCancelBtnIV;

        LinearLayout orderhistoryItemLL;

        public ViewHolder(View v) {
            super(v);

            orderhistoryGoToIV = (ImageView) v.findViewById(R.id.orderhistoryGoToIV);
            orderhistoryOrderNoTV = (TextView) v.findViewById(R.id.orderhistoryOrderNoTV);
            orderhistoryDateTV = (TextView) v.findViewById(R.id.orderhistoryDateTV);
            orderhistoryStatusTV = (TextView) v.findViewById(R.id.orderhistoryStatusTV);
            orderhistoryTrackingNoTV = (TextView) v.findViewById(R.id.orderhistoryTrackingNoTV);
            orderhistoryDeliveryInfoTV = (TextView) v.findViewById(R.id.orderhistoryDeliveryInfoTV);

            OrderNoLabel = (TextView) v.findViewById(R.id.OrderNoLabel);
            DateLabel = (TextView) v.findViewById(R.id.DateLabel);
            StatusLabel = (TextView) v.findViewById(R.id.StatusLabel);
            TrackingLabel = (TextView) v.findViewById(R.id.TrackingLabel);
            DeliveryLabel = (TextView) v.findViewById(R.id.DeliveryLabel);

            payNowTV = (TextView) v.findViewById(R.id.payNowTV);
            cancelTV = (TextView) v.findViewById(R.id.cancelTV);

            orderhistoryIncompleteRL = (RelativeLayout) v.findViewById(R.id.orderhistoryIncompleteRL);
            orderhistoryPayNowBtnIV = (ImageButton) v.findViewById(R.id.orderhistoryPayNowBtnIV);
            orderhistoryCancelBtnIV = (ImageButton) v.findViewById(R.id.orderhistoryCancelBtnIV);

            orderhistoryItemLL = (LinearLayout) v.findViewById(R.id.orderhistoryItemLL);
        }
    }

    public orderHistoryAdapter(Context context, ArrayList<orderHistoryItem> listArray, OrderHistoryFragment fragment) {

        data = listArray;
        mContext = context;
        mFragment = fragment;
    }

    @Override
    public orderHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item_row, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view



        return vhItem; // Returning the created object
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
        //apikey
        apikey =pref.getString("apikey","");
        holder.orderhistoryOrderNoTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.orderhistoryDateTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.orderhistoryStatusTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.orderhistoryTrackingNoTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.orderhistoryDeliveryInfoTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));


        holder.OrderNoLabel.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_BLACK_FONT));
        holder.DateLabel.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.StatusLabel.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.TrackingLabel.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.DeliveryLabel.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));

        holder.cancelTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.payNowTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));

        final orderHistoryItem item = data.get(position);

        holder.orderhistoryItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("this is pos "+position);
                fragmentJump(item.getorderNo());
            }
        });

        cartID = item.getcartID();

        holder.orderhistoryOrderNoTV.setText("# " + item.getorderNo());
        holder.orderhistoryDateTV.setText(item.getorderDate());
        holder.orderhistoryStatusTV.setText(item.getorderStatus());
        holder.orderhistoryTrackingNoTV.setText(item.getTrackingNo());
        holder.orderhistoryDeliveryInfoTV.setText(item.getDeliveyInfo());

        if(item.getorderStatus().contains("KIV") || item.getorderStatus().contains("accepted") || item.getorderStatus().contains("success") || item.getorderStatus().contains("complete")|| item.getorderStatus().contains("canceled"))
        {
            holder.orderhistoryIncompleteRL.setVisibility(View.GONE);
        }
        else
        {
            holder.orderhistoryIncompleteRL.setVisibility(View.VISIBLE);
            holder.orderhistoryPayNowBtnIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOrderDetailsToPayNow(item.getcartID());
                }
            });

            holder.orderhistoryCancelBtnIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Message")
                            .setMessage("Confirm to cancel this order?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelOrderHistory(item.getorderNo());

                                }

                            })
                            .setNegativeButton("Cancel", null)
                            .show();


                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void fragmentJump(String orderID) {
        OrderDetailsFragment odf = new OrderDetailsFragment();
        System.out.println("orderid is "+orderID);
        Bundle bundle = new Bundle();
        bundle.putString("orderID",orderID);
        odf.setArguments(bundle);
//        mBundle = new Bundle();
//        mBundle.putParcelable("item_selected_key", mItemSelected);
//        mFragment.setArguments(mBundle);
        switchContent(R.id.fragmentContainer, odf);
    }

    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

    private void getOrderDetailsToPayNow(String cartID)
    {
        String action="Orders/PayNowFromOrder?apikey="+apikey+"&id_cart="+cartID;

        WebServiceAccessGet callws = new WebServiceAccessGet(mContext, this);

        callws.execute(action);
    }

    private void cancelOrderHistory(String orderID)
    {
        String action="Orders/cancel";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_order",orderID)
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(mContext, this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                String action = result.getString("action");

                if(action.equals("Orders_PayNowFromOrder"))
                {
                    if(result.getBoolean("status"))
                    {
                        PaymentFragment odf = new PaymentFragment();
                        Bundle bundle = new Bundle();
                        String cartResultJObjString = result.toString();
                        bundle.putString("cartResultJObj",cartResultJObjString);
                        bundle.putString("fromOrderHistory","0");
//                        bundle.putString("cartID",cartID);
//
//                        JSONObject orderHistroyJObj = result.getJSONObject("order_history");
//                        bundle.putString("cartResultJObj", result.toString());
//
//
//                        String orderCarrier = orderHistroyJObj.getString("carrier");
//
//                        bundle.putString("carrier", orderCarrier);
//
//
//
//
////                        try {
////                            JSONObject shippingObj = orderHistroyJObj.getJSONObject("shipping_details");
////                            JSONObject billingObj = orderHistroyJObj.getJSONObject("billing_details");
////
////                            String shipping_addressFirstName = shippingObj.getString("firstname");
////                            String shipping_addressLastName = shippingObj.getString("lastname");
////                            String shipping_address1 = shippingObj.getString("address1");
////                            String shipping_address2 = shippingObj.getString("address2");
////                            String shipping_addressPostCode = shippingObj.getString("postcode");
////                            String shipping_addressCity = shippingObj.getString("city");
////                            String shipping_addressCountry = shippingObj.getString("country");
////                            String shipping_addressState = shippingObj.getString("state");
////                            String shipping_addressPhone = shippingObj.getString("phone");
////
////                            bundle.putString("shipping_addressFirstName", shipping_addressFirstName);
////                            bundle.putString("shipping_addressLastName", shipping_addressLastName);
////                            bundle.putString("shipping_address1", shipping_address1);
////                            bundle.putString("shipping_address2", shipping_address2);
////                            bundle.putString("shipping_addressPostCode", shipping_addressPostCode);
////                            bundle.putString("shipping_addressCity", shipping_addressCity);
////                            bundle.putString("shipping_addressCountry", shipping_addressCountry);
////                            bundle.putString("shipping_addressState", shipping_addressState);
////                            bundle.putString("shipping_addressPhone", shipping_addressPhone);
////
////                            String billing_addressFirstName = billingObj.getString("firstname");
////                            String billing_addressLastName = billingObj.getString("lastname");
////                            String billing_address1 = billingObj.getString("address1");
////                            String billing_address2 = billingObj.getString("address2");
////                            String billing_addressPostCode = billingObj.getString("postcode");
////                            String  billing_addressCity = billingObj.getString("city");
////                            String billing_addressCountry = billingObj.getString("country");
////                            String billing_addressState = billingObj.getString("state");
////                            String billing_addressPhone = billingObj.getString("phone");
////
////                            bundle.putString("billing_addressFirstName", billing_addressFirstName);
////                            bundle.putString("billing_addressLastName", billing_addressLastName);
////                            bundle.putString("billing_address1", billing_address1);
////                            bundle.putString("billing_address2", billing_address2);
////                            bundle.putString("billing_addressPostCode", billing_addressPostCode);
////                            bundle.putString("billing_addressCity", billing_addressCity);
////                            bundle.putString("billing_addressCountry", billing_addressCountry);
////                            bundle.putString("billing_addressState", billing_addressState);
////                            bundle.putString("billing_addressPhone", billing_addressPhone);
////
////
////                        }catch (Exception e){
////
////                        }
//////
////                        //Get product list in bag
////                        try {
//////                            JSONArray jsonArr = new JSONArray();
//////                            jsonArr = orderHistroyJObj.getJSONArray("product_details");
////
////
////                        }catch (Exception e){}
//
//
//
////                        //Get voucher list
////                        try {
////
////                            JSONArray voucherArray = orderHistroyJObj.getJSONArray("discount_details");
////
////                            bundle.putString("discount_details", voucherArray.toString());
////
////                        }catch (Exception e){}
//
                        odf.setArguments(bundle);
                        switchContent(R.id.fragmentContainer, odf);


                    }else{
                        String message = result.getString("message");

                        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();

                    }



                }
                else if(action.equals("Orders_cancel"))
                {
                    if(result.getBoolean("status"))
                    {
                        Toast.makeText(mContext,"Order successfully cancelled",Toast.LENGTH_LONG).show();
                        mFragment.reloadFragment();
                    }else{
                        String message = result.getString("message");
                        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
//                        mFragment.reloadFragment();
                    }
                }


            }
            catch (Exception e){

            }

        }
        else{

            new AlertDialog.Builder(mContext)
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
