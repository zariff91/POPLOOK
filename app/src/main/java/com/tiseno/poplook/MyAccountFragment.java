package com.tiseno.poplook;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    LinearLayout myAccountPersonalInformationLL, myAccountMyAddressesLL, myAccountSavedItemsLL, myAccountOrderHistoryLL, myAccountLoyaltyPointsLL,myAccountStoreCreditLL, myAccountloyaltyLL ;
    TextView myAccountPersonalInformationTV, myAccountMyAddressesTV, myAccountSavedItemsTV, myAccountOrderHistoryTV, myAccountLoyaltyPointsTV,myAccountStoreCreditTV, myAccountLoyaltyTV;

    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_my_account, container, false);

        ((MainActivity) getActivity()).changeToolBarText("My Account");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        myAccountPersonalInformationLL = (LinearLayout) contentView.findViewById(R.id.myAccountPersonalInformationLL);
        myAccountMyAddressesLL = (LinearLayout) contentView.findViewById(R.id.myAccountMyAddressesLL);
        myAccountSavedItemsLL = (LinearLayout) contentView.findViewById(R.id.myAccountSavedItemsLL);
        myAccountOrderHistoryLL = (LinearLayout) contentView.findViewById(R.id.myAccountOrderHistoryLL);
        myAccountLoyaltyPointsLL = (LinearLayout) contentView.findViewById(R.id.myAccountLoyaltyPointsLL);
        myAccountStoreCreditLL = (LinearLayout) contentView.findViewById(R.id.myAccountStoreCreditLL);
        myAccountloyaltyLL = (LinearLayout) contentView.findViewById(R.id.myLoyaltyLL);


        myAccountPersonalInformationTV = (TextView) contentView.findViewById(R.id.myAccountPersonalInformationTV);
        myAccountMyAddressesTV = (TextView) contentView.findViewById(R.id.myAccountMyAddressesTV);
        myAccountSavedItemsTV = (TextView) contentView.findViewById(R.id.myAccountSavedItemsTV);
        myAccountOrderHistoryTV = (TextView) contentView.findViewById(R.id.myAccountOrderHistoryTV);
        myAccountLoyaltyPointsTV = (TextView) contentView.findViewById(R.id.myAccountLoyaltyPointsTV);
        myAccountStoreCreditTV = (TextView) contentView.findViewById(R.id.myAccountStoreCreditTV);
        myAccountLoyaltyTV = (TextView) contentView.findViewById(R.id.myLoyaltyTV);


        myAccountPersonalInformationTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        myAccountMyAddressesTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        myAccountSavedItemsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        myAccountOrderHistoryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        myAccountLoyaltyPointsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        myAccountStoreCreditTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        myAccountLoyaltyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));



        myAccountPersonalInformationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PersonalInformationFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        myAccountloyaltyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LoyaltyDashboardFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        myAccountMyAddressesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MyAddressFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        myAccountSavedItemsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SavedItemsFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        myAccountOrderHistoryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OrderHistoryFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        myAccountLoyaltyPointsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LoyaltyPointsFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "LoyaltyPointsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        myAccountStoreCreditLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new StoreCreditFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "StoreCreditFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //Come from payment method. Once finished payment then redirect to order history
        try{

            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
            String PaymentDone = pref.getString("PaymentDone", "");

            if(PaymentDone.equals("1"))
            {
                myAccountOrderHistoryLL.performClick();
            }else{
                System.out.println("Not from payment");
            }
        }catch (Exception e){}
        return contentView;
    }


}
