package com.tiseno.poplook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

public class NewMenuBannerList extends Fragment implements MenuBannerAdapter.AdapterMenuCallback {

    public JSONArray bannerJSONArray;

    View rootView;

    protected RecyclerView bannerRecyclerView;
    protected RecyclerView.LayoutManager bannerLayoutManager;
    protected RecyclerView.Adapter mAdapterCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView != null){

            return rootView;
        }

        else
        {

            rootView = inflater.inflate(R.layout.fragment_new_menu_banner_list, container, false);

            bannerLayoutManager = new LinearLayoutManager(getActivity());

            bannerRecyclerView = (RecyclerView) rootView.findViewById(R.id.MenuBannerRecyclerView);
            bannerRecyclerView.setLayoutManager(bannerLayoutManager);
//
            mAdapterCategory = new MenuBannerAdapter(getContext(),bannerJSONArray,this);
            bannerRecyclerView.setAdapter(mAdapterCategory);

//            getHomeBannerVideoList();
            return rootView;
        }
    }

    @Override
    public void onBannerHorizontalClickPosition(String bannerID, String bannerName) {
        Fragment fragment = new ListOfProductFragment();
//
        if (bannerID.equals("null") || bannerName.equals("null")) {

        } else {
            Bundle bundle = new Bundle();
            bundle.putString("prodID", bannerID);
            bundle.putString("catName", bannerName);
            bundle.putString("fromHome", "Home");
            fragment.setArguments(bundle);
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}