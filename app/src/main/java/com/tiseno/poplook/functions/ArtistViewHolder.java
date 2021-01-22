package com.tiseno.poplook.functions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.tiseno.poplook.R;

public class ArtistViewHolder extends ChildViewHolder {

    public TextView sideMenuSubTitle;

    ImageView dummy2;

    public ArtistViewHolder(View itemView) {
        super(itemView);

        sideMenuSubTitle = (TextView)itemView.findViewById(R.id.title_nameTV);

        dummy2 = (ImageView)itemView.findViewById(R.id.dummy2);


    }

    public void setSubTitleLabel(String name)
    {

        sideMenuSubTitle.setText(name);

    }
}
