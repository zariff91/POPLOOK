package com.tiseno.poplook;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.StoreCreditAdapter;
import com.tiseno.poplook.functions.storeCreditItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreCreditFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;
    String UserID,LanguageID;
    ArrayList<storeCreditItem> listArray2 = new ArrayList<storeCreditItem>();

    public StoreCreditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_store_credit, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Store Credit");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.storeCreditRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        LanguageID = pref.getString("LanguageID", "");

        getStoreCredit();
        return v;
    }

    private void getStoreCredit()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="StoreCredits/list/customer/"+UserID+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                boolean status = result.getBoolean("status");
                String Action = result.getString("action");
                if(Action.equals("StoreCredits_list"))
                {
                    if(status)
                    {
                        JSONArray jsonArr2 = new JSONArray();

                        JSONObject data=result.getJSONObject("data");
                        jsonArr2 = data.getJSONArray("lists");
                        listArray2.clear();
                        for (int j = 0; j < jsonArr2.length(); j++) {
                            JSONObject jObj = jsonArr2.getJSONObject(j);
                            JSONArray jsonArr3 = new JSONArray();

                            try {
                                String code = jObj.getString("code");
                                String date_from = parseDate(jObj.getString("date_from"));
                                String date_to = parseDate(jObj.getString("date_to"));
                                String reduction_amount = jObj.getString("reduction_amount");
                                String original_amount = jObj.getString("total_amount");

                                String date = date_from + " - " + date_to;

                                listArray2.add(new storeCreditItem(code, date, reduction_amount, original_amount));
                            } catch (Exception e) {
                                System.out.println("errorrrrr " + e);
                            }
                        }


                    }
                    RVAdapter = new StoreCreditAdapter(getActivity(),listArray2);
                    // Make Bookends
                    Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

                    // Inflate footer view
                    LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                    LinearLayout header = (LinearLayout) inflater1.inflate(R.layout.store_credit_header, mRecyclerView, false);
                    TextView step1TV = (TextView)header.findViewById(R.id.step1TV);
                    TextView step2TV = (TextView)header.findViewById(R.id.step2TV);

                    step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


                    mBookends.addHeader(header);
                    mRecyclerView.setAdapter(mBookends);
                }

            }
            catch (Exception e){
                System.out.println("errorrrr "+e);

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
    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
