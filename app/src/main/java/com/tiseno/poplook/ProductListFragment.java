package com.tiseno.poplook;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;

import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.appbar.AppBarLayout;
import com.tiseno.poplook.functions.CustomGrid;
import com.tiseno.poplook.functions.EndlessScrollListener;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.GridBookends;
import com.tiseno.poplook.functions.ProductListItem;
import com.tiseno.poplook.functions.SimpleDividerItemDecoration;
import com.tiseno.poplook.functions.savedItemsItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderEvent;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {
    public static int index = -1;
    public static int top = -1;
    boolean fromSearch;
    ImageButton toTop;
    RecyclerView mRecyclerView;
    GridLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ImageButton filerSrtBtn;
//    ImageView spinner, filteredIV;
    LinearLayout llyBehindProdList;
    ArrayList<ProductListItem> mItems = new ArrayList<ProductListItem>();
    int numberPages = 1;
    protected int scrollPosition = 0;
    private boolean loading = true;
    int parsedcatID, parsedsearchID;
    String catID, search, catName;
    String fromHome, UserID, LanguageID, apiKey;
    boolean filtered, isSorted, btnFilterClicked = false;
//    RelativeLayout filterRL;
    TextView shoppingBagEmptyTV;
    LinearLayout footer;
    String sortType = "0";
    String sizesSelected = "", colourSelected = "";
    String getTierLevel;
    Toolbar toolbar;

    ArrayList<String> listArray_attribute = new ArrayList<String>();
    ArrayList<String> listArray_colour = new ArrayList<String>();
    ArrayList<savedItemsItem> wishListArray = new ArrayList<savedItemsItem>();


    ArrayList<Integer> listArray_attribute_selected = new ArrayList<Integer>();
    ArrayList<Integer> listArray_colour_selected = new ArrayList<Integer>();


    boolean sortBy = false;
    String last_page = "0";
    boolean firstLoad = true;

    int currentPage;
    int lastPage;
    int lastPageFiltered;

    int category_selected = 11;
    String catID_API;

    String[] allSizes;
    String[] allSizesID;
    String[] allColours;
    String[] allColoursID;
    String[] category;
    String[] categoryID;

    int NoOfCBSizes = 0, NoOfCBColor = 0, NoOfCBCategory = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        fromHome = bundle.getString("fromHome");
        filtered = bundle.getBoolean("filtered");

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        ((MainActivity) getActivity()).showBottomBar(true);


        UserID = pref.getString("UserID", "");
        LanguageID = pref.getString("LanguageID", "");
        apiKey = pref.getString("apikey", "");

        getSizeList();
        getColorsList();
        getCategoryList();

        mItems.clear();

        if (fromHome.equals("Home")) {
            catName = bundle.getString("catName");
            catID = bundle.getString("prodID");


            try {
                ((MainActivity) getActivity()).changeToolBarText(catName);
//                Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_visited_main_category", catName);
//                Insider.Instance.getCurrentUser().setCustomAttributeWithString("last_visited_main_category_id", catID);

                InsiderEvent event = Insider.Instance.tagEvent("category_visited");
                event.addParameterWithString("category_name", catName);
                event.addParameterWithString("category_id", catID);
                event.build();

                InsiderEvent pushOptInEvent = Insider.Instance.tagEvent("push_opt_in_on");
                pushOptInEvent.addParameterWithBoolean("opt_in_on",false);
                pushOptInEvent.build();

            } catch (Exception e) {
                ((MainActivity) getActivity()).changeToolBarText("");
            }
            catID = bundle.getString("prodID");
//            parsedcatID = Integer.parseInt(catID);

            numberPages = 1;

            if (!filtered) {

                getProductList();

            } else {
                listArray_attribute = getArguments().getStringArrayList("sizeList");
                listArray_colour = getArguments().getStringArrayList("colourList");
                listArray_attribute_selected = getArguments().getIntegerArrayList("sizeList_Selected");
                listArray_colour_selected = getArguments().getIntegerArrayList("colourList_Selected");
                category_selected = getArguments().getInt("selectedCategory_ID");
                catID_API = getArguments().getString("selectedCategory_ID_API");
                sizesSelected = bundle.getString("sizes");
                colourSelected = bundle.getString("colour");
                numberPages = bundle.getInt("number");
                System.out.println("listArray_attribute is " + listArray_attribute);
                System.out.println("listArray_colour is " + listArray_colour);

                sortBy = bundle.getBoolean("sortEd");
                sortType = bundle.getString("sortType");

                getFilteredProductList();
            }


        } else if (fromHome.equals("Search")) {
            numberPages = 1;

            search = bundle.getString("search");

            ((MainActivity) getActivity()).changeToolBarText("Search ''" + search + "''");

            Insider.Instance.tagEvent("searched").addParameterWithString("keyword", search).build();

            searchProduct();


        }
    }

    View _rootView;
    String SelectedShopID = "1";

    @SuppressLint("ClickableViewAccessibility")


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();

        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        String[] taxonomy = {catName};

        Insider.Instance.visitListingPage(taxonomy);

        if (_rootView != null) {

            fromHome = bundle.getString("fromHome");
            filtered = bundle.getBoolean("filtered");

            if (fromHome.equals("Home")) {
                catName = bundle.getString("catName");
                ((MainActivity) getActivity()).changeToolBarText(catName);
                ((MainActivity) getActivity()).changeToolBarTextView(true);
                ((MainActivity) getActivity()).changeBtnBackView(true);
                ((MainActivity) getActivity()).changeToolBarImageView(false);
                ((MainActivity) getActivity()).changeBtnSearchView(true);
                ((MainActivity) getActivity()).changeBtnBagView(true);
                ((MainActivity) getActivity()).changeBtnWishlistView(true);
                ((MainActivity) getActivity()).changeBtnCloseXView(false);
                ((MainActivity) getActivity()).setDrawerState(true);

            } else if (fromHome.equals("Search")) {

                search = bundle.getString("search");


                ((MainActivity) getActivity()).changeToolBarText("Search ''" + search + "''");

                ((MainActivity) getActivity()).changeToolBarTextView(true);
                ((MainActivity) getActivity()).changeBtnBackView(true);
                ((MainActivity) getActivity()).changeToolBarImageView(false);
                ((MainActivity) getActivity()).changeBtnSearchView(true);
                ((MainActivity) getActivity()).changeBtnBagView(true);
                ((MainActivity) getActivity()).changeBtnWishlistView(true);
                ((MainActivity) getActivity()).changeBtnCloseXView(false);
                ((MainActivity) getActivity()).setDrawerState(true);

            }


            return _rootView;

        } else {
            _rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
            fromHome = bundle.getString("fromHome");
            filtered = bundle.getBoolean("filtered");

            ((MainActivity) getActivity()).changeToolBarTextView(true);
            ((MainActivity) getActivity()).changeBtnBackView(true);
            ((MainActivity) getActivity()).changeToolBarImageView(false);
            ((MainActivity) getActivity()).changeBtnSearchView(true);
            ((MainActivity) getActivity()).changeBtnBagView(true);
            ((MainActivity) getActivity()).changeBtnWishlistView(true);
            ((MainActivity) getActivity()).changeBtnCloseXView(false);
            ((MainActivity) getActivity()).setDrawerState(true);

            toTop = (ImageButton) _rootView.findViewById(R.id.toTop);
            shoppingBagEmptyTV = (TextView) _rootView.findViewById(R.id.shoppingBagEmptyTV);
            llyBehindProdList = (LinearLayout) _rootView.findViewById(R.id.llyBehindProdList);
            filerSrtBtn = (ImageButton) _rootView.findViewById(R.id.filterAndSortButton);

            if (filtered) {

                category_selected = getArguments().getInt("selectedCategory_ID");
                catID_API = getArguments().getString("selectedCategory_ID_API");
                listArray_attribute = getArguments().getStringArrayList("sizeList");
                listArray_colour = getArguments().getStringArrayList("colourList");
                listArray_attribute_selected = getArguments().getIntegerArrayList("sizeList_Selected");
                listArray_colour_selected = getArguments().getIntegerArrayList("colourList_Selected");
                sizesSelected = bundle.getString("sizes");
                colourSelected = bundle.getString("colour");
//                numberPages =  bundle.getInt("number");
                System.out.println("listArray_attribute is " + listArray_attribute);
                System.out.println("listArray_colour is " + listArray_colour);

                sortBy = bundle.getBoolean("isSorted");
                sortType = bundle.getString("sortType");


            } else {

            }

            shoppingBagEmptyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//            toTop.setOnTouchListener(new View.OnTouchListener() {
//
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        mLayoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
//                        toTop.setImageResource(R.drawable.ic_scrolltotop2_36dp);
//
//                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                        toTop.setImageResource(R.drawable.ic_scrolltotop1_36dp);
//
//                    }
//
//
//                    return false;
//                }
//            });


            if (fromHome.equals("Home")) {

                filerSrtBtn.setVisibility(View.VISIBLE);

                System.out.println("filtered value = " + filtered);

                catName = bundle.getString("catName");
                ((MainActivity) getActivity()).changeToolBarText(catName);
                catID = bundle.getString("prodID");

                // Calling the RecyclerView
                mRecyclerView = (RecyclerView) _rootView.findViewById(R.id.recycler_view);
//                mRecyclerView.setHasFixedSize(true);

                // The number of Columns
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));
                if (!sortBy) {
                    mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
                        @Override
                        public void onLoadMore(int current_page) {
                            System.out.println("current page is =" + current_page);
                            if (!filtered) {
                                getProductList();
                            } else {
                                getFilteredProductList();
                            }

//                    numberPages++;

                        }
                    });
                } else {

                    mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
                        @Override
                        public void onLoadMore(int current_page) {
                            System.out.println("current page is =" + current_page);
                            if (!filtered) {
                                getProductList();
                            } else {
                                getFilteredProductList();
                            }

//                    numberPages++;

                        }

                    });
                }

            } else if (fromHome.equals("Search")) {

                filerSrtBtn.setVisibility(View.INVISIBLE);


                search = bundle.getString("search");
                ((MainActivity) getActivity()).changeToolBarText("Search ''" + search + "''");


//                searchProduct();

                // Calling the RecyclerView
                mRecyclerView = (RecyclerView) _rootView.findViewById(R.id.recycler_view);
//            mRecyclerView.setHasFixedSize(true);

                // The number of Columns
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        System.out.println("Heloooaskdoskd" + current_page);

                        searchProduct();

//                    numberPages++;

                    }

                });
            }

            filerSrtBtn.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

//                                                Fragment fragment = new FilterAndSortFragment();
//
//
                                                   Bundle bundle = new Bundle();
                                                   bundle.putString("fromHome", fromHome);
                                                   bundle.putString("prodID", catID);
                                                   bundle.putString("catName", catName);

//
                                                   bundle.putStringArrayList("sizeList", listArray_attribute);
                                                   bundle.putStringArrayList("colourList", listArray_colour);
//
                                                   bundle.putIntegerArrayList("sizeList_Selected", listArray_attribute_selected);
                                                   bundle.putIntegerArrayList("colourList_Selected", listArray_colour_selected);

                                                   bundle.putInt("category_id", category_selected);
                                                   bundle.putString("sortType", sortType);

                                                   bundle.putStringArray("sizesFilterData",allSizes);
                                                   bundle.putStringArray("sizesIDFilterData",allSizesID);
                                                   bundle.putStringArray("coloursFilterData",allColours);
                                                   bundle.putStringArray("coloursIDFilterData",allColoursID);
                                                   bundle.putStringArray("categoryFilterData",category);
                                                   bundle.putStringArray("categoryIDFilterData",categoryID);
//
//
//                                                fragment.setArguments(bundle);
//                                                FragmentManager fragmentManager = getActivity().getFragmentManager();
//                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                                fragmentTransaction.replace(R.id.fragmentContainer, fragment,"FilterAndSortFragment");
//                                                fragmentTransaction.addToBackStack(null);
//                                                fragmentTransaction.commit();

                                                   Fragment fragment = new NewFilterAndSortFragment();

                                                   fragment.setArguments(bundle);
                                                   FragmentManager fragmentManager = getActivity().getFragmentManager();
                                                   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                   fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                   fragmentTransaction.replace(R.id.fragmentContainer, fragment, "NewFilterAndSortFragment");
                                                   fragmentTransaction.addToBackStack(null);
                                                   fragmentTransaction.commit();

                                               }

                                           }

            );
            return _rootView;
        }


    }

    //    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
//
//    /**
//     * This is a method for Fragment.
//     * You can do the same in onCreate or onRestoreInstanceState
//     */
    @Override
    public void onDestroyView() {
        if (_rootView.getParent() != null) {
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        }
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);
    }

    private void getProductList() {
        System.out.println("numberPages  " + parsedcatID);

        if (UserID.length() > 0) {

            getSavedItems();

        }

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");
        //
        String action = "Products/category/id/" + catID + "/shop/" + SelectedShopID + "/num_page/" + numberPages + "/sort_options/" + sortType + "?api_version=apps&apikey=" + apikey + "&tier=" + pref.getString("tier_level","") + "&user_id=" + UserID;
        if (firstLoad) {
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(action);

            numberPages++;
            firstLoad = false;

        } else {
            if (!sortBy) {
                WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
                callws.execute(action);

                numberPages++;
            } else {
                WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                callws.execute(action);

                numberPages++;
            }

        }


    }

    public void getSavedItems() {
        String action = "Wishlists/list/customer/" + UserID + "/lang/" + LanguageID + "/sort_options/" + sortType + "?apikey=" + apiKey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void searchProduct() {

        System.out.println("searchProduct  " + parsedsearchID);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");
        getTierLevel = pref.getString("popup_tier", "1");
        //
//        String action="Products/search/keyword/"+search+"/lang/1/shop/"+SelectedShopID+"/num_page/"+numberPages+"/sort_options/"+sortType+"?apikey="+apikey;
        String action = "Products/search/keyword/" + search + "/lang/1/shop/" + SelectedShopID + "/num_page/" + numberPages + "/sort_options/" + sortType + "?apikey=" + apikey + "&tier=" + getTierLevel;

        if (firstLoad) {
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

            callws.execute(action);

            numberPages++;
            firstLoad = false;

        } else {
            if (!sortBy) {
                WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
                callws.execute(action);

                numberPages++;
            } else {
                WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                callws.execute(action);

                numberPages++;
            }

        }


    }

    private void getFilteredProductList() {

        System.out.println("getNumberPages = " + numberPages);

        if (UserID.length() > 0) {

            getSavedItems();

        }

        String colourFilter = "";
        for (int i = 0; i < listArray_colour.size(); i++) {
            if (i == 0) {
                colourFilter += listArray_colour.get(i);
            } else {
                colourFilter += "," + listArray_colour.get(i);
            }

        }
        String sizeFilter = "";
        for (int i = 0; i < listArray_attribute.size(); i++) {
            if (i == 0) {
                sizeFilter += listArray_attribute.get(i);
            } else {
                sizeFilter += "," + listArray_attribute.get(i);
            }

        }
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");

        String action;

        if (catID.equals("45")) {

            action = "Products/filterCategoryProducts?apikey=" + apikey + "&shop=" + SelectedShopID + "&category=" + catID + "&color=" + colourFilter + "&product_attribute=" + sizeFilter + "&num_page=" + numberPages + "&sort_options=" + sortType + "&category_filter=" + catID_API;

        } else {
            action = "Products/filterCategoryProducts?apikey=" + apikey + "&shop=" + SelectedShopID + "&category=" + catID + "&color=" + colourFilter + "&product_attribute=" + sizeFilter + "&num_page=" + numberPages + "&sort_options=" + sortType;

        }

        if (firstLoad) {
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

            callws.execute(action);

            numberPages++;
            firstLoad = false;
        } else {
            if (!sortBy) {
                WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
                callws.execute(action);

                numberPages++;
            } else {
                WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                callws.execute(action);

                numberPages++;
            }
        }

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if (result != null) {
            try {

                if (result.getString("action").equals("Wishlists_list")) {


                    if (result.getBoolean("status")) {

                        wishListArray.clear();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject = result.getJSONObject("data");
                        String wishlistID = jsonObject.getString("id_wishlist");

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("WishlistID", wishlistID);
                        editor.apply();

                        JSONArray jsonArr = new JSONArray();
                        jsonArr = jsonObject.getJSONArray("product_list");
                        try {

                            editor.putString("wishlistItem", String.valueOf(jsonArr.length()));
                            editor.apply();
                            ((MainActivity) getActivity()).changeToolBarWishNotiText(String.valueOf(jsonArr.length()));
                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject jObj = jsonArr.getJSONObject(i);
                                String productID = jObj.getString("id_product");
                                String productName = jObj.getString("name");
                                String productSize = jObj.getString("attributes_small");
                                String productPrice = jObj.getString("price");
                                String productAttributeID = jObj.getString("id_product_attribute");
                                int productAvailableQuantity = jObj.getInt("available_quantity");
                                String productExpires = jObj.getString("no_of_days");
                                String productImage = jObj.getString("image_url");

                                wishListArray.add(new savedItemsItem(productID, productName, productSize, productPrice, productImage, productAttributeID, wishlistID, String.valueOf(productAvailableQuantity), productExpires));

                            }

                            refreshRecyclerView("finishLoad");

                        } catch (Exception e) {

                        }
                    } else {

                        wishListArray.clear();

                        refreshRecyclerView("finishLoad");

                    }
                } else if (result.getBoolean("status")) {
                    if (result.getString("action").equals("Products_category")) {
                        sortBy = false;
                        JSONObject pagination = result.getJSONObject("pagination");
                        currentPage = pagination.getInt("current_page");
                        lastPage = pagination.getInt("last_page");
                        JSONArray jsonArr = null;
                        jsonArr = result.getJSONArray("data");
                        JSONObject jsonCategoryobj = result.getJSONObject("category");

                        try {


                            for (int j = 0; j < jsonArr.length(); j++) {
                                JSONObject jObject = jsonArr.getJSONObject(j);

                                String id_product = jObject.getString("id_product");
                                String name = jObject.getString("name");
                                String reference = jObject.getString("reference");
                                JSONArray image_url = jObject.getJSONArray("image_url");
                                String tax_rate = jObject.getString("tax_rate");
                                String price_with_tax = jObject.getString("price");
                                String reduction = jObject.getString("price_reduction");
                                String out_of_stock = jObject.getString("product_out_of_stock");
                                String total_colours = jObject.getString("total_colours");
                                String id_product_attribute = jsonArr.getJSONObject(j).getString("id_product_attribute");
                                String get_coll_name = jsonArr.getJSONObject(j).getString("collection_name");
                                String get_online_exclusive = jsonArr.getJSONObject(j).getString("online_exclusive");
                                String get_discount = jsonArr.getJSONObject(j).getString("discount_label");
                                String get_Price_withOut_Reduction = jsonArr.getJSONObject(j).getString("price_without_reduction");
                                String get_discount_text = jsonArr.getJSONObject(j).getString("discount_text");


                                JSONArray related_colour_data = null;
                                try {
                                    related_colour_data = jObject.getJSONArray("related_colour_data");

                                } catch (Exception e) {

                                    System.out.println("false");

                                }
//
//                                if (jsonCategoryobj.isNull("tier")) {
//                                    mItems.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, total_colours, id_product_attribute, related_colour_data, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text));
//
//                                } else {
//
//                                    if (UserID.length() > 0) {
//
//                                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
//
//                                        JSONArray getArr = jsonCategoryobj.getJSONArray("tier");
//                                        JSONObject jsonObj = getArr.getJSONObject(0);
//                                        String getTierLevel = jsonObj.getString("tier");
//
//
//                                        if (getTierLevel.contains(pref.getString("tier_level", ""))) {

//                                            mItems.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, total_colours, id_product_attribute, related_colour_data, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text));

//                                        }
//
//                                    }
//
//                                }

                            }


                            if (jsonArr.length() == 0 || jsonArr.length() == 1) {
                                refreshRecyclerView("finishLoad");
                            } else {
                                refreshRecyclerView("");
                            }
                            if (mItems.size() == 0) {
                                if (!filtered) {
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product in this category");
                                } else {
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product using this filter preference");
                                }
                            }
                        } catch (Exception e) {
                            if (mItems.size() != 0) {
                                refreshRecyclerView("finishLoad");
                            } else {
                                if (!filtered) {
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product in this category");
                                } else {
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product using this filter preference");
                                }
                            }
                        }

                    } else if (result.getString("action").equals("Products_filterCategoryProducts")) {
                        sortBy = false;
                        System.out.println("hahaaaaaaac " + result);
                        JSONObject jsonObject = result.getJSONObject("data");
                        String stop = jsonObject.getString("stop");
//                        currentPage = jsonObject.getInt("current_page");
                        lastPageFiltered = Integer.parseInt(stop);
                        if (numberPages > Integer.parseInt(stop) + 1) {
                            if (mItems.size() != 0) {
                                refreshRecyclerView("finishLoad");
                            } else {
                                if (!filtered) {
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product in this category");
                                } else {
                                    System.out.println("MANA NI!! " + numberPages);
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product using this filter preference");
                                }
                            }
                        } else {
                            JSONArray jsonArr = null;
                            jsonArr = jsonObject.getJSONArray("products");

                            try {
                                for (int j = 0; j < jsonArr.length(); j++) {
                                    String id_product = jsonArr.getJSONObject(j).getString("id_product");
                                    String name = jsonArr.getJSONObject(j).getString("name");
                                    String reference = jsonArr.getJSONObject(j).getString("reference");
                                    JSONArray image_url = jsonArr.getJSONObject(j).getJSONArray("image_url");
                                    String tax_rate = jsonArr.getJSONObject(j).getString("tax_rate");
                                    String price_with_tax = jsonArr.getJSONObject(j).getString("price");
                                    String reduction = jsonArr.getJSONObject(j).getString("price_reduction");
                                    String out_of_stock = jsonArr.getJSONObject(j).getString("product_out_of_stock");
                                    String id_product_attribute = jsonArr.getJSONObject(j).getString("id_product_attribute");
                                    String get_coll_name = jsonArr.getJSONObject(j).getString("collection_name");
                                    String get_online_exclusive = jsonArr.getJSONObject(j).getString("online_exclusive");
                                    String get_discount = jsonArr.getJSONObject(j).getString("discount_label");
                                    String get_Price_withOut_Reduction = jsonArr.getJSONObject(j).getString("price_without_reduction");
                                    String get_discount_text = jsonArr.getJSONObject(j).getString("discount_text");


//                                    mItems.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, "0", id_product_attribute, null, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text));

                                    System.out.println("hahaaaaaaa");

                                }
                                System.out.println("dataaaa" + mItems.size());
                                refreshRecyclerView("");
                                if (mItems.size() == 0) {
                                    if (!filtered) {
                                        refreshRecyclerView("finishLoad");
                                        shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                        shoppingBagEmptyTV.setText("There are no product in this category");
                                    } else {
                                        refreshRecyclerView("finishLoad");
                                        shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                        shoppingBagEmptyTV.setText("There are no product using this filter preference");
                                    }
                                }
                            } catch (Exception e) {
                                if (mItems.size() != 0) {
                                    refreshRecyclerView("finishLoad");
                                } else {
                                    if (!filtered) {
                                        refreshRecyclerView("finishLoad");
                                        shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                        shoppingBagEmptyTV.setText("There are no product in this category");
                                    } else {
                                        refreshRecyclerView("finishLoad");
                                        shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                        shoppingBagEmptyTV.setText("There are no product using this filter preference");
                                    }
                                }
                            }
                        }
                    } else if (result.getString("action").equals("Products_search")) {
                        sortBy = false;
                        System.out.println("hahaaaaaaac " + result);
                        JSONArray jsonArr = null;
                        boolean end = true;
                        JSONObject jsonObject = result.getJSONObject("data");
                        JSONObject pagination = jsonObject.getJSONObject("pagination");
                        String total_items = pagination.getString("total_items");

                        if (total_items.equals("0")) {
                            refreshRecyclerViewSearch("finishLoad");
                            shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                            shoppingBagEmptyTV.setText("No search result found for your search ''" + search + "''");
                        }
                        try {
                            jsonArr = jsonObject.getJSONArray("result");
                            System.out.println("hahaaaaaaad " + jsonArr.length());
                            try {
                                for (int j = 0; j < jsonArr.length(); j++) {
                                    String id_product = jsonArr.getJSONObject(j).getString("id_product");
                                    String name = jsonArr.getJSONObject(j).getString("name");
                                    String reference = jsonArr.getJSONObject(j).getString("reference");
                                    JSONArray image_url = jsonArr.getJSONObject(j).getJSONArray("image_url");
                                    String tax_rate = jsonArr.getJSONObject(j).getString("tax_rate");
                                    String price_with_tax = jsonArr.getJSONObject(j).getString("price");
                                    String reduction = jsonArr.getJSONObject(j).getString("reduction");
                                    String out_of_stock = jsonArr.getJSONObject(j).getString("product_out_of_stock");
                                    String id_product_attribute = jsonArr.getJSONObject(j).getString("id_product_attribute");
                                    String get_coll_name = jsonArr.getJSONObject(j).getString("collection_name");
                                    String get_online_exclusive = jsonArr.getJSONObject(j).getString("online_exclusive");
                                    String get_discount = jsonArr.getJSONObject(j).getString("discount_label");
                                    String get_Price_withOut_Reduction = jsonArr.getJSONObject(j).getString("price_without_reduction");
                                    String get_discount_text = jsonArr.getJSONObject(j).getString("discount_text");


//                                    mItems.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, "0", id_product_attribute, null, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text));

                                }
                                System.out.println("dataaaa" + mItems.size());
                                refreshRecyclerViewSearch("");
                            } catch (Exception e) {
                                if (mItems.size() != 0) {
                                    refreshRecyclerViewSearch("finishLoad");
                                } else {
                                    refreshRecyclerViewSearch("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("No search result found for your search ''" + search + "''");
                                }
                            }
                        } catch (Exception e) {
                            end = jsonObject.getBoolean("result");
                            if (!end) {
                                refreshRecyclerViewSearch("finishLoad");
                            }
                        }
                        Insider.Instance.tagEvent("search_made").build();

                        logSearchedEvent("", search, true);

                    }
                    else if(result.getString("action").equals("Products_filterType_size")){
                        if (result.getBoolean("status")) {

                            JSONArray jsonArr = new JSONArray();
                            jsonArr = result.getJSONArray("data");

                            allSizes = new String[jsonArr.length()];
                            allSizesID = new String[jsonArr.length()];


                            for (int i = 0; i < jsonArr.length(); i++) {
                                NoOfCBSizes++;
                                JSONObject jObj = jsonArr.getJSONObject(i);

                                final String attributeID = jObj.getString("id_combination");
                                final String attributeName = jObj.getString("name");

                                String attribute = attributeName;
                                String attID = attributeID;

                                allSizes[i] = attribute;
                                allSizesID[i] = attID;
                            }
                        }
                        }
                    else if(result.getString("action").equals("Products_filterType_color")){
                        if (result.getBoolean("status")) {

                            JSONArray jsonArr = new JSONArray();
                            jsonArr = result.getJSONArray("data");

                            allColours = new String[jsonArr.length()];
                            allColoursID = new String[jsonArr.length()];


                            for (int i = 0; i < jsonArr.length(); i++) {
                                NoOfCBColor++;
                                JSONObject jObj = jsonArr.getJSONObject(i);

                                final String colorID = jObj.getString("id_combination");
                                final String colorName = jObj.getString("name");

                                String color = colorName;
                                String clrID = colorID;

                                allColours[i] = color;
                                allColoursID[i] = colorID;
                            }
                        }
                    }
                    else if(result.getString("action").equals("Menus_filterCategory")){
                        if (result.getBoolean("status")) {

                            JSONArray jsonArr = new JSONArray();
                            jsonArr = result.getJSONArray("data");

                            category = new String[jsonArr.length()];
                            categoryID = new String[jsonArr.length()];


                            for (int i = 0; i < jsonArr.length(); i++) {
                                NoOfCBCategory++;
                                JSONObject jObj = jsonArr.getJSONObject(i);

                                final String catID = jObj.getString("id_combination");
                                final String categoryName = jObj.getString("name");

                                String cat_Name = categoryName;
                                String cat_ID = catID;

                                category[i] = cat_Name;
                                categoryID[i] = cat_ID;
                            }
                        }
                    }
                }
                else {
                    try {
                        if (result.getString("action").equals("Products_search")) {
                            if (mItems.size() != 0) {
                                refreshRecyclerViewSearch("finishLoad");
                            } else {
                                refreshRecyclerViewSearch("finishLoad");
                                shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                shoppingBagEmptyTV.setText("No search result found for your search ''" + search + "''");
                            }
                        } else {
                            if (mItems.size() != 0) {
                                refreshRecyclerView("finishLoad");
                            } else {
                                if (!filtered) {
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product in this category");
                                } else {
                                    refreshRecyclerView("finishLoad");
                                    shoppingBagEmptyTV.setVisibility(View.VISIBLE);
                                    shoppingBagEmptyTV.setText("There are no product using this filter preference");
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }


            } catch (Exception e) {

            }

        } else {
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

    public void refreshRecyclerViewSearch(String finishLoad) {
        System.out.println("OMG WHY JADI SEARCH");

        mAdapter = new CustomGrid(getActivity(), mItems, this, wishListArray);
        mAdapter.notifyDataSetChanged();
        final GridBookends mBookends = new GridBookends(mAdapter, mLayoutManager);

        index = mLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - mRecyclerView.getPaddingTop());
//            mRecyclerView.scrollToPosition(index);
        mLayoutManager.scrollToPositionWithOffset(index, top);

        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
        footer = (LinearLayout) inflater1.inflate(R.layout.progressbar_footer, mRecyclerView, false);
        if (!finishLoad.equals("finishLoad")) {

            if (mItems.size() > 10) {
                System.out.println("PECAH OTAK LA WEI" + mItems.size());
                mBookends.addFooter(footer);
                mRecyclerView.setAdapter(mBookends);
            } else {
                mRecyclerView.setAdapter(mBookends);
            }
        } else {
            mRecyclerView.setAdapter(mBookends);
        }
        fromSearch = true;

    }

    public void clicked(int pos) {

        if (fromSearch) {
            Fragment fragment = new ProductInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("prodID", mItems.get(pos).getproductID());
            String actionBarSearchName = "Search ''" + search + "''";
            bundle.putString("catName", actionBarSearchName);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Fragment fragment = new ProductInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("prodID", mItems.get(pos).getproductID());
            bundle.putString("catName", catName);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void refreshRecyclerView(String finishLoad) {
        mAdapter = new CustomGrid(getActivity(), mItems, this, wishListArray);
        mAdapter.notifyDataSetChanged();
        final GridBookends mBookends = new GridBookends(mAdapter, mLayoutManager);

        index = mLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - mRecyclerView.getPaddingTop());
//            mRecyclerView.scrollToPosition(index);
        mLayoutManager.scrollToPositionWithOffset(index, top);

        System.out.println("pagelah now is = " + currentPage);
        System.out.println("last pagelah is = " + lastPageFiltered);

        Runtime runtime = Runtime.getRuntime();
        long usedMemory=runtime.totalMemory() - runtime.freeMemory();
        long maxMemory=runtime.maxMemory();


        Log.v("Product List", "Used Memory:" + Long.toString(usedMemory));

        long availableMemory=maxMemory-usedMemory;

        Log.v("Product List", "Available Memory:" + Long.toString(availableMemory));


        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
        footer = (LinearLayout) inflater1.inflate(R.layout.progressbar_footer, mRecyclerView, false);

//        if (filtered) {
//            if(numberPages <= lastPageFiltered){
//                mBookends.addFooter(footer);
//            }
//        }
//        else {
//            if(numberPages <= lastPage){
//                mBookends.addFooter(footer);
//            }
//        }
//
//        mRecyclerView.setAdapter(mBookends);


        if (!finishLoad.equals("finishLoad")) {

            if (mItems.size() > 50) {
//                mBookends.addFooter(footer);
                mRecyclerView.setAdapter(mBookends);
            } else {
                mRecyclerView.setAdapter(mBookends);
            }
        } else {
            if (mItems.size() > 50) {
                mBookends.addFooter(footer);
                mRecyclerView.setAdapter(mBookends);

            } else {
                mRecyclerView.setAdapter(mBookends);

            }
        }
        fromSearch = false;

    }

    public void setDimBack(float dimBack) {
        llyBehindProdList.setAlpha(dimBack);
    }

    public void logSearchedEvent(String contentType, String searchString, boolean success) {
        Bundle params = new Bundle();
        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, searchString);
        params.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, success ? 1 : 0);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, params);
    }

    private void getSizeList() {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey = pref.getString("apikey", "");
        String action = "Products/filterType/name/size?apikey=" + apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);

    }
    private void getColorsList() {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey = pref.getString("apikey", "");
        String action = "Products/filterType/name/color?apikey=" + apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);

    }
    private void getCategoryList() {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey = pref.getString("apikey", "");
        String action = "Menus/filterCategory?apikey=" + apikey + "&shop=1&category=" + catID + "&num_page=0&sort_options=1&category_filter=2";

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);

    }
}

