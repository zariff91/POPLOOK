package com.tiseno.poplook;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.Genre;
import com.tiseno.poplook.functions.GenreAdapter;
import com.tiseno.poplook.functions.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BottomNavMyAccFragment extends DialogFragment {

    RecyclerView myAccountNV;
    RecyclerView.LayoutManager mLayoutManager;

    private GenreAdapter mmAdapter;

    private List<Genre> myAccounnList;

    public selectBottomMyAcc selectMyAcc;

    String UserID,SelectedCountryName;

    String SelectedShopID="1";

    TextView side_menu_userTV;


    public BottomNavMyAccFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface selectBottomMyAcc{

        void sendInput3(String data, String data2);


    }

    public static BottomNavMyAccFragment newInstance(String title) {
        BottomNavMyAccFragment frag = new BottomNavMyAccFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_nav_myacc, container, false);

        myAccountNV = (RecyclerView)view.findViewById(R.id.CollectionRV);
        myAccountNV.setHasFixedSize(true);

        side_menu_userTV = (TextView)view.findViewById(R.id.side_menu_userTV);


        mLayoutManager = new LinearLayoutManager(getActivity()); // Creating a layout Manager

        myAccountNV.setLayoutManager(mLayoutManager);

        myAccounnList = new ArrayList<>(1000);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        SelectedCountryName = pref.getString("SelectedCountryName", "");

        SelectedShopID  = pref.getString("SelectedShopID", "1");

        side_menu_userTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));



        List<Artist> artists = new ArrayList<>(4);


        if(UserID.length() > 0) {

            myAccounnList.add(new Genre("Shipping To " + SelectedCountryName + "", null));
            myAccounnList.add(new Genre("Visit Our Store", null));
            myAccounnList.add(new Genre("My Account", null));
            myAccounnList.add(new Genre("Settings", null));
            myAccounnList.add(new Genre("Poplook Loyalty Rewards", null));

            if(SelectedShopID.equals("1"))
            {
                myAccounnList.add(new Genre("My Member ID", null));
            }

            artists.add(new Artist("", "Facebook"));
            artists.add(new Artist("", "Twitter"));
            artists.add(new Artist("", "Instagram"));
            myAccounnList.add(new Genre("Follow Us", artists));
            myAccounnList.add(new Genre("Privacy Policy", null));
            myAccounnList.add(new Genre("Log Out", null));

            side_menu_userTV.setVisibility(View.VISIBLE);

            side_menu_userTV.setText("Hello, " + pref.getString("Name", ""));




        }

        else
        {

            myAccounnList.add(new Genre("Shipping To " + SelectedCountryName + "", null));
            myAccounnList.add(new Genre("Visit Our Store", null));
            myAccounnList.add(new Genre("Settings", null));
            myAccounnList.add(new Genre("Poplook Loyalty Rewards", null));
            artists.add(new Artist("", "Facebook"));
            artists.add(new Artist("", "Twitter"));
            artists.add(new Artist("", "Instagram"));
            myAccounnList.add(new Genre("Follow Us", artists));
            myAccounnList.add(new Genre("Privacy Policy", null));
            myAccounnList.add(new Genre("Login", null));

            side_menu_userTV.setVisibility(View.GONE);

        }


        mmAdapter = new GenreAdapter((MainActivity) getActivity(),"",myAccounnList, new OnItemClickListener() {
            @Override
            public void onItemClick(String title, String catId) {


                selectMyAcc.sendInput3(title,catId);
                dismiss();


            }
        });


        myAccountNV.setAdapter(mmAdapter);

        Window window = getDialog().getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; //psotion
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

    }

    @Override
    public void onAttach(Context context)
    {

        super.onAttach(context);

        try {

            selectMyAcc = (BottomNavMyAccFragment.selectBottomMyAcc)context;
        }

        catch (ClassCastException e){

            throw new ClassCastException(context.toString() + "must implement");

        }

    }

}
