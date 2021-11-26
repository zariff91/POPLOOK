package com.tiseno.poplook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.adroitandroid.chipcloud.FlowLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tiseno.poplook.functions.EndlessScrollListener;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.GridViewProductAdapter;
import com.tiseno.poplook.functions.ProductListItem;
import com.tiseno.poplook.functions.attributeItem;
import com.tiseno.poplook.functions.savedItemsItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;
import com.tiseno.poplook.webservice.WebServiceAccessPut;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ListOfProductFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>, GridViewProductAdapter.ItemClickListener {

    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;

    RecyclerView productListTable;
    GridLayoutManager mLayoutManager;
    GridViewProductAdapter mAdapter;

    String fromSearch, UserID, LanguageID, apiKey;
    String SelectedShopID,SelectedCountryCurrency;
    String catID, search, catName;

    int currentPage;
    int lastPage;

    int currentPageFilter;
    int lastPageFilter;
    int lastPageSearch;

    String sortType = "0";
    String catIDFilter = "";
    String getTierLevel;
    int catFilterPosition;
    int numberPages = 1;

    public static int index = -1;
    public static int top = -1;

    private EndlessScrollListener scrollListener;


    View _rootView;
    PopupWindow popupWindow;

    ChipCloud sortChip;
    ChipCloud sizeChip;
    ChipCloud colourChip;
    ChipCloud categoryChip;

//    Toolbar toolbar;

    ImageButton filterBottomButton;

    ArrayList<ProductListItem> mProductObj = new ArrayList<ProductListItem>();
    List<String> wishlistProdIdArray = new ArrayList<String>();
    ArrayList<savedItemsItem> wishListArray = new ArrayList<savedItemsItem>();
    ArrayList<String> colourIDListForFilter = new ArrayList<String>();
    ArrayList<String> sizeIDListForFilter = new ArrayList<String>();
    ArrayList<Integer> colourIDSelectedIndex = new ArrayList<Integer>();
    ArrayList<Integer> sizeIDSelectedIndex = new ArrayList<Integer>();

    String[] allSizes;
    String[] allSizesID;
    String[] allColours;
    String[] allColoursID;
    String[] category;
    String[] categoryID;

    TextView sortByText,categoryByText,sizeByText,colourByText,clearText,applyText,noProductLbl;
    Boolean isFiltered = false;
    Boolean isSearch = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);

        UserID = pref.getString("UserID", "");
        LanguageID = pref.getString("LanguageID", "");
        apiKey = pref.getString("apikey", "");
        SelectedShopID = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency = pref.getString("SelectedCountryCurrency", "");

        Bundle bundle = this.getArguments();
        catName = bundle.getString("catName");
        catID = bundle.getString("prodID");
        fromSearch = bundle.getString("fromHome");
        search = bundle.getString("search");

    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
//
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
//                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        if (_rootView != null) {

            ((MainActivity) getActivity()).changeToolBarText(catName);
            if(fromSearch.equals("Search")){
                ((MainActivity) getActivity()).changeToolBarText("Search ''" + search + "''");
            }

            return _rootView;
        }

        else {

           _rootView = inflater.inflate(R.layout.list_product_new, container, false);

            ((MainActivity) getActivity()).changeToolBarText(catName);
            ((MainActivity) getActivity()).changeToolBarTextView(true);
            ((MainActivity) getActivity()).changeBtnBackView(false);
            ((MainActivity) getActivity()).changeToolBarImageView(false);
            ((MainActivity) getActivity()).changeBtnSearchView(true);
            ((MainActivity) getActivity()).changeBtnBagView(true);
            ((MainActivity) getActivity()).changeBtnWishlistView(true);
            ((MainActivity) getActivity()).changeBtnCloseXView(false);
            ((MainActivity) getActivity()).setDrawerState(true);
            ((MainActivity) getActivity()).hideTopBar(false);


            productListTable = _rootView.findViewById(R.id.product_list_table);
            filterBottomButton = _rootView.findViewById(R.id.filterButton);

            noProductLbl = _rootView.findViewById(R.id.noProductLabel);
            noProductLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

            filterBottomButton.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

//
                                                   BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialogTheme);

                                                   View bottomSheetView = inflater.inflate(R.layout.new_filter_bottom_sheet,(LinearLayout)_rootView.findViewById(R.id.new_filter_bottom_sheet));

                                                   sortChip = (ChipCloud) bottomSheetView.findViewById(R.id.sort_by_chip);

                                                   sortByText = bottomSheetView.findViewById(R.id.sort_by_text);
                                                   sortByText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                                                   categoryByText = bottomSheetView.findViewById(R.id.category_filter_text);
                                                   categoryByText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                                                   sizeByText = bottomSheetView.findViewById(R.id.size_filter_text);
                                                   sizeByText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                                                   colourByText = bottomSheetView.findViewById(R.id.colour_filter_text);
                                                   colourByText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

//                                                   clearText = bottomSheetView.findViewById(R.id.clear_text);
//                                                   clearText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//                                                   clearText.setOnClickListener(new View.OnClickListener() {
//                                                       @Override
//                                                       public void onClick(View view) {
//                                                           for(int x = 0;x<sizeIDSelectedIndex.size();x++){
//                                                               sizeChip.chipDeselected(sizeIDSelectedIndex.get(x));
//                                                           }
//                                                       }
//                                                   });

                                                   applyText = bottomSheetView.findViewById(R.id.apply_text);
                                                   applyText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                                                   applyText.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View view) {

                                                           System.out.println("filter 1 = " + sizeIDListForFilter);
                                                           System.out.println("filter 2 = " + colourIDListForFilter);

                                                           if(sizeIDListForFilter.size() != 0 || colourIDListForFilter.size() !=0 || !catIDFilter.equals("")){
                                                               mProductObj.clear();
                                                               mAdapter.notifyDataSetChanged();
                                                               getFilteredProductList(1);
                                                               isFiltered = true;
                                                           }

                                                           else {
                                                               mProductObj.clear();
                                                               mAdapter.notifyDataSetChanged();
                                                               getProductList(1);
                                                               isFiltered = false;
                                                           }
                                                           bottomSheetDialog.dismiss();
                                                           scrollListener.reset();
                                                       }
                                                   });

                                                   if(catID.equals("45")){

                                                       categoryByText.setVisibility(View.VISIBLE);
                                                       categoryChip = (ChipCloud) bottomSheetView.findViewById(R.id.filter_by_category);
                                                       categoryChip.setVisibility(View.VISIBLE);
                                                       categoryChip.setMode(ChipCloud.Mode.SINGLE);
                                                       new ChipCloud.Configure()
                                                               .chipCloud(categoryChip)
                                                               .selectedColor(Color.parseColor("#000000"))
                                                               .selectedFontColor(Color.parseColor("#ffffff"))
                                                               .deselectedColor(Color.parseColor("#e1e1e1"))
                                                               .deselectedFontColor(Color.parseColor("#333333"))
                                                               .labels(category)
                                                               .gravity(ChipCloud.Gravity.CENTER)
                                                               .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                                                               .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                                                               .chipListener(new ChipListener() {
                                                                   @Override
                                                                   public void chipSelected(int index) {
                                                                       String selectedSizeID = categoryID[index];
                                                                       catIDFilter = selectedSizeID;
                                                                       catFilterPosition = index;
                                                                   }
                                                                   @Override
                                                                   public void chipDeselected(int index) {
                                                                       catIDFilter = "";
                                                                   }
                                                               })
                                                               .build();

                                                       if(!catIDFilter.equals(""))
                                                       {
                                                           categoryChip.setSelectedChip(catFilterPosition);
                                                       }


                                                       String[] title = {
                                                               "Date",
                                                               "Price lowest to highest",
                                                               "Price highest to lowest",
                                                       };

                                                       sortChip.setMode(ChipCloud.Mode.SINGLE);

                                                       new ChipCloud.Configure()
                                                               .chipCloud(sortChip)
                                                               .selectedColor(Color.parseColor("#000000"))
                                                               .selectedFontColor(Color.parseColor("#ffffff"))
                                                               .deselectedColor(Color.parseColor("#e1e1e1"))
                                                               .deselectedFontColor(Color.parseColor("#333333"))
                                                               .labels(title)
                                                               .gravity(ChipCloud.Gravity.CENTER)
                                                               .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                                                               .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                                                               .chipListener(new ChipListener() {
                                                                   @Override
                                                                   public void chipSelected(int index) {
                                                                       sortType = String.valueOf(index);
                                                                   }
                                                                   @Override
                                                                   public void chipDeselected(int index) {
                                                                       sortType = "0";
                                                                   }
                                                               })
                                                               .build();

                                                       int i = Integer.valueOf(sortType);
                                                       sortChip.setSelectedChip(i);
                                                   }

                                                   else {
                                                       String[] title = {
                                                               "Date",
                                                               "Price lowest to highest",
                                                               "Price highest to lowest",
                                                               "Popularity"
                                                       };

                                                       sortChip.setMode(ChipCloud.Mode.SINGLE);

                                                       new ChipCloud.Configure()
                                                               .chipCloud(sortChip)
                                                               .selectedColor(Color.parseColor("#000000"))
                                                               .selectedFontColor(Color.parseColor("#ffffff"))
                                                               .deselectedColor(Color.parseColor("#e1e1e1"))
                                                               .deselectedFontColor(Color.parseColor("#333333"))
                                                               .labels(title)
                                                               .gravity(ChipCloud.Gravity.CENTER)
                                                               .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                                                               .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                                                               .chipListener(new ChipListener() {
                                                                   @Override
                                                                   public void chipSelected(int index) {
                                                                       sortType = String.valueOf(index);
                                                                       if(index == 3){
                                                                           sortType = "6";
                                                                       }
                                                                   }
                                                                   @Override
                                                                   public void chipDeselected(int index) {
                                                                       sortType = "0";
                                                                   }
                                                               })
                                                               .build();

                                                       if(sortType.equals("6")){
                                                           sortChip.setSelectedChip(3);
                                                       }
                                                       else {
                                                           int i = Integer.valueOf(sortType);
                                                           sortChip.setSelectedChip(i);
                                                       }

                                                   }

                                                   sizeChip = (ChipCloud) bottomSheetView.findViewById(R.id.filter_by_size);
                                                   sizeChip.setMode(ChipCloud.Mode.MULTI);
                                                   new ChipCloud.Configure()
                                                           .chipCloud(sizeChip)
                                                           .selectedColor(Color.parseColor("#000000"))
                                                           .selectedFontColor(Color.parseColor("#ffffff"))
                                                           .deselectedColor(Color.parseColor("#e1e1e1"))
                                                           .deselectedFontColor(Color.parseColor("#333333"))
                                                           .labels(allSizes)
                                                           .gravity(ChipCloud.Gravity.CENTER)
                                                           .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                                                           .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                                                           .chipListener(new ChipListener() {
                                                               @Override
                                                               public void chipSelected(int index) {
                                                                   String selectedSizeID = allSizesID[index];
                                                                   if(!sizeIDListForFilter.contains(selectedSizeID)){
                                                                       sizeIDListForFilter.add(selectedSizeID);
                                                                   }
                                                                   if(!sizeIDSelectedIndex.contains(index)){
                                                                       sizeIDSelectedIndex.add(index);
                                                                   }
                                                               }
                                                               @Override
                                                               public void chipDeselected(int index) {
                                                                   String selectedSizeID = allSizesID[index];
                                                                   sizeIDListForFilter.remove(selectedSizeID);

                                                                   for(int i=0;i<sizeIDSelectedIndex.size();i++){
                                                                       int x = sizeIDSelectedIndex.get(i);
                                                                       if(x == index){
                                                                           sizeIDSelectedIndex.remove(i);
                                                                       }
                                                                   }
                                                               }
                                                           })
                                                           .build();

                                                   for(int i=0;i<sizeIDSelectedIndex.size();i++){
                                                       sizeChip.setSelectedChip(sizeIDSelectedIndex.get(i));
                                                   }

                                                   colourChip = (ChipCloud) bottomSheetView.findViewById(R.id.filter_by_colour);
                                                   colourChip.setMode(ChipCloud.Mode.MULTI);

                                                   new ChipCloud.Configure()
                                                           .chipCloud(colourChip)
                                                           .selectedColor(Color.parseColor("#000000"))
                                                           .selectedFontColor(Color.parseColor("#ffffff"))
                                                           .deselectedColor(Color.parseColor("#e1e1e1"))
                                                           .deselectedFontColor(Color.parseColor("#333333"))
                                                           .labels(allColours)
                                                           .gravity(ChipCloud.Gravity.CENTER)
                                                           .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                                                           .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                                                           .chipListener(new ChipListener() {
                                                               @Override
                                                               public void chipSelected(int index) {

                                                                   String selectedColourID = allColoursID[index];

                                                                   if(!colourIDListForFilter.contains(selectedColourID)){
                                                                       colourIDListForFilter.add(selectedColourID);
                                                                   }
                                                                   if(!colourIDSelectedIndex.contains(index)){
                                                                       colourIDSelectedIndex.add(index);
                                                                   }
                                                               }
                                                               @Override
                                                               public void chipDeselected(int index) {
                                                                   String selectedColourID = allColoursID[index];
                                                                       colourIDListForFilter.remove(selectedColourID);

                                                                   for(int i=0;i<colourIDSelectedIndex.size();i++){
                                                                       int x = colourIDSelectedIndex.get(i);
                                                                       if(x == index){
                                                                           colourIDSelectedIndex.remove(i);
                                                                       }
                                                                   }
                                                               }
                                                           })
                                                           .build();

                                                   for(int i=0;i<colourIDSelectedIndex.size();i++){
                                                       colourChip.setSelectedChip(colourIDSelectedIndex.get(i));
                                                   }

                                                   bottomSheetDialog.setContentView(bottomSheetView);
                                                   bottomSheetDialog.show();

                                               }

                                           }
            );

            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            productListTable.setLayoutManager(mLayoutManager);

            scrollListener = new EndlessScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {

                    System.out.println("got filtered = " +isFiltered);

                    if (isFiltered) {
                            if(current_page < lastPageFilter)
                            {
                                getFilteredProductList(current_page);
                            }

                        }

                   else if (isSearch) {
                        if(current_page < lastPageSearch)
                        {
                            searchProduct(search,current_page);
                        }

                    }
                    else {
                            if(current_page < lastPage)
                            {
                                getProductList(current_page);
                            }
                        }
                }
            };

            productListTable.addOnScrollListener(scrollListener);

//            productListTable.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
//                @Override
//                public void onLoadMore(int current_page) {
//
//                    System.out.println("lalalla");
//
//                    if (isFiltered) {
//
//                            if(current_page < lastPageFilter)
//                            {
//                                getFilteredProductList(current_page);
//                            }
//                        } else {
//                            if(current_page < lastPage)
//                            {
//                                getProductList(current_page);
//                            }
//                        }
//                }
//            });

            mAdapter = new GridViewProductAdapter(getActivity(), mProductObj, this,SelectedCountryCurrency);
            productListTable.setAdapter(mAdapter);

            if(fromSearch.equals("Search")){
                ((MainActivity) getActivity()).changeToolBarText("Search ''" + search + "''");
                Insider.Instance.tagEvent("searched").addParameterWithString("keyword", search).build();
                isSearch = true;

                searchProduct(search,1); }

            else {
                getProductList(1);
                getSizeList();
                getColorsList();
                getCategoryList();
            }


            return _rootView;
        }
    }

    private void searchProduct(String keyword, int pages) {

        System.out.println("searchProduct  " + keyword);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");
        getTierLevel = pref.getString("popup_tier", "1");
        //
//        String action="Products/search/keyword/"+search+"/lang/1/shop/"+SelectedShopID+"/num_page/"+numberPages+"/sort_options/"+sortType+"?apikey="+apikey;
        String action = "Products/search/keyword/" + search + "/lang/1/shop/" + SelectedShopID + "/num_page/" + pages + "/sort_options/" + sortType + "?apikey=" + apikey + "&tier=" + getTierLevel;

//        if (firstLoad) {
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

            callws.execute(action);
            //            firstLoad = false;
//
//        } else {
//            if (!sortBy) {
//                WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
//                callws.execute(action);
//
//                numberPages++;
//            } else {
//                WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
//                callws.execute(action);
//
//                numberPages++;
//            }

//        }


    }

    private void getProductList(int pageNum) {

        if (UserID.length() > 0) {
            getSavedItems();
        }

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");
        //
        String action = "Products/category/id/" + catID + "/shop/" + SelectedShopID + "/num_page/" + pageNum + "/sort_options/" + sortType + "?api_version=apps&apikey=" + apikey + "&tier=" + pref.getString("tier_level","") + "&user_id=" + UserID;
//        if (firstLoad) {
//            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
//            callws.execute(action);
//
////            firstLoad = false;
//
//        } else {
//            if (!sortBy) {
//                WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
//                callws.execute(action);
//
//            } else {
                WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                callws.execute(action);

//            }
//
//        }


    }

    private void getFilteredProductList(int pagNum) {

        String colourFilter = "";
        for (int i = 0; i < colourIDListForFilter.size(); i++) {
            if (i == 0) {
                colourFilter += colourIDListForFilter.get(i);
            } else {
                colourFilter += "," + colourIDListForFilter.get(i);
            }

        }

        String sizeFilter = "";
        for (int i = 0; i < sizeIDListForFilter.size(); i++) {
            if (i == 0) {
                sizeFilter += sizeIDListForFilter.get(i);
            } else {
                sizeFilter += "," + sizeIDListForFilter.get(i);
            }

        }

        System.out.println("Filter Data 1 = " + colourFilter);
        System.out.println("Filter Data 2 = " + sizeFilter);
        System.out.println("Filter Data 3 = " + sizeIDSelectedIndex);
        System.out.println("Filter Data 4 = " + colourIDSelectedIndex);
        System.out.println("Filter Data 5 = " + sortType);
        System.out.println("Filter Data 6 = " + catIDFilter);


        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");
        String action;

        if(catIDFilter.equals(""))
        {
            action = "Products/filterCategoryProducts?apikey=" + apikey + "&shop=" + SelectedShopID + "&category=" + catID + "&color=" + colourFilter + "&product_attribute=" + sizeFilter + "&num_page=" + pagNum + "&sort_options=" + sortType;

        }
        else {
            action = "Products/filterCategoryProducts?apikey=" + apikey + "&shop=" + SelectedShopID + "&category=" + catIDFilter + "&color=" + colourFilter + "&product_attribute=" + sizeFilter + "&num_page=" + pagNum + "&sort_options=" + sortType;

        }

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    public void getSavedItems() {
        String action = "Wishlists/list/customer/" + UserID + "/lang/" + LanguageID + "/sort_options/" + sortType + "?apikey=" + apiKey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

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

    @Override
    public void onTaskComplete(JSONObject result) {

        if (result != null) {
            try {

                if (result.getBoolean("status")) {

                    System.out.println("list product result = " + result);

                    if (result.getString("action").equals("Wishlists_list")) {

                        if (result.getBoolean("status")) {

                            wishlistProdIdArray.clear();
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
                                    wishlistProdIdArray.add(productID);
                                }

                            } catch (Exception e) {

                            }

                        } else {
                            wishlistProdIdArray.clear();
                            wishListArray.clear();
                        }
                    }

                    else if(result.getString("action").equals("Products_category")){

                        JSONObject pagination = result.getJSONObject("pagination");
                        currentPage = pagination.getInt("current_page");
                        lastPage = pagination.getInt("last_page");

                        JSONArray productListArray = result.getJSONArray("data");

                        for (int j = 0; j < productListArray.length(); j++) {

                            JSONObject jObject = productListArray.getJSONObject(j);

                            String id_product = jObject.getString("id_product");
                            String name = jObject.getString("name");
                            String reference = jObject.getString("reference");
                            JSONArray image_url = jObject.getJSONArray("image_url");
                            String tax_rate = jObject.getString("tax_rate");
                            String price_with_tax = jObject.getString("price");
                            String reduction = jObject.getString("price_reduction");
                            String out_of_stock = jObject.getString("product_out_of_stock");
                            String total_colours = jObject.getString("total_colours");

                            String id_product_attribute =  jObject.getString("id_product_attribute");
                            String get_coll_name = jObject.getString("collection_name");
                            String get_online_exclusive = jObject.getString("online_exclusive");
                            String get_discount = jObject.getString("discount_label");
                            String get_Price_withOut_Reduction = jObject.getString("price_without_reduction");
                            String get_discount_text = jObject.getString("discount_text");

                            Boolean isWishlist;

                            JSONArray related_colour_data = null;
                            try {
                                related_colour_data = jObject.getJSONArray("related_colour_data");

                            } catch (Exception e) {

                                System.out.println("false");

                            }

                            ArrayList<attributeItem>sizeArray = new ArrayList<>();

                            JSONArray size_array = null;
                            try {
                                size_array = jObject.getJSONArray("attribute_list");

                                for (int i = 0; i < size_array.length(); i++) {
                                    JSONObject attributeObj = size_array.getJSONObject(i);
                                    String attID = attributeObj.getString("id_product_attribute");
                                    String attName = attributeObj.getString("attribute_name");
                                    int attQuantity = attributeObj.getInt("quantity");

                                    sizeArray.add(new attributeItem(attID,attName,attQuantity));
                                }

                            } catch (Exception e) {

                                System.out.println("false");

                            }

                            if(wishlistProdIdArray.contains(id_product))
                            {
                                isWishlist = true;
                            }

                            else {
                                isWishlist = false;
                            }

                            mProductObj.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, total_colours, id_product_attribute, related_colour_data, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text,sizeArray,isWishlist));
                        }

                        mAdapter.notifyDataSetChanged();

                        if(mProductObj.size() == 0){
                            noProductLbl.setVisibility(View.VISIBLE);
                        }
                        else {
                            noProductLbl.setVisibility(View.GONE);
                        }
                    }

                    else if (result.getString("action").equals("Products_filterCategoryProducts")) {

//                        productListTable.removeAllViewsInLayout();

                        JSONObject jsonObject = result.getJSONObject("data");
                        JSONArray jsonArr = null;
                        jsonArr = jsonObject.getJSONArray("products");

                        JSONObject pagination = jsonObject.getJSONObject("pagination");
                        currentPage = pagination.getInt("current_page");
                        lastPageFilter = pagination.getInt("last_page");

//                        JSONArray jsonArr = null;
//                        jsonArr = result.getJSONArray("products");

                        if(jsonArr == null || jsonArr.length() == 0){
                            noProductLbl.setVisibility(View.VISIBLE);
                            productListTable.setVisibility(View.GONE);
                        }

                        else {
//
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

                                    Boolean isWishlist;

                                    ArrayList<attributeItem>sizeArray = new ArrayList<>();

                                    JSONArray size_array = null;
                                    try {
                                        size_array = jsonArr.getJSONObject(j).getJSONArray("attribute_list");

                                        for (int i = 0; i < size_array.length(); i++) {
                                            JSONObject attributeObj = size_array.getJSONObject(i);
                                            String attID = attributeObj.getString("id_product_attribute");
                                            String attName = attributeObj.getString("attribute_name");
                                            int attQuantity = attributeObj.getInt("quantity");

                                            sizeArray.add(new attributeItem(attID,attName,attQuantity));
                                        }

                                    } catch (Exception e) {

                                        System.out.println("false");

                                    }

                                    if(wishlistProdIdArray.contains(id_product))
                                    {
                                        isWishlist = true;
                                    }

                                    else {
                                        isWishlist = false;
                                    }

                                    mProductObj.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, "", id_product_attribute, null, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text,sizeArray,isWishlist));

                                }



                            } catch (Exception e) {
                                noProductLbl.setVisibility(View.VISIBLE);
                                productListTable.setVisibility(View.GONE);
                            }
                            mAdapter.notifyDataSetChanged();
                            noProductLbl.setVisibility(View.GONE);
                            productListTable.setVisibility(View.VISIBLE);

//                        if(mProductObj.size() == 0){
//                            noProductLbl.setVisibility(View.VISIBLE);
//                        }
//                        else {
//                            noProductLbl.setVisibility(View.GONE);
//                        }
                        }
                    }

                    else if (result.getString("action").equals("Products_search")) {
                        System.out.println("hahaaaaaaac " + result);
                        JSONArray jsonArr = null;
                        boolean end = true;
                        JSONObject jsonObject = result.getJSONObject("data");
                        JSONObject pagination = jsonObject.getJSONObject("pagination");
                        String total_items = pagination.getString("total_items");
                        lastPageSearch = pagination.getInt("last_page");


                        if (total_items.equals("0")) {
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

                                    Boolean isWishlist;

                                    ArrayList<attributeItem>sizeArray = new ArrayList<>();

                                    JSONArray size_array = null;
                                    try {
                                        size_array = jsonArr.getJSONObject(j).getJSONArray("attribute_list");

                                        for (int i = 0; i < size_array.length(); i++) {
                                            JSONObject attributeObj = size_array.getJSONObject(i);
                                            String attID = attributeObj.getString("id_product_attribute");
                                            String attName = attributeObj.getString("attribute_name");
                                            int attQuantity = attributeObj.getInt("quantity");

                                            sizeArray.add(new attributeItem(attID,attName,attQuantity));
                                        }

                                    } catch (Exception e) {

                                        System.out.println("false");

                                    }

                                    if(wishlistProdIdArray.contains(id_product))
                                    {
                                        isWishlist = true;
                                    }

                                    else {
                                        isWishlist = false;
                                    }

                                    mProductObj.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, "", id_product_attribute, null, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text,sizeArray,isWishlist));

                                }

                            } catch (Exception e) {

                                noProductLbl.setVisibility(View.VISIBLE);
                                productListTable.setVisibility(View.GONE);
                                filterBottomButton.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            noProductLbl.setVisibility(View.VISIBLE);
                            productListTable.setVisibility(View.GONE);
                            filterBottomButton.setVisibility(View.GONE);
                        }

                        mAdapter.notifyDataSetChanged();
                        noProductLbl.setVisibility(View.GONE);
                        productListTable.setVisibility(View.VISIBLE);
                        filterBottomButton.setVisibility(View.GONE);

                        Insider.Instance.tagEvent("search_made").build();

                    }

                    else if (result.getString("action").equals("Wishlists_addproduct")) {

                        String message = result.getString("message");
                        if (message.equals("Successfully Added")) {

                            Toast toast = Toast.makeText(getActivity(),
                                    "Item Saved", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
                            popupWindow.dismiss();
                            getSavedItems();
//                            getProductList();
                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                            String totalList = pref.getString("wishlistItem", "0");

                            int totalquant = Integer.parseInt("");

                            totalquant += Integer.parseInt(totalList);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("wishlistItem", String.valueOf(totalquant));
                            editor.apply();
                            ((MainActivity) getActivity()).changeToolBarWishNotiText(String.valueOf(totalquant));
                        }
                    }

                   else if (result.getString("action").equals("Wishlists_removeProduct")) {


                        if (result.getBoolean("status")) {

                           getSavedItems();

                            Toast toast = Toast.makeText(getActivity(),
                                    "Item Removed", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                        }


                    }
                    else if(result.getString("action").equals("Products_filterType_size")){
                        if (result.getBoolean("status")) {

                            JSONArray jsonArr = new JSONArray();
                            jsonArr = result.getJSONArray("data");

                            allSizes = new String[jsonArr.length()];
                            allSizesID = new String[jsonArr.length()];

                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jObj = jsonArr.getJSONObject(i);

                                final String attributeID = jObj.getString("id_combination");
                                final String attributeName = jObj.getString("name");

                                allSizes[i]= attributeName;
                                allSizesID[i] = attributeID;

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
                                JSONObject jObj = jsonArr.getJSONObject(i);

                                final String colorID = jObj.getString("id_combination");
                                final String colorName = jObj.getString("name");

                                allColours[i] = colorName;
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
                                JSONObject jObj = jsonArr.getJSONObject(i);

                                final String catID = jObj.getString("id_category");
                                final String categoryName = jObj.getString("name");

                                String cat_Name = categoryName;
                                String cat_ID = catID;


                                category[i] = cat_Name;
                                categoryID[i] = cat_ID;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(int position) {

        if(isSearch == true){
            catName = search;
        }

        Fragment fragment = new ProductInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("prodID",mProductObj.get(position).getproductID());
        bundle.putString("catName", catName);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onWishlistClick(int position) {

        int itemIndex = position;

        String product_id;

        product_id = mProductObj.get(position).getproductID();

        if (UserID.length() > 0) {

            if(wishlistProdIdArray.contains(product_id))
            {
                for (int j = 0; j < wishListArray.size(); j++) {

                    final savedItemsItem item = wishListArray.get(j);

                    if (item.getproductID().equals(product_id)) {

                        mProductObj.set(itemIndex, new ProductListItem(mProductObj.get(itemIndex).getproductID(), mProductObj.get(itemIndex).getname(), mProductObj.get(itemIndex).getreference(), mProductObj.get(itemIndex).getimage_url(), mProductObj.get(itemIndex).gettax_rate(), mProductObj.get(itemIndex).getprice_with_tax(), mProductObj.get(itemIndex).getprice_with_discount(), mProductObj.get(itemIndex).getout_of_stock(), mProductObj.get(itemIndex).getTotal_colours(), mProductObj.get(itemIndex).getId_product_attribute(), mProductObj.get(itemIndex).getRelated_colour_data(), mProductObj.get(itemIndex).getCollectionName(), mProductObj.get(itemIndex).getOnline_exclusive(), mProductObj.get(itemIndex).getDiscount(), mProductObj.get(itemIndex).getPrice_Without_reduction(), mProductObj.get(itemIndex).getDiscount_text(), mProductObj.get(itemIndex).getSizeArray(), false));
                        mAdapter.notifyItemChanged(itemIndex);
                        removeFromSavedItems(item.getwishlistID(), item.getproductID(), item.getproductAttributeID());

                    } else {


                    }


                }
            }

            else {

                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_layout, null);

                popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                ImageButton btnDismiss = (ImageButton) popupView.findViewById(R.id.ib_close);
                btnDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                    }
                });
                ListView listView1 = (ListView) popupView.findViewById(R.id.listView1);

                List<String> listSize = new ArrayList<String>();
                ArrayList<attributeItem> sizee = mProductObj.get(position).getSizeArray();

                for (int i = 0; i < sizee.size(); i++) {

                    attributeItem siiis = sizee.get(i);
                    String sizeName = siiis.getattributeName();
                    listSize.add(sizeName);
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, listSize);

                listView1.setAdapter(adapter);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        attributeItem sizeArr = sizee.get(position);
                        String attribute_id = sizeArr.getattributeID();

                        mProductObj.set(itemIndex, new ProductListItem(mProductObj.get(itemIndex).getproductID(), mProductObj.get(itemIndex).getname(), mProductObj.get(itemIndex).getreference(), mProductObj.get(itemIndex).getimage_url(), mProductObj.get(itemIndex).gettax_rate(), mProductObj.get(itemIndex).getprice_with_tax(), mProductObj.get(itemIndex).getprice_with_discount(), mProductObj.get(itemIndex).getout_of_stock(), mProductObj.get(itemIndex).getTotal_colours(), mProductObj.get(itemIndex).getId_product_attribute(), mProductObj.get(itemIndex).getRelated_colour_data(), mProductObj.get(itemIndex).getCollectionName(), mProductObj.get(itemIndex).getOnline_exclusive(), mProductObj.get(itemIndex).getDiscount(), mProductObj.get(itemIndex).getPrice_Without_reduction(), mProductObj.get(itemIndex).getDiscount_text(), mProductObj.get(itemIndex).getSizeArray(), true));
                        mAdapter.notifyItemChanged(itemIndex);
                        saveForLater(product_id, attribute_id);

                    }
                });
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            }
        }

        else {
            Toast toast = Toast.makeText(getActivity(),
                    "Log in required for saving item.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();
        }
    }

    private void saveForLater(String prodID, String prodIDAtt) {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
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
            WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
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
            WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
            callws.setAction(action);
            callws.execute(formBody);
        }
    }

    private void removeFromSavedItems(String wishlistID, String productID, String productAttributeID) {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

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
        WebServiceAccessDelete callws = new WebServiceAccessDelete(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);

        System.out.println(" masukkan " + wishlistID + " sini " + productID + " dan sini " + productAttributeID + " sini gak " + apikey + " sini sekali " + UserID);


    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//        params.setScrollFlags(0);
//
//        if(popupWindow != null){
//            popupWindow.dismiss();
//        }
//    }
}
