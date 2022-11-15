package com.tiseno.poplook.functions;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.NewCartAndWishlistFragment;
import com.tiseno.poplook.ProductInfoFragment;
import com.tiseno.poplook.R;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessPut;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CustomGridWishlist extends RecyclerView.Adapter<CustomGridWishlist.ViewHolder> implements AsyncTaskCompleteListener<JSONObject> {

    Context mcontext;
    ArrayList<savedItemsItem> data;
    ImageLoader imageLoader= ImageLoader.getInstance();

    NewCartAndWishlistFragment fragment;

    String UserID, CartID="0", LanguageID;
    String bagNotiItem,apikey;

    int bagNotiCount=0;


    public CustomGridWishlist(Context context,NewCartAndWishlistFragment fragment1,ArrayList<savedItemsItem> listArray){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mcontext = context;
        data = listArray;
        fragment = fragment1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage,deleteIcon;
        TextView itemTitle,itemPrice,itemSize,addToCart;

        FrameLayout gridLL;



        public ViewHolder(View itemView) {
            super(itemView);

            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            addToCart = (TextView) itemView.findViewById(R.id.item_addCart);
            itemSize = (TextView) itemView.findViewById(R.id.item_size);


            itemImage = (ImageView)itemView.findViewById(R.id.grid_image_wishlist);
            deleteIcon = (ImageView)itemView.findViewById(R.id.delete_icon);

            gridLL = (FrameLayout)itemView.findViewById(R.id.gridLayoutLL);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_wishlist_grid,viewGroup,false); //Inflating the layout

        imageLoader.init(ImageLoaderConfiguration.createDefault(mcontext.getApplicationContext()));


        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "0");
        LanguageID = pref.getString("LanguageID", "");
        bagNotiItem = pref.getString("cartItem", "0");


        //Creating ViewHolder and passing the object of type view

        return new ViewHolder(v); // Return
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
        //multishop
        String SelectedShopID  = pref.getString("SelectedShopID", "1");
        String SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");

        apikey =pref.getString("apikey","");

        holder.itemTitle.setTypeface(FontUtil.getTypeface(mcontext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.itemPrice.setTypeface(FontUtil.getTypeface(mcontext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.addToCart.setTypeface(FontUtil.getTypeface(mcontext, FontUtil.FontType.AVENIR_ROMAN_FONT));

        final savedItemsItem item = data.get(position);

        holder.itemTitle.setText(item.getproductName());

        if(item.getproductAttributeID().equals("0")){
            holder.itemSize.setText("-");
        }else {
            holder.itemSize.setText("Size : "+item.getproductSize());
        }

        holder.itemPrice.setText(SelectedCountryCurrency+" "+item.getproductPrice());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage(item.getproductImage(), holder.itemImage,options);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(mcontext,
                        "Adding Item", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show();
                addToBag(item.getwishlistID(),item.getproductID(), item.getproductAttributeID());
            }
        });

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromSavedItems(item.getwishlistID(), item.getproductID(), item.getproductAttributeID());
            }
        });

        holder.gridLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(item.getproductID(),item.getproductName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void addToBag(String wishlistID,String productID, String productAttributeID)
    {
        String action="Wishlists/addProductCart";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_wishlist",wishlistID)
                .add("id_customer",UserID)
                .add("id_product",productID)
                .add("id_product_attribute",productAttributeID)
                .add("id_cart",CartID)
                .add("quantity","1")
                .build();
        WebServiceAccessPut callws = new WebServiceAccessPut(mcontext, this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    private void removeFromSavedItems(String wishlistID, String productID, String productAttributeID)
    {

        String action="Wishlists/removeProduct";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_wishlist",wishlistID)
                .add("id_customer",UserID)
                .add("id_product",productID)
                .add("id_product_attribute",productAttributeID)
                .build();
        WebServiceAccessDelete callws = new WebServiceAccessDelete(mcontext, this);
        callws.setAction(action);
        callws.execute(formBody);

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{
                if(result.getString("action").equals("Wishlists_addProductCart"))
                {
                    if(result.getBoolean("status"))
                    {
                        System.out.println("get wishlists JSON"+result.getString("action"));

                        Toast toast = Toast.makeText(mcontext,
                                "Item Added", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
//                            bagNotiItem++;
//                            System.out.println("ITEM" + bagNotiItem);
                        bagNotiCount=Integer.parseInt(bagNotiItem)+1;

                        ((MainActivity) mcontext).changeToolBarBagNotiText(String.valueOf(bagNotiCount));

                        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("cartItem", String.valueOf(bagNotiCount));
                        editor.apply();




//                        //remove item
//                        SavedItemsFragment fragment = new SavedItemsFragment();
//                        switchContent2(R.id.fragmentContainer, fragment);
                        fragment.getSavedItems();
                    }
                    else {
                        String message = result.getString("message");
                        String ItemInCart = result.getString("ItemInCart");
                        Toast toast = Toast.makeText(mcontext,
                                message, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

                        //remove item
//                        SavedItemsFragment fragment = new SavedItemsFragment();
//                        switchContent2(R.id.fragmentContainer, fragment);
                        fragment.getSavedItems();
                    }
                }
                else if(result.getString("action").equals("Wishlists_removeProduct"))
                {
                    if(result.getBoolean("status"))
                    {
                        Toast toast = Toast.makeText(mcontext,
                                "Item successfully removed from Saved Items.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

                        //remove item
//                        SavedItemsFragment fragment = new SavedItemsFragment();
//                        switchContent2(R.id.fragmentContainer, fragment);
                        fragment.getSavedItems();
                    }
                }
            }
            catch (Exception e){

            }

        }
        else{

            new AlertDialog.Builder(mcontext)
                    .setTitle("Message")
                    .setMessage("Please connect to the Internet")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    private void fragmentJump(String prodID,String prodName) {
        ProductInfoFragment odf = new ProductInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("prodID",prodID);
        bundle.putString("catName",prodName);
        odf.setArguments(bundle);
//        mBundle = new Bundle();
//        mBundle.putParcelable("item_selected_key", mItemSelected);
//        mFragment.setArguments(mBundle);
        switchContent(R.id.fragmentContainer, odf);
    }

    public void switchContent(int id, Fragment fragment) {
        if (mcontext == null)
            return;
        if (mcontext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mcontext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

}
