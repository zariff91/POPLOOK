package com.tiseno.poplook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.addressListCheckout;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangeEditAddressList extends Fragment implements AsyncTaskCompleteListener {

    public interface selectAddress{

        void selectedDeliveryAdd(Integer deliveryAdd, Integer billingAdd);

        void selectedBillingAdd(Integer deliveryAdd, Integer billingAdd);



    }


    public  selectAddress select;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter addressAdapter;

    ArrayList<addressItem> listArray_address = new ArrayList<addressItem>();
    JSONObject cartResultJObj,cartResultJObj1,cartResultJObj2;

    String UserID, CartID, LanguageID,SelectedShopID,SelectedCountryCurrency;

    String noInBag;

    TextView addNewAddress;

    String forBilling;

    Boolean changeDeliverAdd = false;
    int selectedBillingIndex;

    Boolean changeBillingAdd = false;
    int selectedAddIndex;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.chane_checkout_address, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Select Address");
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

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.addressLISTRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        forBilling = getArguments().getString("FOR_BILLING");

        changeDeliverAdd  = getArguments().getBoolean("changeDeliveryAdd");
        selectedBillingIndex = getArguments().getInt("selectedBilling");

        changeBillingAdd  = getArguments().getBoolean("changeBillingAdd");
        selectedAddIndex = getArguments().getInt("selectedAddress");

        getCartDetailList();


        return contentView;

    }

    public void getCartDetailList() {
        String cartResultJObjString = getArguments().getString("cartResultJObj");

        try {
            cartResultJObj = new JSONObject(cartResultJObjString);
            String action = cartResultJObj.getString("action");

            if (action.equals("editShippingAddress")||action.equals("addShippingAddress")) {




            }
            else if(action.equals("Carts_OrderStep1")) {
                if (cartResultJObj.getBoolean("status")) {

                    JSONObject data= cartResultJObj.getJSONObject("data");
                    JSONArray jsonArr = new JSONArray();

                    String stateID = "", address2 = "", company = "", addressState = "";

                        try {
                            JSONArray jsonArr1 = new JSONArray();
                            jsonArr1 = data.getJSONArray("address_list");

                        ArrayList<String> addressArrayList = new ArrayList<String>();

                        listArray_address.clear();


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

                            addressAdapter = new addressListCheckout(getActivity(),listArray_address,ChangeEditAddressList.this);
                            final Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(addressAdapter);

                            LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                            LinearLayout footer = (LinearLayout) inflater1.inflate(R.layout.address_list_footer, mRecyclerView, false);

                            addNewAddress = (TextView)footer.findViewById(R.id.addAddressTV);
                            addNewAddress.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

                            SpannableString content = new SpannableString("Add new address");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            addNewAddress.setText(content);

                            addNewAddress.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Fragment fragment = new AddAdressFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("COME_FROM_WHERE", 1);
                                    bundle.putBoolean("EDIT_ADDRESS", false);
                                    String cartResultJObjString = cartResultJObj.toString();
                                    bundle.putString("cartResultJObj", cartResultJObjString);
                                    bundle.putString("for_billing", forBilling);


                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }
                            });


                            mBookends.addFooter(footer);

                            mRecyclerView.setAdapter(mBookends);


                    } catch (Exception e) {
                    }

                }
            }
        }
        catch  (Exception e){}

    }

    public void updateAddress(Integer selectedAddressIndex)
    {

        if(changeDeliverAdd)
        {
            select.selectedDeliveryAdd(selectedAddressIndex,selectedBillingIndex);
            getFragmentManager().popBackStack();

        }

        if(changeBillingAdd)
        {
            System.out.println("sinini 1 = " + selectedAddIndex);

            System.out.println("sinini 2 = " + selectedAddressIndex);


            select.selectedBillingAdd(selectedAddIndex,selectedAddressIndex);
            getFragmentManager().popBackStack();


        }


    }

    @Override

    public void onAttach(Context context)
    {

        super.onAttach(context);

        try {

            select = (selectAddress) context;
        }

        catch (ClassCastException e){

            throw new ClassCastException(context.toString() + "must implement");

        }

    }

    @Override
    public void onTaskComplete(Object result) {

    }
}
