package com.tiseno.poplook;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;


public class SettingsFragment extends Fragment {
//    Button settingsAboutUsButton,settingsContactUsButton,settingsMyPreferenceButton,settingsPushNotiButton,settingsShippingInfoButton,settingsTermsCondButton,settingsPrivacyPolicyButton;

    LinearLayout settingsVisitOurStoreLL,settingsAboutUsLL, settingsContactUsLL, settingsMyPreferencesLL, settingsPushNotiLL, settingsShippingInfoLL, settingsTermCondLL, settingsPrivacyPolicyLL;
    TextView settingsVisitOurStoreTV,settingsAboutUsTV, settingsContactUsTV, settingsMyPreferencesTV, settingsPushNotiTV, settingsShippingInfoTV, settingsTermCondTV, settingsPrivacyPolicyTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Settings");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        settingsVisitOurStoreLL = (LinearLayout)view.findViewById(R.id.settingsVisitOurStoreLL);
        settingsAboutUsLL = (LinearLayout)view.findViewById(R.id.settingsAboutUsLL);
        settingsContactUsLL = (LinearLayout)view.findViewById(R.id.settingsContactUsLL);
        settingsMyPreferencesLL = (LinearLayout)view.findViewById(R.id.settingsMyPreferencesLL);
        settingsPushNotiLL = (LinearLayout)view.findViewById(R.id.settingsPushNotiLL);
        settingsShippingInfoLL = (LinearLayout)view.findViewById(R.id.settingsShippingInfoLL);
        settingsTermCondLL = (LinearLayout)view.findViewById(R.id.settingsTermCondLL);
        settingsPrivacyPolicyLL = (LinearLayout)view.findViewById(R.id.settingsPrivacyPolicyLL);

        settingsVisitOurStoreTV = (TextView)view.findViewById(R.id.settingsVisitOurStoreTV);
        settingsAboutUsTV = (TextView)view.findViewById(R.id.settingsAboutUsTV);
        settingsContactUsTV = (TextView)view.findViewById(R.id.settingsContactUsTV);
        settingsMyPreferencesTV = (TextView)view.findViewById(R.id.settingsMyPreferencesTV);
        settingsPushNotiTV = (TextView)view.findViewById(R.id.settingsPushNotiTV);
        settingsShippingInfoTV = (TextView)view.findViewById(R.id.settingsShippingInfoTV);
        settingsTermCondTV = (TextView)view.findViewById(R.id.settingsTermCondTV);
        settingsPrivacyPolicyTV = (TextView)view.findViewById(R.id.settingsPrivacyPolicyTV);

        settingsVisitOurStoreTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsAboutUsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsContactUsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsMyPreferencesTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsPushNotiTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsShippingInfoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsTermCondTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        settingsPrivacyPolicyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        settingsVisitOurStoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new VisitOurStoreFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }

        });

        settingsAboutUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new AboutUsFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }

        });

        settingsContactUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new CustomerServiceFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }

        });

        settingsMyPreferencesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new MyPreferenceFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        settingsPushNotiLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PushNotificationFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        settingsShippingInfoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShippingInfoFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        settingsTermCondLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new TermsCondFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromPayment", "Nope");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }

        });

        settingsPrivacyPolicyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PrivacyPolicyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromSignUp", "Nope");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }

        });







        return view;

    }


}
