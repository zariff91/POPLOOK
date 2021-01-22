package com.tiseno.poplook;


import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.transition.Explode;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.tiseno.poplook.functions.AlphabetListAdapter;
import com.tiseno.poplook.functions.AlphabetListAdapter.Item;
import com.tiseno.poplook.functions.AlphabetListAdapter.Row;
import com.tiseno.poplook.functions.AlphabetListAdapter.Section;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.StatePickerItem;
import com.tiseno.poplook.functions.countryStateItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class CountryPickerFragment extends ListActivity implements AsyncTaskCompleteListener<JSONObject> {

    private AlphabetListAdapter adapter = new AlphabetListAdapter();
    private GestureDetector mGestureDetector;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
    private int sideIndexHeight;
    private static float sideIndexX;
    private static float sideIndexY;
    private int indexListSize;
    Toolbar toolbar;
    LinearLayout sideIndex;
    ArrayList<countryStateItem> countries = new ArrayList<countryStateItem>();
    ArrayList<StatePickerItem> mItems= new ArrayList<StatePickerItem>();
    ImageButton xCloseBtnCountry;
    List<Row> rows = new ArrayList<Row>();
    List<String> rowIDList = new ArrayList<String>();
    List<String> rowNameList = new ArrayList<String>();
    ArrayList<countryStateItem> countryItem = new ArrayList<countryStateItem>();
    JSONObject countryJObjresult;
    TextView toolbar_title_country;
    String SelectedCountryIsoCode,SelectedShopID;
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
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_country_picker, container, false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        setContentView(R.layout.fragment_country_picker);

        SharedPreferences pref = this.getSharedPreferences("MyPref", 0);
        SelectedCountryIsoCode = pref.getString("SelectedCountryIsoCode", "MY");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        //
        toolbar = (Toolbar) findViewById(R.id.tool_bar_address);
        toolbar_title_country= (TextView) toolbar.findViewById(R.id.toolbar_title_country);

        toolbar_title_country.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_BLACK_FONT));

        xCloseBtnCountry = (ImageButton) toolbar.findViewById(R.id.xCloseBtnCountry);
        mGestureDetector = new GestureDetector(this, new SideIndexGestureListener());
        sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
//        List<String> countries = populateCountries();
        xCloseBtnCountry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();


            }

        });
        getCountryList();

//        updateList();

//        return view;
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

            tmpTV = new TextView(this);
            tmpTV.setText(tmpLetter);
            tmpTV.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_ROMAN_FONT));
            tmpTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextColor(this.getResources().getColor(R.color.light_green));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }

        sideIndexHeight = sideIndex.getHeight();

        sideIndex.setOnTouchListener(new OnTouchListener() {
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


    private void getCountryList(){
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        if(SelectedCountryIsoCode.equals("MY")) {
            String action = "Infos/countries/" + SelectedCountryIsoCode + "?apikey="+apikey;
            WebServiceAccessGet callws = new WebServiceAccessGet(this, this);
            callws.execute(action);
        }else{
            String action = "Infos/countries/shop/" + SelectedShopID + "?apikey="+apikey;
            WebServiceAccessGet callws = new WebServiceAccessGet(this, this);
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
                            countryJObjresult = result;

                            JSONObject jsonObject = null;

                            jsonObject = result.getJSONObject("data");

                            String id_country = jsonObject.getJSONObject("country").getString("id_country");
                            String countryname = jsonObject.getJSONObject("country").getString("name");

                            countries.add(0, new countryStateItem(id_country, countryname, "", "", false));
//                            mItems.add(new StatePickerItem(id_country,state));
                            System.out.println("hahaaaaaaa");


                            Collections.sort(countries, new Comparator<countryStateItem>() {
                                public int compare(countryStateItem emp1, countryStateItem emp2) {
                                    return emp1.getCountryName().compareToIgnoreCase(emp2.getCountryName());
                                }
                            });

//                        List<Row> rows = new ArrayList<Row>();
                            int start = 0;
                            int end = 0;
                            String previousLetter = null;
                            Object[] tmpIndexItem = null;
                            Pattern numberPattern = Pattern.compile("[0-9]");

                            for (countryStateItem country : countries) {
                                String firstLetter = country.getCountryName().substring(0, 1);

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
                                    rows.add(new Section(firstLetter));
                                    rowIDList.add(firstLetter.toUpperCase(Locale.UK));
                                    rowNameList.add(firstLetter.toUpperCase(Locale.UK));
                                    sections.put(firstLetter, start);
                                }

                                // Add the country to the list
                                rows.add(new Item(country.getCountryName()));
                                rowIDList.add(country.getcCountryID());
                                rowNameList.add(country.getCountryName());
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
                } catch (Exception e) {
                    System.out.println("erorr " + e);
                }
            }else{
                try {
                    if (result.getBoolean("status")) {
                        System.out.println("hahaaaaaaab");
                        if (result.getString("action").equals("Infos_countries")) {
                            countryJObjresult = result;

                            JSONArray jsonArray = null;

                            jsonArray = result.getJSONArray("data");

                            for (int k = 0; k < jsonArray.length(); k++) {
                                String id_country = jsonArray.getJSONObject(k).getString("id_country");
                                String countryname = jsonArray.getJSONObject(k).getString("name");

                                countries.add(k, new countryStateItem(id_country, countryname, "", "", false));
//                            mItems.add(new StatePickerItem(id_country,state));
                                System.out.println("hahaaaaaaa");
                            }

                            Collections.sort(countries, new Comparator<countryStateItem>() {
                                public int compare(countryStateItem emp1, countryStateItem emp2) {
                                    return emp1.getCountryName().compareToIgnoreCase(emp2.getCountryName());
                                }
                            });

//                        List<Row> rows = new ArrayList<Row>();
                            int start = 0;
                            int end = 0;
                            String previousLetter = null;
                            Object[] tmpIndexItem = null;
                            Pattern numberPattern = Pattern.compile("[0-9]");

                            for (countryStateItem country : countries) {
                                String firstLetter = country.getCountryName().substring(0, 1);

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
                                    rows.add(new Section(firstLetter));
                                    rowIDList.add(firstLetter.toUpperCase(Locale.UK));
                                    rowNameList.add(firstLetter.toUpperCase(Locale.UK));
                                    sections.put(firstLetter, start);
                                }

                                // Add the country to the list
                                rows.add(new Item(country.getCountryName()));
                                rowIDList.add(country.getcCountryID());
                                rowNameList.add(country.getCountryName());
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
                } catch (Exception e) {
                    System.out.println("erorr " + e);
                }
            }
        }
        else{

            new AlertDialog.Builder(this)
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(rowNameList.get(position).equals("A")||rowNameList.get(position).equals("B")||rowNameList.get(position).equals("C")||rowNameList.get(position).equals("D")||rowNameList.get(position).equals("E")||rowNameList.get(position).equals("F")||rowNameList.get(position).equals("G")||rowNameList.get(position).equals("H")||rowNameList.get(position).equals("I")||rowNameList.get(position).equals("J")||rowNameList.get(position).equals("K")||rowNameList.get(position).equals("L")||rowNameList.get(position).equals("M")||rowNameList.get(position).equals("N")||rowNameList.get(position).equals("O")||rowNameList.get(position).equals("P")||rowNameList.get(position).equals("Q")||rowNameList.get(position).equals("R")||rowNameList.get(position).equals("S")||rowNameList.get(position).equals("T")||rowNameList.get(position).equals("U")||rowNameList.get(position).equals("V")||rowNameList.get(position).equals("W")||rowNameList.get(position).equals("X")||rowNameList.get(position).equals("Y")||rowNameList.get(position).equals("Z"))
        {

        }else {
            Intent intent = new Intent();
            intent.putExtra("SelectedCountryIDAdd", rowIDList.get(position));
            intent.putExtra("SelectedCountryNameAdd", rowNameList.get(position));
            intent.putExtra("countries", countryJObjresult.toString());
            setResult(Activity.RESULT_OK, intent);
            SharedPreferences pref = this.getSharedPreferences("MyPref", 0);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("SelectedCountryIDAdd", rowIDList.get(position));
            editor.putString("SelectedCountryNameAdd", rowNameList.get(position));
            editor.putString("countries", countryJObjresult.toString());
            editor.apply();
            finish();
        }

//        Intent intent = new Intent();
//        getFragmentManager().findFragmentByTag("CountryFragment").onActivityResult(1, Activity.RESULT_OK, intent);
//        getFragmentManager().popBackStack();



    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
    }
}
