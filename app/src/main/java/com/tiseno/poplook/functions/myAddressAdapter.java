package com.tiseno.poplook.functions;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.AddAdressFragment;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.MyAddressFragment;
import com.tiseno.poplook.R;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by billygoh on 9/9/15.
 */
public class myAddressAdapter extends RecyclerView.Adapter<myAddressAdapter.ViewHolder> implements AsyncTaskCompleteListener<JSONObject>{

    ArrayList<addressItem> data ;
    Context mContext;
    int ItemPosition;
    String apikey;
    MyAddressFragment fragment;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout myaddressAddressLblLL;
        TextView myaddressAddressLblTV;
        TextView myaddressAddressTV,deleteAddressBtnTV;
        TextView myaddressContactNoTV,myaddressContactNoLblTV;
        ImageButton myaddressEditAddressIB;
        ImageButton myaddressDeleteAddressBtnIB;
        ImageButton myaddressAddAddressBtnIB;
        TextView addressTitleTV;

        public ViewHolder(View v) {
            super(v);

            myaddressAddressLblLL = (LinearLayout) v.findViewById(R.id.myaddressAddressLblLL);
            myaddressAddressLblTV = (TextView) v.findViewById(R.id.myaddressAddressLblTV);
            myaddressAddressTV = (TextView) v.findViewById(R.id.myaddressAddressTV);
            myaddressContactNoLblTV = (TextView) v.findViewById(R.id.myaddressContactNoLblTV);
            addressTitleTV = (TextView) v.findViewById(R.id.addressTitleTV);
            myaddressContactNoTV = (TextView) v.findViewById(R.id.myaddressContactNoTV);
            myaddressEditAddressIB = (ImageButton) v.findViewById(R.id.myaddressEditAddressIB);
            myaddressDeleteAddressBtnIB = (ImageButton) v.findViewById(R.id.myaddressDeleteAddressBtnIB);
            deleteAddressBtnTV = (TextView) v.findViewById(R.id.deleteAddressBtnTV);
            myaddressAddAddressBtnIB = (ImageButton) v.findViewById(R.id.myaddressAddAddressBtnIB);

        }
    }

    public myAddressAdapter(Context context, ArrayList<addressItem> listArray, MyAddressFragment fragment1) {
        fragment=fragment1;
        data = listArray;
        mContext = context;
    }

    @Override
    public myAddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_address_item_row, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view



        return vhItem; // Returning the created object
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
        //apikey
        apikey = pref.getString("apikey", "");
        holder.myaddressAddressLblTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.myaddressAddressTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.myaddressContactNoTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.myaddressContactNoLblTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.addressTitleTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.deleteAddressBtnTV.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));

        final addressItem item = data.get(position);
        ItemPosition = position;

        String billingAddFirstName = item.getaddressFirstName();
        String billingAddLastName = item.getaddressLastName();
        String billingAdd1 = item.getaddress1();
        String billingAdd2 = item.getaddress2();
        String billingAddPostCode = item.getaddressPostCode();
        String billingAddCity = item.getaddressCity();
        String billingAddCountry = item.getaddressCountry();
        String billingAddState = item.getaddressState();
        String billingAddPhone = item.getaddressPhone();

        if (position == 0) {
            holder.myaddressAddressLblLL.setVisibility(View.VISIBLE);
        } else {
            holder.myaddressAddressLblLL.setVisibility(View.GONE);
        }

        int no = position + 1;

        holder.myaddressAddressLblTV.setText("My Address " + no);

        if (billingAdd2 != null && !billingAdd2.isEmpty() && !billingAdd2.equals("null")) {

            holder.myaddressAddressTV.setText(billingAddFirstName + " " + billingAddLastName + "\n" + billingAdd1 + "\n" + billingAdd2 + "\n" + billingAddPostCode + " " + billingAddCity + "\n" + billingAddState + "\n" + billingAddCountry);

        }

        else

            {

                holder.myaddressAddressTV.setText(billingAddFirstName + " " + billingAddLastName + "\n" + billingAdd1 + " " + billingAdd2 + "\n" + billingAddPostCode + " " + billingAddCity + "\n" + billingAddState + "\n" + billingAddCountry);

            }


        holder.myaddressContactNoTV.setText(billingAddPhone);

        holder.myaddressAddAddressBtnIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddAddressFragment();
            }
        });

        holder.myaddressEditAddressIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddAdressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("COME_FROM_WHERE", 0);
                bundle.putBoolean("EDIT_ADDRESS", true);

                bundle.putString("ADD_ID", item.getaddressID());
                bundle.putString("ID_GENDER", item.getId_gender());
                bundle.putString("FIRST_NAME", item.getaddressFirstName());
                bundle.putString("LAST_NAME", item.getaddressLastName());
                bundle.putString("COMPANY",item.getCompany());
                bundle.putString("ADD1",item.getaddress1());
                bundle.putString("ADD2",item.getaddress2());
                bundle.putString("COUNTRY_NAME",item.getaddressCountry());
                bundle.putString("COUNTRY_ID",item.getaddressCountryID());
                bundle.putString("STATE_NAME",item.getaddressState());
                bundle.putString("STATE_ID", item.getaddressStateID());
                bundle.putString("CITY_NAME", item.getaddressCity());
                bundle.putString("POSTCODE_NAME", item.getaddressPostCode());
                bundle.putString("PHONE", item.getaddressPhone());

                fragment.setArguments(bundle);
                switchContent(R.id.fragmentContainer, fragment);
            }
        });

        holder.myaddressDeleteAddressBtnIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Message")
                        .setMessage("Delete this address")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteAddress(item.getaddressID());

                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void toAddAddressFragment() {
        AddAdressFragment fragment = new AddAdressFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("COME_FROM_WHERE", 0);
        bundle.putBoolean("EDIT_ADDRESS", false);
        fragment.setArguments(bundle);

        switchContent(R.id.fragmentContainer, fragment);
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

    public void switchContent2(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent2(id, frag);
        }

    }

    private void DeleteAddress(String addressID)
    {
        String action="Addresses/deleteAddress";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_address",addressID)
                .build();
        WebServiceAccessDelete callws = new WebServiceAccessDelete(mContext, this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                String action = result.getString("action");

                if(action.equals("Addresses_deleteAddress"))
                {
                    if(result.getBoolean("status"))
                    {
                        Toast toast = Toast.makeText(mContext,
                                "Address Deleted", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                        fragment.getAddressList();
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
