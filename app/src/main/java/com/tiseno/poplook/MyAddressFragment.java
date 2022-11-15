package com.tiseno.poplook;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.myAddressAdapter;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAddressFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    String UserID, LanguageID;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    LinearLayout myaddressNoAddressLL;
    ImageButton myaddressAddAddressBtnIV;
    TextView TV,noAddLabel;
    ArrayList<addressItem> listArray_address = new ArrayList<addressItem>();

    public MyAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_my_address, container, false);

        ((MainActivity) getActivity()).changeToolBarText("My Addresses");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        LanguageID = pref.getString("LanguageID", "");
        TV = (TextView) contentView.findViewById(R.id.TV);
        noAddLabel = (TextView) contentView.findViewById(R.id.noAddLabel);
        myaddressNoAddressLL = (LinearLayout) contentView.findViewById(R.id.myaddressNoAddressLL);
        myaddressAddAddressBtnIV = (ImageButton) contentView.findViewById(R.id.myaddressAddAddressBtnIV);

        TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        noAddLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.myAddressRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getAddressList();

        myaddressAddAddressBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAdressFragment fragment = new AddAdressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("COME_FROM_WHERE", 0);
                bundle.putBoolean("EDIT_ADDRESS", false);
                fragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragmentContainer, fragment, fragment.toString());
                ft.addToBackStack(null);
                ft.commit();
            }
        });



        return contentView;
    }

    public void getAddressList()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Addresses/customer/id/"+UserID+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                String action = result.getString("action");

                if(action.equals("Addresses_customer"))
                {
                    if(result.getBoolean("status"))
                    {
                        listArray_address.clear();
                        String stateID="",address2="",company="",addressState="";
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = result.getJSONArray("data");

                        for(int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jObj = jsonArr.getJSONObject(i);
                            String addressID = jObj.getString("id_address");
                            String addressFirstName = jObj.getString("firstname");
                            String addressLastName = jObj.getString("lastname");
                            String id_gender = jObj.getString("id_gender");
                            if (jObj.has("company")) {
                                company = jObj.getString("company");
                            }
                            String address1 = jObj.getString("address1");
                            address1=address1.replace("\\","");
                            if (jObj.has("address2")) {
                                address2 = jObj.getString("address2");
                            }
                            address2=address2.replace("\\","");
                            String addressPostCode = jObj.getString("postcode");
                            String addressCity = jObj.getString("city");
                            String countryID = jObj.getString("id_country");
                            String addressCountry = jObj.getString("country_name");
                            if (jObj.has("id_state")) {
                                stateID = jObj.getString("id_state");
                            }
                            if (jObj.has("state_name")) {
                                addressState = jObj.getString("state_name");
                            }
                            String addressPhone = jObj.getString("phone");

                            listArray_address.add(new addressItem(addressID,addressFirstName,addressLastName,address1,address2,addressPostCode,addressCity,countryID,addressCountry,stateID,addressState,addressPhone,company,id_gender));

                        }

                        RVAdapter = new myAddressAdapter(getActivity(),listArray_address,MyAddressFragment.this);
                        // Make Bookends
                        Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

                        // Inflate footer view
                        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                        RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.my_address_header_view, mRecyclerView, false);
                        TextView step1TV = (TextView)header.findViewById(R.id.step1TV);
                        TextView step2TV = (TextView)header.findViewById(R.id.step2TV);

                        step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                        step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


                        mBookends.addHeader(header);
                        mRecyclerView.setAdapter(mBookends);


                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                        myaddressNoAddressLL.setVisibility(View.VISIBLE);
                    }
                }

            }
            catch (Exception e){
                //No address available
                mRecyclerView.setVisibility(View.GONE);
                myaddressNoAddressLL.setVisibility(View.VISIBLE);
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
