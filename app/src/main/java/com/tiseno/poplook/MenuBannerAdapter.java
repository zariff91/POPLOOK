package com.tiseno.poplook;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.HomeItem;
import com.tiseno.poplook.functions.NewHomeBannerHorizontalAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements NewHomeBannerHorizontalAdapter.ViewHolder.AdapterCallback{


    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;

    ImageLoader imageLoader= ImageLoader.getInstance();
    JSONArray arr;
    Context context;
    AdapterMenuCallback callback;


    public interface AdapterMenuCallback {
        void onBannerHorizontalClickPosition(String bannerName,String bannerID);
    }

    public MenuBannerAdapter(Context mContext, JSONArray array,AdapterMenuCallback adapterCallback){
        arr = array;
        context = mContext;
        callback = adapterCallback;

        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext.getApplicationContext()));

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;

        switch (viewType){
            case VERTICAL:
                view = inflater.inflate(R.layout.verticalbanner,parent,false);
                holder = new verticalBanner(view);
                break;
            case  HORIZONTAL:
                view = inflater.inflate(R.layout.horizontalrv,parent,false);
                holder = new horizontalViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.verticalbanner,parent,false);
                holder = new horizontalViewHolder(view);
                break;
        }

        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == VERTICAL){

            String bannerURL = new String();
            String bannerID = new String();
            String bannerCategoryName = new String();


            try {
                JSONObject child_object = arr.getJSONObject(position);
                bannerURL = child_object.getString("image_url");
                bannerID = child_object.getString("category_id");
                bannerCategoryName = child_object.getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            verticalView((verticalBanner) holder,bannerURL,bannerID,bannerCategoryName);
        }
        else if(holder.getItemViewType() == HORIZONTAL){

            String title = "";

            ArrayList<HomeItem> horiData = new  ArrayList<>();

            try {
                JSONObject child_object = arr.getJSONObject(position);
                title = child_object.getString("name");

                JSONArray child = child_object.getJSONArray("children");

                System.out.println("title = "+ title);
                System.out.println("children data = "+ child);


                for(int x=0;x<child.length();x++){

                    JSONObject obj = child.getJSONObject(x);

                    String catIDPrimary = obj.getString("category_id");
                    String catNamePrimary = obj.getString("name");
//                    String linkPrimary = obj.getString("link");
                    String hrefPrimary = obj.getString("image_url");
                    horiData.add(new HomeItem(catIDPrimary, catNamePrimary, "", hrefPrimary,"",""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            horizontalView((horizontalViewHolder) holder,title,"parentKey",horiData);
        }


    }

    @Override
    public int getItemCount() {
        return arr.length();
    }

    @Override
    public int getItemViewType(int position){

        int VIEWTYPE = 0;

        try {

            JSONObject child_object = arr.getJSONObject(position);
            String type = child_object.getString("type");

            if(type.equals("slider"))
            {
                VIEWTYPE = HORIZONTAL;
            }

            else {
                VIEWTYPE = VERTICAL;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return VIEWTYPE;
    }

    @Override
    public void onMethodCallbackHorizontal(String bannerID, String bannerName) {

        callback.onBannerHorizontalClickPosition(bannerID,bannerName);

    }

    public class horizontalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView view;
        TextView title;
        horizontalViewHolder(View itemView){
            super(itemView);
            view = (RecyclerView) itemView.findViewById(R.id.inner_rv_horizontal);
            title = (TextView) itemView.findViewById(R.id.title_slider);

        };

    }

    public class verticalBanner extends RecyclerView.ViewHolder {
        ImageView bannerImageView;
        verticalBanner(View itemView){
            super(itemView);
            bannerImageView = (ImageView) itemView.findViewById(R.id.bannerImage);
        };

    }
    private void horizontalView(horizontalViewHolder holder,String parentName, String parentKey,ArrayList<HomeItem>data){
        NewHomeBannerHorizontalAdapter adapter2 = new NewHomeBannerHorizontalAdapter(context,data,this,parentKey);
        holder.view.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.view.setAdapter(adapter2);
        holder.title.setText(parentName);
        holder.title.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
        holder.title.setTextColor(Color.BLACK);
    }
    private void verticalView(verticalBanner holder,String bannerURL, String bannerID, String categoryName){

        holder.bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onBannerHorizontalClickPosition(bannerID,categoryName);
            }
        });

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        display(holder.bannerImageView, bannerURL, options,null);

    }

    public void display(final ImageView img, String url, DisplayImageOptions options, ProgressBar spinner)
    {
        imageLoader.displayImage(url, img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                img.setVisibility(View.GONE);
//                spinner.setVisibility(View.VISIBLE); // set the spinner visible
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                img.setVisibility(View.VISIBLE);
//                spinner.setVisibility(View.GONE); // set the spinenr visibility to gone


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                img.setVisibility(View.VISIBLE);
//                spinner.setVisibility(View.GONE); //  loading completed set the spinenr visibility to gone
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

        });
    }

}
