package com.tiseno.poplook;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.ListFragment;

import com.tiseno.poplook.functions.AlphabetListAdapter;
import com.tiseno.poplook.functions.countryStateItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Pattern;

public class ChooseCountryAddress extends ListFragment implements AsyncTaskCompleteListener<JSONObject> {

    String SelectedCountryIsoCode,SelectedShopID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.choose_country_address, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

        SelectedCountryIsoCode = pref.getString("SelectedCountryIsoCode", "MY");
        SelectedShopID  = pref.getString("SelectedShopID", "1");


        ((MainActivity) getActivity()).changeToolBarText("Change Country");

        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);

        getCountryList();

        return contentView;
    }

    private void getCountryList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        if(SelectedCountryIsoCode.equals("MY")) {
            String action = "Infos/countries/" + SelectedCountryIsoCode + "?apikey="+apikey;
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(action);
        }else{
            String action = "Infos/countries/shop/" + SelectedShopID + "?apikey="+apikey;
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(action);
        }

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        if(result!=null){
            if(SelectedCountryIsoCode.equals("MY")) {
                try {
                    if (result.getBoolean("status")) {
                        System.out.println("hahaaaaaaab");
                        if (result.getString("action").equals("Infos_countries")) {
//                            countryJObjresult = result;

                            JSONObject jsonObject = null;

                            jsonObject = result.getJSONObject("data");

                            String id_country = jsonObject.getJSONObject("country").getString("id_country");
                            String countryname = jsonObject.getJSONObject("country").getString("name");

//                            countries.add(0, new countryStateItem(id_country, countryname, "", "", false));
////                            mItems.add(new StatePickerItem(id_country,state));
//                            System.out.println("hahaaaaaaa");
//
//
//                            Collections.sort(countries, new Comparator<countryStateItem>() {
//                                public int compare(countryStateItem emp1, countryStateItem emp2) {
//                                    return emp1.getCountryName().compareToIgnoreCase(emp2.getCountryName());
//                                }
//                            });

//                        List<Row> rows = new ArrayList<Row>();
//                            int start = 0;
//                            int end = 0;
//                            String previousLetter = null;
//                            Object[] tmpIndexItem = null;
//                            Pattern numberPattern = Pattern.compile("[0-9]");
//
//                            for (countryStateItem country : countries) {
//                                String firstLetter = country.getCountryName().substring(0, 1);
//
//                                // Group numbers together in the scroller
//                                if (numberPattern.matcher(firstLetter).matches()) {
//                                    firstLetter = "#";
//                                }
//
//                                // If we've changed to a new letter, add the previous letter to the alphabet scroller
//                                if (previousLetter != null && !firstLetter.equals(previousLetter)) {
//                                    end = rows.size() - 1;
//                                    tmpIndexItem = new Object[3];
//                                    tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
//                                    tmpIndexItem[1] = start;
//                                    tmpIndexItem[2] = end;
//                                    alphabet.add(tmpIndexItem);
//
//                                    start = end + 1;
//                                }
//
//                                // Check if we need to add a header row
//                                if (!firstLetter.equals(previousLetter)) {
//                                    rows.add(new AlphabetListAdapter.Section(firstLetter));
//                                    rowIDList.add(firstLetter.toUpperCase(Locale.UK));
//                                    rowNameList.add(firstLetter.toUpperCase(Locale.UK));
//                                    sections.put(firstLetter, start);
//                                }
//
//                                // Add the country to the list
//                                rows.add(new AlphabetListAdapter.Item(country.getCountryName()));
//                                rowIDList.add(country.getcCountryID());
//                                rowNameList.add(country.getCountryName());
//                                previousLetter = firstLetter;
//                            }
//
//                            if (previousLetter != null) {
//                                // Save the last letter
//                                tmpIndexItem = new Object[3];
//                                tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
//                                tmpIndexItem[1] = start;
//                                tmpIndexItem[2] = rows.size() - 1;
//                                alphabet.add(tmpIndexItem);
//                            }
//
//                            adapter.setRows(rows);
//                            setListAdapter(adapter);
//
//                            updateList();


                        }

//                    for(int i = 0; i < rows.size(); i++)
//                    {
//                        countryItem.add(i,new );
//                    }


                    }
                } catch (Exception e) {
                    System.out.println("erorr " + e);
                }
            }else{
                try {
                    if (result.getBoolean("status")) {
//                        System.out.println("hahaaaaaaab");
//                        if (result.getString("action").equals("Infos_countries")) {
//                            countryJObjresult = result;
//
//                            JSONArray jsonArray = null;
//
//                            jsonArray = result.getJSONArray("data");
//
//                            for (int k = 0; k < jsonArray.length(); k++) {
//                                String id_country = jsonArray.getJSONObject(k).getString("id_country");
//                                String countryname = jsonArray.getJSONObject(k).getString("name");
//
//                                countries.add(k, new countryStateItem(id_country, countryname, "", "", false));
////                            mItems.add(new StatePickerItem(id_country,state));
//                                System.out.println("hahaaaaaaa");
//                            }
//
//                            Collections.sort(countries, new Comparator<countryStateItem>() {
//                                public int compare(countryStateItem emp1, countryStateItem emp2) {
//                                    return emp1.getCountryName().compareToIgnoreCase(emp2.getCountryName());
//                                }
//                            });
//
////                        List<Row> rows = new ArrayList<Row>();
//                            int start = 0;
//                            int end = 0;
//                            String previousLetter = null;
//                            Object[] tmpIndexItem = null;
//                            Pattern numberPattern = Pattern.compile("[0-9]");
//
//                            for (countryStateItem country : countries) {
//                                String firstLetter = country.getCountryName().substring(0, 1);
//
//                                // Group numbers together in the scroller
//                                if (numberPattern.matcher(firstLetter).matches()) {
//                                    firstLetter = "#";
//                                }
//
//                                // If we've changed to a new letter, add the previous letter to the alphabet scroller
//                                if (previousLetter != null && !firstLetter.equals(previousLetter)) {
//                                    end = rows.size() - 1;
//                                    tmpIndexItem = new Object[3];
//                                    tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
//                                    tmpIndexItem[1] = start;
//                                    tmpIndexItem[2] = end;
//                                    alphabet.add(tmpIndexItem);
//
//                                    start = end + 1;
//                                }
//
//                                // Check if we need to add a header row
//                                if (!firstLetter.equals(previousLetter)) {
//                                    rows.add(new AlphabetListAdapter.Section(firstLetter));
//                                    rowIDList.add(firstLetter.toUpperCase(Locale.UK));
//                                    rowNameList.add(firstLetter.toUpperCase(Locale.UK));
//                                    sections.put(firstLetter, start);
//                                }
//
//                                // Add the country to the list
//                                rows.add(new AlphabetListAdapter.Item(country.getCountryName()));
//                                rowIDList.add(country.getcCountryID());
//                                rowNameList.add(country.getCountryName());
//                                previousLetter = firstLetter;
//                            }
//
//                            if (previousLetter != null) {
//                                // Save the last letter
//                                tmpIndexItem = new Object[3];
//                                tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
//                                tmpIndexItem[1] = start;
//                                tmpIndexItem[2] = rows.size() - 1;
//                                alphabet.add(tmpIndexItem);
//                            }
//
//                            adapter.setRows(rows);
//                            setListAdapter(adapter);
//
//                            updateList();
//
//
//                        }

//                    for(int i = 0; i < rows.size(); i++)
//                    {
//                        countryItem.add(i,new );
//                    }


                    }
                }
                catch (Exception e) {
                    System.out.println("erorr " + e);
                }
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
}
