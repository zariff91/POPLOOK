package com.tiseno.poplook;

//import android.app.AlertDialog;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.view.GravityCompat;
//import android.support.v7.app.ActionBarActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.transition.Explode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tiseno.poplook.functions.Artist;
import com.tiseno.poplook.functions.BottomMenuAdapter;
import com.tiseno.poplook.functions.ChildSideMenuItem;
import com.tiseno.poplook.functions.Genre;
import com.tiseno.poplook.functions.GenreAdapter;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.GridViewProductAdapter;
import com.tiseno.poplook.functions.HackyDrawerLayout;
import com.tiseno.poplook.functions.RegistrationIntentService;
import com.tiseno.poplook.functions.sideMenuItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderCallback;
import com.useinsider.insider.InsiderCallbackType;
import com.useinsider.insider.MessageCenterData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity implements AsyncTaskCompleteListener<JSONObject>, ChangeEditAddressList.selectAddress, AddtoCartFragment.addToCartMethod, BottomNavCollectionFragment.selectedBottomCategoryMenu, HomeBottomFragment.selectBottomCollectionMenu, BottomNavMyAccFragment.selectBottomMyAcc,BottomMenuAdapter.onClickItem{
    public FirebaseAnalytics mFirebaseAnalytics;
    String UserID, CartID, LanguageID;
    String cartItem, apikey;
    ArrayList<sideMenuItem> parentSideMenuArray = new ArrayList<>();
    ArrayList<sideMenuItem>sideMenuArray= new ArrayList<>();
    ArrayList<sideMenuItem>childSideMenu= new ArrayList<>();

//    ArrayList<ChildSideMenuItem> childMenuArray = new ArrayList<ChildSideMenuItem>();

    JSONObject cartResultJObj;

    String selectedCategory = "";

    // GCM Config -----------------------------------------------------

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "GCM POPLOOK";

//------------------------------------------------------------------


    private Toolbar toolbar, toolbar2;                              // Declaring the Toolbar Object

    RecyclerView sideMenuRV;
    RecyclerView.LayoutManager mLayoutManager;

    BottomMenuAdapter mmAdapter;
// Declaring RecyclerView

    TextView top, bagNoti, wishlistNoti,shippingToText,sideMenuUserNameText,sideMenuText,homeMenuBtn,changeLabelText;
    ImageButton shoppingBagBtn, wishlistBtn, searchBtn, xCloseBtn, homeButton,backBtn;
    ImageView logo,backSideMenuBtn;

    RelativeLayout sideMenuTopText;
    private String numberInBag = "0";
    private String numberInWishlist = "0";
    String tickTock = "", tickTockID = "", SelectedCountryName = "";
    boolean isAppInstalled = false;
    String SelectedShopID = "1";

    AppBarLayout appBar;
    DrawerLayout mDrawer;
    boolean childOpened =  false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //First commit bugs branch
        //Create conflict

        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        Log.v("onCreate Here", "maxMemory:" + Long.toString(maxMemory));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here

            Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.lightgrey));
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
// set an exit transition
            window.setExitTransition(new Explode());
            window.setEnterTransition(new Explode());

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        try {
            if (checkPlayServices()) {

                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);

            } else {
                Log.i("GCM", "No valid Google Play Services APK found.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "0");
        LanguageID = pref.getString("LanguageID", "");
        SelectedCountryName = pref.getString("SelectedCountryName", "");
        cartItem = pref.getString("cartItem", "0");
        isAppInstalled = pref.getBoolean("isAppInstalled", false);
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");
        //
        //apikey
        apikey = pref.getString("apikey", "");
//        if(!isAppInstalled){
//            // add shortcutIcon code here
//            System.out.println("COME SHORTCUT");268435456
//            Intent intent = this.getIntent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            this.overridePendingTransition(0, 0);
//            this.finish();
//
//            this.overridePendingTransition(0, 0);
//            startActivity(intent);
//
//        }
    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mDrawer = findViewById(R.id.sideDrawer);
        sideMenuRV = findViewById(R.id.sideMenuRecyclerView);
        sideMenuTopText = findViewById(R.id.mainMenuTextLayout);
        backSideMenuBtn = findViewById(R.id.back_menu_btn);
        sideMenuText = findViewById(R.id.main_menu_text_label);
        sideMenuUserNameText = findViewById(R.id.userName);
        shippingToText = findViewById(R.id.shipping_change_label);
        homeMenuBtn = findViewById(R.id.homeMenuBtn);
        changeLabelText = findViewById(R.id.change_shippinglabel);

        mLayoutManager = new LinearLayoutManager(this); // Creating a layout Manager
        sideMenuRV.setLayoutManager(mLayoutManager);

        changeLabelText.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        SpannableString content = new SpannableString("Change");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        changeLabelText.setText(content);

        changeLabelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.closeDrawer(GravityCompat.START,true);
                openFragment(new ChangeCountryFragment(), "ChangeCountryFragment");

            }
        });

        shippingToText.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingToText.setTextColor(Color.BLACK);

        shippingToText.setText("Ship to "+SelectedCountryName);

        sideMenuText.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_BLACK_FONT));
        sideMenuText.setTextColor(Color.WHITE);
        sideMenuUserNameText.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_BLACK_FONT));
        sideMenuUserNameText.setTextColor(Color.BLACK);
        homeMenuBtn.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_BLACK_FONT));
        homeMenuBtn.setTextColor(Color.BLACK);

        SpannableString homeText = new SpannableString("Home");
        homeText.setSpan(new UnderlineSpan(), 0, homeText.length(), 0);
        homeMenuBtn.setText(homeText);

        homeMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.closeDrawer(GravityCompat.START,true);
                openFragment(new NewProductListFragment(), "NewProductListFragment");

            }
        });

        if (UserID.length() > 0) {
            sideMenuUserNameText.setText("Hello, " + pref.getString("Name", ""));
        }

        else {
            sideMenuUserNameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDrawer.closeDrawer(GravityCompat.START, false);

                    Fragment fragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.commit();
                }

            });
        }


//        toolbar2= (Toolbar) findViewById(R.id.tool_bar1);
//        toolbar2.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        top = (TextView) toolbar.findViewById(R.id.toolbar_title);
        top.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_BLACK_FONT));
        logo = (ImageView) toolbar.findViewById(R.id.toolbar_title_image);
        backBtn = (ImageButton) toolbar.findViewById(R.id.backBtn);
        shoppingBagBtn = (ImageButton) toolbar.findViewById(R.id.shoppingBagBtn);
        wishlistBtn = (ImageButton) toolbar.findViewById(R.id.wishlistBtn);
        searchBtn = (ImageButton) toolbar.findViewById(R.id.searchBtn);
        xCloseBtn = (ImageButton) toolbar.findViewById(R.id.xCloseBtn);
        bagNoti = (TextView) toolbar.findViewById(R.id.bagNoti);
        wishlistNoti = (TextView) toolbar.findViewById(R.id.wishlistNoti);

        homeButton = (ImageButton) toolbar.findViewById(R.id.btnHome);
        /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        top = (TextView) findViewById(R.id.toolbar_title);
        top.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_BLACK_FONT));


//        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

//        mRecyclerView.setHasFixedSize(true);

        appBar = (AppBarLayout) findViewById(R.id.tabanim_appbar);

        backBtnControl("");
        wishlistBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new SavedItemsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "SavedItemsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });
        shoppingBagBtn.setOnClickListener(new View.OnClickListener() {

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

            }

        });

        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Fragment fragment = new SearchFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "SearchFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                openFragment(new NewProductListFragment(), "NewProductListFragment");
//                hideTopBar(true);
                mDrawer.openDrawer(GravityCompat.START);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    FragmentManager fm = getFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        Log.i("MainActivity", "popping backstack");
                        ProductListFragment productListFragment = (ProductListFragment) fm.findFragmentByTag("ProductListFragment");

                        if (productListFragment != null && productListFragment.isVisible()) {
                            // add your code here
                            FragmentTransaction trans = fm.beginTransaction();
                            trans.remove(productListFragment);
                            trans.commit();

                            fm.popBackStack();
                        } else {
                            fm.popBackStack();

                        }

                        SharedPreferences pref = getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        //editor.putBoolean("atMyVoucher", false);
                        editor.putBoolean("atMyVoucher", true);
                        editor.apply();
                    }

                }

            });

        xCloseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                }

            }

        });

        SharedPreferences.Editor editor = pref.edit();
        //editor.putBoolean("atMyVoucher", false);
        editor.putBoolean("atMyVoucher", true);
        editor.putBoolean("isAppInstalled", true);
        editor.putBoolean("notFirstTime", true);
        editor.apply();

        String NeedToGoBackToCart = pref.getString("NeedToGoBackToCart", "0");
        if (NeedToGoBackToCart.equals("1")) {
            shoppingBagBtn.performClick();

        }

        getSideMenuList();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            String title = intent.getStringExtra("title");
            final String orderId = intent.getStringExtra("Id");

            Log.e("Main Activity",title+" : "+message);

            //AnyIntent or Query

        }
    };

    private void openFragment(final Fragment fragment, final String fragmentTag) {

        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragmentContainer, fragment, fragmentTag);
//                ft.addToBackStack(null);
        ft.commit();


    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
// O
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {
//        if (Drawer.isDrawerOpen(GravityCompat.START)) {
//            Drawer.closeDrawer(GravityCompat.START);
//        } else {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            NewProductListFragment homeFragment = (NewProductListFragment) fm.findFragmentByTag("NewProductListFragment");

            if (homeFragment != null && homeFragment.isVisible()) {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_noti)
                        .setTitle("Closing POPLOOK")
                        .setMessage("Are you sure you want to close this application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                FragmentManager fmHome = getFragmentManager();
                fmHome.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragmentContainer, new NewProductListFragment(), "NewProductListFragment");
//                ft.addToBackStack(null);
                ft.commit();
            }
        }

        getSupportActionBar().show();
//        hideTopBar(true);
     }

//    }

    public void switchContent(int id, Fragment fragment) {

        System.out.println("switch content");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(id, fragment, "ProductInfoFragment");
        ft.addToBackStack(null);
        ft.commit();

    }

    public void switchContent2(int id, Fragment fragment) {

        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(id, fragment, "ProductInfoFragment");
        ft.addToBackStack(null);
        ft.commit();

        System.out.println("aaaaaaa");
    }

    public void getSideMenuList() {

//        String action="Menus/mobile?apikey="+apikey+"&shop="+SelectedShopID+"&lang=1";
        String action = "Menus/mobilecategories/lang/1/shop/"+SelectedShopID+"?apikey=" + apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(this, this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if (result != null) {

            try {

                if (result.getString("action").equals("Menus_mobilecategories")) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    UserID = pref.getString("UserID", "");
                    SelectedCountryName = pref.getString("SelectedCountryName", "");
                    parentSideMenuArray.clear();

                    if (UserID.length() > 0) {
                        sideMenuUserNameText.setText("Hello, " + pref.getString("Name", ""));
                    }

                    parentSideMenuArray.clear();
                    sideMenuArray.clear();

                    JSONObject test = result.getJSONObject("data");
                    JSONArray parentMenuArray = test.getJSONArray("children");

                    for (int i = 0; i < parentMenuArray.length(); i++) {

                        JSONObject obj = parentMenuArray.getJSONObject(i);

                        String parentSideMenuTitle = obj.getString("name");
                        String parentSideMenuCatID = obj.getString("id");

                        Object aObj = obj.get("children");
                        if (aObj instanceof JSONObject) {

                            JSONObject children = obj.getJSONObject("children");

                            if(children.has("Discover")){
                                JSONArray discoverList = children.getJSONArray("Discover");
                                JSONArray shopByList = children.getJSONArray("Shop By");
                                parentSideMenuArray.add(new sideMenuItem(parentSideMenuCatID,parentSideMenuTitle,shopByList,discoverList));
                            }
                            else {
                                JSONArray shopByList = children.getJSONArray("Shop By");
                                parentSideMenuArray.add(new sideMenuItem(parentSideMenuCatID,parentSideMenuTitle,shopByList,null));
                            }

                            // do what you want
                        }

                        else {
                            parentSideMenuArray.add(new sideMenuItem(parentSideMenuCatID,parentSideMenuTitle,null,null));

                        }
                    }

                    parentSideMenuArray.add(new sideMenuItem("1000","Follow Us",null,null));
                    parentSideMenuArray.add(new sideMenuItem("1001","Visit Our Store",null,null));
                    parentSideMenuArray.add(new sideMenuItem("1002","Settings",null,null));
                    parentSideMenuArray.add(new sideMenuItem("1003","POPLOOK Loyalty",null,null));

                    if(UserID.length() != 0){

                        if(SelectedShopID.equals("1")){
                            parentSideMenuArray.add(new sideMenuItem("1004","My Member ID",null,null));
                            parentSideMenuArray.add(new sideMenuItem("1005","My Account",null,null));
                            parentSideMenuArray.add(new sideMenuItem("1006","Log Out",null,null));
                        }
                        else {
                            parentSideMenuArray.add(new sideMenuItem("1005","My Account",null,null));
                            parentSideMenuArray.add(new sideMenuItem("1006","Log Out",null,null));
                        }
                    }
//
                    mmAdapter = new BottomMenuAdapter(parentSideMenuArray,this,this);
                    sideMenuRV.setAdapter(mmAdapter);
//
                }

            } catch (Exception e) {

            }

        } else {
        }
    }

    public void changeToolBarText(String txt) {
        try {
            if (txt != null && !txt.isEmpty()) {
                top.setText(txt);
            } else {
                top.setText("");
            }
        } catch (Exception e) {

        }
    }

    public void changeToolBarTextView(Boolean view) {
        if (view) {
            top.setVisibility(View.VISIBLE);
        } else {
            top.setVisibility(View.GONE);
        }
    }

    public void changeBtnBackView(Boolean view) {
        if (view) {
            backBtn.setVisibility(View.GONE);
            homeButton.setVisibility(View.VISIBLE);
        } else {
            backBtn.setVisibility(View.VISIBLE);
            homeButton.setVisibility(View.GONE);

        }
    }

    public void changeToolBarImageView(Boolean view) {
        if (view) {
            logo.setVisibility(View.VISIBLE);
        } else {
            logo.setVisibility(View.GONE);
        }


    }

    public void changeBtnSearchView(Boolean view) {
        if (view) {
            searchBtn.setVisibility(View.VISIBLE);
        } else {
            searchBtn.setVisibility(View.INVISIBLE);
        }


    }

    public void changeBtnBagView(Boolean view) {
        if (view) {
            shoppingBagBtn.setVisibility(View.VISIBLE);
            bagNoti.setVisibility(View.VISIBLE);
        } else {
            shoppingBagBtn.setVisibility(View.GONE);
            bagNoti.setVisibility(View.GONE);
        }


    }

    public void changeBtnWishlistView(Boolean view) {
        if (view) {
            wishlistBtn.setVisibility(View.VISIBLE);
            wishlistNoti.setVisibility(View.VISIBLE);
        } else {
            wishlistBtn.setVisibility(View.GONE);
            wishlistNoti.setVisibility(View.GONE);
        }


    }

    public void changeBtnCloseXView(Boolean view) {
        if (view) {
            xCloseBtn.setVisibility(View.VISIBLE);
        } else {
            xCloseBtn.setVisibility(View.GONE);
        }


    }
//
//    public void hideSideMenu(Boolean view) {
//        if (view) {
//            homeButton.setVisibility(View.GONE);
//            backBtn.setVisibility(View.VISIBLE);
//            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        } else {
//            homeButton.setVisibility(View.VISIBLE);
//            backBtn.setVisibility(View.GONE);
//            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//
//        }
//
//
//    }

    public void showBottomBar(Boolean view) {
    }

    public void disableExpandToolbar(Boolean view) {
        if (view) {
            appBar.setExpanded(true, true);
        } else {
            appBar.setExpanded(false, true);
        }


    }

    public void changeToolBarBagNotiText(String txt) {
        numberInBag = txt;
        if (numberInBag.equals("-1") || numberInBag.equals("-2") || numberInBag.equals("0") || numberInBag.isEmpty() || numberInBag.equalsIgnoreCase("") || numberInBag.equals(null) || numberInBag.equals("null")) {
            bagNoti.setVisibility(View.INVISIBLE);
            bagNoti.setText("");
        } else {

            bagNoti.setVisibility(View.VISIBLE);
            bagNoti.setText(numberInBag);
        }
    }

    public void changeToolBarWishNotiText(String txt) {
        numberInWishlist = txt;
        if (numberInWishlist.equals("-1") || numberInWishlist.equals("-2") || numberInWishlist.equals("0") || numberInWishlist.isEmpty() || numberInWishlist.equalsIgnoreCase("") || numberInWishlist.equals(null) || numberInWishlist.equals("null")) {
            wishlistNoti.setVisibility(View.GONE);
            wishlistNoti.setText("");
        } else {

            wishlistNoti.setVisibility(View.VISIBLE);
            wishlistNoti.setText(numberInWishlist);
        }
    }

    public void hideTopBar(Boolean view) {
        if (view) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    public void setDrawerState(Boolean isEnabled) {
        if (isEnabled) {
//            Drawer.setDrawerLockMode(HackyDrawerLayout.LOCK_MODE_UNLOCKED);
//            mDrawerToggle.setDrawerIndicatorEnabled(true);
//            mDrawerToggle.syncState();
//            toolbar.setNavigationIcon(R.drawable.btn_menu_pressed);

        } else {
//            Drawer.setDrawerLockMode(HackyDrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            mDrawerToggle.setDrawerIndicatorEnabled(false);
//            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onSupportNavigateUp();
//                }
//            });
//            mDrawerToggle.syncState();
        }
    }


    /**
     * GCM Configurations -------------------------------------------------------------
     */

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * END of GCM Configurations -------------------------------------------------------
     */
    public void backBtnControl(String change) {
        FragmentManager fm1 = getFragmentManager();
        final NewFilterFragment filterFragment = (NewFilterFragment) fm1.findFragmentByTag("NewFilterFragment");

        if (change.equalsIgnoreCase("CHANGE")) {
            System.out.println("LALU SINI ATAU");
//            backBtn.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//                    filterFragment.backBtn();
//
//                }
//
//            });
        } else {
            System.out.println("LALU SINI SANA");

        }
    }

    public void onResume() {
        super.onResume();

        //**************************** PUSH NOTIFICATION CONFIGURATIONS

        SharedPreferences pref = getSharedPreferences("MyPref", 0);

        String comeFromNotification = pref.getString("comeFromNotification", "0");
        String categoryID = pref.getString("categoryID", "");
        String categoryName = pref.getString("categoryName", "");
        String searchKeyword = pref.getString("searchKeyword","");
        String productID = pref.getString("productID","");
        String productName = pref.getString("productName","");
        String lastVisitProductPageID = pref.getString("lastVisitPage_ID","");
        String lastVisitProductPageName = pref.getString("lastVisitedPage","");
        String lastVisitProductDetailID = pref.getString("lastVisitProductID","");
        String lastVisitProductDetailName = pref.getString("lastVisitProductName","");

        if(comeFromNotification.equals("1")) {

            String signup_inapp = "0";

            try {
                cartResultJObj = new JSONObject(pref.getString("signup_page_open", "0"));
                JSONObject data = cartResultJObj.getJSONObject("data");

                signup_inapp = data.getString("test");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String cartNotification = pref.getString("cartPage", "");
            String wishlistNotification = pref.getString("wishlistPage", "");
            String orderHistoryPage = pref.getString("orderHistoryPage", "");
            String cartReminder = pref.getString("goToCart", "");


            if (!categoryID.equals("") && !categoryName.equals("")) {

                Fragment fragment = new ListOfProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("prodID", categoryID);
                bundle.putString("catName", categoryName);
                bundle.putString("fromHome", "Home");
                fragment.setArguments(bundle);

                pref.edit().remove("categoryID").commit();
                pref.edit().remove("categoryName").commit();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragmentContainer, fragment, "ListOfProductFragment");
//                ft.addToBackStack(null);
                ft.commit();

            } else if (!lastVisitProductPageID.equals("") && !lastVisitProductPageName.equals("")) {

                Fragment fragment = new ListOfProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("prodID", lastVisitProductPageID);
                bundle.putString("catName", lastVisitProductPageName);
                bundle.putString("fromHome", "Home");
                fragment.setArguments(bundle);

                pref.edit().remove("lastVisitPage_ID").commit();
                pref.edit().remove("lastVisitedPage").commit();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragmentContainer, fragment, "ListOfProductFragment");
//                ft.addToBackStack(null);
                ft.commit();
            } else if (!searchKeyword.equals("")) {
                Fragment fragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromHome", "Search");
                bundle.putString("search", searchKeyword.toString());
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                SearchFragment searchFragment = (SearchFragment) fragmentManager.findFragmentByTag("SearchFragment");

                if (searchFragment != null && searchFragment.isVisible()) {
                    // add your code here
                    FragmentTransaction trans1 = fragmentManager.beginTransaction();
                    trans1.remove(searchFragment);
                    trans1.commit();
                    fragmentManager.popBackStack();
                }
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fragmentManager.beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragmentContainer, fragment, "SearchFragment");
//                ft.addToBackStack(null);
                ft.commit();

            } else if (!productID.equals("") && !productName.equals("")) {
                Fragment fragment = new ProductInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("prodID", productID);
                bundle.putString("catName", productName);
                fragment.setArguments(bundle);

                pref.edit().remove("productID").commit();
                pref.edit().remove("productName").commit();

                FragmentManager fm = getFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
//                ft.addToBackStack(null);
                ft.commit();
            } else if (!lastVisitProductDetailID.equals("") && !lastVisitProductDetailName.equals("")) {
                Fragment fragment = new ProductInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("prodID", lastVisitProductDetailID);
                bundle.putString("catName", lastVisitProductDetailName);
                fragment.setArguments(bundle);

                pref.edit().remove("lastVisitProductID").commit();
                pref.edit().remove("lastVisitProductName").commit();

                FragmentManager fm = getFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
//                ft.addToBackStack(null);
                ft.commit();
            } else if (cartNotification.equals("1")) {

                pref.edit().remove("cartPage").commit();

                Fragment fragment = new ShoppingBagFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else if (wishlistNotification.equals("1")) {

                pref.edit().remove("wishlistPage").commit();

                Fragment fragment = new SavedItemsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "SavedItemsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else if (orderHistoryPage.equals("1")) {

                pref.edit().remove("orderHistoryPage").commit();

                Fragment fragment = new OrderHistoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else if (cartReminder.equals("1")) {

                pref.edit().remove("goToCart").commit();

                Fragment fragment = new ShoppingBagFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
//                    fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else if (signup_inapp.equals("1")) {

                pref.edit().remove("signup_page_open").commit();

                Insider.Instance.tagEvent("register_form_viewed").addParameterWithBoolean("needRegister", false).build();

                Fragment fragment = new SignUpFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragmentContainer, fragment);

                ft.commit();

            } else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragmentContainer, new NewProductListFragment(), "NewProductListFragment");
//                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    @Override
    public void sendInputSaveItem(String data, String data2, String data3) {


        FragmentManager fm = getFragmentManager();
        ProductInfoFragment productInfo = (ProductInfoFragment) fm.findFragmentByTag("ProductInfoFragment");

        if (productInfo == null) {

            System.out.println("get here");

        } else {

            productInfo.saveForLater(data, data2, data3);


        }

    }

    @Override
    public void sendInput(String data, String data2, String data3) {


        FragmentManager fm = getFragmentManager();
        ProductInfoFragment productInfo = (ProductInfoFragment) fm.findFragmentByTag("ProductInfoFragment");

        productInfo.addToCart(data, data2, data3);

    }

    @Override
    public void sendInput(String data, String data2) {


        selectedCategory = data2;

        Fragment fragment = new ListOfProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("prodID", data2);
        bundle.putString("catName", data);
        bundle.putString("fromHome", "Home");
        fragment.setArguments(bundle);

        openFragment(fragment, "ListOfProductFragment");


    }

    @Override
    public void sendInput2(String data, String data2) {

        Fragment fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("prodID", data2);
        bundle.putString("catName", data);
        bundle.putString("fromHome", "Home");
        fragment.setArguments(bundle);

        openFragment(fragment, "ProductListFragment");

    }

    @Override
    public void sendInput3(String data, String data2) {

        if (data.contains("Shipping To")) {
            openFragment(new ChangeCountryFragment(), "ChangeCountryFragment");
        }
        if (data.equals("Visit Our Store")) {
            openFragment(new VisitOurStoreFragment(), "VisitOurStoreFragment");
        }
        if (data.equals("My Account")) {
            openFragment(new MyAccountFragment(), "MyAccountFragment");
        }
        if (data.equals("Login")) {
            openFragment(new LoginFragment(), "LoginFragment");
        }
        if (data.equals("Poplook Loyalty Rewards")) {
            openFragment(new LoyaltyMainPageFragment(), "LoyaltyMain");
        }

        if (data.equals("Privacy Policy")) {

            Fragment fragment = new PrivacyPolicyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("fromSignUp", "Nope");
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        if (data.equals("Settings")) {
            openFragment(new SettingsFragment(), "SettingsFragment");
        }
        if (data.equals("My Member ID")) {
            openFragment(new MyMemberIDFragment(), "MemberIDFragment");
        }

        if (data.equals("Facebook")) {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            this.startActivity(facebookIntent);

        }

        if (data.equals("Twitter")) {
            Intent intent = null;
            try {
                // get the Twitter app if possible
                this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=50962757"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/poplookshop"));
            }
            this.startActivity(intent);


        }

        if (data.equals("Instagram")) {

            Uri uri = Uri.parse("http://instagram.com/_u/poplook");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                this.startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                this.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/poplook")));
            }
        }

        if (data.equals("Log Out")) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences pref = MainActivity.this.getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("UserID", "");
                            editor.putString("CartID", "");
                            editor.putString("LanguageID", "1");
                            editor.putString("Name", "");
                            editor.putString("LastName", "");
                            editor.putString("Email", "");
                            editor.putString("WishlistID", "");
                            editor.putString("cartItem", "0");
                            editor.putString("wishlistItem", "0");
                            editor.putString("loyalty_id", "");
                            editor.putString("popup_show", "");
                            editor.putString("tier_level", "");
                            editor.putString("entity_id", "");

                            editor.apply();

                            Insider.Instance.tagEvent("logout").build();

                            new Handler().post(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = MainActivity.this.getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    MainActivity.this.overridePendingTransition(0, 0);
                                    MainActivity.this.finish();

                                    MainActivity.this.overridePendingTransition(0, 0);
                                    startActivity(intent);
                                }
                            });

                        }

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

    }

    public static String FACEBOOK_URL = "https://www.facebook.com/POPLOOK";
    public static String FACEBOOK_PAGE_ID = "138171308208";

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://profile/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


    @Override
    public void selectedDeliveryAdd(Integer deliveryAdd, Integer billingAdd) {

        FragmentManager fm = getFragmentManager();
        NewOrderConfirmationFragment addressFragment = (NewOrderConfirmationFragment) fm.findFragmentByTag("NewOrderConfirmationFragment");

        if (addressFragment == null) {

            System.out.println("get here");

        } else {

            addressFragment.updateAddressConfirmationPage(deliveryAdd, billingAdd);

        }

    }

    @Override
    public void selectedBillingAdd(Integer deliveryAdd, Integer billingAdd) {

        FragmentManager fm = getFragmentManager();
        NewAddressBillingFragment addressFragment = (NewAddressBillingFragment) fm.findFragmentByTag("NewAddressBillingFragment");

        if (addressFragment == null) {

            System.out.println("get here");

        } else {

            addressFragment.updateAddressBilling(deliveryAdd, billingAdd);

        }

    }

    @Override
    public void onItemClick(int position) {

        if (childOpened) {

            String catName = sideMenuArray.get(position).gettitle();

            if(catName.equals("Shop By") || catName.equals("Discover")) {


            }

            else {
                String catID = sideMenuArray.get(position).getcategoryID();
                if (catID.equals("fb")) {

                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getFacebookPageURL(this);
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    this.startActivity(facebookIntent);

                } else if (catID.equals("twit")) {

                    Intent intent = null;
                    try {
                        // get the Twitter app if possible
                        this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=50962757"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/poplookshop"));
                    }
                    this.startActivity(intent);

                } else if (catID.equals("insta")) {

                    Uri uri = Uri.parse("http://instagram.com/_u/poplook");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        this.startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        this.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/poplook")));
                    }

                } else {

                    Fragment fragment = new ListOfProductFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("prodID", sideMenuArray.get(position).getcategoryID());
                    bundle.putString("catName", sideMenuArray.get(position).gettitle());
                    bundle.putString("fromHome", "Home");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                mDrawer.closeDrawer(GravityCompat.START, false);

            }

    }
                        else {

                            String cat = parentSideMenuArray.get(position).getcategoryID();

                            if (cat.equals("1000")) {
                                homeMenuBtn.setVisibility(View.GONE);
                                sideMenuText.setText("Follow us on");

                                sideMenuArray.add(new sideMenuItem("fb","Facebook",null,null));
                                sideMenuArray.add(new sideMenuItem("twit","Twitter",null,null));
                                sideMenuArray.add(new sideMenuItem("insta","Instagram",null,null));

                                childOpened = true;
                                mmAdapter = new BottomMenuAdapter(sideMenuArray, this, this);
                                mmAdapter.notifyDataSetChanged();
                                sideMenuRV.setAdapter(mmAdapter);
                                sideMenuTopText.setVisibility(View.VISIBLE);
                                sideMenuTopText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        backPressed();
                                    }
                                });

                            }

                           else if (cat.equals("1001")) {
                                mDrawer.closeDrawer(GravityCompat.START, false);
                                openFragment(new VisitOurStoreFragment(), "VisitOurStoreFragment");
                            }
                            else if (cat.equals("1002")) {
                                mDrawer.closeDrawer(GravityCompat.START, false);
                                openFragment(new SettingsFragment(), "SettingsFragment");
                            }
                            else if (cat.equals("1003")) {
                                mDrawer.closeDrawer(GravityCompat.START, false);
                                openFragment(new LoyaltyMainPageFragment(), "LoyaltyMain");
                            }
                            else if (cat.equals("1004")) {
                                mDrawer.closeDrawer(GravityCompat.START, false);
                                openFragment(new MyMemberIDFragment(), "MemberIDFragment");
                            }

                            else if (cat.equals("1005")) {
                                mDrawer.closeDrawer(GravityCompat.START, false);
                                openFragment(new MyAccountFragment(), "MyAccountFragment");
                            }

                            else if (cat.equals("1006")) {
                                mDrawer.closeDrawer(GravityCompat.START, false);
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Log Out")
                                        .setMessage("Are you sure you want to log out?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                SharedPreferences pref = MainActivity.this.getSharedPreferences("MyPref", 0);
                                                SharedPreferences.Editor editor = pref.edit();

                                                editor.putString("UserID", "");
                                                editor.putString("CartID", "");
                                                editor.putString("LanguageID", "1");
                                                editor.putString("Name", "");
                                                editor.putString("LastName", "");
                                                editor.putString("Email", "");
                                                editor.putString("WishlistID", "");
                                                editor.putString("cartItem", "0");
                                                editor.putString("wishlistItem", "0");
                                                editor.putString("loyalty_id", "");
                                                editor.putString("popup_show", "");
                                                editor.putString("tier_level", "");
                                                editor.putString("entity_id", "");

                                                editor.apply();

                                                Insider.Instance.tagEvent("logout").build();

                                                new Handler().post(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        Intent intent = MainActivity.this.getIntent();
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        MainActivity.this.overridePendingTransition(0, 0);
                                                        MainActivity.this.finish();

                                                        MainActivity.this.overridePendingTransition(0, 0);
                                                        startActivity(intent);
                                                    }
                                                });

                                            }

                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }


                           else if(parentSideMenuArray.get(position).getShopByArray() != null) {
                                homeMenuBtn.setVisibility(View.GONE);
                                sideMenuText.setText(parentSideMenuArray.get(position).gettitle());
                                JSONArray childArr1 = parentSideMenuArray.get(position).getShopByArray();

                                sideMenuArray.add(new sideMenuItem("","Shop By",null,null));

                                for(int i=0;i<childArr1.length();i++){

                                    try {
                                        JSONObject childObject = childArr1.getJSONObject(i);

                                        String catID = childObject.getString("id");
                                        String catName = childObject.getString("name");

                                        sideMenuArray.add(new sideMenuItem(catID,catName,null,null));


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                JSONArray childArr2 = parentSideMenuArray.get(position).getDiscoverArray();

                                System.out.println("data menu = " + parentSideMenuArray.get(position).gettitle());

                                System.out.println("data menu discover = " + childArr2);


                                if(childArr2 != null){

                                    sideMenuArray.add(new sideMenuItem("","Discover",null,null));

                                    for(int i=0;i<childArr2.length();i++){

                                        try {
                                            JSONObject childObject = childArr2.getJSONObject(i);

                                            String catID = childObject.getString("id");
                                            String catName = childObject.getString("name");

                                            sideMenuArray.add(new sideMenuItem(catID,catName,null,null));


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                                childOpened = true;
                                mmAdapter = new BottomMenuAdapter(sideMenuArray, this, this);
                                mmAdapter.notifyDataSetChanged();
                                sideMenuRV.setAdapter(mmAdapter);
                                sideMenuTopText.setVisibility(View.VISIBLE);
                                sideMenuTopText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        backPressed();
                                    }
                                });
                            }

                            else {

                                    mDrawer.closeDrawer(GravityCompat.START, false);

                                    Fragment fragment = new ListOfProductFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("prodID", parentSideMenuArray.get(position).getcategoryID());
                                    bundle.putString("catName", parentSideMenuArray.get(position).gettitle());
                                    bundle.putString("fromHome", "Home");
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }

                        }

        }

    void backPressed(){
        homeMenuBtn.setVisibility(View.VISIBLE);
        sideMenuArray.clear();
        mmAdapter = new BottomMenuAdapter(parentSideMenuArray,this,this);
        mmAdapter.notifyDataSetChanged();
        sideMenuRV.setAdapter(mmAdapter);
        sideMenuTopText.setVisibility(View.GONE);
        childOpened = false;
        homeMenuBtn.setVisibility(View.VISIBLE);
    }
}


