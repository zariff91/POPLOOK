package com.tiseno.poplook;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.carrierItem;
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
public class shippingMethodFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{
    Bookends<RecyclerView.Adapter> mBookends;
    String CartID,noInBag,totalPriceWt,totalPriceWt_sc,totalPrice,taxAmount,shipping_price,totalDiscount="0.00";

    JSONObject cartResultJObj,cartResultJObj1;
    float TOTALDIS;
    String appVersion="1.0.0";

    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<shoppingBagItem>();
    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();
    ArrayList<voucherItem> listArray_store_credit = new ArrayList<voucherItem>();
    ArrayList<addressItem> listArray_address = new ArrayList<addressItem>();
    ArrayList<addressItem> listArray_addressInvoice = new ArrayList<addressItem>();
    ArrayList<carrierItem> listArray_carrier = new ArrayList<carrierItem>();

    int SelectedDeliveryAddress;
    int SelectedBillingAddress;
    int SelectedCarrierPosition = 0;
    addressItem deliveryAddItem;
    addressItem billingAddItem;

    float totalAllProductPrice;
    float totalVoucherPrice;
    float totalPayable;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    RadioGroup shippingMethodRG;
    ImageButton shippingMethodNextBtnIV,shippingMethodBottomNextBtnIV;

    TextView totalInclGSTTV,shippingMethodTV,totalPayableTV,shippingAddressTV,shippingAddressContactNoTV,billingAddressInfoTV,billingAddressContactNoTV,subTotalTV,addGSTTV;
    TextView totalInclGSTTVlbl,totalInclGSTTVlblRM,shippingMethodTVlbl,subTotalLblTV,subTotalLblTVRM,addGSTLblTV,addGSTLblTVRM,addressfooterRM1,shippingMethodTVbill1;

    int fromGuestCheckOut;

    public shippingMethodFragment() {
        // Required empty public constructor
    }

    String SelectedCountryCurrency,SelectedShopID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_shipping_method, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Shipping Method");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);


        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.shippingMethodRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        CartID = pref.getString("CartID", "");
        noInBag = pref.getString("cartItem", "");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");
        //
        SelectedDeliveryAddress = getArguments().getInt("SelectedDeliveryAddress");
        SelectedBillingAddress = getArguments().getInt("SelectedBillingAddress");
        fromGuestCheckOut = getArguments().getInt("fromGuestCheckOut");


        getCartDetailList();

        return contentView;
    }
    private void getCartDetailList() {

        String cartResultJObjString = getArguments().getString("cartResultJObj");
        try {
            cartResultJObj = new JSONObject(cartResultJObjString);
            listArray_shoppingBag.clear();
            totalAllProductPrice = 0;
            int totalquantity = 0;

            String action = cartResultJObj.getString("action");

            if (action.equals("Carts_OrderStep2")) {
                if (cartResultJObj.getBoolean("status")) {
                    JSONObject data= cartResultJObj.getJSONObject("data");
                    JSONArray jsonArr = new JSONArray();
                    jsonArr = data.getJSONArray("product_list");
//                    CartID = cartResultJObj.getString("id_cart");
                    totalPriceWt = data.getString("totalProductsWt");
                    totalPriceWt_sc = data.getString("totalPrice_no_sc");
                    totalPrice = data.getString("totalPrice");
                    taxAmount = data.getString("taxCost");
                    totalDiscount =data.getString("totalDiscountsWt");
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
                            TOTALDIS=TOTALDIS+Float.parseFloat(voucherAmount);
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
                            TOTALDIS=TOTALDIS+Float.parseFloat(voucherAmount);
                            listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                        }
                    }catch (Exception e)
                    {

                    }

//                    if (fromGuestCheckOut == 1) {
//                        listArray_address.clear();
//
//                        System.out.println("add id is " + getArguments().getString("addressID"));
//
//                        listArray_address.add(new addressItem(getArguments().getString("addressID"), getArguments().getString("firstName"), getArguments().getString("lastName"), getArguments().getString("address1"), getArguments().getString("address2"), getArguments().getString("postcode"), getArguments().getString("city"), getArguments().getString("countryID"), getArguments().getString("country"), getArguments().getString("stateID"), getArguments().getString("state"), getArguments().getString("telephone")));
//
//                        refreshRecycleView();
//                    } else {
                    JSONObject object = new JSONObject();
                    object = data.getJSONObject("cart_list");
                    String addressIDDelivery=object.getString("id_address_delivery");
                    String addressIDInvoice=object.getString("id_address_invoice");

                        listArray_address.clear();


                        try {
                            String stateID="",address2="",company="",addressState="";
                            JSONObject jObj3 = new JSONObject();
                            jObj3 = data.getJSONObject("address_delivery");
                            ArrayList<String> addressArrayList = new ArrayList<String>();

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
                            String addressCountry = jObj3.getString("country");
                            if (jObj3.has("id_state")) {
                                stateID = jObj3.getString("id_state");
                            }
                            if (jObj3.has("state")) {
                                addressState = jObj3.getString("state");
                            }
                            String addressPhone = jObj3.getString("phone");


                                System.out.println("country issss " + jObj3.getString("country"));

                                listArray_address.add(new addressItem(addressIDDelivery, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                                addressArrayList.add(address1);



                        } catch (Exception e) {
                            System.out.println("Hello Ta LALU KE");
                        }
                    listArray_addressInvoice.clear();
                    try {
                        String stateID="",address2="",company="",addressState="";
                        JSONObject jObj3 = new JSONObject();
                        jObj3 = data.getJSONObject("address_invoice");

                        ArrayList<String> addressArrayList = new ArrayList<String>();

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
                        String addressCountry = jObj3.getString("country");
                        if (jObj3.has("id_state")) {
                            stateID = jObj3.getString("id_state");
                        }
                        if (jObj3.has("state")) {
                            addressState = jObj3.getString("state");
                        }
                        String addressPhone = jObj3.getString("phone");


                        System.out.println("country issss " + jObj3.getString("country"));

                        listArray_addressInvoice.add(new addressItem(addressIDDelivery, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                            addressArrayList.add(address1);



                    } catch (Exception e) {
                        System.out.println("Hello Ta LALU KE2");

                    }

                    listArray_carrier.clear();

                    try {
                        JSONArray jsonArr2 = new JSONArray();
                        jsonArr2 = data.getJSONArray("carrier_list");


                        for (int i = 0; i < jsonArr2.length(); i++) {
                            JSONObject jObj = jsonArr2.getJSONObject(i);
                            String carrierID = jObj.getString("id_carrier");
                            String carrierName = jObj.getString("name");
                            String carrierPrice = jObj.getString("price");

                            if (i > 0 && i < jsonArr2.length()) {

                                JSONObject jObj2 = jsonArr2.getJSONObject(i - 1);
                                String prevCarrierID = jObj2.getString("id_carrier");
                                String prevCarrierName = jObj2.getString("name");
                                String prevCarrierPrice = jObj2.getString("price");


                                if (carrierID.equals(prevCarrierID)) {
                                    if (Float.parseFloat(carrierPrice) > Float.parseFloat(prevCarrierPrice)) {
                                        listArray_carrier.add(new carrierItem(carrierID, carrierName, carrierPrice));
                                        carrierItem item = listArray_carrier.get(listArray_carrier.size() - 2);
                                        listArray_carrier.remove(item);
                                    }
                                } else {
                                    listArray_carrier.add(new carrierItem(carrierID, carrierName, carrierPrice));

                                }
                            } else {
                                listArray_carrier.add(new carrierItem(carrierID, carrierName, carrierPrice));
                            }
                        }
                        refreshRecycleView();

                    }catch (Exception e){}
                    mBookends.notifyDataSetChanged();


                }
            }else if (action.equals("checkoutAsGuest")) {
                if (cartResultJObj.getBoolean("status")) {
                    JSONObject data= cartResultJObj.getJSONObject("data");
                    JSONArray jsonArr = new JSONArray();
                    jsonArr = data.getJSONArray("product_list");
//                    CartID = cartResultJObj.getString("id_cart");
                    totalPriceWt = data.getString("totalProductsWt");
                    totalPriceWt_sc = data.getString("totalPrice_no_sc");
                    totalPrice = data.getString("totalPrice");
                    taxAmount = data.getString("taxCost");
                    totalDiscount =data.getString("totalDiscountsWt");
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
                            TOTALDIS=TOTALDIS+Float.parseFloat(voucherAmount);
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
                            TOTALDIS=TOTALDIS+Float.parseFloat(voucherAmount);
                            listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                        }
                    }catch (Exception e)
                    {

                    }

//                    if (fromGuestCheckOut == 1) {
//                        listArray_address.clear();
//
//                        System.out.println("add id is " + getArguments().getString("addressID"));
//
//                        listArray_address.add(new addressItem(getArguments().getString("addressID"), getArguments().getString("firstName"), getArguments().getString("lastName"), getArguments().getString("address1"), getArguments().getString("address2"), getArguments().getString("postcode"), getArguments().getString("city"), getArguments().getString("countryID"), getArguments().getString("country"), getArguments().getString("stateID"), getArguments().getString("state"), getArguments().getString("telephone")));
//
//                        refreshRecycleView();
//                    } else {
                    JSONObject object = new JSONObject();
                    object = data.getJSONObject("cart_list");
                    String addressIDDelivery=object.getString("id_address_delivery");
                    String addressIDInvoice=object.getString("id_address_invoice");

                    listArray_address.clear();


                    try {
                        String stateID="",address2="",company="",addressState="";
                        JSONObject jObj3 = new JSONObject();
                        jObj3 = data.getJSONObject("address_delivery");
                        ArrayList<String> addressArrayList = new ArrayList<String>();

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
                        String addressCountry = jObj3.getString("country");
                        if (jObj3.has("id_state")) {
                            stateID = jObj3.getString("id_state");
                        }
                        if (jObj3.has("state")) {
                            addressState = jObj3.getString("state");
                        }
                        String addressPhone = jObj3.getString("phone");


                        System.out.println("country issss " + jObj3.getString("country"));

                        listArray_address.add(new addressItem(addressIDDelivery, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                        addressArrayList.add(address1);



                    } catch (Exception e) {
                        System.out.println("Hello Ta LALU KE");
                    }
                    listArray_addressInvoice.clear();
                    try {
                        String stateID="",address2="",company="",addressState="";
                        JSONObject jObj3 = new JSONObject();
                        jObj3 = data.getJSONObject("address_invoice");

                        ArrayList<String> addressArrayList = new ArrayList<String>();

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
                        String addressCountry = jObj3.getString("country");
                        if (jObj3.has("id_state")) {
                            stateID = jObj3.getString("id_state");
                        }
                        if (jObj3.has("state")) {
                            addressState = jObj3.getString("state");
                        }
                        String addressPhone = jObj3.getString("phone");


                        System.out.println("country issss " + jObj3.getString("country"));

                        listArray_addressInvoice.add(new addressItem(addressIDDelivery, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                        addressArrayList.add(address1);



                    } catch (Exception e) {
                        System.out.println("Hello Ta LALU KE2");

                    }

                    listArray_carrier.clear();

                    try {
                        JSONArray jsonArr2 = new JSONArray();
                        jsonArr2 = data.getJSONArray("carrier_list");


                        for (int i = 0; i < jsonArr2.length(); i++) {
                            JSONObject jObj = jsonArr2.getJSONObject(i);
                            String carrierID = jObj.getString("id_carrier");
                            String carrierName = jObj.getString("name");
                            String carrierPrice = jObj.getString("price");

                            if (i > 0 && i < jsonArr2.length()) {

                                JSONObject jObj2 = jsonArr2.getJSONObject(i - 1);
                                String prevCarrierID = jObj2.getString("id_carrier");
                                String prevCarrierName = jObj2.getString("name");
                                String prevCarrierPrice = jObj2.getString("price");


                                if (carrierID.equals(prevCarrierID)) {
                                    if (Float.parseFloat(carrierPrice) > Float.parseFloat(prevCarrierPrice)) {
                                        listArray_carrier.add(new carrierItem(carrierID, carrierName, carrierPrice));
                                        carrierItem item = listArray_carrier.get(listArray_carrier.size() - 2);
                                        listArray_carrier.remove(item);
                                    }
                                } else {
                                    listArray_carrier.add(new carrierItem(carrierID, carrierName, carrierPrice));

                                }
                            } else {
                                listArray_carrier.add(new carrierItem(carrierID, carrierName, carrierPrice));
                            }
                        }
                        refreshRecycleView();

                    }catch (Exception e){}
                    mBookends.notifyDataSetChanged();


                }
            }


        }catch (Exception e) {
        }
    }

    private void refreshRecycleView()
    {
        RVAdapter = new shoppingBagAdapter(getActivity(),listArray_shoppingBag,null,false);
        // Make Bookends
       mBookends = new Bookends<>(RVAdapter);

        // Inflate footer view
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

        RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.shipping_method_header_view, mRecyclerView, false);

        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.shipping_method_footer_view, mRecyclerView, false);
        RelativeLayout footer1 = (RelativeLayout) inflater1.inflate(R.layout.shipping_method_footer_view1, mRecyclerView, false);
        RelativeLayout footer2 = (RelativeLayout) inflater1.inflate(R.layout.shipping_method_footer_first_view, mRecyclerView, false);
        RelativeLayout footer3 = (RelativeLayout) inflater1.inflate(R.layout.shipping_method_footer_second_view, mRecyclerView, false);

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

        shippingMethodRG = (RadioGroup) header.findViewById(R.id.shippingMethodRG);
        TextView chooseDeliveryTV = (TextView) header.findViewById(R.id.chooseDeliveryTV);
        TextView nextTV = (TextView) header.findViewById(R.id.nextTV);
        TextView orderSummaryTV = (TextView) header.findViewById(R.id.orderSummaryTV);

        orderSummaryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        nextTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        chooseDeliveryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        RadioGroup.LayoutParams rprms;
        System.out.println("sizee 2is " + listArray_carrier.size());
        for (int i = 0; i < listArray_carrier.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            if (listArray_carrier.get(i).getcarrierPrice().equals("0.00")) {
                if(listArray_carrier.get(i).getcarrierName()==null||listArray_carrier.get(i).getcarrierName().equals("")||listArray_carrier.get(i).getcarrierName().isEmpty()){
                    radioButton.setText("Poslaju (FREE)");
                }else {
                    radioButton.setText(listArray_carrier.get(i).getcarrierName());
                }
            }else {
                radioButton.setText(listArray_carrier.get(i).getcarrierName());
            }
            radioButton.setId(i);
            radioButton.setButtonDrawable(R.drawable.checkbox_layout);
            radioButton.setPadding(30, 0, 0, 15);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            radioButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            shippingMethodRG.addView(radioButton, rprms);
        }

        shippingMethodRG.check(SelectedCarrierPosition);
                    shippingMethodRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup arg0, int id) {
                            switch (id) {
                                case -1:
                                    Log.v("LOL", "Choices cleared!");
                                    break;
                                case 0:
                                    Log.v("LOL", "Chose 1");

                                    shipping_price=listArray_carrier.get(0).getcarrierPrice();
                                    SelectedCarrierPosition=0;
                                    totalPayable=Float.parseFloat(totalPriceWt);
                                    totalPayable=totalPayable+Float.parseFloat(taxAmount);
                                    totalPrice=String.valueOf(totalPayable-TOTALDIS+Float.parseFloat(shipping_price));
                                    refreshRecycleView();
                                    break;
                                case 1:
                                    Log.v("LOL", "Chose 2");
                                    shipping_price=listArray_carrier.get(1).getcarrierPrice();
                                    SelectedCarrierPosition=1;
                                    totalPayable=Float.parseFloat(totalPriceWt);
                                    totalPayable=totalPayable+Float.parseFloat(taxAmount);
                                    totalPrice=String.valueOf(totalPayable-TOTALDIS+Float.parseFloat(shipping_price));
                                    refreshRecycleView();
                                    break;
                                case 2:
                                    Log.v("LOL", "Chose 3");
                                    shipping_price=listArray_carrier.get(2).getcarrierPrice();
                                    SelectedCarrierPosition=2;
                                    totalPayable=Float.parseFloat(totalPriceWt);
                                    totalPayable=totalPayable+Float.parseFloat(taxAmount);
                                    totalPrice=String.valueOf(totalPayable-TOTALDIS+Float.parseFloat(shipping_price));
                                    refreshRecycleView();
                                    break;
                                case 3:
                                    Log.v("LOL", "Chose 4");
                                    shipping_price=listArray_carrier.get(3).getcarrierPrice();
                                    SelectedCarrierPosition=3;
                                    totalPayable=Float.parseFloat(totalPriceWt);
                                    totalPayable=totalPayable+Float.parseFloat(taxAmount);
                                    totalPrice=String.valueOf(totalPayable-TOTALDIS+Float.parseFloat(shipping_price));
                                    refreshRecycleView();
                                    break;
                                default:
                                    shipping_price=listArray_carrier.get(SelectedCarrierPosition).getcarrierPrice();
                                    Log.v("LOL", "Huh?");
                                    break;
                            }
                        }
                    });

        TextView totalPayableRMTV = (TextView) footer1.findViewById(R.id.totalPayableRMTV);
        TextView totalPayableRM1TV = (TextView) footer1.findViewById(R.id.totalPayableRM1TV);
        TextView shippingAddressLabel = (TextView) footer1.findViewById(R.id.shippingAddressLabel);
        TextView shippingAddressContactNoLblTV = (TextView) footer1.findViewById(R.id.shippingAddressContactNoLblTV);
        TextView billingAddressLabel = (TextView) footer1.findViewById(R.id.billingAddressLabel);
        TextView billingAddressContactNoLblTV = (TextView) footer1.findViewById(R.id.billingAddressContactNoLblTV);

        RelativeLayout totalInclGSTRL = (RelativeLayout) footer.findViewById(R.id.totalInclGSTRL);

        shippingAddressTV = (TextView) footer1.findViewById(R.id.shippingAddressTV);
        billingAddressInfoTV = (TextView) footer1.findViewById(R.id.billingAddressInfoTV);
        shippingAddressContactNoTV = (TextView) footer1.findViewById(R.id.shippingAddressContactNoTV);
        billingAddressContactNoTV = (TextView) footer1.findViewById(R.id.billingAddressContactNoTV);
        totalInclGSTTVlbl  = (TextView) footer.findViewById(R.id.totalInclGSTTVlbl);
        totalInclGSTTVlblRM  = (TextView) footer.findViewById(R.id.totalInclGSTTVlblRM);
        shippingMethodTVlbl  = (TextView) footer.findViewById(R.id.shippingMethodTVlbl);
        shippingMethodTV  = (TextView) footer.findViewById(R.id.shippingMethodTV);
        addressfooterRM1 = (TextView) footer.findViewById(R.id.addressfooterRM1);
        shippingMethodTVbill1 = (TextView) footer.findViewById(R.id.shippingMethodTVbill1);
        totalInclGSTTV = (TextView) footer.findViewById(R.id.totalInclGSTTV);
        totalPayableTV = (TextView) footer1.findViewById(R.id.totalPayableTV);

        totalPayableRMTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalPayableRM1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        shippingAddressLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressContactNoLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressContactNoLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressInfoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        subTotalTV = (TextView) footer2.findViewById(R.id.subTotalTV);
        addGSTTV = (TextView) footer3.findViewById(R.id.addGSTTV);
        subTotalLblTV = (TextView) footer2.findViewById(R.id.subTotalLblTV);
        subTotalLblTVRM = (TextView) footer2.findViewById(R.id.subTotalLblTVRM);
        addGSTLblTV = (TextView) footer3.findViewById(R.id.addGSTLblTV);
        addGSTLblTVRM = (TextView) footer3.findViewById(R.id.addGSTLblTVRM);

        addressfooterRM1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodTVbill1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        subTotalTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalInclGSTTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalPayableTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        subTotalLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        subTotalLblTVRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTLblTVRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalInclGSTTVlbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalInclGSTTVlblRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodTVlbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        totalPayableRM1TV.setText(SelectedCountryCurrency);
        subTotalLblTVRM.setText(SelectedCountryCurrency);

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


        subTotalTV.setText(String.format("%.2f",Double.parseDouble(totalPriceWt)));
        if(taxAmount.equals("0.00")){
            totalInclGSTRL.setVisibility(View.GONE);
        }else{
            totalInclGSTRL.setVisibility(View.VISIBLE);
            addGSTTV.setText(taxAmount);
        }


        if (totalPrice.equalsIgnoreCase("0.00")){
            totalPayableTV.setText("0.00");
        }else if(!totalPrice.contains(".")) {
            totalPayableTV.setText(totalPrice+".00");

            float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
            System.out.print("GST" + rounded);
        }else if(Float.parseFloat(totalPrice)<0.00){
            totalPayableTV.setText("0.00");
        }else{
            totalPrice=String.format("%.2f", Double.parseDouble(totalPrice));
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

        shippingMethodNextBtnIV = (ImageButton) header.findViewById(R.id.shippingMethodNextBtnIV);


        shippingMethodNextBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectedCarrierPosition = shippingMethodRG.getCheckedRadioButtonId();
                System.out.print("GST" + SelectedCarrierPosition);
                if (SelectedCarrierPosition != -1) {
                    String CarrierID = listArray_carrier.get(SelectedCarrierPosition).getcarrierID();
                    GoToNextStepWS(CarrierID);
                }

            }
        });

        shippingMethodBottomNextBtnIV = (ImageButton) footer1.findViewById(R.id.shippingBottomNextBtnIV);
        shippingMethodBottomNextBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectedCarrierPosition = shippingMethodRG.getCheckedRadioButtonId();
                System.out.print("GST" + SelectedCarrierPosition);
                if (SelectedCarrierPosition != -1) {
                    String CarrierID = listArray_carrier.get(SelectedCarrierPosition).getcarrierID();
                    GoToNextStepWS(CarrierID);
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

        System.out.println("sizee is " + listArray_address.size());
        System.out.println("aaa is "+SelectedDeliveryAddress);


        deliveryAddItem = listArray_address.get(0);
        billingAddItem = listArray_addressInvoice.get(0);

        String deliveryAddFirstName = deliveryAddItem.getaddressFirstName();
        String deliveryAddLastName = deliveryAddItem.getaddressLastName();
        String deliveryAdd1 = deliveryAddItem.getaddress1();
        String deliveryAdd2 = deliveryAddItem.getaddress2();
        String deliveryAddPostCode = deliveryAddItem.getaddressPostCode();
        String deliveryAddCity = deliveryAddItem.getaddressCity();
        String deliveryAddCountry = deliveryAddItem.getaddressCountry();
        String deliveryAddState = deliveryAddItem.getaddressState();
        String deliveryAddPhone = deliveryAddItem.getaddressPhone();

        String billingAddFirstName = billingAddItem.getaddressFirstName();
        String billingAddLastName = billingAddItem.getaddressLastName();
        String billingAdd1 = billingAddItem.getaddress1();
        String billingAdd2 = billingAddItem.getaddress2();
        String billingAddPostCode = billingAddItem.getaddressPostCode();
        String billingAddCity = billingAddItem.getaddressCity();
        String billingAddCountry = billingAddItem.getaddressCountry();
        String billingAddState = billingAddItem.getaddressState();
        String billingAddPhone = billingAddItem.getaddressPhone();

        shippingAddressTV.setText(deliveryAddFirstName + " " + deliveryAddLastName + "\n" + deliveryAdd1 + " " + deliveryAdd2 + "\n" + deliveryAddPostCode + " " + deliveryAddCity + "\n" + deliveryAddState + "\n" + deliveryAddCountry);
        shippingAddressContactNoTV.setText(deliveryAddPhone);

        billingAddressInfoTV.setText(billingAddFirstName + " " + billingAddLastName + "\n" + billingAdd1 + " " + billingAdd2 + "\n" + billingAddPostCode + " " + billingAddCity + "\n" + billingAddState + "\n" + billingAddCountry);
        billingAddressContactNoTV.setText(billingAddPhone);



    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                String action = result.getString("action");

                if(action.equals("Carts_OrderStep3"))
                {   cartResultJObj1=result;
                    if(result.getBoolean("status"))
                    {
                        JSONObject data = result.getJSONObject("data");
                        String nextPage = data.getString("next_page");

                        if(nextPage.equals("paymentSelectionPage"))
                        {
                            Fragment fragment = new PaymentFragment();
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment,"PaymentFragment");
                            fragmentTransaction.addToBackStack(null);

                            System.out.println("hello0000 :"+cartResultJObj1+":"+fromGuestCheckOut+":"+":"+SelectedDeliveryAddress+":"+SelectedBillingAddress+":"+SelectedCarrierPosition);


                            Bundle args = new Bundle();
                            String cartResultJObjString = cartResultJObj1.toString();
                            args.putString("fromOrderHistory","0");

                            if(fromGuestCheckOut == 0)
                            {
                                System.out.println("hello3 sent" +cartResultJObjString);

                                args.putString("cartResultJObj", cartResultJObjString);
                                args.putInt("SelectedDeliveryAddress", SelectedDeliveryAddress);
                                args.putInt("SelectedBillingAddress",SelectedBillingAddress);
                                args.putInt("SelectedCarrierPosition", SelectedCarrierPosition);
                                fragment.setArguments(args);
                            }
                            else
                            {
                                System.out.println("hello");

                                args.putString("addressID", getArguments().getString("addressID"));
                                args.putString("firstName", getArguments().getString("firstName"));
                                args.putString("lastName",getArguments().getString("lastName"));
                                args.putString("company",getArguments().getString("company"));
                                args.putString("address1",getArguments().getString("address1"));
                                args.putString("address2",getArguments().getString("address2"));
                                args.putString("city",getArguments().getString("city"));
                                args.putString("postcode",getArguments().getString("postcode"));
                                args.putString("country",getArguments().getString("country"));
                                args.putString("countryID",getArguments().getString("countryID"));
                                args.putString("state",getArguments().getString("state"));
                                args.putString("stateID",getArguments().getString("stateID"));
                                args.putString("telephone", getArguments().getString("telephone"));

                                System.out.println("hello1");


                                args.putInt("fromGuestCheckOut", 1);
                                args.putString("cartResultJObj", cartResultJObjString);
                                args.putInt("SelectedDeliveryAddress",0);
                                args.putInt("SelectedBillingAddress",0);
                                args.putInt("SelectedCarrierPosition", SelectedCarrierPosition);
                                fragment.setArguments(args);

                                System.out.println("hello2");

                            }

                            System.out.println("hello4");


                            fragmentTransaction.commit();
                        }
                        else if(nextPage.equals("orderHistory"))
                        {
                            Fragment fragment = new OrderHistoryFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("CartID", "");
                            editor.putString("cartItem", "0");
                            editor.putString("PaymentDone", "1");
                            editor.commit();

                            ((MainActivity) getActivity()).changeToolBarBagNotiText("0");
                        }
                        else if(nextPage.equals("cart"))
                        {
                            Fragment fragment = new ShoppingBagFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
                            fragmentTransaction.commit();
                        }



                    }
                    else
                    {
                        String message = result.getString("message");
                        new AlertDialog.Builder(getActivity())
                                .setTitle("POPLOOK")
                                .setMessage(message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Fragment fragment = new ShoppingBagFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                }).show();
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

    private void GoToNextStepWS(String carrierID)
    {
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //get the app version Name for display
        appVersion = pInfo.versionName;
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Carts/OrderStep3?apikey="+apikey+"&id_cart="+CartID+"&id_address_delivery="+deliveryAddItem.getaddressID()+"&id_carrier="+carrierID+"&device_type=android&app_version="+appVersion;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);
    }

    private void reloadFragment(){
        Fragment fragment = new shippingMethodFragment();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);


        Bundle args = new Bundle();
        String cartResultJObjString = cartResultJObj.toString();
        args.putString("cartResultJObj", cartResultJObjString);
        args.putInt("SelectedDeliveryAddress", SelectedDeliveryAddress);
        args.putInt("SelectedBillingAddress",SelectedBillingAddress);
        fragment.setArguments(args);

        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        shipping_price=listArray_carrier.get(SelectedCarrierPosition).getcarrierPrice();

//        totalPayable=Float.parseFloat(totalPriceWt);
//        totalPayable=totalPayable+Float.parseFloat(taxAmount);
//        totalPrice=String.valueOf(totalPayable-TOTALDIS+Float.parseFloat(shipping_price));
//
//        System.out.println("TOTALPRICEWT "+totalPriceWt);
//        System.out.println("taxAmount "+taxAmount);
//        System.out.println("TOTALDIS "+TOTALDIS);
//        System.out.println("shipping_price "+shipping_price);
//        System.out.println("totalPrice "+totalPrice);

         refreshRecycleView();
    }
}
