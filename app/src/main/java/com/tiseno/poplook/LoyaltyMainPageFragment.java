package com.tiseno.poplook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoyaltyMainPageFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    TextView salesTV,salesTV_Below ,rayaTV,rayaTV_Below ,giftTV,giftTV_Below ,mysteryTV,mysteryTV_Below ,inviteTV,inviteTV_Below ,excTV,excTV_Below ,bdayTV,bdayTV_Below;

    TextView lookFwd;

    Button joinInBtn, signInBtn, faqBtn;

    ImageView barcodeImg;

    String UserID;
    String SelectedShopID;
    String linkFAQ;

    ImageView circle_main;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.loyalty_mainpage, container, false);

        ((MainActivity) getActivity()).changeToolBarText("POPLOOK Rewards");
        ((MainActivity) getActivity()).changeBtnBackView(true);


        joinInBtn = (Button)view.findViewById(R.id.joinBtn);
        signInBtn = (Button)view.findViewById(R.id.signInBtnn);
        joinInBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        signInBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        faqBtn = (Button)view.findViewById(R.id.FAQBtn);
        faqBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        salesTV = (TextView)view.findViewById(R.id.earlyAccessTxtView);
        salesTV_Below = (TextView)view.findViewById(R.id.earlyAccessTxtViewBelow);
        salesTV_Below.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        salesTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        rayaTV = (TextView)view.findViewById(R.id.earlyAccessSalesTxtView);
        rayaTV_Below = (TextView)view.findViewById(R.id.earlyAccessSalesTxtViewBelow);
        rayaTV_Below.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        rayaTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        giftTV = (TextView)view.findViewById(R.id.giftTxtView);
        giftTV_Below = (TextView)view.findViewById(R.id.giftTxtViewBelow);
        giftTV_Below.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        giftTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        mysteryTV = (TextView)view.findViewById(R.id.mysteryTxtView);
        mysteryTV_Below = (TextView)view.findViewById(R.id.mysteryTxtViewBelow);
        mysteryTV_Below.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        mysteryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        inviteTV = (TextView)view.findViewById(R.id.invitesTxtView);
        inviteTV_Below = (TextView)view.findViewById(R.id.invitesTxtViewBelow);
        inviteTV_Below.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        inviteTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        excTV = (TextView)view.findViewById(R.id.excPromoTxtView);
        excTV_Below = (TextView)view.findViewById(R.id.excPromoTxtViewBelow);
        excTV_Below.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        excTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        bdayTV = (TextView)view.findViewById(R.id.bdayTxtView);
        bdayTV_Below = (TextView)view.findViewById(R.id.bdayTxtViewBelow);
        bdayTV_Below.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        bdayTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        lookFwd = (TextView)view.findViewById(R.id.lookForwardTV);
        lookFwd.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        circle_main = (ImageView)view.findViewById(R.id.circle_mainImage);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");

         SelectedShopID = pref.getString("SelectedShopID", "1");

         if(SelectedShopID.equals("1"))
         {

             circle_main.setImageResource(R.drawable.myr_graph );

         }

         else if (SelectedShopID.equals("2"))
         {

             circle_main.setImageResource(R.drawable.sgd_graph);

         }

         else
         {

             circle_main.setImageResource(R.drawable.usd_graph);


         }



        if(UserID.length() > 0)
        {

            joinInBtn.setVisibility(View.GONE);
            signInBtn.setVisibility(View.GONE);

        }

        else {


            SharedPreferences.Editor editor = pref.edit();
            editor.putString("fromLoyalty", "1");

            editor.apply();

        }


        joinInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("fromLoyaltyPage", "1");
                editor.apply();

                Fragment fragment = new LoginFragment();

                Bundle bundle = new Bundle();
                bundle.putString("fromLoyalty", "1");
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("fromLoyaltyPage", "1");
                editor.apply();


                    Fragment fragment = new LoginFragment();

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    }

        });

        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment fragment = new CSWVFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", "POPLOOK Rewards");
                bundle.putString("content", linkFAQ);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

        getCS();

        return view;

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
                        JSONArray jsonArr = null;
                        jsonArr = result.getJSONArray("data");

                        for (int j = 0; j < jsonArr.length(); j++) {
                            String catID = jsonArr.getJSONObject(j).getString("meta_title");
                            String link = jsonArr.getJSONObject(j).getString("content");

                            if(catID.equals("Poplook Rewards"))
                            {
                                linkFAQ = link;
                            }



                        }

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
