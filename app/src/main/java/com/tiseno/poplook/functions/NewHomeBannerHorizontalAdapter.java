package com.tiseno.poplook.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.R;

import java.util.ArrayList;

public class NewHomeBannerHorizontalAdapter extends RecyclerView.Adapter<NewHomeBannerHorizontalAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<HomeItem> data;
    ImageLoader imageLoader= ImageLoader.getInstance();
    String parentName;

    ViewHolder.AdapterCallback listener;

    public NewHomeBannerHorizontalAdapter(Context context,ArrayList<HomeItem> data1, ViewHolder.AdapterCallback callback,String parentName){

        mContext = context;
        data = data1;
        this.listener = callback;
        this.parentName = parentName;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_banner_view, parent, false);

        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext.getApplicationContext()));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.horizontalBannerTitle.setText(data.get(position).getcatName());
        holder.horizontalBannerTitle.setTypeface(FontUtil.getTypeface(mContext, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder.horizontalBannerTitle.setTextColor(Color.BLACK);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        display(holder.horizontalBannerIcon, data.get(position).gethref(), options,holder.loadingImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMethodCallbackHorizontal(position,parentName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView horizontalBannerTitle;
        private ImageView horizontalBannerIcon;
        ProgressBar loadingImg;

        View itemView;

        public interface AdapterCallback {
            void onMethodCallbackHorizontal(int positionClicked,String parentName);
        }


        public ViewHolder(View view) {
            super(view);

            horizontalBannerTitle = view.findViewById(R.id.horizontalIconTextView);
            horizontalBannerIcon = view.findViewById(R.id.horizontalIconImageView);
            loadingImg = view.findViewById(R.id.loadingImageHorizontal);

            this.itemView = view;
        }

        @Override
        public void onClick(View view) {
        }
    }

    public void display(final ImageView img, String url, DisplayImageOptions options,ProgressBar spinner)
    {
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
