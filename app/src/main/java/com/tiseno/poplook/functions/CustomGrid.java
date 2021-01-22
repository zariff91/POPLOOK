package com.tiseno.poplook.functions;

/**
 * Created by rahn on 8/28/15.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.LoginFragment;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.ProductListFragment;
import com.tiseno.poplook.R;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPut;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CustomGrid extends RecyclerView.Adapter<CustomGrid.ViewHolder> implements AsyncTaskCompleteListener<JSONObject> {
    Context mcontext;
    ImageLoader imageLoader = ImageLoader.getInstance();
    Boolean imageLoaded = false;
    String out_of_stock, online_exclusive, discount_label, discount_textt;
    String SelectedCountryCurrency, SelectedShopID, wishListingID;
    ArrayList<ProductListItem> data;
    protected RecyclerView.Adapter mAdapter;
    public FirebaseAnalytics mFirebaseAnalytics;
    List<String> listSize = new ArrayList<String>();
    List<String> attributeIDList = new ArrayList<String>();
    ArrayList<savedItemsItem> wishListProdID;
    List<String> prodIDD = new ArrayList<String>();

    Button wishlistBtnclicked;
    int posClicked = 0;
    String SelectedAttributeID = "";
    PopupWindow popupWindow;
    ProductListFragment productListFragment;
    public float dimBack = (float) 0.1;

    public CustomGrid(Context context, ArrayList<ProductListItem> data1, ProductListFragment fragment, ArrayList<savedItemsItem> wishListID) { // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        data = data1;
        mcontext = context;
        productListFragment = fragment;
        wishListProdID = wishListID;
        productListFragment = fragment;

        getSavedItemProductID();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
        SelectedCountryCurrency = pref.getString("SelectedCountryCurrency", "");
        SelectedShopID = pref.getString("SelectedShopID", "1");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_list, viewGroup, false); //Inflating the layout
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mcontext);

        //Creating ViewHolder and passing the object of type view

        return new ViewHolder(v); // Return
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        imageLoader.init(ImageLoaderConfiguration.createDefault(mcontext.getApplicationContext()));

        viewHolder.textView.setText(data.get(i).getname());

        if (prodIDD.contains(data.get(i).getproductID())) {

            wishlistBtnclicked = viewHolder.wishlistBtn;
            wishlistBtnclicked.setBackgroundResource(R.drawable.ic_love2_18dp);

        }


        if (data.get(i).get_collection_name == "null") {

            viewHolder.category_label.setVisibility(View.GONE);


        } else {

            viewHolder.category_label.setText(data.get(i).get_collection_name);

        }

        if (data.get(i).getprice_with_discount().equals("0.00")) {
            if (SelectedCountryCurrency.equalsIgnoreCase("RM")) {
//                viewHolder.textView1.setText(SelectedCountryCurrency + " " + data.get(i).getprice_with_tax());
                viewHolder.textView1.setText(SelectedCountryCurrency + " " + data.get(i).getprice_with_tax());
                viewHolder.discount_TextView.setVisibility(View.GONE);


            } else {
                viewHolder.textView1.setText(SelectedCountryCurrency + " " + data.get(i).getprice_with_tax());
                viewHolder.discount_TextView.setVisibility(View.GONE);
            }
            viewHolder.textView1.setVisibility(View.VISIBLE);
            viewHolder.grid_text_price_discount.setVisibility(View.GONE);
        } else {
//            final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            final ForegroundColorSpan fcs = new ForegroundColorSpan(mcontext.getResources().getColor(R.color.red));
            String s;
            if (SelectedCountryCurrency.equalsIgnoreCase("RM")) {
//                s = SelectedCountryCurrency + " " + data.get(i).getprice_with_tax();
                s = SelectedCountryCurrency + " " + data.get(i).getprice_with_tax() + " ";

                viewHolder.textView1.setText(SelectedCountryCurrency + " " + data.get(i).getPrice_Without_reduction());


            } else {

                s = SelectedCountryCurrency + " " + data.get(i).getprice_with_tax();

                viewHolder.textView1.setText(SelectedCountryCurrency + " " + data.get(i).getPrice_Without_reduction());

            }
            viewHolder.discount_TextView.setText(s, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) viewHolder.discount_TextView.getText();

            spannable.setSpan(fcs, 0, data.get(i).getprice_with_tax().length() + 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            viewHolder.grid_text_price_discount.setVisibility(View.VISIBLE);
//            viewHolder.textView1.setVisibility(View.GONE);
//            viewHolder.textView1.setText("RM " + data.get(i).getprice_with_discount());
//            viewHolder.textView1.setTextColor(mcontext.getResources().getColor(R.color.red));
            viewHolder.textView1.setVisibility(View.VISIBLE);

            viewHolder.textView1.setPaintFlags(viewHolder.textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            viewHolder.grid_text_price_discount.setVisibility(View.GONE);
        }

        viewHolder.textView.setTypeface(FontUtil.getTypeface(mcontext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        viewHolder.textView1.setTypeface(FontUtil.getTypeface(mcontext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        viewHolder.outOFstockTV.setTypeface(FontUtil.getTypeface(mcontext, FontUtil.FontType.AVENIR_ROMAN_FONT));
        viewHolder.category_label.setTypeface(FontUtil.getTypeface(mcontext, FontUtil.FontType.AVENIR_ROMAN_FONT));

        viewHolder.loadingImageList.setVisibility(View.VISIBLE);

        viewHolder.RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productListFragment.clicked(i);


            }

        });
        viewHolder.wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = mcontext.getApplicationContext().getSharedPreferences("MyPref", 0);

                String UserID = pref.getString("UserID", "");

                if (UserID.length() > 0) {


                    if (prodIDD.contains(data.get(i).getproductID())) {

                        for (int j = 0; j < wishListProdID.size(); j++) {

                            final savedItemsItem item = wishListProdID.get(j);

                            if (item.getproductID().equals(data.get(i).getproductID())) {

                                removeFromSavedItems(item.getwishlistID(), item.getproductID(), item.getproductAttributeID());

                            } else {


                            }


                        }

                    }

//                    else
//                    {
//
//                        wishlistBtnclicked.setBackgroundResource(R.drawable.ic_love2_18dp);
//
//
//                    }

                    else {

                        wishlistBtnclicked = viewHolder.wishlistBtn;
                        getProductDetails(data.get(i).getproductID());
                        posClicked = i;
                    }

                } else {
                    new android.app.AlertDialog.Builder(mcontext)
                            .setTitle("Message")
                            .setMessage("To use Save function, please login to your account")
                            .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("LoginFromSavedItem", true);
                                    editor.putString("LoginFromSavedItemProductID", data.get(i).getproductID());
                                    editor.putString("LoginFromSavedItemCatName", data.get(i).getname());
                                    editor.apply();

                                    Fragment fragment = new LoginFragment();

                                    FragmentManager fragmentManager = ((MainActivity) mcontext).getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                    fragmentTransaction.commit();

                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();

                }


            }

        });
//        viewHolder.imageView.setVisibility(View.GONE);
        String imageUrl = data.get(i).getimage_url();
        out_of_stock = data.get(i).getout_of_stock();

        online_exclusive = data.get(i).getOnline_exclusive();

        discount_label = data.get(i).getDiscount();

        discount_textt = data.get(i).getDiscount_text();

        System.out.print("dapatdiscounttext " + discount_textt);

        if (out_of_stock.equals("0")) {

            if (discount_label.equals("1")) {

                viewHolder.oosRL.setVisibility(View.VISIBLE);
                viewHolder.outOFstockTV.setText(discount_textt);

            } else {

                if (online_exclusive.equals("1")) {

                    viewHolder.oosRL.setVisibility(View.VISIBLE);
                    viewHolder.outOFstockTV.setText("ONLINE EXCLUSIVE");

                } else {

                    viewHolder.oosRL.setVisibility(View.GONE);

                }

            }
        } else {

            viewHolder.oosRL.setVisibility(View.VISIBLE);


        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        display(viewHolder.imageView, data.get(i).getimage_url(), options, viewHolder.loadingImageList);

//        Picasso.with(viewHolder.imageView.getContext()).load(data.get(i).getimage_url())
//                .into(viewHolder.imageView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        viewHolder.loadingImageList.setVisibility(View.GONE);
//                        viewHolder.imageView.setVisibility(View.VISIBLE);
////                        if (out_of_stock.equals("0")){
////                            viewHolder.oosRL.setVisibility(View.GONE);
////                        }
////                        else{
////                            viewHolder.oosRL.setVisibility(View.VISIBLE);
////                        }
//                    }
//
//                    @Override
//                    public void onError() {
//                        //error
//                    }
//                });
        if (data.get(i).getTotal_colours().equals("0") || data.get(i).getTotal_colours().equals("") || data.get(i).getTotal_colours() == null) {
            viewHolder.colorGroupingRL.setVisibility(View.INVISIBLE);
            viewHolder.colorGroupingIV1RL.setVisibility(View.GONE);
            viewHolder.colorGroupingIV1BG.setVisibility(View.GONE);
        } else {
            viewHolder.colorGroupingRL.setVisibility(View.VISIBLE);
            viewHolder.colorGroupingIV1RL.setVisibility(View.VISIBLE);
            viewHolder.colorGroupingIV1BG.setVisibility(View.VISIBLE);
            try {
                JSONArray related_colour_data = data.get(i).getRelated_colour_data();

                for (int j = 0; j < related_colour_data.length(); j++) {
                    JSONObject jObject = related_colour_data.getJSONObject(j);
                    if (j < 5) {
                        viewHolder.colorGroupingTV.setVisibility(View.GONE);
                        if (j == 0) {
                            viewHolder.colorGroupingIV2.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV3.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV4.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV1, options);
//                            Picasso.with(viewHolder.colorGroupingIV1.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV1, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 1) {
                            viewHolder.colorGroupingIV2.setVisibility(View.VISIBLE);
                            viewHolder.colorGroupingIV3.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV4.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV2, options);

//                            Picasso.with(viewHolder.colorGroupingIV2.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV2, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 2) {
                            viewHolder.colorGroupingIV3.setVisibility(View.VISIBLE);
                            viewHolder.colorGroupingIV4.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV3, options);

//                            Picasso.with(viewHolder.colorGroupingIV3.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV3, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 3) {
                            viewHolder.colorGroupingIV4.setVisibility(View.VISIBLE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV4, options);
//
//                            Picasso.with(viewHolder.colorGroupingIV4.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV4, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 4) {
                            viewHolder.colorGroupingIV5.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV5, options);

//                            Picasso.with(viewHolder.colorGroupingIV5.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV5, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        }
                    } else {
                        viewHolder.colorGroupingTV.setVisibility(View.VISIBLE);
                        if (j == 0) {
                            viewHolder.colorGroupingIV2.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV3.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV4.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV1, options);
//
//                            Picasso.with(viewHolder.colorGroupingIV1.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV1, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 1) {
                            viewHolder.colorGroupingIV2.setVisibility(View.VISIBLE);
                            viewHolder.colorGroupingIV3.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV4.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV2, options);

//                            Picasso.with(viewHolder.colorGroupingIV2.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV2, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 2) {
                            viewHolder.colorGroupingIV3.setVisibility(View.VISIBLE);
                            viewHolder.colorGroupingIV4.setVisibility(View.GONE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV3, options);

//                            Picasso.with(viewHolder.colorGroupingIV3.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV3, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 3) {
                            viewHolder.colorGroupingIV4.setVisibility(View.VISIBLE);
                            viewHolder.colorGroupingIV5.setVisibility(View.GONE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV4, options);

//                            Picasso.with(viewHolder.colorGroupingIV4.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV4, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        } else if (j == 4) {
                            viewHolder.colorGroupingIV5.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(jObject.getString("image_color_url"), viewHolder.colorGroupingIV5, options);

//                            Picasso.with(viewHolder.colorGroupingIV5.getContext()).load(jObject.getString("image_color_url"))
//                                    .into(viewHolder.colorGroupingIV5, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            //error
//                                        }
//                                    });

                        }
                    }
                }


            } catch (Exception e) {
                viewHolder.colorGroupingRL.setVisibility(View.INVISIBLE);
                viewHolder.colorGroupingIV1RL.setVisibility(View.GONE);
                viewHolder.colorGroupingIV1BG.setVisibility(View.GONE);

            }


        }

    }

    private void getProductDetails(String prodID) {
        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        String action = "Products/details/id/" + prodID + "/shop/" + SelectedShopID + "/lang/1/full/1?apikey=" + apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(mcontext, this);
        callws.execute(action);

    }

    private void saveForLater(String prodID, String prodIDAtt) {
        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        String WishlistID = pref.getString("WishlistID", "");
        String UserID = pref.getString("UserID", "");
        String action = "Wishlists/addproduct";
        if (WishlistID.equals("")) {
            RequestBody formBody = new FormBody.Builder()
                    .add("apikey", apikey)
                    .add("id_customer", UserID)
                    .add("id_product", prodID)
                    .add("id_product_attribute", prodIDAtt)
                    .build();
            WebServiceAccessPut callws = new WebServiceAccessPut(mcontext, this);
            callws.setAction(action);
            callws.execute(formBody);
        } else {
            RequestBody formBody = new FormBody.Builder()
                    .add("apikey", apikey)
                    .add("id_wishlist", WishlistID)
                    .add("id_customer", UserID)
                    .add("id_product", prodID)
                    .add("id_product_attribute", prodIDAtt)
                    .build();
            WebServiceAccessPut callws = new WebServiceAccessPut(mcontext, this);
            callws.setAction(action);
            callws.execute(formBody);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout RL;
        Button wishlistBtn;
        TextView textView1, outOFstockTV, colorGroupingTV;
        ImageView imageView, grid_image_soldOut;
        TextView textView, grid_text_price_discount, category_label, discount_TextView;
        ProgressBar loadingImageList;
        RelativeLayout oosRL, colorGroupingRL, colorGroupingIV1RL;
        ImageView colorGroupingIV1, colorGroupingIV2, colorGroupingIV3, colorGroupingIV4, colorGroupingIV5;
        View colorGroupingIV1BG;

        public ViewHolder(View itemView) {
            super(itemView);
            RL = (RelativeLayout) itemView.findViewById(R.id.RL);
            wishlistBtn = (Button) itemView.findViewById(R.id.wishlistBtn);
            textView = (TextView) itemView.findViewById(R.id.grid_text);
            imageView = (ImageView) itemView.findViewById(R.id.grid_image);
            textView1 = (TextView) itemView.findViewById(R.id.grid_text_price);
            discount_TextView = (TextView) itemView.findViewById(R.id.grid_text_price2);
            loadingImageList = (ProgressBar) itemView.findViewById(R.id.loadingImageProductList);
//            grid_image_soldOut=(ImageView)itemView.findViewById(R.id.grid_image_soldOut);
            oosRL = (RelativeLayout) itemView.findViewById(R.id.oosRL);
            outOFstockTV = (TextView) itemView.findViewById(R.id.outOFstockTV);
            grid_text_price_discount = (TextView) itemView.findViewById(R.id.grid_text_price_discount);
            colorGroupingRL = (RelativeLayout) itemView.findViewById(R.id.colorGroupingRL);
            colorGroupingIV1RL = (RelativeLayout) itemView.findViewById(R.id.colorGroupingIV1RL);
            colorGroupingIV1BG = (View) itemView.findViewById(R.id.colorGroupingIV1BG);
            colorGroupingIV1 = (ImageView) itemView.findViewById(R.id.colorGroupingIV1);
            colorGroupingIV2 = (ImageView) itemView.findViewById(R.id.colorGroupingIV2);
            colorGroupingIV3 = (ImageView) itemView.findViewById(R.id.colorGroupingIV3);
            colorGroupingIV4 = (ImageView) itemView.findViewById(R.id.colorGroupingIV4);
            colorGroupingIV5 = (ImageView) itemView.findViewById(R.id.colorGroupingIV5);
            colorGroupingTV = (TextView) itemView.findViewById(R.id.colorGroupingTV);

            category_label = (TextView) itemView.findViewById(R.id.category_text);
        }
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if (result != null) {
            try {

                if (result.getString("action").equals("Wishlists_removeProduct")) {


                    if (result.getBoolean("status")) {

                        productListFragment.getSavedItems();

                        Toast toast = Toast.makeText(mcontext,
                                "Item Removed", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

                    }


                } else if (result.getString("action").equals("Products_details")) {
                    listSize.clear();
                    JSONObject data1 = result.getJSONObject("data");

                    try {
                        JSONArray jsonArr1 = null;
                        jsonArr1 = data1.getJSONArray("attribute_list");
                        for (int i = 0; i < jsonArr1.length(); i++) {
                            if (jsonArr1.getJSONObject(i).getString("id_attribute") == null) {
                            } else {

                                System.out.println("MASUK SINI GILA");
                                String id_attribute = jsonArr1.getJSONObject(i).getString("id_product_attribute");
                                String group_name = jsonArr1.getJSONObject(i).getString("group_name");
                                String attribute_name = jsonArr1.getJSONObject(i).getString("attribute_name");
                                int quantity = jsonArr1.getJSONObject(i).getInt("quantity");
                                String hide_size = jsonArr1.getJSONObject(i).getString("hide_size");

                                attributeIDList.add(id_attribute);
                                if (hide_size.equals("1")) {
                                    listSize.add(attribute_name + " - Sold Out");

                                } else {
                                    if (quantity <= 0) {
                                        listSize.add(attribute_name + " - Sold Out");
//                                    spinner2.setEnabled(false);
                                    } else {
                                        listSize.add(attribute_name);
//                                    spinner2.setEnabled(true);
                                    }
//
                                }
                            }


                        }
                    } catch (Exception e) {
                        listSize.clear();
                        listSize.add("-");
                        showPopup(wishlistBtnclicked, false);
                    }
                    if (attributeIDList.size() == 0) {
                        listSize.clear();
                        listSize.add("-");
                        showPopup(wishlistBtnclicked, false);

                    } else {
                        showPopup(wishlistBtnclicked, true);
                    }
                } else if (result.getString("action").equals("Wishlists_addproduct")) {

                    String message = result.getString("message");
                    if (message.equals("Successfully Added")) {

                        Toast toast = Toast.makeText(mcontext,
                                "Item Saved", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
//                        wishlistBtnclicked.setBackgroundResource(R.drawable.ic_love2_18dp);
                        popupWindow.dismiss();
                        dimBack = 1;
                        productListFragment.setDimBack(dimBack);
                        productListFragment.getSavedItems();
//                        Insider.Instance.tagEvent(((MainActivity) mcontext),"add_to_favorite");
//
//                        String currency="MYR";
//                        if(SelectedShopID.equalsIgnoreCase("1")){
//                            currency="MYR";
//                        }else if(SelectedShopID.equalsIgnoreCase("2")){
//                            currency="SGD";
//                        }else if(SelectedShopID.equalsIgnoreCase("3")){
//                            currency="USD";
//                        }
//                        Bundle bundle = new Bundle();
//                        bundle.putString(FirebaseAnalytics.Param.QUANTITY, "1");
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, catName);
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, prodname);
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, prodID);
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, SelectedShopID);
//                        bundle.putDouble(FirebaseAnalytics.Param.PRICE, Double.parseDouble(price_with_tax));
//                        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);
//                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle);

                        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);
                        String totalList = pref.getString("wishlistItem", "0");

                        int totalquant = Integer.parseInt("");

                        totalquant += Integer.parseInt(totalList);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("wishlistItem", String.valueOf(totalquant));
                        editor.apply();
                        ((MainActivity) mcontext).changeToolBarWishNotiText(String.valueOf(totalquant));

                    } else {
                        Toast toast = Toast.makeText(mcontext,
                                "This item cannot be saved. Please try again", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                        popupWindow.dismiss();
                        dimBack = 1;
                        productListFragment.setDimBack(dimBack);
                    }

                }
            } catch (Exception e) {

            }

        } else {

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

    public void showPopup(View view, boolean haveSize) {
        if (haveSize) {
            View popupView = LayoutInflater.from(mcontext).inflate(R.layout.popup_layout, null);

            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            ImageButton btnDismiss = (ImageButton) popupView.findViewById(R.id.ib_close);
            btnDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                    dimBack = 1;
                    productListFragment.setDimBack(dimBack);
                }
            });
            ListView listView1 = (ListView) popupView.findViewById(R.id.listView1);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext,
                    android.R.layout.simple_list_item_1, listSize);

            listView1.setAdapter(adapter);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    SelectedAttributeID = attributeIDList.get(position);
                    saveForLater(data.get(posClicked).getproductID(), SelectedAttributeID);

                }
            });
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            productListFragment.setDimBack(dimBack);
        } else {
            saveForLater(data.get(posClicked).getproductID(), data.get(posClicked).getId_product_attribute());
        }

    }

    public void display(final ImageView img, String url, DisplayImageOptions options, final ProgressBar spinner) {

        System.out.println("get image url" + url);

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

    private void removeFromSavedItems(String wishlistID, String productID, String productAttributeID) {


        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0);

        String apikey = pref.getString("apikey", "");
        String UserID = pref.getString("UserID", "");

        String action = "Wishlists/removeProduct";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey", apikey)
                .add("id_wishlist", wishlistID)
                .add("id_customer", UserID)
                .add("id_product", productID)
                .add("id_product_attribute", productAttributeID)
                .build();
        WebServiceAccessDelete callws = new WebServiceAccessDelete(mcontext, this);
        callws.setAction(action);
        callws.execute(formBody);

        System.out.println(" masukkan " + wishlistID + " sini " + productID + " dan sini " + productAttributeID + " sini gak " + apikey + " sini sekali " + UserID);


    }

    public void getSavedItemProductID() {

        for (int j = 0; j < wishListProdID.size(); j++) {

            final savedItemsItem item = wishListProdID.get(j);

            String savedProductID = item.getproductID();

            System.out.println("gotsaveItemhere" + savedProductID);

            prodIDD.add(savedProductID);

        }

    }
}

