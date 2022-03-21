package com.tiseno.poplook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiseno.poplook.functions.HomeAdapter;
import com.tiseno.poplook.functions.HomeItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGetWithoutLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FirstSampleFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>, HomeAdapter.ViewHolder.AdapterCallback {
    protected RecyclerView mainRV;
    protected RecyclerView.Adapter mainRvAdapter;
    protected RecyclerView.LayoutManager mainLayoutManager;

    ArrayList<HomeItem> mImagesCategory= new ArrayList<HomeItem>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_first_sample, container, false);
        mainRV = (RecyclerView) rootView.findViewById(R.id.homeRV);

        mainLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mainRV.setLayoutManager(mainLayoutManager);

        getHomeBannerList();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getHomeBannerList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="banners/mobile/shop/1/lang/1?apikey="+apikey;
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
                    if (result.getString("action").equals("banners_mobile"))
                    {
                        mImagesCategory.clear();

                        JSONObject jsonObject = result.getJSONObject("data");
                        JSONArray jsonArrPrimary = null;
//                        jsonArrPrimary = jsonObject.getJSONArray("New home destop banners");

                        jsonArrPrimary = jsonObject.names();


                        for (int i = 0; i < jsonArrPrimary.length (); ++i) {

                            String key = jsonArrPrimary.getString(i); // Here's your key
                            JSONArray jsonArr = null;

                            jsonArr = jsonObject.getJSONArray(key);


                            for (int y = 0; y < jsonArr.length (); ++y) {

                                String catIDPrimary = jsonArr.getJSONObject(y).getString("category_id");
                                String catNamePrimary = jsonArr.getJSONObject(y).getString("category_name");
                                String linkPrimary = jsonArr.getJSONObject(y).getString("link");
                                String hrefPrimary = jsonArr.getJSONObject(y).getString("href");

                                mImagesCategory.add(new HomeItem(catIDPrimary, catNamePrimary, linkPrimary, hrefPrimary,""));


                            }
                        }
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


                    mainRvAdapter = new HomeAdapter(mImagesCategory,getActivity(),false,this);

                    System.out.println("get banners amount " + mImagesCategory.size());

                    mainRV.setAdapter(mainRvAdapter);

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

    }
}