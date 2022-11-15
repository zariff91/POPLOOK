package com.tiseno.poplook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.tiseno.poplook.functions.MenuPagerAdapter;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;

import org.json.JSONArray;
import org.json.JSONObject;

public class Collection extends Fragment implements AsyncTaskCompleteListener<JSONObject> ,TabLayout.OnTabSelectedListener{

    ViewPager2 viewPager;
    TabLayout tablayout;
    protected RecyclerView menuRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManagerMenu;

    String SelectedShopID="1";
    private JSONArray menuArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);

        ((MainActivity) getActivity()).changeToolBarTextView(false);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(true);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).showBottomBar(true);
        ((MainActivity) getActivity()).hideTopBar(false);

        getMenuBannerList();

        menuRecyclerView = (RecyclerView) rootView.findViewById(R.id.NewMenuRecyclerView);
        mLayoutManagerMenu = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);


        tablayout = rootView.findViewById(R.id.tabs);
        tablayout.addOnTabSelectedListener(this);
        viewPager = rootView.findViewById(R.id.viewpager);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getMenuBannerList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        String action="menus/get_active_mobile_menu/shop/"+SelectedShopID+"/lang/1?apikey="+apikey;
        WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
        callws.execute(action);

        System.out.println("api home banner url "+action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try {

                if (result.getBoolean("status"))
                {
                    menuArray = result.getJSONArray("data");

                    for(int x=0;x<menuArray.length();x++){

                        JSONObject menuObj = menuArray.getJSONObject(x);
                        String menuType = menuObj.getString("name");
                        tablayout.addTab(tablayout.newTab().setText(menuType));

                    }

                    FragmentManager supp = getParentFragmentManager();
                    MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(supp,getLifecycle(),menuArray);
                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setUserInputEnabled(false);
                }
            }
            catch (Exception e){

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
    public void onTabSelected(TabLayout.Tab tab) {

        if(tab.getText().equals("Bestsellers"))
        {
            Fragment fragment = new ListOfProductFragment();

            Bundle bundle = new Bundle();
            bundle.putString("prodID", "45");
            bundle.putString("catName", "Bestsellers");
            bundle.putString("fromHome", "Home");
            fragment.setArguments(bundle);
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else {
            viewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroyView() {

        System.out.println("destroy this fragment");

        menuRecyclerView  = null;
        super.onDestroyView();
    }
}