package com.tiseno.poplook.functions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.tiseno.poplook.R;

import java.net.URI;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

         //int Resource for header view profile picture
         ImageLoader imageLoader= ImageLoader.getInstance();

    ArrayList<HomeItem> data ;
    boolean collection;
    String videoURL;
    Context context;

    ViewHolder.AdapterCallback buttonListener;


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

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
            homeImage = (ImageView) itemView.findViewById(R.id.homeImage);// Creating Image view object from header.xml for profile pic
            frameL = (FrameLayout) itemView.findViewById(R.id.frameL);
            loadingImageHome = (ProgressBar) itemView.findViewById(R.id.loadingImageHome);
            videoView = (VideoView)itemView.findViewById(R.id.videoViewPlayer);
            youTubePlayerView = itemView.findViewById(R.id.youtubeWebView);
            // Setting holder id = 0 as the object being populated are of type header view
            }
        }






    public HomeAdapter(ArrayList<HomeItem> data1, Context context1, boolean collection1, ViewHolder.AdapterCallback adapter){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we

        data = data1;
        context = context1;
        collection = collection1;
        this.buttonListener = adapter;
        //here we assign those passed values to the values we declared here
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
    public void onViewAttachedToWindow(ViewHolder holder) {

        if(holder.videoView.getVisibility() == View.VISIBLE)
        {

            holder.videoView.setVideoPath(videoURL);
            holder.videoView.seekTo(1);
            holder.videoView.start();
            holder.loadingImageHome.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));

        if(collection) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row, parent, false); //Inflating the layout

            //Creating ViewHolder and passing the object of type view

            return new ViewHolder(v); // Returning the created object
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row1, parent, false); //Inflating the layout

            //Creating ViewHolder and passing the object of type view

            return new ViewHolder(v); // Returning the created object
        }
            //inflate your layout and pass it to view holder

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(data.get(position).gethref().length()==0){

            holder.loadingImageHome.setVisibility(View.GONE);
            holder.homeImage.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.youTubePlayerView.setVisibility(View.GONE);

        }else {

            int height = getScreenHeight();
            int width = getScreenWidth();
            if (collection) {
                holder.frameL.getLayoutParams().width = width - 100;
            } else {
//                holder.frameL.getLayoutParams().width = width-200;
            }

                if (data.get(position).getlink().equals("isVideo")) {

                    videoURL = data.get(position).gethref();

                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                    int x = displayMetrics.heightPixels;
                    double y = displayMetrics.widthPixels/2.086;

                    String widthh = String.valueOf(y);

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

                        buttonListener.onMethodCallback(data.get(position).getcategoryID(), data.get(position).gethref(),data.get(position).getcatName(),data.get(position).getlink());

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
//                            Log.e(TAG, "Load failed", e);
//
                                        // You can also log the individual causes:
                                        for (Throwable t : e.getRootCauses()) {
//                                Log.e(TAG, "Caused by", t);
                                        }
                                        // Or, to log all root causes locally, you can use the built in helper method:
//                            e.logRootCauses(TAG);
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
//            holder.homeImage.setImageResource(homeImage[position]);           // Similarly we set the resources for header view

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

