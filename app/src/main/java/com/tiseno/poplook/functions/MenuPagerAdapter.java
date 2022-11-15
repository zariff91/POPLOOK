package com.tiseno.poplook.functions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tiseno.poplook.NewMenuBannerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuPagerAdapter extends FragmentStateAdapter {

    JSONArray menuArr;
    String type = null;
    JSONArray children = null;

    public MenuPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, JSONArray menuArrayObject) {
        super(fragmentManager, lifecycle);
        menuArr = menuArrayObject;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            JSONObject menuObj = menuArr.getJSONObject(position);
            type = menuObj.getString("type");
            children = menuObj.getJSONArray("children");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(type.equals("tab")){

            NewMenuBannerList multipleBannerType = new NewMenuBannerList();
            multipleBannerType.bannerJSONArray = children;

            return multipleBannerType;
        }
        else
        {
            return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return menuArr.length();
    }
}
