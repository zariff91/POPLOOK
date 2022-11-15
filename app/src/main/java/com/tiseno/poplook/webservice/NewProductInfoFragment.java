package com.tiseno.poplook.webservice;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;
import com.tiseno.poplook.InStoreAvailabilityFragment;
import com.tiseno.poplook.MainActivity;
import com.tiseno.poplook.ProductInfoFragment;
import com.tiseno.poplook.R;
import com.tiseno.poplook.ShoppingBagFragment;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.MatchItemWith;
import com.tiseno.poplook.functions.MotherAndDaughterItem;
import com.tiseno.poplook.functions.RecyclerItemClickListener;
import com.tiseno.poplook.functions.SliderImageAdapter;
import com.tiseno.poplook.functions.StyleWithAdapter;
import com.tiseno.poplook.functions.attributeItem;
import com.tiseno.poplook.functions.coloursAdapter;
import com.tiseno.poplook.functions.coloursItem;
import com.tiseno.poplook.functions.shownHereWithItem;
import com.tiseno.poplook.functions.sizequantityitem;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderEvent;
import com.useinsider.insider.InsiderProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class NewProductInfoFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    ImageButton topBackBtn, topCartBtn;
    ImageButton careImageBtn,deliveryImageBtn;

    RelativeLayout careLayout;
    RelativeLayout careWVLayout,deliveryWVLayout;

    LinearLayout deliveryLayout;

    RecyclerView coloursListRecyclerView;
    RecyclerView.Adapter colourListAdapter;
    RecyclerView.LayoutManager colourListLayoutManager;

    ChipCloud chipCloud;
    SliderView imageSlider;
    TextView categoryTxt,productNameTxt,productOnlineExcTxt,productPriceTxt,productDiscountPriceTxt,inStoreTxt,selectSizeTxt,addToCartTxt,itemLeftTxt,detailsTxt,coloursTxt,totalItemInCart,shownHereWithTxt,matchItemWithTxt,motherDaughterTxt,productRefTxt,measurementTxt,careTxt,deliveryTxt;

    WebView measurementsWebView,deliveryWebView,detailsWebView,careWebView;

    TagView shownHereTag,matchItemTag,motherDaughterTag;

    ProgressBar detailWVprogressBar,progressBar2,progressBar3,progressBar4;

    String prodID,catName,SelectedShopID,reference_presta,UserID,LanguageID;
    String price_with_tax,selected_attribute_id = "";
    String[] size_name_array;
    String bagNotiItem;

    private String numberInBag= "0";
    private Animation animShow, animHide;

    ArrayList<Uri> imgList = new ArrayList<>();
    ArrayList<String> sizeforInstore = new ArrayList<String>();
    ArrayList<String> attributeIDList = new ArrayList<>();
    ArrayList<attributeItem> product_sizes_array = new ArrayList<>();
    ArrayList<coloursItem> colour_list_array= new ArrayList<>();
    ArrayList<shownHereWithItem>shownHereWithItemsArr = new ArrayList<shownHereWithItem>();
    ArrayList<MatchItemWith>matchItemWithArr = new ArrayList<MatchItemWith>();
    ArrayList<MotherAndDaughterItem>motherAndDaughterArr = new ArrayList<MotherAndDaughterItem>();

    RelativeLayout addTocartview;

    boolean goToCart = false;
    boolean careClicked=false,deliveryClicked=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).hideTopBar(true);
        ((MainActivity) getActivity()).showBottomBar(false);

        Bundle bundle = this.getArguments();
        prodID = bundle.getString("prodID");
        catName = bundle.getString("catName");

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        UserID = pref.getString("UserID", "");
        LanguageID = pref.getString("LanguageID", "");
        bagNotiItem = pref.getString("cartItem", "0");

        System.out.println("total bag 1 = " + bagNotiItem);


        View v = inflater.inflate(R.layout.new_product_info_frgament, container, false);

        imageSlider = (SliderView)v.findViewById(R.id.imageSliderNew);

        addTocartview = v.findViewById(R.id.addToCartView);
        addTocartview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(goToCart){
                    Fragment fragment = new ShoppingBagFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {

                    if(selected_attribute_id.isEmpty() || selected_attribute_id.length() == 0 || selected_attribute_id.equals("")){
                        Toast toast = Toast.makeText(getActivity(),
                                "Please select size", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                    }

                    else {
                        addToCart(selected_attribute_id, prodID);
                    }
                }

            }
        });

        categoryTxt = v.findViewById(R.id.categoryTextView);
        productNameTxt = v.findViewById(R.id.productTitleTextView);
        productOnlineExcTxt = v.findViewById(R.id.productOnlineExclusiveTV);
        productPriceTxt = v.findViewById(R.id.productPriceTextView);
        productDiscountPriceTxt = v.findViewById(R.id.productDiscountPriceTV);
        inStoreTxt = v.findViewById(R.id.instoreTextView);
        selectSizeTxt = v.findViewById(R.id.selectSizeTextView);
        addToCartTxt = v.findViewById(R.id.addtoCartTextView);
        itemLeftTxt = v.findViewById(R.id.itemLeftTextView);
        totalItemInCart = v.findViewById(R.id.cartItemNumber);
        topBackBtn = v.findViewById(R.id.back_btn);
        topCartBtn = v.findViewById(R.id.cartBtnTop);
        coloursTxt = v.findViewById(R.id.colorsTextView);
        detailsTxt = v.findViewById(R.id.detailsTextView);
        detailsWebView = v.findViewById(R.id.details_webview);
        detailWVprogressBar = v.findViewById(R.id.loading_details_webview);
        shownHereWithTxt = v.findViewById(R.id.shownHereWithTextView);
        matchItemWithTxt = v.findViewById(R.id.matchItemWithTextView);
        motherDaughterTxt = v.findViewById(R.id.motherAndDaughterTextView);
        productRefTxt = v.findViewById(R.id.productInfoRefNoTextView);
        measurementTxt = v.findViewById(R.id.measurementsTextView);
        measurementsWebView = v.findViewById(R.id.measurement_webview);
        progressBar2 = v.findViewById(R.id.measurement_webview_loading);
        progressBar3 = v.findViewById(R.id.care_loading_webview);

        animShow = AnimationUtils.loadAnimation(getActivity(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(getActivity(), R.anim.view_hide);

        careTxt = v.findViewById(R.id.careTextView);
        careLayout = v.findViewById(R.id.care_parent_layout);
        careWVLayout = v.findViewById(R.id.care_webview_layout);
        careWebView = v.findViewById(R.id.care_webview);
        careImageBtn = v.findViewById(R.id.careImageButton);
        careLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!careClicked) {
                    careWVLayout.setVisibility(View.VISIBLE);
//                    careWVLayout.startAnimation(animShow);
                    careImageBtn.setImageResource(R.drawable.btn_dropdown);
                    careClicked=true;
                }else{
                    animHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            careWVLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                    });
                    careWVLayout.startAnimation(animHide);
                    careImageBtn.setImageResource(R.drawable.icon_go);
                    careClicked=false;
                }

            }

        });

        deliveryWVLayout = v.findViewById(R.id.delivery_webview_layout);
        deliveryWebView = v.findViewById(R.id.delivery_webview);
        progressBar4 = v.findViewById(R.id.delivery_webview_loading);
        deliveryImageBtn = v.findViewById(R.id.deliveryImageButton);
        deliveryTxt = v.findViewById(R.id.DeliveryTextView);
        deliveryLayout = v.findViewById(R.id.delivery_parent_layout);
        deliveryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!deliveryClicked) {
                    deliveryWVLayout.setVisibility(View.VISIBLE);
                    deliveryImageBtn.setImageResource(R.drawable.btn_dropdown);
                    deliveryWVLayout.startAnimation(animShow);
                    deliveryClicked=true;
                }else{
                    animHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            deliveryWVLayout.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                        @Override
                        public void onAnimationStart(Animation animation) {}
                    });
                    deliveryWVLayout.startAnimation(animHide);
                    deliveryImageBtn.setImageResource(R.drawable.icon_go);
                    deliveryClicked=false;
                }



            }

        });

        detailsWebView.getSettings().setJavaScriptEnabled(true);
        detailsWebView.getSettings().setSupportZoom(false);
        detailsWebView.getSettings().setBuiltInZoomControls(true);
        detailsWebView.getSettings().setDisplayZoomControls(false);
        detailsWebView.getSettings().setLoadWithOverviewMode(true);
        detailsWebView.getSettings().setUseWideViewPort(false);

        shownHereTag = v.findViewById(R.id.shown_here_tagView);
        matchItemTag = v.findViewById(R.id.match_item_tagView);
        motherDaughterTag = v.findViewById(R.id.motherDaughter_tagView);

        coloursListRecyclerView = v.findViewById(R.id.coloursOptionRecyclerView);
        colourListLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        coloursListRecyclerView.setHasFixedSize(true);
        coloursListRecyclerView.setLayoutManager(colourListLayoutManager);

        categoryTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        productNameTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        productOnlineExcTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        productPriceTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        productDiscountPriceTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        inStoreTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        addToCartTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        selectSizeTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        productRefTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        itemLeftTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        coloursTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        detailsTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        shownHereWithTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        matchItemWithTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        motherDaughterTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        measurementTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        careTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        deliveryTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));


        topBackBtn.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {


                                              getActivity().onBackPressed();

                                          }

                                      }

        );
        topCartBtn.setOnClickListener(new View.OnClickListener() {

                                          @Override
                                          public void onClick(View v) {

                                              Fragment fragment = new ShoppingBagFragment();
                                              FragmentManager fragmentManager = getFragmentManager();
                                              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                                              fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                              fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
                                              fragmentTransaction.addToBackStack(null);
                                              fragmentTransaction.commit();

//                Fragment fragment = new NewCartAndWishlistFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("forWishlist", "0");
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "NewCartAndWishlistFragment");
//                fragmentTransaction.commit();


                                              ((MainActivity) getActivity()).getSupportActionBar().show();
                                              ((MainActivity) getActivity()).showBottomBar(true);
//
//                Fragment fragment = new NewCartAndWishlistFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                Bundle bundle = new Bundle();
//                bundle.putString("forWishlist", "0");
//                fragment.setArguments(bundle);
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "NewCartAndWishlistFragment");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

                                          }

        });
        chipCloud = v.findViewById(R.id.select_size_chip);

        changeToolBarBagNotiText(bagNotiItem);

        getProductDetails();

        return v;

    }

    private void getProductDetails(){

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Products/details/id/"+prodID+"/shop/"+SelectedShopID+"/lang/1/full/1?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    public void addToCart(String attributeID, String product_ID) {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String cart_id = pref.getString("CartID", "0");

//        if(cartError)
//        {
//            cart_id = "0";
//        }
//

        String action="Carts/add";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_customer",UserID)
                .add("id_cart", cart_id)
                .add("id_lang", LanguageID)
                .add("id_product", product_ID)
                .add("id_product_attribute", attributeID)
                .add("quantity", "1")
                .add("id_shop",SelectedShopID)
                .build();

        WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);

    }

        @Override
    public void onTaskComplete(JSONObject result) {

        if(result!=null) {


            try {
                if (result.getBoolean("status")) {

                    //                    list.clear();
                    if (result.getString("action").equals("Products_details")) {

                        imgList.clear();

                        JSONObject data = result.getJSONObject("data");
                        JSONArray jsonArr = null;
                        jsonArr = data.getJSONArray("image_url");

                        for (int j = 0; j < jsonArr.length(); j++) {

                            imgList.add(Uri.parse(jsonArr.getString(j)));

//                            SliderImageAdapter adapter = new SliderImageAdapter(getActivity(), imgList);
////
//                            imageSlider.setSliderAdapter(adapter);
//
//                            imageSlider.setIndicatorAnimation(IndicatorAnimationType.SLIDE); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                            imageSlider.setIndicatorSelectedColor(Color.WHITE);
//                            imageSlider.setIndicatorUnselectedColor(Color.GRAY);


                        }

                        String productName = data.getString("name");
                        productNameTxt.setText(productName);

                        if (data.isNull("collection_name")) {
                            categoryTxt.setVisibility(View.GONE);
                        } else {
                            String category = data.getString("collection_name");
                            categoryTxt.setVisibility(View.VISIBLE);
                            categoryTxt.setText(category);
                        }


                        String currency_sign = data.getString("currency_sign");
                        String price_without_reduction = data.getString("price_without_reduction");
                        String price_reduction = data.getString("price_reduction");
                        price_with_tax = data.getString("price_tax_inc");

                        if(price_reduction.equals("0.00")){
                            if(currency_sign.equalsIgnoreCase("RM")) {
                                productPriceTxt.setText("RM " + price_with_tax + " ");

                            }else{
                                productPriceTxt.setText(currency_sign +" "+ price_with_tax);
                            }
                            productPriceTxt.setVisibility(View.VISIBLE);
                            productDiscountPriceTxt.setVisibility(View.GONE);
                        }
                        else{
                            final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
                            final ForegroundColorSpan fcs = new ForegroundColorSpan(getActivity().getResources().getColor(R.color.red));
                            String s="";
                            if(currency_sign.equalsIgnoreCase("RM")) {
                                s = "RM " + price_without_reduction + " RM " + price_with_tax + " ";
                                productDiscountPriceTxt.setText(s, TextView.BufferType.SPANNABLE);
                                Spannable spannable = (Spannable)productDiscountPriceTxt.getText();
                                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, price_with_tax.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(fcs, price_without_reduction.length()+3, price_without_reduction.length()+3+price_with_tax.length()+4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                productDiscountPriceTxt.setVisibility(View.VISIBLE);
                                productPriceTxt.setVisibility(View.GONE);
                            }else{
                                s = currency_sign +" "+ price_without_reduction + " " +currency_sign +" "+ price_with_tax;
                                productDiscountPriceTxt.setText(s, TextView.BufferType.SPANNABLE);
                                Spannable spannable = (Spannable)productDiscountPriceTxt.getText();
                                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, price_with_tax.length()+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(fcs, price_without_reduction.length()+4, price_without_reduction.length()+4+price_with_tax.length()+5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                productDiscountPriceTxt.setVisibility(View.VISIBLE);
                                productPriceTxt.setVisibility(View.GONE);
                            }
                        }

                        try {

                            JSONArray attributeList = data.getJSONArray("attribute_list");

                            product_sizes_array.clear();
                            sizeforInstore.clear();
                            attributeIDList.clear();

                            for (int i = 0; i < attributeList.length(); i++) {
                                if (attributeList.getJSONObject(i).getString("id_attribute") == null) {
                                } else {

                                    String id_attribute = attributeList.getJSONObject(i).getString("id_product_attribute");
                                    String group_name = attributeList.getJSONObject(i).getString("group_name");
                                    String attribute_name = attributeList.getJSONObject(i).getString("attribute_name");
                                    int quantity = attributeList.getJSONObject(i).getInt("quantity");
                                    String hide_size = attributeList.getJSONObject(i).getString("hide_size");

                                    System.out.println("get size ID = " + id_attribute);

                                    sizeforInstore.add(attribute_name);
                                    attributeIDList.add(id_attribute);
//                                listQuantSize.add(new sizequantityitem(attribute_name, String.valueOf(quantity)));
//                                listQuant.add(String.valueOf(quantity));
                                    if (hide_size.equals("1")) {
                                        product_sizes_array.add(new attributeItem(id_attribute, attribute_name + " - Sold Out", quantity));
                                    } else {
                                        if (quantity <= 0) {
                                            product_sizes_array.add(new attributeItem(id_attribute, attribute_name + " - Sold Out", quantity));
                                        } else {
                                            product_sizes_array.add(new attributeItem(id_attribute, attribute_name, quantity));
                                        }
//
                                    }
                                }

                                size_name_array = new String[product_sizes_array.size()];

                                for (int x = 0; x < product_sizes_array.size(); x++) {

                                    String sizes;

                                    sizes = product_sizes_array.get(x).getattributeName();

                                    size_name_array[x] = sizes;

                                }

                                chipCloud.setMode(ChipCloud.Mode.SINGLE);

                                new ChipCloud.Configure()
                                        .chipCloud(chipCloud)
                                        .selectedColor(Color.parseColor("#000000"))
                                        .selectedFontColor(Color.parseColor("#ffffff"))
                                        .deselectedColor(Color.parseColor("#e1e1e1"))
                                        .deselectedFontColor(Color.parseColor("#333333"))
                                        .labels(size_name_array)
                                        .gravity(ChipCloud.Gravity.LEFT)
                                        .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                                        .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                                        .chipListener(new ChipListener() {
                                            @Override
                                            public void chipSelected(int index) {

                                                int quantity = product_sizes_array.get(index).getQuantity();

//                                            if(quantity<5){
                                                itemLeftTxt.setVisibility(View.VISIBLE);
                                                itemLeftTxt.setText(quantity + " items left");
//                                            }
//
                                                selected_attribute_id = product_sizes_array.get(index).getattributeID();
//
//                                            System.out.println("getSelectedIndexSize " + SelectedAttID);
//
                                                goToCart = false;
//                                            btn_add_prodinfo_text.setText("ADD TO CART");
//                                            addBtn.setBackgroundColor(Color.parseColor("#1CAE49"));
//
//                                                quantityValue = convertedVal;
//
//                                                minteger = 1;
//                                                selectAmount.setText("" + minteger);

                                            }

                                            @Override
                                            public void chipDeselected(int index) {

                                                selected_attribute_id = "";
//
                                                goToCart = false;
//                                            btn_add_prodinfo_text.setText("ADD TO CART");
//                                            addBtn.setBackgroundColor(Color.parseColor("#1CAE49"));

                                            }
                                        })
                                        .build();
                            }
                        }
                        catch (Exception e){

                            chipCloud.setVisibility(View.GONE);
                        }

                        String reference = data.getString("reference");
                        reference_presta = reference;
                        inStoreTxt.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {


                                Fragment fragment = new InStoreAvailabilityFragment();

                                String first_image_url = imgList.get(0).toString();

                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("sizes", (ArrayList<String>) sizeforInstore);
                                bundle.putStringArrayList("attID", (ArrayList<String>) attributeIDList);
                                bundle.putString("Product_ID", reference_presta);
                                bundle.putString("Product_Name", productName);
                                bundle.putString("Product_Price", price_with_tax);
                                bundle.putString("image_url", first_image_url);


                                fragment.setArguments(bundle);

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "InStoreAvailabilityFragment");
//                fragmentTransaction.add(R.id.fragmentContainer,new InStoreAvailabilityFragment());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }

                        });

                        colour_list_array.clear();

                        try{
                            JSONArray colourList = data.getJSONArray("color_related");

                            for(int i = 0; i < colourList.length(); i++)
                            {
                                String id_product =  colourList.getJSONObject(i).getString("id_product");
                                String image_color_url = colourList.getJSONObject(i).getString("image_color_url");

                                System.out.println("COLORIDPROD "+id_product);
                                colour_list_array.add(new coloursItem(id_product, image_color_url));
                            }

                            if(colour_list_array.size() == 0)
                            {
                                coloursListRecyclerView.setVisibility(View.GONE);
                            }

                            colourListAdapter = new coloursAdapter(getActivity(),colour_list_array);
                            coloursListRecyclerView.setAdapter(colourListAdapter);
                            coloursListRecyclerView.addOnItemTouchListener(
                                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                            Fragment fragment = new ProductInfoFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("prodID", colour_list_array.get(position).getColorProdID());
                                            bundle.putString("catName","");
                                            fragment.setArguments(bundle);
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }

                                    })

                            );

                        }
                        catch (Exception e){
                            coloursListRecyclerView.setVisibility(View.GONE);
                            coloursTxt.setVisibility(View.GONE);
                        }

                        String desc = data.getString("description");

                        detailsWebView.loadDataWithBaseURL("", "<style>img{display: inline; height: auto; max-width: 100%;}</style>"+desc, "text/html", "UTF-8", "");

                        detailsWebView.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                detailsWebView.setVisibility(View.VISIBLE);
                                detailWVprogressBar.setVisibility(View.GONE);
                            }
                        });

                        shownHereWithItemsArr.clear();

                        try{
                            JSONArray jsonArr3 = null;
                            jsonArr3 = data.getJSONArray("shown_here_with");
                            for(int i = 0; i < jsonArr3.length(); i++)
                            {


                                String shownHereWithID =  jsonArr3.getJSONObject(i).getString("id_product");
                                String shownHereName = jsonArr3.getJSONObject(i).getString("name");
                                String imageLink = jsonArr3.getJSONObject(i).getString("image_url");
                                String price = jsonArr3.getJSONObject(i).getString("price");

                                shownHereWithItemsArr.add(new shownHereWithItem(shownHereWithID,shownHereName));

                            }

//                            shownAdapter = new StyleWithAdapter(getActivity(),shownItems);

//                            shownItemRecycler.setAdapter(shownAdapter);
//                            shownItemRecycler.addOnItemTouchListener(
//                                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(View view, int position) {
//
//                                            Fragment fragment = new ProductInfoFragment();
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("prodID", shownItems.get(position).getstyleWithID());
//                                            bundle.putString("catName", catName);
//                                            fragment.setArguments(bundle);
//                                            FragmentManager fragmentManager = getActivity().getFragmentManager();
//                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//                                            fragmentTransaction.addToBackStack(null);
//                                            fragmentTransaction.commit();
//                                            mItems.clear();
//                                            mColoursItems.clear();
//                                        }
//
//                                    })
//                            );



                            String[] title = new String[shownHereWithItemsArr.size()];

                            ArrayList<Tag> tags = new ArrayList<>();

                            Tag tag;

                            for (int i = 0; i <shownHereWithItemsArr.size(); i++)
                            {

                                String substr;

                                int v =shownHereWithItemsArr.get(i).shownWithName().length();

                                if (v > 45) {

                                    substr = shownHereWithItemsArr.get(i).shownWithName().substring(0,43);

                                }

                                else {

                                    substr = shownHereWithItemsArr.get(i).shownWithName();

                                }


                                tag = new Tag(substr);
                                tag.layoutColor = Color.rgb(28,174,73);
                                tags.add(tag);


                                title[i] = substr;
                            }

                            shownHereTag.addTags(tags);

                            shownHereTag.setOnTagClickListener(new TagView.OnTagClickListener() {
                                @Override
                                public void onTagClick(Tag tag, int position) {

//                                    Fragment fragment = new ProductInfoFragment();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("prodID", shownHereWithItemsArr.get(position).shownWithID());
//                                    bundle.putString("catName", catName);
//                                    fragment.setArguments(bundle);
//                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
//                                    fragmentTransaction.addToBackStack(null);
//                                    fragmentTransaction.commit();
//
//                                    shownHereWithItemsArr.clear();
//                                    matchItemWithArr.clear();
//                                    motherAndDaughterArr.clear();

                                }
                            });

                        }
                        catch (Exception e) {
                            shownHereWithTxt.setVisibility(View.GONE);
                            shownHereTag.setVisibility(View.GONE);
                        }

                        matchItemWithArr.clear();

                        try{
                            JSONArray jsonArr3 = null;
                            jsonArr3 = data.getJSONArray("match_item_with");
                            for(int i = 0; i < jsonArr3.length(); i++)
                            {


                                String matchItemWithID =  jsonArr3.getJSONObject(i).getString("id_product");
                                String matchItemName = jsonArr3.getJSONObject(i).getString("name");

                                matchItemWithArr.add(new MatchItemWith(matchItemWithID,matchItemName));

                            }

                            String[] title = new String[matchItemWithArr.size()];

                            ArrayList<Tag> tags2 = new ArrayList<>();
                            Tag tag2;

                            for (int i = 0; i <matchItemWithArr.size(); i++)
                            {

                                String substr;

                                int v = matchItemWithArr.get(i).matchItemName().length();

                                if (v > 45) {

                                    substr = matchItemWithArr.get(i).matchItemName().substring(0,43);

                                }

                                else {

                                    substr = matchItemWithArr.get(i).matchItemName();

                                }

                                tag2 = new Tag(substr);
                                tag2.layoutColor = Color.rgb(24,174,73);
                                tags2.add(tag2);

                                title[i] = substr;
                            }

                            matchItemTag.addTags(tags2);

                            matchItemTag.setOnTagClickListener(new TagView.OnTagClickListener() {
                                @Override
                                public void onTagClick(Tag tag, int position) {

//                                    Fragment fragment = new ProductInfoFragment();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("prodID", matchItemWithArr.get(position).matchItemWithID());
//                                    bundle.putString("catName", catName);
//                                    fragment.setArguments(bundle);
//                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
//                                    fragmentTransaction.addToBackStack(null);
//                                    fragmentTransaction.commit();
//
//                                    shownHereWithItemsArr.clear();
//                                    matchItemWithArr.clear();
//                                    motherAndDaughterArr.clear();

                                }
                            });

                        }
                        catch (Exception e) {
                            matchItemWithTxt.setVisibility(View.GONE);
                            matchItemTag.setVisibility(View.GONE);
                        }

                        motherAndDaughterArr.clear();

                        try{
                            JSONArray jsonArr3 = null;
                            jsonArr3 = data.getJSONArray("mother_daughter_with");
                            for(int i = 0; i < jsonArr3.length(); i++)
                            {


                                String motherDaughterID =  jsonArr3.getJSONObject(i).getString("id_product");
                                String motherDaughterName = jsonArr3.getJSONObject(i).getString("name");

                                motherAndDaughterArr.add(new MotherAndDaughterItem(motherDaughterID,motherDaughterName));

                            }


                            String[] title = new String[motherAndDaughterArr.size()];

                            ArrayList<Tag> tags3 = new ArrayList<>();
                            Tag tag3;

                            for (int i = 0; i <motherAndDaughterArr.size(); i++)
                            {

                                String substr;

                                int v = motherAndDaughterArr.get(i).motherAndDaughterName().length();

                                if (v > 45) {

                                    substr = motherAndDaughterArr.get(i).motherAndDaughterName().substring(0,43);

                                }

                                else {

                                    substr = motherAndDaughterArr.get(i).motherAndDaughterName();

                                }


                                title[i] = substr;

                                tag3 = new Tag(substr);
                                tag3.layoutColor = Color.rgb(28,174,73);
                                tags3.add(tag3);


                            }

                            motherDaughterTag.addTags(tags3);

                            motherDaughterTag.setOnTagClickListener(new TagView.OnTagClickListener() {
                                @Override
                                public void onTagClick(Tag tag, int position) {

//                                    Fragment fragment = new ProductInfoFragment();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("prodID", motherAndDaughterArr.get(position).motherAndDaughterID());
//                                    bundle.putString("catName", catName);
//                                    fragment.setArguments(bundle);
//                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
//                                    fragmentTransaction.addToBackStack(null);
//                                    fragmentTransaction.commit();
//
//                                    shownHereWithItemsArr.clear();
//                                    matchItemWithArr.clear();
//                                    motherAndDaughterArr.clear();

                                }
                            });


                        }
                        catch (Exception e) {
                            motherDaughterTag.setVisibility(View.GONE);
                            motherDaughterTxt.setVisibility(View.GONE);
                        }

                        if(reference.length() == 0 || reference.equals(null)|| reference.isEmpty() || reference.equalsIgnoreCase("") || reference.equalsIgnoreCase("null"))
                        {

                            productRefTxt.setText("Reference Number: -");
                        }else{
                            productRefTxt.setText("Reference Number: " + reference);
                        }

                        String measurements = data.getString("measurements");

                        measurementsWebView.loadDataWithBaseURL("","<style>img{display: inline; height: auto; max-width: 100%;}</style>"+ measurements, "text/html", "UTF-8", "");

                        measurementsWebView.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                measurementsWebView.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                            }
                        });

                        JSONObject careObj = data.getJSONObject("care");
                        String care= careObj.getString("content");

                        careWebView.loadDataWithBaseURL("","<style>img{display: inline; height: auto; max-width: 100%;-webkit-tap-highlight-color: rgba(0,0,0,0);}</style>"+ care, "text/html", "UTF-8", "");

                        careWebView.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                careWebView.setVisibility(View.VISIBLE);
                                progressBar3.setVisibility(View.GONE);
                            }
                        });

                        JSONObject deliveryObj = data.getJSONObject("delivery_returns");
                        String delivery= deliveryObj.getString("content");

                        deliveryWebView.loadDataWithBaseURL("", "<style>img{display: inline; height: auto; max-width: 100%;}</style>"+ delivery , "text/html", "UTF-8", "");
                        deliveryWebView.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                deliveryWebView.setVisibility(View.VISIBLE);
                                progressBar4.setVisibility(View.GONE);
                            }
                        });

                        InsiderEvent lastVisitEvent = Insider.Instance.tagEvent("last_product_viewed");
                        lastVisitEvent.build();

                        Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_visit_product",productName);
                        Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_visit_product_id",prodID);
                    }

                    else if (result.getString("action").equals("Carts_add")) {
                        String message = result.getString("message");
                        String cartID = result.getJSONObject("data").getString("id_cart");

                        if (message.equals("Item added")) {

                            Toast toast = Toast.makeText(getActivity(),
                                    "Item Added", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                            System.out.println("added cart = " + result);

                            goToCart = true;
                            addToCartTxt.setText("Go to bag");
                            addTocartview.setBackgroundColor(Color.parseColor("#199453"));

                            int itemInBag = Integer.valueOf(numberInBag) + 1;

                            System.out.println("total bag 3 = " + String.valueOf(bagNotiItem));


//                            System.out.println("ITEM" + bagNotiItem);

//                            Insider.Instance.tagEvent(getActivity(),"item_added_to_cart");
//                            Insider.Instance.tagEvent("item_added_to_cart").build();

                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
//                            String totalBag = pref.getString("cartItem", "0");


//                            String[] taxonomy = {catName};
//                            InsiderProduct product = Insider.Instance.createNewProduct(prodID, prodname, taxonomy, imageURL, productPrice, currency);
//                            Insider.Instance.itemAddedToCart(product);

//                            Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_added_to_cart",prodID);


//                            int totalquant = Integer.parseInt(quantity);
//
//                            Insider.Instance.getCurrentUser().setCustomAttributeWithInt("basket_item_quantity", totalquant);


//                            totalquant += Integer.parseInt(totalBag);
//
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("cartItem", String.valueOf(itemInBag));
                            editor.putString("CartID", cartID);
                            editor.apply();

                            ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(itemInBag));
                            changeToolBarBagNotiText(String.valueOf(itemInBag));


                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeToolBarBagNotiText(String txt){
        numberInBag=txt;

        System.out.println("total bag 2 = " + txt);

        if (numberInBag.equals("-1")||numberInBag.equals("-2")||numberInBag.equals("0")||numberInBag.isEmpty()||numberInBag.equalsIgnoreCase("") || numberInBag.equals(null)||numberInBag.equals("null"))
        {
            totalItemInCart.setVisibility(View.INVISIBLE);
            totalItemInCart.setText("");
        } else {

            totalItemInCart.setVisibility(View.VISIBLE);
            totalItemInCart.setText(numberInBag);
        }
    }
}
