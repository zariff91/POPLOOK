package com.tiseno.poplook.functions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class GridViewProductAdapter extends RecyclerView.Adapter<GridViewProductAdapter.ViewHolder> {

    private ItemClickListener mListener;

    ArrayList<ProductListItem> productObj = new ArrayList<ProductListItem>();

    Context context;

    ImageLoader imageLoader = ImageLoader.getInstance();

    String currency;

    String productOutOfStock,discountAvailability,discountText,onlineExclusive;

    public GridViewProductAdapter(Context mContext, ArrayList<ProductListItem> product,ItemClickListener itemClickListener,String selectedCurrency){
        productObj = product;
        mListener = itemClickListener;
        currency = selectedCurrency;
        this.context = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_list_product_layout, parent, false); //Inflating the layout
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(productObj.get(position).isWishlist){
            holder.wishlistBtn.setBackgroundResource(R.drawable.ic_love2_18dp);
        }
        else {
            holder.wishlistBtn.setBackgroundResource(R.drawable.ic_love_18dp);
        }

        productOutOfStock = productObj.get(position).getout_of_stock();

        if (productOutOfStock.equals("0")) {
            discountAvailability = productObj.get(position).getDiscount();
            onlineExclusive = productObj.get(position).getOnline_exclusive();

            if (discountAvailability.equals("1")) {
                discountText = productObj.get(position).getDiscount_text();

                holder.productSaleBlackTV.setVisibility(View.VISIBLE);
                holder.productSaleBlackTV.setText(discountText);
            }
            else if(onlineExclusive.equals("1")) {
                    holder.productSaleBlackTV.setVisibility(View.VISIBLE);
                    holder.productSaleBlackTV.setText("ONLINE EXCLUSIVE");
            }
            else {
                    holder.productSaleBlackTV.setVisibility(View.GONE);
                }

            }
        else {
            holder.productSaleBlackTV.setVisibility(View.VISIBLE);
            holder.productSaleBlackTV.setText("SOLD OUT");
        }


        holder.productName.setText(productObj.get(position).getname());
//        holder.productPrice.setText(productObj.get(position).price_with_tax);

        if (productObj.get(position).get_collection_name == null || productObj.get(position).get_collection_name == "null") {
            holder.categoryName.setVisibility(View.GONE);
        } else {
            holder.categoryName.setText(productObj.get(position).get_collection_name);
            holder.categoryName.setVisibility(View.VISIBLE);

        }

        if (productObj.get(position).getprice_with_discount().equals("0.00")) {
//            if (currency.equalsIgnoreCase("RM")) {
////                viewHolder.textView1.setText(SelectedCountryCurrency + " " + data.get(i).getprice_with_tax());
//                holder.productPrice.setText(currency + " " + productObj.get(position).getprice_with_tax());
//                holder.productDiscountPrice.setVisibility(View.GONE);
//
//
//            } else {
            holder.productPrice.setText(currency + " " + productObj.get(position).getprice_with_tax());
            holder.productPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.productDiscountPrice.setVisibility(View.GONE);
//            }
//            holder.productPrice.setVisibility(View.VISIBLE);
//            holder.productDiscountPrice.setVisibility(View.GONE);
        } else {
//            final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            final ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources().getColor(R.color.red));
            String s;
            if (currency.equalsIgnoreCase("RM")) {
//                s = SelectedCountryCurrency + " " + data.get(i).getprice_with_tax();
                s = currency + " " + productObj.get(position).getprice_with_tax() + " ";

                holder.productPrice.setText(currency + " " + productObj.get(position).getPrice_Without_reduction());


            } else {

                s = currency + " " + productObj.get(position).getprice_with_tax();

                holder.productPrice.setText(currency + " " + productObj.get(position).getPrice_Without_reduction());

            }
            holder.productDiscountPrice.setText(s, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) holder.productDiscountPrice.getText();

            spannable.setSpan(fcs, 0, productObj.get(position).getprice_with_tax().length() + 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            viewHolder.grid_text_price_discount.setVisibility(View.VISIBLE);
//            viewHolder.textView1.setVisibility(View.GONE);
//            viewHolder.textView1.setText("RM " + data.get(i).getprice_with_discount());
//            viewHolder.textView1.setTextColor(mcontext.getResources().getColor(R.color.red));
            holder.productPrice.setVisibility(View.VISIBLE);

            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.productDiscountPrice.setVisibility(View.VISIBLE);
        }

        holder.productSaleBlackTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.categoryName.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productName.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productPrice.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productDiscountPrice.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));



//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//
//        display(holder.productImg, productObj.get(position).image_url,options,holder.loadingIcon);

        Glide.with(holder.productImg)
                .load(productObj.get(position).image_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.loadingIcon.setVisibility(View.VISIBLE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.loadingIcon.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.productImg);

    }

    @Override
    public int getItemCount() {
        return productObj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView categoryName,productName,productPrice,productDiscountPrice,productSaleBlackTV;
        ImageView productImg;
        ProgressBar loadingIcon;
        Button wishlistBtn;

        ItemClickListener itemClickListener;

        ViewHolder(View itemView, ItemClickListener itemClickListener){
            super(itemView);

            wishlistBtn = itemView.findViewById(R.id.wishlist_button);
            productSaleBlackTV = itemView.findViewById(R.id.product_sale_label);
            categoryName = itemView.findViewById(R.id.category_label);
            productName = itemView.findViewById(R.id.product_label);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscountPrice = itemView.findViewById(R.id.product_discounted_price);
            productImg = itemView.findViewById(R.id.product_img);
            loadingIcon = itemView.findViewById(R.id.productLoadingIcon);
            this.itemClickListener = itemClickListener;

            productImg.setOnClickListener(this);
            wishlistBtn.setOnClickListener(this);

            productImg.setTag(1);
            wishlistBtn.setTag(2);
        }

        @Override
        public void onClick(View view)
        {
            String getTag = view.getTag().toString();

            if(getTag.equals("1"))
            {
                itemClickListener.onItemClick(getAdapterPosition());
            }
            else {
                itemClickListener.onWishlistClick(getAdapterPosition());
            }

        }

    }


    public interface ItemClickListener{
        void onItemClick(int position);
        void onWishlistClick(int position);
    }

    public void display(final ImageView img, String url, DisplayImageOptions options, final ProgressBar spinner) {

        imageLoader.displayImage(url, img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                img.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE); // set the spinner visible
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                img.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE); // set the spinenr visibility to gone


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                img.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE); //  loading completed set the spinenr visibility to gone
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

        });
    }
}
