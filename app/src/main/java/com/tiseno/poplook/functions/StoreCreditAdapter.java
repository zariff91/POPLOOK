package com.tiseno.poplook.functions;

/**
 * Created by rahn on 12/10/15.
 */

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiseno.poplook.R;

import java.util.ArrayList;

/**
 * Created by billygoh on 9/11/15.
 */
public class StoreCreditAdapter extends RecyclerView.Adapter<StoreCreditAdapter.ViewHolder> {

    private static final int TYPE_LOYALTYPOINTSVOUCHER = 2;
    private static final int EMPTY = 1;
    ArrayList<storeCreditItem> voucher_data;
    boolean IsPointsEmpty = false;
    boolean IsVoucherEmpty = false;

    String UserID, CartID, LanguageID,SelectedCountryCurrency;

    Context mContext;

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

        ImageView loyaltyPointsBelowDividerIV;
        TextView loyaltyPointsVoucherDateTV;
        TextView loyaltyPointsVoucherCodeTV;
        TextView loyaltyPointsVoucherPriceTV;
        LinearLayout loyaltyPointsVoucherInfoLL;
        TextView loyaltyPointsMoreTV;
        TextView loyaltyPointsVoucherCommaTV;
        TextView loyaltyPointsVoucherCodeLblTV;
        TextView originalAmountLabel;
        TextView totalOriginalAmountLabel;

        TextView textViewSaja;

        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            if(ViewType == TYPE_LOYALTYPOINTSVOUCHER)
            {

                loyaltyPointsVoucherDateTV = (TextView) itemView.findViewById(R.id.loyaltyPointsVoucherDateTV);
                loyaltyPointsVoucherCodeTV = (TextView) itemView.findViewById(R.id.loyaltyPointsVoucherCodeTV);
                loyaltyPointsVoucherPriceTV = (TextView) itemView.findViewById(R.id.loyaltyPointsVoucherPriceTV);
                loyaltyPointsBelowDividerIV = (ImageView) itemView.findViewById(R.id.loyaltyPointsBelowDividerIV);
                loyaltyPointsVoucherInfoLL = (LinearLayout) itemView.findViewById(R.id.loyaltyPointsVoucherInfoLL);
                loyaltyPointsVoucherCommaTV = (TextView) itemView.findViewById(R.id.loyaltyPointsVoucherCommaTV);
                loyaltyPointsVoucherCodeLblTV = (TextView) itemView.findViewById(R.id.loyaltyPointsVoucherCodeLblTV);
                originalAmountLabel = (TextView)itemView.findViewById(R.id.originalAmountLabel);
                totalOriginalAmountLabel = (TextView)itemView.findViewById(R.id.totalOriginalAmountLabel);


                Holderid = 2;
            }else{
                textViewSaja = (TextView) itemView.findViewById(R.id.textViewSaja);

                Holderid = 1;
            }




        }


    }



    public  StoreCreditAdapter(Context context, ArrayList<storeCreditItem> listArray2) {
        voucher_data = listArray2;
        mContext = context;

    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_LOYALTYPOINTSVOUCHER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_credit_item_row,parent,false); //Inflating the layout
            SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
            SelectedCountryCurrency = pref.getString("SelectedCountryCurrency", "");
            ViewHolder vhVoucher = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhVoucher; //returning the object created
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_voucher_empty,parent,false); //Inflating the layout

            ViewHolder vhVoucher = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhVoucher;
        }

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(holder.Holderid == 2)
        {
            holder.loyaltyPointsVoucherDateTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.loyaltyPointsVoucherCodeTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.loyaltyPointsVoucherPriceTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.loyaltyPointsVoucherCommaTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.loyaltyPointsVoucherCodeLblTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.originalAmountLabel.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            holder.totalOriginalAmountLabel.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
            System.out.println("voucher_data size is "+voucher_data);

            if(voucher_data.size() == 0)
            {
                holder.loyaltyPointsVoucherDateTV.setText(R.string.noSCsyet);
                holder.loyaltyPointsBelowDividerIV.setVisibility(View.GONE);
                holder.loyaltyPointsVoucherCommaTV.setVisibility(View.INVISIBLE);
                holder.loyaltyPointsVoucherCodeLblTV.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.loyaltyPointsBelowDividerIV.setVisibility(View.VISIBLE);
                holder.loyaltyPointsVoucherCommaTV.setVisibility(View.VISIBLE);
                holder.loyaltyPointsVoucherCodeLblTV.setVisibility(View.VISIBLE);

                final storeCreditItem item = voucher_data.get(position);

                holder.loyaltyPointsVoucherDateTV.setText(item.getvoucherDate());
                holder.loyaltyPointsVoucherCodeTV.setText(item.getvoucherCode());
                holder.loyaltyPointsVoucherPriceTV.setText(SelectedCountryCurrency+" " + item.getvoucherPrice());
                holder.totalOriginalAmountLabel.setText(SelectedCountryCurrency+" " +item.getOriAmount());

            }
        }else{
            holder.textViewSaja.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_BLACK_FONT));
            holder.textViewSaja.setText("No store credit yet");
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
        if(voucher_data.size() > 0)
        {
            return voucher_data.size();
        }
        else if(voucher_data.size() > 0)
        {
            return voucher_data.size();
        }
        else if(voucher_data.size() == 0)
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

        if(voucher_data.size() > 0)
        {
            return TYPE_LOYALTYPOINTSVOUCHER;
        }
        else
        {
            return EMPTY;
        }


    }



}