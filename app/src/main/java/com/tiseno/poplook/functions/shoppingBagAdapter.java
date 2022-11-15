package com.tiseno.poplook.functions;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.NewCartAndWishlistFragment;
import com.tiseno.poplook.ProductInfoFragment;
import com.tiseno.poplook.R;
import com.tiseno.poplook.ShoppingBagFragment;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessPost;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by billygoh on 9/1/15.
 */
public class shoppingBagAdapter extends RecyclerView.Adapter<shoppingBagAdapter.ViewHolder> implements AsyncTaskCompleteListener<JSONObject>{
    ImageLoader imageLoader= ImageLoader.getInstance();

    Context context;
    ShoppingBagFragment fragment;
    ArrayList<shoppingBagItem> data;
    boolean isFromCart = true,removeItemTimer= true;
    boolean refreshCallApi = false;

    String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d1 = null;
    Date d2 = null;
    long diff;
    long timeRem;
    long remainingtime;
    CountDownTimer countDownTimer;
    String apikey;

//    String reachableDate = "17-09-2015 16:13:00";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTV;
        ImageView productIV;
        TextView productSizeTV;
        TextView productRefNoTV;
        TextView productUnitPriceTV;
        TextView productQuantityTV;
        TextView productTotalPriceTV;
        TextView productTimeLeftTV;
        ImageView productRemoveTV;
        Spinner productQuantitySpinner;
        ProgressBar loadingImageLorh;
        TextView productTimeLeftLblTV;
        TextView productSizeLblTV;
        TextView productRefNoLblTV;
        TextView productUnitPriceLblTV;
        TextView productQuantityLblTV;
        TextView productTotalPriceLblTV,productTimeLeft1TV;

        ImageView timeLeftIV;

        FrameLayout shoppingBagItemLL;

        public ViewHolder(View v) {
            super(v);

            productNameTV = (TextView) v.findViewById(R.id.productNameTV);
//            productTimeLeftTV = (TextView) v.findViewById(R.id.productTimeLeftTV);
            productSizeTV = (TextView) v.findViewById(R.id.productSizeTV);
            productRefNoTV = (TextView) v.findViewById(R.id.productRefNoTV);
            productUnitPriceTV = (TextView) v.findViewById(R.id.productUnitPriceTV);
            productQuantityTV = (TextView) v.findViewById(R.id.productQuantityTV);
            productTotalPriceTV = (TextView) v.findViewById(R.id.productTotalPriceTV);
            productRemoveTV = (ImageView) v.findViewById(R.id.productRemoveTV);
            productIV = (ImageView) v.findViewById(R.id.productIV);
            productQuantitySpinner = (Spinner) v.findViewById(R.id.productQuantitySpinner);
            loadingImageLorh = (ProgressBar) v.findViewById(R.id.loadingImageLorh);
            productSizeLblTV = (TextView) v.findViewById(R.id.productSizeLblTV);
            productRefNoLblTV = (TextView) v.findViewById(R.id.productRefNoLblTV);
            productUnitPriceLblTV = (TextView) v.findViewById(R.id.productUnitPriceLblTV);
            productQuantityLblTV = (TextView) v.findViewById(R.id.productQuantityLblTV);
            productTotalPriceLblTV = (TextView) v.findViewById(R.id.productTotalPriceLblTV);

//            timeLeftIV = (ImageView) v.findViewById(R.id.timeLeftIV);
            productTimeLeft1TV = (TextView) v.findViewById(R.id.productTimeLeft1TV);
            shoppingBagItemLL = (FrameLayout) v.findViewById(R.id.shoppingBagItemLL);
        }
    }

    public shoppingBagAdapter(Context context1, ArrayList<shoppingBagItem> listArray, ShoppingBagFragment fragment1, boolean isFromCart1) {

        context = context1;
        data = listArray;
        fragment = fragment1;
        isFromCart = isFromCart1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_bag_item_row,parent,false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view

        imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));


        return vhItem; // Returning the created object
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final shoppingBagItem item = data.get(position);
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        //multishop
        String SelectedShopID  = pref.getString("SelectedShopID", "1");
        String SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");
        apikey =pref.getString("apikey","");
        //
        holder.productNameTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder.productSizeTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder.productRefNoTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder.productUnitPriceTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder.productQuantityTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
        holder.productTotalPriceTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
//        holder.productRemoveTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));

        holder.productSizeLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder. productRefNoLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder.productUnitPriceLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        holder.productQuantityLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
        holder.productTotalPriceLblTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));

        holder.productNameTV.setText(item.getproductName());
        holder.productSizeTV.setText(item.getproductSize());
        holder.productRefNoTV.setText(item.getproductRefNo());
        holder.productUnitPriceTV.setText(SelectedCountryCurrency+" " + item.getproductUnitPrice());
        holder.productQuantityTV.setText(item.getproductQuantity());

        holder.productTotalPriceTV.setText(String.format(SelectedCountryCurrency+" %.2f", Float.parseFloat(item.getproductTotalPrice())));

        if(item.getproductQuantity().equals("1"))
        {
            holder.productQuantitySpinner.setSelection(0);
        }
        else if(item.getproductQuantity().equals("2"))
        {
            holder.productQuantitySpinner.setSelection(1);
        }
        else if(item.getproductQuantity().equals("3"))
        {
            holder.productQuantitySpinner.setSelection(2);
        }
        else if(item.getproductQuantity().equals("4"))
        {
            holder.productQuantitySpinner.setSelection(3);
        }
        else if(item.getproductQuantity().equals("5"))
        {
            holder.productQuantitySpinner.setSelection(4);
        }

        holder.productRemoveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemTimer=false;

                removeFromCart(item.getcartID(), item.getproductID(), item.getproductAttributeID());

//                data.remove(position);

            }
        });

//        Glide.with(context)
//                .load(item.getproductImg())
//                .into(holder.productIV);
//
//        Glide.with(context)
//                .load(item.getproductImg())
//                .into(new GlideDrawableImageViewTarget(holder.productIV) {
//                    @Override
//                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
//                        super.onResourceReady(drawable, anim);
//                        holder.loadingImageLorh.setVisibility(View.GONE);
//
//
//                    }
//                });
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        display(holder.productIV, item.getproductImg(),options, holder.loadingImageLorh);
//        Picasso.with(context).load(item.getproductImg())
//                .into(holder.productIV, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        holder.loadingImageLorh.setVisibility(View.GONE);
//                        holder.productIV.setVisibility(View.VISIBLE);
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        //error
//                    }
//                });


        if(item.getAvailableQuantity() < 1)
        {
//            holder.productTimeLeftTV.setVisibility(View.INVISIBLE);
//            holder.timeLeftIV.setVisibility(View.INVISIBLE);
            holder.productTimeLeft1TV.setVisibility(View.VISIBLE);


            try{
//                    current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                    d1 = format.parse(current_date);
//                    d2 = format.parse(item.getproductTimeLeft());
////                Calendar cal = Calendar.getInstance();
////                cal.setTime(d1);
////                cal.add(Calendar.MINUTE, 2);
////                cal.add(Calendar.SECOND, 8);
////                String d3=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
////                Date d3d=format.parse(d3);
//                //in milliseconds
                diff =Long.parseLong(item.getproductTimeLeft());
//                    System.out.println("current date is " + current_date);
                System.out.println("current diff is 1" + diff);
//                timeRem=Long.parseLong(item.getproductTimeLeft());
                diff=diff-58000;
                countDownTimer = new CountDownTimer(diff, 1000) {

                    public void onTick(long millisUntilFinished) {
                        startCountDown(millisUntilFinished,holder);
                        remainingtime=millisUntilFinished;

                    }

                    public void onFinish() {
//                        holder.productTimeLeftTV.setText("00:00");
                        removeItemTimer=true;
//                        removeFromCart(item.getcartID(), item.getproductID(), item.getproductAttributeID());
//                        fragment.getUserCartList();
                        try {
//                            data.remove(position);
//                            notifyItemRemoved(position);
//                            notifyItemRangeChanged(position, data.size());
                        }catch (Exception e){

                        }
                    }
                }.start();

            }catch (Exception e){

            }
            holder.productQuantityTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.productQuantitySpinner.performClick();

                }
            });
        }
        else
        {
//            holder.productTimeLeftTV.setVisibility(View.INVISIBLE);
//            holder.timeLeftIV.setVisibility(View.INVISIBLE);
            holder.productTimeLeft1TV.setVisibility(View.INVISIBLE);

        }

        if(isFromCart)
        {
            holder.productQuantitySpinner.setVisibility(View.INVISIBLE);
            holder.productRemoveTV.setVisibility(View.VISIBLE);

            holder.productQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int count = 0;

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (count >= 1) {
                        getUserCartList(item, holder.productQuantitySpinner.getSelectedItem().toString());
                    }
                    count++;


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }
        else
        {
            holder.productQuantitySpinner.setVisibility(View.INVISIBLE);
            holder.productRemoveTV.setVisibility(View.INVISIBLE);
        }


        holder.shoppingBagItemLL.setOnClickListener(new View.OnClickListener() {
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

    private void removeFromCart(String cartID, String productID, String productAttributeID)
    {

        System.out.println("getresultcart 1");

        Insider.Instance.itemRemovedFromCart(productID);

        String action="Carts/removeProduct";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_cart",cartID)
                .add("id_product", productID)
                .add("id_product_attribute",productAttributeID)
                .build();
        WebServiceAccessDelete callws = new WebServiceAccessDelete(context, this);
        callws.setAction(action);
        callws.execute(formBody);
    }


    private void startCountDown(long milliseconds, ViewHolder itemHolder) {
        try {


            long diffSeconds = milliseconds / 1000 % 60;
            long diffMinutes = milliseconds / (60 * 1000) % 60;
            long diffHours = milliseconds / (60 * 60 * 1000) % 24;
            long diffDays = milliseconds / (24 * 60 * 60 * 1000);


//            itemHolder.productTimeLeftTV.setText(String.format("%02d:%02d", diffMinutes, diffSeconds));


//            itemHolder.hours_tf.setText(""+diffHours);
//            itemHolder.minutes_tf.setText(""+diffMinutes);
//            itemHolder.seconds_tf.setText(""+diffSeconds);
            //counterTextView.setText(diffDays + " days, "+diffHours + " hours, "+diffMinutes + " minutes, "+diffSeconds + " seconds.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserCartList(shoppingBagItem item, String quantity){

        String action="Carts/updateqtyproduct";

        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_cart",item.getcartID())
                .add("id_product", item.getproductID())
                .add("id_product_attribute", item.getproductAttributeID())
                .add("quantity", quantity)
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(context, this);
        callws.setAction(action);
        callws.execute(formBody);

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){

            try{
                if(result.getString("action").equals("Carts_updateqtyproduct"))
                {
                    if(result.getBoolean("status"))
                    {
                        fragment.getUserCartList();

                        if(!refreshCallApi)
                        {
                            fragment.getUserCartList();
                            refreshCallApi = true;
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(context,
                                "Unable to change quantity at the moment", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                    }
                }
                else if(result.getString("action").equals("Carts_removeProduct"))
                {
                    if(result.getBoolean("status"))
                    {

                        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("giftMessage", "");
                        editor.putString("LeaveMessage", "");
                        editor.apply();
                        JSONArray jsonArray = result.getJSONObject("data").getJSONArray("ProductsInCart");
                        String ItemInCart=String.valueOf(jsonArray.length()-1);
                        if(removeItemTimer){

                        }else{
                            Toast toast = Toast.makeText(context,
                                    "Successfully removed item", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
                        }


                       pref = context.getSharedPreferences("MyPref", 0);
//                        int bagNotiItem = pref.getInt("cartItem", 0);
//
//                        System.out.println("bagNotiItem is "+bagNotiItem);
//
//                        bagNotiItem = bagNotiItem - 1;
//
//                        System.out.println("bagNotiItem2 is "+bagNotiItem);
                        
                        editor = pref.edit();

                        editor.putString("cartItem", ItemInCart);
                        editor.commit();

                        ((MainActivity) context).changeToolBarBagNotiText(ItemInCart);

                        fragment.getUserCartList();

                        if(!refreshCallApi)
                        {
                            fragment.getUserCartList();
                            refreshCallApi = true;
                        }

                    }
                    else
                    {
                        Toast toast = Toast.makeText(context,
                                "Unable to remove cart. Please try again.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
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
    }

    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
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
}
