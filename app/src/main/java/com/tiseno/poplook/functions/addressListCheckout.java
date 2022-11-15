package com.tiseno.poplook.functions;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tiseno.poplook.AddAdressFragment;
import com.tiseno.poplook.AddressBillingFragment;
import com.tiseno.poplook.ChangeEditAddressList;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.ProductInfoFragment;
import com.tiseno.poplook.R;

import java.util.ArrayList;

public class addressListCheckout extends RecyclerView.Adapter<addressListCheckout.ViewHolder> {

    ArrayList<addressItem> data;

    ChangeEditAddressList fragment;


    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView addressTV,editAddressTV;


        public ViewHolder(View v) {
            super(v);

            addressTV = (TextView) v.findViewById(R.id.addressText);
            editAddressTV = (TextView) v.findViewById(R.id.editaddressTV);

        }
    }

    public addressListCheckout(Context context1, ArrayList<addressItem> listArray, ChangeEditAddressList fragment1)
    {
        data = listArray;
        context = context1;
        fragment = fragment1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_address_list,parent,false); //Inflating the layout

        ViewHolder v = new ViewHolder(view);

        return v;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final addressItem item = data.get(position);

        final String fullAddress = ""+item.getaddress1()+", "+item.getaddress2()+", "+item.getaddressPostCode()+", "+item.getaddressCity()+", "+item.getaddressCountry()+".";


        holder.addressTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.editAddressTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));

        holder.addressTV.setText(fullAddress);

       final String fulladd = item.getaddress1() + " " + item.getaddress2() + "\n" + item.getaddressPostCode() + " " + item.getaddressCity() + "\n" + item.getaddressState() + "\n" + item.getaddressCountry();

        final String fullName = item.getaddressFirstName() + " " + item.getaddressLastName();

                holder.addressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.updateAddress(position);
            }
        });

        holder.editAddressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               fragmentJump(position);
//                System.out.println("country name is " + billingAddItem.getaddressCountry());
            }
        });

    }

    private void fragmentJump(int position) {

        final addressItem item = data.get(position);

        Fragment fragment = new AddAdressFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("COME_FROM_WHERE", 1);
        bundle.putBoolean("EDIT_ADDRESS", true);

        bundle.putString("ADD_ID", item.getaddressID());
        bundle.putString("FIRST_NAME", item.getaddressFirstName());
        bundle.putString("LAST_NAME", item.getaddressLastName());
        bundle.putString("COMPANY","");
        bundle.putString("ADD1",item.getaddress1());
        bundle.putString("ADD2",item.getaddress2());
        bundle.putString("COUNTRY_NAME",item.getaddressCountry());
        bundle.putString("COUNTRY_ID",item.getaddressCountryID());
        bundle.putString("STATE_NAME",item.getaddressState());
        bundle.putString("STATE_ID",item.getaddressStateID());
        bundle.putString("CITY_NAME",item.getaddressCity());
        bundle.putString("POSTCODE_NAME",item.getaddressPostCode());
        bundle.putString("PHONE", item.getaddressPhone());
        fragment.setArguments(bundle);

        switchContent(R.id.fragmentContainer, fragment);
    }

    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
