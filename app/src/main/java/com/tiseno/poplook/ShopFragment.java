package com.tiseno.poplook;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {


    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_shop, container, false);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragmentContainer, new NewProductListFragment(), "NewProductListFragment");
        //                ft.addToBackStack(null);
        ft.commit();

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();

//        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
//        String comeFromNotification = pref.getString("comeFromNotification", "0");
//        String categoryID = pref.getString("categoryID", "");
//        String categoryName = pref.getString("categoryName", "");
//        String searchKeyword = pref.getString("searchKeyword","");
//        String productID = pref.getString("productID","");
//        String productName = pref.getString("productName","");
//        String lastVisitProductPageID = pref.getString("lastVisitPage_ID","");
//        String lastVisitProductPageName = pref.getString("lastVisitedPage","");
//        String lastVisitProductDetailID = pref.getString("lastVisitProductID","");
//        String lastVisitProductDetailName = pref.getString("lastVisitProductName","");
//
//        String cartNotification = pref.getString("cartPage","");
//        String wishlistNotification = pref.getString("wishlistPage","");
//        String orderHistoryPage = pref.getString("orderHistoryPage","");
//
//        String fromChooseActivity = pref.getString("fromChooseCountryActivity","0");
//
//        try{
//            if(comeFromNotification.equals("0")) {
//
//                if(fromChooseActivity.equals("1")){
//
//                    pref.edit().putString("fromChooseCountryActivity","0");
//
//                }
//
//                else {
//
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.replace(R.id.fragmentContainer, new HomeFragment(), "HomeFragment");
////                ft.addToBackStack(null);
//                    ft.commit();
//
//                }
//
//            }else{
//
//                pref.edit().remove("comeFromNotification").commit();
//
//                if(!categoryID.equals("") && !categoryName.equals("")){
//
//                    Fragment fragment = new ProductListFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("prodID", categoryID);
//                    bundle.putString("catName",categoryName);
//                    bundle.putString("fromHome", "Home");
//                    fragment.setArguments(bundle);
//
//                    pref.edit().remove("categoryID").commit();
//                    pref.edit().remove("categoryName").commit();
//
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.replace(R.id.fragmentContainer, fragment, "ProductListFragment");
////                ft.addToBackStack(null);
//                    ft.commit();
//                }
//                else if(!lastVisitProductPageID.equals("") && !lastVisitProductPageName.equals("")){
//
//                    Fragment fragment = new ProductListFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("prodID", lastVisitProductPageID);
//                    bundle.putString("catName",lastVisitProductPageName);
//                    bundle.putString("fromHome", "Home");
//                    fragment.setArguments(bundle);
//
//                    pref.edit().remove("lastVisitPage_ID").commit();
//                    pref.edit().remove("lastVisitedPage").commit();
//
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.replace(R.id.fragmentContainer, fragment, "ProductListFragment");
////                ft.addToBackStack(null);
//                    ft.commit();
//                }else if(!searchKeyword.equals(""))
//                {
//                    Fragment fragment = new ProductListFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("fromHome","Search");
//                    bundle.putString("search", searchKeyword.toString());
//                    fragment.setArguments(bundle);
//
//                    FragmentManager fragmentManager = getFragmentManager();
//                    SearchFragment searchFragment = (SearchFragment)fragmentManager.findFragmentByTag("SearchFragment");
//
//                    if (searchFragment != null && searchFragment.isVisible()) {
//                        // add your code here
//                        FragmentTransaction trans1 = fragmentManager.beginTransaction();
//                        trans1.remove(searchFragment);
//                        trans1.commit();
//                        fragmentManager.popBackStack();
//                    }
//                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    FragmentTransaction ft = fragmentManager.beginTransaction();
////                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.replace(R.id.fragmentContainer, fragment, "SearchFragment");
////                ft.addToBackStack(null);
//                    ft.commit();
//
//                }else if(!productID.equals("") && !productName.equals("")){
//                    Fragment fragment = new ProductInfoFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("prodID", productID);
//                    bundle.putString("catName", productName);
//                    fragment.setArguments(bundle);
//
//                    pref.edit().remove("productID").commit();
//                    pref.edit().remove("productName").commit();
//
//                    FragmentManager fm = getFragmentManager();
//                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    FragmentTransaction ft = fm.beginTransaction();
////                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
////                ft.addToBackStack(null);
//                    ft.commit();
//                }
//                else if(!lastVisitProductDetailID.equals("") && !lastVisitProductDetailName.equals("")){
//                    Fragment fragment = new ProductInfoFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("prodID", lastVisitProductDetailID);
//                    bundle.putString("catName", lastVisitProductDetailName);
//                    fragment.setArguments(bundle);
//
//                    pref.edit().remove("lastVisitProductID").commit();
//                    pref.edit().remove("lastVisitProductName").commit();
//
//                    FragmentManager fm = getFragmentManager();
//                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    FragmentTransaction ft = fm.beginTransaction();
////                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_left);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.replace(R.id.fragmentContainer, fragment, "ProductInfoFragment");
////                ft.addToBackStack(null);
//                    ft.commit();
//                }
//                else if(cartNotification.equals("1")){
//
//                    pref.edit().remove("cartPage").commit();
//
//                    Fragment fragment = new ShoppingBagFragment();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//
//                }
//                else if(wishlistNotification.equals("1")){
//
//                    pref.edit().remove("wishlistPage").commit();
//
//                    Fragment fragment = new SavedItemsFragment();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "SavedItemsFragment");
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//
//                }
//                else if(orderHistoryPage.equals("1")){
//
//                    pref.edit().remove("orderHistoryPage").commit();
//
//                    Fragment fragment = new OrderHistoryFragment();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//
//                }
//                else{
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.replace(R.id.fragmentContainer, new HomeFragment(), "HomeFragment");
////                ft.addToBackStack(null);
//                    ft.commit();
//                }
//
//
//            }
//
//            // Inflate the layout for this fragment
//        }catch (Exception e){
//
//        }
    }


}
