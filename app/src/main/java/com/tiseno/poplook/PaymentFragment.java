package com.tiseno.poplook;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import com.google.android.material.appbar.AppBarLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.carrierItem;
import com.tiseno.poplook.functions.shoppingBagAdapter;
import com.tiseno.poplook.functions.shoppingBagItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    String UserID, CartID, LanguageID,noInBag,totalPriceWt,totalPriceWt_sc,totalPrice,taxAmount,shipping_price;

    String bank_msg_enable,first_bank_message,second_bank_message;

    JSONObject cartResultJObj;
    JSONObject refreshStep3Obj;
    JSONObject CarrierResultObj;
    String skipTimer="0",extra_cart="0";
    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<shoppingBagItem>();
    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();
    ArrayList<voucherItem> listArray_store_credit = new ArrayList<voucherItem>();
    ArrayList<addressItem> listArray_address = new ArrayList<addressItem>();
    ArrayList<addressItem> listArray_addressInvoice = new ArrayList<addressItem>();
    ArrayList<carrierItem> listArray_carrier = new ArrayList<carrierItem>();

    String carrier_selected = "";

    int SelectedDeliveryAddress;
    int SelectedBillingAddress;
    int SelectedCarrierPosition;
    int fromGuestCheckOut;
    addressItem deliveryAddItem;
    addressItem billingAddItem;

    String FinalPrice;

    //VARIABLE FROM ORDER HISTORY//

    String fromOrderHistory;
    String carrier;
    String cartIDFromOrderHistory;

    String shipping_addressFirstName;
    String shipping_addressLastName;
    String shipping_address1;
    String shipping_address2;
    String shipping_addressPostCode;
    String shipping_addressCity;
    String shipping_addressCountry;
    String shipping_addressState;
    String shipping_addressPhone;

    String billing_addressFirstName;
    String billing_addressLastName;
    String billing_address1;
    String billing_address2;
    String billing_addressPostCode;
    String billing_addressCity;
    String billing_addressCountry;
    String billing_addressState;
    String billing_addressPhone;

    String carrier_id_API;
    String delivery_id_API;

    //////////////////////////////

    float totalAllProductPrice;
    float totalVoucherPrice;

    boolean TermCondChecked = false;

    boolean isCCSelected = false;

    boolean eWalletExist = false;
    boolean creditCardExist = false;
    boolean onlineBankingExist = false;

    boolean refreshVoucher = false;


    Bookends<RecyclerView.Adapter> mBookends;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    RelativeLayout codeVoucherRL;

    ImageView paymentTSCheckIV;
    ImageButton paymentNextBtnIV,paymentBottomNextBtnIV;
//    RadioGroup paymentRG;

    RadioGroup paymentGroup;
    RadioButton paymentList;
    RadioButton paymentList2;
    RadioButton paymentList3;
    RadioButton paymentList4;

    ImageButton applyCodeBtn;

    EditText codeInputET;


    TextView eWalletTxt,onlineBankTxt;

    TextView bank_first_message,bank_second_message;

    TextView code_textView,applyButton_textview;

    int selectedEWallet;
    int selectedBanking;



    NumberPicker onlineBankingNP;
    ImageView onlineBankingDropDownIV,EWalletDropDownIV;
    RelativeLayout onlineBankingRL;
    TextView onlineBankingTV,eWalletTV,shippingMethodTVlbl,totalInclGSTTVlblRM,totalInclGSTTVlbl;

    Toolbar toolbar;

    ImageButton onlineBankingSelectIB;

    TextView paymentTSIV,termsTV,privacyTV,andTV,belowTermsTV,subTotalLblTV,subTotalLblTVRM,addGSTLblTV,addGSTLblTVRM,shippingMethodTV,addressfooterRM1,shippingMethodTVbill1,totalPayableRMTV,totalPayableRM1TV;

    String PaymentIDForIPay88 = "",SelectedShopID,SelectedCountryCurrency;

    String eWalletType = "";
    String eWalletID = "";


    String[] onlinebankingarray;
    String[] onlinebankingarrayValue;

    String[] EWalletgarray;
    String[] EWalletarrayValue;

    TextView subTotalTV,addGSTTV,totalInclGSTTV,totalPayableTV,shippingAddressTV,shippingAddressContactNoTV,billingAddressInfoTV,billingAddressContactNoTV,shippingMethodInfoTV;

    int PAYMENT_METHOD;
    final int CREDIT_CARD_PAYMENT = 0;
    final int ONLINE_BANKING_PAYMENT = 1;
    final int PAYPAL_PAYMENT = 2;
    final int ENETS_PAYMENT = 3;
    final int TNG_EWALLET = 4;


    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_payment, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Payment");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.paymentRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SelectedDeliveryAddress = getArguments().getInt("SelectedDeliveryAddress");
        SelectedBillingAddress = getArguments().getInt("SelectedBillingAddress");
        SelectedCarrierPosition = getArguments().getInt("SelectedCarrierPosition");

        carrier_id_API = getArguments().getString("carrier_id_api");
        delivery_id_API = getArguments().getString("delivery_address_id");

        fromGuestCheckOut = getArguments().getInt("fromGuestCheckOut");

        //FROM USER HISTORY//

        fromOrderHistory = getArguments().getString("fromOrderHistory");
        carrier = getArguments().getString("carrier");
        cartIDFromOrderHistory = getArguments().getString("cartID");

        //////////////////////

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "");
        noInBag = pref.getString("cartItem", "");
        LanguageID = pref.getString("LanguageID", "");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");
        //
        TextView selectTV = (TextView) contentView.findViewById(R.id.selectTV);

        selectTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        onlineBankingRL = (RelativeLayout) contentView.findViewById(R.id.onlineBankingRL);
        onlineBankingNP = (NumberPicker) contentView.findViewById(R.id.onlineBankingNP);
        onlineBankingSelectIB = (ImageButton) contentView.findViewById(R.id.onlineBankingSelectIB);

        toolbar = (Toolbar)getActivity().findViewById(R.id.tool_bar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);

        if(!fromOrderHistory.equals("1"))
        {   getCartDetailList1();
        }else{
            getCartDetailList();
        }

        return contentView;
    }

    private void getCartDetailList1() {
        String cartResultJObjString = getArguments().getString("cartResultJObj");
        try {

            if(refreshVoucher){
                cartResultJObj = refreshStep3Obj;
            }
            else {
                cartResultJObj = new JSONObject(cartResultJObjString);
            }
            listArray_shoppingBag.clear();
            totalAllProductPrice = 0;
            int totalquantity = 0;

            String action = cartResultJObj.getString("action");
            if (action.equals("Orders_PayNowFromOrder")) {
                skipTimer="1";
                extra_cart="1";
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

                    if(data.has("enable_bank_msg"))
                    {
                        bank_msg_enable = data.getString("enable_bank_msg");

                        if(bank_msg_enable.equals("1")){
                            first_bank_message = data.getString("bank_bsn_message");
                            second_bank_message = data.getString("bank_cimb_message");
                        }
                    }

                    else {
                        bank_msg_enable = "0";
                        first_bank_message = "false";
                        second_bank_message = "false";
                    }


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
                        String productRef = jObj.getString("reference");
                        String quantity = jObj.getString("quantity");
                        String item_total = jObj.getString("total");
//                        Integer quantity_available = jObj.getInt("quantity_available");

                        int tot=Integer.parseInt(quantity);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        Date d1 = format.parse(current_date);

                        //in milliseconds
//                            if(diff > 0)
//                            {
                        if(!discountedPrice.equals("0"))
                        {
                            price = discountedPrice;
                        }

                        price = String.format("%.2f", Float.parseFloat(price));


                        String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                        totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);
                        listArray_shoppingBag.add(new shoppingBagItem(CartID,productAttributeID,productID,imageURL,productName,productSize,productRef,price,item_total,quantity,"",1));


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
                    CartID=object.getString("id");
                    listArray_address.clear();

                    try {
                        JSONObject jObj3 = new JSONObject();
                        jObj3 = data.getJSONObject("address_delivery");
                        ArrayList<String> addressArrayList = new ArrayList<String>();

                        String company = jObj3.getString("company");
                        String id_gender = jObj3.getString("id_gender");
                        String addressFirstName = jObj3.getString("firstname");
                        String addressLastName = jObj3.getString("lastname");
                        String address1 = jObj3.getString("address1");
                        address1=address1.replace("\\","");
                        String address2 = jObj3.getString("address2");
                        address2=address2.replace("\\","");
                        String addressPostCode = jObj3.getString("postcode");
                        String addressCity = jObj3.getString("city");
                        String countryID = jObj3.getString("id_country");
                        String addressCountry = jObj3.getString("country");
                        String stateID = jObj3.getString("id_state");
                        String addressState = jObj3.getString("state");
                        String addressPhone = jObj3.getString("phone");

                        System.out.println("country issss " + jObj3.getString("country"));

                        listArray_address.add(new addressItem(addressIDDelivery, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                        addressArrayList.add(address1);



                    } catch (Exception e) {
                        System.out.println("Hello Ta LALU KE");
                    }
                    listArray_addressInvoice.clear();
                    try {
                        JSONObject jObj3 = new JSONObject();
                        jObj3 = data.getJSONObject("address_invoice");

                        ArrayList<String> addressArrayList = new ArrayList<String>();

                        String company = jObj3.getString("company");
                        String id_gender = jObj3.getString("id_gender");
                        String addressFirstName = jObj3.getString("firstname");
                        String addressLastName = jObj3.getString("lastname");
                        String address1 = jObj3.getString("address1");
                        address1=address1.replace("\\","");
                        String address2 = jObj3.getString("address2");
                        address2=address2.replace("\\","");
                        String addressPostCode = jObj3.getString("postcode");
                        String addressCity = jObj3.getString("city");
                        String countryID = jObj3.getString("id_country");
                        String addressCountry = jObj3.getString("country");
                        String stateID = jObj3.getString("id_state");
                        String addressState = jObj3.getString("state");
                        String addressPhone = jObj3.getString("phone");

                        System.out.println("country issss " + jObj3.getString("country"));

                        listArray_addressInvoice.add(new addressItem(addressIDInvoice, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,company,id_gender));

                        addressArrayList.add(address1);



                    } catch (Exception e) {
                        System.out.println("Hello Ta LALU KE2");

                    }


                    try {
                        JSONArray jsonArr3 = new JSONArray();
                        jsonArr3 = data.getJSONArray("payment_list");

                        System.out.println("get payment type = " + jsonArr3);


                        for(int i = 0;i< jsonArr3.length();i++)
                        {
                            String paymentType;

                            JSONObject paymentObj = jsonArr3.getJSONObject(i);
                            paymentType = paymentObj.getString("name");

                            if(paymentType.equals("E-Wallet"))
                            {

                                eWalletExist = true;

                                System.out.println("get hereerer");


                                JSONArray jsonEwallet = new JSONArray();
                                jsonEwallet = jsonArr3.getJSONObject(i).getJSONArray("options");

                                EWalletgarray = new String[jsonEwallet.length()];
                                EWalletarrayValue = new String[jsonEwallet.length()];

                                for (int x = 0; x < jsonEwallet.length(); x++) {
                                    JSONObject jObj = jsonEwallet.getJSONObject(x);
                                    EWalletgarray[x]=jObj.getString("name");
                                    EWalletarrayValue[x]=jObj.getString("value");

                                }
                            }

                            if(paymentType.equals("Credit Card"))
                            {

                                creditCardExist = true;

                            }

                            if(paymentType.equals("Online Banking"))
                            {

                                onlineBankingExist = true;

                                JSONArray jsonArrOnlineBanking = new JSONArray();
                                jsonArrOnlineBanking = jsonArr3.getJSONObject(i).getJSONArray("options");


                                onlinebankingarray = new String[jsonArrOnlineBanking.length()];
                                onlinebankingarrayValue = new String[jsonArrOnlineBanking.length()];

                                for (int x = 0; x < jsonArrOnlineBanking.length(); x++) {
                                    JSONObject jObj = jsonArrOnlineBanking.getJSONObject(x);
                                    onlinebankingarray[x]=jObj.getString("name");
                                    onlinebankingarrayValue[x]=jObj.getString("value");

                                }

                            }
                        }

                    }catch (Exception e){
                        System.out.println("Hello Ta LALUList KE3");


                    }


                    listArray_carrier.clear();

                    try {
                        JSONArray jsonArr2 = new JSONArray();
                        jsonArr2 = data.getJSONArray("carrier_list");

                        System.out.println("carrier list here = " + jsonArr2);

                        if(data.has("carrier_name")){
                            carrier_selected = data.getString("carrier_name");
                        }

                        else {
                            JSONObject jObj = jsonArr2.getJSONObject(0);
                            String carrierID = jObj.getString("name");
                            carrier_selected = carrierID;
                        }

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

                    }catch (Exception e){}

                    refreshRecycleView();
                    mBookends.notifyDataSetChanged();

                }
                refreshRecycleView();
                mBookends.notifyDataSetChanged();
            }else if(action.equals("Carts_OrderStep3")){
                skipTimer="0";
                extra_cart="0";
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

                    bank_msg_enable = data.getString("enable_bank_msg");

                    if(bank_msg_enable.equals("1")){
                        first_bank_message = data.getString("bank_bsn_message");
                        second_bank_message = data.getString("bank_cimb_message");
                    }

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

                        if(!discountedPrice.equals("0"))
                        {
                            price = discountedPrice;
                        }

                        price = String.format("%.2f", Float.parseFloat(price));


                        String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                        totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);


                        listArray_shoppingBag.add(new shoppingBagItem(CartID,productAttributeID,productID,imageURL,productName,productSize,productRef,price,item_total,quantity,timeRem,quantity_available));

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
                        JSONObject jObj3 = new JSONObject();
                        jObj3 = data.getJSONObject("address_delivery");
                        ArrayList<String> addressArrayList = new ArrayList<String>();

                        String addressFirstName = jObj3.getString("firstname");
                        String addressLastName = jObj3.getString("lastname");
                        String address1 = jObj3.getString("address1");
                        address1=address1.replace("\\","");
                        String address2 = jObj3.getString("address2");
                        address2=address2.replace("\\","");
                        String addressPostCode = jObj3.getString("postcode");
                        String addressCity = jObj3.getString("city");
                        String countryID = jObj3.getString("id_country");
                        String addressCountry = jObj3.getString("country");
                        String stateID = jObj3.getString("id_state");
                        String addressState = jObj3.getString("state");
                        String addressPhone = jObj3.getString("phone");

                        System.out.println("country issss " + jObj3.getString("country"));

                        listArray_address.add(new addressItem(addressIDDelivery, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,"",""));

                        addressArrayList.add(address1);



                    } catch (Exception e) {
                        System.out.println("Hello Ta LALU KE");
                    }
                    listArray_addressInvoice.clear();
                    try {
                        JSONObject jObj3 = new JSONObject();
                        jObj3 = data.getJSONObject("address_invoice");

                        ArrayList<String> addressArrayList = new ArrayList<String>();

                        String addressFirstName = jObj3.getString("firstname");
                        String addressLastName = jObj3.getString("lastname");
                        String address1 = jObj3.getString("address1");
                        address1=address1.replace("\\","");
                        String address2 = jObj3.getString("address2");
                        address2=address2.replace("\\","");
                        String addressPostCode = jObj3.getString("postcode");
                        String addressCity = jObj3.getString("city");
                        String countryID = jObj3.getString("id_country");
                        String addressCountry = jObj3.getString("country");
                        String stateID = jObj3.getString("id_state");
                        String addressState = jObj3.getString("state");
                        String addressPhone = jObj3.getString("phone");

                        System.out.println("country issss " + jObj3.getString("country"));

                        listArray_addressInvoice.add(new addressItem(addressIDInvoice, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone,"",""));

                        addressArrayList.add(address1);



                    } catch (Exception e) {
                        System.out.println("Hello Ta LALU KE2");

                    }

                    try {
                        JSONArray jsonArr3 = new JSONArray();
                        jsonArr3 = data.getJSONArray("payment_list");

                        System.out.println("get payment type = " + jsonArr3);


                        for(int i = 0;i< jsonArr3.length();i++)
                        {
                            String paymentType;

                            JSONObject paymentObj = jsonArr3.getJSONObject(i);
                            paymentType = paymentObj.getString("name");

                            if(paymentType.equals("E-Wallet"))
                            {

                                eWalletExist = true;


                                JSONArray jsonEwallet = new JSONArray();
                                jsonEwallet = jsonArr3.getJSONObject(i).getJSONArray("options");

                                EWalletgarray = new String[jsonEwallet.length()];
                                EWalletarrayValue = new String[jsonEwallet.length()];

                                for (int x = 0; x < jsonEwallet.length(); x++) {
                                    JSONObject jObj = jsonEwallet.getJSONObject(x);
                                    EWalletgarray[x]=jObj.getString("name");
                                    EWalletarrayValue[x]=jObj.getString("value");

                                }
                            }

                            if(paymentType.equals("Credit Card"))
                            {

                                creditCardExist = true;

                            }

                            if(paymentType.equals("Online Banking"))
                            {

                                onlineBankingExist = true;

                                JSONArray jsonArrOnlineBanking = new JSONArray();
                                jsonArrOnlineBanking = jsonArr3.getJSONObject(i).getJSONArray("options");


                                onlinebankingarray = new String[jsonArrOnlineBanking.length()];
                                onlinebankingarrayValue = new String[jsonArrOnlineBanking.length()];

                                for (int x = 0; x < jsonArrOnlineBanking.length(); x++) {
                                    JSONObject jObj = jsonArrOnlineBanking.getJSONObject(x);
                                    onlinebankingarray[x]=jObj.getString("name");
                                    onlinebankingarrayValue[x]=jObj.getString("value");

                                }

                            }
                        }
                    }catch (Exception e){
                        System.out.println("Hello Ta LALUList KE3");


                    }

                    listArray_carrier.clear();

                    try {
                        JSONArray jsonArr2 = new JSONArray();
                        jsonArr2 = data.getJSONArray("carrier_list");

                        if(data.has("carrier_name")){
                            carrier_selected = data.getString("carrier_name");
                        }

                        else {
                            JSONObject jObj = jsonArr2.getJSONObject(0);
                            String carrierID = jObj.getString("name");
                            carrier_selected = carrierID;
                        }
                        System.out.println("carrier list here 2 = " + jsonArr2);



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

                    }catch (Exception e){}

                    refreshRecycleView();
                    mBookends.notifyDataSetChanged();

                }
                refreshRecycleView();
                mBookends.notifyDataSetChanged();
            }


        }catch (Exception e) {
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
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        //}
    }

    private void getCartDetailList()
    {
        String cartResultJObjString=getArguments().getString("cartResultJObj");
        try {
            cartResultJObj = new JSONObject(cartResultJObjString);

            listArray_shoppingBag.clear();
            totalAllProductPrice = 0;
            String action = cartResultJObj.getString("action");

            if(action.equals("getUserCart"))
            {
                if(cartResultJObj.getString("result").equals("1"))
                {

                    JSONArray jsonArr = new JSONArray();
                    jsonArr = cartResultJObj.getJSONArray("product_list");
                    CartID = cartResultJObj.getString("id_cart");

                    for(int i = 0; i < jsonArr.length(); i++)
                    {
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

                            if (diff > 0) {
                                if (!discountedPrice.equals("0")) {
                                    price = discountedPrice;
                                }

                                price = String.format("%.2f", Float.parseFloat(price));


                                String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                                totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);


                                listArray_shoppingBag.add(new shoppingBagItem(CartID, productAttributeID, productID, imageURL, productName, productSize, productRef, price, totalPrice, quantity, timeRem,quantity_available));
                            }
                        }catch (Exception e){}

//                        if(!discountedPrice.equals("0"))
//                        {
//                            price = discountedPrice;
//                        }
//
//                        price = String.format("%.2f", Float.parseFloat(price));
//
//
//                        String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));
//
//                        totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);
//
//
//                        listArray_shoppingBag.add(new shoppingBagItem(CartID,productAttributeID,productID,imageURL,productName,productSize,productRef,price,totalPrice,quantity,timeEnd));

                    }

                    ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(noInBag));
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("cartItem", noInBag);
                    editor.commit();

                    listArray_voucher.clear();
                    totalVoucherPrice = 0;

                    JSONArray voucherArray = cartResultJObj.getJSONArray("voucher_list");
                    for(int j = 0; j < voucherArray.length(); j++)
                    {
                        JSONObject jObj2 = voucherArray.getJSONObject(j);
                        String voucherID = jObj2.getString("id_voucher");
                        String voucher_name= jObj2.getString("voucher_name");
                        String voucherCode = jObj2.getString("code");
                        String voucherPercentage = jObj2.getString("reduction_percent");
                        String voucherAmount = jObj2.getString("value_tax_exc");

//                        if(!voucherPercentage.equals("0.00"))
//                        {
//                            float Percentage = Float.parseFloat(voucherPercentage);
//                            float amount = totalAllProductPrice * (Percentage/100);
//                            voucherAmount=String.format("%.2f", amount);
//                        }

                        totalVoucherPrice += Float.parseFloat(voucherAmount);


                        listArray_voucher.add(new voucherItem(voucherID,voucher_name,voucherCode,voucherAmount));
                    }

                    listArray_store_credit.clear();

                    try {
                        JSONArray voucherArray1 = cartResultJObj.getJSONArray("storeCredit_list");
                        for (int j = 0; j < voucherArray1.length(); j++) {
                            JSONObject jObj2 = voucherArray1.getJSONObject(j);
                            String voucherID = jObj2.getString("id_voucher");
                            String voucher_name= jObj2.getString("voucher_name");
                            String voucherCode = jObj2.getString("code");
                            String voucherPercentage = jObj2.getString("reduction_percent");
                            String voucherAmount = jObj2.getString("value_real");

//                            if (!voucherPercentage.equals("0.00")) {
//                                float Percentage = Float.parseFloat(voucherPercentage);
//                                float amount = totalAllProductPrice * (Percentage / 100);
//                                voucherAmount=String.format("%.2f", amount);
//                            }

                            totalVoucherPrice += Float.parseFloat(voucherAmount);

                            listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                        }
                    }catch (Exception e)
                    {

                    }
                }
            }
            else if(action.equals("getOrderDetail"))
            {
                if(cartResultJObj.getString("result").equals("1"))
                {
                    JSONObject orderHistroyJObj = cartResultJObj.getJSONObject("order_history");

                    try {
                        JSONObject shippingObj = orderHistroyJObj.getJSONObject("shipping_details");
                        JSONObject billingObj = orderHistroyJObj.getJSONObject("billing_details");

                        shipping_addressFirstName = shippingObj.getString("firstname");
                        shipping_addressLastName = shippingObj.getString("lastname");
                        shipping_address1 = shippingObj.getString("address1");
                        shipping_address1=shipping_address1.replace("\\","");
                        shipping_address2 = shippingObj.getString("address2");
                        shipping_address2=shipping_address2.replace("\\","");
                        shipping_addressPostCode = shippingObj.getString("postcode");
                        shipping_addressCity = shippingObj.getString("city");
                        shipping_addressCountry = shippingObj.getString("country");
                        shipping_addressState = shippingObj.getString("state");
                        shipping_addressPhone = shippingObj.getString("phone");

                        billing_addressFirstName = billingObj.getString("firstname");
                        billing_addressLastName = billingObj.getString("lastname");
                        billing_address1 = billingObj.getString("address1");
                        billing_address1=billing_address1.replace("\\","");
                        billing_address2 = billingObj.getString("address2");
                        billing_address2=billing_address2.replace("\\","");
                        billing_addressPostCode = billingObj.getString("postcode");
                        billing_addressCity = billingObj.getString("city");
                        billing_addressCountry = billingObj.getString("country");
                        billing_addressState = billingObj.getString("state");
                        billing_addressPhone = billingObj.getString("phone");

                    }catch (Exception e){

                    }

                    try {
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = orderHistroyJObj.getJSONArray("product_details");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jObj = jsonArr.getJSONObject(i);
                            String productID = jObj.getString("product_id");
                            String productName = jObj.getString("product_name");
                            String productRef = jObj.getString("product_reference");
                            String price = jObj.getString("product_price");
                            String quantity = jObj.getString("product_quantity");
                            String image = jObj.getString("product_image");
                            String size = jObj.getString("product_attribute");
                            Integer quantity_available = jObj.getInt("quantity_available");


                            price = String.format("%.2f", Float.parseFloat(price));


                            String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                            totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);


                            listArray_shoppingBag.add(new shoppingBagItem("", "", productID, image, productName, size, productRef, price, totalPrice, quantity, "",quantity_available));


                        }
                    }catch (Exception e){}

                    listArray_voucher.clear();

                    try {

                        JSONArray voucherArray = orderHistroyJObj.getJSONArray("discount_details");

                        totalVoucherPrice = 0;

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
                    }catch (Exception e){}
                }

                refreshRecycleView();

            }

        }catch (JSONException e) {

        }
    }

    private void refreshRecycleView()
    {
        RVAdapter = new shoppingBagAdapter(getActivity(),listArray_shoppingBag,null,false);
        // Make Bookends
        mBookends= new Bookends<>(RVAdapter);

        // Inflate footer view
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

        final RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.payment_header_view, mRecyclerView, false);

        //New Header

        LinearLayout newHeader = (LinearLayout) inflater1.inflate(R.layout.new_checkout_page, mRecyclerView, false);

        codeVoucherRL = (RelativeLayout)newHeader.findViewById(R.id.codeBarRLPayment);

        TextView shipping_text = (TextView) newHeader.findViewById(R.id.shippingText);
        shipping_text.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        TextView shipping_text_choice = (TextView) newHeader.findViewById(R.id.deliveryMethodOne);
        shipping_text_choice.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        TextView payment_text = (TextView) newHeader.findViewById(R.id.selectPaymentTxt);
        payment_text.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        bank_first_message = (TextView) newHeader.findViewById(R.id.bank_message_one);
        bank_first_message.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        bank_second_message = (TextView) newHeader.findViewById(R.id.bank_message_two);
        bank_second_message.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        code_textView = (TextView)newHeader.findViewById(R.id.codeTV);
        code_textView.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        paymentTSIV = (TextView) newHeader.findViewById(R.id.paymentTSIVNew);
        paymentTSIV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        termsTV = (TextView) newHeader.findViewById(R.id.termsService);
        termsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        privacyTV = (TextView) newHeader.findViewById(R.id.privacyPolicy);
        privacyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        andTV = (TextView) newHeader.findViewById(R.id.andTV);
        andTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        belowTermsTV = (TextView) newHeader.findViewById(R.id.textAfter);
        belowTermsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        bank_first_message.setText(first_bank_message);
        bank_second_message.setText(second_bank_message);

        applyCodeBtn = (ImageButton) newHeader.findViewById(R.id.codeApplyIBPayment);
        codeInputET = (EditText) newHeader.findViewById(R.id.codeFieldPayment);

        SpannableString content = new SpannableString("Terms Of Service");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        termsTV.setText(content);

        SpannableString content2 = new SpannableString("Privacy Policy");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        privacyTV.setText(content2);

//        final SpannableStringBuilder str2 = new SpannableStringBuilder(paymentTSIV.getText().toString());
//        str2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 37, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        paymentTSIV.setText(str2);


        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TermsCondFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromPayment", "Yeah");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        privacyTV.setOnClickListener(new View.OnClickListener() {
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



        applyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedBanking == 12 || selectedBanking == 2 || isCCSelected == true) {

                    if (totalPrice.equals("0.00")) {
                        Toast.makeText(getActivity(), "Your order has been fully paid", Toast.LENGTH_LONG).show();
                    } else if (codeInputET.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "You must enter a voucher code", Toast.LENGTH_LONG).show();
                    } else {
                        applyVoucherWS(codeInputET.getText().toString(), UserID, CartID);
                    }


                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
//                refreshAll();

//                oriGiftOptionsText = giftOptionsET.getText().toString();
//                oriLeaveMessageText = leaveMsgET.getText().toString();
//
//                refreshRecyclerView();

                }
                else {
                    Toast.makeText(getActivity(), "Voucher is not valid for selected bank", Toast.LENGTH_LONG).show();
                }
            }
        });

        eWalletTxt = (TextView) newHeader.findViewById(R.id.EWalletTxtView);
        eWalletTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        onlineBankTxt = (TextView) newHeader.findViewById(R.id.OnlineBankingTxtView);
        onlineBankTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        paymentGroup = newHeader.findViewById(R.id.PaymentGroup);
        paymentList = newHeader.findViewById(R.id.CreditCardBtn);
        paymentList2 = newHeader.findViewById(R.id.EWalletBtn);
        paymentList3 = newHeader.findViewById(R.id.onlineBankingBtn);
        paymentList4 = newHeader.findViewById(R.id.enetsorpaypal);

        shipping_text_choice.setText(carrier_selected);

        System.out.println("step 4 data = " + carrier_selected);
//        System.out.println("step 4 data 2 = " + deliveryAddItem.getaddressID());

        paymentList.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        paymentList2.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        paymentList3.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        paymentList4.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));


        paymentGroup.setOnCheckedChangeListener((radioGroup, i) -> {

            checkButton(i);

        });

        eWalletTxt.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("E-Wallet");
            builder.setSingleChoiceItems(EWalletgarray, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    selectedEWallet = i;
                    eWalletID = EWalletarrayValue[i];
                    eWalletType = EWalletgarray[i];

                }
            });

            builder.setPositiveButton("Done",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            eWalletTxt.setText(EWalletgarray[selectedEWallet]);

                        }
                    });


            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.show();

        });

        onlineBankTxt.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Online Banking");
            builder.setSingleChoiceItems(onlinebankingarray, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    selectedBanking = i;
                    PaymentIDForIPay88 = onlinebankingarrayValue[i];

                    System.out.println("bank select 1 = " + selectedBanking);
                    System.out.println("bank select 2 = " + PaymentIDForIPay88);


                }
            });

            builder.setPositiveButton("Done",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            onlineBankTxt.setText(onlinebankingarray[selectedBanking]);

                        }
                    });


            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.show();

        });


        //New Header



        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.payment_footer_view, mRecyclerView, false);
        RelativeLayout footer1 = (RelativeLayout) inflater1.inflate(R.layout.payment_footer_view1, mRecyclerView, false);
        RelativeLayout footer2 = (RelativeLayout) inflater1.inflate(R.layout.payment_footer_first_view, mRecyclerView, false);
        RelativeLayout footer3 = (RelativeLayout) inflater1.inflate(R.layout.payment_footer_second_view, mRecyclerView, false);

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

        TextView paymentLblTV  = (TextView) header.findViewById(R.id.paymentLblTV);
        TextView paymentTSLblTV  = (TextView) header.findViewById(R.id.paymentTSLblTV);
        TextView nextTV  = (TextView) header.findViewById(R.id.nextTV);
        TextView orderSummaryTV  = (TextView) header.findViewById(R.id.orderSummaryTV);

        paymentLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        paymentTSLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        nextTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        orderSummaryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        TextView shippingAddressLabel  = (TextView) footer1.findViewById(R.id.shippingAddressLabel);
        TextView shippingAddressContactNoLblTV  = (TextView) footer1.findViewById(R.id.shippingAddressContactNoLblTV);
        TextView billingAddressLabel  = (TextView) footer1.findViewById(R.id.billingAddressLabel);
        TextView billingAddressContactNoLblTV  = (TextView) footer1.findViewById(R.id.billingAddressContactNoLblTV);
        TextView shippingMethodLabel  = (TextView) footer1.findViewById(R.id.shippingMethodLabel);


        shippingAddressLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressContactNoLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressContactNoLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        shippingAddressTV = (TextView) footer1.findViewById(R.id.shippingAddressTV);
        billingAddressInfoTV = (TextView) footer1.findViewById(R.id.billingAddressInfoTV);
        shippingAddressContactNoTV = (TextView) footer1.findViewById(R.id.shippingAddressContactNoTV);
        billingAddressContactNoTV = (TextView) footer1.findViewById(R.id.billingAddressContactNoTV);
//        paymentTSIV = (TextView) header.findViewById(R.id.paymentTSIV);
//        paymentTSIV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));



        shippingAddressTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressInfoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        paymentTSIV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//
//        final SpannableStringBuilder str = new SpannableStringBuilder(paymentTSIV.getText().toString());
//        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 17, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        paymentTSIV.setText(str);
//
//        paymentTSIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new TermsCondFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("fromPayment", "Yeah");
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = getActivity().getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });

        subTotalTV = (TextView)footer2.findViewById(R.id.subTotalTV);
        addGSTTV = (TextView)footer3.findViewById(R.id.addGSTTV);
        subTotalLblTV = (TextView)footer2.findViewById(R.id.subTotalLblTV);
        subTotalLblTVRM = (TextView)footer2.findViewById(R.id.subTotalLblTVRM);
        addGSTLblTV = (TextView)footer3.findViewById(R.id.addGSTLblTV);
        addGSTLblTVRM = (TextView)footer3.findViewById(R.id.addGSTLblTVRM);

        totalInclGSTTV = (TextView) footer.findViewById(R.id.totalInclGSTTV);
        totalPayableRMTV = (TextView) footer1.findViewById(R.id.totalPayableRMTV);
        totalPayableRM1TV = (TextView) footer1.findViewById(R.id.totalPayableRM1TV);
        totalPayableTV = (TextView) footer1.findViewById(R.id.totalPayableTV);
        shippingMethodTVlbl = (TextView) footer.findViewById(R.id.shippingMethodTVlbl);
        totalInclGSTTVlblRM = (TextView) footer.findViewById(R.id.totalInclGSTTVlblRM);
        totalInclGSTTVlbl = (TextView) footer.findViewById(R.id.totalInclGSTTVlbl);
        shippingMethodTV = (TextView) footer.findViewById(R.id.shippingMethodTV);
        addressfooterRM1 = (TextView) footer.findViewById(R.id.addressfooterRM1);
        shippingMethodTVbill1 = (TextView) footer.findViewById(R.id.shippingMethodTVbill1);


        addressfooterRM1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodTVbill1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        subTotalTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalInclGSTTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalPayableRMTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalPayableRM1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalPayableTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        shippingMethodTVlbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalInclGSTTVlblRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        totalInclGSTTVlbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        subTotalLblTVRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        subTotalLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTLblTVRM.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addGSTLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        shippingMethodTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        RelativeLayout totalInclGSTRL = (RelativeLayout) footer.findViewById(R.id.totalInclGSTRL);
        if(!fromOrderHistory.equals("1"))
        {
            subTotalLblTVRM.setText(SelectedCountryCurrency);
            totalPayableRM1TV.setText(SelectedCountryCurrency);

            subTotalTV.setText(String.valueOf(totalPriceWt));
            if(taxAmount.equals("0.00")){
                totalInclGSTRL.setVisibility(View.GONE);
            }else{
                totalInclGSTRL.setVisibility(View.VISIBLE);
                addGSTTV.setText(taxAmount);
            }


            if (totalPrice.equalsIgnoreCase("0.00")){
                totalPayableTV.setText("0.00");
                FinalPrice = "0.00";
            }else if(!totalPrice.contains(".")) {
                totalPayableTV.setText(totalPrice+".00");
                FinalPrice = totalPrice+".00";
                float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
                System.out.print("GST" + rounded);
            }else{

                totalPayableTV.setText(totalPrice);
                FinalPrice = totalPrice;
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

        }else{
            subTotalTV.setText(String.valueOf(totalAllProductPrice));
            addGSTTV.setText(String.format("%.2f", totalAllProductPrice * 0.06));

            double totalInclGST= totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice;
            if (totalInclGST<=0){
                totalInclGSTTV.setText("0.00");
                totalPayableTV.setText("0.00");
                FinalPrice = "0.00";
            }else {
                totalInclGSTTV.setText(String.format("%.2f", totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice));
                totalPayableTV.setText(String.format("%.2f",totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice));
                FinalPrice = String.format("%.2f",totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice);
            }
        }



        if(listArray_carrier.get(SelectedCarrierPosition).getcarrierPrice().equals("0.00")){
            shippingMethodTV.setVisibility(View.VISIBLE);
            addressfooterRM1.setVisibility(View.GONE);
            shippingMethodTVbill1.setVisibility(View.GONE);
            shippingMethodTV.setText("Free Shipping");

        }else if(listArray_carrier.get(SelectedCarrierPosition).getcarrierPrice().equals("")||listArray_carrier.get(0).getcarrierPrice()==null||listArray_carrier.get(0).getcarrierPrice().isEmpty()||listArray_carrier.get(0).getcarrierPrice().equals("null")){
        shippingMethodTV.setVisibility(View.VISIBLE);
        addressfooterRM1.setVisibility(View.GONE);
        shippingMethodTVbill1.setVisibility(View.GONE);
        shippingMethodTV.setText("Free Shipping");

    }else{
            shippingMethodTV.setVisibility(View.GONE);
            addressfooterRM1.setVisibility(View.VISIBLE);
            addressfooterRM1.setText(SelectedCountryCurrency);
            shippingMethodTVbill1.setVisibility(View.VISIBLE);
            shippingMethodTVbill1.setText(listArray_carrier.get(SelectedCarrierPosition).getcarrierPrice());
        }

        shippingMethodInfoTV = (TextView) footer1.findViewById(R.id.shippingMethodInfoTV);
        shippingMethodInfoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        if(fromOrderHistory.equals("1"))
        {
            shippingMethodInfoTV.setText(carrier);
        }
        else
        {

            shippingMethodInfoTV.setText(carrier_selected);
        }

        onlineBankingDropDownIV = (ImageView)header.findViewById(R.id.onlineBankingDropDownIV);
        onlineBankingTV = (TextView)header.findViewById(R.id.onlineBankingTV);

        EWalletDropDownIV = (ImageView)header.findViewById(R.id.EWalletIV);
        eWalletTV = (TextView)header.findViewById(R.id.EWalletTV);



//        EWalletDropDownIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                isEWallet = true;
//
//                PAYMENT_METHOD = TNG_EWALLET;
//
//                onlineBankingNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//                onlineBankingNP.setDisplayedValues(null);
//                onlineBankingNP.setMinValue(1);
//                onlineBankingNP.setMaxValue(EWalletgarray.length);
//                onlineBankingNP.setDisplayedValues(EWalletgarray);
//                setDividerColor(onlineBankingNP);
//
//                onlineBankingRL.setVisibility(View.VISIBLE);
//            }
//        });
//
//
//        onlineBankingDropDownIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                isEWallet = false;
//
//                PAYMENT_METHOD = ONLINE_BANKING_PAYMENT;
//
//                onlineBankingNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//                onlineBankingNP.setDisplayedValues(null);
//                onlineBankingNP.setDisplayedValues(onlinebankingarray);
//                onlineBankingNP.setMinValue(0);
//                onlineBankingNP.setMaxValue(onlinebankingarray.length - 1);
//                setDividerColor(onlineBankingNP);
//
//                onlineBankingRL.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        onlineBankingSelectIB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onlineBankingRL.setVisibility(View.GONE);
//
//                if(onlineBankingNP.getValue() > 0)
//                {
//
//                    if(isEWallet)
//                    {
//
//                        eWalletTV.setBackgroundResource(R.drawable.placeholder_payment);
//
//                        eWalletTV.setText(EWalletgarray[onlineBankingNP.getValue()-1]);
//                        eWalletType = EWalletgarray[onlineBankingNP.getValue()-1];
//                        PaymentIDForIPay88 = EWalletarrayValue[onlineBankingNP.getValue()-1];
//
//                        eWalletID = PaymentIDForIPay88;
//
//
//                        onlineBankingTV.setBackgroundResource(R.drawable.placeholder_payment_inactive);
//                        onlineBankingTV.setText("");
//
//                        System.out.println("Payment ID sini = " + PaymentIDForIPay88);
//
//                    }
//
//                    else
//                    {
//
//                        onlineBankingTV.setBackgroundResource(R.drawable.placeholder_payment);
//                        eWalletTV.setBackgroundResource(R.drawable.placeholder_payment_inactive);
//                        eWalletTV.setText("");
//
//                        onlineBankingTV.setText(onlinebankingarray[onlineBankingNP.getValue()]);
//                        PaymentIDForIPay88 = onlinebankingarrayValue[onlineBankingNP.getValue()];
//
//                        System.out.println("Payment ID sini = " + PaymentIDForIPay88);
//
//
//                    }
//
//                }
//                else
//                {
//                    onlineBankingTV.setBackgroundResource(R.drawable.placeholder_payment_inactive);
//                    onlineBankingTV.setText("");
//                    eWalletTV.setBackgroundResource(R.drawable.placeholder_payment_inactive);
//                    eWalletTV.setText("");
//                    PaymentIDForIPay88 = "";
//                }
//            }
//        });
//
////        paymentRG = (RadioGroup) header.findViewById(R.id.paymentRG);
//        RadioButton creditCardRB = (RadioButton) header.findViewById(R.id.creditCardRB);
//        RadioButton onlineBankingRB = (RadioButton) header.findViewById(R.id.onlineBankingRB);
//        RadioButton tngRB = (RadioButton) header.findViewById(R.id.tngEWallet);
//        RadioButton paypalRB = (RadioButton) header.findViewById(R.id.paypalRB);
//
//
//        creditCardRB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        onlineBankingRB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        tngRB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//
//        paypalRB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        onlineBankingTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        paymentTSCheckIV = (ImageView) newHeader.findViewById(R.id.paymentTSCheckIV);
        paymentTSCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TermCondChecked)
                {
                    paymentTSCheckIV.setImageResource(R.drawable.btn_check_active);
                    TermCondChecked = true;
                }
                else
                {
                    paymentTSCheckIV.setImageResource(R.drawable.btn_check);
                    TermCondChecked = false;
                }
            }
        });

        paymentNextBtnIV = (ImageButton) header.findViewById(R.id.paymentNextBtnIV);
        paymentBottomNextBtnIV = (ImageButton) footer1.findViewById(R.id.paymentBottomNextBtnIV);

        RadioButton enets=(RadioButton) header.findViewById(R.id.enetsRB);

        enets.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        LinearLayout onlineBankingLL=(LinearLayout) header.findViewById(R.id.onlineBankingLL);
        LinearLayout eWalletLL=(LinearLayout) header.findViewById(R.id.EWalletLL);


        String ShopID;
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        ShopID  = pref.getString("SelectedShopID", "1");
        //
        if(ShopID.equals("1")){
            paymentList4.setVisibility(View.GONE);

//            if(creditCardExist)
//            {
//                creditCardRB.setVisibility(View.VISIBLE);
//            }
//
//            if(eWalletExist)
//            {
//
//                tngRB.setVisibility(View.VISIBLE);
//                eWalletLL.setVisibility(View.VISIBLE);
//            }
//
//            if(onlineBankingExist)
//            {
//                onlineBankingRB.setVisibility(View.VISIBLE);
//                onlineBankingLL.setVisibility(View.VISIBLE);
//            }
        }else if(ShopID.equals("2")){

            paymentList2.setVisibility(View.GONE);
            paymentList3.setVisibility(View.GONE);

            paymentList4.setText("ENets");

//            enets.setVisibility(View.VISIBLE);
//            onlineBankingRB.setVisibility(View.GONE);
//            onlineBankingLL.setVisibility(View.GONE);
//            eWalletLL.setVisibility(View.GONE);
//            tngRB.setVisibility(View.GONE);
//            paypalRB.setVisibility(View.GONE);
//            creditCardRB.setVisibility(View.VISIBLE);

        }
        else if(ShopID.equals("3")){

            paymentList2.setVisibility(View.GONE);
            paymentList3.setVisibility(View.GONE);

            paymentList4.setText("Paypal");

//            enets.setVisibility(View.GONE);
//            onlineBankingRB.setVisibility(View.GONE);
//            onlineBankingLL.setVisibility(View.GONE);
//            eWalletLL.setVisibility(View.GONE);
//            tngRB.setVisibility(View.GONE);
//            creditCardRB.setVisibility(View.VISIBLE);
//            paypalRB.setVisibility(View.VISIBLE);



        }
        paymentNextBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (TermCondChecked) {
//                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_PHONE_STATE}, 0);
//
//                        }else{
                            int radioBtnID = paymentGroup.getCheckedRadioButtonId();
                            switch (radioBtnID) {
                                case R.id.CreditCardBtn:

                                    PAYMENT_METHOD = CREDIT_CARD_PAYMENT;
                                    if (SelectedShopID.equals("1")) {
                                        GoToNextStepWS("myr_cc");
                                    } else if (SelectedShopID.equals("2")) {
                                        GoToNextStepWS("sgd_cc");
                                    } else {
                                        GoToNextStepWS("usd_cc");
                                    }

                                    break;
                                case R.id.tngEWallet:

                                    PAYMENT_METHOD = TNG_EWALLET;

                                    if(eWalletID.length() > 0)
                                    {

                                        if (eWalletType.equals("Touch 'n Go")) {
                                            eWalletType = "tng";
                                        } else if (eWalletType.equals("Boost")) {

                                            eWalletType = "boost";
                                        } else {


                                        }

                                        System.out.println("ewalletType = " + eWalletType);
                                        System.out.println("ewalletID = " + eWalletID);


                                        GoToNextStepWS(eWalletType);
                                    }

                                    else {

                                        Toast.makeText(getActivity(), "Please select payment type!", Toast.LENGTH_LONG).show();


                                    }

                                    break;
                                case R.id.onlineBankingRB:

                                    PAYMENT_METHOD = ONLINE_BANKING_PAYMENT;

                                    if (PaymentIDForIPay88.length() > 0) {
                                        GoToNextStepWS("ipay88");
                                    } else {
                                        Toast.makeText(getActivity(), "Please select online banking!", Toast.LENGTH_LONG).show();
                                    }

                                    break;
                                case R.id.paypalRB:

                                    PAYMENT_METHOD = PAYPAL_PAYMENT;

                                    GoToNextStepWS("paypal");
                                    break;
                                case R.id.enetsRB:

                                    PAYMENT_METHOD = ENETS_PAYMENT;

                                    GoToNextStepWS("enets");
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "Please select payment type", Toast.LENGTH_SHORT).show();
                                    break;
//                        }

                        }
                    } else {
                        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                                .setTitle("Message")
                                .setMessage("You must agree to the Terms of Service and Privacy Policy before continuing")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }

            }
        });

        paymentBottomNextBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TermCondChecked) {
//                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_PHONE_STATE}, 0);
//
//                        }else{
                    int radioBtnID = paymentGroup.getCheckedRadioButtonId();
                    switch (radioBtnID) {
                        case R.id.CreditCardBtn:

                            PAYMENT_METHOD = CREDIT_CARD_PAYMENT;
                            if (SelectedShopID.equals("1")) {
                                GoToNextStepWS("myr_cc");
                            } else if (SelectedShopID.equals("2")) {
                                GoToNextStepWS("sgd_cc");
                            } else {
                                GoToNextStepWS("usd_cc");
                            }

                            break;
                        case R.id.EWalletBtn:

                            PAYMENT_METHOD = TNG_EWALLET;

                            if(eWalletID.length() > 0)
                            {

                                if (eWalletType.equals("Touch 'n Go")) {
                                    eWalletType = "tng";

                                } else if (eWalletType.equals("Boost")) {
                                    eWalletType = "boost";
                                }

                                GoToNextStepWS(eWalletType);
                            }

                            else {

                                Toast.makeText(getActivity(), "Please select payment type!", Toast.LENGTH_LONG).show();


                            }

                            break;
                        case R.id.onlineBankingBtn:

                            PAYMENT_METHOD = ONLINE_BANKING_PAYMENT;

                            if (PaymentIDForIPay88.length() > 0) {
                                GoToNextStepWS("ipay88");
                            } else {
                                Toast.makeText(getActivity(), "Please select online banking!", Toast.LENGTH_LONG).show();
                            }

                            break;
                        case R.id.enetsorpaypal:

                            if (SelectedShopID.equals("2")) {
                                PAYMENT_METHOD = ENETS_PAYMENT;
                                GoToNextStepWS("enets");
                            } else {
                                PAYMENT_METHOD = PAYPAL_PAYMENT;
                                GoToNextStepWS("paypal");
                            }

                            break;
                        case R.id.enetsRB:

                            break;
                        default:
                            Toast.makeText(getActivity(), "Please select payment type", Toast.LENGTH_SHORT).show();
                            break;
//                        }

                    }
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("You must agree to the Terms of Service and Privacy Policy before continuing")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();
                }

            }
        });


        mBookends.addHeader(newHeader);
        mBookends.addFooter(footer2);

        for(int i = 0; i <listArray_voucher.size(); i++)
        {
            RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view, mRecyclerView, false);

            final voucherItem item=listArray_voucher.get(i);

            TextView voucherCodeNameTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV);
            TextView voucherCodeNameTV1 = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV1);
            TextView voucherCodePriceLblTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceLblTV);
            TextView voucherCodePriceTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceTV);
            ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode.findViewById(R.id.voucherCodeRemove);


            voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeNameTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
            voucherCodeRemove.setVisibility(View.VISIBLE);

            voucherCodePriceLblTV.setText(SelectedCountryCurrency);
            voucherCodeNameTV.setText("(-) " + listArray_voucher.get(i).getvoucherCode());
            voucherCodeNameTV1.setText(listArray_voucher.get(i).getvoucherName());
            voucherCodePriceTV.setText(listArray_voucher.get(i).getvoucherReduceAmount());


            voucherCodeRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Remove Voucher Code?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteVoucherFromCart(item.getvoucherID(), CartID);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .show();


                }
            });


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
        mRecyclerView.setAdapter(mBookends);

        if(fromOrderHistory.equals("1"))
        {
            shippingAddressTV.setText(shipping_addressFirstName + " " + shipping_addressLastName + "\n" + shipping_address1 + " " + shipping_address2 + "\n" + shipping_addressPostCode + " " + shipping_addressCity + "\n" + shipping_addressState + "\n" + shipping_addressCountry);
            shippingAddressContactNoTV.setText(shipping_addressPhone);

            billingAddressInfoTV.setText(billing_addressFirstName + " " + billing_addressLastName + "\n" + billing_address1 + " " + billing_address2 + "\n" + billing_addressPostCode + " " + billing_addressCity + "\n" + billing_addressState + "\n" + billing_addressCountry);
            billingAddressContactNoTV.setText(billing_addressPhone);
        }
        else
        {
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



    }

    private void GoToNextStepWS(String paymentType)
    {
        String action;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String leaveMessage = pref.getString("LeaveMessage", "");
        String apikey =pref.getString("apikey","");
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PaymentType", paymentType);
        editor.apply();


        if(paymentType.equals("sgd_cc")||paymentType.equals("enets")) {
            action = "Carts/OrderStep4?apikey="+apikey+"&id_cart=" + CartID + "&payment=" + paymentType + "&message=" + leaveMessage + "&skip_timer=" + skipTimer + "&extra_cart=" + extra_cart;
        }else{
            action = "Carts/OrderStep4?apikey="+apikey+"&id_cart=" + CartID + "&payment=" + paymentType + "&message=" + leaveMessage + "&skip_timer=" + skipTimer;
        }
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);
    }


    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{
                int totalquantity=0;
                String action = result.getString("action");

                if(action.equals("Carts_OrderStep4"))
                {
                    if(result.getBoolean("status"))
                    {

                        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

                        if(result.getJSONObject("data").getString("next_page").equals("callPaymentGateway"))

                        {
                            JSONObject data = result.getJSONObject("data");
                            CartID=data.getString("id_cart");
                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("giftMessage", "");
                            editor.putString("LeaveMessage", "");
                            editor.putString("CartID",CartID);
                            editor.apply();

                            String orderID = data.getString("id_order");
                            String totalPrice= data.getString("totalPrice");

                            System.out.println("SINI LALALLA = "+ totalPrice );

                            if(PAYMENT_METHOD == CREDIT_CARD_PAYMENT)
                            {

                                if(!SelectedShopID.equals("2")) {

                                    System.out.println("here for myr/usd payment");

                                    Intent ipay88Intent = new Intent(getActivity(), IPay88PaymentActivity.class);
                                    ipay88Intent.putExtra("CREDITCARD_PAYMENT", "1");
                                    ipay88Intent.putExtra("ORDER_ID", orderID);
                                    ipay88Intent.putExtra("CART_ID", CartID);
                                    ipay88Intent.putExtra("ITEM_PRICE", totalPrice);
                                    startActivityForResult(ipay88Intent, 1);
                                    getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
                                }else{

                                    System.out.println("here for sgd payment");


                                    Intent intent = new Intent(getActivity(), ENetsPaymentActivity.class);
                                    intent.putExtra("CREDITCARD_PAYMENT", "1");
                                    intent.putExtra("ITEM_DETAIL", "POPLOOK purchase");
                                    intent.putExtra("ITEM_PRICE", totalPrice);
                                    intent.putExtra("ORDER_ID", orderID);
                                    intent.putExtra("CART_ID", CartID);

                                    System.out.println("ENETS input 1 = "+totalPrice);
                                    System.out.println("ENETS input 2 = "+ orderID);
                                    System.out.println("ENETS input 3 = "+CartID);


                                    if(data.has("payment_voucher"))
                                    {

                                        String paymentVoucher = data.getString("payment_voucher");
//
                                    System.out.println("voucher code 1 = " + paymentVoucher);

                                        intent.putExtra("PAYMENT_VOUCHER", paymentVoucher);

                                    }
                                    else {
                                        intent.putExtra("PAYMENT_VOUCHER", "0");

                                    }
                                    startActivityForResult(intent, 1);
                                    getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
                                }
                            }
                            else if(PAYMENT_METHOD == TNG_EWALLET)
                            {

                                Intent ipay88Intent = new Intent(getActivity(), IPay88PaymentActivity.class);
                                ipay88Intent.putExtra("CREDITCARD_PAYMENT", "4");
                                ipay88Intent.putExtra("eWallet", eWalletID);
                                ipay88Intent.putExtra("ORDER_ID", orderID);
                                ipay88Intent.putExtra("CART_ID", CartID);
                                ipay88Intent.putExtra("ITEM_PRICE", totalPrice);
                                startActivityForResult(ipay88Intent, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);

                            }
                            else if(PAYMENT_METHOD == ONLINE_BANKING_PAYMENT)
                            {
                                Intent ipay88Intent2 = new Intent(getActivity(), IPay88PaymentActivity.class);
                                ipay88Intent2.putExtra("CREDITCARD_PAYMENT", "0");
                                ipay88Intent2.putExtra("ITEM_PRICE", totalPrice);
                                ipay88Intent2.putExtra("PAYMENT_ID", PaymentIDForIPay88);
                                ipay88Intent2.putExtra("ORDER_ID", orderID);
                                ipay88Intent2.putExtra("CART_ID", CartID);
                                startActivityForResult(ipay88Intent2, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);

                            }
                            else if(PAYMENT_METHOD == PAYPAL_PAYMENT)
                            {
                                Intent intent = new Intent(getActivity(), PaypalPaymentActivity.class);
                                intent.putExtra("ITEM_DETAIL", "POPLOOK purchase");
                                intent.putExtra("ITEM_PRICE", totalPrice);
                                intent.putExtra("ORDER_ID", orderID);
                                intent.putExtra("CART_ID", CartID);
                                startActivityForResult(intent, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);

                            }
                            else if(PAYMENT_METHOD == ENETS_PAYMENT)
                            {
                                Intent intent = new Intent(getActivity(), ENetsPaymentActivity.class);
                                intent.putExtra("CREDITCARD_PAYMENT", "0");
                                intent.putExtra("ITEM_DETAIL", "POPLOOK purchases");
                                intent.putExtra("ITEM_PRICE", totalPrice);
                                intent.putExtra("ORDER_ID", orderID);
                                intent.putExtra("CART_ID", CartID);
                                startActivityForResult(intent, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
                            }
                            Insider.Instance.tagEvent("checkout_visited").build();

                        }
                        else if(result.getJSONObject("data").getString("next_page").equals("cart"))
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

//                    else if(result.getJSONObject("data").getString("next_page").equals("cart"))
//
//                    {
//
//                        JSONObject data = result.getJSONObject("data");
//                        CartID=data.getString("id_cart");
//                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
//                        SharedPreferences.Editor editor = pref.edit();
//                        editor.putString("giftMessage", "");
//                        editor.putString("LeaveMessage", "");
//                        editor.putString("CartID",CartID);
//                        editor.apply();
//
//                        String orderID = data.getString("id_order");
//                        String totalPrice= data.getString("totalPrice");
//
//                        Intent ipay88Intent = new Intent(getActivity(), IPay88PaymentActivity.class);
//                        ipay88Intent.putExtra("CREDITCARD_PAYMENT", "538");
//                        ipay88Intent.putExtra("ORDER_ID", orderID);
//                        ipay88Intent.putExtra("CART_ID", CartID);
//                        ipay88Intent.putExtra("ITEM_PRICE", totalPrice);
//                        startActivityForResult(ipay88Intent, 1);
//                        getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
//
//                    }
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
                else if(action.equals("Vouchers_validate"))
                {
                    if(result.getBoolean("status"))
                    {
                        String message=result.getString("message");

                        if (!message.equals("This voucher does not exists")) {
                            Toast.makeText(getActivity(), "Voucher Accepted", Toast.LENGTH_LONG).show();
//                             Insider.Instance.tagEvent("coupon_used").addParameterWithString("coupon_code",couponCode).build();

                            refreshAppliedVoucher();

                        }
                        else  {
                            Toast.makeText(getActivity(), "Invalid Voucher Code", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String message=result.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }

                }

                else if(action.equals("Carts_OrderStep3"))
                {
                    refreshVoucher = true;
                    refreshStep3Obj = result;
                    getCartDetailList1();
                }

                else if(action.equals("Carts_removeVoucher"))
                {
                    if(result.getBoolean("status"))
                    {
                        String message=result.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        refreshAppliedVoucher();
                    }
                    else
                    {

                        Toast.makeText(getActivity(), "Unable to remove voucher please try again", Toast.LENGTH_LONG).show();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                String paymentSuccess = pref.getString("payment_success","0");

                if(paymentSuccess.equals("1"))
                {

                    Fragment fragment = new OrderConfirmationFragment();

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

                else
                {

                    Fragment fragment = new MyAccountFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "MyAccountFragment");
                    fragmentTransaction.commit();
                    editor.putString("CartID", "");
                    editor.putString("cartItem", "0");
                    editor.putString("PaymentDone", "1");
                    editor.apply();

                    ((MainActivity) getActivity()).changeToolBarBagNotiText("0");

                }

            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Fragment fragment = new MyAccountFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "MyAccountFragment");
                fragmentTransaction.commit();

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("CartID", "");
                editor.putString("cartItem", "0");
                editor.putString("PaymentDone", "1");
                editor.apply();

                ((MainActivity) getActivity()).changeToolBarBagNotiText("0");
            }
            else
            {
                Toast.makeText(getActivity(),"Some problems occured. Please try again.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkButton(int id){

        switch (id){

            case R.id.CreditCardBtn:

                isCCSelected = true;

                onlineBankTxt.setVisibility(View.GONE);
                eWalletTxt.setVisibility(View.GONE);

                bank_first_message.setVisibility(View.GONE);
                bank_second_message.setVisibility(View.GONE);

                if(first_bank_message.equals("false")){
                    bank_first_message.setVisibility(View.GONE);
                }

                if(second_bank_message.equals("false")){
                    bank_second_message.setVisibility(View.GONE);
                }

                if(bank_msg_enable.equals("0")){
                    bank_second_message.setVisibility(View.GONE);
                    bank_first_message.setVisibility(View.GONE);
                    codeVoucherRL.setVisibility(View.GONE);
                }
                else {

                    if(listArray_voucher.size() == 0){
                        codeVoucherRL.setVisibility(View.GONE);
                    }

                    else {
                        codeVoucherRL.setVisibility(View.GONE);
                    }
                }

                break;
            case R.id.EWalletBtn:

                isCCSelected = false;

                onlineBankTxt.setVisibility(View.GONE);
                eWalletTxt.setVisibility(View.VISIBLE);

                bank_first_message.setVisibility(View.GONE);
                bank_second_message.setVisibility(View.GONE);
                codeVoucherRL.setVisibility(View.GONE);


                break;
            case R.id.onlineBankingBtn:

                isCCSelected = false;

                onlineBankTxt.setVisibility(View.VISIBLE);
                eWalletTxt.setVisibility(View.GONE);

                bank_first_message.setVisibility(View.GONE);
                bank_second_message.setVisibility(View.GONE);

                if(first_bank_message.equals("false")){
                    bank_first_message.setVisibility(View.GONE);
                }

                if(second_bank_message.equals("false")){
                    bank_second_message.setVisibility(View.GONE);
                }

                if(bank_msg_enable.equals("0")){
                    bank_second_message.setVisibility(View.GONE);
                    bank_first_message.setVisibility(View.GONE);
                    codeVoucherRL.setVisibility(View.GONE);
                }
                else {
                    if(listArray_voucher.size() == 0){
                        codeVoucherRL.setVisibility(View.GONE);
                    }

                    else {
                        codeVoucherRL.setVisibility(View.GONE);
                    }
                }

                break;


        }


    }

    private void applyVoucherWS(String voucherCode, String userID, String cartID){

//        couponCode = voucherCode;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID+"&mobile=1";
//        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void refreshAppliedVoucher(){

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String appVersion="1.0.0";
        String apikey =pref.getString("apikey","");
        String shippingAPI="Carts/OrderStep3?apikey="+apikey+"&id_cart="+CartID+"&id_address_delivery="+delivery_id_API+"&id_carrier="+carrier_id_API+"&device_type=android&app_version="+appVersion;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(shippingAPI);
    }

    private void deleteVoucherFromCart(String voucherID,String cartID){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Carts/removeVoucher";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_cart",cartID)
                .add("id_cart_rule",voucherID)
                .build();
        WebServiceAccessDelete callws = new WebServiceAccessDelete(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);

    }
}

