package com.tiseno.poplook;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.savedItemsAdapter;
import com.tiseno.poplook.functions.savedItemsItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedItemsFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{
    String UserID, CartID, LanguageID;
    String bagNotiItem;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    ArrayList<savedItemsItem> listArray = new ArrayList<savedItemsItem>();

    NumberPicker savedItemSortByNP;
    ImageView savedItemSortByDropDownIV;
    RelativeLayout savedItemSortByRL,emptyRL;
    TextView savedItemSortByTV,productListSortByTV1,textView1,textViewSaja;
    ImageButton savedItemSortBySelectIB;
    String sortType="0";


    public SavedItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_saved_items, container, false);
        TextView step1TV = (TextView)contentView.findViewById(R.id.step1TV);
        TextView step2TV = (TextView)contentView.findViewById(R.id.step2TV);

        step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        ((MainActivity) getActivity()).changeToolBarText("Saved Items");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);
//        ((MainActivity) getActivity()).hideSideMenu(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "0");
        LanguageID = pref.getString("LanguageID", "");
        bagNotiItem = pref.getString("cartItem", "");

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.savedItemsRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        emptyRL = (RelativeLayout) contentView.findViewById(R.id.emptyRL);
        textViewSaja = (TextView) contentView.findViewById(R.id.textViewSaja);
        savedItemSortByRL = (RelativeLayout) contentView.findViewById(R.id.savedItemSortByRL);
        savedItemSortByNP = (NumberPicker) contentView.findViewById(R.id.savedItemSortByNP);
        savedItemSortBySelectIB = (ImageButton) contentView.findViewById(R.id.savedItemSortBySelectIB);

        savedItemSortByNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        final String sortbyarray[] = { "View all", "Price: Lowest First", "Price: Highest First" };

        savedItemSortByNP.setMinValue(0);
        savedItemSortByNP.setMaxValue(sortbyarray.length - 1);
        savedItemSortByNP.setDisplayedValues(sortbyarray);
        setDividerColor(savedItemSortByNP);

        savedItemSortByDropDownIV = (ImageView)contentView.findViewById(R.id.savedItemSortByDropDownIV);
        textView1 = (TextView)contentView.findViewById(R.id.textView1);
        productListSortByTV1 = (TextView)contentView.findViewById(R.id.productListSortByTV1);
        savedItemSortByTV = (TextView)contentView.findViewById(R.id.savedItemSortByTV);

        textView1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        productListSortByTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        savedItemSortByTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        savedItemSortByDropDownIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedItemSortByRL.setVisibility(View.VISIBLE);
                savedItemSortByDropDownIV.setImageResource(R.drawable.btn_dropdown_active);
            }
        });

        savedItemSortByTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedItemSortByRL.setVisibility(View.VISIBLE);
                savedItemSortByDropDownIV.setImageResource(R.drawable.btn_dropdown_active);
            }
        });
        savedItemSortBySelectIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedItemSortByRL.setVisibility(View.GONE);
                savedItemSortByDropDownIV.setImageResource(R.drawable.btn_dropdown);

                if (savedItemSortByNP.getValue() > 0) {
                    savedItemSortByTV.setText(sortbyarray[savedItemSortByNP.getValue()]);
                } else {
                    savedItemSortByTV.setText("View all");
                }
                if (sortbyarray[savedItemSortByNP.getValue()].equals("View all")) {
                    sortType = "0";
                } else if (sortbyarray[savedItemSortByNP.getValue()].equals("Price: Lowest First")) {
                    sortType = "1";
                } else if (sortbyarray[savedItemSortByNP.getValue()].equals("Price: Highest First")) {
                    sortType = "2";
                } else if (sortbyarray[savedItemSortByNP.getValue()].equals("Product Name: A to Z")) {
                    sortType = "3";
                } else if (sortbyarray[savedItemSortByNP.getValue()].equals("Product Name: Z to A")) {
                    sortType = "4";
                }else if (sortbyarray[savedItemSortByNP.getValue()].equals("Expired Soon")) {
                    sortType = "5";
                }else if (sortbyarray[savedItemSortByNP.getValue()].equals("In Stock")) {
                    sortType = "6";
                }
                getSavedItems();
            }
        });

        getSavedItems();

        return contentView;
    }

    public void getSavedItems()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Wishlists/list/customer/"+UserID+"/lang/"+LanguageID+"/sort_options/"+sortType+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                if(result.getString("action").equals("Wishlists_list"))
                {
                    if(result.getBoolean("status")) {

                        listArray.clear();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject = result.getJSONObject("data");
                        String wishlistID = jsonObject.getString("id_wishlist");

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("WishlistID", wishlistID);
                        editor.apply();

                        JSONArray jsonArr = new JSONArray();
                        jsonArr = jsonObject.getJSONArray("product_list");
                        try{
                            if(jsonArr.length()==0){
                                emptyRL.setVisibility(View.VISIBLE);
                                textViewSaja.setText("Your saved list is empty");
                                textViewSaja.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

                            }else{
                                emptyRL.setVisibility(View.GONE);
                            }
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

                                listArray.add(new savedItemsItem(productID, productName, productSize, productPrice, productImage, productAttributeID, wishlistID, String.valueOf(productAvailableQuantity), productExpires));
                            }

                            RVAdapter = new savedItemsAdapter(getActivity(),SavedItemsFragment.this, listArray);
                            RVAdapter = new savedItemsAdapter(getActivity(),SavedItemsFragment.this, listArray);

                            // Inflate footer view
                            LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                            mRecyclerView.setAdapter(RVAdapter);

                        }catch (Exception e){
                            emptyRL.setVisibility(View.VISIBLE);
                            textViewSaja.setText("Your saved list is empty");
                            textViewSaja.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                        }
                    }else{
                        listArray.clear();
                        emptyRL.setVisibility(View.VISIBLE);
                        textViewSaja.setText("Your saved list is empty");
                        textViewSaja.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    }
                }

            }
            catch (Exception e){
                emptyRL.setVisibility(View.VISIBLE);
                textViewSaja.setText("Your saved list is empty");
                textViewSaja.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
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

    private void setDividerColor (NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //pf.set(picker, getResources().getColor(R.color.my_orange));
                    //Log.v(TAG,"here");
                    pf.set(picker, getResources().getDrawable(R.drawable.divider_1));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        //}
    }

}
