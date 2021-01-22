package com.tiseno.poplook.functions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiseno.poplook.R;

import java.util.ArrayList;

public class InStoreAdapter extends RecyclerView.Adapter<InStoreAdapter.ViewHolder> {


    Context context;

    ArrayList<StoreListItem> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView storeTitle, storeAddress;

        public ViewHolder(View v) {
            super(v);

            storeTitle = (TextView) v.findViewById(R.id.store_Title);
//            productTimeLeftTV = (TextView) v.findViewById(R.id.productTimeLeftTV);
            storeAddress = (TextView) v.findViewById(R.id.store_Address);

        }
    }

    public InStoreAdapter(Context context1,ArrayList<StoreListItem> listArray) {

        context = context1;
        data = listArray;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.in_store_row,viewGroup,false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view



        return vhItem; // Returning the created object

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.storeTitle.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.storeAddress.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));

        final StoreListItem storeItem = data.get(position);

        holder.storeAddress.setText(storeItem.getstore_Address());
        holder.storeTitle.setText(storeItem.getstore_Title());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
