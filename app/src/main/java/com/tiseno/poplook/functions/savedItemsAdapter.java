package com.tiseno.poplook.functions;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.ProductInfoFragment;
import com.tiseno.poplook.R;
import com.tiseno.poplook.SavedItemsFragment;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessPut;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by billygoh on 9/9/15.
 */
public class savedItemsAdapter extends RecyclerView.Adapter<savedItemsAdapter.ViewHolder> implements AsyncTaskCompleteListener<JSONObject>{
    ImageLoader imageLoader= ImageLoader.getInstance();

    ArrayList<savedItemsItem> data ;

    Context context;
    SavedItemsFragment fragment;

    String UserID, CartID="0", LanguageID;
    String bagNotiItem,apikey;
    int bagNotiCount=0;


    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView productNameTV;
        TextView productSizeLblTV;
        TextView productSizeTV;
        TextView productUnitPriceLblTV;
        TextView productUnitPriceTV;
        TextView productAddToBagTV;
        TextView productRemoveTV;
        TextView soldOutInfo;
        TextView productExpiresLblTV;
        TextView productExpiresTV;

        FrameLayout SavedItemLL;
        RelativeLayout productAddToBagRL;

        ImageView productImageIV;

        public ViewHolder(View v) {
            super(v);

            productNameTV = (TextView) v.findViewById(R.id.productNameTV);
            productSizeLblTV = (TextView) v.findViewById(R.id.productSizeLblTV);
            productSizeTV = (TextView) v.findViewById(R.id.productSizeTV);
            productUnitPriceLblTV = (TextView) v.findViewById(R.id.productUnitPriceLblTV);
            productUnitPriceTV = (TextView) v.findViewById(R.id.productUnitPriceTV);
            productAddToBagTV = (TextView) v.findViewById(R.id.productAddToBagTV);
            productRemoveTV = (TextView) v.findViewById(R.id.productRemoveTV);
            productExpiresLblTV  = (TextView) v.findViewById(R.id.productExpiresLblTV);
            productExpiresTV = (TextView) v.findViewById(R.id.productExpiresTV);
            soldOutInfo = (TextView) v.findViewById(R.id.soldOutInfo);

            SavedItemLL = (FrameLayout) v.findViewById(R.id.SavedItemLL);
            productAddToBagRL = (RelativeLayout) v.findViewById(R.id.productAddToBagRL);
            productImageIV = (ImageView) v.findViewById(R.id.productImageIV);

        }
    }

    public savedItemsAdapter(Context context1,SavedItemsFragment fragment1,ArrayList<savedItemsItem> listArray) {
        fragment = fragment1;
        context = context1;
        data = listArray;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_items_item_row, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view
        imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "0");
        LanguageID = pref.getString("LanguageID", "");
        bagNotiItem = pref.getString("cartItem", "0");



        return vhItem; // Returning the created object
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        //multishop
        String SelectedShopID  = pref.getString("SelectedShopID", "1");
        String SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");
        apikey =pref.getString("apikey","");
        //
        holder.productNameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productSizeLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productSizeTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productUnitPriceLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productUnitPriceTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productAddToBagTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productRemoveTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productExpiresLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.productExpiresTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.soldOutInfo.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));


        final savedItemsItem item = data.get(position);

        holder.productNameTV.setText(item.getproductName());
        if(item.getproductAttributeID().equals("0")){
            holder.productSizeTV.setText("-");
        }else {
            holder.productSizeTV.setText(item.getproductSize());
        }
            holder.productUnitPriceTV.setText(SelectedCountryCurrency+" "+item.getproductPrice());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage(item.getproductImage(), holder.productImageIV,options);
//        Picasso.with(context).load(item.getproductImage())
//                .into(holder.productImageIV, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        holder.productImageIV.setVisibility(View.VISIBLE);
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        //error
//                    }
//                });
        holder.productExpiresTV.setText(item.getProductExpiresIn()+" days");

        if(item.getProductAvailableQuantity().equalsIgnoreCase("0")){
            holder.soldOutInfo.setVisibility(View.VISIBLE);
            holder.productAddToBagRL.setVisibility(View.GONE);
        }else{
            holder.soldOutInfo.setVisibility(View.INVISIBLE);
            holder.productAddToBagRL.setVisibility(View.VISIBLE);
        }

        holder.productAddToBagRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context,
                        "Adding Item", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show();
                addToBag(item.getwishlistID(),item.getproductID(), item.getproductAttributeID());
            }
        });

        holder.productAddToBagTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context,
                        "Adding Item", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show();
                addToBag(item.getwishlistID(),item.getproductID(), item.getproductAttributeID());
            }
        });

        holder.productRemoveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromSavedItems(item.getwishlistID(), item.getproductID(), item.getproductAttributeID());
            }
        });

        holder.SavedItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(item.getproductID(), item.getproductName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //code by Helmi
    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

    public void switchContent2(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent2(id, frag);
        }

    }
    //end code by Helmi


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
        WebServiceAccessPut callws = new WebServiceAccessPut(context, this);
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
        WebServiceAccessDelete callws = new WebServiceAccessDelete(context, this);
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

                        String cartID = result.getJSONObject("data").getString("id_cart");

                        Toast toast = Toast.makeText(context,
                                "Item Added", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
//                            bagNotiItem++;
//                            System.out.println("ITEM" + bagNotiItem);
                            bagNotiCount=Integer.parseInt(bagNotiItem)+1;

                                    ((MainActivity) context).changeToolBarBagNotiText(String.valueOf(bagNotiCount));

                            SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("cartItem", String.valueOf(bagNotiCount));
                            editor.putString("CartID", cartID);

                        editor.apply();




//                        //remove item
//                        SavedItemsFragment fragment = new SavedItemsFragment();
//                        switchContent2(R.id.fragmentContainer, fragment);
                        fragment.getSavedItems();
                    }
                    else {
                        String message = result.getString("message");
                        String ItemInCart = result.getString("ItemInCart");
                        Toast toast = Toast.makeText(context,
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
                        Toast toast = Toast.makeText(context,
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

            new AlertDialog.Builder(context)
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

//        FragmentManager fragmentManager = getActivity().getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }
}
