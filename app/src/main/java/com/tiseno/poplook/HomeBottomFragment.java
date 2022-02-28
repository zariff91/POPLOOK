package com.tiseno.poplook;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tiseno.poplook.functions.Artist;
import com.tiseno.poplook.functions.Genre;
import com.tiseno.poplook.functions.GenreAdapter;
import com.tiseno.poplook.functions.OnItemClickListener;
import com.tiseno.poplook.functions.sideMenuItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class HomeBottomFragment extends DialogFragment  implements AsyncTaskCompleteListener<JSONObject> {
    String SelectedShopID="1";
    String apikey="PL:@KrAk!fA9RpGDcnIfDKzljGkEqW48yU4M6Y2GckgawSVbEg62FHKHBU7awnidFZ4wxVxUdcTAvkxT1GrlVhuZ1dKlqzl9zlsedD66G";

    RecyclerView collectionHomeRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    private GenreAdapter mmAdapter;
    private List<Genre>genres;

    public selectBottomCollectionMenu selectCat;

    ArrayList<sideMenuItem> parentSideMenuArray = new ArrayList<sideMenuItem>();

    TextView side_menu_userTV;



// Declaring Layout Manager as a linear layout manager
// Declaring RecyclerView

    public HomeBottomFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface selectBottomCollectionMenu{

        void sendInput2(String data, String data2);


    }
    public static HomeBottomFragment newInstance(String title) {
        HomeBottomFragment frag = new HomeBottomFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_nav_myacc, container, false);

        collectionHomeRecyclerView = (RecyclerView)view.findViewById(R.id.CollectionRV);
        collectionHomeRecyclerView.setHasFixedSize(true);

        side_menu_userTV = (TextView)view.findViewById(R.id.side_menu_userTV);
        side_menu_userTV.setVisibility(View.GONE);


        mLayoutManager = new LinearLayoutManager(getActivity()); // Creating a layout Manager

        collectionHomeRecyclerView.setLayoutManager(mLayoutManager);

        getSideMenuList();


        Window window = getDialog().getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; //psotion
        lp.height = 80;
        window.setAttributes(lp);


        return view;
    }

    public void getSideMenuList(){

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        SelectedShopID = pref.getString("SelectedShopID", "1");

//        String action="Menus/mobile?apikey="+apikey+"&shop="+SelectedShopID+"&lang=1";
        String action="Menus/webcategories/lang/1/shop/"+SelectedShopID+"?apikey="+apikey+"&tier="+pref.getString("tier_level","")+"&user_id="+pref.getString("UserID", "");

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

        genres = new ArrayList<>(1000);


    }

    @Override
    public void onTaskComplete(JSONObject result) {

        if (result != null) {

            try {

                if (result.getString("action").equals("Menus_webcategories"))
                {

                    parentSideMenuArray.clear();

                    JSONObject test = result.getJSONObject("data");
                    JSONArray Titles = test.getJSONArray("children");

                    if(genres.size() == 0) {
                        for (int i = 0; i < Titles.length(); i++) {

                            String sideMenuTitle = Titles.getJSONObject(i).getString("name");
                            String sideMenuCatID = Titles.getJSONObject(i).getString("id");

                            JSONArray subSideMenuArray = Titles.getJSONObject(i).getJSONArray("children");

                            List<Artist> artists = new ArrayList<>(subSideMenuArray.length());

                            System.out.println("SideArray" + subSideMenuArray);

                            if (subSideMenuArray.length() > 0) {

                                if(sideMenuTitle.equals("Collection"))
                                {

                                    for (int j = 0; j < subSideMenuArray.length(); j++) {
                                        String catID = subSideMenuArray.getJSONObject(j).getString("id");
                                        String catName = subSideMenuArray.getJSONObject(j).getString("name");

//                                        parentSideMenuArray.add(new sideMenuItem(catID, catName, "",false,false,false));
                                        genres.add(new Genre(catName, null));

                                    }




                                }
                            }

                        }

                        mmAdapter = new GenreAdapter((MainActivity) getActivity(),"",genres, new OnItemClickListener() {
                            @Override
                            public void onItemClick(String title, String catId) {

                                for(int i=0; i < parentSideMenuArray.size(); i++)

                                {
                                    if(parentSideMenuArray.get(i).gettitle() == title)

                                        catId = parentSideMenuArray.get(i).getcategoryID();
                                }

                                if(catId.length() > 0)
                                {


                                    selectCat.sendInput2(title,catId);
                                    dismiss();

                                }

                            }
                        });


                        collectionHomeRecyclerView.setAdapter(mmAdapter);
                    }

                    }
            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onAttach(Context context)
    {

        super.onAttach(context);

        try {

            selectCat = (HomeBottomFragment.selectBottomCollectionMenu) context;
        }

        catch (ClassCastException e){

            throw new ClassCastException(context.toString() + "must implement");

        }

    }
}
