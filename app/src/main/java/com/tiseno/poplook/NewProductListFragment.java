package com.tiseno.poplook;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.SliderView;
import com.tiseno.poplook.functions.Artist;
import com.tiseno.poplook.functions.BottomMenuAdapter;
import com.tiseno.poplook.functions.ChildSideMenuItem;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.Genre;
import com.tiseno.poplook.functions.GenreAdapter;
import com.tiseno.poplook.functions.HomeAdapter;
import com.tiseno.poplook.functions.HomeItem;
import com.tiseno.poplook.functions.NewHomeBannerHorizontalAdapter;
import com.tiseno.poplook.functions.OnItemClickListener;
import com.tiseno.poplook.functions.RecyclerItemClickListener;
import com.tiseno.poplook.functions.SliderImageAdapter;
import com.tiseno.poplook.functions.sideMenuItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewProductListFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> ,SliderImageAdapter.ItemClickListener,HomeAdapter.AdapterHomeCallback{

    protected RecyclerView bannerRecyclerView;
    protected RecyclerView.LayoutManager bannerLayoutManager;
    protected RecyclerView.Adapter mAdapterCategory;

//    protected RecyclerView bannerRecyclerViewHorizontal;
//    protected RecyclerView.LayoutManager horizontalBannerLayout;
//    protected RecyclerView.Adapter mAdapterHorizontal;

    ArrayList<HomeItem> mImagesCollection= new ArrayList<HomeItem>();
    ArrayList<HomeItem> mImagesCategory= new ArrayList<HomeItem>();
    ArrayList<HomeItem>horizontal_item= new ArrayList<HomeItem>();
    ArrayList<String>parentTitle= new ArrayList<>();


    JSONObject videoDataa = null;
    JSONObject jsonObjectBanner = null;

    String SelectedShopID="1";
    String apikey="PL:@KrAk!fA9RpGDcnIfDKzljGkEqW48yU4M6Y2GckgawSVbEg62FHKHBU7awnidFZ4wxVxUdcTAvkxT1GrlVhuZ1dKlqzl9zlsedD66G";

    View _rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (_rootView != null) {

            ((MainActivity) getActivity()).changeToolBarImageView(true);
            ((MainActivity) getActivity()).changeToolBarTextView(false);
            return _rootView;
        }

        else {
            _rootView = inflater.inflate(R.layout.new_product_list_layout, container, false);

            ((MainActivity) getActivity()).changeToolBarTextView(false);
            ((MainActivity) getActivity()).changeBtnBackView(true);
            ((MainActivity) getActivity()).changeToolBarImageView(true);
            ((MainActivity) getActivity()).changeBtnSearchView(true);
            ((MainActivity) getActivity()).changeBtnBagView(true);
            ((MainActivity) getActivity()).changeBtnWishlistView(true);
            ((MainActivity) getActivity()).changeBtnCloseXView(false);
            ((MainActivity) getActivity()).setDrawerState(true);
            ((MainActivity) getActivity()).showBottomBar(false);
            ((MainActivity) getActivity()).hideTopBar(false);


            bannerLayoutManager = new LinearLayoutManager(getActivity());

            bannerRecyclerView = (RecyclerView) _rootView.findViewById(R.id.HomeBannerRecyclerView);
            bannerRecyclerView.setLayoutManager(bannerLayoutManager);

            getHomeBannerList();
            getHomeBannerVideoList();

            return _rootView;
        }
    }

    private void getHomeBannerVideoList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="banners/video/1?apikey="+apikey;
        WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
        callws.execute(action);

        System.out.println("api home banner url "+action);


    }

    private void getHomeBannerList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        String action="banners/mobilev2/shop/"+SelectedShopID+"/lang/1?apikey="+apikey;
        WebServiceAccessGetWithoutLoading callws = new WebServiceAccessGetWithoutLoading(getActivity(), this);
        callws.execute(action);

        System.out.println("api home banner url "+action);


    }

    @Override
    public void onTaskComplete(JSONObject result) {
        System.out.println("Get JSON Response : " + result);


        if(result!=null){
            try {

                if (result.getBoolean("status"))
                {

                    if (result.getString("action").equals("banners_mobilev2"))
                    {
                        jsonObjectBanner = result.getJSONObject("data");
                    }
                    if(result.getString("action").equals("banners_video"))
                    {

                        videoDataa = result;

                        JSONArray dataVideo = null;
                        dataVideo = videoDataa.getJSONArray("data");
                        String position = "0";

                        if(dataVideo instanceof JSONArray)
                        {
                            JSONArray jsonObject = result.getJSONArray("data");
                            JSONObject videoData = jsonObject.getJSONObject(0);
//
//                            String catIDPrimary = videoData.getString("category_id");
//                            String catNamePrimary = videoData.getString("category_name");
//                            String linkPrimary = videoData.getString("link");
//                            String hrefPrimary = videoData.getString("href");

                           position = videoData.getString("position");
//
                        }

                        JSONArray jsonArrPrimary = null;
                        jsonArrPrimary = jsonObjectBanner.names();
                        int y = Integer.parseInt(position)-1;

                        for(int x=0;x<jsonArrPrimary.length();x++)
                        {
                            parentTitle.add(jsonArrPrimary.getString(x));
                        }
                        parentTitle.add(y,"isVideo");
                        mAdapterCategory = new HomeAdapter(jsonObjectBanner,parentTitle,videoDataa,getActivity(),false,this);

                    }

                    LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                    LinearLayout header = (LinearLayout) inflater1.inflate(R.layout.new_home_banner_header, bannerRecyclerView, false);
                    SliderView sliderView = (SliderView) header.findViewById(R.id.imageSlider);

                    SliderImageAdapter sliderAdapter = new SliderImageAdapter(getActivity(),mImagesCollection);
                    sliderAdapter.addItemClickListener(this);

                    sliderView.setSliderAdapter(sliderAdapter);
                    sliderView.startAutoCycle();

//                    bannerRecyclerViewHorizontal = (RecyclerView)header.findViewById(R.id.HomeBannerHorizontalRecyclerView);
//                    horizontalBannerLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
//                    bannerRecyclerViewHorizontal.setLayoutManager(horizontalBannerLayout);

//                    NewHomeBannerHorizontalAdapter hori = new NewHomeBannerHorizontalAdapter(getActivity(),horizontal_item);
//                    hori.addItemClickListener(this);
//
//                    mAdapterHorizontal = hori;
//
//                    bannerRecyclerViewHorizontal.setAdapter(mAdapterHorizontal);


                    Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(mAdapterCategory);
                    mBookends.addHeader(header);
                    bannerRecyclerView.setAdapter(mAdapterCategory);

                    if(mImagesCollection.size() == 0){
                        sliderView.setVisibility(View.GONE);
                    }
//                    if(horizontal_item.size() == 0){
//                        bannerRecyclerViewHorizontal.setVisibility(View.GONE);
//                    }

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

//    @Override
//    public void onMethodCallback(String bannerID, String href, String categoryName, String link) {
//
//        if(link.contains("http")){
//
////                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mImagesCategory.get(position).getlink()));
////                                        startActivity(browserIntent);
////                                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
////                                            CustomTabsIntent customTabsIntent = builder.build();
////                                            customTabsIntent.launchUrl(getActivity(), Uri.parse(mImagesCategory.get(position).getlink()));
//
//
//            String linkBrowser = "https://poplook.com"+link;
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkBrowser));
//            startActivity(browserIntent);
//
//
//        } else {
//
//            Fragment fragment = new ListOfProductFragment();
//
//            Bundle bundle = new Bundle();
//            bundle.putString("prodID", bannerID);
//            bundle.putString("catName", categoryName);
//            bundle.putString("fromHome", "Home");
//            fragment.setArguments(bundle);
//            FragmentManager fragmentManager = getActivity().getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
//    }


    @Override
    public void onItemSliderClick(int position) {

        Fragment fragment = new ListOfProductFragment();

        Bundle bundle = new Bundle();
        bundle.putString("prodID", mImagesCollection.get(position).getcategoryID());
        bundle.putString("catName", mImagesCollection.get(position).getcatName());
        bundle.putString("fromHome","Home");
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mImagesCategory.clear();

    }

    @Override
    public void onBannerClickPosition(int position, String parentName) {

        if(parentName.equals("isVideo")){

            JSONArray dataVideo = null;
            try {
                dataVideo = videoDataa.getJSONArray("data");

                if(dataVideo instanceof JSONArray)
                {
                    JSONArray jsonObject = videoDataa.getJSONArray("data");
                    JSONObject videoData = jsonObject.getJSONObject(0);
//
                            String catIDPrimary = videoData.getString("category_id");
                            String catNamePrimary = videoData.getString("category_name");
                            String linkPrimary = videoData.getString("link");
                            String hrefPrimary = videoData.getString("href");

                    Fragment fragment = new ListOfProductFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("prodID", catIDPrimary);
                    bundle.putString("catName", catNamePrimary);
                    bundle.putString("fromHome","Home");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

//
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else {
            JSONObject child_object = null;
            try {
                child_object = jsonObjectBanner.getJSONObject(parentName);
                JSONArray jsonArr = null;
                jsonArr = child_object.getJSONArray("data");

                String catIDPrimary = jsonArr.getJSONObject(position).getString("category_id");
                String catNamePrimary = jsonArr.getJSONObject(position).getString("category_name");
                String link = jsonArr.getJSONObject(position).getString("link");

                if(link.contains("http")){
//
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mImagesCategory.get(position).getlink()));
//                    startActivity(browserIntent);
//                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                    CustomTabsIntent customTabsIntent = builder.build();
//                    customTabsIntent.launchUrl(getActivity(), Uri.parse(mImagesCategory.get(position).getlink()));

                    String linkBrowser = "https://poplook.com"+link;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkBrowser));
                    startActivity(browserIntent);
                }
                else {
                    Fragment fragment = new ListOfProductFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("prodID", catIDPrimary);
                    bundle.putString("catName", catNamePrimary);
                    bundle.putString("fromHome","Home");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
