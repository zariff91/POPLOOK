package com.tiseno.poplook;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.attributeItem;
import com.tiseno.poplook.functions.loyaltyPointsOrderItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FilterFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{
    boolean atSizes;
    String sizesSelected="",colourSelected="";
    TextView colorTV,sizeTV,sizeFilteredTV,colorFilteredTV,colorFilteredTV1,sizeFilteredTV1;
    LinearLayout linearLayoutColor,linearLayoutSize,linearLayoutFilter,linearLayoutFiltered,linearLayoutBtnColorSize,linearLayoutBtnFilter,linearLayoutBtnFiltered,ColorCheckBoxLL;
    Button btn_close_filter,btn_clear_size_colour,btn_done_size_colour,btn_clear_filtered,btn_apply_filtered;
    FrameLayout flfilter;
    CheckBox BlueCheckBox,BlackCheckBox,PurpleCheckBox,GreenCheckBox,PinkCheckBox,GreyCheckBox,BrownCheckBox, RedCheckBox, NavyCheckBox,BeigeCheckBox;
    String fromHome,catID,catName;

    LinearLayout SizeCheckBoxLL;

    int NoOfCBSizes = 0, NoOfCBColor = 0;

    ArrayList<String> listArray_attribute = new ArrayList<String>();
    List<String> listArray_attributeName = new ArrayList<String>();
    ArrayList<String> listArray_colour = new ArrayList<String>();
    List<String> listArray_colorName = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_filter, container, false);
        Bundle bundle = this.getArguments();
        fromHome = bundle.getString("fromHome");
        catID = bundle.getString("prodID");
        catName=bundle.getString("catName");
        listArray_attribute = getArguments().getStringArrayList("sizeList");
        listArray_colour = getArguments().getStringArrayList("colourList");
        sizesSelected = bundle.getString("sizes");
        colourSelected = bundle.getString("colour");
        ((MainActivity) getActivity()).changeToolBarText("Filter By");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(true);
        ((MainActivity) getActivity()).setDrawerState(false);




        colorFilteredTV = (TextView)view.findViewById(R.id.filteredColourTV);
        sizeFilteredTV = (TextView)view.findViewById(R.id.filteredSizeTV);
        sizeTV = (TextView)view.findViewById(R.id.SizeTV);
        colorTV = (TextView)view.findViewById(R.id.ColourTV);
        colorFilteredTV1 = (TextView)view.findViewById(R.id.colorFilteredTV);
        sizeFilteredTV1 = (TextView)view.findViewById(R.id.sizeFilteredTV);

        flfilter = (FrameLayout)view.findViewById(R.id.flfilter);

        linearLayoutColor = (LinearLayout)view.findViewById(R.id.linear_layout_colour);
        linearLayoutSize = (LinearLayout)view.findViewById(R.id.linear_layout_size);
        linearLayoutFilter = (LinearLayout)view.findViewById(R.id.linear_layout);
        linearLayoutFiltered = (LinearLayout)view.findViewById(R.id.linear_layout_filtered);

        linearLayoutBtnColorSize = (LinearLayout)view.findViewById(R.id.linear_layout_btn_color_size);
        linearLayoutBtnFilter = (LinearLayout)view.findViewById(R.id.linear_layout_btn);
        linearLayoutBtnFiltered = (LinearLayout)view.findViewById(R.id.linear_layout_btn_filtered);

        btn_close_filter = (Button)view.findViewById(R.id.btn_close);
        btn_clear_size_colour = (Button)view.findViewById(R.id.btn_clear_size_colour);
        btn_done_size_colour = (Button)view.findViewById(R.id.btn_done_size_colour);
        btn_clear_filtered = (Button)view.findViewById(R.id.btn_clear_filter);
        btn_apply_filtered = (Button)view.findViewById(R.id.btn_apply_filter);

//        BlueCheckBox = (CheckBox)view.findViewById(R.id.BlueCheckBox);
//        BlackCheckBox = (CheckBox)view.findViewById(R.id.BlackCheckBox);
//        PurpleCheckBox = (CheckBox)view.findViewById(R.id.PurpleCheckBox);
//        GreenCheckBox = (CheckBox)view.findViewById(R.id.GreenCheckBox);
//        PinkCheckBox = (CheckBox)view.findViewById(R.id.PinkCheckBox);
//        GreyCheckBox = (CheckBox)view.findViewById(R.id.GreyCheckBox);
//        BrownCheckBox = (CheckBox)view.findViewById(R.id.BrownCheckBox);
//        RedCheckBox = (CheckBox)view.findViewById(R.id.RedCheckBox);
//        NavyCheckBox = (CheckBox)view.findViewById(R.id.NavyCheckBox);
//        BeigeCheckBox = (CheckBox)view.findViewById(R.id.BeigeCheckBox);
//        XSCheckBox = (CheckBox)view.findViewById(R.id.XSCheckBox);
//        SCheckBox = (CheckBox)view.findViewById(R.id.SCheckBox);
//        MCheckBox = (CheckBox)view.findViewById(R.id.MCheckBox);
//        LCheckBox = (CheckBox)view.findViewById(R.id.LCheckBox);
//        XLCheckBox = (CheckBox)view.findViewById(R.id.XLCheckBox);
//        DXLCheckBox = (CheckBox)view.findViewById(R.id.DXLCheckBox);
//        TXLCheckBox = (CheckBox)view.findViewById(R.id.TXLCheckBox);
//        FXLCheckBox = (CheckBox)view.findViewById(R.id.FXLCheckBox);
//        OneYearCheckBox = (CheckBox)view.findViewById(R.id.OneYearCheckBox);
//        TwoYearCheckBox = (CheckBox)view.findViewById(R.id.TwoYearCheckBox);

        SizeCheckBoxLL = (LinearLayout)view.findViewById(R.id.SizeCheckBoxLL);
        ColorCheckBoxLL = (LinearLayout)view.findViewById(R.id.ColorCheckBoxLL);


        colorFilteredTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        sizeFilteredTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        sizeTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        colorTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        colorFilteredTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        sizeFilteredTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        btn_close_filter.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        btn_clear_size_colour.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        btn_done_size_colour.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        btn_clear_filtered.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        btn_apply_filtered.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

//        BlueCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        BlackCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        PurpleCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        GreenCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        PinkCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        GreyCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        BrownCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        RedCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        NavyCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        BeigeCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        XSCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        SCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        MCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        LCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        XLCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        DXLCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        TXLCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        FXLCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        OneYearCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        TwoYearCheckBox.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


if(!sizesSelected.equals("")||!colourSelected.equals("")){
    colorFilteredTV.setText(colourSelected);
    linearLayoutFilter.setVisibility(View.GONE);
    linearLayoutBtnFilter.setVisibility(View.GONE);
    colorFilteredTV.setVisibility(View.VISIBLE);
    ((MainActivity) getActivity()).changeToolBarText("Filter By");
    linearLayoutFiltered.setVisibility(View.VISIBLE);
    linearLayoutBtnFiltered.setVisibility(View.VISIBLE);
    sizeFilteredTV.setVisibility(View.VISIBLE);
    sizeFilteredTV.setText(sizesSelected);

}


//        getSizeList();
//        getColorsList();





        sizeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atSizes = true;
                ((MainActivity) getActivity()).changeBtnBackView(true);
                linearLayoutSize.setVisibility(View.VISIBLE);
                linearLayoutBtnColorSize.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).changeToolBarText("Filter By: Sizes");
                ((MainActivity) getActivity()).backBtnControl("CHANGE");
                linearLayoutFilter.setVisibility(View.GONE);
                linearLayoutBtnFilter.setVisibility(View.GONE);
                getSizeList();

            }

        });

        colorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atSizes=false;
                ((MainActivity) getActivity()).changeBtnBackView(true);
                linearLayoutColor.setVisibility(View.VISIBLE);
                linearLayoutBtnColorSize.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).changeToolBarText("Filter By: Colours");
                ((MainActivity) getActivity()).backBtnControl("CHANGE");
                linearLayoutFilter.setVisibility(View.GONE);
                linearLayoutBtnFilter.setVisibility(View.GONE);
                getColorsList();
            }

        });

        sizeFilteredTV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atSizes=true;
                ((MainActivity) getActivity()).changeBtnBackView(true);
                linearLayoutSize.setVisibility(View.VISIBLE);
                linearLayoutBtnColorSize.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).changeToolBarText("Filter By: Sizes");
                ((MainActivity) getActivity()).backBtnControl("CHANGE");
                linearLayoutFiltered.setVisibility(View.GONE);
                linearLayoutBtnFiltered.setVisibility(View.GONE);
                getSizeList();


            }

        });

        colorFilteredTV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atSizes=false;
                ((MainActivity) getActivity()).changeBtnBackView(true);
                linearLayoutColor.setVisibility(View.VISIBLE);
                linearLayoutBtnColorSize.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).changeToolBarText("Filter By: Colours");
                ((MainActivity) getActivity()).backBtnControl("CHANGE");
                linearLayoutFiltered.setVisibility(View.GONE);
                linearLayoutBtnFiltered.setVisibility(View.GONE);
                getColorsList();
            }

        });

        btn_clear_size_colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!atSizes){
                    colourSelected=null;
                    System.out.println("listArray_attribute size is "+listArray_colour.size());
                    for(int i = 0; i < NoOfCBColor; i++)
                    {
                        CheckBox cb = (CheckBox)(view.findViewWithTag(i));
                        cb.setChecked(false);

                    }

//                    if (BlueCheckBox.isChecked()) {
//                        BlueCheckBox.setChecked(false);
//                    }
//                    if (BlackCheckBox.isChecked()) {
//                        BlackCheckBox.setChecked(false);
//                    }
//                    if (PurpleCheckBox.isChecked()) {
//                        PurpleCheckBox.setChecked(false);
//                    }
//                    if (GreenCheckBox.isChecked()) {
//                        GreenCheckBox.setChecked(false);
//                    }
//                    if (PinkCheckBox.isChecked()) {
//                        PinkCheckBox.setChecked(false);
//                    }
//                    if (GreyCheckBox.isChecked()) {
//                        GreyCheckBox.setChecked(false);
//                    }
//                    if (BrownCheckBox.isChecked()) {
//                        BrownCheckBox.setChecked(false);
//                    }
//                    if (RedCheckBox.isChecked()) {
//                        RedCheckBox.setChecked(false);
//                    }
//                    if (NavyCheckBox.isChecked()) {
//                        NavyCheckBox.setChecked(false);
//                    }
//                    if (BeigeCheckBox.isChecked()) {
//                        BeigeCheckBox.setChecked(false);
//                    }
                }
                else{
                    sizesSelected=null;
                    System.out.println("listArray_attribute size is "+listArray_attribute.size());
                    for(int i = 0; i < NoOfCBSizes; i++)
                    {
                        CheckBox cb = (CheckBox)(view.findViewWithTag(i));
                        cb.setChecked(false);

                    }

//                   if (XSCheckBox.isChecked()) {
//                       XSCheckBox.setChecked(false);
//                   }
//                   if (SCheckBox.isChecked()) {
//                       SCheckBox.setChecked(false);
//                   }
//                   if (MCheckBox.isChecked()) {
//                       MCheckBox.setChecked(false);
//                   }
//                   if (LCheckBox.isChecked()) {
//                       LCheckBox.setChecked(false);
//                   }
//                   if (XLCheckBox.isChecked()) {
//                       XLCheckBox.setChecked(false);
//                   }
//                   if (DXLCheckBox.isChecked()) {
//                       DXLCheckBox.setChecked(false);
//                   }
//                   if (TXLCheckBox.isChecked()) {
//                       TXLCheckBox.setChecked(false);
//                   }
//                   if (FXLCheckBox.isChecked()) {
//                       FXLCheckBox.setChecked(false);
//                   }
//                   if (OneYearCheckBox.isChecked()) {
//                       OneYearCheckBox.setChecked(false);
//                   }
//                   if (TwoYearCheckBox.isChecked()) {
//                       TwoYearCheckBox.setChecked(false);
//                   }
                }


            }

        });

        btn_done_size_colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!atSizes){

                    colourSelected = "";

                    for(int i = 0; i < listArray_colorName.size(); i++)
                    {
                        if(i == 0)
                        {
                            colourSelected += listArray_colorName.get(i);
                        }
                        else
                        {
                            colourSelected += ", " + listArray_colorName.get(i);
                        }

                    }

//                    if (BlueCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Blue"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Blue";
//                        }else
//                            colourSelected = colourSelected + ", Blue";
//                    }
//                    else {
//                        listArray_colour.remove("{\"color\":\"" + "Blue" + "\"}");
//                    }
//
//
//                    if (BlackCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Black"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Black";
//                        }else
//                            colourSelected = colourSelected + ", Black";
//                    }
//                    else {
//                        listArray_colour.remove("{\"color\":\"" + "Black" + "\"}");
//                    }
//
//
//                    if (PurpleCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Purple"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Purple";
//                        }else
//                            colourSelected = colourSelected + ", Purple";
//                    }
//                    else{
//                        listArray_colour.remove("{\"color\":\"" + "Purple" + "\"}");
//                    }
//
//
//                    if (GreenCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Green"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Green";
//                        }else
//                            colourSelected = colourSelected + ", Green";
//                    }
//                    else{
//                        listArray_colour.remove("{\"color\":\"" + "Green" + "\"}");
//                    }
//
//
//                    if (PinkCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Pink"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Pink";
//                        }else
//                            colourSelected = colourSelected + ", Pink";
//                    }
//                    else{
//
//                        listArray_colour.remove("{\"color\":\"" + "Pink" + "\"}");
//
//                    }
//
//
//                    if (GreyCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Grey"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Grey";
//                        }else
//                            colourSelected = colourSelected + ", Grey";
//                    }
//                    else{
//                        listArray_colour.remove("{\"color\":\"" + "Grey" + "\"}");
//
//                    }
//
//
//
//                    if (BrownCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Brown"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Brown";
//                        }else
//                            colourSelected = colourSelected + ", Brown";
//                    }
//                    else{
//                        listArray_colour.remove("{\"color\":\"" + "Brown" + "\"}");
//                    }
//
//
//                    if (RedCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Red"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Red";
//                        }else
//                            colourSelected = colourSelected + ", Red";
//                    }
//                    else{
//                        listArray_colour.remove("{\"color\":\"" + "Red" + "\"}");
//                    }
//
//
//
//                    if (NavyCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Navy"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Navy";
//                        }else
//                            colourSelected = colourSelected + ", Navy";
//                    }
//                    else{
//                        listArray_colour.remove("{\"color\":\"" + "Navy" + "\"}");
//                    }
//
//
//                    if (BeigeCheckBox.isChecked()) {
//
//                        listArray_colour.add("{\"color\":\""+"Beige"+"\"}");
//
//                        if (colourSelected.equals("")){
//                            colourSelected="Beige";
//                        }else
//                            colourSelected = colourSelected + ", Beige";
//                    }
//                    else{
//                        listArray_colour.remove("{\"color\":\""+"Beige"+"\"}");
//
//                    }

                    colorFilteredTV.setText(colourSelected);
                    linearLayoutColor.setVisibility(View.GONE);
                    linearLayoutBtnColorSize.setVisibility(View.GONE);
                    colorFilteredTV.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).changeToolBarText("Filter By");
                    linearLayoutFiltered.setVisibility(View.VISIBLE);
                    linearLayoutBtnFiltered.setVisibility(View.VISIBLE);
                    System.out.println("selected color is "+listArray_colour);
                }
                else{

                    sizesSelected = "";

                    for(int i = 0; i < listArray_attributeName.size(); i++)
                    {
                        if(i == 0)
                        {
                            sizesSelected += listArray_attributeName.get(i);
                        }
                        else
                        {
                            sizesSelected += ", " + listArray_attributeName.get(i);
                        }

                    }


//                    if (XSCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="XS";
//                        }else
//                        sizesSelected = sizesSelected + ", XS";
//                    }
//                    if (SCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="S";
//                        }else
//                        sizesSelected = sizesSelected + ", S";
//                    }
//                    if (MCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="M";
//                        }else
//                        sizesSelected = sizesSelected + ", M";
//                    }
//                    if (LCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="L";
//                        }else
//                        sizesSelected = sizesSelected + ", L";
//                    }
//                    if (XLCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="XL";
//                        }else
//                        sizesSelected = sizesSelected + ", XL";
//                    }
//                    if (DXLCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="2XL";
//                        }else
//                        sizesSelected = sizesSelected + ", 2XL";
//                    }
//                    if (TXLCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="3XL";
//                        }else
//                        sizesSelected = sizesSelected + ", 3XL";
//                    }
//                    if (FXLCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="4XL";
//                        }else
//                        sizesSelected = sizesSelected + ", 4XL";
//                    }
//                    if (OneYearCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="1 YEAR";
//                        }else
//                        sizesSelected = sizesSelected + ", 1 YEAR";
//                    }
//                    if (TwoYearCheckBox.isChecked()) {
//                        if (sizesSelected.equals("")){
//                            sizesSelected="2 YEAR";
//                        }else
//                        sizesSelected = sizesSelected + ", 2 YEAR";
//
//                    }
                    sizeFilteredTV.setText(sizesSelected);
                    linearLayoutSize.setVisibility(View.GONE);
                    linearLayoutBtnColorSize.setVisibility(View.GONE);
                    sizeFilteredTV.setVisibility(View.VISIBLE);
                    linearLayoutFiltered.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).changeToolBarText("Filter By");
                    linearLayoutBtnFiltered.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).changeBtnBackView(false);
                }



            }

        });



        btn_apply_filtered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromHome", fromHome);
                bundle.putString("prodID", catID);
                bundle.putString("catName", catName);
                if (sizesSelected.equals("")&&colourSelected.equals("")){
                    bundle.putBoolean("filtered", false);
                }else {
                    bundle.putBoolean("filtered", true);
                }
                bundle.putStringArrayList("sizeList", listArray_attribute);
                bundle.putStringArrayList("colourList", listArray_colour);
                bundle.putString("sizes", sizesSelected);
                bundle.putString("colour",colourSelected);
                bundle.putInt("number",1);
                System.out.println("size selected is " + sizesSelected);
                System.out.println("colour selected is " + colourSelected);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FilterFragment filterFragment = (FilterFragment)fragmentManager.findFragmentByTag("FilterFragment");

                if (filterFragment != null && filterFragment.isVisible()) {
                    // add your code here
                    FragmentTransaction trans1 = fragmentManager.beginTransaction();
                    trans1.remove(filterFragment);
                    trans1.commit();
                    fragmentManager.popBackStack();
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductListFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }

        });


        btn_clear_filtered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                colorFilteredTV.setText(null);
                sizeFilteredTV.setText(null);
                colourSelected="";
                sizesSelected="";
                sizeFilteredTV.setVisibility(View.GONE);
                colorFilteredTV.setVisibility(View.GONE);
                linearLayoutFiltered.setVisibility(View.VISIBLE);
                linearLayoutBtnFiltered.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).changeToolBarText("Filter By");
                ((MainActivity) getActivity()).changeBtnBackView(false);
//                linearLayoutBtnFilter.setVisibility(View.VISIBLE);
//                linearLayoutFilter.setVisibility(View.VISIBLE);

            }

        });



        btn_close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }

        });


        return view;
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    private void getSizeList()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Products/filterType/name/size?apikey="+apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);
    }

    private void getColorsList()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Products/filterType/name/color?apikey="+apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{
                String action = result.getString("action");

                if(action.equals("Products_filterType_size"))
                {
                    if(result.getBoolean("status"))
                    {   listArray_attribute.clear();
                        listArray_attributeName.clear();

                        JSONArray jsonArr = new JSONArray();
                        jsonArr = result.getJSONArray("data");
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            NoOfCBSizes++;
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            final String attributeID = jObj.getString("id_combination");
                            final String attributeName = jObj.getString("name");

                            CheckBox cb = new CheckBox(getActivity());
                            cb.setText(" " + attributeName);
                            cb.setTag(i);
                            cb.setButtonDrawable(R.drawable.checkbox_style);
                            cb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.bottomMargin = dpToPx(15);
                            cb.setChecked(false);

                            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {

                                        String attribute = attributeID;

                                        System.out.println("Checked "+attributeName);
                                        listArray_attribute.add(attribute);
                                        listArray_attributeName.add(attributeName);
                                    } else {

                                        String attribute = attributeID;

                                        System.out.println("Unchecked "+attributeName);
                                        listArray_attribute.remove(attribute);
                                        listArray_attributeName.remove(attributeName);

                                    }

                                    System.out.println("list size is "+listArray_attribute);
                                }
                            });

                            SizeCheckBoxLL.addView(cb,params);
                        }
                    }
                }
                else
                if(action.equals("Products_filterType_color"))
                {
                    if(result.getBoolean("status"))
                    {   listArray_colour.clear();
                        listArray_colorName.clear();
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = result.getJSONArray("data");
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            NoOfCBColor++;
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            final String colorID = jObj.getString("id_combination");
                            final String colorName = jObj.getString("name");

                            CheckBox cb = new CheckBox(getActivity());
                            cb.setText(" " + colorName);
                            cb.setTag(i);
                            cb.setButtonDrawable(R.drawable.checkbox_style);
                            cb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.bottomMargin = dpToPx(15);
                            cb.setChecked(false);

                            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {

                                        String color = colorID;

                                        System.out.println("Checked "+colorName);
                                        listArray_colour.add(color);
                                        listArray_colorName.add(colorName);
                                    } else {

                                        String color = colorID;

                                        System.out.println("Unchecked "+colorName);
                                        listArray_colour.remove(color);
                                        listArray_colorName.remove(colorName);

                                    }

                                    System.out.println("list size is "+listArray_colour);
                                }
                            });

                            ColorCheckBoxLL.addView(cb,params);
                        }
                    }
                }
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

    public void backBtn(){
        if(!atSizes){
            if(colourSelected.length()!=0){
                colorFilteredTV.setText(colourSelected);
                colorFilteredTV.setVisibility(View.VISIBLE);
            }else{
                colorFilteredTV.setVisibility(View.GONE);
            }
            linearLayoutColor.setVisibility(View.GONE);
            linearLayoutBtnColorSize.setVisibility(View.GONE);

            ((MainActivity) getActivity()).changeToolBarText("Filter By");
            linearLayoutFiltered.setVisibility(View.VISIBLE);
            linearLayoutBtnFiltered.setVisibility(View.VISIBLE);
            System.out.println("selected color is "+listArray_colour);
        }
        else{
            if(sizesSelected.length()!=0) {
                sizeFilteredTV.setText(sizesSelected);
                sizeFilteredTV.setVisibility(View.VISIBLE);
            }else {
                sizeFilteredTV.setVisibility(View.GONE);

            }
            linearLayoutSize.setVisibility(View.GONE);
            linearLayoutBtnColorSize.setVisibility(View.GONE);
            linearLayoutFiltered.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).changeToolBarText("Filter By");
            linearLayoutBtnFiltered.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).changeBtnBackView(false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).backBtnControl("");
    }
}
