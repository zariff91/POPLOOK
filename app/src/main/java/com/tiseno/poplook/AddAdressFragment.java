package com.tiseno.poplook;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.countryStateItem;
import com.tiseno.poplook.functions.stateItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPost;
import com.tiseno.poplook.webservice.WebServiceAccessPut;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class AddAdressFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    String UserID,noInBag,giftMessage;
    String forBilling;
    int COME_FROM_WHERE;
    boolean EDIT_ADDRESS = false;
    final int FROM_MY_ADDRESS = 0;
    final int FROM_SHOPPING_BAG = 1;
    final int FROM_REGISTRATION = 2;
    final int FROM_SHOPPING_BAG_NOADDRESS = 3;
    JSONObject cartResultJObj1;
    ArrayList<countryStateItem> countries = new ArrayList<countryStateItem>();
    ArrayList<stateItem> stateList = new ArrayList<stateItem>();
    ArrayList<String> stateNameArrayList = new ArrayList<String>();
    String[] stateNameList;
    int countResume=0;
    Button addAddress_countryButton,addAddress_stateButton,addAddressstateSelectButton,addAddress_dateButton,addAddress_continueButton;
    RelativeLayout addAddressstateNumberPickerLayout;
    LinearLayout countryLL,stateLL;
    ScrollView addAddressscrollviewBehind;
    NumberPicker addAddressstateNumberPicker;
    EditText addAddress_DOBET;

    EditText addAddress_TitleET,addAddress_nameET,addAddress_surnameET,addAddress_companyET,addAddress_add1ET,addAddress_add2ET,addAddress_countryET,addAddress_stateET,addAddress_townET,addAddress_poscodeET,addAddress_telET;
    TextView addAddress_TitleTV,addAddress_nameTV,addAddress_surnameTV,addAddress_companyTV,addAddress_add1TV,addAddress_add2TV,addAddress_countryTV,addAddress_stateTV,addAddress_townTV,addAddress_poscodeTV,addAddress_telTV;

    String GenderID="2",IDGender="2",AddressID, AddressFirstName, AddressLastName, AddressCompany, Address1, Address2, AddressCountryName, AddressCountryID, AddressStateName, AddressStateID="", AddressCityName, AddressPostcode, AddressPhone;

    RelativeLayout titleSelectionRL;
    ImageButton titleSelectionSelectIB;
    Button addAddress_TitleButton;
    NumberPicker titleSelectionNP;
    String[] titleList = {"Ms","Mr"};
    String actionIn;
    String sort = "",CartID,SelectedCountryIsoCode,SelectedShopID;
    float dimBack= (float)0.1;
    boolean lowerlolipop=false;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_adress, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        noInBag = pref.getString("cartItem", "");
        CartID = pref.getString("CartID", "");
        SelectedCountryIsoCode = pref.getString("SelectedCountryIsoCode", "MY");
        giftMessage  = pref.getString("giftMessage", "");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        //
        COME_FROM_WHERE = getArguments().getInt("COME_FROM_WHERE");
        EDIT_ADDRESS = getArguments().getBoolean("EDIT_ADDRESS");
        forBilling = getArguments().getString("for_billing");

        if(EDIT_ADDRESS)
        {
            try {
                ((MainActivity) getActivity()).changeToolBarText("Edit Address");

                AddressID = getArguments().getString("ADD_ID");
                IDGender = getArguments().getString("ID_GENDER","2");
                AddressFirstName = getArguments().getString("FIRST_NAME");
                AddressLastName = getArguments().getString("LAST_NAME");
                AddressCompany = getArguments().getString("COMPANY","");
                Address1 = getArguments().getString("ADD1");
                Address2 = getArguments().getString("ADD2","");
                AddressCountryName = getArguments().getString("COUNTRY_NAME");
                AddressCountryID = getArguments().getString("COUNTRY_ID");
                AddressStateName = getArguments().getString("STATE_NAME","");
                AddressStateID = getArguments().getString("STATE_ID","");
                AddressCityName = getArguments().getString("CITY_NAME");
                AddressPostcode = getArguments().getString("POSTCODE_NAME");
                AddressPhone = getArguments().getString("PHONE");
            }catch (Exception e){}

        }
        else
        {
            ((MainActivity) getActivity()).changeToolBarText("Add New Address");
        }

        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(true);
        ((MainActivity) getActivity()).setDrawerState(false);

        TextView selectTV  = (TextView) view.findViewById(R.id.selectTV);
        TextView TV1  = (TextView) view.findViewById(R.id.TV1);

        selectTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        TV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        addAddress_TitleTV = (TextView) view.findViewById(R.id.addAddress_TitleTV);
        addAddress_nameTV = (TextView)view.findViewById(R.id.addAddress_nameTV);
        addAddress_surnameTV = (TextView)view.findViewById(R.id.addAddress_surnameTV);
        addAddress_companyTV = (TextView)view.findViewById(R.id.addAddress_companyTV);
        addAddress_add1TV = (TextView)view.findViewById(R.id.addAddress_add1TV);
        addAddress_add2TV = (TextView)view.findViewById(R.id.addAddress_add2TV);
        addAddress_countryTV = (TextView)view.findViewById(R.id.addAddress_countryTV);
        addAddress_stateTV = (TextView)view.findViewById(R.id.addAddress_stateTV);
        addAddress_townTV = (TextView)view.findViewById(R.id.addAddress_townTV);
        addAddress_poscodeTV = (TextView)view.findViewById(R.id.addAddress_poscodeTV);
        addAddress_telTV = (TextView)view.findViewById(R.id.addAddress_telTV);

       addAddress_TitleET = (EditText)view.findViewById(R.id.addAddress_TitleET);
        addAddress_nameET = (EditText)view.findViewById(R.id.addAddress_nameET);
        addAddress_surnameET = (EditText)view.findViewById(R.id.addAddress_surnameET);
        addAddress_companyET = (EditText)view.findViewById(R.id.addAddress_companyET);
        addAddress_add1ET = (EditText)view.findViewById(R.id.addAddress_add1ET);
        addAddress_add2ET = (EditText)view.findViewById(R.id.addAddress_add2ET);
        addAddress_countryET = (EditText)view.findViewById(R.id.addAddress_countryET);
        addAddress_stateET = (EditText)view.findViewById(R.id.addAddress_stateET);
        addAddress_townET = (EditText)view.findViewById(R.id.addAddress_townET);
        addAddress_poscodeET = (EditText)view.findViewById(R.id.addAddress_poscodeET);
        addAddress_telET = (EditText)view.findViewById(R.id.addAddress_telET);

        if(EDIT_ADDRESS)
        {
            if(IDGender.equals("1")){
                addAddress_TitleET.setText("Mr");
                GenderID="1";
            }else{
                addAddress_TitleET.setText("Ms");
                GenderID="2";
            }
            addAddress_nameET.setText(AddressFirstName);
            addAddress_surnameET.setText(AddressLastName);
            addAddress_companyET.setText(AddressCompany);
            addAddress_add1ET.setText(Address1);
            addAddress_add2ET.setText(Address2);
            addAddress_countryET.setText(AddressCountryName);
            addAddress_countryET.setTextColor(getActivity().getResources().getColor(R.color.black));
            addAddress_stateET.setText(AddressStateName);
            addAddress_stateET.setTextColor(getActivity().getResources().getColor(R.color.black));
            addAddress_townET.setText(AddressCityName);
            addAddress_poscodeET.setText(AddressPostcode);
            addAddress_telET.setText(AddressPhone);
        }


        addAddress_continueButton = (Button)view.findViewById(R.id.addAddress_continueButton);

        addAddress_countryButton = (Button)view.findViewById(R.id.addAddress_countryButton);
        addAddress_stateButton = (Button)view.findViewById(R.id.addAddress_stateButton);
        addAddressstateSelectButton = (Button)view.findViewById(R.id.addAddressstateSelectButton);
//        addAddress_dateButton = (Button)view.findViewById(R.id.addAddress_dateButton);
        addAddressstateNumberPickerLayout = (RelativeLayout)view.findViewById(R.id.addAddressstateNumberPickerLayout);
        countryLL = (LinearLayout) view.findViewById(R.id.countryLLID);
        stateLL = (LinearLayout) view.findViewById(R.id.stateLLID);
        addAddressscrollviewBehind = (ScrollView)view.findViewById(R.id.addAddressscrollviewBehind);
        addAddressstateNumberPicker = (NumberPicker)view.findViewById(R.id.addAddressstateNumberPicker);

        addAddress_countryET = (EditText)view.findViewById(R.id.addAddress_countryET);
        addAddress_stateET = (EditText)view.findViewById(R.id.addAddress_stateET);
//        addAddress_DOBET = (EditText)view.findViewById(R.id.addAddress_DOBET);
//        addAddress_stateET.setEnabled(false);
        setDividerColor(addAddressstateNumberPicker, Color.LTGRAY);

        addAddressstateSelectButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_TitleET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_nameET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_surnameET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_companyET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_add1ET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_add2ET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_countryET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_stateET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_townET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_poscodeET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_telET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_continueButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        addAddress_TitleTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_nameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_surnameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_companyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_add1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_add2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_countryTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_stateTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_townTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_poscodeTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        addAddress_telTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        addAddress_countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    lowerlolipop=true;
                }else{
                    lowerlolipop=false;
                }
                Intent i = new Intent(getActivity(), CountryPickerFragment.class);
                startActivityForResult(i, 1);
                getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
//                Fragment fragment = new CountryPickerFragment();
//                FragmentManager fragmentManager = getActivity().getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainer, fragment,"CountryFragment");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();


            }

        });

        addAddress_countryET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    lowerlolipop=true;
                }else{
                    lowerlolipop=false;
                }
                Intent i = new Intent(getActivity(), CountryPickerFragment.class);
                startActivityForResult(i, 1);
                getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
//                Fragment fragment = new CountryPickerFragment();
//                FragmentManager fragmentManager = getActivity().getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainer, fragment,"CountryFragment");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();


            }

        });

        addAddress_stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!stateList.isEmpty())
                {
                    addAddressstateNumberPickerLayout.setVisibility(View.VISIBLE);
                    addAddressscrollviewBehind.setAlpha(dimBack);
                    addAddressstateNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                    addAddressstateNumberPicker.setMaxValue(stateNameList.length-1);
                    addAddressstateNumberPicker.setDisplayedValues(stateNameList);
                    sort=stateNameList[0];
                    addAddressstateNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            // TODO Auto-generated method stub

                            String[] values = picker.getDisplayedValues();

                            sort = values[newVal];

                        }
                    });
                }

            }
        });

        addAddress_stateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!stateList.isEmpty())
                {
                    addAddressstateNumberPickerLayout.setVisibility(View.VISIBLE);
                    addAddressscrollviewBehind.setAlpha(dimBack);
                    addAddressstateNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                    addAddressstateNumberPicker.setMaxValue(stateNameList.length-1);
                    addAddressstateNumberPicker.setDisplayedValues(stateNameList);
                    sort=stateNameList[0];
                    addAddressstateNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            // TODO Auto-generated method stub

                            String[] values = picker.getDisplayedValues();

                            sort = values[newVal];

                        }
                    });
                }


            }
        });

        addAddressstateSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addAddressstateNumberPickerLayout.setVisibility(View.GONE);
                addAddressscrollviewBehind.setAlpha(1);

                addAddress_stateET.setText(sort);
                addAddress_stateET.setTextColor(getResources().getColor(R.color.black));
                AddressStateID = stateList.get(addAddressstateNumberPicker.getValue()).getStateID();


            }

        });

        addAddress_continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EDIT_ADDRESS)
                {
                    if(SelectedShopID.equals("1")){

                        if (addAddress_stateET.getText().toString().isEmpty()||addAddress_nameET.getText().toString().isEmpty() || addAddress_surnameET.getText().toString().isEmpty() || addAddress_add1ET.getText().toString().isEmpty() || addAddress_countryET.getText().toString().isEmpty() || addAddress_townET.getText().toString().isEmpty() || addAddress_poscodeET.getText().toString().isEmpty() || addAddress_telET.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Please do not leave compulsory field empty", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
                        } else {

                            AddressFirstName = addAddress_nameET.getText().toString();
                            AddressLastName = addAddress_surnameET.getText().toString();
                            AddressCompany = addAddress_companyET.getText().toString();
                            Address1 = addAddress_add1ET.getText().toString();
                            Address2 = addAddress_add2ET.getText().toString();
                            AddressCountryName = addAddress_countryET.getText().toString();
                            AddressStateName = addAddress_stateET.getText().toString();
                            AddressCityName = addAddress_townET.getText().toString();
                            AddressPostcode = addAddress_poscodeET.getText().toString();
                            AddressPhone = addAddress_telET.getText().toString();

                            boolean digitsOnly = TextUtils.isDigitsOnly(AddressCityName);
                            if (digitsOnly) {

                                Toast toast = Toast.makeText(getActivity(),"City is not valid", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();


                            }else {



                                validatePostcode(addAddress_poscodeET.getText().toString());

                            }
                        }

                    }

                    else {
                        if (addAddress_nameET.getText().toString().isEmpty() || addAddress_surnameET.getText().toString().isEmpty() || addAddress_add1ET.getText().toString().isEmpty() || addAddress_countryET.getText().toString().isEmpty() || addAddress_townET.getText().toString().isEmpty() || addAddress_poscodeET.getText().toString().isEmpty() || addAddress_telET.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Please do not leave compulsory field empty", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
                        } else {


                            AddressFirstName = addAddress_nameET.getText().toString();
                            AddressLastName = addAddress_surnameET.getText().toString();
                            AddressCompany = addAddress_companyET.getText().toString();
                            Address1 = addAddress_add1ET.getText().toString();
                            Address2 = addAddress_add2ET.getText().toString();
                            AddressCountryName = addAddress_countryET.getText().toString();
                            AddressStateName = addAddress_stateET.getText().toString();
                            AddressCityName = addAddress_townET.getText().toString();
                            AddressPostcode = addAddress_poscodeET.getText().toString();
                            AddressPhone = addAddress_telET.getText().toString();

                            AddAddressWS(UserID, AddressFirstName, AddressLastName, AddressCompany, Address1, Address2, AddressCityName, AddressPostcode, AddressCountryID, AddressStateID, AddressPhone);


                        }
                    }
                }
                else
                {
                    if(SelectedShopID.equals("1")){


                        if (addAddress_stateET.getText().toString().isEmpty()||addAddress_nameET.getText().toString().isEmpty() || addAddress_surnameET.getText().toString().isEmpty() || addAddress_add1ET.getText().toString().isEmpty() || addAddress_countryET.getText().toString().isEmpty() || addAddress_townET.getText().toString().isEmpty() || addAddress_poscodeET.getText().toString().isEmpty() || addAddress_telET.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Please do not leave compulsory field empty", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
                        } else {

                            AddressFirstName = addAddress_nameET.getText().toString();
                            AddressLastName = addAddress_surnameET.getText().toString();
                            AddressCompany = addAddress_companyET.getText().toString();
                            Address1 = addAddress_add1ET.getText().toString();
                            Address2 = addAddress_add2ET.getText().toString();
                            AddressCountryName = addAddress_countryET.getText().toString();
                            AddressStateName = addAddress_stateET.getText().toString();
                            AddressCityName = addAddress_townET.getText().toString();
                            AddressPostcode = addAddress_poscodeET.getText().toString();
                            AddressPhone = addAddress_telET.getText().toString();

                            boolean digitsOnly = TextUtils.isDigitsOnly(AddressCityName);
                            if (digitsOnly) {

                                Toast toast = Toast.makeText(getActivity(),"City is not valid", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }else {

                                if(AddressFirstName.matches(".*\\d.*") || AddressLastName.matches(".*\\d.*"))
                                {
                                    Toast toast = Toast.makeText(getActivity(),"Invalid Name", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                                else {

                                    if(AddressFirstName.matches(".*\\d.*") || AddressLastName.matches(".*\\d.*"))
                                    {
                                        Toast toast = Toast.makeText(getActivity(),"Invalid Name", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }

                                    else {
                                        validatePostcode(addAddress_poscodeET.getText().toString());
                                    }

                                }


                            }



                        }

                    }else {
                        if (addAddress_nameET.getText().toString().isEmpty() || addAddress_surnameET.getText().toString().isEmpty() || addAddress_add1ET.getText().toString().isEmpty() || addAddress_countryET.getText().toString().isEmpty() || addAddress_townET.getText().toString().isEmpty() || addAddress_poscodeET.getText().toString().isEmpty() || addAddress_telET.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Please do not leave compulsory field empty", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();
                        } else {

                            AddressFirstName = addAddress_nameET.getText().toString();
                            AddressLastName = addAddress_surnameET.getText().toString();
                            AddressCompany = addAddress_companyET.getText().toString();
                            Address1 = addAddress_add1ET.getText().toString();
                            Address2 = addAddress_add2ET.getText().toString();
                            AddressCountryName = addAddress_countryET.getText().toString();
                            AddressStateName = addAddress_stateET.getText().toString();
                            AddressCityName = addAddress_townET.getText().toString();
                            AddressPostcode = addAddress_poscodeET.getText().toString();
                            AddressPhone = addAddress_telET.getText().toString();

                            EditAddressWS(AddressID, AddressFirstName, AddressLastName, AddressCompany, Address1, Address2, AddressCityName, AddressPostcode, AddressCountryID, AddressStateID, AddressPhone);


                        }
                    }
                }
            }
        });

        if(SelectedShopID.equals("1"))
        {
            addAddress_stateET.setCursorVisible(false);
            addAddress_stateET.setFocusable(false);
        }

        else {

            stateLL.setVisibility(View.GONE);
        }

        //Title pickerview
        titleSelectionRL = (RelativeLayout) view.findViewById(R.id.titleSelectionRL);
        titleSelectionSelectIB = (ImageButton) view.findViewById(R.id.titleSelectionSelectIB);
        addAddress_TitleButton = (Button) view.findViewById(R.id.addAddress_TitleButton);
        titleSelectionNP = (NumberPicker) view.findViewById(R.id.titleSelectionNP);

        addAddress_TitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleSelectionRL.setVisibility(View.VISIBLE);

                titleSelectionNP.setMaxValue(titleList.length-1);
                titleSelectionNP.setDisplayedValues(titleList);

                titleSelectionNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        // TODO Auto-generated method stub

                        String[] values = picker.getDisplayedValues();
                        if(values[newVal].equals("Ms")){
                            GenderID="2";
                        }else{
                            GenderID="1";
                        }
                        addAddress_TitleET.setText(values[newVal]);

                    }
                });
            }
        });

        titleSelectionSelectIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleSelectionRL.setVisibility(View.GONE);
            }
        });
        if(SelectedCountryIsoCode.equals("MY")) {
            getCountryList();
        }
        return view;

    }
    private void updateLabel() {

        GregorianCalendar c = new GregorianCalendar(myCalendar
                .get(Calendar.YEAR),  myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

//        addAddress_DOBET.setText(sdf.format(c.getTime()));
//        addAddress_DOBET.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));



    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
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
    }

    private void getCountryList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
            String action = "Infos/countries/" + SelectedCountryIsoCode + "?apikey="+apikey;
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(action);
    }
    private void goToNextStepWS(String giftMessage,String action1)
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        actionIn=action1;
        String action="Carts/OrderStep1?apikey="+apikey+"&id_cart="+CartID+"&gift_message="+giftMessage;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);
    }

    private void AddAddressWS(String userID, String firstName, String lastName, String company, String address1, String address2, String City, String Postcode, String CountryID, String StateID, String phoneNo)
    { System.out.println("addressbackslash" + address1);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Addresses/addAddress";
        RequestBody formBody;
        if(company.length()==0){
             formBody = new FormBody.Builder()
                     .add("apikey",apikey)
                    .add("id_customer",userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }else  if(StateID.length()==0){
             formBody = new FormBody.Builder()
                     .add("apikey",apikey)
                    .add("id_customer",userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }else  if(address2.length()==0){
             formBody = new FormBody.Builder()
                     .add("apikey",apikey)
                    .add("id_customer",userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }else if(address2.length()==0||company.length()==0) {
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_customer", userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }else if(address2.length()==0||StateID.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_customer", userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }else if(company.length()==0||StateID.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_customer", userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }
        else if(company.length()==0||StateID.length()==0||address2.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_customer", userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }else{
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_customer",userID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }

        WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    private void EditAddressWS(String addressID, String firstName, String lastName, String company, String address1, String address2, String City, String Postcode, String CountryID, String StateID, String phoneNo)
    {   System.out.println(
            "POST URL PARAMS id_address" +addressID+
                    "company"+ company+
            "firstname"+ firstName+
            "lastname"+ lastName+
            "address1"+ address1+
            "address2"+ address2+
            "city"+ City+
            "postcode"+ Postcode+
            "phone"+ phoneNo+
            "id_country"+ CountryID+
            "id_state"+ StateID+
            "id_gender"+ GenderID);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Addresses/updateAddress";
        RequestBody formBody;
        if(company.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }else  if(StateID.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }else  if(address2.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }else if(address2.length()==0||company.length()==0) {
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }else if(address2.length()==0||StateID.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }else if(company.length()==0||StateID.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }
        else if(company.length()==0||StateID.length()==0||address2.length()==0){
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("address1", address1)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_gender", GenderID)
                    .build();
        }else{
            formBody = new FormBody.Builder()
                    .add("apikey",apikey)
                    .add("id_address",addressID)
                    .add("firstname", firstName)
                    .add("lastname", lastName)
                    .add("company", company)
                    .add("address1", address1)
                    .add("address2", address2)
                    .add("city", City)
                    .add("postcode", Postcode)
                    .add("phone", phoneNo)
                    .add("id_country", CountryID)
                    .add("id_state", StateID)
                    .add("id_gender", GenderID)
                    .build();
        }

        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("get JSON Response = " + result);

        if(result!=null){
            try{
                String action = result.getString("action");

                if(action.equals("Addresses_addAddress"))
                {
                    if(result.getBoolean("status"))
                    {
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("SelectedCountryIDAdd", "");
                        editor.putString("countries", "");
                        editor.putString("SelectedCountryNameAdd", "");

                        editor.apply();
                        if(COME_FROM_WHERE == FROM_MY_ADDRESS)
                        {
                            Toast.makeText(getActivity(), "Added New Address", Toast.LENGTH_LONG).show();
                            System.out.println("Datang 1");
                            getFragmentManager().popBackStack();



                        }
                        else if(COME_FROM_WHERE == FROM_SHOPPING_BAG)
                        {

                            goToNextStepWS(giftMessage,action);

                        }
                        else if(COME_FROM_WHERE == FROM_REGISTRATION)
                        {
                            Toast.makeText(getActivity(), "Added New Address", Toast.LENGTH_LONG).show();
                            System.out.println("Datang 3");
                            new Handler().post(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = getActivity().getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().overridePendingTransition(0, 0);
                                    getActivity().finish();

                                    getActivity().overridePendingTransition(0, 0);
                                    startActivity(intent);



                                }
                            });
                        }
                        else if(COME_FROM_WHERE == FROM_SHOPPING_BAG_NOADDRESS)
                        {
                            goToNextStepWS(giftMessage,action);
                        }
                    }
                }
                else if(action.equals("Addresses_updateAddress"))
                {
                    if(result.getBoolean("status"))
                    {
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("SelectedCountryIDAdd", "");
                        editor.putString("countries", "");
                        editor.putString("SelectedCountryNameAdd", "");

                        editor.apply();
                        if(COME_FROM_WHERE == FROM_MY_ADDRESS)
                        {
                            Toast.makeText(getActivity(), "Address Edited", Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack();


                        }
                        else if(COME_FROM_WHERE == FROM_SHOPPING_BAG)
                        {
                            goToNextStepWS(giftMessage,action);

                        }
                        else if(COME_FROM_WHERE == FROM_REGISTRATION)
                        {
                            Toast.makeText(getActivity(), "Address Edited", Toast.LENGTH_LONG).show();
                            System.out.println("Datang 3");
                            new Handler().post(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = getActivity().getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().overridePendingTransition(0, 0);
                                    getActivity().finish();

                                    getActivity().overridePendingTransition(0, 0);
                                    startActivity(intent);



                                }
                            });
                        }
                    }
                }else if(action.equals("Infos_countries")) {
                    if (result.getBoolean("status")) {
                        JSONObject jsonObject = null;

                        jsonObject = result.getJSONObject("data");

                        String id_country = jsonObject.getJSONObject("country").getString("id_country");
                        String country = jsonObject.getJSONObject("country").getString("name");

                        if (AddressCountryID.equals(id_country)) {
                            if (!country.equalsIgnoreCase("Malaysia")) {
                                addAddress_stateET.setText("");
                                addAddress_stateET.setHint("");
                                addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown_inactive);
                                addAddress_stateButton.setEnabled(false);
                            } else {
                                addAddress_stateET.setHint("State*");
                                addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown);
                                addAddress_stateButton.setEnabled(true);
                            }
                            addAddress_countryET.setText(country);
                            addAddress_countryET.setTextColor(getResources().getColor(R.color.black));

                            JSONArray stateArray = jsonObject.getJSONArray("states");

                            for (int k = 0; k < stateArray.length(); k++) {
                                stateList.add(k, new stateItem(stateArray.getJSONObject(k).getString("id"), stateArray.getJSONObject(k).getString("name")));

                                stateNameArrayList.add(k, stateArray.getJSONObject(k).getString("name"));
                            }

                            stateNameList = stateNameArrayList.toArray(new String[stateNameArrayList.size()]);

                        }

                    }
                } else if(action.equals("Carts_OrderStep1")) {
                    if (result.getBoolean("status")) {
                        cartResultJObj1 = result;
                        JSONObject data = new JSONObject();
                        data = result.getJSONObject("data");


                        String nextPage = data.getString("next_page");

                        if (nextPage.equals("addressPage")) {
                            if(COME_FROM_WHERE == FROM_SHOPPING_BAG)
                            {
                                if(actionIn.equals("Addresses_addAddress")) {
                                    Toast.makeText(getActivity(), "Added New Address", Toast.LENGTH_LONG).show();
                                    Fragment fragment = new NewOrderConfirmationFragment();
                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment,"NewOrderConfirmationFragment");
                                    fragmentTransaction.addToBackStack(null);

                                    Bundle args = new Bundle();
                                    String cartResultJObjString = result.toString();
                                    System.out.println("cartResultObj" + cartResultJObjString);
                                    args.putString("cartResultJObj", cartResultJObjString);
                                    args.putString("edited", "done");
                                    args.putString("FOR_BILLING", forBilling);

                                    fragment.setArguments(args);

                                    fragmentTransaction.commit();
                                }else {
                                    Toast.makeText(getActivity(), "Address Edited", Toast.LENGTH_LONG).show();
                                    Fragment fragment = new NewOrderConfirmationFragment();
                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.replace(R.id.fragmentContainer, fragment,"NewOrderConfirmationFragment");
                                    fragmentTransaction.addToBackStack(null);

                                    Bundle args = new Bundle();
                                    String cartResultJObjString = result.toString();
                                    System.out.println("cartResultObj" + cartResultJObjString);
                                    args.putString("cartResultJObj", cartResultJObjString);
                                    args.putString("edited", "done");
                                    args.putString("FOR_BILLING", forBilling);
                                    fragment.setArguments(args);

                                    fragmentTransaction.commit();
                                }
                            }
                            else if(COME_FROM_WHERE == FROM_REGISTRATION)
                            {
                                System.out.println("Datang 3");

                            }
                            else if(COME_FROM_WHERE == FROM_SHOPPING_BAG_NOADDRESS)
                            {
                                Fragment fragment = new NewAddressBillingFragment();
                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment,"NewAddressBillingFragment");
                                fragmentTransaction.addToBackStack(null);

                                Bundle args = new Bundle();
                                String cartResultJObjString = cartResultJObj1.toString();
                                System.out.println("cartResultObj" + cartResultJObjString);
                                args.putString("cartResultJObj", cartResultJObjString);
                                fragment.setArguments(args);

                                fragmentTransaction.commit();
                                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("NeedToGoBackToCart", "0");
                                editor.apply();
                            }

                        } else if (nextPage.equals("addAddressPage")) {
                            Fragment fragment = new AddAdressFragment();
                            Bundle bundle = new Bundle();
                            String cartResultJObjString = cartResultJObj1.toString();
                            bundle.putString("cartResultJObj", cartResultJObjString);
                            bundle.putInt("COME_FROM_WHERE", 3);
                            bundle.putBoolean("EDIT_ADDRESS", false);
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if (nextPage.equals("cart")) {
                            // Reload current fragment
                            Fragment frg = null;
                            frg = getFragmentManager().findFragmentByTag("ShoppingBagFragment");
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                        }
                    }

                }

                else if (action.equals("Addresses_postcode")){

                    System.out.println("get JSON postcode = " +result);

                    if(result.getBoolean("status"))
                    {

                        if(!result.has("data") || result.isNull("data"))
                        {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Invalid Postcode. Please enter correct postcode.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                        }

                        JSONObject data = new JSONObject();
                        data = result.getJSONObject("data");

                        String validate = data.getString("StatusPostcode");


                        if(validate.equals("true"))
                        {

                            if(EDIT_ADDRESS)
                            {

                                EditAddressWS(AddressID, AddressFirstName, AddressLastName, AddressCompany, Address1, Address2, AddressCityName, AddressPostcode, AddressCountryID, AddressStateID, AddressPhone);

                            }

                            else {

                                AddAddressWS(UserID, AddressFirstName, AddressLastName, AddressCompany, Address1, Address2, AddressCityName, AddressPostcode, AddressCountryID, AddressStateID, AddressPhone);

                            }

                        }
                        else
                        {

                            Toast toast = Toast.makeText(getActivity(),
                                    "Invalid Postcode. Please enter correct postcode.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                        }

                    }


                }
            }
            catch (Exception e){


            }

        }
        else{

            new androidx.appcompat.app.AlertDialog.Builder(getActivity())
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

        System.out.println("Hello OnKitkat add address "+data);

            if(data != null) {
                if (SelectedCountryIsoCode.equals("MY")) {
                    AddressCountryID = data.getStringExtra("SelectedCountryID");
                    try {
                        JSONObject jObj = new JSONObject(data.getStringExtra("countries"));

                        if (jObj.getBoolean("status")) {
                            if (jObj.getString("action").equals("Infos_countries")) {

                                JSONObject jsonObject = null;

                                jsonObject = jObj.getJSONObject("data");

                                String id_country = jsonObject.getJSONObject("country").getString("id_country");
                                String country = jsonObject.getJSONObject("country").getString("name");

                                if (AddressCountryID.equals(id_country)) {
                                    if (!country.equalsIgnoreCase("Malaysia")) {
                                        addAddress_stateET.setText("");
                                        addAddress_stateET.setHint("");
                                        addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown_inactive);
                                        addAddress_stateButton.setEnabled(false);
                                    } else {
                                        addAddress_stateET.setHint("State*");
                                        addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown);
                                        addAddress_stateButton.setEnabled(true);
                                    }
                                    addAddress_countryET.setText(country);
                                    addAddress_countryET.setTextColor(getResources().getColor(R.color.black));

                                    JSONArray stateArray = jsonObject.getJSONArray("states");

                                    for (int k = 0; k < stateArray.length(); k++) {
                                        stateList.add(k, new stateItem(stateArray.getJSONObject(k).getString("id"), stateArray.getJSONObject(k).getString("name")));

                                        stateNameArrayList.add(k, stateArray.getJSONObject(k).getString("name"));
                                    }

                                    stateNameList = stateNameArrayList.toArray(new String[stateNameArrayList.size()]);

                                }

                            }
                        }


                    } catch (Exception e) {
                    }
                }else{
                    AddressCountryID = data.getStringExtra("SelectedCountryIDAdd");
                    addAddress_stateET.setText("");
                    addAddress_stateET.setHint("");
                    addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown_inactive);
                    addAddress_stateButton.setEnabled(false);
                    addAddress_countryET.setText(data.getStringExtra("SelectedCountryNameAdd"));
                    addAddress_countryET.setTextColor(getResources().getColor(R.color.black));
                }
            }


        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onResume() {
        super.onResume();
        if(countResume!=0) {
            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
            if (SelectedCountryIsoCode.equals("MY")) {
                AddressCountryID = pref.getString("SelectedCountryIDAdd", "");
                try {
                    JSONObject jObj = new JSONObject(pref.getString("countries", ""));

                    if (jObj.getBoolean("status")) {
                        if (jObj.getString("action").equals("Infos_countries")) {

                            JSONObject jsonObject = null;

                            jsonObject = jObj.getJSONObject("data");

                            String id_country = jsonObject.getJSONObject("country").getString("id_country");
                            String country = jsonObject.getJSONObject("country").getString("name");

                            if (AddressCountryID.equals(id_country)) {
                                if (!country.equalsIgnoreCase("Malaysia")) {
                                    addAddress_stateET.setText("");
                                    addAddress_stateET.setHint("");
                                    addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown_inactive);
                                    addAddress_stateButton.setEnabled(false);
                                } else {
                                    addAddress_stateET.setHint("State*");
                                    addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown);
                                    addAddress_stateButton.setEnabled(true);
                                }
                                addAddress_countryET.setText(country);
                                addAddress_countryET.setTextColor(getResources().getColor(R.color.black));

                                JSONArray stateArray = jsonObject.getJSONArray("states");

                                for (int k = 0; k < stateArray.length(); k++) {
                                    stateList.add(k, new stateItem(stateArray.getJSONObject(k).getString("id"), stateArray.getJSONObject(k).getString("name")));

                                    stateNameArrayList.add(k, stateArray.getJSONObject(k).getString("name"));
                                }

                                stateNameList = stateNameArrayList.toArray(new String[stateNameArrayList.size()]);

                            }

                        }
                    }


                } catch (Exception e) {
                }
            } else {

                AddressCountryID = pref.getString("SelectedCountryIDAdd", "");
                addAddress_stateET.setText("");
                addAddress_stateET.setHint("");
                addAddress_stateButton.setBackgroundResource(R.drawable.btn_dropdown_inactive);
                addAddress_stateButton.setEnabled(false);
                addAddress_countryET.setText(pref.getString("SelectedCountryNameAdd", ""));
                addAddress_countryET.setTextColor(getResources().getColor(R.color.black));
            }
        }
        countResume++;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("SelectedCountryIDAdd", "");
        editor.putString("countries", "");
        editor.putString("SelectedCountryNameAdd", "");

        editor.apply();

    }

    private void validatePostcode(String postCode)
    {

        System.out.println("get Postcode = " + postCode);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");


        String action="Addresses/postcode";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("postcode", postCode)
                .add("id_customer",UserID)
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }
}
