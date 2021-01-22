package com.tiseno.poplook;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.Phone;
import com.tiseno.poplook.functions.PhoneCategory;
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

import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class NewFilterFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    Button closeBtn, clearAllBtn, btn_filtered;

    TextView sortTV, filterTV;

    boolean atSizes;

    ExpandableLayout  expandLayout;

    ScrollView scrollVieeew;

    int NoOfCBSizes = 0, NoOfCBColor = 0;

    ArrayList<String> listArray_attribute = new ArrayList<String>();
    List<String> listArray_attributeName = new ArrayList<String>();
    ArrayList<String> listArray_colour = new ArrayList<String>();
    List<String> listArray_colorName = new ArrayList<String>();

    ArrayList<String> listArray_attribute_selected = new ArrayList<String>();
    ArrayList<String> listArray_colour_selected = new ArrayList<String>();

    ArrayList<String> listArray_attribute_fromJSON = new ArrayList<String>();
    ArrayList<String> listArray_colour_fromJSON  = new ArrayList<String>();

    String fromHome,catID,catName;
    String sizesSelected="",colourSelected="";

    boolean emptyArrFromProductList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_newfilter, container, false);

        Bundle bundle = this.getArguments();
        fromHome = bundle.getString("fromHome");
        catID = bundle.getString("prodID");
        catName=bundle.getString("catName");
        listArray_attribute = getArguments().getStringArrayList("sizeList");
        listArray_colour = getArguments().getStringArrayList("colourList");
        listArray_attribute_selected = getArguments().getStringArrayList("sizeList_Selected");
        listArray_colour_selected = getArguments().getStringArrayList("colourList_Selected");
        sizesSelected = bundle.getString("sizes");
        colourSelected = bundle.getString("colour");

        if(listArray_attribute.size() == 0 && listArray_colour.size() == 0){

            emptyArrFromProductList = true;

        }

        else {

            emptyArrFromProductList = false;

        }

        ((MainActivity) getActivity()).getSupportActionBar().hide();

//        sortTV = (TextView)view.findViewById(R.id.sortbyTV);

        closeBtn = (Button)view.findViewById(R.id.closeSearch);
        clearAllBtn = (Button)view.findViewById(R.id.clearAll);
        expandLayout = (ExpandableLayout)view.findViewById(R.id.expandable_layout);
        scrollVieeew = (ScrollView)view.findViewById(R.id.scroll_View);
        btn_filtered = (Button)view.findViewById(R.id.btn_apply_filter_2);

        btn_filtered.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        clearAllBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

//        filterTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        expandLayout.setRenderer(new ExpandableLayout.Renderer<PhoneCategory, Phone> (){
            @Override
            public void renderParent(View view, PhoneCategory phoneCategory, boolean isExpanded, int parentPosition) {

                ((TextView)view.findViewById(R.id.tv_parent_name)).setText(phoneCategory.name);
                ((TextView)view.findViewById(R.id.tv_parent_name)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded?R.drawable.ic_arrow_up:R.drawable.ic_arrow_down);

            }

            @Override
            public void renderChild(View view, Phone phone, final int parentPosition,final int childPosition) {


                ((TextView)view.findViewById(R.id.tv_child_name)).setText(phone.name);
                ((TextView)view.findViewById(R.id.tv_child_name)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

                if(parentPosition == 0)
                {

                    if(listArray_attribute_selected.contains(phone.name))
                    {

                        ((TextView)view.findViewById(R.id.tv_child_name)).setTextColor(Color.parseColor("#D3D3D3"));

                        Drawable img = getActivity().getResources().getDrawable( R.drawable.ic_checkmark_2);
                        img.setBounds( 0, 0, 50, 50 );
                        ((TextView)view.findViewById(R.id.tv_child_name)).setCompoundDrawables( null, null, img, null );

                    }

                }

                if(parentPosition == 1)
                {

                    if(listArray_colour_selected.contains(phone.name))
                    {

                        ((TextView)view.findViewById(R.id.tv_child_name)).setTextColor(Color.parseColor("#D3D3D3"));

                        Drawable img = getActivity().getResources().getDrawable( R.drawable.ic_checkmark_2);
                        img.setBounds( 0, 0, 50, 50 );
                        ((TextView)view.findViewById(R.id.tv_child_name)).setCompoundDrawables( null, null, img, null );

                    }

                }


                ((TextView)view.findViewById(R.id.tv_child_name)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TextView selectedSize = (TextView)view;

                        if(parentPosition == 0)
                        {

                            String selected_Size = listArray_attribute_fromJSON.get(childPosition);
                            String selected_Size_Exact = listArray_attributeName.get(childPosition);


                            if(listArray_attribute_selected.contains(selectedSize.getText().toString())){

                                selectedSize.setTextColor(Color.BLACK);

                                selectedSize.setCompoundDrawables( null, null, null, null );

                                listArray_attribute.remove(selected_Size);
                                listArray_attribute_selected.remove(selected_Size_Exact);

                            }

                            else
                                {

                                    selectedSize.setTextColor(Color.parseColor("#D3D3D3"));

                                    Drawable img = getActivity().getResources().getDrawable( R.drawable.ic_checkmark_2);
                                    img.setBounds( 0, 0, 50, 50 );
                                    selectedSize.setCompoundDrawables( null, null, img, null );

                                    listArray_attribute.add(selected_Size);
                                    listArray_attribute_selected.add(selected_Size_Exact);

                                }


                                if(childPosition == 0)
                                {
                                    sizesSelected += listArray_attributeName.get(childPosition);
                                }
                                else
                                {
                                    sizesSelected += ", " + listArray_attributeName.get(childPosition);
                                }

                        }

                        if(parentPosition == 1)
                        {

                            String selected_Colour = listArray_colour_fromJSON.get(childPosition);
                            String selected_Colour_Exact = listArray_colorName.get(childPosition);

                            if(listArray_colour_selected.contains(selectedSize.getText().toString())){

                                selectedSize.setTextColor(Color.BLACK);

                                selectedSize.setCompoundDrawables( null, null, null, null );

                                listArray_colour.remove(selected_Colour);
                                listArray_colour_selected.remove(selected_Colour_Exact);

                            }

                            else
                            {

                                selectedSize.setTextColor(Color.parseColor("#D3D3D3"));

                                Drawable img = getActivity().getResources().getDrawable( R.drawable.ic_checkmark_2);
                                img.setBounds( 0, 0, 50, 50 );
                                selectedSize.setCompoundDrawables( null, null, img, null );

                                listArray_colour.add(selected_Colour);
                                listArray_colour_selected.add(selected_Colour_Exact);

                            }


                            if(childPosition == 0)
                            {
                                colourSelected += listArray_colorName.get(childPosition);
                            }
                            else
                            {
                                colourSelected += ", " + listArray_colorName.get(childPosition);
                            }

                        }

                        refreshClearAllButton();
                    }
                });

            }
        });

        expandLayout.setExpandListener(new ExpandCollapseListener.ExpandListener<PhoneCategory>() {
            @Override
            public void onExpanded(int i, PhoneCategory phoneCategory, View view) {

                view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.ic_arrow_up);

                if(i == 1)
                {

                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            scrollVieeew.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    };
                    scrollVieeew.post(runnable);

                }


            }

        });

        expandLayout.setCollapseListener(new ExpandCollapseListener.CollapseListener<PhoneCategory>() {
            @Override
            public void onCollapsed(int i, PhoneCategory phoneCategory, View view) {

                view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.ic_arrow_down);
            }
        });


        getSizeList();
        getColorsList();

        refreshClearAllButton();

        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FragmentManager fm = getActivity().getFragmentManager();
                fm.popBackStack();
                ((MainActivity) getActivity()).getSupportActionBar().show();

            }

        });

        clearAllBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(emptyArrFromProductList)
                {

                    listArray_colour_selected.clear();
                    listArray_colour.clear();

                    listArray_attribute_selected.clear();
                    listArray_attribute.clear();

                    FragmentManager fm = getActivity().getFragmentManager();
                    fm.popBackStack();
                    ((MainActivity) getActivity()).getSupportActionBar().show();

                }

                else {

                    listArray_colour_selected.clear();
                    listArray_colour.clear();

                    listArray_attribute_selected.clear();
                    listArray_attribute.clear();

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

                    bundle.putStringArrayList("colourList_Selected", listArray_colour_selected);
                    bundle.putStringArrayList("sizeList_Selected", listArray_attribute_selected);

                    bundle.putString("sizes", sizesSelected);
                    bundle.putString("colour",colourSelected);
                    bundle.putInt("number",1);
                    System.out.println("size selected is " + sizesSelected);
                    System.out.println("colour selected is " + colourSelected);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    NewFilterFragment filterFragment = (NewFilterFragment)fragmentManager.findFragmentByTag("NewFilterFragment");

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

            }

        });

        btn_filtered.setOnClickListener(new View.OnClickListener() {
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

                bundle.putStringArrayList("colourList_Selected", listArray_colour_selected);
                bundle.putStringArrayList("sizeList_Selected", listArray_attribute_selected);

                bundle.putString("sizes", sizesSelected);
                bundle.putString("colour",colourSelected);
                bundle.putInt("number",1);
                System.out.println("size selected is " + listArray_attribute);
                System.out.println("colour selected is " + listArray_colour);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                NewFilterFragment filterFragment = (NewFilterFragment)fragmentManager.findFragmentByTag("NewFilterFragment");

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


        return view;

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


    private Section<PhoneCategory,Phone>getSection()
    {

        Section<PhoneCategory,Phone>section = new Section<>();

        PhoneCategory phoneCat = new PhoneCategory("Size");

        List<Phone> listPhone = new ArrayList<>();

        for(int i = 0;i<listArray_attributeName.size();i++)
        {

            String sizeList = listArray_attributeName.get(i);

            listPhone.add(new Phone(sizeList));
            section.parent = phoneCat;


        }

        section.children.addAll(listPhone);

        return section;

    }

    private Section<PhoneCategory,Phone>getColourSection()
    {

        Section<PhoneCategory,Phone>section = new Section<>();

        PhoneCategory phoneCat = new PhoneCategory("Colour");

        List<Phone> listPhone = new ArrayList<>();

        for(int i = 0;i<listArray_colorName.size();i++)
        {

            String colourList = listArray_colorName.get(i);


            listPhone.add(new Phone(colourList));
            section.parent = phoneCat;


        }

        section.children.addAll(listPhone);

        return section;

    }

    private void refreshClearAllButton()
    {

        if(listArray_attribute_selected.size() == 0 && listArray_colour_selected.size() == 0)
        {

            clearAllBtn.setVisibility(View.INVISIBLE);

        }

        else
        {

            clearAllBtn.setVisibility(View.VISIBLE);

        }

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
//                        listArray_attribute.clear();
                        listArray_attributeName.clear();

                        JSONArray jsonArr = new JSONArray();
                        jsonArr = result.getJSONArray("data");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            NoOfCBSizes++;
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            final String attributeID = jObj.getString("id_combination");
                            final String attributeName = jObj.getString("name");

                            String attribute = attributeName;
                            String attID = attributeID;

                            listArray_attributeName.add(attribute);
                            listArray_attribute_fromJSON.add(attID);


                        }

                        expandLayout.addSection(getSection());

                    }
                }
                else
                if(action.equals("Products_filterType_color"))
                {
                    if(result.getBoolean("status"))
                    {
//                        listArray_colour.clear();
                        listArray_colorName.clear();
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = result.getJSONArray("data");
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            NoOfCBColor++;
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            final String colorID = jObj.getString("id_combination");
                            final String colorName = jObj.getString("name");

                            String color = colorName;
                            String clrID = colorID;

                            listArray_colorName.add(color);
                            listArray_colour_fromJSON.add(clrID);

                        }

                        expandLayout.addSection(getColourSection());

                    }
                }
            }
            catch (Exception e){

            }

        }

    }

    public void backBtn(){
        if(!atSizes){
            if(colourSelected.length()!=0){

            }else{

            }
//            linearLayoutColor.setVisibility(View.GONE);
//            linearLayoutBtnColorSize.setVisibility(View.GONE);

            ((MainActivity) getActivity()).changeToolBarText("Filter By");

            System.out.println("selected color is "+listArray_colour);
        }
        else{
            if(sizesSelected.length()!=0) {

            }else {

            }

            ((MainActivity) getActivity()).changeToolBarText("Filter By");

            ((MainActivity) getActivity()).changeBtnBackView(false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).backBtnControl("");
    }


}


