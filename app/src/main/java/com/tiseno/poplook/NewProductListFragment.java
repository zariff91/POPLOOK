package com.tiseno.poplook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewProductListFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> ,HomeAdapter.ViewHolder.AdapterCallback,SliderImageAdapter.ItemClickListener,NewHomeBannerHorizontalAdapter.ItemClickListener{

    protected RecyclerView bannerRecyclerView;
    protected RecyclerView.LayoutManager bannerLayoutManager;
    protected RecyclerView.Adapter mAdapterCategory;

    protected RecyclerView bannerRecyclerViewHorizontal;
    protected RecyclerView.LayoutManager horizontalBannerLayout;
    protected RecyclerView.Adapter mAdapterHorizontal;

    ArrayList<HomeItem> mImagesCollection= new ArrayList<HomeItem>();
    ArrayList<HomeItem> mImagesCategory= new ArrayList<HomeItem>();
    ArrayList<HomeItem>horizontal_item= new ArrayList<HomeItem>();

    String SelectedShopID="2";
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
            ((MainActivity) getActivity()).changeBtnBackView(false);
            ((MainActivity) getActivity()).changeToolBarImageView(true);
            ((MainActivity) getActivity()).changeBtnSearchView(true);
            ((MainActivity) getActivity()).changeBtnBagView(true);
            ((MainActivity) getActivity()).changeBtnWishlistView(true);
            ((MainActivity) getActivity()).changeBtnCloseXView(false);
            ((MainActivity) getActivity()).setDrawerState(true);
            ((MainActivity) getActivity()).showBottomBar(false);
            ((MainActivity) getActivity()).hideTopBar(false);


            bannerLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            bannerRecyclerView = (RecyclerView) _rootView.findViewById(R.id.HomeBannerRecyclerView);
            bannerRecyclerView.setLayoutManager(bannerLayoutManager);

            getHomeBannerList();

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
                        mImagesCollection.clear();
                        mImagesCategory.clear();
                        horizontal_item.clear();

                        JSONObject jsonObject = result.getJSONObject("data");
                        JSONArray jsonArrPrimary = null;
//                        jsonArrPrimary = jsonObject.getJSONArray("New home destop banners");

                        jsonArrPrimary = jsonObject.names();


                        for (int i = 0; i < jsonArrPrimary.length (); ++i) {

                            String key = jsonArrPrimary.getString(i); // Here's your key
                            JSONObject child_object = jsonObject.getJSONObject(key);

                            String banner_type = child_object.getString("type");

                            if(banner_type.equals("slider")){

                                JSONArray jsonArr = null;
                                jsonArr = child_object.getJSONArray("data");

                                for (int j = 0; j < jsonArr.length(); j++) {

                                    String categori_id = jsonArr.getJSONObject(j).getString("category_id");
                                    String category_name = jsonArr.getJSONObject(j).getString("category_name");
                                    String link = jsonArr.getJSONObject(j).getString("link");
                                    String href = jsonArr.getJSONObject(j).getString("href");
                                    String position = jsonArr.getJSONObject(j).getString("position");

                                    mImagesCollection.add(new HomeItem(categori_id, category_name, link, href, position));

                                }
                            }

                           else if(banner_type.equals("horizontal")){

                                JSONArray jsonArr = null;
                                jsonArr = child_object.getJSONArray("data");

                                for (int y = 0; y < jsonArr.length (); ++y) {

                                    String categori_id = jsonArr.getJSONObject(y).getString("category_id");
                                    String category_name = jsonArr.getJSONObject(y).getString("category_name");
                                    String link = jsonArr.getJSONObject(y).getString("link");
                                    String href = jsonArr.getJSONObject(y).getString("href");
                                    String position = jsonArr.getJSONObject(y).getString("position");

                                    horizontal_item.add(new HomeItem(categori_id, category_name, link, href, position));

                                }
                            }

                           else if(banner_type.equals("vertical")){
                                JSONArray jsonArr = null;
                                jsonArr = child_object.getJSONArray("data");

                                for (int y = 0; y < jsonArr.length (); ++y) {

                                    String catIDPrimary = jsonArr.getJSONObject(y).getString("category_id");
                                    String catNamePrimary = jsonArr.getJSONObject(y).getString("category_name");
                                    String linkPrimary = jsonArr.getJSONObject(y).getString("link");
                                    String hrefPrimary = jsonArr.getJSONObject(y).getString("href");
                                    mImagesCategory.add(new HomeItem(catIDPrimary, catNamePrimary, linkPrimary, hrefPrimary,""));


                                }
                            }

                           else {
                                JSONArray jsonArr = null;
                                jsonArr = child_object.getJSONArray("data");

                                for (int y = 0; y < jsonArr.length (); ++y) {

                                    String catIDPrimary = jsonArr.getJSONObject(y).getString("category_id");
                                    String catNamePrimary = jsonArr.getJSONObject(y).getString("category_name");
                                    String linkPrimary = jsonArr.getJSONObject(y).getString("link");
                                    String hrefPrimary = jsonArr.getJSONObject(y).getString("href");
                                    mImagesCategory.add(new HomeItem(catIDPrimary, catNamePrimary, linkPrimary, hrefPrimary,""));

                                }
                            }

                        }

                        getHomeBannerVideoList();

                    }

                    if(result.getString("action").equals("banners_video"))
                    {

                        Object videoDataa = result.get("data");

                        if(videoDataa instanceof JSONArray)
                        {

                            JSONArray jsonObject = result.getJSONArray("data");
                            JSONObject videoData = jsonObject.getJSONObject(0);

                            String catIDPrimary = videoData.getString("category_id");
                            String catNamePrimary = videoData.getString("category_name");
                            String linkPrimary = videoData.getString("link");
                            String hrefPrimary = videoData.getString("href");
                            String position = videoData.getString("position");

                            int x = Integer.parseInt(position)-1;
                            mImagesCategory.add(x,new HomeItem(catIDPrimary, catNamePrimary, "isVideo", hrefPrimary, position));


                        }

                    }

                    mAdapterCategory = new HomeAdapter(mImagesCategory,getActivity(),false,this);

                    LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                    LinearLayout header = (LinearLayout) inflater1.inflate(R.layout.new_home_banner_header, bannerRecyclerView, false);
                    SliderView sliderView = (SliderView) header.findViewById(R.id.imageSlider);

                    SliderImageAdapter sliderAdapter = new SliderImageAdapter(getActivity(),mImagesCollection);
                    sliderAdapter.addItemClickListener(this);

                    sliderView.setSliderAdapter(sliderAdapter);
                    sliderView.startAutoCycle();

                    bannerRecyclerViewHorizontal = (RecyclerView)header.findViewById(R.id.HomeBannerHorizontalRecyclerView);
                    horizontalBannerLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
                    bannerRecyclerViewHorizontal.setLayoutManager(horizontalBannerLayout);

                    NewHomeBannerHorizontalAdapter hori = new NewHomeBannerHorizontalAdapter(getActivity(),horizontal_item);
                    hori.addItemClickListener(this);

                    mAdapterHorizontal = hori;

                    bannerRecyclerViewHorizontal.setAdapter(mAdapterHorizontal);


                    Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(mAdapterCategory);
                    mBookends.addHeader(header);
                    bannerRecyclerView.setAdapter(mBookends);

                    if(mImagesCollection.size() == 0){
                        sliderView.setVisibility(View.GONE);
                    }
                    if(horizontal_item.size() == 0){
                        bannerRecyclerViewHorizontal.setVisibility(View.GONE);
                    }

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
    public void onMethodCallback(String bannerID, String href, String categoryName, String link) {

        if(link.contains("http")){

//                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mImagesCategory.get(position).getlink()));
//                                        startActivity(browserIntent);
//                                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                                            CustomTabsIntent customTabsIntent = builder.build();
//                                            customTabsIntent.launchUrl(getActivity(), Uri.parse(mImagesCategory.get(position).getlink()));


            String linkBrowser = "https://poplook.com"+link;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkBrowser));
            startActivity(browserIntent);


        } else {

            Fragment fragment = new ListOfProductFragment();

            Bundle bundle = new Bundle();
            bundle.putString("prodID", bannerID);
            bundle.putString("catName", categoryName);
            bundle.putString("fromHome", "Home");
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onItemSliderClick(int position) {

        Fragment fragment = new ListOfProductFragment();

        Bundle bundle = new Bundle();
        bundle.putString("prodID", mImagesCollection.get(position).getcategoryID());
        bundle.putString("catName", mImagesCollection.get(position).getcatName());
        bundle.putString("fromHome","Home");
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mImagesCategory.clear();

    }

    @Override
    public void onItemHorizontalClick(int position) {

            Fragment fragment = new ListOfProductFragment();

            Bundle bundle = new Bundle();
            bundle.putString("prodID",horizontal_item.get(position).getcategoryID());
            bundle.putString("catName", horizontal_item.get(position).getcatName());
            bundle.putString("fromHome","Home");
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mImagesCategory.clear();

    }

}
