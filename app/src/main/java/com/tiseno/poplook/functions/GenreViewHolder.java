package com.tiseno.poplook.functions;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiseno.poplook.R;


public class GenreViewHolder extends GroupViewHolder {

    public TextView genreTitle;

    public ImageView fbIV,twitIV,igIV,dummy, rayaIcon;


    public GenreViewHolder(View v)
    {

        super(v);

        genreTitle = (TextView)v.findViewById(R.id.title_nameTV);
        fbIV = (ImageView) v.findViewById(R.id.fbIV);
        twitIV = (ImageView) v.findViewById(R.id.twitIV);
        igIV = (ImageView) v.findViewById(R.id.igIV);
        dummy = (ImageView) v.findViewById(R.id.dummy);
        rayaIcon = (ImageView) v.findViewById(R.id.rayaIcon);

    }

    public void setGenreName(String name){
        genreTitle.setText(name);
    }

    public interface OnContactClick {
        public void onContactClick(int position);
    }

    OnContactClick onContactClick;

    public void setOnContactClick(OnContactClick onContactClick) {
        this.onContactClick = onContactClick;
    }

}
