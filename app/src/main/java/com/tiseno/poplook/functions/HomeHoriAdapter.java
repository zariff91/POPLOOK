package com.tiseno.poplook.functions;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tiseno.poplook.R;

import java.util.ArrayList;

/**
 * Created by rahnrazamai on 13/10/2016.
 */

public class HomeHoriAdapter extends RecyclerView.Adapter<HomeHoriAdapter.ViewHolder> {

    //int Resource for header view profile picture

    ArrayList<HomeItem> data ;

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView homeImage;
        ProgressBar loadingImageHome;


        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            homeImage = (ImageView) itemView.findViewById(R.id.homeImage);// Creating Image view object from header.xml for profile pic
            loadingImageHome = (ProgressBar) itemView.findViewById(R.id.loadingImageHome);                                       // Setting holder id = 0 as the object being populated are of type header view
        }
    }






    public HomeHoriAdapter(ArrayList<HomeItem> data1){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we

        data = data1;                     //here we assign those passed values to the values we declared here
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_hori_row,parent,false); //Inflating the layout

        //Creating ViewHolder and passing the object of type view

        return new ViewHolder(v); // Returning the created object

        //inflate your layout and pass it to view holder

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.loadingImageHome.setVisibility(View.VISIBLE);
        holder.homeImage.setVisibility(View.GONE);
        Picasso.with(holder.homeImage.getContext()).load(data.get(position).gethref())
                .into(holder.homeImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.loadingImageHome.setVisibility(View.GONE);
                        holder.homeImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        //error
                    }
                });
//            holder.homeImage.setImageResource(homeImage[position]);           // Similarly we set the resources for header view

    }

}