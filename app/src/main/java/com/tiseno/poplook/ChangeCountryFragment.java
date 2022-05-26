package com.tiseno.poplook;


import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.AlphabetListAdapter;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.countryShopChooserItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeCountryFragment extends ListFragment implements AsyncTaskCompleteListener<JSONObject> {


    private AlphabetListAdapter adapter = new AlphabetListAdapter();
    private GestureDetector mGestureDetector;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
    private int sideIndexHeight;
    private static float sideIndexX;
    private static float sideIndexY;
    private int indexListSize;
    LinearLayout sideIndex;
    ArrayList<countryShopChooserItem> countries = new ArrayList<countryShopChooserItem>();
    SharedPreferences pref;
    ImageButton xCloseBtnCountry;
    List<AlphabetListAdapter.Row> rows = new ArrayList<AlphabetListAdapter.Row>();
    List<String> rowIDList = new ArrayList<String>();
    List<String> rowNameList = new ArrayList<String>();
    List<String> rowISOList = new ArrayList<String>();
    List<String> rowCurrency = new ArrayList<String>();

    class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            if (sideIndexX >= 0 && sideIndexY >= 0) {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_country, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Change Country");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        mGestureDetector = new GestureDetector(getActivity(), new SideIndexGestureListener());
        sideIndex = (LinearLayout) v.findViewById(R.id.sideIndex);

        getShippingTo();
        return v;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            return false;
        }
    }

    public void updateList() {

        sideIndex.removeAllViews();
        indexListSize = alphabet.size();
        if (indexListSize < 1) {
            return;
        }

        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
        int tmpIndexListSize = indexListSize;
        while (tmpIndexListSize > indexMaxSize) {
            tmpIndexListSize = tmpIndexListSize / 2;
        }
        double delta;
        if (tmpIndexListSize > 0) {
            delta = indexListSize / tmpIndexListSize;
        } else {
            delta = 1;
        }

        TextView tmpTV;
        for (double i = 1; i <= indexListSize; i = i + delta) {
            Object[] tmpIndexItem = alphabet.get((int) i - 1);
            String tmpLetter = tmpIndexItem[0].toString();

            tmpTV = new TextView(getActivity());
            tmpTV.setText(tmpLetter);
            tmpTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
            tmpTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextColor(getActivity().getResources().getColor(R.color.light_green));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }

        sideIndexHeight = sideIndex.getHeight();

        sideIndex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // now you know coordinates of touch
                sideIndexX = event.getX();
                sideIndexY = event.getY();

                // and can display a proper item it country list
                displayListItem();

                return false;
            }
        });
    }

    public void displayListItem() {

        sideIndexHeight = sideIndex.getHeight();
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        if (itemPosition < alphabet.size()) {
            Object[] indexItem = alphabet.get(itemPosition);
            int subitemPosition = sections.get(indexItem[0]);

            //ListView listView = (ListView) findViewById(android.R.id.list);
            getListView().setSelection(subitemPosition);
        }
    }

    private void getShippingTo(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="preferences/shippingto?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(),this);
        callws.execute(action);

    }
    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){

            try {
                if (result.getBoolean("status"))
                {
                    System.out.println("hahaaaaaaab");
                    countries.clear();
                    rowIDList.clear();
                    rowNameList.clear();
                    rowISOList.clear();
                    rowCurrency.clear();
                    alphabet.clear();
                        System.out.println("hahaaaaaaac "+result);
                        JSONArray jsonArr = null;
                        JSONArray jsonArr1 = null;
                        jsonArr = result.getJSONArray("data");
                        System.out.println("hahaaaaaaad "+jsonArr);
                        for(int j = 0; j < jsonArr.length(); j++)
                        {

                            String id_country =  jsonArr.getJSONObject(j).getString("id_country");
                            String country_name = jsonArr.getJSONObject(j).getString("country_name");
                            String country_iso_code =  jsonArr.getJSONObject(j).getString("country_iso_code");
                            String id_shop = jsonArr.getJSONObject(j).getString("id_shop");
                            String shop_url =  jsonArr.getJSONObject(j).getString("shop_url");
                            String currency_sign =  jsonArr.getJSONObject(j).getString("currency_sign");

                            countries.add(j,new countryShopChooserItem(id_country,country_name,country_iso_code,id_shop,shop_url,currency_sign,false));
//                            mItems.add(new StatePickerItem(id_country,state));
                            System.out.println("hahaaaaaaa");
                        }

                        Collections.sort(countries, new Comparator<countryShopChooserItem>(){
                            public int compare(countryShopChooserItem emp1, countryShopChooserItem emp2) {
                                return emp1.getCountry_name().compareToIgnoreCase(emp2.getCountry_name());
                            }
                        });

//                        List<Row> rows = new ArrayList<Row>();
                        int start = 0;
                        int end = 0;
                        String previousLetter = null;
                        Object[] tmpIndexItem = null;
                        Pattern numberPattern = Pattern.compile("[0-9]");

                        for (countryShopChooserItem country : countries) {
                            String firstLetter = country.getCountry_name().substring(0, 1);

                            // Group numbers together in the scroller
                            if (numberPattern.matcher(firstLetter).matches()) {
                                firstLetter = "#";
                            }

                            // If we've changed to a new letter, add the previous letter to the alphabet scroller
                            if (previousLetter != null && !firstLetter.equals(previousLetter)) {
                                end = rows.size() - 1;
                                tmpIndexItem = new Object[3];
                                tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
                                tmpIndexItem[1] = start;
                                tmpIndexItem[2] = end;
                                alphabet.add(tmpIndexItem);

                                start = end + 1;
                            }

                            // Check if we need to add a header row
                            if (!firstLetter.equals(previousLetter)) {
                                rows.add(new AlphabetListAdapter.Section(firstLetter));
                                rowIDList.add(firstLetter.toUpperCase(Locale.UK));
                                rowNameList.add(firstLetter.toUpperCase(Locale.UK));
                                rowISOList.add(firstLetter.toUpperCase(Locale.UK));
                                rowCurrency.add(firstLetter.toUpperCase(Locale.UK));
                                sections.put(firstLetter, start);
                            }

                            // Add the country to the list
                            if(country.getCountry_iso_code().equals("MY")) {
                                rows.add(new AlphabetListAdapter.Item(country.getCountry_name() + " (RM)"));
                            }else if(country.getCountry_iso_code().equals("SG")){
                                rows.add(new AlphabetListAdapter.Item(country.getCountry_name() + " (SGD)"));
                            }else {
                                rows.add(new AlphabetListAdapter.Item(country.getCountry_name() + " (USD)"));
                            }
                            rowIDList.add(country.getId_shop());
                            if(country.getCountry_iso_code().equals("MY")) {
                                rowNameList.add(country.getCountry_name() + " (RM)");
                            }else if(country.getCountry_iso_code().equals("SG")){
                                rowNameList.add(country.getCountry_name() + " (SGD)");
                            }else {
                                rowNameList.add(country.getCountry_name() + " (USD)");
                            }
                            rowISOList.add(country.getCountry_iso_code());
                            rowCurrency.add(country.getCurrency_sign());
                            previousLetter = firstLetter;
                        }

                        if (previousLetter != null) {
                            // Save the last letter
                            tmpIndexItem = new Object[3];
                            tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
                            tmpIndexItem[1] = start;
                            tmpIndexItem[2] = rows.size() - 1;
                            alphabet.add(tmpIndexItem);
                        }

                        adapter.setRows(rows);
                        setListAdapter(adapter);

                        updateList();



                    }

//                    for(int i = 0; i < rows.size(); i++)
//                    {
//                        countryItem.add(i,new );
//                    }



                }

            catch (Exception e){
                System.out.println("erorr "+e);
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

    @Override
    public void onListItemClick(ListView l, View v,final int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(rowNameList.get(position).equals("A")||rowNameList.get(position).equals("B")||rowNameList.get(position).equals("C")||rowNameList.get(position).equals("D")||rowNameList.get(position).equals("E")||rowNameList.get(position).equals("F")||rowNameList.get(position).equals("G")||rowNameList.get(position).equals("H")||rowNameList.get(position).equals("I")||rowNameList.get(position).equals("J")||rowNameList.get(position).equals("K")||rowNameList.get(position).equals("L")||rowNameList.get(position).equals("M")||rowNameList.get(position).equals("N")||rowNameList.get(position).equals("O")||rowNameList.get(position).equals("P")||rowNameList.get(position).equals("Q")||rowNameList.get(position).equals("R")||rowNameList.get(position).equals("S")||rowNameList.get(position).equals("T")||rowNameList.get(position).equals("U")||rowNameList.get(position).equals("V")||rowNameList.get(position).equals("W")||rowNameList.get(position).equals("X")||rowNameList.get(position).equals("Y")||rowNameList.get(position).equals("Z"))
        {

        }else {
            pref = getActivity().getSharedPreferences("MyPref", 0);
            String SelectedCountryName = pref.getString("SelectedCountryName", "");
            if(rowNameList.get(position).equals(SelectedCountryName)){
                Toast toast = Toast.makeText(getActivity(),
                        "You already selected "+ SelectedCountryName, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show();
            }else {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Message")
                        .setMessage("Are you sure you want to change country? You will be logged out and item in your cart will be removed.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("UserID", "");
                                editor.putString("CartID", "");
                                editor.putString("cartItem", "0");
                                editor.putString("SelectedShopID", rowIDList.get(position));
                                editor.putString("SelectedCountryName", rowNameList.get(position));
                                editor.putString("SelectedCountryIsoCode", rowISOList.get(position));
                                editor.putString("SelectedCountryCurrency", rowCurrency.get(position));

                                editor.apply();
                                Toast toast = Toast.makeText(getActivity(),
                                        "Shipping To "+ rowNameList.get(position), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 50);
                                toast.show();
                                new Handler().post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent intent = getActivity().getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        getActivity().overridePendingTransition(0, 0);
                                        getActivity().finish();

                                        getActivity().overridePendingTransition(0, 0);
                                        startActivity(intent);
                                    }
                                });

                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }
    }
}
