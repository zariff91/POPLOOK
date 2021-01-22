package com.tiseno.poplook.functions;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiseno.poplook.R;

import java.util.ArrayList;

/**
 * Created by rahn on 1/19/16.
 */
public class voucherGeneratedFromRVAdapter extends RecyclerView.Adapter<voucherGeneratedFromRVAdapter.ViewHolder> {

    ArrayList<voucherGeneratedFromRVItem> data ;

    Context context;
    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView voucherGeneratedFromTV;

        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            voucherGeneratedFromTV = (TextView) itemView.findViewById(R.id.voucherGeneratedFromTV);

        }
    }






    public voucherGeneratedFromRVAdapter(ArrayList<voucherGeneratedFromRVItem> data1,Context context1){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we

        data=data1; //here we assign those passed values to the values we declared here
        context=context1;
        //in adapter



    }

    @Override
    public int getItemCount() {
        return data.size(); // the number of items in the list will be +1 the titles including the header view.
    }

    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_generated_row,parent,false); //Inflating the layout
        return new ViewHolder(v); // Returning the created object

        //inflate your layout and pass it to view holder

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        //multishop
        String SelectedShopID  = pref.getString("SelectedShopID", "1");
        String SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");
        holder.voucherGeneratedFromTV.setText("Order #" + data.get(position).getId_order() + "("+SelectedCountryCurrency+data.get(position).getPrice1()+"): " + data.get(position).getPoints() + " points.");
        // Similarly we set the resources for header view

    }

}
