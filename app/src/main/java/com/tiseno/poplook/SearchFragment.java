package com.tiseno.poplook;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.tiseno.poplook.functions.FontUtil;


public class SearchFragment extends Fragment {
    String search;
    FrameLayout llsearch;
    SearchView mSearchView;
    Button x,apply,clear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_search, container, false);

//        ((MainActivity) getActivity()).changeToolBarText("Log In");
//        ((MainActivity) getActivity()).changeToolBarTextView(true);
//        ((MainActivity) getActivity()).changeBtnBackView(false);
//        ((MainActivity) getActivity()).changeToolBarImageView(false);
//        ((MainActivity) getActivity()).changeBtnSearchView(false);
//        ((MainActivity) getActivity()).changeBtnBagView(false);
//        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        mSearchView = (SearchView)view.findViewById(R.id.search_box);
        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(queryTextListener);

        mSearchView.setOnQueryTextListener(queryTextListener);
        x = (Button)view.findViewById(R.id.closeSearch);
        apply = (Button)view.findViewById(R.id.btn_apply_search);
        clear = (Button)view.findViewById(R.id.btn_clear_search);

        apply.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        clear.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                fm.popBackStack();
                ((MainActivity) getActivity()).getSupportActionBar().show();

            }

        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               search=mSearchView.getQuery().toString();
                ((MainActivity) getActivity()).changeToolBarText("Search ''" + search + "''");
                ((MainActivity) getActivity()).changeToolBarTextView(true);
                ((MainActivity) getActivity()).changeToolBarImageView(false);
                Fragment fragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromHome","Search");
                bundle.putString("search", search);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getFragmentManager();
                SearchFragment searchFragment = (SearchFragment)fragmentManager.findFragmentByTag("SearchFragment");

                if (searchFragment != null && searchFragment.isVisible()) {
                    // add your code here
                    FragmentTransaction trans1 = fragmentManager.beginTransaction();
                    trans1.remove(searchFragment);
                    trans1.commit();
                    fragmentManager.popBackStack();
                }

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).getSupportActionBar().show();
                System.out.print("Heloooooooo cariiiiii" + search);

            }

        });



        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search=null;
                mSearchView.setQuery("", false);
            }

        });

        return view;
    }
    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        public boolean onQueryTextChange(String newText) {
            // This is your adapter that will be filtered
            return false;
        }

        public boolean onQueryTextSubmit(String query) {
            // **Here you can get the value "query" which is entered in the search box.**
            search=query;
            ((MainActivity) getActivity()).changeToolBarText("Search ''"+search+"''");
            ((MainActivity) getActivity()).changeToolBarTextView(true);
            ((MainActivity) getActivity()).changeToolBarImageView(false);

            Fragment fragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("fromHome","Search");
            bundle.putString("search",search);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            SearchFragment searchFragment = (SearchFragment)fragmentManager.findFragmentByTag("SearchFragment");

            if (searchFragment != null && searchFragment.isVisible()) {
                // add your code here
                FragmentTransaction trans1 = fragmentManager.beginTransaction();
                trans1.remove(searchFragment);
                trans1.commit();
                fragmentManager.popBackStack();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ProductListFragment");
                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            ((MainActivity) getActivity()).getSupportActionBar().show();
            System.out.print("Heloooooooo cariiiiii" + search);

            return false;
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);}catch (Exception e){}
    }
}
