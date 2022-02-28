package com.tiseno.poplook.functions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class orderConfirmationBagAdapter  extends RecyclerView.Adapter<orderConfirmationBagAdapter.ViewHolder> {

    Context mContext;
    ArrayList<shoppingBagItem> data;

    public orderConfirmationBagAdapter(Context context, ArrayList<shoppingBagItem> listArray){

        mContext = context;
        data = listArray;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle,quantity;


        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            productImage = (ImageView)itemView.findViewById(R.id.cartImage);
            productTitle = (TextView)itemView.findViewById(R.id.productTitleConfirmPage);
            quantity = (TextView)itemView.findViewById(R.id.quantityProduct);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_confirm_row,parent,false); //Inflating the layout
        return new ViewHolder(adapterView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final shoppingBagItem item = data.get(position);

        Glide.with(holder.productImage)
                .load(item.getproductImg())
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
        String quantity = item.getproductQuantity();
        holder.productTitle.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productTitle.setText(title);
        holder.quantity.setText("x"+quantity);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
