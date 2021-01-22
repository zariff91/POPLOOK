package com.tiseno.poplook;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import com.google.android.material.appbar.AppBarLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.shoppingBagAdapter;
import com.tiseno.poplook.functions.shoppingBagItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressBillingFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    String UserID, CartID, LanguageID,totalPriceWt,totalPriceWt_sc,totalPrice,taxAmount,shipping_price,SelectedShopID,SelectedCountryCurrency;

    JSONObject cartResultJObj,cartResultJObj1,cartResultJObj2;
    JSONObject addressResultJObj;

    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<shoppingBagItem>();
    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();
    ArrayList<voucherItem> listArray_store_credit = new ArrayList<voucherItem>();
    ArrayList<addressItem> listArray_address = new ArrayList<addressItem>();
    boolean notSameAddress=false;
    String [] addressarray;

    int SelectedDeliveryAddress = 0;
    int SelectedBillingAddress = 0;
    addressItem deliveryAddItem;
    addressItem billingAddItem;

    float totalAllProductPrice = 0;
    float totalVoucherPrice = 0;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    ImageButton billingAddressNextBtnIV,billingAddressBottomNextBtnIV ,addNewAddressBtnIV2;
    ImageButton shippingAddressEditBtnIV,shippingAddressAddtBtnIV, billingAddressEditBtnIV, billingAddressAddBtnIV;

    ImageView deliveryAddressDropDownIV,billingAddressDropDownIV;

    Toolbar toolbar;

    NumberPicker addressPickerNP;
    RelativeLayout addressPickerRL;
    ImageButton addressPickerSelectIB;
    TextView deliveryAddressTV, billingAddressTV,shippingAddressTV, billingAddressInfoTV,shippingAddressContactNoTV,billingAddressContactNoTV,shippingAddressContactLblNoTV,billingAddressContactNoLblTV;

    CheckBox deliveryAddressCB;
    String noInBag;
    ImageView deliveryAddressDividerIV,shippingAddressDividerIV,billingAddressDividerIV;
    TextView chooseBillingTV,chooseDeliveryTV,shippingAddressContactNoLblTV,addressfooterTotalInc,addressfooterRM,addressfooterShipping,shippingMethodTV,addressfooterRM1,shippingMethodTVbill1;
    LinearLayout billingAddressLL, deliveryAddressLL, shippingAddressLL, billingAddressInfoLL;
    RelativeLayout addNewAddressBtnRL2, billingAddressNextBtnRL;

    TextView subTotalTV,addGSTTV,totalInclGSTTV,totalPayableRMTV,totalPayableRM1TV,totalPayableTV,subTotalLblTV,addressRM1,addGSTLblTV,addressRM;

    Boolean FirstTimeReachHere = true;

    boolean IsDeliveryAddressPickerSelected = false;
    boolean IsSameAddressChecked = true;

    public AddressBillingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Create a RecyclerView with a linear layout manager
        View contentView = inflater.inflate(R.layout.fragment_address_billing, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Address");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        noInBag = pref.getString("cartItem", "");
        CartID = pref.getString("CartID", "");
        LanguageID = pref.getString("LanguageID", "");
//multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");
        //
//        try{
//            System.out.println("edited" + getArguments().getString("edited"));
//            if(getArguments().getString("edited").equals("done")){
//
//
//            }
//
//        }catch (Exception e){
//
//        }
//        System.out.println("edited" + getArguments().getString("edited"));

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.addressBillingRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        addressPickerNP = (NumberPicker) contentView.findViewById(R.id.addressPickerNP);

        addressPickerRL = (RelativeLayout) contentView.findViewById(R.id.addressPickerRL);
        addressPickerSelectIB = (ImageButton) contentView.findViewById(R.id.addressPickerSelectIB);

        addressPickerSelectIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressPickerRL.setVisibility(View.GONE);

                if (IsDeliveryAddressPickerSelected) {
                    SelectedDeliveryAddress = addressPickerNP.getValue();
                } else {
                    SelectedBillingAddress = addressPickerNP.getValue();
                    notSameAddress = true;
                }

                updateOrderSummary();
                updateAddressInfo();
            }
        });
        getCartDetailList();

        return contentView;
    }

    public void getCartDetailList() {
        String cartResultJObjString=getArguments().getString("cartResultJObj");

        try {
            cartResultJObj = new JSONObject(cartResultJObjString);

            listArray_shoppingBag.clear();
            totalAllProductPrice = 0;
            String action = cartResultJObj.getString("action");

            if (action.equals("editShippingAddress")||action.equals("addShippingAddress")) {
                if (cartResultJObj.getString("result").equals("1")) {

                    JSONArray jsonArr = new JSONArray();
                    jsonArr = cartResultJObj.getJSONArray("product_list");
//                    CartID = cartResultJObj.getString("id_cart");
                    totalPriceWt = cartResultJObj.getString("totalProductsWt");
                    totalPriceWt_sc = cartResultJObj.getString("totalPrice_no_sc");
                    totalPrice = cartResultJObj.getString("totalPrice");
                    taxAmount = cartResultJObj.getString("taxCost");
                    shipping_price = cartResultJObj.getString("shipping_price");

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jObj = jsonArr.getJSONObject(i);
                        String productAttributeID = jObj.getString("id_product_attribute");
                        String productID = jObj.getString("id_product");
                        String productName = jObj.getString("name");
                        String productSize = jObj.getString("size");
                        String price = jObj.getString("price");
                        String discountedPrice = jObj.getString("discounted_price");
                        String imageURL = jObj.getString("image_url");
                        String timeEnd = jObj.getString("time_end");
                        String timeRem = jObj.getString("time_remainder");
                        String productRef = jObj.getString("reference");
                        String quantity = jObj.getString("quantity");
                        Integer quantity_available = jObj.getInt("quantity_available");


                        try {

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            Date d1 = format.parse(current_date);
                            Date d2 = format.parse(timeEnd);

                            //in milliseconds
                            long diff = d2.getTime() - d1.getTime();
                            System.out.println("current diff is " + diff);


                                if (!discountedPrice.equals("0")) {
                                    price = discountedPrice;
                                }

                                price = String.format("%.2f", Float.parseFloat(price));


                                String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                                totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);


                                listArray_shoppingBag.add(new shoppingBagItem(CartID, productAttributeID, productID, imageURL, productName, productSize, productRef, price, totalPrice, quantity, timeRem,quantity_available));

                        } catch (Exception e) {
                        }

                    }

                    ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(noInBag));
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("cartItem", String.valueOf(noInBag));
                    editor.apply();

                    listArray_voucher.clear();
                    totalVoucherPrice = 0;

                    try {
                        JSONArray voucherArray = cartResultJObj.getJSONArray("voucher_list");
                        for (int j = 0; j < voucherArray.length(); j++) {
                            JSONObject jObj2 = voucherArray.getJSONObject(j);
                            String voucherID = jObj2.getString("id_voucher");
                            String voucher_name= jObj2.getString("voucher_name");
                            String voucherCode = jObj2.getString("code");
                            String voucherPercentage = jObj2.getString("reduction_percent");
                            String voucherAmount = jObj2.getString("reduction_amount");

//                            if (!voucherPercentage.equals("0.00")) {
//                                float Percentage = Float.parseFloat(voucherPercentage);
//                                float amount = totalAllProductPrice * (Percentage / 100);
//                                voucherAmount=String.format("%.2f", amount);
//                            }

                            totalVoucherPrice += Float.parseFloat(voucherAmount);


                            listArray_voucher.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                        }
                    } catch (Exception e) {
                    }

                    listArray_store_credit.clear();

                    try {
                        JSONArray voucherArray1 = cartResultJObj.getJSONArray("store_credit_list");
                        for (int j = 0; j < voucherArray1.length(); j++) {
                            JSONObject jObj2 = voucherArray1.getJSONObject(j);
                            String voucherID = jObj2.getString("id_voucher");
                            String voucher_name= jObj2.getString("voucher_name");
                            String voucherCode = jObj2.getString("code");
                            String voucherPercentage = jObj2.getString("reduction_percent");
                            String voucherAmount = jObj2.getString("reduction_amount");

//                            if (!voucherPercentage.equals("0.00")) {
//                                float Percentage = Float.parseFloat(voucherPercentage);
//                                float amount = totalAllProductPrice * (Percentage / 100);
//                                voucherAmount=String.format("%.2f", amount);
//                            }

                            listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                        }
                    }catch (Exception e)
                    {

                    }

                    listArray_address.clear();
                    String stateID="",address2="",company="",addressState="";
                    try {
                        JSONArray jsonArr1 = new JSONArray();
                        jsonArr1 = cartResultJObj.getJSONArray("address_list");

                        ArrayList<String> addressArrayList = new ArrayList<String>();


                        for (int i = 0; i < jsonArr1.length(); i++) {
                            JSONObject jObj3 = jsonArr1.getJSONObject(i);
                            String addressID = jObj3.getString("id_address");
                            String addressFirstName = jObj3.getString("firstname");
                            String addressLastName = jObj3.getString("lastname");
                            String id_gender = jObj3.getString("id_gender");
                            if (jObj3.has("company")) {
                                company = jObj3.getString("company");
                            }
                            String address1 = jObj3.getString("address1");
                            address1=address1.replace("\\","");
                            if (jObj3.has("address2")) {
                                address2 = jObj3.getString("address2");
                            }
                            address2=address2.replace("\\","");
                            String addressPostCode = jObj3.getString("postcode");
                            String addressCity = jObj3.getString("city");
                            String countryID = jObj3.getString("id_country");
                            String addressCountry = jObj3.getString("country_name");
                            if (jObj3.has("id_state")) {
                                stateID = jObj3.getString("id_state");
                            }
                            if (jObj3.has("state_name")) {
                                addressState = jObj3.getString("state_name");
                            }
                            String addressPhone = jObj3.getString("phone");


                            System.out.println("country issss " + jObj3.getString("country_name"));

                            listArray_address.add(new addressItem(addressID, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                            addressArrayList.add(address1);

                        }


                            addressarray = addressArrayList.toArray(new String[addressArrayList.size()]);


                            addressPickerNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                            addressPickerNP.setMinValue(0);
                            addressPickerNP.setMaxValue(addressarray.length - 1);
                            addressPickerNP.setDisplayedValues(addressarray);
                            setDividerColor(addressPickerNP);

                        refreshRecycleView();
                        updateAddressInfo();

                    }catch (Exception e){}

                }

                //Havent Log in
                if (FirstTimeReachHere) {
                    if (UserID.length() == 0) {
                        FirstTimeReachHere = false;
                        Fragment fragment = new CheckOutMethodFragment();

                        Bundle args = new Bundle();
                        String cartResultJObjString1 = getArguments().getString("cartResultJObj");

                        args.putString("cartResultJObj", cartResultJObjString1);
                        fragment.setArguments(args);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else {
                    if (UserID.length() == 0) {
                        getFragmentManager().popBackStack();

                    }
                }

            }else if(action.equals("Carts_OrderStep1")) {
                if (cartResultJObj.getBoolean("status")) {
                    JSONObject data= cartResultJObj.getJSONObject("data");
                    JSONArray jsonArr = new JSONArray();
                    jsonArr = data.getJSONArray("product_list");
//                    CartID = cartResultJObj.getString("id_cart");
                    totalPriceWt = data.getString("totalProductsWt");
                    totalPriceWt_sc = data.getString("totalPrice_no_sc");
                    totalPrice = data.getString("totalPrice");
                    taxAmount = data.getString("taxCost");
                    shipping_price = data.getString("shipping_price");

                    for(int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jObj = jsonArr.getJSONObject(i);
                        String productAttributeID = jObj.getString("id_product_attribute");
                        String productID = jObj.getString("id_product");
                        String productName = jObj.getString("name");
                        String productSize = jObj.getString("attributes_small");
                        if(productSize.equals("")){
                            productSize="-";
                        }
                        String price = jObj.getString("price");
                        String discountedPrice = jObj.getString("price");
                        String imageURL = jObj.getString("image_url");
                        String timeEnd = jObj.getString("time_end");
                        String timeRem = jObj.getString("time_remainder");
                        String productRef = jObj.getString("reference");
                        String quantity = jObj.getString("quantity");
                        String item_total = jObj.getString("total");
                        Integer quantity_available = jObj.getInt("quantity_available");

                        int tot=Integer.parseInt(quantity);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        Date d1 = format.parse(current_date);
                        Date d2 = format.parse(timeEnd);
                        System.out.println("current date is " + current_date);
                        System.out.println("current timeEnd is " + timeEnd);


                        System.out.println("current timeRem is " + timeRem);
                        //in milliseconds
                        long diff = d2.getTime() - d1.getTime();
                        System.out.println("current diff is " + diff);

//                            if(diff > 0)
//                            {
                        if(!discountedPrice.equals("0"))
                        {
                            price = discountedPrice;
                        }

                        price = String.format("%.2f", Float.parseFloat(price));


                        String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                        totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);


                        listArray_shoppingBag.add(new shoppingBagItem(CartID,productAttributeID,productID,imageURL,productName,productSize,productRef,price,item_total,quantity,timeRem,quantity_available));

//                            }





                    }

                    ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(noInBag));
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("cartItem", String.valueOf(noInBag));
                    editor.apply();

                    listArray_voucher.clear();

                    try {
                        JSONArray voucherArray = data.getJSONArray("voucher_list");
                        for (int j = 0; j < voucherArray.length(); j++) {
                            JSONObject jObj2 = voucherArray.getJSONObject(j);
                            String voucherID = jObj2.getString("id_discount");
                            String voucher_name= jObj2.getString("name");
                            String voucherCode = jObj2.getString("code");
                            String voucherPercentage = jObj2.getString("reduction_percent");
                            String voucherAmount = jObj2.getString("value_tax_exc");

//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                            listArray_voucher.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                        }
                    }catch (Exception e)
                    {

                    }

                    listArray_store_credit.clear();

                    try {
                        JSONArray voucherArray1 = data.getJSONArray("store_credit_list");
                        for (int j = 0; j < voucherArray1.length(); j++) {
                            JSONObject jObj2 = voucherArray1.getJSONObject(j);
                            String voucherID = jObj2.getString("id_discount");
                            String voucher_name= jObj2.getString("name");
                            String voucherCode = jObj2.getString("code");
                            String voucherPercentage = jObj2.getString("reduction_percent");
                            String voucherAmount = jObj2.getString("value_real");

//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                            listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                        }
                    }catch (Exception e)
                    {

                    }

                    listArray_address.clear();
                    String stateID="",address2="",company="",addressState="";
                    try {
                        JSONArray jsonArr1 = new JSONArray();
                        jsonArr1 = data.getJSONArray("address_list");

                        ArrayList<String> addressArrayList = new ArrayList<String>();


                        for (int i = 0; i < jsonArr1.length(); i++) {
                            JSONObject jObj3 = jsonArr1.getJSONObject(i);
                            String addressID = jObj3.getString("id_address");
                            String addressFirstName = jObj3.getString("firstname");
                            String addressLastName = jObj3.getString("lastname");
                            String id_gender = jObj3.getString("id_gender");
                            if (jObj3.has("company")) {
                                company = jObj3.getString("company");
                            }
                            String address1 = jObj3.getString("address1");
                            address1=address1.replace("\\","");
                            if (jObj3.has("address2")) {
                                address2 = jObj3.getString("address2");
                            }
                            address2=address2.replace("\\","");
                            String addressPostCode = jObj3.getString("postcode");
                            String addressCity = jObj3.getString("city");
                            String countryID = jObj3.getString("id_country");
                            String addressCountry = jObj3.getString("country_name");
                            if (jObj3.has("id_state")) {
                                stateID = jObj3.getString("id_state");
                            }
                            if (jObj3.has("state_name")) {
                                addressState = jObj3.getString("state_name");
                            }
                            String addressPhone = jObj3.getString("phone");
                            System.out.println("country issss " + jObj3.getString("country_name"));

                            listArray_address.add(new addressItem(addressID, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                            addressArrayList.add(address1);

                        }


                        addressarray = addressArrayList.toArray(new String[addressArrayList.size()]);


                        addressPickerNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                        addressPickerNP.setMinValue(0);
                        addressPickerNP.setMaxValue(addressarray.length - 1);
                        addressPickerNP.setDisplayedValues(addressarray);
                        setDividerColor(addressPickerNP);

                        refreshRecycleView();
                        updateAddressInfo();

                    }catch (Exception e){}

                }

                //Havent Log in
                if (FirstTimeReachHere) {

                    System.out.println("sini kann");

                    if (UserID.length() == 0) {
                        FirstTimeReachHere = false;
                        Fragment fragment = new CheckOutMethodFragment();

                        Bundle args = new Bundle();
                        String cartResultJObjString1 = getArguments().getString("cartResultJObj");

                        args.putString("cartResultJObj", cartResultJObjString1);
                        fragment.setArguments(args);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else {
                    if (UserID.length() == 0) {
                        getFragmentManager().popBackStack();

                    }
                }
            }
        }catch(Exception e){
                System.out.println("ada prob is " + e);
            }

    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                String action = result.getString("action");
              if(action.equals("Carts_OrderStep2"))
                {
                    if(result.getBoolean("status"))
                    {   cartResultJObj1 = result;
                        String nextPage = result.getJSONObject("data").getString("next_page");

                        if(nextPage.equals("shippingMethod"))
                        {
                            Fragment fragment = new shippingMethodFragment();
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                            fragmentTransaction.addToBackStack(null);


                            Bundle args = new Bundle();
                            String cartResultJObjString = cartResultJObj1.toString();
                            args.putString("cartResultJObj", cartResultJObjString);
                            args.putInt("SelectedDeliveryAddress", SelectedDeliveryAddress);
                            args.putInt("SelectedBillingAddress",SelectedBillingAddress);
                            fragment.setArguments(args);

                            fragmentTransaction.commit();
                        }
                        else if(nextPage.equals("cart"))
                        {
                            getActivity().onBackPressed();
                        }

                    }
                    else
                    {
                        String message = result.getString("message");
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Poplook")
                                .setMessage(message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
//                                        Fragment fragment = new ShoppingBagFragment();
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
//                                        fragmentTransaction.addToBackStack(null);
//                                        fragmentTransaction.commit();
                                    }
                                }).show();
                    }
                }else if(action.equals("Carts_updateOrderSummary"))
              {
                  if(result.getBoolean("status"))
                  {   cartResultJObj2 = result;
                      JSONObject data= cartResultJObj2.getJSONObject("data");
                      totalPriceWt = data.getString("totalProductsWt");
                      totalPriceWt_sc = data.getString("totalPrice_no_sc");
                      totalPrice = data.getString("totalPrice");
                      taxAmount = data.getString("taxCost");
                      shipping_price = data.getString("shipping_price");

                      listArray_store_credit.clear();

                      try {
                          JSONArray voucherArray1 = data.getJSONArray("store_credit_list");
                          for (int j = 0; j < voucherArray1.length(); j++) {
                              JSONObject jObj2 = voucherArray1.getJSONObject(j);
                              String voucherID = jObj2.getString("id_discount");
                              String voucher_name= jObj2.getString("name");
                              String voucherCode = jObj2.getString("code");
                              String voucherPercentage = jObj2.getString("reduction_percent");
                              String voucherAmount = jObj2.getString("value_real");


//                              if (!voucherPercentage.equals("0.00")) {
//                                  float Percentage = Float.parseFloat(voucherPercentage);
//                                  float amount = totalAllProductPrice * (Percentage / 100);
//                                  voucherAmount=String.format("%.2f", amount);
//                              }

                              listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                          }

                      }catch (Exception e)
                      {

                      }


                      listArray_voucher.clear();
                      totalVoucherPrice = 0;

                      try {
                          JSONArray voucherArray = data.getJSONArray("voucher_list");
                          for (int j = 0; j < voucherArray.length(); j++) {
                              JSONObject jObj2 = voucherArray.getJSONObject(j);
                              String voucherID = jObj2.getString("id_discount");
                              String voucher_name= jObj2.getString("name");
                              String voucherCode = jObj2.getString("code");
                              String voucherPercentage = jObj2.getString("reduction_percent");
                              String voucherAmount = jObj2.getString("value_tax_exc");


//                              if (!voucherPercentage.equals("0.00")) {
//                                  float Percentage = Float.parseFloat(voucherPercentage);
//                                  float amount = totalAllProductPrice * (Percentage / 100);
//                                  voucherAmount=String.format("%.2f", amount);
//                              }

                              totalVoucherPrice += Float.parseFloat(voucherAmount);


                              listArray_voucher.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                          }
                            System.out.println("Store Cred" + listArray_voucher.get(0));
                      } catch (Exception e) {
                      }


                      refreshRecycleView();
                      updateAddressInfo();
                  }
                  else
                  {
                      String message = result.getString("message");
                      new AlertDialog.Builder(getActivity())
                              .setTitle("Poplook")
                              .setMessage(message)
                              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int id) {
                                      dialog.cancel();
//                                      Fragment fragment = new ShoppingBagFragment();
//                                      FragmentManager fragmentManager = getFragmentManager();
//                                      fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                                      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                      fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
//                                      fragmentTransaction.addToBackStack(null);
//                                      fragmentTransaction.commit();
                                  }
                              }).show();
                  }
              }
                refreshRecycleView();
                updateAddressInfo();

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

    private void updateAddressInfo()
    {
        try {
            if(listArray_address.size() == 0)
            {
//                Fragment fragment = new AddAdressFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("COME_FROM_WHERE", 1);
//                bundle.putBoolean("EDIT_ADDRESS", false);
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = getActivity().getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

                chooseBillingTV.setVisibility(View.GONE);
                deliveryAddressLL.setVisibility(View.GONE);
                deliveryAddressCB.setVisibility(View.GONE);
                deliveryAddressDividerIV.setVisibility(View.GONE);
                chooseBillingTV.setVisibility(View.GONE);
                billingAddressLL.setVisibility(View.GONE);
                shippingAddressLL.setVisibility(View.GONE);
                shippingAddressDividerIV.setVisibility(View.GONE);
                shippingAddressTV.setVisibility(View.GONE);
                shippingAddressContactNoLblTV.setVisibility(View.GONE);
                shippingAddressContactNoTV.setVisibility(View.GONE);
                billingAddressInfoLL.setVisibility(View.GONE);
                billingAddressDividerIV.setVisibility(View.GONE);
                billingAddressInfoTV.setVisibility(View.GONE);
                billingAddressContactNoLblTV.setVisibility(View.GONE);
                billingAddressContactNoTV.setVisibility(View.GONE);
                billingAddressNextBtnRL.setVisibility(View.GONE);

                addNewAddressBtnRL2.setVisibility(View.VISIBLE);
            }
            else
            {
                deliveryAddItem = listArray_address.get(SelectedDeliveryAddress);

                String deliveryAddFirstName = deliveryAddItem.getaddressFirstName();
                String deliveryAddLastName = deliveryAddItem.getaddressLastName();
                String deliveryAdd1 = deliveryAddItem.getaddress1();
                String deliveryAdd2 = deliveryAddItem.getaddress2();
                String deliveryAddPostCode = deliveryAddItem.getaddressPostCode();
                String deliveryAddCity = deliveryAddItem.getaddressCity();
                String deliveryAddCountry = deliveryAddItem.getaddressCountry();
                String deliveryAddState = deliveryAddItem.getaddressState();
                String deliveryAddPhone = deliveryAddItem.getaddressPhone();

                deliveryAddressTV.setText(addressarray[SelectedDeliveryAddress]);

                shippingAddressTV.setText(deliveryAddFirstName + " " + deliveryAddLastName + "\n" + deliveryAdd1 + " " + deliveryAdd2 + "\n" + deliveryAddPostCode + " " + deliveryAddCity + "\n" + deliveryAddState + "\n" + deliveryAddCountry);
                shippingAddressContactNoTV.setText(deliveryAddPhone);

                if (IsSameAddressChecked) {
                    SelectedBillingAddress = SelectedDeliveryAddress;
                    billingAddItem = deliveryAddItem;

                    billingAddressTV.setText(addressarray[SelectedDeliveryAddress]);
                    billingAddressInfoTV.setText(deliveryAddFirstName + " " + deliveryAddLastName + "\n" + deliveryAdd1 + " " + deliveryAdd2 + "\n" + deliveryAddPostCode + " " + deliveryAddCity + "\n" + deliveryAddState + "\n" + deliveryAddCountry);
                    billingAddressContactNoTV.setText(deliveryAddPhone);
                } else if (IsSameAddressChecked == false && listArray_address.size() == 1) {
                    billingAddressTV.setText(addressarray[SelectedDeliveryAddress]);

                    billingAddressInfoTV.setText(deliveryAddFirstName + " " + deliveryAddLastName + "\n" + deliveryAdd1 + " " + deliveryAdd2 + "\n" + deliveryAddPostCode + " " + deliveryAddCity + "\n" + deliveryAddState + "\n" + deliveryAddCountry);
                    billingAddressContactNoTV.setText(deliveryAddPhone);
                } else if (IsSameAddressChecked == false && listArray_address.size() > 1) {
                    billingAddItem = listArray_address.get(SelectedBillingAddress);

                    String billingAddFirstName = billingAddItem.getaddressFirstName();
                    String billingAddLastName = billingAddItem.getaddressLastName();
                    String billingAdd1 = billingAddItem.getaddress1();
                    String billingAdd2 = billingAddItem.getaddress2();
                    String billingAddPostCode = billingAddItem.getaddressPostCode();
                    String billingAddCity = billingAddItem.getaddressCity();
                    String billingAddCountry = billingAddItem.getaddressCountry();
                    String billingAddState = billingAddItem.getaddressState();
                    String billingAddPhone = billingAddItem.getaddressPhone();

                    billingAddressTV.setText(addressarray[SelectedBillingAddress]);

                    billingAddressInfoTV.setText(billingAddFirstName + " " + billingAddLastName + "\n" + billingAdd1 + " " + billingAdd2 + "\n" + billingAddPostCode + " " + billingAddCity + "\n" + billingAddState + "\n" + billingAddCountry);
                    billingAddressContactNoTV.setText(billingAddPhone);
                }
            }


        }catch (Exception e){
//            Toast.makeText(getActivity(),"No address available. Please add an address.",Toast.LENGTH_LONG).show();
        }
    }

    private void setDividerColor (NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //pf.set(picker, getResources().getColor(R.color.my_orange));
                    //Log.v(TAG,"here");
                    pf.set(picker, getResources().getDrawable(R.drawable.divider_1));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void GoToNextStepWS(int SelectedDeliveryAddress, int SelectedBillingAddress)
    {
        addressItem deliveryItem = listArray_address.get(SelectedDeliveryAddress);
        addressItem billingItem = listArray_address.get(SelectedBillingAddress);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Carts/OrderStep2?apikey="+apikey+"&id_cart="+CartID+"&id_address_delivery="+deliveryItem.getaddressID()+"&id_address_billing="+billingItem.getaddressID()+"";

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);
    }
    public void updateOrderSummary(){
        addressItem deliveryItem = listArray_address.get(SelectedDeliveryAddress);
        addressItem billingItem = listArray_address.get(SelectedBillingAddress);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Carts/updateOrderSummary?apikey="+apikey+"&id_cart="+CartID+"&id_address_delivery="+deliveryItem.getaddressID()+"&id_address_billing="+billingItem.getaddressID()+"";

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);

    }

    private void refreshRecycleView()
    {
        System.out.println("come to refreshRecycleView");
        RVAdapter = new shoppingBagAdapter(getActivity(),listArray_shoppingBag,null,false);
        // Make Bookends
        final Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

        // Inflate footer view
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

        RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.billing_address_header_view, mRecyclerView, false);
        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.billing_address_footer_view, mRecyclerView, false);
        LinearLayout footer1 = (LinearLayout) inflater1.inflate(R.layout.billing_address_footer_view1, mRecyclerView, false);
        RelativeLayout footer2 = (RelativeLayout) inflater1.inflate(R.layout.billing_address_footer_first_view, mRecyclerView, false);
        RelativeLayout footer3 = (RelativeLayout) inflater1.inflate(R.layout.billing_address_footer_second_view, mRecyclerView, false);

        TextView step1TV = (TextView) header.findViewById(R.id.step1TV);
        TextView step2TV = (TextView) header.findViewById(R.id.step2TV);
        TextView step2toTV = (TextView) header.findViewById(R.id.step2toTV);
        TextView step3TV = (TextView) header.findViewById(R.id.step3TV);
        TextView step3toTV = (TextView) header.findViewById(R.id.step3toTV);
        TextView step4TV = (TextView) header.findViewById(R.id.step4TV);
        TextView step4toTV = (TextView) header.findViewById(R.id.step4toTV);
        TextView step5TV = (TextView) header.findViewById(R.id.step5TV);

        step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        step2toTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        step3TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        step3toTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        step4TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        step4toTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        step5TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        deliveryAddressDropDownIV = (ImageView)header.findViewById(R.id.deliveryAddressDropDownIV);
        billingAddressDropDownIV = (ImageView)header.findViewById(R.id.billingAddressDropDownIV);
        deliveryAddressTV = (TextView) header.findViewById(R.id.deliveryAddressTV);
        billingAddressTV = (TextView) header.findViewById(R.id.billingAddressTV);
        shippingAddressTV = (TextView) header.findViewById(R.id.shippingAddressTV);
        billingAddressInfoTV = (TextView) header.findViewById(R.id.billingAddressInfoTV);
        shippingAddressContactNoTV = (TextView) header.findViewById(R.id.shippingAddressContactNoTV);
        billingAddressContactNoTV = (TextView) header.findViewById(R.id.billingAddressContactNoTV);


        deliveryAddressDividerIV = (ImageView) header.findViewById(R.id.deliveryAddressDividerIV);
        chooseBillingTV = (TextView) header.findViewById(R.id.chooseBillingTV);
        billingAddressLL = (LinearLayout) header.findViewById(R.id.billingAddressLL);
        addNewAddressBtnRL2 = (RelativeLayout) header.findViewById(R.id.addNewAddressBtnRL2);
        addNewAddressBtnIV2 = (ImageButton) header.findViewById(R.id.addNewAddressBtnIV2);

        chooseDeliveryTV= (TextView) header.findViewById(R.id.chooseDeliveryTV);
        deliveryAddressLL = (LinearLayout) header.findViewById(R.id.deliveryAddressLL);
        shippingAddressLL = (LinearLayout) header.findViewById(R.id.shippingAddressLL);
        shippingAddressDividerIV = (ImageView) header.findViewById(R.id.shippingAddressDividerIV);
        shippingAddressContactNoTV= (TextView) header.findViewById(R.id.shippingAddressContactNoTV);
        shippingAddressContactLblNoTV = (TextView) header.findViewById(R.id.shippingAddressContactNoLblTV);
        billingAddressContactNoLblTV = (TextView) header.findViewById(R.id.billingAddressContactNoLblTV);
        billingAddressInfoLL = (LinearLayout) header.findViewById(R.id.billingAddressInfoLL);
        billingAddressDividerIV = (ImageView) header.findViewById(R.id.billingAddressDividerIV);
        billingAddressNextBtnRL = (RelativeLayout) header.findViewById(R.id.billingAddressNextBtnRL);

        TextView orderSummaryTV= (TextView) header.findViewById(R.id.orderSummaryTV);
        TextView shippingAddressLabel= (TextView) header.findViewById(R.id.shippingAddressLabel);
        TextView billingAddLabel= (TextView) header.findViewById(R.id.billingAddLabel);

        chooseDeliveryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        chooseBillingTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        deliveryAddressTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressInfoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressContactLblNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        orderSummaryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressContactNoLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));





        addNewAddressBtnIV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddAdressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("COME_FROM_WHERE", 1);
                bundle.putBoolean("EDIT_ADDRESS", false);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        RelativeLayout totalInclGSTRL = (RelativeLayout) footer.findViewById(R.id.totalInclGSTRL);


        subTotalTV = (TextView) footer2.findViewById(R.id.subTotalTV);
        addGSTTV = (TextView) footer3.findViewById(R.id.addGSTTV);
        totalInclGSTTV = (TextView) footer.findViewById(R.id.totalInclGSTTV);
        addressfooterTotalInc = (TextView) footer.findViewById(R.id.addressfooterTotalInc);
        addressfooterRM = (TextView) footer.findViewById(R.id.addressfooterRM);
        addressfooterShipping = (TextView) footer.findViewById(R.id.addressfooterShipping);
        shippingMethodTV = (TextView) footer.findViewById(R.id.shippingMethodTVbill);
        addressfooterRM1 = (TextView) footer.findViewById(R.id.addressfooterRM1);
        shippingMethodTVbill1 = (TextView) footer.findViewById(R.id.shippingMethodTVbill1);
        subTotalLblTV = (TextView) footer2.findViewById(R.id.subTotalLblTVbill);
        addressRM1 = (TextView) footer2.findViewById(R.id.addressRM1);
        addGSTLblTV = (TextView) footer3.findViewById(R.id.addGSTLblTVBill);
        addressRM = (TextView) footer3.findViewById(R.id.addressRM);

        totalPayableRMTV = (TextView) footer1.findViewById(R.id.totalPayableRMTV);
        totalPayableRM1TV = (TextView) footer1.findViewById(R.id.totalPayableRM1TV);
        totalPayableTV = (TextView) footer1.findViewById(R.id.totalPayableTV);

        addressfooterRM1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodTVbill1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        subTotalTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalInclGSTTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalPayableRMTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalPayableRM1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalPayableTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        addressfooterTotalInc.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addressfooterRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addressfooterShipping.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        subTotalLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addressRM1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addressRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));


        totalPayableRM1TV.setText(SelectedCountryCurrency);
        addressRM1.setText(SelectedCountryCurrency);

        if(shipping_price.equals("0.00")){
            shippingMethodTV.setVisibility(View.VISIBLE);
            addressfooterRM1.setVisibility(View.GONE);
            shippingMethodTVbill1.setVisibility(View.GONE);
            shippingMethodTV.setText("Free Shipping");

    }else{
            shippingMethodTV.setVisibility(View.GONE);
            addressfooterRM1.setVisibility(View.VISIBLE);
            addressfooterRM1.setText(SelectedCountryCurrency);
            shippingMethodTVbill1.setVisibility(View.VISIBLE);
            shippingMethodTVbill1.setText(shipping_price);
    }
        subTotalTV.setText(totalPriceWt);
        if(taxAmount.equals("0.00")){
            totalInclGSTRL.setVisibility(View.GONE);
        }else {
            totalInclGSTRL.setVisibility(View.VISIBLE);
            addGSTTV.setText(taxAmount);
        }

        if (totalPriceWt.equalsIgnoreCase("0.00")){
            totalPayableTV.setText("0.00");
        }else if(!totalPriceWt.contains(".")) {
            totalPayableTV.setText(totalPriceWt+".00");

            float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
            System.out.print("GST" + rounded);
        }else{

            totalPayableTV.setText(totalPrice);
        }

        if (totalPriceWt_sc.equalsIgnoreCase("0.00")){
            totalInclGSTTV.setText("0.00");
        }else if(!totalPriceWt_sc.contains(".")) {
            totalInclGSTTV.setText(totalPriceWt_sc+".00");

            float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
            System.out.print("GST" + rounded);
        }else{

            totalInclGSTTV.setText(totalPriceWt_sc);
        }


        deliveryAddressCB = (CheckBox) header.findViewById(R.id.deliveryAddressCB);
        deliveryAddressCB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        if (notSameAddress){
            deliveryAddressCB.setChecked(false);
            IsSameAddressChecked = false;
            updateAddressInfo();
        }else{
            deliveryAddressCB.setChecked(true);
            IsSameAddressChecked = true;
            updateAddressInfo();
        }
        deliveryAddressCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                if(checkBox.isChecked()){
                    IsSameAddressChecked = true;

                    deliveryAddressDividerIV.setVisibility(View.GONE);
                    chooseBillingTV.setVisibility(View.GONE);
                    billingAddressLL.setVisibility(View.GONE);
                    addNewAddressBtnRL2.setVisibility(View.GONE);

                }
                else {
                    IsSameAddressChecked = false;

                    if(listArray_address.size() == 1)
                    {
                        deliveryAddressDividerIV.setVisibility(View.INVISIBLE);
                        chooseBillingTV.setVisibility(View.INVISIBLE);
                        billingAddressLL.setVisibility(View.INVISIBLE);
                        addNewAddressBtnRL2.setVisibility(View.VISIBLE);
                    }
                    else if(listArray_address.size() > 1)
                    {
                        deliveryAddressDividerIV.setVisibility(View.VISIBLE);
                        chooseBillingTV.setVisibility(View.VISIBLE);
                        billingAddressLL.setVisibility(View.VISIBLE);
                        addNewAddressBtnRL2.setVisibility(View.GONE);
                    }
                    else if(listArray_address.size() == 0)
                    {
                        chooseBillingTV.setVisibility(View.GONE);
                        deliveryAddressLL.setVisibility(View.GONE);
                        deliveryAddressCB.setVisibility(View.GONE);
                        deliveryAddressDividerIV.setVisibility(View.GONE);
                        chooseBillingTV.setVisibility(View.GONE);
                        billingAddressLL.setVisibility(View.GONE);
                        shippingAddressLL.setVisibility(View.GONE);
                        shippingAddressDividerIV.setVisibility(View.GONE);
                        shippingAddressTV.setVisibility(View.GONE);
                        shippingAddressContactNoTV.setVisibility(View.GONE);
                        shippingAddressContactNoTV.setVisibility(View.GONE);
                        billingAddressInfoLL.setVisibility(View.GONE);
                        billingAddressDividerIV.setVisibility(View.GONE);
                        billingAddressInfoTV.setVisibility(View.GONE);
                        billingAddressContactNoLblTV.setVisibility(View.GONE);
                        billingAddressContactNoTV.setVisibility(View.GONE);
                        billingAddressNextBtnRL.setVisibility(View.GONE);

                        addNewAddressBtnRL2.setVisibility(View.VISIBLE);
                    }

                }

                updateAddressInfo();
            }
        });

        deliveryAddressDropDownIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listArray_address.size() > 0)
                {
                    IsDeliveryAddressPickerSelected = true;
                    addressPickerRL.setVisibility(View.VISIBLE);
                }

            }
        });

        billingAddressDropDownIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listArray_address.size() > 0)
                {
                    IsDeliveryAddressPickerSelected = false;
                    addressPickerRL.setVisibility(View.VISIBLE);
                }

            }
        });



        shippingAddressEditBtnIV = (ImageButton) header.findViewById(R.id.shippingAddressEditBtnIV);
        shippingAddressAddtBtnIV = (ImageButton) header.findViewById(R.id.shippingAddressAddtBtnIV);
        billingAddressEditBtnIV = (ImageButton) header.findViewById(R.id.billingAddressEditBtnIV);
        billingAddressAddBtnIV = (ImageButton) header.findViewById(R.id.billingAddressAddBtnIV);

        shippingAddressEditBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddAdressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("COME_FROM_WHERE", 1);
                bundle.putBoolean("EDIT_ADDRESS", true);

                bundle.putString("ADD_ID", deliveryAddItem.getaddressID());
                bundle.putString("FIRST_NAME", deliveryAddItem.getaddressFirstName());
                bundle.putString("LAST_NAME", deliveryAddItem.getaddressLastName());
                bundle.putString("COMPANY","");
                bundle.putString("ADD1",deliveryAddItem.getaddress1());
                bundle.putString("ADD2",deliveryAddItem.getaddress2());
                bundle.putString("COUNTRY_NAME",deliveryAddItem.getaddressCountry());
                bundle.putString("COUNTRY_ID",deliveryAddItem.getaddressCountryID());
                bundle.putString("STATE_NAME",deliveryAddItem.getaddressState());
                bundle.putString("STATE_ID",deliveryAddItem.getaddressStateID());
                bundle.putString("CITY_NAME",deliveryAddItem.getaddressCity());
                bundle.putString("POSTCODE_NAME",deliveryAddItem.getaddressPostCode());
                bundle.putString("PHONE", deliveryAddItem.getaddressPhone());

//                System.out.println("country name is " + billingAddItem.getaddressCountry());

                String cartResultJObjString = cartResultJObj.toString();
                bundle.putString("cartResultJObj", cartResultJObjString);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        shippingAddressAddtBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddAdressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("COME_FROM_WHERE", 1);
                bundle.putBoolean("EDIT_ADDRESS", false);
                String cartResultJObjString = cartResultJObj.toString();
                bundle.putString("cartResultJObj", cartResultJObjString);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        billingAddressEditBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddAdressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("COME_FROM_WHERE", 1);
                bundle.putBoolean("EDIT_ADDRESS", true);

                bundle.putString("ADD_ID", billingAddItem.getaddressID());
                bundle.putString("FIRST_NAME", billingAddItem.getaddressFirstName());
                bundle.putString("LAST_NAME", billingAddItem.getaddressLastName());
                bundle.putString("COMPANY","");
                bundle.putString("ADD1",billingAddItem.getaddress1());
                bundle.putString("ADD2",billingAddItem.getaddress2());
                bundle.putString("COUNTRY_NAME",billingAddItem.getaddressCountry());
                bundle.putString("COUNTRY_ID",billingAddItem.getaddressCountryID());
                bundle.putString("STATE_NAME",billingAddItem.getaddressState());
                bundle.putString("STATE_ID",billingAddItem.getaddressStateID());
                bundle.putString("CITY_NAME",billingAddItem.getaddressCity());
                bundle.putString("POSTCODE_NAME",billingAddItem.getaddressPostCode());
                bundle.putString("PHONE", billingAddItem.getaddressPhone());
                String cartResultJObjString = cartResultJObj.toString();
                bundle.putString("cartResultJObj", cartResultJObjString);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        billingAddressAddBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddAdressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("COME_FROM_WHERE", 1);
                bundle.putBoolean("EDIT_ADDRESS", false);
                String cartResultJObjString = cartResultJObj.toString();
                bundle.putString("cartResultJObj", cartResultJObjString);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        billingAddressNextBtnIV = (ImageButton)header.findViewById(R.id.billingAddressNextBtnIV);

        billingAddressNextBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(UserID.length() > 0)
                {
                    GoToNextStepWS(SelectedDeliveryAddress,SelectedBillingAddress);
                }
                else
                {
                    Fragment fragment = new CheckOutMethodFragment();

                    Bundle args = new Bundle();
                    String cartResultJObjString = cartResultJObj.toString();

                    args.putString("cartResultJObj", cartResultJObjString);
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });


        billingAddressBottomNextBtnIV = (ImageButton)footer1.findViewById(R.id.billingAddressBottomNextBtnIV);
        billingAddressBottomNextBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(UserID.length() > 0)
                {
                    GoToNextStepWS(SelectedDeliveryAddress,SelectedBillingAddress);
                }
                else
                {
                    Fragment fragment = new CheckOutMethodFragment();

                    Bundle args = new Bundle();
                    String cartResultJObjString = cartResultJObj.toString();

                    args.putString("cartResultJObj", cartResultJObjString);
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });


        mBookends.addHeader(header);
        mBookends.addFooter(footer2);

        for(int i = 0; i <listArray_voucher.size(); i++)
        {
            RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view, mRecyclerView, false);

            TextView voucherCodeNameTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV);
            TextView voucherCodeNameTV1 = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV1);
            TextView voucherCodePriceLblTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceLblTV);
            TextView voucherCodePriceTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceTV);
            ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode.findViewById(R.id.voucherCodeRemove);

            voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeNameTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeRemove.setVisibility(View.GONE);

            voucherCodePriceLblTV.setText(SelectedCountryCurrency);
            voucherCodeNameTV.setText("(-) " + listArray_voucher.get(i).getvoucherCode());
            voucherCodeNameTV1.setText(listArray_voucher.get(i).getvoucherName());
            voucherCodePriceTV.setText(listArray_voucher.get(i).getvoucherReduceAmount());

            mBookends.addFooter(footerVoucherCode);
        }
        if(!taxAmount.equals("0.00")) {
            mBookends.addFooter(footer3);
        }

        mBookends.addFooter(footer);

        for(int i = 0; i <listArray_store_credit.size(); i++)
        {
            RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view, mRecyclerView, false);

            TextView voucherCodeNameTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV);
            TextView voucherCodeNameTV1 = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV1);
            TextView voucherCodePriceLblTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceLblTV);
            TextView voucherCodePriceTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceTV);
            ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode.findViewById(R.id.voucherCodeRemove);

            voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeNameTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeRemove.setVisibility(View.GONE);

            voucherCodePriceLblTV.setText(SelectedCountryCurrency);
            voucherCodeNameTV.setText("(-) " + listArray_store_credit.get(i).getvoucherCode());
            voucherCodeNameTV1.setText(listArray_store_credit.get(i).getvoucherName());
            voucherCodePriceTV.setText(listArray_store_credit.get(i).getvoucherReduceAmount());

            mBookends.addFooter(footerVoucherCode);
        }

        mBookends.addFooter(footer1);

        mRecyclerView.setAdapter(mBookends);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(listArray_address.size()!=0){
            updateOrderSummary();
        }
        updateAddressInfo();
        refreshRecycleView();
    }
}
