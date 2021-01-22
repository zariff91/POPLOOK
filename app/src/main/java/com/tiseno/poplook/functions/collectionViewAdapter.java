package com.tiseno.poplook.functions;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiseno.poplook.R;

public class collectionViewAdapter extends RecyclerView.Adapter <collectionViewAdapter.ViewHolder> {


    Context context;



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView collectionName;

        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            collectionName = (TextView)itemView.findViewById(R.id.collection_text);


        }
    }

    public collectionViewAdapter(Context context1){

        context = context1;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_row, parent, false); //Inflating the layout

            return new collectionViewAdapter.ViewHolder(v);

    }

    @Override
    public int getItemCount() {
        return 3; // the number of items in the list will be +1 the titles including the header view.
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(final collectionViewAdapter.ViewHolder holder, int position) {
    }



}
