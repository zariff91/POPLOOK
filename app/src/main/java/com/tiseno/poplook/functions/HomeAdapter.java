package com.tiseno.poplook.functions;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VerticalAdapter.ViewHolder.AdapterCallback ,NewHomeBannerHorizontalAdapter.ViewHolder.AdapterCallback{

    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;
    private final int VIDEO = 3;


    ImageLoader imageLoader= ImageLoader.getInstance();
    ArrayList<HashMap<String,ArrayList>> data;
    JSONObject jsonObject;
    JSONObject jsonObjectVideo;
    JSONArray jsonArrayVideo;
    ArrayList<HomeItem>horiData = new  ArrayList<>();
    ArrayList<HomeItem>verticalData = new  ArrayList<>();
    ArrayList<HomeItem>videoArray = new  ArrayList<>();
    ArrayList<String>parentTitle = new  ArrayList<>();

    AdapterHomeCallback callback;

    boolean collection;
    String videoURL;
    String sliderTitle;

    Context context;

    @Override
    public void onMethodCallback(int positionClicked, String parentName) {
        callback.onBannerClickPosition(positionClicked,parentName);

    }

    @Override
    public void onMethodCallbackHorizontal(int positionClicked, String parentName) {
        callback.onBannerClickPosition(positionClicked,parentName);
    }

    public interface AdapterHomeCallback {
        void onBannerClickPosition(int position,String parentName);
    }

    public HomeAdapter(JSONObject data1,ArrayList<String> parentTitle,JSONObject jsonVideo, Context context1, boolean collection1,AdapterHomeCallback callback){

        jsonObject = data1;
        this.parentTitle = parentTitle;
        context = context1;
        collection = collection1;
        this.jsonObjectVideo = jsonVideo;
        this.callback = callback;

    }

    @Override
    public int getItemCount() {
        return parentTitle.size();// the number of items in the list will be +1 the titles including the header view.
    }
    @Override
    public int getItemViewType(int position){

        verticalData.clear();

        try {
            String key = parentTitle.get(position);

            if(key.equals("isVideo")){

//                if(videoArray.size() != jsonArrayVideo.length())
//                {

                jsonArrayVideo = jsonObjectVideo.getJSONArray("data");

                    JSONObject videoData = jsonArrayVideo.getJSONObject(0);
//
                    String catIDPrimary = videoData.getString("category_id");
                    String catNamePrimary = videoData.getString("category_name");
                    String linkPrimary = videoData.getString("link");
                    String hrefPrimary = videoData.getString("href");
                    String positions = videoData.getString("position");

                    int x = Integer.parseInt(positions)-1;

                    videoArray.add(new HomeItem(catIDPrimary,catNamePrimary,linkPrimary,hrefPrimary,positions,""));

//                }
                return VIDEO;
            }
            else {

                JSONObject child_object = jsonObject.getJSONObject(key);


                String banner_type = child_object.getString("type");

                if(banner_type.equals("slider")){

                    JSONArray jsonArr = null;
                    jsonArr = child_object.getJSONArray("data");
                    sliderTitle = child_object.getString("name");

                    if(horiData.size() != jsonArr.length()){

                        for (int j = 0; j < jsonArr.length(); j++) {

                            String categori_id = jsonArr.getJSONObject(j).getString("category_id");
                            String category_name = jsonArr.getJSONObject(j).getString("category_name");
                            String link = jsonArr.getJSONObject(j).getString("link");
                            String href = jsonArr.getJSONObject(j).getString("href");
                            horiData.add(new HomeItem(categori_id, category_name, link, href, "",key));
                        }
                    }

                    return HORIZONTAL;

                }
                else {

                    JSONArray jsonArr = null;
                    jsonArr = child_object.getJSONArray("data");

                    for (int y = 0; y < jsonArr.length (); ++y) {

                        String catIDPrimary = jsonArr.getJSONObject(y).getString("category_id");
                        String catNamePrimary = jsonArr.getJSONObject(y).getString("category_name");
                        String linkPrimary = jsonArr.getJSONObject(y).getString("link");
                        String hrefPrimary = jsonArr.getJSONObject(y).getString("href");
                        verticalData.add(new HomeItem(catIDPrimary, catNamePrimary, linkPrimary, hrefPrimary,"",key));
                    }

                    return VERTICAL;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return VERTICAL;

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;

        switch (viewType){
            case VERTICAL:
                view = inflater.inflate(R.layout.vertical_rv,parent,false);
                holder = new verticalViewHolder(view);
                break;
            case  HORIZONTAL:
                view = inflater.inflate(R.layout.horizontal_rv,parent,false);
                holder = new horizontalViewHolder(view);
                break;
            case  VIDEO:
                view = inflater.inflate(R.layout.video_view_banner,parent,false);
                holder = new videoViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.vertical_rv,parent,false);
                holder = new verticalViewHolder(view);
                break;
        }

        return holder;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        
        String parentKey = parentTitle.get(position);

        if(holder.getItemViewType() == VERTICAL){
            verticalView((verticalViewHolder)holder,parentKey);
        }
        else if(holder.getItemViewType() == HORIZONTAL){
            horizontalView((horizontalViewHolder) holder,sliderTitle,parentKey);
        }

        else if(holder.getItemViewType() == VIDEO){
           videoView((videoViewHolder)holder,"",parentKey);
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

    public class horizontalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView view;
        TextView title;
        horizontalViewHolder(View itemView){
            super(itemView);
            view = (RecyclerView)itemView.findViewById(R.id.inner_rv_horizontal);
            title = (TextView) itemView.findViewById(R.id.title_slider);


        };

    }

    public class videoViewHolder extends RecyclerView.ViewHolder {
        VideoView view;
        videoViewHolder(View itemView){
            super(itemView);
            view = (VideoView) itemView.findViewById(R.id.videoViewPlayer);

        };

    }

    public class verticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView view;
        verticalViewHolder(View itemView){
            super(itemView);
            view = (RecyclerView)itemView.findViewById(R.id.inner_rv);

        };

    }

    private void verticalView(verticalViewHolder holder, String parentName){
        VerticalAdapter adapter1 = new VerticalAdapter(verticalData,context,false,this,parentName);
        holder.view.setLayoutManager(new LinearLayoutManager(context));
        holder.view.setAdapter(adapter1);
    }
    private void horizontalView(horizontalViewHolder holder,String parentName, String parentKey){
        NewHomeBannerHorizontalAdapter adapter2 = new NewHomeBannerHorizontalAdapter(context,horiData,this,parentKey);
        holder.view.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.view.setAdapter(adapter2);
        holder.title.setText(parentName);
        holder.title.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
        holder.title.setTextColor(Color.BLACK);


    }

    private void videoView(videoViewHolder holder,String parentName, String parentKey){

        if(videoArray.get(0).gethref().contains("mp4")){
            holder.view.seekTo(1);
            holder.view.start();
            holder.view.getLayoutParams().height = getScreenWidth(context) * 29 / 16;
            holder.view.setVideoPath(videoArray.get(0).gethref());
            holder.view.setOnPreparedListener(mp -> mp.setVolume(0f, 0f));
            holder.view.setOnCompletionListener (mediaPlayer -> holder.view.start());
            holder.view.setOnClickListener(view -> {
                callback.onBannerClickPosition(0,parentKey);
            });
        }
        else {
            holder.view.setVisibility(View.GONE);
        }
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

