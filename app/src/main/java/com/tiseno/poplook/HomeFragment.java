package com.tiseno.poplook;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.HomeAdapter;
import com.tiseno.poplook.functions.HomeItem;
import com.tiseno.poplook.functions.RecyclerItemClickListener;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;
import com.tiseno.poplook.webservice.WebServiceAccessPost;
import com.tiseno.poplook.webservice.WebServiceAccessPut;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class HomeFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> , HomeAdapter.ViewHolder.AdapterCallback {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private DatePickerDialog.OnDateSetListener mDateListener;


    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }
    ImageLoader imageLoader= ImageLoader.getInstance();
    protected LayoutManagerType mCurrentLayoutCollectionManagerType;
    protected LayoutManagerType mCurrentLayoutCategoryManagerType;

    ImageView popupLevelFirstImg, popupLevelSecongImg, popupLevelThirdImg;
    protected RecyclerView mCollectionRecyclerView;
    protected RecyclerView mCategoryRecyclerView;
    protected ImageView primaryBannerIV;
    protected ProgressBar homeprogress,homeprogresspromo;
    protected RecyclerView.Adapter mAdapterCollection;
    protected RecyclerView.Adapter mAdapterCategory;
    protected RecyclerView.LayoutManager mLayoutManagerCollection;
    protected RecyclerView.LayoutManager mLayoutManagerCategory;

    ArrayList<HomeItem> mImagesCollection= new ArrayList<HomeItem>();
    ArrayList<HomeItem> mImagesCategory= new ArrayList<HomeItem>();
    protected int scrollPosition = 0;
    protected int scrollPositionHori = 0;

    static  FrameLayout saleBannerFrame;

    TextView firstLabel,scdLabel, termsLabel, fourthLabel, birthDateLabel, tvWithEnddate, tvBelow;

    TextView termsTV, supportTV;

    Button monthBtn, agreeBtn, submitBtn;

    String birthDay;

    String agreementCheck;

    Boolean selected;

    String fromLoyalty, LanguageID;

    ProgressBar centerBar;

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Initialize dataset, this data would usually come from a local content provider or
//        // remote server.
////        initDataset();
//    }
    String SelectedShopID="1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.setTag(TAG);

        ((MainActivity) getActivity()).changeToolBarTextView(false);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(true);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).showBottomBar(true);

        mCategoryRecyclerView = (RecyclerView) rootView.findViewById(R.id.HomeCategoryRecyclerView);
        centerBar = (ProgressBar)rootView.findViewById(R.id.progressBarCenterHome);


        final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String bagNotiItem = pref.getString("cartItem", "0");
        String wishlistItem = pref.getString("wishlistItem", "0");
        String getAboutPolicy = pref.getString("aboutPolicy", "0");
        String comeFromNotification = pref.getString("", "0");



        if(getAboutPolicy.equals("0")) {

            AlertDialog.Builder privacyAlert = new AlertDialog.Builder(getActivity());
            privacyAlert.setTitle("Privacy Policy");
            privacyAlert.setMessage("Poplook collects your personal information such as phone number, email address, credit/debit card or other payment options for purchasing our products. This information required as part of the purchasing process. Other information such as address details may be given to our courier for delivery services. These details allow us to process your order and to let you know the status of your order. For more information you can read our Privacy Policy by clicking Read or go to My Account > Privacy Policy. By choosing Agree, you are accepting and consenting to the practices described in Privacy Policy. ");
            privacyAlert.setCancelable(false);
            privacyAlert.setPositiveButton("Read",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //Do nothing here because we override this button later to change the close behaviour.
                            //However, we still need this because on older versions of Android unless we
                            //pass a handler the button doesn't get instantiated
                        }
                    });
            privacyAlert.setNegativeButton("Agree", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            final AlertDialog dialog = privacyAlert.create();
            dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://poplook.com/en/content/6-privacy-policy"));
                    startActivity(browserIntent);
                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("aboutPolicy", "1");
                    editor.apply();

                    dialog.dismiss();

                }
            });

        }

        fromLoyalty = pref.getString("fromLoyaltyPage","");

        if(fromLoyalty.equals("1"))
        {

            Fragment fragment = new LoyaltyDashboardFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "LoyaltyDashboard");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("fromLoyaltyPage", "0");
            editor.apply();

        }

        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        LanguageID = pref.getString("LanguageID", "");
        //
        ((MainActivity) getActivity()).changeToolBarBagNotiText(bagNotiItem);
        ((MainActivity) getActivity()).changeToolBarWishNotiText(wishlistItem);


        String NeedToGoBackToCart = pref.getString("NeedToGoBackToCart","");
        boolean LoginFromSavedItem = pref.getBoolean("LoginFromSavedItem",false);
        String LoginFromSavedItemProductID = pref.getString("LoginFromSavedItemProductID","");
        String LoginFromSavedItemCatName = pref.getString("LoginFromSavedItemCatName","");

        System.out.println("NeedToGoBackToCart is "+NeedToGoBackToCart);


        if(NeedToGoBackToCart.equals("1"))
        {
            Fragment fragment = new ShoppingBagFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(LoginFromSavedItem){
            Fragment fragment = new ProductInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("prodID", LoginFromSavedItemProductID);
            bundle.putString("catName", LoginFromSavedItemCatName);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

//        mLayoutManagerCollection = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
//        mCollectionRecyclerView.setHasFixedSize(true);
//        mCurrentLayoutCollectionManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
//        mCollectionRecyclerView.setLayoutManager(mLayoutManagerCollection);
//        mRecyclerView.scrollToPosition(scrollPosition);
//        mCollectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));


        mLayoutManagerCategory = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
//        mCategoryRecyclerView.setHasFixedSize(true);
        mCurrentLayoutCategoryManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mCategoryRecyclerView.setLayoutManager(mLayoutManagerCategory);
//        mRecyclerView.scrollToPosition(scrollPosition);
//        mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));


//        System.out.println("DUAKALIIIIII");
//        getHomeImageListNew();

        Insider.Instance.visitHomePage();

        getHomeBannerList();

//        deliveryBanner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    Fragment fragment = new CustomerServiceFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("fromHome","Home");
//                    fragment.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                    mImagesCollection.clear();
//                    mImagesCategory.clear();
//
//                }
//
//
//        });

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());


        View viewPopUp = inflater.inflate(R.layout.birthday_popup, null);
        mBuilder.setView(viewPopUp);

        firstLabel = (TextView) viewPopUp.findViewById(R.id.firstLabelBDay);
        firstLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        scdLabel = (TextView) viewPopUp.findViewById(R.id.scdLabelBDay);
        scdLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        termsLabel = (TextView) viewPopUp.findViewById(R.id.thirdLabelBDay);
        termsLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        fourthLabel = (TextView) viewPopUp.findViewById(R.id.fourLabelBDay);
        fourthLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        birthDateLabel = (TextView) viewPopUp.findViewById(R.id.birthDate);
        birthDateLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        submitBtn = (Button) viewPopUp.findViewById(R.id.submit);
        submitBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        agreeBtn = (Button) viewPopUp.findViewById(R.id.agreentBtn);
        selected = true;

        agreementCheck = "0";


        AlertDialog.Builder levelUpBuilder = new AlertDialog.Builder(getActivity());

        View levelView = inflater.inflate(R.layout.level_up_layout, null);

        popupLevelFirstImg = (ImageView)levelView.findViewById(R.id.level_image);
        popupLevelSecongImg = (ImageView)levelView.findViewById(R.id.level_text);
        popupLevelThirdImg = (ImageView)levelView.findViewById(R.id.level_click_text);

        tvWithEnddate = (TextView)levelView.findViewById(R.id.textWithEnddate);
        tvBelow = (TextView)levelView.findViewById(R.id.textBottom);

        tvWithEnddate.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        tvBelow.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));


        levelUpBuilder.setView(levelView);


        String getLoyalty = pref.getString("loyalty_id","");
        String getTier = pref.getString("popup_show","");

        if(getLoyalty.equals("1"))
        {

            final AlertDialog dialog = mBuilder.create();

            dialog.show();
//        dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);


            birthDateLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Calendar cal = Calendar.getInstance();

                    int year = cal.get(Calendar.YEAR);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int month = cal.get(Calendar.MONTH);

//                                                 cal.set(2006, 11, 31);//Year,Mounth -1,Day

                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth, mDateListener, year, day, month);

//                                                 dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);

                }
            });


            mDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    month = month + 1;

                    String date = month + "/" + dayOfMonth + "/" + year;
                    birthDay =  year + "-" + month + "-" + dayOfMonth;
                    birthDateLabel.setText(date);

                }
            };

            agreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (selected)
                    {

                        agreeBtn.setBackgroundResource(R.drawable.button_selector);
                        selected=false;

                        agreementCheck = "1";

                    }

                    else
                    {
                        agreeBtn.setBackgroundResource(R.drawable.button_border);
                        selected=true;
                        agreementCheck = "0";

                    }

                }
            });

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(birthDateLabel.getText().length() == 0)

                    {
                        birthDateLabel.setHintTextColor(Color.RED);
                    }

                    else if(agreementCheck.equals("0"))

                    {

                        Toast toast = Toast.makeText(getActivity(),
                                "Please agree to Terms & Condition before proceed", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 50);
                        toast.show();

                    }


                    else {
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("loyalty_id", "");
                        editor.apply();

                        updateCustomerBirthday(birthDay);

                        Fragment fragment = new LoyaltyDashboardFragment();

                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        dialog.dismiss();
                    }
                }
            });

        }
        else if (getTier.equals("true"))
        {

            final AlertDialog levelUpDialog = levelUpBuilder.create();

            String getPopupLevel = pref.getString("popup_tier","");
            String getPopupLevel_ID = pref.getString("popup_id","");

            String getEnddate = pref.getString("levelup_enddate", "");

            tvWithEnddate.setText("*Term : Purchase should be done before " + getEnddate);

            popupLevelThirdImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    dismissLevelUpPopUp(pref.getString("popup_id",""), pref.getString("popup_tier",""));
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("popup_show", "");
                    editor.apply();
                    levelUpDialog.dismiss();
                }

            });


            if(getPopupLevel.equals("2"))
            {

                popupLevelFirstImg.setImageResource(R.drawable.level_up_silver);
                popupLevelSecongImg.setImageResource(R.drawable.level_up_silver_text);
                popupLevelThirdImg.setImageResource(R.drawable.level_up_click_silver);

            }

            else if (getPopupLevel.equals("3"))
            {

                popupLevelFirstImg.setImageResource(R.drawable.level_up_gold);
                popupLevelSecongImg.setImageResource(R.drawable.level_up_gold_text);
                popupLevelThirdImg.setImageResource(R.drawable.level_up_click_gold);



            }

            else
            {


            }


            levelUpDialog.show();
            levelUpDialog.setCanceledOnTouchOutside(false);


        }


        else {


        }


        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
//        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutCollectionManagerType);
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutCategoryManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void getHomeImageListNew(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="banners/mobile_home/shop/"+SelectedShopID+"?apikey="+apikey;
        WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
        callws.execute(action);

        System.out.println("api home banner url "+action);


    }

    private void getHomeBannerVideoList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="banners/video/1?apikey="+apikey;
        WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
        callws.execute(action);

        System.out.println("api home banner url "+action);


    }

    private void getHomeBannerList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="banners/mobile/shop/"+SelectedShopID+"/lang/1?apikey="+apikey;
        WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
        callws.execute(action);

        System.out.println("api home banner url "+action);


    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("Get JSON Response : " + result);


        if(result!=null){
            try {

                if (result.getBoolean("status"))
                {
                    if (result.getString("action").equals("banners_mobile"))
                    {   mImagesCollection.clear();
                        mImagesCategory.clear();

                        JSONObject jsonObject = result.getJSONObject("data");
                        JSONArray jsonArrPrimary = null;
//                        jsonArrPrimary = jsonObject.getJSONArray("New home destop banners");

                        jsonArrPrimary = jsonObject.names();


                        for (int i = 0; i < jsonArrPrimary.length (); ++i) {

                            String key = jsonArrPrimary.getString(i); // Here's your key
                            JSONArray jsonArr = null;

                            jsonArr = jsonObject.getJSONArray(key);


                            for (int y = 0; y < jsonArr.length (); ++y) {

                                    String catIDPrimary = jsonArr.getJSONObject(y).getString("category_id");
                                    String catNamePrimary = jsonArr.getJSONObject(y).getString("category_name");
                                    String linkPrimary = jsonArr.getJSONObject(y).getString("link");
                                    String hrefPrimary = jsonArr.getJSONObject(y).getString("href");

                                mImagesCategory.add(new HomeItem(catIDPrimary, catNamePrimary, linkPrimary, hrefPrimary,""));


                            }
                        }

                        getHomeBannerVideoList();

                    }

                    if(result.getString("action").equals("banners_video"))
                    {

                        Object videoDataa = result.get("data");

                        if(videoDataa instanceof JSONArray)
                        {

                            JSONArray jsonObject = result.getJSONArray("data");
                            JSONObject videoData = jsonObject.getJSONObject(0);

                            String catIDPrimary = videoData.getString("category_id");
                            String catNamePrimary = videoData.getString("category_name");
                            String linkPrimary = videoData.getString("link");
                            String hrefPrimary = videoData.getString("href");
                            String position = videoData.getString("position");

                            int x = Integer.parseInt(position)-1;
                            mImagesCategory.add(x,new HomeItem(catIDPrimary, catNamePrimary, "isVideo", hrefPrimary, position));


                        }

                    }


                    mAdapterCategory = new HomeAdapter(mImagesCategory,getActivity(),false,this);

                    System.out.println("get banners amount " + mImagesCategory.size());

                    mCategoryRecyclerView.setAdapter(mAdapterCategory);

                }

                centerBar.setVisibility(View.INVISIBLE);

            }
            catch (Exception e){

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

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void updateCustomerBirthday(String birtdate)
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String UserID = pref.getString("UserID", "");
//        String loyalty = pref.getString("loyalty_id", "1");
        String apikey =pref.getString("apikey","");
        String action="Customers/birthday";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_user",UserID)
                .add("birthday",birtdate)
                .add("loyalty", "2")
                .build();

        System.out.println("User ID == " + UserID);
        System.out.println("Birth Date == " + birtdate);
//        System.out.println("Loyalty == " + loyalty);
        System.out.println("API Key == " + apikey);


        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    void dismissLevelUpPopUp(String popupID, String popupLevel)
    {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String UserID = pref.getString("UserID", "");
//        String loyalty = pref.getString("loyalty_id", "1");
        String apikey =pref.getString("apikey","");
        String action="Customers/loyaltypopup";
        RequestBody formBody = new FormBody.Builder()
                .add("id_shop",SelectedShopID)
                .add("id_lang",LanguageID)
                .add("user_id",UserID)
                .add("popup_tier",popupLevel)
                .add("popup_tier_id",popupID)
                .add("apikey",apikey)
                .build();


        WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);

    }


    @Override
    public void onMethodCallback(String bannerID, String href, String categoryName, String link) {

        System.out.println("banner data 1 = " + bannerID);
        System.out.println("banner data 2 = " + href);
        System.out.println("banner data 3 = " + categoryName);
        System.out.println("banner data 4 = " + link);


        try{

                                         if(link.contains("http")){

//                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mImagesCategory.get(position).getlink()));
//                                        startActivity(browserIntent);
//                                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                                            CustomTabsIntent customTabsIntent = builder.build();
//                                            customTabsIntent.launchUrl(getActivity(), Uri.parse(mImagesCategory.get(position).getlink()));


                                            String linkBrowser = "https://poplook.com"+link;
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkBrowser));
                                            startActivity(browserIntent);


                                        } else{
                                            Fragment fragment = new ListOfProductFragment();

                                            if(bannerID.equals("null") || categoryName.equals("null"))
                                            {

                                            }

                                            else {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("prodID", bannerID);
                                                bundle.putString("catName", categoryName);
                                                bundle.putString("fromHome","Home");
                                                fragment.setArguments(bundle);
                                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                                mImagesCategory.clear();
                                            }

                                        }
                                    }catch (Exception e){
                                        Fragment fragment = new ProductListFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("prodID", bannerID);
                                        bundle.putString("catName", categoryName);
                                        bundle.putString("fromHome","Home");
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        mImagesCategory.clear();
                                    }

    }

}
