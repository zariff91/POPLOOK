package com.tiseno.poplook.functions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.R;

import java.util.ArrayList;

/**
 * Created by billygoh on 8/26/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOLLOW = 2;
    private static final int TYPE_COLLECTION = 3;

    ArrayList<sideMenuItem> data ;
    Context context;
    static  OnItemClickListener listener;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView title_nameTV,title_nameTVChange,header_name_collapseTV,title_name_collapseTV;
        TextView header_nameTV;
        TextView title_nameTV2;
        ImageView sideImageIV,sideImagecollapseIV,sideImage_collapseIV;
        ImageView fbIV,twitIV,igIV;

        RelativeLayout title_withDividerRL,title_withDivider_collapseRL;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            if(ViewType == TYPE_ITEM) {
                title_nameTV = (TextView) itemView.findViewById(R.id.title_nameTV);
//                title_nameTVChange = (TextView) itemView.findViewById(R.id.title_nameTVChange);
//                sideImageIV = (ImageView) itemView.findViewById(R.id.sideImageIV);
                title_withDividerRL = (RelativeLayout) itemView.findViewById(R.id.title_withDividerRL);
                Holderid = 1;

            } else if(ViewType == TYPE_HEADER)
            {


                header_nameTV = (TextView) itemView.findViewById(R.id.header_nameTV);
                Holderid = 0;
            }
            else if(ViewType == TYPE_FOLLOW)
            {


                title_nameTV2 = (TextView) itemView.findViewById(R.id.title_nameTV);
                fbIV = (ImageView) itemView.findViewById(R.id.fbIV);
                twitIV = (ImageView) itemView.findViewById(R.id.twitIV);
                igIV = (ImageView) itemView.findViewById(R.id.igIV);
                Holderid = 2;
            }
            else if(ViewType == TYPE_COLLECTION)
            {
                //HEADER
                sideImagecollapseIV = (ImageView) itemView.findViewById(R.id.sideImagecollapseIV);
                header_name_collapseTV = (TextView) itemView.findViewById(R.id.header_name_collapseTV);
                //CHILD
                title_name_collapseTV = (TextView) itemView.findViewById(R.id.title_name_collapseTV);
                sideImage_collapseIV = (ImageView) itemView.findViewById(R.id.sideImage_collapseIV);
                title_withDivider_collapseRL = (RelativeLayout) itemView.findViewById(R.id.title_withDivider_collapseRL);
                    Holderid = 3;

            }
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {

          //  listener.onItemClick(v, this.getLayoutPosition());

        }

    }



    public MyAdapter(Context context1, ArrayList<sideMenuItem> data1,OnItemClickListener listener1) {
        data = data1;
        context = context1;
        listener=listener1;
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);

            ViewHolder vhItem = new ViewHolder(v,viewType);

            return vhItem;


        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);

            ViewHolder vhHeader = new ViewHolder(v,viewType);

            return vhHeader;


        } else if (viewType == TYPE_FOLLOW) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_us_row,parent,false);

            ViewHolder vhHeader = new ViewHolder(v,viewType);

            return vhHeader;



        } else if (viewType == TYPE_COLLECTION) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_collapsible,parent,false);

            ViewHolder vhHeader = new ViewHolder(v,viewType);

            return vhHeader;


        }
        return null;

    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {

            if(position >= data.size()-3)
            {
                holder.title_withDividerRL.setVisibility(View.VISIBLE);
//                holder.sideImageIV.setVisibility(View.VISIBLE);
//                holder.title_nameTVChange.setVisibility(View.INVISIBLE);


            }
            else
            {
                holder.title_withDividerRL.setVisibility(View.INVISIBLE);
//                holder.sideImageIV.setVisibility(View.VISIBLE);
//                holder.title_nameTVChange.setVisibility(View.INVISIBLE);

            }
            if(data.get(position).gettitle().contains("Shipping To")){
                holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
//                holder.title_nameTVChange.setVisibility(View.VISIBLE);
//                holder.title_nameTVChange.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
                holder.sideImageIV.setVisibility(View.INVISIBLE);

            }else
            if(data.get(position).gettitle().equalsIgnoreCase("Backorders")) {
                holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
                holder.title_withDividerRL.setVisibility(View.VISIBLE);
                holder.title_nameTV.setText(data.get(position).gettitle());

            }else
            if(data.get(position).gettitle().equalsIgnoreCase("Visit Our Store")) {
                holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
                holder.title_nameTV.setText(data.get(position).gettitle());

            }else
            if(data.get(position).gettitle().equalsIgnoreCase("My Account")) {
                holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
                holder.title_nameTV.setText(data.get(position).gettitle());

            }else
            if(data.get(position).gettitle().equalsIgnoreCase("Settings")) {
                holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
                holder.title_nameTV.setText(data.get(position).gettitle());

            }else
            if(data.get(position).gettitle().equalsIgnoreCase("Log Out")) {
                holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
                holder.title_nameTV.setText(data.get(position).gettitle());

            }else
            if(data.get(position).gettitle().equalsIgnoreCase("Log In")) {
                holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
                holder.title_nameTV.setText(data.get(position).gettitle());

            }else
            holder.title_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
            holder.title_nameTV.setText(data.get(position).gettitle());
                if(!data.get(position).getTextColor().equalsIgnoreCase("")){
                    holder.title_nameTV.setTextColor(Color.parseColor(data.get(position).getTextColor()));
                }else{
                    holder.title_nameTV.setTextColor(Color.parseColor("#555555"));
                }


        }
        else if(holder.Holderid ==0){
            holder.header_nameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
            holder.header_nameTV.setText(data.get(position).gettitle());


        }else if(holder.Holderid ==2){
            holder.title_nameTV2.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
            holder.title_nameTV2.setText(data.get(position).gettitle());

            holder.fbIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getFacebookPageURL(context);
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    context.startActivity(facebookIntent);
                }

            });
            holder.twitIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    try {
                        // get the Twitter app if possible
                        context.getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=50962757"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/poplookshop"));
                    }
                    context.startActivity(intent);



                }

            });
            holder.igIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri uri = Uri.parse("http://instagram.com/_u/poplook");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        context.startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/poplook")));
                    }


                }

            });
        } else if(holder.Holderid ==3){
            holder.header_name_collapseTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
            holder.header_name_collapseTV.setText(data.get(position).gettitle());


        }
    }
    public static String FACEBOOK_URL = "https://www.facebook.com/POPLOOK";
    public static String FACEBOOK_PAGE_ID = "138171308208";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://profile/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {

        if(data.get(position).getFollow())
        {
            return TYPE_FOLLOW;
        }
        else
        {   if(data.get(position).getCollection()){

                    return TYPE_COLLECTION;

            }else{
                if(data.get(position).getisHeader())
                {
                    return TYPE_HEADER;
                }
                else
                {
                    return TYPE_ITEM;
                }
            }

        }

    }

}