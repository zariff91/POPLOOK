package com.tiseno.poplook;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.Gravity;


import com.tiseno.poplook.functions.Artist;
import com.tiseno.poplook.functions.Genre;
import com.tiseno.poplook.functions.GenreAdapter;
import com.tiseno.poplook.functions.OnItemClickListener;
import com.tiseno.poplook.functions.sideMenuItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import android.content.Context;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BottomNavCollectionFragment extends DialogFragment implements AsyncTaskCompleteListener<JSONObject> {

    String SelectedShopID="1";
    String apikey="PL:@KrAk!fA9RpGDcnIfDKzljGkEqW48yU4M6Y2GckgawSVbEg62FHKHBU7awnidFZ4wxVxUdcTAvkxT1GrlVhuZ1dKlqzl9zlsedD66G";

    String selectedCategory = "";

    RecyclerView collectionRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
// Declaring RecyclerView


    private GenreAdapter mmAdapter;
    private List<Genre>genres;

    ArrayList<sideMenuItem> parentSideMenuArray = new ArrayList<sideMenuItem>();

    public selectedBottomCategoryMenu selectMethod;



    public BottomNavCollectionFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface selectedBottomCategoryMenu{

        void sendInput(String data, String data2);


    }

    public static BottomNavCollectionFragment newInstance(String title) {
        BottomNavCollectionFragment frag = new BottomNavCollectionFragment();
        Bundle args = new Bundle();
        args.putString("selectedCat", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_nav_collection, container, false);

        collectionRecyclerView = (RecyclerView)view.findViewById(R.id.CollectionRV);
        collectionRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity()); // Creating a layout Manager

        collectionRecyclerView.setLayoutManager(mLayoutManager);

        getSideMenuList();

        Window window = getDialog().getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; //psotion
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // fuill screen
        lp.height = 80;
        window.setAttributes(lp);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        Bundle args = getArguments();
        selectedCategory = args.getString("selectedCat");

    }

    public void getSideMenuList(){

//        String action="Menus/mobile?apikey="+apikey+"&shop="+SelectedShopID+"&lang=1";
        String action="Menus/webcategories/lang/1/shop/"+SelectedShopID+"?apikey="+apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

        genres = new ArrayList<>(1000);

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        if(result!=null) {

            try {

                if (result.getString("action").equals("Menus_webcategories")) {

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

                                for (int j = 0; j < subSideMenuArray.length(); j++) {
                                    String catID = subSideMenuArray.getJSONObject(j).getString("id");
                                    String catName = subSideMenuArray.getJSONObject(j).getString("name");

                                    artists.add(new Artist(catID, catName));


                                }

                                genres.add(new Genre(sideMenuTitle, artists));

                            } else {
                                String catID = Titles.getJSONObject(i).getString("id");
                                String catName = Titles.getJSONObject(i).getString("name");

                                genres.add(new Genre(catName, null));

                                parentSideMenuArray.add(new sideMenuItem(sideMenuCatID, sideMenuTitle, "",false,false,false));



                            }



                        }

                    }

                    mmAdapter = new GenreAdapter((MainActivity) getActivity(),selectedCategory,genres, new OnItemClickListener() {
                        @Override
                        public void onItemClick(String title, String catId) {

                            for(int i=0; i < parentSideMenuArray.size(); i++)

                            {
                                if(parentSideMenuArray.get(i).gettitle() == title)

                                catId = parentSideMenuArray.get(i).getcategoryID();
                            }

                            if(catId.length() > 0)
            {


                selectMethod.sendInput(title,catId);

                dismiss();

            }

                        }
                    });


                    collectionRecyclerView.setAdapter(mmAdapter);


                }
            }
            catch (Exception e){

            }
        }


    }

    @Override
    public void onAttach(Context context)
    {

        super.onAttach(context);

        try {

            selectMethod = (selectedBottomCategoryMenu) context;
        }

        catch (ClassCastException e){

            throw new ClassCastException(context.toString() + "must implement");

        }

    }

}
