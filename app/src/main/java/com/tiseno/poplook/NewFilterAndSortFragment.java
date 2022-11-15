package com.tiseno.poplook;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.adroitandroid.chipcloud.FlowLayout;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.attributeItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewFilterAndSortFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    TextView filterText, categoryText, sizeText, colourText, sortText, clearAllTxt;

    TextView categorySelectedText, sizeSelectedText, colourSelectedText;

    Button applyBtn;

    String fromHome, catID, catName;
    String sortType;
    String catIDforAPI;

    RelativeLayout categoryRL, sizeRL, colourRL;

    int selected;

//    ArrayList<Integer> selected_item_size = new ArrayList<Integer>();
//    ArrayList<Integer> selected_item_colour = new ArrayList<Integer>();
//
//    ArrayList<String> selected_item_size_text = new ArrayList<String>();
//    ArrayList<String> selected_item_colour_text = new ArrayList<String>();
//
//    ArrayList<String> forApiCallSizeSelected = new ArrayList<>();
//    ArrayList<String> forApiCallColourSelected = new ArrayList<>();


    String[] allSizes;
    String[] allSizesID;
    String[] allColours;
    String[] allColoursID;
    String[] category;
    String[] categoryID;

    boolean clearPressed = false;

    int NoOfCBSizes = 0, NoOfCBColor = 0, NoOfCBCategory = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.new_filter_sort, container, false);

        Bundle bundle = this.getArguments();
        fromHome = bundle.getString("fromHome");
        catID = bundle.getString("prodID");
        catName = bundle.getString("catName");
        selected = bundle.getInt("category_id");
        sortType = bundle.getString("sortType", "0");
        allSizes = bundle.getStringArray("sizesFilterData");
        allSizesID = bundle.getStringArray("sizesIDFilterData");
        allColours = bundle.getStringArray("coloursFilterData");
        allColoursID = bundle.getStringArray("coloursIDFilterData");

//        selected_item_size = bundle.getIntegerArrayList("sizeList_Selected");
//        selected_item_colour = bundle.getIntegerArrayList("colourList_Selected");
//        forApiCallColourSelected = bundle.getStringArrayList("colourList");
//        forApiCallSizeSelected = bundle.getStringArrayList("sizeList");

        filterText = contentView.findViewById(R.id.filter_text);
        filterText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        categoryText = contentView.findViewById(R.id.category_text);
        categoryText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        sizeText = contentView.findViewById(R.id.size_text);
        sizeText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        colourText = contentView.findViewById(R.id.colour_text);
        colourText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        sortText = contentView.findViewById(R.id.sort_text);
        sortText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        categorySelectedText = contentView.findViewById(R.id.category_selected_text);
        categorySelectedText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        sizeSelectedText = contentView.findViewById(R.id.size_selected_text);
        sizeSelectedText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        colourSelectedText = contentView.findViewById(R.id.colour_selected_text);
        colourSelectedText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        clearAllTxt = contentView.findViewById(R.id.clearAllText);
        clearAllTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        applyBtn = contentView.findViewById(R.id.applyButtonNew);

        categoryRL = contentView.findViewById(R.id.category_RL);
        sizeRL = contentView.findViewById(R.id.size_RL);
        colourRL = contentView.findViewById(R.id.colour_RL);

        SpannableString content = new SpannableString("Clear All");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        clearAllTxt.setText(content);

        if (catID.equals("45") || catID.equals("310")) {
            categoryRL.setVisibility(View.VISIBLE);
            getCategoryList();

        }


        final ChipCloud chipSort = (ChipCloud) contentView.findViewById(R.id.sortingOption);
        chipSort.setMode(ChipCloud.Mode.SINGLE);

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        clearAllTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                selected_item_colour.clear();
//                selected_item_size.clear();
//                selected_item_size_text.clear();
//                selected_item_colour_text.clear();
//                forApiCallColourSelected.clear();
//                forApiCallSizeSelected.clear();

                if (catID.equals("45")) {
                    sortType = "6";
                    chipSort.setSelectedChip(2);
                }

                else {
                    sortType = "0";
                    chipSort.setSelectedChip(0);

                }

                selected = 11;


                sizeSelectedText.setText("All");
                colourSelectedText.setText("All");
                categorySelectedText.setText("Shop All");


            }
        });


        applyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("fromHome", fromHome);
                bundle.putString("prodID", catID);
                bundle.putString("catName", catName);
                bundle.putInt("selectedCategory_ID", selected);
                bundle.putString("selectedCategory_ID_API", catIDforAPI);
                bundle.putBoolean("isSorted", true);
                bundle.putString("sortType", sortType);
//                bundle.putStringArrayList("sizeList", forApiCallSizeSelected);
//                bundle.putStringArrayList("colourList", forApiCallColourSelected);
//                bundle.putIntegerArrayList("sizeList_Selected", selected_item_size);
//                bundle.putIntegerArrayList("colourList_Selected", selected_item_colour);
                bundle.putInt("number",1);


//                if (forApiCallSizeSelected.size() == 0 && forApiCallColourSelected.size() == 0 && sortType == "0" && selected == 11) {
//                    bundle.putBoolean("filtered", false);
//                }
//
//               else if (forApiCallSizeSelected.size() == 0 && forApiCallColourSelected.size() == 0 && sortType == "6" && selected == 11) {
//                    bundle.putBoolean("filtered", false);
//                }
//
//                else {
//                    bundle.putBoolean("filtered", true);
//                }


                Fragment fragment = new ProductListFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                NewFilterAndSortFragment filterFragment = (NewFilterAndSortFragment) fragmentManager.findFragmentByTag("NewFilterAndSortFragment");

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

        if (catID.equals("45")) {

            final String[] sortText = new String[3];
            sortText[0] = "Price lowest to highest";
            sortText[1] = "Price highest to lowest";
            sortText[2] = "Newest";


//            new ChipCloud.Configure()
//                    .chipCloud(chipSort)
//                    .selectedColor(Color.parseColor("#000000"))
//                    .selectedFontColor(Color.parseColor("#ffffff"))
//                    .deselectedColor(Color.parseColor("#e1e1e1"))
//                    .deselectedFontColor(Color.parseColor("#333333"))
//                    .labels(sortText)
//                    .gravity(ChipCloud.Gravity.CENTER)
//                    .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
//                    .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
//                    .chipListener(new ChipListener() {
//                        @Override
//                        public void chipSelected(int index) {
//
//                            sortType = String.valueOf(index);
//
//                            if (sortText[index].equals("Newest")) {
//                                sortType = "6";
//                            }
//                            if (sortText[index].equals("Price lowest to highest")) {
//                                sortType = "1";
//                            }
//                            if (sortText[index].equals("Price highest to lowest")) {
//                                sortType = "2";
//                            }
//
//                        }
//
//                        @Override
//                        public void chipDeselected(int index) {
//                            //...
//                        }
//                    })
//                    .build();

            if (sortType.equals("6")) {
                chipSort.setSelectedChip(2);

            }
            if (sortType.equals("1")) {
                chipSort.setSelectedChip(0);
            }
            if (sortType.equals("2")) {
                chipSort.setSelectedChip(1);
            }

        } else {

            final String[] sortText = new String[4];
            sortText[0] = "Popularity";
            sortText[1] = "Price lowest to highest";
            sortText[2] = "Price highest to lowest";
            sortText[3] = "Newest";


//            new ChipCloud.Configure()
//                    .chipCloud(chipSort)
//                    .selectedColor(Color.parseColor("#000000"))
//                    .selectedFontColor(Color.parseColor("#ffffff"))
//                    .deselectedColor(Color.parseColor("#e1e1e1"))
//                    .deselectedFontColor(Color.parseColor("#333333"))
//                    .labels(sortText)
//                    .gravity(ChipCloud.Gravity.CENTER)
//                    .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
//                    .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
//                    .chipListener(new ChipListener() {
//                        @Override
//                        public void chipSelected(int index) {
//
//                            sortType = String.valueOf(index);
//
//                            if (sortText[index].equals("Newest")) {
//                                sortType = "6";
//                            }
//                        }
//
//                        @Override
//                        public void chipDeselected(int index) {
//                            //...
//                        }
//                    })
//                    .build();

            if (sortType.equals("6")) {
                chipSort.setSelectedChip(3);

            } else {
                chipSort.setSelectedChip(Integer.parseInt(sortType));

            }
        }

        loadSizesFilter();
        loadColoursFilter();

//        getColorsList();

        return contentView;
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
                String action = result.getString("action");

                if (action.equals("Menus_filterCategory")) {
                    if (result.getBoolean("status")) {

                        JSONArray jsonArr = new JSONArray();
                        jsonArr = result.getJSONArray("data");

                        category = new String[jsonArr.length()];
                        categoryID = new String[jsonArr.length()];


                        for (int i = 0; i < jsonArr.length(); i++) {
                            NoOfCBCategory++;
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            final String attributeID = jObj.getString("id_category");
                            final String attributeName = jObj.getString("name");

                            String attribute = attributeName;
                            String attID = attributeID;

                            category[i] = attribute;
                            categoryID[i] = attID;

                        }

                        categoryRL.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Pick a Category");
                                builder.setSingleChoiceItems(category, selected,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                selected = item;
                                            }

                                        });


                                builder.setPositiveButton("Done",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String test = category[selected];
                                                categorySelectedText.setText(test);
                                                catIDforAPI = categoryID[selected];
                                            }
                                        });


                                builder.setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });

                                builder.show();

                            }
                        });

                        String test = category[selected];
                        categorySelectedText.setText(test);
                        catIDforAPI = categoryID[selected];

                    }
                }


            } catch (Exception e) {

            }

        }


    }

    private void loadSizesFilter(){

        sizeRL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                                selected_item_size.clear();
//                selected_item_size_text.clear();


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick a Size");

                boolean[] checkedItems = new boolean[allSizes.length];

                for (int i = 0; i < allSizes.length; i++) {
//                    if (selected_item_size.contains(i)) {
//                        checkedItems[i] = true;
//                    } else {
//                        checkedItems[i] = false;
//                    }
                }

                System.out.println("check filter array =  " + allSizes);

                builder.setMultiChoiceItems(allSizes, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                String selected = allSizesID[item];


//                                if (isChecked) {
//                                    selected_item_size.add(item);
//                                    forApiCallSizeSelected.add(selected);
//
//                                } else {
//
//                                    for (int y = 0; y < selected_item_size.size(); y++) {
//
//                                        int ss = selected_item_size.get(y);
//
//                                        if (ss == item) {
//                                            selected_item_size.remove(y);
//                                            forApiCallSizeSelected.remove(y);
//                                        }
//
//                                    }
//
//
//                                }


                            }
                        });
                builder.setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                selected_item_size_text.clear();
//
//                                for (int x = 0; x < selected_item_size.size(); x++) {
//                                    String selectedSize = allSizes[selected_item_size.get(x)];
//                                    selected_item_size_text.add(selectedSize);
//                                }
//
//                                sizeSelectedText.setText(selected_item_size_text.toString());
//
//                                if (selected_item_size.size() == 0) {
//
//                                    sizeSelectedText.setText("All");
//
//                                } else {
//
//                                    sizeSelectedText.setText(selected_item_size_text.toString());
//                                }

                            }
                        });

                builder.show();

            }
        });

//        for (int x = 0; x < selected_item_size.size(); x++) {
//            String selectedColour = allSizes[selected_item_size.get(x)];
//
//            selected_item_size_text.add(selectedColour);
//        }
//
//        sizeSelectedText.setText(selected_item_size_text.toString());
//
//        if (selected_item_size.size() == 0) {
//
//            sizeSelectedText.setText("All");
//
//        } else {
//
//            sizeSelectedText.setText(selected_item_size_text.toString());
//        }


    }

    private void loadColoursFilter(){

        colourRL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                                selected_item_colour.clear();
//                selected_item_colour_text.clear();

                boolean[] checkedItems = new boolean[allColours.length];

//                for (int i = 0; i < allColours.length; i++) {
//                    if (selected_item_colour.contains(i)) {
//                        checkedItems[i] = true;
//                    } else {
//                        checkedItems[i] = false;
//                    }
//                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick a Colour");
                builder.setMultiChoiceItems(allColours, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                String selected = allColoursID[item];

//
//                                if (isChecked) {
//
//                                    forApiCallColourSelected.add(selected);
//                                    selected_item_colour.add(item);
//                                } else {
//                                    for (int y = 0; y < selected_item_colour.size(); y++) {
//
//                                        int ss = selected_item_colour.get(y);
//
//                                        if (ss == item) {
//                                            selected_item_colour.remove(y);
//                                            forApiCallColourSelected.remove(y);
//
//
//                                        }
//
//                                    }
//                                }


                            }
                        });
                builder.setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                for (int x = 0; x < selected_item_colour.size(); x++) {
//                                    String selectedColour = allColours[selected_item_colour.get(x)];
//
//                                    selected_item_colour_text.add(selectedColour);
//                                }
//
//                                colourSelectedText.setText(selected_item_colour_text.toString());
//
//                                if (selected_item_colour.size() == 0) {
//
//                                    colourSelectedText.setText("All");
//
//                                } else {
//
//                                    colourSelectedText.setText(selected_item_colour_text.toString());
//                                }

                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                builder.show();

            }
        });

//        for (int x = 0; x < selected_item_colour.size(); x++) {
//            String selectedColour = allColours[selected_item_colour.get(x)];
//
//            selected_item_colour_text.add(selectedColour);
//        }
//
//        colourSelectedText.setText(selected_item_colour_text.toString());
//
//        if (selected_item_colour.size() == 0) {
//
//            colourSelectedText.setText("All");
//
//        } else {
//
//            colourSelectedText.setText(selected_item_colour_text.toString());
//        }
    }
}
