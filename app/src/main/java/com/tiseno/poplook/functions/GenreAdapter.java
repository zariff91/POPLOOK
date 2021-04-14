package com.tiseno.poplook.functions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import com.tiseno.poplook.functions.Genre;


import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.R;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.OnItemClickListener;
import com.tiseno.poplook.functions.sideMenuItem;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends ExpandableRecyclerViewAdapter<GenreViewHolder, ArtistViewHolder> {

    static OnItemClickListener listener;

    String select;

    Context context;

    List<String> selectedSide = new ArrayList<String>();

    public GenreAdapter(MainActivity context1 , String getSelected, List<? extends ExpandableGroup> groups, OnItemClickListener listener1) {
        super(groups);

        listener = listener1;
        context = context1;
        select = getSelected;


    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_us_row, parent, false);

        return new GenreViewHolder(view);
    }

    @Override
    public ArtistViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final ArtistViewHolder holder, int flatPosition, ExpandableGroup group, final int childIndex) {

        final Artist artist = (Artist) group.getItems().get(childIndex);

        holder.setSubTitleLabel(artist.getTitle());

        holder.sideMenuSubTitle.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));


        final String catTitle = artist.getTitle();
        final String catID = artist.getCategoryID();

        holder.sideMenuSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                listener.onItemClick(catTitle,catID);

                getSelected(catTitle);

                notifyDataSetChanged();

            }
        });


        if(catID.equals(select))

        {

            holder.sideMenuSubTitle.setTextColor(Color.parseColor("#1CAE49"));

        }

        else {

            holder.sideMenuSubTitle.setTextColor(Color.BLACK);

        }

    }

    public void getSelected(String selectedSideMenu)
    {


        if(selectedSide.contains(selectedSideMenu))

        {



        }

        else {

            selectedSide.clear();
            selectedSide.add(selectedSideMenu);

        }



    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, final int flatPosition, ExpandableGroup group) {

        holder.setGenreName(group.getTitle());

        final String test = group.getTitle();

        if(test.equals("Raya 2021")){

            holder.genreTitle.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
            holder.genreTitle.setTextColor(Color.parseColor("#9BCBCE"));
            holder.genreTitle.setTextSize(20);

            holder.rayaIcon.setVisibility(View.VISIBLE);

        }

        else {

            holder.genreTitle.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
            holder.genreTitle.setTextColor(Color.BLACK);
            holder.genreTitle.setTextSize(20);
            holder.rayaIcon.setVisibility(View.INVISIBLE);

        }

        if(group.getItemCount() == 0)
        {

            if(test == "Follow Us")
            {

                holder.dummy.setVisibility(View.INVISIBLE);

            }

            else {

                holder.dummy.setVisibility(View.VISIBLE);
                holder.dummy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listener.onItemClick(test,"");

                        getSelected(test);

                        notifyDataSetChanged();
                    }

                });

                if(selectedSide.contains(test))

                {

                    holder.genreTitle.setTextColor(Color.parseColor("#1CAE49"));

                }

                else {

                    holder.genreTitle.setTextColor(Color.BLACK);

                }

            }


        }

        else {

            holder.dummy.setVisibility(View.INVISIBLE);

        }

    }

    public static String FACEBOOK_URL = "https://www.facebook.com/POPLOOK";
    public static String FACEBOOK_PAGE_ID = "138171308208";

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

}

