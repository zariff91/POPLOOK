package com.tiseno.poplook;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
//import com.amar.library.ui.StickyScrollView;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.HackyViewPager;
import com.tiseno.poplook.functions.RecyclerItemClickListener;
import com.tiseno.poplook.functions.SimpleDividerItemDecoration;
import com.tiseno.poplook.functions.StyleWithAdapter;
import com.tiseno.poplook.functions.StyleWithItem;
import com.tiseno.poplook.functions.URLSpanNoUnderline;
import com.tiseno.poplook.functions.ViewPagerAdapter;
import com.tiseno.poplook.functions.coloursAdapter;
import com.tiseno.poplook.functions.coloursItem;
import com.tiseno.poplook.functions.sizequantityitem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPut;
import com.tiseno.poplook.functions.shownHereWithItem;
import com.tiseno.poplook.functions.MatchItemWith;
import com.tiseno.poplook.functions.MotherAndDaughterItem;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderEvent;
import com.useinsider.insider.InsiderProduct;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class ProductInfoFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{
    public FirebaseAnalytics mFirebaseAnalytics;
    String prodname = "";
    String price_with_tax="0.00";
    int saveBtnClick=0;
    String UserID, CartID="0", LanguageID,link_rewrite;
    String id_product_attribute = "";
    TextView colorTV,productInfoTitleTV,productInfoPriceTV,productInfoRefNoTV,sizeTV,quantityTV,detailsTV,measurementsTV,careTV,DeliveryTV,soldOutInfo,productInfoDiscountPriceTV,shownHereLabel,matchItemLabel,motherDaughterLabel, categoryLabel, onlineExLabel;
    TextView sizeSelectTv, noSizeTv;
    Spinner spinner2;
    Spinner spinner;
    private static final String TAG = "RecyclerViewFragment";
    ArrayList<Uri> imgList = new ArrayList<Uri>();
    ViewPager productViewVP;
    PagerAdapter adapter;
    Uri[] imageListProduct;
    ProgressBar progressBar1,progressBar2,progressBar3,progressBar4;
    TextView btn_add_prodinfo_text,styleITwithTV;
    WebView measurementsWV,webview_Delivery,detailWV,careWV;
    Button saveBtn,btn_share_prodinfo, saleButton;
    RelativeLayout addBtn,btn_share_touch_lay;
//    StickyScrollView sv;
    ImageButton careImageBtn,deliveryImageBtn;
    LinearLayout ly1,deliveryOpenLayout,careOpenLayout;
    RelativeLayout layoutAdd;

    TagView shownHereTag,matchItemTag,motherDaughterTag;

    RelativeLayout addToCartBottomView;


    private Animation animShow, animHide;
    String size,quantity;
    protected RecyclerView mRecyclerView,ColoursRecyclerView,shownItemRecycler;
    protected RecyclerView.Adapter mAdapter,mColoursAdapter,shownAdapter;
    protected RecyclerView.LayoutManager mLayoutManager,mColoursLayoutManager,shownItemLayoutManager;
    protected int scrollPosition = 0;
    CirclePageIndicator imageIndicator;
    Object item1,item2;
    List<String> listSize = new ArrayList<String>();
    List<String> sizeforInstore = new ArrayList<String>();

    List<String> list = new ArrayList<String>();
    ArrayList<sizequantityitem> listQuantSize = new ArrayList<sizequantityitem>();
    ArrayList<StyleWithItem> mItems= new ArrayList<StyleWithItem>();
    ArrayList<StyleWithItem> shownItems= new ArrayList<StyleWithItem>();

    ArrayList<coloursItem> mColoursItems= new ArrayList<coloursItem>();
    ArrayList<shownHereWithItem>shownHereWithItemsArr = new ArrayList<shownHereWithItem>();
    ArrayList<MatchItemWith>matchItemWithArr = new ArrayList<MatchItemWith>();
    ArrayList<MotherAndDaughterItem>motherAndDaughterArr = new ArrayList<MotherAndDaughterItem>();

    int parsedProdID,parsedQuantity;
    String bagNotiItem;
    String prodID,catName,WishlistID;
    String collection_name_true;
    String out_of_stock;
    Boolean IsVideoAvailable = false;
    LinearLayout ProductInfoStyleWtithItLL,ColourLL;
    ImageView sizeDividerIV;
    RelativeLayout careLayout,deliveryLayout,btn_save_touch;
    private static final String ISLOCKED_ARG = "isLocked";
    List<String> attributeIDList = new ArrayList<String>();
    List<String> listQuant = new ArrayList<String>();
    String SelectedAttributeID="",SelectedShopID,quant;
    boolean careClicked=false,deliveryClicked=false,sizeSelect=false,quantitySelect=false,isAddedTocart=false;

    String SelectedAttID;

    String selectedSize;


    String[] itemSizeAvailableArray;

    ChipCloud chipCloud;

    ImageButton backButton, shoppingBagBtn_prodInfo;

    boolean goToCart = false;

    boolean cartError = false;

    private String numberInBag= "0";
    TextView bagNoti_prod,inStoreTextView;

    String reference_presta;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) productViewVP).setLocked(isLocked);
        }
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        if(!pref.getString("comeFromNotification", "0").equals("0")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("comeFromNotification", "0");
            editor.putString("productID", "");
            editor.putString("productName", "");
            editor.apply();
        }

        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        view.setTag(TAG);
        mFirebaseAnalytics =FirebaseAnalytics.getInstance(getActivity());


        Bundle bundle = this.getArguments();
        prodID = bundle.getString("prodID");
        catName = bundle.getString("catName");

        System.out.println("sininsi " + prodID);

        ((MainActivity) getActivity()).changeToolBarText(catName);
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).showBottomBar(false);
        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).disableExpandToolbar(true);


        inStoreTextView = (TextView)view.findViewById(R.id.inStoreText);
        colorTV = (TextView)view.findViewById(R.id.colorTV);
        soldOutInfo  = (TextView)view.findViewById(R.id.soldOutInfo);
        productInfoTitleTV = (TextView)view.findViewById(R.id.productInfoTitleTV);
        productInfoPriceTV = (TextView)view.findViewById(R.id.productInfoPriceTV);
        productInfoDiscountPriceTV = (TextView)view.findViewById(R.id.productInfoDiscountPriceTV);
        productInfoRefNoTV = (TextView)view.findViewById(R.id.productInfoRefNoTV);
        sizeTV = (TextView)view.findViewById(R.id.sizeTV);
        sizeSelectTv = (TextView)view.findViewById(R.id.productInfoSizeTV);
        noSizeTv = (TextView)view.findViewById(R.id.noSizeTV);
        quantityTV = (TextView)view.findViewById(R.id.quantityTV);
        detailsTV = (TextView)view.findViewById(R.id.detailsTV);
        measurementsTV = (TextView)view.findViewById(R.id.measurementsTV);
        careTV = (TextView)view.findViewById(R.id.careTV);
        DeliveryTV = (TextView)view.findViewById(R.id.DeliveryTVPI);
        imageIndicator = (CirclePageIndicator)view.findViewById(R.id.welcomePageIndicator);
        sizeDividerIV = (ImageView)view.findViewById(R.id.sizeDividerIV);
        animShow = AnimationUtils.loadAnimation(getActivity(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(getActivity(), R.anim.view_hide);
        styleITwithTV = (TextView)view.findViewById(R.id.styleITwithTV);
        ProductInfoStyleWtithItLL = (LinearLayout)view.findViewById(R.id.ProductInfoStyleWtithItLL);
        ColourLL = (LinearLayout)view.findViewById(R.id.ColourLL);
        btn_share_touch_lay = (RelativeLayout) view.findViewById(R.id.btn_share_touch_lay);
        btn_share_prodinfo = (Button) view.findViewById(R.id.btn_share_prodinfo2);
//        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
//        CartID = pref.getString("CartID", "");
        LanguageID = pref.getString("LanguageID", "");
        bagNotiItem = pref.getString("cartItem", "0");
        WishlistID = pref.getString("WishlistID", "");
        SelectedShopID  = pref.getString("SelectedShopID", "1");

        careLayout = (RelativeLayout)view.findViewById(R.id.careLayout);
        deliveryLayout = (RelativeLayout)view.findViewById(R.id.deliveryLayout);
        ly1= (LinearLayout)view.findViewById(R.id.ly1);
//        sv = (StickyScrollView) view.findViewById(R.id.sv);
        spinner = (Spinner) view.findViewById(R.id.spinner_size);
        spinner2 = (Spinner) view.findViewById(R.id.spinner_quantity);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ProductInfoRecyclerView);
//        shownItemRecycler =(RecyclerView) view.findViewById(R.id.shownItemRecyclerView);
        ColoursRecyclerView = (RecyclerView) view.findViewById(R.id.ColoursRecyclerView);
        productViewVP = (HackyViewPager)view.findViewById(R.id.productViewVP);
        addBtn = (RelativeLayout) view.findViewById(R.id.btn_add_prodinfo2);
        btn_add_prodinfo_text = (TextView)view.findViewById(R.id.btn_add_prodinfo_text2);
        saveBtn = (Button)view.findViewById(R.id.btn_save_prodinfo2);
        btn_save_touch = (RelativeLayout)view.findViewById(R.id.btn_save_touch_lay);
        deliveryOpenLayout= (LinearLayout)view.findViewById(R.id.deliveryOpenLayout);
        careOpenLayout= (LinearLayout)view.findViewById(R.id.careOpenLayout);
        careImageBtn = (ImageButton)view.findViewById(R.id.careImageBtn);
        deliveryImageBtn = (ImageButton)view.findViewById(R.id.deliveryImageBtn);
        addToCartBottomView = (RelativeLayout) view.findViewById(R.id.addToCartBelowView);



        layoutAdd = (RelativeLayout)view.findViewById(R.id.rl3);

        shownHereLabel = (TextView)view.findViewById(R.id.mTV);
        matchItemLabel = (TextView)view.findViewById(R.id.matchItemWithTV);
        motherDaughterLabel = (TextView)view.findViewById(R.id.motherAndDaughterTV);

        categoryLabel = (TextView)view.findViewById(R.id.productInfoCategoryTV);
        onlineExLabel = (TextView)view.findViewById(R.id.productInfoOnlineExTV);

        saleButton = (Button)view.findViewById(R.id.saleBar);
        saleButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        productInfoTitleTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        productInfoPriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        productInfoDiscountPriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        productInfoDiscountPriceTV.setShadowLayer(0,0,0,0);
        productInfoRefNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        soldOutInfo.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        sizeTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        sizeSelectTv.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        noSizeTv.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        quantityTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        detailsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        measurementsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        careTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        DeliveryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        btn_add_prodinfo_text.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        styleITwithTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        colorTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        shownHereLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        matchItemLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        motherDaughterLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        inStoreTextView.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        SpannableString content = new SpannableString("Check in-store availability");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        inStoreTextView.setText(content);

        shownHereTag = (TagView)view.findViewById(R.id.shown_here_tag_view);
        matchItemTag = (TagView)view.findViewById(R.id.match_item_tag_view) ;
        motherDaughterTag = (TagView)view.findViewById(R.id.motherDaughter_tag_view);

        categoryLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        onlineExLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        saleButton.setVisibility(View.INVISIBLE);

        chipCloud = (ChipCloud) view.findViewById(R.id.select_size_tv_view);

        backButton = (ImageButton)view.findViewById(R.id.backButtonCustom);

        shoppingBagBtn_prodInfo = (ImageButton)view.findViewById(R.id.shoppingBagBtn_productInfo);
        bagNoti_prod = (TextView)view.findViewById(R.id.bagNoti_prodInfo) ;

        changeToolBarBagNotiText(bagNotiItem);


//        sv.setVerticalScrollBarEnabled(false);

        shoppingBagBtn_prodInfo.setOnClickListener(new View.OnClickListener() {

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

        backButton.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {


                                             getActivity().onBackPressed();

                                           }

                                       }

        );


        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(goToCart)
                {

//                    Fragment fragment = new ShoppingBagFragme        android:id="@+id/ "nt();
////                    FragmentManager fragmentManager = getFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//////                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
////                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
////                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
////                    fragmentTransaction.addToBackStack(null);
////                    fragmentTransaction.commit();

                    Fragment fragment = new ShoppingBagFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("forWishlist", "0");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
                    fragmentTransaction.commit();
//
//

                    ((MainActivity) getActivity()).getSupportActionBar().show();
                    ((MainActivity) getActivity()).showBottomBar(true);

//                    Fragment fragment = new NewCartAndWishlistFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("forWishlist", "0");
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "NewCartAndWishlistFragment");
//                fragmentTransaction.commit();

                }

                else
                {
                    if(SelectedAttID == null || SelectedAttID.equals(""))
                    {

                        if(listSize.size() == 0)
                        {
                            addToCart("","1",prodID);
                        }

                        else {

                            AddtoCartFragment test = new AddtoCartFragment();

                            test.show(((MainActivity) getActivity()).getSupportFragmentManager(), "example");

                            Bundle bundle = new Bundle();
                            bundle.putString("prodID", prodID);
                            bundle.putString("catName", SelectedShopID);
                            bundle.putString("forWishlist", "0");
                            bundle.putStringArrayList("sizes", (ArrayList<String>) listSize);
                            bundle.putStringArrayList("listQuanity", (ArrayList<String>) listQuant);
                            bundle.putStringArrayList("attID", (ArrayList<String>) attributeIDList);

                            System.out.println("data 1 = " + listSize);
                            System.out.println("data 2 = " + listQuant);
                            System.out.println("data 3 = " + attributeIDList);

                            test.setArguments(bundle);
                        }

                    }

                    else {

                        addToCart(SelectedAttID,"1",prodID);

                    }
                }


            }

        });

        //code by Helmi
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(UserID.length() > 0)
                {
                    AddtoCartFragment test = new AddtoCartFragment();
                    test.show(((MainActivity) getActivity()).getSupportFragmentManager(),"example");

                    Bundle bundle = new Bundle();
                    bundle.putString("prodID", prodID);
                    bundle.putString("catName", SelectedShopID);
                    bundle.putString("forWishlist", "1");
                    bundle.putStringArrayList("sizes", (ArrayList<String>) listSize);
                    bundle.putStringArrayList("listQuanity", (ArrayList<String>) listQuant);
                    bundle.putStringArrayList("attID", (ArrayList<String>) attributeIDList);

                    test.setArguments(bundle);

                }
                else
                {
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("To use Save function, please login to your account")
                            .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("LoginFromSavedItem", true);
                                    editor.putString("LoginFromSavedItemProductID", prodID);
                                    editor.putString("LoginFromSavedItemCatName", catName);
                                    editor.apply();

                                    Fragment fragment = new LoginFragment();

                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
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
        btn_save_touch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(UserID.length() > 0)
                {
                    if(!sizeSelect && !quantitySelect)
                    {
                        new android.app.AlertDialog.Builder(getActivity())
                                .setTitle("Message")
                                .setMessage("Please select size before saving product")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                    }

                }
                else
                {
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("To use Save function, please login to your account")
                            .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("LoginFromSavedItem", true);
                                    editor.putString("LoginFromSavedItemProductID", prodID);
                                    editor.putString("LoginFromSavedItemCatName", catName);
                                    editor.apply();

                                    Fragment fragment = new LoginFragment();

                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
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

        btn_share_touch_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri imageUri = imgList.get(0);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "send"));

            }

        });

        btn_share_prodinfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Uri imageUri = imgList.get(0);
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/jpeg");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, "send"));
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, productInfoTitleTV.getText().toString());
                i.putExtra(Intent.EXTRA_TEXT, "http://poplook.com/en/home/"+prodID+"-"+link_rewrite);
                startActivity(Intent.createChooser(i, "Share Product"));

            }

        });

        inStoreTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Fragment fragment = new InStoreAvailabilityFragment();

                String tet = imgList.get(0).toString();

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("sizes", (ArrayList<String>) sizeforInstore);
                bundle.putStringArrayList("attID", (ArrayList<String>) attributeIDList);
                bundle.putString("Product_ID", reference_presta);
                bundle.putString("Product_Name", prodname);
                bundle.putString("Product_Price", price_with_tax);
                bundle.putString("image_url", tet);


                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "InStoreAvailabilityFragment");
//                fragmentTransaction.add(R.id.fragmentContainer,new InStoreAvailabilityFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        final float density = getResources().getDisplayMetrics().density;
//        imageIndicator.setBackgroundColor(Color.parseColor("#888888"));
        imageIndicator.setRadius(8 * density - 10);
        imageIndicator.setPageColor(Color.parseColor("#C2C2C2"));
        imageIndicator.setFillColor(Color.parseColor("#000000"));
//        imageIndicator.setStrokeColor(Color.parseColor("#000000"));
        imageIndicator.setStrokeWidth((float) (0.3 * density));
        imageIndicator.setCentered(true);


        // Create an ArrayAdapter using the string array and a default spinner layout



//        Resources res = getResources();
//        int fontSize = 30;

        detailWV = (WebView) view.findViewById(R.id.webview_details);
//        detailWV.getSettings().setLoadWithOverviewMode(true);
//        detailWV.getSettings().setUseWideViewPort(true);
//        detailWV.getSettings().setLoadsImagesAutomatically(true);
//        detailWV.getSettings().setAppCacheEnabled(false);
//        detailWV.getSettings().setBlockNetworkImage(true);
//        detailWV.getSettings().setLoadsImagesAutomatically(true);
//        detailWV.getSettings().setGeolocationEnabled(false);
//        detailWV.getSettings().setNeedInitialFocus(false);
//        detailWV.getSettings().setSaveFormData(false);
////        detailWV.getSettings().setDefaultFontSize(fontSize);
////        detailWV.getSettings().setTextZoom(measurementsWV.getSettings().getTextZoom() + 150);
//        detailWV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        detailWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        detailWV.getSettings().setJavaScriptEnabled(true);
        detailWV.getSettings().setSupportZoom(false);
        detailWV.getSettings().setBuiltInZoomControls(true);
        detailWV.getSettings().setDisplayZoomControls(false);
        detailWV.getSettings().setLoadWithOverviewMode(true);
        detailWV.getSettings().setUseWideViewPort(false);
        measurementsWV = (WebView) view.findViewById(R.id.webview_measurements);
//        measurementsWV.getSettings().setLoadWithOverviewMode(true);
//        measurementsWV.getSettings().setUseWideViewPort(true);
//        measurementsWV.getSettings().setLoadsImagesAutomatically(true);
//        measurementsWV.getSettings().setAppCacheEnabled(false);
//        measurementsWV.getSettings().setBlockNetworkImage(true);
//        measurementsWV.getSettings().setLoadsImagesAutomatically(true);
//        measurementsWV.getSettings().setGeolocationEnabled(false);
//        measurementsWV.getSettings().setNeedInitialFocus(false);
//        measurementsWV.getSettings().setSaveFormData(false);
////        measurementsWV.getSettings().setDefaultFontSize(fontSize);
////        measurementsWV.getSettings().setTextZoom(measurementsWV.getSettings().getTextZoom() + 150);
//        measurementsWV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        measurementsWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        measurementsWV.getSettings().setJavaScriptEnabled(true);
        measurementsWV.getSettings().setSupportZoom(false);
        measurementsWV.getSettings().setBuiltInZoomControls(true);
        measurementsWV.getSettings().setDisplayZoomControls(false);
        measurementsWV.getSettings().setLoadWithOverviewMode(true);
        measurementsWV.getSettings().setUseWideViewPort(false);
        careWV = (WebView) view.findViewById(R.id.webview_care);
//        careWV.getSettings().setLoadWithOverviewMode(true);
//        careWV.getSettings().setUseWideViewPort(true);
//        careWV.getSettings().setLoadsImagesAutomatically(true);
//        careWV.getSettings().setAppCacheEnabled(false);
//        careWV.getSettings().setBlockNetworkImage(true);
//        careWV.getSettings().setLoadsImagesAutomatically(false);
//        careWV.getSettings().setGeolocationEnabled(false);
//        careWV.getSettings().setNeedInitialFocus(true);
//        careWV.getSettings().setSaveFormData(false);
////        careWV.getSettings().setDefaultFontSize(fontSize);
////        careWV.getSettings().setTextZoom(measurementsWV.getSettings().getTextZoom() + 260);
//        careWV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        careWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        careWV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        careWV.getSettings().setJavaScriptEnabled(true);
        careWV.getSettings().setSupportZoom(false);
        careWV.getSettings().setBuiltInZoomControls(true);
        careWV.getSettings().setDisplayZoomControls(false);
        careWV.getSettings().setLoadWithOverviewMode(true);
        careWV.getSettings().setUseWideViewPort(false);
        webview_Delivery  = (WebView) view.findViewById(R.id.webview_Delivery);
//        webview_Delivery.getSettings().setLoadWithOverviewMode(true);
//        webview_Delivery.getSettings().setUseWideViewPort(true);
//        webview_Delivery.getSettings().setLoadsImagesAutomatically(true);
//        webview_Delivery.getSettings().setAppCacheEnabled(false);
//        webview_Delivery.getSettings().setBlockNetworkImage(true);
//        webview_Delivery.getSettings().setLoadsImagesAutomatically(false);
//        webview_Delivery.getSettings().setGeolocationEnabled(false);
//        webview_Delivery.getSettings().setNeedInitialFocus(true);
//        webview_Delivery.getSettings().setSaveFormData(false);
////        webview_Delivery.getSettings().setDefaultFontSize(fontSize);
////        webview_Delivery.getSettings().setTextZoom(measurementsWV.getSettings().getTextZoom() + 260);
//        webview_Delivery.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webview_Delivery.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview_Delivery.getSettings().setJavaScriptEnabled(true);
        webview_Delivery.getSettings().setSupportZoom(false);
        webview_Delivery.getSettings().setBuiltInZoomControls(true);
        webview_Delivery.getSettings().setDisplayZoomControls(false);
        webview_Delivery.getSettings().setLoadWithOverviewMode(true);
        webview_Delivery.getSettings().setUseWideViewPort(false);
        progressBar1 = (ProgressBar) view.findViewById(R.id.loading_webview_details);
        progressBar2 = (ProgressBar) view.findViewById(R.id.loading_webview_measurements);
        progressBar3 = (ProgressBar) view.findViewById(R.id.loading_webview_care);
        progressBar4 = (ProgressBar) view.findViewById(R.id.loading_webview_delivery);


        //         size = spinner.getSelectedItem().toString();
        //         quantity = spinner2.getSelectedItem().toString();
        //
        //        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
        //                // your code here
        //
        //                size = parentView.getItemAtPosition(position).toString();
        //                System.out.println("HELOooaoaodsoadooaso SIZEEEEE"+size);
        //                if (size.equals("Pick A Size"))
        //                { ((TextView) parentView.getChildAt(0)).setTextColor(Color.LTGRAY);
        //                    addBtn.setVisibility(View.INVISIBLE);
        //                }
        //                else if(!quantity.equals("Pick A Quantity")&&!size.equals("Pick A Size"))
        //                {
        //                    addBtn.setVisibility(View.VISIBLE);
        //                }
        //            }
        //
        //            @Override
        //            public void onNothingSelected(AdapterView<?> parentView) {
        //                // your code here
        //
        //
        //            }
        //
        //        });
        //
        //        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(AdapterView<?> parentView1, View view1, int position1, long id1) {
        //                // your code here
        //
        //                quantity = parentView1.getItemAtPosition(position1).toString();
        //                if (quantity.equals("Pick A Quantity")) {
        //                    ((TextView) parentView1.getChildAt(0)).setTextColor(Color.LTGRAY);
        //                    addBtn.setVisibility(View.INVISIBLE);
        //                } else if (!quantity.equals("Pick A Quantity") && !size.equals("Pick A Size")) {
        //                    addBtn.setVisibility(View.VISIBLE);
        //                }
        //                System.out.println("HELOooaoaodsoadooaso SIZEEEEE" + quantity);
        //            }
        //
        //            @Override
        //            public void onNothingSelected(AdapterView<?> parentView) {
        //                // your code here
        //
        //            }
        //
        //        });





        //
        //        detailWV.setWebChromeClient(new WebChromeClient() {
        //            public void onProgressChanged(WebView view, int progress) {
        //
        //                if (progress < 100) {
        //                    progressBar1.setVisibility(View.VISIBLE);
        //                    detailWV.setVisibility(View.GONE);
        //                }
        //                if (progress == 100) {
        //                    progressBar1.setVisibility(View.GONE);
        //                    detailWV.setVisibility(View.VISIBLE);
        //                }
        //            }
        //
        //        });
        //
        //        measurementsWV.setWebChromeClient(new WebChromeClient() {
        //            public void onProgressChanged(WebView view, int progress) {
        //
        //                if (progress < 100) {
        //                    progressBar2.setVisibility(View.VISIBLE);
        //                    measurementsWV.setVisibility(View.GONE);
        //                }
        //                if (progress == 100) {
        //                    progressBar2.setVisibility(View.GONE);
        //                    measurementsWV.setVisibility(View.VISIBLE);
        //                }
        //            }
        //        });
        //
        //
        //        careWV.setWebChromeClient(new WebChromeClient() {
        //            public void onProgressChanged(WebView view, int progress) {
        //
        //                if (progress < 100) {
        //                    progressBar3.setVisibility(View.VISIBLE);
        //                    careWV.setVisibility(View.GONE);
        //                }
        //                if (progress == 100) {
        //                    progressBar3.setVisibility(View.GONE);
        //                    careWV.setVisibility(View.VISIBLE);
        //                }
        //            }
        //        });

        careOpenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!careClicked) {
                    careLayout.setVisibility(View.VISIBLE);
                    careLayout.startAnimation(animShow);
                    careImageBtn.setImageResource(R.drawable.btn_dropdown);
                    careClicked=true;
                }else{
                    animHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            careLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                    });
                    careLayout.startAnimation(animHide);
                    careImageBtn.setImageResource(R.drawable.icon_go);
                    careClicked=false;
                }

            }

        });

        deliveryOpenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!deliveryClicked) {
                    deliveryLayout.setVisibility(View.VISIBLE);
                    deliveryImageBtn.setImageResource(R.drawable.btn_dropdown);
                    deliveryLayout.startAnimation(animShow);
                    deliveryClicked=true;
                }else{
                    animHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            deliveryLayout.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                        @Override
                        public void onAnimationStart(Animation animation) {}
                    });
                    deliveryLayout.startAnimation(animHide);
                    deliveryImageBtn.setImageResource(R.drawable.icon_go);
                    deliveryClicked=false;
                }



            }

        });

        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setHasFixedSize(true);
        //        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        //shown Item Recycler View

        shownItemLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

//        shownItemRecycler.setHasFixedSize(true);
//        //        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//        // Set CustomAdapter as the adapter for RecyclerView.
//        shownItemRecycler.setLayoutManager(shownItemLayoutManager);
//        shownItemRecycler.scrollToPosition(scrollPosition);
//        shownItemRecycler.setItemAnimator(new DefaultItemAnimator());
//        shownItemRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));


        mColoursLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        ColoursRecyclerView.setHasFixedSize(true);
        //        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // Set CustomAdapter as the adapter for RecyclerView.
        ColoursRecyclerView.setLayoutManager(mColoursLayoutManager);
        ColoursRecyclerView.scrollToPosition(scrollPosition);
        ColoursRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ColoursRecyclerView.getContext(),
//                mColoursLayoutManager.getLayoutDirection());
//        ColoursRecyclerView.addItemDecoration(dividerItemDecoration);
        ColoursRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

//        btn_scrollDown = (ImageButton)view.findViewById(R.id.btn_scrollDown);

//        btn_scrollDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                scroolToTop();
//
//
//            }
//
//        });
        getProductDetails();
        setRetainInstance(true);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("LoginFromSavedItem", false);
        editor.apply();
        return view;
    }

    //    @Override
    //    public void onDetach() {
    //        super.onDetach();
    //        try {
    //            Field childFragmentManager = Fragment.class
    //                    .getDeclaredField("mChildFragmentManager");
    //            childFragmentManager.setAccessible(true);
    //            childFragmentManager.set(this, null);
    //        } catch (NoSuchFieldException e) {
    //            throw new RuntimeException(e);
    //        } catch (IllegalAccessException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }



    public void addToCart(String getSize, String getQuantity, String product_ID){

        quantity = getQuantity;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        CartID = pref.getString("CartID", "0");

        if(cartError)
        {
            CartID = "0";
        }

        System.out.println("sininini cart" + cartError);


        String action="Carts/add";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_customer",UserID)
                .add("id_cart", CartID)
                .add("id_lang", LanguageID)
                .add("id_product", product_ID)
                .add("id_product_attribute", getSize)
                .add("quantity", getQuantity)
                .add("id_shop",SelectedShopID)
                .build();

        WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);

    }

    private void getProductDetails(){

        byte[] inputBytes;
        try {
            inputBytes = prodID.getBytes("iso-8859-1");
            String newStr = new String(inputBytes, StandardCharsets.UTF_8);

            prodID = newStr.replaceAll("[^0-9.]", "");

            System.out.println("Byte Prod ID = " + inputBytes);
            System.out.println("Converted Product ID = " + prodID);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Products/details/id/"+prodID+"/shop/"+SelectedShopID+"/lang/1/full/1?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }


    public void saveForLater(String data, String data2, String data3){

        quantity = data2;

        selectedSize = data;

        System.out.println("get wishlist product id " + data3);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Wishlists/addproduct";
        if(WishlistID.equals("")){
            RequestBody formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_customer",UserID)
                    .add("id_product", data3)
                    .add("id_product_attribute", data)
                    .build();
            WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
            callws.setAction(action);
            callws.execute(formBody);
        }else {
            RequestBody formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_wishlist", WishlistID)
                    .add("id_customer", UserID)
                    .add("id_product", data3)
                    .add("id_product_attribute", data)
                    .build();
            WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
            callws.setAction(action);
            callws.execute(formBody);
        }
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){


            try {
                if (result.getBoolean("status"))
                {

                    //                    list.clear();
                    if (result.getString("action").equals("Products_details"))
                    {listSize.clear();
                    sizeforInstore.clear();
                    listSize.add("Pick A Size");
                        mColoursItems.clear();
                        mItems.clear();
                        imgList.clear();
                        shownHereWithItemsArr.clear();
                        matchItemWithArr.clear();
                        motherAndDaughterArr.clear();

                        JSONObject data = result.getJSONObject("data");

                        id_product_attribute = data.getString("id_product_attribute");

                        String reference = data.getString("reference");
                        reference_presta = reference;
                        String desc = data.getString("description");
                        JSONObject careObj = data.getJSONObject("care");
                        link_rewrite = data.getString("link_rewrite");
                        JSONObject deliveryObj = data.getJSONObject("delivery_returns");
                        String care= careObj.getString("content");
                        String delivery= deliveryObj.getString("content");
                        String measurements = data.getString("measurements");
                        String videoURL = data.getString("video_url");
                        String videoThumb = data.getString("video_url_thumb");
                        String tax_rate = data.getString("tax_rate");
                        String currency_sign = data.getString("currency_sign");
                        price_with_tax = data.getString("price_tax_inc");
                        String price_without_reduction= data.getString("price_without_reduction");
                        String price_reduction = data.getString("price_reduction");
                        String product_availability = data.getString("available_stock");
                        String online_exclusive = data.getString("online_exclusive");
                        String get_collection = data.getString("collection_name");
                        String get_discount = data.getString("discount_label");
                        String get_discount_text = data.getString("discount_text");
                        if (get_discount.equals("1"))
                        {

                            saleButton.setVisibility(View.VISIBLE);
                            saleButton.setText(get_discount_text);

                        }

                        else
                        {

                            saleButton.setVisibility(View.GONE);


                        }

                        out_of_stock = data.getString("product_out_of_stock");
                        String active = data.getString("active");
                        System.out.println("habis stock "+out_of_stock);
                        System.out.println("hahaaaaaaac "+result);
                        JSONArray jsonArr = null;
                        jsonArr = data.getJSONArray("image_url");
                        System.out.println("hahaaaaaaad " + jsonArr);
                        System.out.println("hahaaaaaaad1 " + delivery);
                        System.out.println("Measurements" + care);


                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, prodID);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, prodID);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                        if(get_collection == "null"){

                            categoryLabel.setVisibility(View.INVISIBLE);

                        }

                        else {


                            collection_name_true = data.getString("collection_name");

                            categoryLabel.setText(collection_name_true);
                            categoryLabel.setVisibility(View.VISIBLE);


                        }

                        String imageURLinsider = "";

                        for(int j = 0; j < jsonArr.length(); j++)
                        {

                            imgList.add(Uri.parse(jsonArr.getString(j)));
                            //
                            //                            mItems.add(new ProductListItem(id_product, name, reference, image_url, tax_rate, price_with_tax, price_with_discount, out_of_stock));

                            if(jsonArr.getString(0).length() != 0) {

                                imageURLinsider = jsonArr.getString(0);
                            }

                        }

                        double productPrice = Double.parseDouble(price_with_tax);

//                        String[] taxonomy = {catName};
//                        InsiderProduct product = Insider.Instance.createNewProduct(prodID,prodname,taxonomy,imageURLinsider,productPrice,currency_sign);
//                        Insider.Instance.visitProductDetailPage(product);

                        prodname = data.getString("name");

                        InsiderEvent lastVisitEvent = Insider.Instance.tagEvent("last_product_viewed");
                        lastVisitEvent.build();

                        Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_visit_product",prodname);
                        Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_visit_product_id",prodID);

                        try {
//                            listSize.add("Pick A Size");
                            JSONArray jsonArr1 = null;
                            jsonArr1 = data.getJSONArray("attribute_list");
                            for (int i = 0; i < jsonArr1.length(); i++) {
                                if (jsonArr1.getJSONObject(i).getString("id_attribute") == null) {
                                } else {

                                    System.out.println("MASUK SINI GILA");
                                    String id_attribute = jsonArr1.getJSONObject(i).getString("id_product_attribute");
                                    String group_name = jsonArr1.getJSONObject(i).getString("group_name");
                                    String attribute_name = jsonArr1.getJSONObject(i).getString("attribute_name");
                                    int quantity = jsonArr1.getJSONObject(i).getInt("quantity");
                                    String hide_size = jsonArr1.getJSONObject(i).getString("hide_size");

                                    System.out.println("get size ID = " + id_attribute);

                                    sizeforInstore.add(attribute_name);
                                    attributeIDList.add(id_attribute);
                                    listQuantSize.add(new sizequantityitem(attribute_name, String.valueOf(quantity)));
                                    listQuant.add(String.valueOf(quantity));
                                    if(hide_size.equals("1")) {
                                        listSize.add(attribute_name + " - Sold Out");

                                    }else{
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

                                itemSizeAvailableArray = new String[listSize.size() - 1];

                                for (int x = 1; x <listSize.size(); x++)
                                {

                                    String sizes;

                                    sizes = listSize.get(x);

                                    itemSizeAvailableArray[x-1] = sizes;

                                }

                                chipCloud.setMode(ChipCloud.Mode.SINGLE);

                                new ChipCloud.Configure()
                                        .chipCloud(chipCloud)
                                        .selectedColor(Color.parseColor("#000000"))
                                        .selectedFontColor(Color.parseColor("#ffffff"))
                                        .deselectedColor(Color.parseColor("#e1e1e1"))
                                        .deselectedFontColor(Color.parseColor("#333333"))
                                        .labels(itemSizeAvailableArray)
                                        .gravity(ChipCloud.Gravity.LEFT)
                                        .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                                        .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                                        .chipListener(new ChipListener() {
                                            @Override
                                            public void chipSelected(int index) {

                                                String quantity = listQuant.get(index);

                                                int convertedVal = Integer.parseInt(quantity);

                                                System.out.println("getSelectedIndex " + convertedVal);

                                                SelectedAttID = attributeIDList.get(index);

                                                System.out.println("getSelectedIndexSize " + SelectedAttID);

                                                goToCart = false;
                                                btn_add_prodinfo_text.setText("ADD TO CART");
                                                addBtn.setBackgroundColor(Color.parseColor("#1CAE49"));
//
//                                                quantityValue = convertedVal;
//
//                                                minteger = 1;
//                                                selectAmount.setText("" + minteger);

                                            }
                                            @Override
                                            public void chipDeselected(int index) {

                                                SelectedAttID = "";

                                                goToCart = false;
                                                btn_add_prodinfo_text.setText("ADD TO CART");
                                                addBtn.setBackgroundColor(Color.parseColor("#1CAE49"));

                                            }
                                        })
                                        .build();

                            }
                        } catch(Exception e){
                                listSize.clear();
//                                listSize.add("-");
                                spinner.setEnabled(false);
                                ly1.setVisibility(View.GONE);

                                sizeDividerIV.setVisibility(View.GONE);

                            }
                            if(attributeIDList.size()==0){
                                listSize.clear();
//                                listSize.add("-");
                                spinner.setEnabled(false);
                                ly1.setVisibility(View.GONE);
                                sizeDividerIV.setVisibility(View.GONE);
                                listQuant.add(data.getString("quantity"));
                            }

                            if(listSize.size() == 0)
                            {
                                noSizeTv.setVisibility(View.VISIBLE);
                            }

                        try{
                            JSONArray jsonArr2 = null;
                            jsonArr2 = data.getJSONArray("style_it_with");
                            for(int i = 0; i < jsonArr2.length(); i++)
                            {


                                String styleWithID =  jsonArr2.getJSONObject(i).getString("id_product");
                                String styleWithName = jsonArr2.getJSONObject(i).getString("name");
                                String styleWithPrice = jsonArr2.getJSONObject(i).getString("price");
                                String styleWithDiscount = jsonArr2.getJSONObject(i).getString("price_reduction");
                                String styleWithImage = jsonArr2.getJSONObject(i).getString("image_url");
                                mItems.add(new StyleWithItem(styleWithID, styleWithName, styleWithPrice, styleWithDiscount, styleWithImage));

                                System.out.println("namaaaaaa "+styleWithName);
                            }

                            if(mItems.size() == 0)
                            {
                                ProductInfoStyleWtithItLL.setVisibility(View.GONE);
                            }


                        }
                        catch (Exception e){
                            ProductInfoStyleWtithItLL.setVisibility(View.GONE);
                        }
                        try{
                            JSONArray jsonArrColor = null;
                            jsonArrColor = data.getJSONArray("color_related");
                            for(int i = 0; i < jsonArrColor.length(); i++)
                            {


                                String id_product =  jsonArrColor.getJSONObject(i).getString("id_product");
                                String image_color_url = jsonArrColor.getJSONObject(i).getString("image_color_url");

                                System.out.println("COLORIDPROD "+id_product);
                                mColoursItems.add(new coloursItem(id_product, image_color_url));

                            }

                            if(mColoursItems.size() == 0)
                            {
                                ColourLL.setVisibility(View.GONE);
                                System.out.println("COLORIDPRODX ");

                            }


                        }
                        catch (Exception e){
                            ColourLL.setVisibility(View.GONE);
                            System.out.println("COLORIDPRODXLOL "+e);

                        }

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
//                                shownItems.add(new StyleWithItem(shownHereWithID,shownHereName,price,"",imageLink));

                            }

                            shownAdapter = new StyleWithAdapter(getActivity(),shownItems);

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

                                    Fragment fragment = new ProductInfoFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("prodID", shownHereWithItemsArr.get(position).shownWithID());
                                    bundle.putString("catName", catName);
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    shownHereWithItemsArr.clear();
                                    matchItemWithArr.clear();
                                    motherAndDaughterArr.clear();

                                }
                            });

                        }
                        catch (Exception e) {
                        }

                        if(shownHereWithItemsArr.size() == 0)
                        {

                            shownHereLabel.setVisibility(View.VISIBLE);

                        }

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

                                    Fragment fragment = new ProductInfoFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("prodID", matchItemWithArr.get(position).matchItemWithID());
                                    bundle.putString("catName", catName);
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    shownHereWithItemsArr.clear();
                                    matchItemWithArr.clear();
                                    motherAndDaughterArr.clear();

                                }
                            });

                        }
                        catch (Exception e) {
                        }

                        if(matchItemWithArr.size() == 0)
                        {

                            matchItemLabel.setVisibility(View.GONE);

                        }

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

                                    Fragment fragment = new ProductInfoFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("prodID", motherAndDaughterArr.get(position).motherAndDaughterID());
                                    bundle.putString("catName", catName);
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    shownHereWithItemsArr.clear();
                                    matchItemWithArr.clear();
                                    motherAndDaughterArr.clear();

                                }
                            });


                        }
                        catch (Exception e) {
                        }

                        if(motherAndDaughterArr.size() == 0)
                        {

                            motherDaughterLabel.setVisibility(View.GONE);

                        }

                        if(out_of_stock.equals("0")){
                            soldOutInfo.setVisibility(View.INVISIBLE);

                        }else{
                            soldOutInfo.setVisibility(View.VISIBLE);
//                            addBtn.setVisibility(View.VISIBLE);
//                            addBtn.setEnabled(false);
//                            spinner.setEnabled(false);
//                            spinner2.setEnabled(false);
//                            ly1.setVisibility(View.GONE);
//                            sizeDividerIV.setVisibility(View.GONE);
                            layoutAdd.setVisibility(View.GONE);

                        }



//                        Spanned htmlAsSpanned = Html.fromHtml(desc);
//                        detailWV.setText(htmlAsSpanned);

//                        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
//                                Html.fromHtml(desc));
//                        Spannable processedText = removeUnderlines(spannedText);
//                        if (detailWV != null) {
//                            detailWV.setText(processedText);
//                        }
                        detailWV.loadDataWithBaseURL("", "<style>img{display: inline; height: auto; max-width: 100%;}</style>"+ desc , "text/html", "UTF-8", "");

                        detailWV.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                detailWV.setVisibility(View.VISIBLE);
                                progressBar1.setVisibility(View.GONE);
                            }
                        });

                        measurementsWV.loadDataWithBaseURL("","<style>img{display: inline; height: auto; max-width: 100%;}</style>"+ measurements, "text/html", "UTF-8", "");

                        measurementsWV.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                measurementsWV.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                            }
                        });

//                        Spanned htmlAsSpanned2 = Html.fromHtml(care);
//                        careWV.setText(htmlAsSpanned2);
                        careWV.loadDataWithBaseURL("","<style>img{display: inline; height: auto; max-width: 100%;-webkit-tap-highlight-color: rgba(0,0,0,0);}</style>"+ care, "text/html", "UTF-8", "");

                        careWV.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                careWV.setVisibility(View.VISIBLE);
                                progressBar3.setVisibility(View.GONE);
                            }
                        });
//                        Spanned htmlAsSpanned3 = Html.fromHtml(delivery);
//                        webview_Delivery.setText(htmlAsSpanned3);

                        webview_Delivery.loadDataWithBaseURL("", "<style>img{display: inline; height: auto; max-width: 100%;}</style>"+ delivery , "text/html", "UTF-8", "");


                        webview_Delivery.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                webview_Delivery.setVisibility(View.VISIBLE);
                                progressBar4.setVisibility(View.GONE);
                            }
                        });

                        productInfoTitleTV.setText(prodname);


                        if(product_availability.equals("0"))
                        {

                            onlineExLabel.setVisibility(View.GONE);

                        }

                        else
                        {
                            if(online_exclusive.equals("0"))
                            {

                                onlineExLabel.setVisibility(View.GONE);

                            }

                            else {

                                onlineExLabel.setVisibility(View.VISIBLE);

                            }


                        }

                        if(price_reduction.equals("0.00")){
                            if(currency_sign.equalsIgnoreCase("RM")) {
//                                productInfoPriceTV.setText("RM " + price_with_tax);
                                productInfoPriceTV.setText("RM " + price_with_tax + " ");

                            }else{
                                productInfoPriceTV.setText(currency_sign +" "+ price_with_tax);
                            }
                            productInfoPriceTV.setVisibility(View.VISIBLE);
                            productInfoDiscountPriceTV.setVisibility(View.GONE);
                        }
                        else{
                            final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
                            final ForegroundColorSpan fcs = new ForegroundColorSpan(getActivity().getResources().getColor(R.color.red));
                            String s="";
                            if(currency_sign.equalsIgnoreCase("RM")) {
                                 s = "RM " + price_without_reduction + " RM " + price_with_tax + " ";
                                productInfoDiscountPriceTV.setText(s, TextView.BufferType.SPANNABLE);
                                Spannable spannable = (Spannable)productInfoDiscountPriceTV.getText();
                                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, price_with_tax.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(fcs, price_without_reduction.length()+3, price_without_reduction.length()+3+price_with_tax.length()+4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                productInfoDiscountPriceTV.setVisibility(View.VISIBLE);
                                productInfoPriceTV.setVisibility(View.GONE);
                            }else{
                                 s = currency_sign +" "+ price_without_reduction + " " +currency_sign +" "+ price_with_tax;
                                productInfoDiscountPriceTV.setText(s, TextView.BufferType.SPANNABLE);
                                Spannable spannable = (Spannable)productInfoDiscountPriceTV.getText();
                                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, price_with_tax.length()+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(fcs, price_without_reduction.length()+4, price_without_reduction.length()+4+price_with_tax.length()+5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                productInfoDiscountPriceTV.setVisibility(View.VISIBLE);
                                productInfoPriceTV.setVisibility(View.GONE);
                            }
                        }



                        if(reference.length() == 0 || reference.equals(null)|| reference.isEmpty() || reference.equalsIgnoreCase("") || reference.equalsIgnoreCase("null"))
                        {

                            productInfoRefNoTV.setText("Reference Number: -");
                        }else{
                            productInfoRefNoTV.setText("Reference Number: " + reference);
                        }


                        System.out.println("HELOVIDEO!!!" + videoURL);

                        if(videoURL.length()>4)
                        {
                            System.out.println("video not null");
                            int last = imgList.size();
                            imgList.add(last, Uri.parse("android.resource://"+getActivity().getPackageName()+"/drawable/default_video"));
                            IsVideoAvailable = true;
                        }
                        else
                        {
                            System.out.println("video null");
                        }

                        System.out.println("hahaaaaaaa" + imgList.size());
                        imageListProduct = new Uri[imgList.size()];
                        Arrays.fill(imageListProduct, null);

                        imageListProduct = imgList.toArray(imageListProduct);

                        adapter = new ViewPagerAdapter(getActivity(), imageListProduct, IsVideoAvailable);
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        String apikey =pref.getString("apikey","");
                        productViewVP.setTag("http://poplook.com/webapi/VideoEmbed/player?video="+videoURL+"&apikey="+apikey);
                        productViewVP.setAdapter(adapter);
                        productViewVP.setOffscreenPageLimit(20);


                        imageIndicator.setViewPager(productViewVP);

                        mAdapter = new StyleWithAdapter(getActivity(),mItems);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        Fragment fragment = new ProductInfoFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("prodID", mItems.get(position).getstyleWithID());
                                        bundle.putString("catName", catName);
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        mItems.clear();
                                        mColoursItems.clear();
                                    }

                                })
                        );
                        mColoursAdapter = new coloursAdapter(getActivity(),mColoursItems);
                        ColoursRecyclerView.setAdapter(mColoursAdapter);
                        ColoursRecyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        System.out.println("chamngeproductidhere " + mColoursItems.get(position).getColorProdID());

                                        Fragment fragment = new ProductInfoFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("prodID", mColoursItems.get(position).getColorProdID());
                                        bundle.putString("catName", catName);
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        mItems.clear();
                                        mColoursItems.clear();


                                    }

                                })
                        );
                        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_layout, listSize);
                        // Specify the layout to use when the list of choices appears

                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
                        spinner.setAdapter(adapter1);
                        spinner.setSelection(0);
                        size = spinner.getSelectedItem().toString();

//                        spinner.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View v, MotionEvent event) {
//                                if(listSize.get(0).equals("Pick A Size")) {
//                                    listSize.remove(0);
//                                    adapter1.notifyDataSetChanged();
//                                }
//                                return false;
//                            }
//                        });

                        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                                // your code here
                                if (parentView.getChildAt(0) != null) {
                                    ((TextView) parentView.getChildAt(0)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                                }

                                if (position > 0) {
                                    SelectedAttributeID = attributeIDList.get(position-1);
                                    quant = listQuant.get(position-1);
                                    if (quant.equalsIgnoreCase("1")) {
                                        list.clear();
                                        list.add("1");
                                    } else if (quant.equalsIgnoreCase("2")) {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                    } else if (quant.equalsIgnoreCase("3")) {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                        list.add("3");
                                    } else if (quant.equalsIgnoreCase("4")) {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                        list.add("3");
                                        list.add("4");
                                    }else {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                        list.add("3");
                                        list.add("4");
                                        list.add("5");
                                    }

                                    System.out.println("quantity drop total" + quant);
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                            R.layout.spinner_layout, list);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner2.setAdapter(dataAdapter);

                                    quantity = "";


                                    quantity = spinner2.getSelectedItem().toString();

                                    spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parentView1, View view1, int position1, long id1) {
                                            // your code here
                                            if (parentView1.getChildAt(0) != null) {
                                                ((TextView) parentView1.getChildAt(0)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                                            }

                                            quantity = parentView1.getItemAtPosition(position1).toString();
                                            if (!quantity.equals("Pick A Quantity")) {
                                                if (parentView1.getChildAt(0) != null) {
                                                    ((TextView) parentView1.getChildAt(0)).setTextColor(Color.BLACK);
                                                }
                                            }

                                            if (out_of_stock.equals("0")) {
                                                if (!quantity.equals("Pick A Quantity") && !size.equals("Pick A Size")) {
                                                    quantitySelect = true;
                                                    if (!size.contains("Sold Out")) {
                                                        addBtn.setVisibility(View.VISIBLE);
                                                        if(isAddedTocart){
                                                        }
                                                        else {
                                                        }
                                                        spinner2.setEnabled(true);
                                                    } else {
                                                        addBtn.setVisibility(View.VISIBLE);
                                                        spinner2.setEnabled(false);
                                                    }
                                                } else {
                                                    addBtn.setVisibility(View.VISIBLE);
                                                    if(isAddedTocart){
                                                    }
                                                    else {
                                                    }
                                                    quantitySelect = false;
                                                }

                                            } else {
                                                addBtn.setVisibility(View.VISIBLE);
                                            }
                                            System.out.println("HELOooaoaodsoadooaso SIZEEEEE" + quantity);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parentView) {
                                            // your code here


                                        }

                                    });
                                }
                                else{
//                                    list.clear();
//                                    quantity = "";
//
//                                    list.add("1");
//                                    list.add("2");
//                                    list.add("3");
//                                    list.add("4");
//                                    list.add("5");
                                    SelectedAttributeID=attributeIDList.get(0);

                                    quant=listQuant.get(0);
                                    if (quant.equalsIgnoreCase("1")) {
                                        list.clear();
                                        list.add("1");
                                    } else if (quant.equalsIgnoreCase("2")) {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                    } else if (quant.equalsIgnoreCase("3")) {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                        list.add("3");
                                    } else if (quant.equalsIgnoreCase("4")) {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                        list.add("3");
                                        list.add("4");
                                    }else {
                                        list.clear();
                                        list.add("1");
                                        list.add("2");
                                        list.add("3");
                                        list.add("4");
                                        list.add("5");
                                    }
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                            R.layout.spinner_layout, list);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner2.setAdapter(dataAdapter);

                                    quantity = spinner2.getSelectedItem().toString();

                                    spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parentView1, View view1, int position1, long id1) {
                                            // your code here
                                            if (parentView1.getChildAt(0) != null) {
                                                ((TextView) parentView1.getChildAt(0)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                                            }

                                            quantity = parentView1.getItemAtPosition(position1).toString();
                                            if (!quantity.equals("Pick A Quantity")) {
                                                if (parentView1.getChildAt(0) != null) {
                                                    ((TextView) parentView1.getChildAt(0)).setTextColor(Color.BLACK);
                                                }
                                            }

                                            if (out_of_stock.equals("0")) {
                                                if (!quantity.equals("Pick A Quantity") && !size.equals("Pick A Size")) {
                                                    quantitySelect = true;
                                                    if (!size.contains("Sold Out")) {
                                                        addBtn.setVisibility(View.VISIBLE);
                                                        if(isAddedTocart){
                                                        }
                                                        else {
                                                        }
                                                        spinner2.setEnabled(true);
                                                    } else {
                                                        addBtn.setVisibility(View.VISIBLE);
                                                        spinner2.setEnabled(false);
                                                    }
                                                } else {
                                                    addBtn.setVisibility(View.VISIBLE);
                                                    if(isAddedTocart){
                                                    }
                                                    else {
                                                    }
                                                    quantitySelect = false;
                                                }

                                            } else {
                                                addBtn.setVisibility(View.VISIBLE);
                                            }
                                            System.out.println("HELOooaoaodsoadooaso SIZEEEEE" + quantity);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parentView) {
                                            // your code here

                                        }

                                    });
                                }

                                size = parentView.getItemAtPosition(position).toString();
                                System.out.println("HELOooaoaodsoadooaso SIZEEEEE" + size);
                                if (!size.equals("Pick A Size")) {
                                    if (parentView.getChildAt(0) != null) {
                                        ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                                    }
                                }
                                if (out_of_stock.equals("0")) {
                                    if (!quantity.equals("Pick A Quantity") && !size.equals("Pick A Size")) {
                                        sizeSelect = true;
                                        if (!size.contains("Sold Out")) {
                                            addBtn.setVisibility(View.VISIBLE);
                                            if(isAddedTocart){
                                            }
                                            else {
                                            }
                                            spinner2.setEnabled(true);
                                        } else {
                                            addBtn.setVisibility(View.VISIBLE);
                                            spinner2.setEnabled(false);

                                        }
                                    } else {
                                        addBtn.setVisibility(View.VISIBLE);
                                        if(isAddedTocart){
                                        }
                                        else {
                                        }
                                        sizeSelect = false;

                                    }
                                } else {
                                    addBtn.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here


                            }

                        });

                        list.clear();
                        quantity = "";

                        list.add("1");
                        list.add("2");
                        list.add("3");
                        list.add("4");
                        list.add("5");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_layout, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(dataAdapter);

                        quantity = spinner2.getSelectedItem().toString();

                        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView1, View view1, int position1, long id1) {
                                // your code here
                                if (parentView1.getChildAt(0) != null) {
                                    ((TextView) parentView1.getChildAt(0)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                                }

                                quantity = parentView1.getItemAtPosition(position1).toString();
                                if (!quantity.equals("Pick A Quantity")) {
                                    if (parentView1.getChildAt(0) != null) {
                                        ((TextView) parentView1.getChildAt(0)).setTextColor(Color.BLACK);
                                    }
                                }

                                if (out_of_stock.equals("0")) {
                                    if (!quantity.equals("Pick A Quantity") && !size.equals("Pick A Size")) {
                                        quantitySelect = true;
                                        if (!size.contains("Sold Out")) {
                                            addBtn.setVisibility(View.VISIBLE);
                                            if(isAddedTocart){
                                            }
                                            else {
                                            }
                                            spinner2.setEnabled(true);
                                        } else {
                                            addBtn.setVisibility(View.VISIBLE);
                                            spinner2.setEnabled(false);
                                        }
                                    } else {
                                        addBtn.setVisibility(View.VISIBLE);
                                        if(isAddedTocart){
                                        }
                                        else {
                                        }
                                        quantitySelect = false;
                                    }

                                } else {
                                    addBtn.setVisibility(View.VISIBLE);
                                }
                                System.out.println("HELOooaoaodsoadooaso SIZEEEEE" + quantity);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here

                            }

                        });


                    }

                    else if (result.getString("action").equals("Carts_add"))
                    {
                        String message = result.getString("message");
                        String cartID = result.getJSONObject("data").getString("id_cart");

                        System.out.println("hahaaaaaaa MASUK sini" + result.getString("action"));

                        System.out.println("message sini "+ message);

                        if ( message.equals("Item added")){

                            Toast toast = Toast.makeText(getActivity(),
                                    "Item Added", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                            goToCart = true;
                            btn_add_prodinfo_text.setText("Go to bag");
                            addBtn.setBackgroundColor(Color.parseColor("#199453"));

//                            bagNotiItem++;
//                            System.out.println("ITEM" + bagNotiItem);

//                            Insider.Instance.tagEvent(getActivity(),"item_added_to_cart");
//                            Insider.Instance.tagEvent("item_added_to_cart").build();

                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

                            String totalBag = pref.getString("cartItem", "0");

                            String currency="MYR";
                            if(SelectedShopID.equalsIgnoreCase("1")){
                                currency="MYR";
                            }else if(SelectedShopID.equalsIgnoreCase("2")){
                                currency="SGD";
                            }else if(SelectedShopID.equalsIgnoreCase("3")){
                                currency="USD";
                            }

                            logAddToCartEvent(catName,prodID,prodname,currency,Double.parseDouble(price_with_tax));


                            double productPrice = Double.parseDouble(price_with_tax);

                            String imageURL = imgList.get(0).toString();

                            String[] taxonomy = {catName};
                            InsiderProduct product = Insider.Instance.createNewProduct(prodID,prodname,taxonomy,imageURL,productPrice,currency);
                            Insider.Instance.itemAddedToCart(product);

//                            Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_added_to_cart",prodID);



                            int totalquant=Integer.parseInt(quantity);

                            Insider.Instance.getCurrentUser().setCustomAttributeWithInt("basket_item_quantity",totalquant);



                            totalquant+=Integer.parseInt(totalBag);

                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("cartItem", String.valueOf(totalquant));
                            editor.putString("CartID", cartID);

                            editor.apply();
//                            ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(totalquant));
                            changeToolBarBagNotiText(String.valueOf(totalquant));


                        }
                        else if ( message.equals("Item updated"))
                        {

                            Toast toast = Toast.makeText(getActivity(),
                                    "Item Updated", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                            String totalBag = pref.getString("cartItem", "0");

                            int totalquant=Integer.parseInt(quantity);

                            totalquant+=Integer.parseInt(totalBag);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("cartItem", String.valueOf(totalquant));
                            editor.putString("CartID", cartID);
                            editor.apply();
//                            ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(totalquant));
                            changeToolBarBagNotiText(String.valueOf(totalquant));

                        }
//                        Insider.Instance.tagEvent(getActivity(),"item_added_to_cart");
//                        Insider.Instance.tagEvent("item_added_to_cart").build();
                        String currency="MYR";
                        if(SelectedShopID.equalsIgnoreCase("1")){
                            currency="MYR";
                        }else if(SelectedShopID.equalsIgnoreCase("2")){
                            currency="SGD";
                        }else if(SelectedShopID.equalsIgnoreCase("3")){
                            currency="USD";
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.QUANTITY, quantity);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, catName);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, prodname);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, prodID);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, SelectedShopID);
                        bundle.putDouble(FirebaseAnalytics.Param.PRICE, Double.parseDouble(price_with_tax));
                        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);

                    }
                    else if (result.getString("action").equals("Wishlists_addproduct"))
                    {

                        String message = result.getString("message");
                        if ( message.equals("Successfully Added")){

                            Toast toast = Toast.makeText(getActivity(),
                                    "Item Saved", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
//                            Insider.Instance.tagEvent(getActivity(),"add_to_favorite");
                            Insider.Instance.tagEvent("add_to_favorite").build();
//                            Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_added_to_fav",prodID);

                            InsiderEvent event = Insider.Instance.tagEvent("add_to_fav");
                            event.addParameterWithString("category_name",catName);
                            event.addParameterWithString("product_name",prodname);
                            event.addParameterWithString("product_id",prodID);
                            event.addParameterWithString("product_size",selectedSize);

                            event.build();


                            String currency="MYR";
                            if(SelectedShopID.equalsIgnoreCase("1")){
                                currency="MYR";
                            }else if(SelectedShopID.equalsIgnoreCase("2")){
                                currency="SGD";
                            }else if(SelectedShopID.equalsIgnoreCase("3")){
                                currency="USD";
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.QUANTITY, "1");
                            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, catName);
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, prodname);
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, prodID);
                            bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, SelectedShopID);
                            bundle.putDouble(FirebaseAnalytics.Param.PRICE, Double.parseDouble(price_with_tax));
                            bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle);

                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                            String totalList = pref.getString("wishlistItem", "0");

                            int totalquant=Integer.parseInt(quantity);

                            totalquant+=Integer.parseInt(totalList);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("wishlistItem", String.valueOf(totalquant));
                            editor.apply();
                            ((MainActivity) getActivity()).changeToolBarWishNotiText(String.valueOf(totalquant));

                        }
                        else{
                            Toast toast = Toast.makeText(getActivity(),
                                    "This item cannot be saved. Please try again", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
                        }

                    }

                }
                else {
                    if (result.getString("action").equals("Carts_add")) {
                        String message = result.getString("message");

                        System.out.println("sininini "+message);

                        if(message.contains("Cart cannot be loaded"))
                        {
                            cartError = true;

                            addToCart(SelectedAttID,"1",prodID);

                        }

                        else {

                            new android.app.AlertDialog.Builder(getActivity())
                                    .setTitle("Message")
                                    .setMessage(message)
                                    .setPositiveButton("OK", null)
                                    .show();
                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("CartID", "");
                            editor.apply();
                        }

                    } else if (result.getString("action").equals("Wishlists_addproduct")) {
                        Toast toast = Toast.makeText(getActivity(),
                                result.getString("message"), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();


                    }
                }



            }

            catch (Exception e){
                System.out.println("eeeee "+e);
            }

        }
        else{

            new AlertDialog.Builder(getActivity())
                    .setTitle("Message")
                    .setMessage("Please connect to the Internet")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();

        }
    }

//    private void toggleViewPagerScrolling() {
//        if (isViewPagerActive()) {
//            ((HackyViewPager) productViewVP).toggleLock();
//        }
//    }
//
//    private void toggleLockBtnTitle() {
//        boolean isLocked = false;
//        if (isViewPagerActive()) {
//            isLocked = ((HackyViewPager) productViewVP).isLocked();
//        }
//    }

    private boolean isViewPagerActive() {
        return (productViewVP != null && productViewVP instanceof HackyViewPager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) productViewVP).isLocked());
        }
        super.onSaveInstanceState(outState);
    }



    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    @Override
    public void onResume() {
        super.onResume();

        goToCart = false;
        btn_add_prodinfo_text.setText("ADD TO CART");
        addBtn.setBackgroundColor(Color.parseColor("#1CAE49"));

        ((MainActivity) getActivity()).getSupportActionBar().hide();
        ((MainActivity) getActivity()).showBottomBar(false);

    }

    public void changeToolBarBagNotiText(String txt){
        numberInBag=txt;
        if (numberInBag.equals("-1")||numberInBag.equals("-2")||numberInBag.equals("0")||numberInBag.isEmpty()||numberInBag.equalsIgnoreCase("") || numberInBag.equals(null)||numberInBag.equals("null"))
        {
            bagNoti_prod.setVisibility(View.INVISIBLE);
            bagNoti_prod.setText("");
        } else {

            bagNoti_prod.setVisibility(View.VISIBLE);
            bagNoti_prod.setText(numberInBag);
        }
    }


    public void logAddToCartEvent (String contentData, String contentId, String contentType, String currency, double price) {
        Bundle params = new Bundle();

        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, price, params);

    }

}
