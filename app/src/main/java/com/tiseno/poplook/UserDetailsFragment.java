package com.tiseno.poplook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.useinsider.insider.Insider;

public class UserDetailsFragment extends Fragment {

    TextView shipTo,changeCountry,welcomeUser,myAccountTV,settingsTV,followUsTV,loyaltyTV,visitOurStoreTV;
    LinearLayout myAccountLinearLayout,settingsLinearLayout,followUsLinearLayout,loyaltyLinearLayout,visitOurStoreLinearLayout;
    RelativeLayout changeContryRL;
    Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_user_details, container, false);

        changeContryRL = view.findViewById(R.id.changeCountryRL);
        myAccountLinearLayout = view.findViewById(R.id.myAcLL);
        settingsLinearLayout = view.findViewById(R.id.settingsLL);
        followUsLinearLayout = view.findViewById(R.id.followUsLL);
        loyaltyLinearLayout = view.findViewById(R.id.loyaltyLL);
        visitOurStoreLinearLayout = view.findViewById(R.id.visitOurStoreLL);

        shipTo = view.findViewById(R.id.shipToTextView);
        changeCountry = view.findViewById(R.id.changeCountryTextView);
        welcomeUser = view.findViewById(R.id.userWelcomeTextView);
        myAccountTV = view.findViewById(R.id.myAccountText);
        settingsTV = view.findViewById(R.id.settingsText);
        followUsTV = view.findViewById(R.id.followUsText);
        loyaltyTV = view.findViewById(R.id.loyaltyText);
        visitOurStoreTV = view.findViewById(R.id.shipToTextView);

        logoutButton = view.findViewById(R.id.logoutButton);

        shipTo.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        changeCountry.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        welcomeUser.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        myAccountTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        followUsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        loyaltyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        visitOurStoreTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        logoutButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        welcomeUser.setText("Hello, " + pref.getString("Name", ""));
        shipTo.setText("Ship To " + pref.getString("SelectedCountryName", ""));

        myAccountLinearLayout.setOnClickListener(view1 -> {


            MyAccountFragment myAccountFragment = new MyAccountFragment();

            FragmentManager fragmentManagerLogin = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransactionLogin = fragmentManagerLogin.beginTransaction();
            fragmentTransactionLogin.setTransition(FragmentTransaction.TRANSIT_NONE);
            fragmentTransactionLogin.replace(R.id.fragmentContainer, myAccountFragment,"MyAccountFragment");
            fragmentTransactionLogin.addToBackStack(null);
            fragmentTransactionLogin.commit();
        });

        settingsLinearLayout.setOnClickListener(view12 -> {


            SettingsFragment myAccountFragment = new SettingsFragment();

            FragmentManager fragmentManagerLogin = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransactionLogin = fragmentManagerLogin.beginTransaction();
            fragmentTransactionLogin.setTransition(FragmentTransaction.TRANSIT_NONE);
            fragmentTransactionLogin.replace(R.id.fragmentContainer, myAccountFragment);
            fragmentTransactionLogin.commit();
        });

        loyaltyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoyaltyMainPageFragment myAccountFragment = new LoyaltyMainPageFragment();

                FragmentManager fragmentManagerLogin = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionLogin = fragmentManagerLogin.beginTransaction();
                fragmentTransactionLogin.setTransition(FragmentTransaction.TRANSIT_NONE);
                fragmentTransactionLogin.replace(R.id.fragmentContainer, myAccountFragment);
                fragmentTransactionLogin.commit();
            }
        });

        visitOurStoreLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisitOurStoreFragment myAccountFragment = new VisitOurStoreFragment();

                FragmentManager fragmentManagerLogin = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionLogin = fragmentManagerLogin.beginTransaction();
                fragmentTransactionLogin.setTransition(FragmentTransaction.TRANSIT_NONE);
                fragmentTransactionLogin.replace(R.id.fragmentContainer, myAccountFragment);
                fragmentTransactionLogin.commit();
            }
        });

        changeContryRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment changeCountry = new ChangeCountryFragment();

                FragmentManager fm = getParentFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragmentContainer, changeCountry, "ChangeCountryFragment");
//                ft.addToBackStack(null);
                ft.commit();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("UserID", "");
                                editor.putString("CartID", "");
                                editor.putString("LanguageID", "1");
                                editor.putString("Name", "");
                                editor.putString("LastName", "");
                                editor.putString("Email", "");
                                editor.putString("WishlistID", "");
                                editor.putString("cartItem", "0");
                                editor.putString("wishlistItem", "0");
                                editor.putString("loyalty_id", "");
                                editor.putString("popup_show", "");
                                editor.putString("tier_level", "");
                                editor.putString("entity_id", "");

                                editor.apply();

                                Insider.Instance.tagEvent("logout").build();

                                new Handler().post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent intent =getActivity().getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        getActivity().overridePendingTransition(0, 0);
                                        getActivity().finish();

                                        getActivity().overridePendingTransition(0, 0);
                                        startActivity(intent);
                                    }
                                });

                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        return  view;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}