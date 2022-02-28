package com.tiseno.poplook;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tiseno.poplook.functions.Artist;
import com.tiseno.poplook.functions.BottomMenuAdapter;
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

public class BottomMenuFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    RecyclerView bottomMenuRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    private GenreAdapter mmAdapter;
    private List<Genre> genres;

    ArrayList<sideMenuItem> parentSideMenuArray = new ArrayList<sideMenuItem>();

    String SelectedShopID="1";
    String apikey="PL:@KrAk!fA9RpGDcnIfDKzljGkEqW48yU4M6Y2GckgawSVbEg62FHKHBU7awnidFZ4wxVxUdcTAvkxT1GrlVhuZ1dKlqzl9zlsedD66G";

    public BottomMenuFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_dialog_menu,container,false);
        bottomMenuRecyclerView = (RecyclerView)view.findViewById(R.id.menu_bottom);
        mLayoutManager = new LinearLayoutManager(getActivity()); // Creating a layout Manager
        bottomMenuRecyclerView.setLayoutManager(mLayoutManager);

        getSideMenuList();

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
        if(result!=null) {


        }

    }
}
