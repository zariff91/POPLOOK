package com.tiseno.poplook;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.addressListCheckout;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewAddressBillingFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {


    CheckBox changeBillingAddressCB;
    TextView aboveText;
    TextView deliverTopText, deliverName, deliverAddress;
    TextView billingTopText, billingName, billingAddress;
    TextView contactNo,contactNoBill;
    Button deliverButton, billingButton, proceedButton;

    ArrayList<addressItem> listArray_address = new ArrayList<addressItem>();

    JSONObject cartResultJObj,cartResultJObj1;

    int SelectedDeliveryAddress = 0;
    int SelectedBillingAddress = 0;


    String addressObj;
    String UserID, CartID;

    LinearLayout addTopLL;

    addressItem deliveryAddItem;

    addressItem billingAddItem;

    String carrierID_forAPI = "";

    String forBilling;

    boolean openBill = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View contentView = inflater.inflate(R.layout.new_address_billing, container, false);

        addressObj = getArguments().getString("cartResultJObj");

        forBilling = getArguments().getString("FOR_BILLING");

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "");

        ((MainActivity) getActivity()).changeToolBarText("Address");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).showBottomBar(false);
        ((MainActivity) getActivity()).disableExpandToolbar(true);


        changeBillingAddressCB = (CheckBox) contentView.findViewById(R.id.newdeliveryAddressCB);
        changeBillingAddressCB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        aboveText = (TextView) contentView.findViewById(R.id.selectAddressTV);
        aboveText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        deliverTopText = (TextView) contentView.findViewById(R.id.deliveryAddressTV);
        deliverTopText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        deliverName = (TextView) contentView.findViewById(R.id.nameTV);
        deliverName.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        deliverAddress = (TextView) contentView.findViewById(R.id.selecteddeliveryAddressTV);
        deliverAddress.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        billingTopText = (TextView) contentView.findViewById(R.id.billingAddressTV);
        billingTopText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingName = (TextView) contentView.findViewById(R.id.billingnameTV);
        billingName.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        billingAddress = (TextView) contentView.findViewById(R.id.selectedbillingAddressTV);
        billingAddress.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        contactNo = (TextView) contentView.findViewById(R.id.contactNumber);
        contactNo.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        contactNoBill = (TextView) contentView.findViewById(R.id.contactNumberBilling);
        contactNoBill.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        deliverButton = (Button) contentView.findViewById(R.id.changeDeliveryAdd);
        deliverButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        billingButton = (Button) contentView.findViewById(R.id.changeBillingAdd);
        billingButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        proceedButton = (Button) contentView.findViewById(R.id.proceedFromAdd);
        proceedButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        addTopLL = (LinearLayout) contentView.findViewById(R.id.billingLL);


        changeBillingAddressCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (checkBox.isChecked()) {

                    openBill = false;

                    addTopLL.setVisibility(View.GONE);

                    SelectedBillingAddress = SelectedDeliveryAddress;

                } else {

                    openBill = true;
                    addTopLL.setVisibility(View.VISIBLE);

                }

            }
        });

        deliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ChangeEditAddressList();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ChangeEditAddressList");
                fragmentTransaction.addToBackStack(null);

                Bundle args = new Bundle();
                String cartResultJObjString = addressObj;
                System.out.println("cartResultObj" + cartResultJObjString);
                args.putString("cartResultJObj", cartResultJObjString);
                args.putString("FOR_BILLING","0");

                args.putBoolean("changeDeliveryAdd",true);
                args.putInt("selectedBilling",SelectedBillingAddress);


                fragment.setArguments(args);

                fragmentTransaction.commit();
            }
        });

        billingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ChangeEditAddressList();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ChangeEditAddressList");
                fragmentTransaction.addToBackStack(null);

                Bundle args = new Bundle();
                String cartResultJObjString = addressObj;
                System.out.println("cartResultObj" + cartResultJObjString);
                args.putString("cartResultJObj", cartResultJObjString);
                args.putString("FOR_BILLING","1");

                args.putBoolean("changeBillingAdd",true);
                args.putInt("selectedAddress",SelectedDeliveryAddress);

                fragment.setArguments(args);

                fragmentTransaction.commit();
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserID.length() > 0) {
                    GoToNextStepWS(SelectedDeliveryAddress, SelectedBillingAddress);
                } else {
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

        getCartDetailList();


        return contentView;
    }

    public void getCartDetailList() {
        String cartResultJObjString = getArguments().getString("cartResultJObj");

        try {
            cartResultJObj = new JSONObject(cartResultJObjString);
            String action = cartResultJObj.getString("action");

            if (action.equals("editShippingAddress") || action.equals("addShippingAddress")) {

                if (UserID.length() == 0) {
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



            } else if (action.equals("Carts_OrderStep1")) {
                if (cartResultJObj.getBoolean("status")) {

                    JSONObject data = cartResultJObj.getJSONObject("data");
                    JSONArray jsonArr = new JSONArray();

                    String stateID = "", address2 = "", company = "", addressState = "";

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
                            address1 = address1.replace("\\", "");
                            if (jObj3.has("address2")) {
                                address2 = jObj3.getString("address2");
                            }
                            address2 = address2.replace("\\", "");
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

                            listArray_address.add(new addressItem(addressID, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone, company, id_gender));


                            addressArrayList.add(address1);

                        }

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

                        contactNo.setText(deliveryAddPhone);
                        deliverName.setText(deliveryAddFirstName + " " + deliveryAddLastName);
                        deliverAddress.setText(deliveryAdd1 + " " + deliveryAdd2 + "\n" + deliveryAddPostCode + " " + deliveryAddCity + "\n" + deliveryAddState + "\n" + deliveryAddCountry);

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

                        billingName.setText(billingAddFirstName + " " + billingAddLastName);
                        billingAddress.setText(billingAdd1 + " " + billingAdd2 + "\n" + billingAddPostCode + " " + billingAddCity + "\n" + billingAddCountry + "\n" + billingAddState);
                        contactNoBill.setText(billingAddPhone);


                            if(openBill)
                            {
                                addTopLL.setVisibility(View.VISIBLE);

                            }

                            else {
                                addTopLL.setVisibility(View.GONE);
                            }




                            if(forBilling.equals("1"))
                            {
                                addTopLL.setVisibility(View.VISIBLE);

                                changeBillingAddressCB.setChecked(false);

                                openBill = true;

                            }




                    } catch (Exception e) {
                    }

                    if (UserID.length() == 0) {
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


                }
            }
        } catch (Exception e) {
        }

    }

    private void GoToNextStepWS(int SelectedDeliveryAddress, int SelectedBillingAddress) {
        addressItem deliveryItem = listArray_address.get(SelectedDeliveryAddress);
        addressItem billingItem = listArray_address.get(SelectedBillingAddress);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey = pref.getString("apikey", "");

        String deliverID = deliveryItem.getaddressID();
        String billingID = billingItem.getaddressID();



            String action = "Carts/OrderStep2?apikey=" + apikey + "&id_cart=" + CartID + "&id_address_delivery=" + deliverID + "&id_address_billing=" + billingID + "";
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

            callws.execute(action);
    }

    public void updateAddressDelivery(Integer delivery, Integer billing) {

        System.out.println("sini laaaa = " + SelectedDeliveryAddress);

        SelectedDeliveryAddress = delivery;
        SelectedBillingAddress = billing;

        if(!openBill)
        {
            SelectedBillingAddress = SelectedDeliveryAddress;
        }


    }

    public void updateAddressBilling(Integer delivery, Integer billing) {

        System.out.println("sini laaaa 2  = " + SelectedBillingAddress);

        SelectedDeliveryAddress = delivery;
        SelectedBillingAddress = billing;

    }

    @Override
    public void onResume() {
        super.onResume();

        //multishop

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
//                            shippingMethodFragment fragment = new shippingMethodFragment();
//                            FragmentManager fragmentManager = getActivity().getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//                            fragmentTransaction.addToBackStack(null);
//
////
//                            Bundle args = new Bundle();
//                            String cartResultJObjString = cartResultJObj1.toString();
//                            args.putString("cartResultJObj", cartResultJObjString);
//                            args.putString("fromOrderHistory","0");
//                            args.putInt("SelectedDeliveryAddress", SelectedDeliveryAddress);
//                            args.putInt("SelectedBillingAddress",SelectedBillingAddress);
//                            args.putInt("SelectedCarrierPosition", 0);
//
//                            fragment.setArguments(args);
//
//                            fragmentTransaction.commit();

                            JSONArray jsonArr = new JSONArray();
                            jsonArr = result.getJSONObject("data").getJSONArray("carrier_list");




                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jObj = jsonArr.getJSONObject(i);
                                String carrierID = jObj.getString("id_carrier");
                                String carrierName = jObj.getString("name");
                                String carrierPrice = jObj.getString("price");

                                carrierID_forAPI = carrierID;
                            }


                                String appVersion="1.0.0";


                            String apikey =pref.getString("apikey","");
                            String shippingAPI="Carts/OrderStep3?apikey="+apikey+"&id_cart="+CartID+"&id_address_delivery="+deliveryAddItem.getaddressID()+"&id_carrier="+carrierID_forAPI+"&device_type=android&app_version="+appVersion;

                            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

                            callws.execute(shippingAPI);

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
                }

                else if(action.equals("Carts_OrderStep3")){
                    String nextPage = result.getJSONObject("data").getString("next_page");

                    JSONObject dataa = result;

                    if(nextPage.equals("paymentSelectionPage")){

                        Fragment fragment = new PaymentFragment();
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment,"PaymentFragment");
                        fragmentTransaction.addToBackStack(null);

                        Bundle args = new Bundle();
                        String cartResultJObjString = dataa.toString();
                        args.putString("fromOrderHistory","0");
                        args.putString("cartResultJObj", cartResultJObjString);
                        args.putString("carrier_id_api",carrierID_forAPI);
                        args.putString("delivery_address_id", deliveryAddItem.getaddressID());
                        args.putInt("SelectedDeliveryAddress", SelectedDeliveryAddress);
                        args.putInt("SelectedBillingAddress",SelectedBillingAddress);
                        args.putInt("SelectedCarrierPosition", 0);
                        fragment.setArguments(args);

                        fragmentTransaction.commit();

                    }
                }

                else if(action.equals("Carts_updateOrderSummary"))
                {


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
