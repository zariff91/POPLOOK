package com.tiseno.poplook.functions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tiseno.poplook.R;

import java.util.ArrayList;

public class GiftWrapAdapter extends RecyclerView.Adapter<GiftWrapAdapter.ViewHolder>{

    Context mContext;
    ArrayList<shoppingBagItem> data;

    public GiftWrapAdapter(Context context, ArrayList<shoppingBagItem> listArray){

        mContext = context;
        data = listArray;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView giftProductTitle;


        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            productImage = (ImageView)itemView.findViewById(com.tiseno.poplook.R.id.giftImage);
            giftProductTitle = (TextView)itemView.findViewById(com.tiseno.poplook.R.id.giftWrapItemTitle);
        }
    }

    @NonNull
    @Override
    public GiftWrapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_wrap_layout,parent,false); //Inflating the layout
        return new GiftWrapAdapter.ViewHolder(adapterView);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftWrapAdapter.ViewHolder holder, int position) {

        final shoppingBagItem item = data.get(position);


        String imgURL = item.getproductImg().replace("\\","");

        System.out.println("macib 4 = " + imgURL);


        Glide.with(holder.productImage)
                .load("https://poplook.com/22610-146497/gift-wrap-drawstring-bag.jpg")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        holder.loadingIcon.setVisibility(View.VISIBLE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.loadingIcon.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.productImage);

        String title = item.getproductName();
        holder.giftProductTitle.setText("Add Gift Wrap -RM 10.00");
        holder.giftProductTitle.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
//        holder.productTitle.setText(title);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

