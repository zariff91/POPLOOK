package com.tiseno.poplook.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

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
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.R;

import java.util.ArrayList;

public class NewHomeBannerVerticalAdapter extends RecyclerView.Adapter<NewHomeBannerVerticalAdapter.ViewHolder> {

    ImageLoader imageLoader= ImageLoader.getInstance();
    ArrayList<HomeItem> data ;
    boolean collection;
    String videoURL;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView homeImage;
        FrameLayout frameL;
        ProgressBar loadingImageHome;

        VideoView videoView;

        WebView youTubePlayerView;


        public interface AdapterCallback {
            void onMethodCallback(String bannerID, String href, String categoryName, String link);
        }


        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            homeImage = (ImageView) itemView.findViewById(com.tiseno.poplook.R.id.homeImage);// Creating Image view object from header.xml for profile pic
            frameL = (FrameLayout) itemView.findViewById(com.tiseno.poplook.R.id.frameL);
            loadingImageHome = (ProgressBar) itemView.findViewById(com.tiseno.poplook.R.id.loadingImageHome);
            videoView = (VideoView)itemView.findViewById(com.tiseno.poplook.R.id.videoViewPlayer);
            youTubePlayerView = itemView.findViewById(com.tiseno.poplook.R.id.youtubeWebView);
            // Setting holder id = 0 as the object being populated are of type header view
        }
    }






    public NewHomeBannerVerticalAdapter(ArrayList<HomeItem> data1, Context context1, boolean collection1){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we

        data = data1;
        context = context1;
        collection = collection1;
        //here we assign those passed values to the values we declared here
        //in adapter



    }

    @Override
    public int getItemCount() {
        return data.size(); // the number of items in the list will be +1 the titles including the header view.
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row1, parent, false); //Inflating the layout
        return new ViewHolder(v); // Returning the created object
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final NewHomeBannerVerticalAdapter.ViewHolder holder, int position) {

        if(data.get(position).gethref().length()==0){

            holder.loadingImageHome.setVisibility(View.GONE);
            holder.homeImage.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.youTubePlayerView.setVisibility(View.GONE);

        }else {

            int width = getScreenWidth();
            if (collection) {
                holder.frameL.getLayoutParams().width = width - 100;
            } else {
            }

            if (data.get(position).getlink().equals("isVideo")) {

                videoURL = data.get(position).gethref();

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                int x = displayMetrics.heightPixels;
                double y = displayMetrics.widthPixels/2.086;

                if(videoURL.contains("youtube"))
                {
                    String youtubeID = videoURL.substring(30);

                    String playVideo= "<html><body><br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"100%\" height=\"250\" src=\""+videoURL+"\" frameborder=\"0\"></body></html>";

                    holder.youTubePlayerView.loadData(playVideo,"text/html", "utf-8");
                    holder.youTubePlayerView.getSettings().setJavaScriptEnabled(true);
                    holder.youTubePlayerView.getSettings().setLoadWithOverviewMode(true);
//                        holder.youTubePlayerView.getSettings().setUseWideViewPort(true);

//                        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                            @Override
//                            public void onReady(YouTubePlayer youTubePlayer) {
//                                String videoId = youtubeID;
//                                youTubePlayer.loadVideo(videoId, 0);
//
//                            }
//                        });

                    holder.homeImage.setVisibility(View.GONE);
                    holder.loadingImageHome.setVisibility(View.GONE);
                    holder.youTubePlayerView.setVisibility(View.VISIBLE);
                }

                else {

                    if(videoURL.equals(""))
                    {
                        holder.videoView.setVisibility(View.GONE);
                        holder.homeImage.setVisibility(View.GONE);
                        holder.loadingImageHome.setVisibility(View.GONE);
                    }

                    else {

                        holder.videoView.setVideoPath(videoURL);
//                holder.videoView.setMediaController(new MediaController(context));
                        holder.videoView.seekTo(1);
                        holder.videoView.start();
                        holder.loadingImageHome.setVisibility(View.VISIBLE);


                        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                holder.videoView.start();

                            }
                        });

                        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setVolume(0f, 0f);
                                holder.loadingImageHome.setVisibility(View.INVISIBLE);
                                holder.videoView.setVisibility(View.VISIBLE);


                            }
                        });
                        holder.videoView.setVisibility(View.VISIBLE);
                        holder.homeImage.setVisibility(View.GONE);
                        holder.videoView.getLayoutParams().height = getScreenWidth(context) * 29 / 16;

                        holder.frameL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                buttonListener.onMethodCallback(data.get(position).getcategoryID(), data.get(position).gethref(),data.get(position).getcatName(),data.get(position).getlink());

                            }
                        });

                    }
                }
            }

            else {

                holder.videoView.setVisibility(View.GONE);
                holder.youTubePlayerView.setVisibility(View.GONE);
                holder.homeImage.setVisibility(View.VISIBLE);

                holder.homeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        buttonListener.onMethodCallback(data.get(position).getcategoryID(), data.get(position).gethref(),data.get(position).getcatName(),data.get(position).getlink());

                    }
                });

                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                display(holder.homeImage, data.get(position).gethref(), options, holder.loadingImageHome);
//                    Picasso.with(holder.homeImage.getContext()).load(data.get(position).gethref()).into(holder.homeImage);
//                    Glide.with(holder.homeImage.getContext()).load(data.get(position).gethref()).listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.loadingImageHome.setVisibility(View.VISIBLE);
//
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                            holder.loadingImageHome.setVisibility(View.INVISIBLE);
//
//                            return false;
//                        }
//                    }).into(holder.homeImage);

                if(data.get(position).gethref().contains("gif")) {

                    Glide.with(holder.homeImage.getContext())
                            .load(data.get(position).gethref())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    for (Throwable t : e.getRootCauses()) {
                                    }
                                    holder.loadingImageHome.setVisibility(View.GONE);
                                    holder.homeImage.setVisibility(View.VISIBLE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.loadingImageHome.setVisibility(View.GONE);
                                    holder.homeImage.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            })
                            .into(holder.homeImage);
                }
//            Picasso.with(holder.homeImage.getContext()).load(data.get(position).gethref())
//                    .into(holder.homeImage, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            holder.loadingImageHome.setVisibility(View.GONE);
//                            holder.homeImage.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onError() {
//                            //error
//                        }
//                    });
            }
        }
    }
    public void display(final ImageView img, String url, DisplayImageOptions options, final ProgressBar spinner)
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
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context c) {
        int screenWidth = 0; // this is part of the class not the method
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }
}
