package com.tiseno.poplook.functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.R;

import java.util.ArrayList;

/**
 * Created by rahn on 9/1/15.
 */
public class StyleWithAdapter extends RecyclerView.Adapter<StyleWithAdapter.ViewHolder> {



    ArrayList<StyleWithItem> data ;

    Context context;
    ImageLoader imageLoader= ImageLoader.getInstance();
    Boolean imageLoaded=false;
    String SelectedCountryCurrency;
    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView styleWithImage;
        TextView styleWithTitle,styleWithPrice;
        ProgressBar loadingImage;

        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            styleWithImage = (ImageView) itemView.findViewById(R.id.styleWithImage);// Creating Image view object from header.xml for profile pic
            // Setting holder id = 0 as the object being populated are of type header view
            styleWithTitle = (TextView) itemView.findViewById(R.id.styleWithTitle);
            styleWithPrice = (TextView) itemView.findViewById(R.id.styleWithPrice);
            loadingImage = (ProgressBar) itemView.findViewById(R.id.loadingImageStyleWith);
        }
    }






    public StyleWithAdapter(Context context1,ArrayList<StyleWithItem> data1){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we

             data=data1; //here we assign those passed values to the values we declared here
             context=context1;
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


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_row,parent,false); //Inflating the layout
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SelectedCountryCurrency = pref.getString("SelectedCountryCurrency", "");
        //Creating ViewHolder and passing the object of type view
        imageLoader.init(ImageLoaderConfiguration.createDefault(parent.getContext()));
        return new ViewHolder(v); // Returning the created object

        //inflate your layout and pass it to view holder

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.loadingImage.setVisibility(View.VISIBLE);
        holder.styleWithImage.setVisibility(View.GONE);
//
//        Picasso.with(holder.styleWithImage.getContext()).load(styleWithImage[position])
//                .into(holder.styleWithImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        holder.loadingImage.setVisibility(View.GONE);
//                        holder.styleWithImage.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError() {
//                        //error
//                    }
//                });
        String imageUri= data.get(position).getstyleWithImage();
        display(holder.styleWithImage, imageUri, holder.loadingImage);
        holder.styleWithTitle.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.styleWithPrice.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.styleWithTitle.setText(data.get(position).getstyleWithName());
        if(SelectedCountryCurrency.equals("RM")) {
            holder.styleWithPrice.setText("RM " + data.get(position).getstyleWithPrice() + "");
        }else{
            holder.styleWithPrice.setText(SelectedCountryCurrency+" " + data.get(position).getstyleWithPrice());

        }
         // Similarly we set the resources for header view

    }
    public void display(final ImageView img, String url, final ProgressBar spinner)
    {
        imageLoader.displayImage(url, img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(!imageLoaded)
                {
                    img.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE); // set the spinner visible
                    System.out.println("HASHSAHSAKDSAHDKSAHDKSAHKDSAKHDSADKDASH");
                }

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
                imageLoaded=true;
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

        });
    }
}

