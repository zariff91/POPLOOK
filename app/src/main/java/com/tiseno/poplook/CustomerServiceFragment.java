package com.tiseno.poplook;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;

import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.customerServiceItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerServiceFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    public CustomerServiceFragment() {
        // Required empty public constructor
    }

    LinearLayout CSContactUsLL, CSRegistrationLL, CSMakingAnOrderLL, CSGSTLL, CSBackorderLL,CSLPSCLL,CSReturnsLL,CSLocalDevLL,CSInterDevLL,CSGiftLL, CSPoplookRewards;
    TextView CSContactUsTV, CSRegistrationTV, CSMakingAnOrderTV, CSGSTTV, CSBackorderTV,CSLPSCTV,CSReturnsTV,CSLocalDevTV,CSInterDevTV,CSGiftTV, CSPoplookTV;
    ArrayList<customerServiceItem> arrayList= new ArrayList<customerServiceItem>();
    String title,content,fromHome="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView= inflater.inflate(R.layout.fragment_customer, container, false);
        try {
            Bundle bundle = this.getArguments();
            fromHome = bundle.getString("fromHome");
        }catch (Exception e){
            fromHome="";
        }

        ((MainActivity) getActivity()).changeToolBarText("Customer Service (FAQ)");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        getCS();

        CSContactUsLL = (LinearLayout) contentView.findViewById(R.id.CSContactUsLL);
        CSRegistrationLL = (LinearLayout) contentView.findViewById(R.id.CSRegistrationLL);
        CSMakingAnOrderLL = (LinearLayout) contentView.findViewById(R.id.CSMakingAnOrderLL);
        CSGSTLL = (LinearLayout) contentView.findViewById(R.id.CSGSTLL);
        CSBackorderLL = (LinearLayout) contentView.findViewById(R.id.CSBackorderLL);
        CSLPSCLL = (LinearLayout) contentView.findViewById(R.id.CSLPSCLL);
        CSReturnsLL = (LinearLayout) contentView.findViewById(R.id.CSReturnsLL);
        CSLocalDevLL = (LinearLayout) contentView.findViewById(R.id.CSLocalDevLL);
        CSInterDevLL = (LinearLayout) contentView.findViewById(R.id.CSInterDevLL);
        CSGiftLL = (LinearLayout) contentView.findViewById(R.id.CSGiftLL);
        CSPoplookRewards = (LinearLayout) contentView.findViewById(R.id.CSPoplookReward);

        CSContactUsTV = (TextView) contentView.findViewById(R.id.CSContactUsTV);
        CSRegistrationTV = (TextView) contentView.findViewById(R.id.CSRegistrationTV);
        CSMakingAnOrderTV = (TextView) contentView.findViewById(R.id.CSMakingAnOrderTV);
        CSGSTTV = (TextView) contentView.findViewById(R.id.CSGSTTV);
        CSBackorderTV = (TextView) contentView.findViewById(R.id.CSBackorderTV);
        CSLPSCTV = (TextView) contentView.findViewById(R.id.CSLPSCTV);
        CSReturnsTV = (TextView) contentView.findViewById(R.id.CSReturnsTV);
        CSLocalDevTV = (TextView) contentView.findViewById(R.id.CSLocalDevTV);
        CSInterDevTV = (TextView) contentView.findViewById(R.id.CSInterDevTV);
        CSGiftTV = (TextView) contentView.findViewById(R.id.CSGiftTV);
        CSPoplookTV = (TextView) contentView.findViewById(R.id.CSPoplookRewardTV);

        CSContactUsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSRegistrationTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSMakingAnOrderTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSGSTTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSBackorderTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSLPSCTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSReturnsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSLocalDevTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSInterDevTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSGiftTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        CSPoplookTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));


        CSContactUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ContactUsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromInstore", "0");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        CSPoplookRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Poplook Rewards")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }

                System.out.println("sinini1" + title);
                System.out.println("sinini2" + content);

                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        CSRegistrationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Registration")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        CSMakingAnOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Making An Order")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        CSGSTLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Goods and Services Tax (GST)")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        CSBackorderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Backorders/Restockable Items")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        CSLPSCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Loyalty Points/Store Credit Codes")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        CSReturnsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Returns")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        CSLocalDevLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Local Delivery (Within Malaysia)")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        CSInterDevLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {
                    if(arrayList.get(j).getcategoryID().equals("International Delivery (Worldwide)")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        CSGiftLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j < arrayList.size(); j++)
                {

                    if(arrayList.get(j).getcategoryID().equals("Gift")){

                        title=arrayList.get(j).getcategoryID();
                        content=arrayList.get(j).getlink();
                        break;
                    }

                }
                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content",content);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return contentView;
    }

    private void getCS(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String SelectedShopID=pref.getString("SelectedShopID","1");
        String action="Infos/contactus_pages/lang/1/shop/"+SelectedShopID+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try {
                if (result.getBoolean("status"))
                {
                    if (result.getString("action").equals("Infos_contactus_pages")) {
                        arrayList.clear();
                        JSONArray jsonArr = null;
                        jsonArr = result.getJSONArray("data");

                        for (int j = 0; j < jsonArr.length(); j++) {
                            String catID = jsonArr.getJSONObject(j).getString("meta_title");
                            String link = jsonArr.getJSONObject(j).getString("content");

                            arrayList.add(new customerServiceItem(catID, link));


                        }

                    }
                    if(!fromHome.equalsIgnoreCase("")){
                        CSInterDevLL.performClick();
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

}
