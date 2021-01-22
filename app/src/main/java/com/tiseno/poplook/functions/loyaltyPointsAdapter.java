package com.tiseno.poplook.functions;

import android.content.Context;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.LoyaltyPointsFragment;
import com.tiseno.poplook.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by billygoh on 9/11/15.
 */
public class loyaltyPointsAdapter extends RecyclerView.Adapter<loyaltyPointsAdapter.ViewHolder>{

    private static final int TYPE_LOYALTYPOINTSORDER = 0;  // Declaring Variable to Understand which View is being worked on
    private static final int TYPE_CONVERTPOINTS = 1;
    private static final int EMPTY = 2;

    ArrayList<loyaltyPointsOrderItem> order_data;
    boolean IsPointsEmpty = false;
    boolean IsVoucherEmpty = false;

    String UserID, CartID, LanguageID;

    Context mContext;
    LoyaltyPointsFragment mFragment;

//    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
//    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java
//
//    private String name;        //String Resource for header View Name
//    private int profile;        //int Resource for header view profile picture
//    private String email;       //String Resource for header view email


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView loyaltyPointsOrderTV;
        TextView loyaltyPointsOrderDateTV;
        TextView loyaltyPointsOrderStatusTV;
        TextView loyaltyPointsOrderPointsTV;
        TextView loyaltyPointsOrderPointsLblTV;
        ImageView loyaltyPointsBelowDivider2IV;

        RelativeLayout paymentNextRL;
        TextView convertBtnTextTV;

        TextView textViewSaja;

        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_LOYALTYPOINTSORDER)
            {
                loyaltyPointsOrderTV = (TextView) itemView.findViewById(R.id.loyaltyPointsOrderTV);
                loyaltyPointsOrderDateTV = (TextView) itemView.findViewById(R.id.loyaltyPointsOrderDateTV);
                loyaltyPointsOrderStatusTV = (TextView) itemView.findViewById(R.id.loyaltyPointsOrderStatusTV);
                loyaltyPointsOrderPointsTV = (TextView) itemView.findViewById(R.id.loyaltyPointsOrderPointsTV);
                loyaltyPointsOrderPointsLblTV = (TextView) itemView.findViewById(R.id.loyaltyPointsOrderPointsLblTV);
                loyaltyPointsBelowDivider2IV = (ImageView) itemView.findViewById(R.id.loyaltyPointsBelowDivider2IV);

                Holderid = 0;
            }
            else if(ViewType == TYPE_CONVERTPOINTS)
            {
                paymentNextRL = (RelativeLayout) itemView.findViewById(R.id.paymentNextRL);
                convertBtnTextTV = (TextView) itemView.findViewById(R.id.convertBtnTextTV);

                Holderid = 1;
            }else{
                textViewSaja = (TextView) itemView.findViewById(R.id.textViewSaja);

                Holderid = 2;
            }


        }


    }



    public loyaltyPointsAdapter(Context context, ArrayList<loyaltyPointsOrderItem> listArray, LoyaltyPointsFragment fragment) {

        order_data = listArray;
        mContext = context;
        mFragment = fragment;
    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_LOYALTYPOINTSORDER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loyalty_points_order_item_row, parent, false); //Inflating the layout

            ViewHolder vhOrder= new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhOrder; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_CONVERTPOINTS) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loyalty_points_convert_point_item_row,parent,false); //Inflating the layout

            ViewHolder vhConvert = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhConvert; //returning the object created


        }else{

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_voucher_empty,parent,false); //Inflating the layout

            ViewHolder vhConvert = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhConvert; //returning the object created

        }


    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(holder.Holderid == 0)
        {   holder.loyaltyPointsOrderTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.loyaltyPointsOrderStatusTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.loyaltyPointsOrderPointsTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.loyaltyPointsOrderPointsLblTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            if(order_data.size() == 0)
            {
                System.out.println("nononono");
                holder.loyaltyPointsOrderTV.setText("No loyalty points yet");
                holder.loyaltyPointsOrderPointsLblTV.setVisibility(View.INVISIBLE);
                holder.loyaltyPointsBelowDivider2IV.setVisibility(View.INVISIBLE);


            }
            else
            {
                holder.loyaltyPointsOrderPointsLblTV.setVisibility(View.VISIBLE);
                holder.loyaltyPointsBelowDivider2IV.setVisibility(View.VISIBLE);

                loyaltyPointsOrderItem item = order_data.get(position);

                holder.loyaltyPointsOrderTV.setText("Order: #" + item.getorderID());
                holder.loyaltyPointsOrderDateTV.setText(parseDate(item.getorderDate()));

                holder.loyaltyPointsOrderStatusTV.setText(item.getorderStatus());


                if(item.getorderStatus().equals("Already converted"))
                {
                    holder.loyaltyPointsOrderPointsTV.setPaintFlags(holder.loyaltyPointsOrderPointsTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.loyaltyPointsOrderPointsTV.setText(item.getloyaltyPoints());

                }
                else
                {
                    holder.loyaltyPointsOrderPointsTV.setPaintFlags( holder.loyaltyPointsOrderPointsTV.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.loyaltyPointsOrderPointsTV.setText(item.getloyaltyPoints());
                }
            }


        }
        else if(holder.Holderid == 1)
        {
            holder.convertBtnTextTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            if(order_data.size() == 0)
            {
                holder.convertBtnTextTV.setVisibility(View.VISIBLE);
                holder.convertBtnTextTV.setText("-END-");
                holder.convertBtnTextTV.setTextColor(mContext.getResources().getColor(R.color.black));


            }else {
                loyaltyPointsOrderItem item = order_data.get(0);

                System.out.println("hahahahaaaaaq " + item.getAvailableForConvert());

                if (item.getAvailableForConvert().equals("0.00")) {
                    holder.convertBtnTextTV.setVisibility(View.VISIBLE);
                    holder.convertBtnTextTV.setText("-END-");
                    holder.convertBtnTextTV.setTextColor(mContext.getResources().getColor(R.color.black));
                } else {
                    holder.convertBtnTextTV.setVisibility(View.INVISIBLE);
//                    final String text = "CONVERT MY POINTS INTO A VOUCHER WORTH RM "+item.getAvailableForConvert();
//                    mFragment.showPaymentNextBtnIB(text);
                }

            }
        }else{
            holder.textViewSaja.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_BLACK_FONT));
            holder.textViewSaja.setText("No loyalty points yet");
        }



//        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
//            // position by 1 and pass it to the holder while setting the text and image
//            holder.textView.setText(mNavTitles[position ]); // Setting the Text with the array of our Titles
////            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
//        }
//        else{
//
//            holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
//            holder.Name.setText(name);
//            holder.email.setText(email);
//        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        if(order_data.size() > 0)
        {
            return order_data.size() + 1;
        }
        else if(order_data.size() > 0)
        {
            return order_data.size() + 2;
        }
        else if(order_data.size() == 0)
        {
            return 1;
        }
        else
        {
            return 2;
        }

    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {

        if(order_data.size() > 0)
        {
            if(position < order_data.size())
            {
                return TYPE_LOYALTYPOINTSORDER;
            }
            else if(position == order_data.size())
            {
                return TYPE_CONVERTPOINTS;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return EMPTY;
        }

    }


    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yyyy HH:mm:ss";
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

}