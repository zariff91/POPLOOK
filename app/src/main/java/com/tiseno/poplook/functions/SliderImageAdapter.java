package com.tiseno.poplook.functions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.tiseno.poplook.R;

import java.util.ArrayList;

public class SliderImageAdapter  extends SliderViewAdapter<SliderImageAdapter.SliderAdapterVH> {
    private Context context;
    private ArrayList<HomeItem> mSliderItems = new ArrayList<>();
    private ItemClickListener mItemClickListener;


    ImageLoader imageLoader= ImageLoader.getInstance();

    public SliderImageAdapter(Context context, ArrayList<HomeItem>mArray) {
        this.context = context;
        this.mSliderItems = mArray;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tiseno.poplook.R.layout.custom_slider_image_layout, parent, false); //Inflating the layout
        return new SliderAdapterVH(view);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }


    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

//        SliderItem sliderItem = mSliderItems.get(position);

        Glide.with(context).clear(viewHolder.imageViewBackground);

        Glide.with(viewHolder.itemView)
                .load(mSliderItems.get(position).gethref())
                .skipMemoryCache(true)
                .into(viewHolder.imageViewBackground);

//        String imageUrl = mSliderItems.get(position).toString();
//
//        viewHolder.imageView.loadUrl(imageUrl);
//        viewHolder.imageView.getSettings().setLoadWithOverviewMode(true);
//        viewHolder.imageView.getSettings().setUseWideViewPort(true);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String test = mSliderItems.get(position).getcategoryID();
                String test2 = mSliderItems.get(position).getcategoryID();

                System.out.println("lkakka 1 = " + test);
                System.out.println("lkakka 2 = " + test);

                if (mItemClickListener != null) {
                    mItemClickListener.onItemSliderClick(position);
                }

            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;

        WebView imageView;

        ProgressBar ImageProgressBar;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(com.tiseno.poplook.R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(com.tiseno.poplook.R.id.iv_gif_container);
            ImageProgressBar = itemView.findViewById(R.id.loadingImageIcon);
//            imageView = itemView.findViewById(R.id.imageWV);
            this.itemView = itemView;
        }
    }

    public void display(final ImageView img, String url, final ProgressBar spinner)
    {
        imageLoader.displayImage(url, img, new ImageLoadingListener() {
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
                spinner.setVisibility(View.GONE); //  loading completed set the spinenr visibility to gone


            }

        });
    }

    public interface ItemClickListener {
        void onItemSliderClick(int position);
    }
}

