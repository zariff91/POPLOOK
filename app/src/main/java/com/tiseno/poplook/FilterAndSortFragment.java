package com.tiseno.poplook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FilterAndSortFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {


    ArrayList<Integer> sizeSelectedArray = new ArrayList<>();
    ArrayList<Integer> colorSelectedArray = new ArrayList<>();

    ArrayList<String> forApiCallSizeSelected = new ArrayList<>();
    ArrayList<String> forApiCallColourSelected = new ArrayList<>();


    String[] allSizes;
    String[] allSizesID;
    String[] allColours;
    String[] allColoursID;

    String sortType;


    boolean[] checkedItemsSize;
    boolean[] checkedItemsColour;

    NumberPicker sortBy;

    boolean fromFilter = false;

    Button clearBtn, closeBtn, applyBtn, btnSelectSort;

    ChipCloud chipCloud;
    ChipCloud chipCloudColor;


    TextView filterSortTV, sortByTV,selectSortInsideTV, filterTV, select_size_tv, select_colour_tv;

    RelativeLayout sortRV;

    String fromHome,catID,catName;

    int NoOfCBSizes = 0, NoOfCBColor = 0;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        fromFilter = bundle.getBoolean("isFiltered");

        fromHome = bundle.getString("fromHome");
        catID = bundle.getString("prodID");
        catName=bundle.getString("catName");

        ////////Get selected size and color array/////

        sizeSelectedArray = bundle.getIntegerArrayList("sizeList_Selected");
        colorSelectedArray = bundle.getIntegerArrayList("colourList_Selected");



        forApiCallColourSelected = bundle.getStringArrayList("colourList");
        forApiCallSizeSelected = bundle.getStringArrayList("sizeList");

        sortType = bundle.getString("sortType", "0");


        ////////Get selected size and color array end/////


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.filterandsort_layout, container, false);

        clearBtn = (Button) view.findViewById(R.id.clearAllBtn);
        closeBtn = (Button) view.findViewById(R.id.closeSearchBtn);
        applyBtn = (Button) view.findViewById(R.id.applySortFilterBtn);

        filterSortTV = (TextView) view.findViewById(R.id.textViewFilterSort);
        sortByTV = (TextView) view.findViewById(R.id.sortbyTV);
        filterTV = (TextView) view.findViewById(R.id.filterBelowTV);
        select_size_tv = (TextView) view.findViewById(R.id.selectSizeTV);
        select_colour_tv = (TextView) view.findViewById(R.id.selectColourTV);

        clearBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        closeBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        applyBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        filterSortTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        sortByTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        selectSortInsideTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        filterTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        select_size_tv.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        select_colour_tv.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        sortRV  = (RelativeLayout) view.findViewById(R.id.RL2);

        getSizeList();
        getColorsList();


        final AlertDialog.Builder sortByPopup = new AlertDialog.Builder(getActivity());

        View levelView = inflater.inflate(R.layout.sort_by_picker, null);

        sortBy = (NumberPicker)levelView.findViewById(R.id.sortByList);
        btnSelectSort = (Button)levelView.findViewById(R.id.selectSortBtn);
        btnSelectSort.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        sortByPopup.setView(levelView);

        final ChipCloud chipSort = (ChipCloud) view.findViewById(R.id.chipSortCloud);
        chipSort.setMode(ChipCloud.Mode.SINGLE);

        if(catName.equals("What's New"))
        {

            sortBy.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            sortBy.setMaxValue(2);
            sortBy.setDisplayedValues(new String[]{"Newest", "Price highest to lowest", "Price lowest to highest"});
            sortBy.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    // TODO Auto-generated method stub

                    String filtering = selectSortInsideTV.getText().toString();

                    if (filtering.equals("Newest")) {
                        sortType = "0";
                    } else if (filtering.equals("Price lowest to highest")) {
                        sortType = "1";
                    } else if (filtering.equals("Price highest to lowest")) {
                        sortType = "2";
                    }

                }
            });

            String[] sortText = new String[3];
            sortText[0] ="Newest";
            sortText[1] ="Price lowest to highest";
            sortText[2] ="Price highest to lowest";

            new ChipCloud.Configure()
                    .chipCloud(chipSort)
                    .selectedColor(Color.parseColor("#000000"))
                    .selectedFontColor(Color.parseColor("#ffffff"))
                    .deselectedColor(Color.parseColor("#e1e1e1"))
                    .deselectedFontColor(Color.parseColor("#333333"))
                    .labels(sortText)
                    .gravity(ChipCloud.Gravity.CENTER)
                    .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                    .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                    .chipListener(new ChipListener() {
                        @Override
                        public void chipSelected(int index) {

                            sortType = String.valueOf(index);

                        }
                        @Override
                        public void chipDeselected(int index) {
                            //...
                        }
                    })
                    .build();


            int selectedSort = Integer.valueOf(sortType);

            chipSort.setSelectedChip(selectedSort);
        }

        else {

            sortBy.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            sortBy.setMaxValue(3);
            sortBy.setDisplayedValues(new String[]{"Popularity", "Newest", "Price highest to lowest", "Price lowest to highest"});
            sortBy.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    // TODO Auto-generated method stub

                    String filtering = selectSortInsideTV.getText().toString();

                    if (filtering.equals("Popularity")) {
                        sortType = "0";
                    } else if (filtering.equals("Price lowest to highest")) {
                        sortType = "1";
                    } else if (filtering.equals("Price highest to lowest")) {
                        sortType = "2";
                    } else if (filtering.equals("Newest")) {
                        sortType = "6";
                    }

                }
            });

           final String[] sortText = new String[4];
            sortText[0] = "Popularity";
            sortText[1] = "Price lowest to highest";
            sortText[2] = "Price highest to lowest";
            sortText[3] = "Newest";


            new ChipCloud.Configure()
                    .chipCloud(chipSort)
                    .selectedColor(Color.parseColor("#000000"))
                    .selectedFontColor(Color.parseColor("#ffffff"))
                    .deselectedColor(Color.parseColor("#e1e1e1"))
                    .deselectedFontColor(Color.parseColor("#333333"))
                    .labels(sortText)
                    .gravity(ChipCloud.Gravity.CENTER)
                    .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                    .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                    .chipListener(new ChipListener() {
                        @Override
                        public void chipSelected(int index) {

                            sortType = String.valueOf(index);

                            if(sortText[index].equals("Newest"))
                            {
                                sortType = "6";
                            }

                        }
                        @Override
                        public void chipDeselected(int index) {
                            //...
                        }
                    })
                    .build();

            if(sortType.equals("6"))
            {
                chipSort.setSelectedChip(3);

            }

            else {

                int selectedSort = Integer.valueOf(sortType);
                chipSort.setSelectedChip(selectedSort);

            }

        }

//        sortRV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sortByDialog.show();
//
//                btnSelectSort.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        sortByDialog.dismiss();
//
//
//                    }
//                });
//
//            }
//        });


        clearBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        closeBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        applyBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));



        applyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("fromHome", fromHome);
                bundle.putString("prodID", catID);
                bundle.putString("catName", catName);

                if(sortType == "0")
                {

                    bundle.putBoolean("isSorted",false);
                    bundle.putString("sortType","0");

                }
                else {

                    bundle.putBoolean("isSorted",true);
                    bundle.putString("sortType",sortType);

                }

                System.out.println("data sini 1 = " + forApiCallSizeSelected);
                System.out.println("data sini 2 = " + forApiCallColourSelected);
                System.out.println("data sini 3 = " + sizeSelectedArray);
                System.out.println("data sini 4 = " + colorSelectedArray);



                bundle.putStringArrayList("sizeList", forApiCallSizeSelected);
                bundle.putStringArrayList("colourList", forApiCallColourSelected);
                bundle.putIntegerArrayList("sizeList_Selected", sizeSelectedArray);
                bundle.putIntegerArrayList("colourList_Selected", colorSelectedArray);


                if (forApiCallSizeSelected.size() == 0&&forApiCallColourSelected.size()==0&&sortType == "0"){
                    bundle.putBoolean("filtered", false);
                }else {
                    bundle.putBoolean("filtered", true);
                }

                Fragment fragment = new ProductListFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FilterAndSortFragment filterFragment = (FilterAndSortFragment)fragmentManager.findFragmentByTag("FilterSortFragment");

                ((MainActivity) getActivity()).getSupportActionBar().show();
//
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

        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FragmentManager fm = getActivity().getFragmentManager();
                fm.popBackStack();
                ((MainActivity) getActivity()).getSupportActionBar().show();

            }

        });

        clearBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               forApiCallSizeSelected.clear();
               sizeSelectedArray.clear();
               forApiCallColourSelected.clear();
               colorSelectedArray.clear();

               sortType = "0";

               chipSort.setSelectedChip(0);
               chipCloud.setMode(ChipCloud.Mode.NONE);
               chipCloudColor.setMode(ChipCloud.Mode.NONE);


            }

        });

        chipCloud = (ChipCloud) view.findViewById(R.id.chipSizeCloud);
        chipCloud.setMode(ChipCloud.Mode.MULTI);

        chipCloudColor = (ChipCloud) view.findViewById(R.id.chipColourCloud);
        chipCloudColor.setMode(ChipCloud.Mode.MULTI);


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

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
                    {


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

                        new ChipCloud.Configure()
                                .chipCloud(chipCloud)
                                .selectedColor(Color.parseColor("#000000"))
                                .selectedFontColor(Color.parseColor("#ffffff"))
                                .deselectedColor(Color.parseColor("#e1e1e1"))
                                .deselectedFontColor(Color.parseColor("#333333"))
                                .labels(allSizes)
                                .gravity(ChipCloud.Gravity.LEFT)
                                .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                                .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                                .chipListener(new ChipListener() {
                                    @Override
                                    public void chipSelected(int index) {

                                        String selected = allSizesID[index];

                                        if(sizeSelectedArray.contains(index))
                                        {

                                        }

                                        else {
                                            sizeSelectedArray.add(index);
                                            forApiCallSizeSelected.add(selected);

                                        }

                                    }
                                    @Override
                                    public void chipDeselected(int index) {

                                        String selected = allSizesID[index];

//                                        forApiCallSizeSelected.remove(selected);

//                                        System.out.println("lai 1 = " + selected);
//
//                                        System.out.println("lai 2 = " + index);
//
//                                        System.out.println("lai 3 = " + colorSelectedArray);

                                        for(int y=0;y<sizeSelectedArray.size();y++)
                                        {

                                            int ss = sizeSelectedArray.get(y);

                                            if(ss == index)
                                            {
                                                sizeSelectedArray.remove(y);

                                            }

                                        }

                                        for(int x=0;x<forApiCallSizeSelected.size();x++)
                                        {

                                            String sizeSelected = forApiCallSizeSelected.get(x);

                                            if(sizeSelected.equals(selected))
                                            {
                                                forApiCallSizeSelected.remove(x);

                                            }

                                        }

                                        //...
                                    }
                                })
                                .build();


//                        checkedItemsSize = new boolean[allSizes.length];
//
//                        String[] title = new String[sizeSelectedArray.size()];
//
//                        for(int x = 0; x < sizeSelectedArray.size(); x++){
//                            checkedItemsSize[sizeSelectedArray.get(x)] = true;
//
//                            String size = allSizes[sizeSelectedArray.get(x)];
//                            title[x] = size;
//
//                        }



                    }

                    if(sizeSelectedArray.size()!= 0)
                    {
                        for(int x=0;x<sizeSelectedArray.size();x++)
                        {
                            chipCloud.setSelectedChip(sizeSelectedArray .get(x));
                        }

                    }

                }
                else
                if(action.equals("Products_filterType_color"))
                {
                    if(result.getBoolean("status"))
                    {
//
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = result.getJSONArray("data");

                        allColours = new String[jsonArr.length()];
                        allColoursID = new String[jsonArr.length()];

                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            NoOfCBColor++;
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            final String colorID = jObj.getString("id_combination");
                            final String colorName = jObj.getString("name");

                            String color = colorName;
                            String clrID = colorID;

                            allColours[i] = color;
                            allColoursID[i] = clrID;

                        }

//
                        new ChipCloud.Configure()
                                .chipCloud(chipCloudColor)
                                .selectedColor(Color.parseColor("#000000"))
                                .selectedFontColor(Color.parseColor("#ffffff"))
                                .deselectedColor(Color.parseColor("#e1e1e1"))
                                .deselectedFontColor(Color.parseColor("#333333"))
                                .labels(allColours)
                                .gravity(ChipCloud.Gravity.LEFT)
                                .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                                .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                                .chipListener(new ChipListener() {
                                    @Override
                                    public void chipSelected(int index) {


                                        String selected = allColoursID[index];


                                        if(colorSelectedArray.contains(index))
                                        {

                                        }

                                        else {
                                            forApiCallColourSelected.add(selected);

                                            colorSelectedArray.add(index);
                                        }

                                    }
                                    @Override
                                    public void chipDeselected(int index) {

                                        String selected = allColoursID[index];

//                                        forApiCallColourSelected.remove(selected);

//                                        System.out.println("lai 1 = " + selected);
//
//                                        System.out.println("lai 2 = " + index);
//
//                                        System.out.println("lai 3 = " + colorSelectedArray);

                                        for(int y=0;y<colorSelectedArray.size();y++)
                                        {

                                            int ss = colorSelectedArray.get(y);

                                            if(ss == index)
                                            {
                                                colorSelectedArray.remove(y);

                                            }

                                        }

                                        for(int x=0;x<forApiCallColourSelected.size();x++)
                                        {

                                            String colouSelected = forApiCallColourSelected.get(x);

                                            if(colouSelected.equals(selected))
                                            {
                                                forApiCallColourSelected.remove(x);

                                            }

                                        }


                                        //...
                                    }
                                })
                                .build();


                    }

                    if(colorSelectedArray.size()!= 0)
                    {
                        for(int x=0;x<colorSelectedArray.size();x++)
                        {
                            chipCloudColor.setSelectedChip(colorSelectedArray.get(x));
                        }

                    }


                }


            }
            catch (Exception e){

            }

        }

    }
}
