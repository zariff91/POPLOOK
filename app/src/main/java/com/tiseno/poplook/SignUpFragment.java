package com.tiseno.poplook;

import android.app.DatePickerDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessPut;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderIdentifiers;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class SignUpFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {
    TextView signUp_nameTV,signUp_surnameTV,signUp_DOBTV,signUp_emailTV,signUp_reEmailTV,signUp_passTV,signUp_rePassTV;
    EditText signUp_nameET,signUp_surnameET,signUp_emailET,signUp_reEmailET,signUp_passET,signUp_rePassET;
    CheckBox signUp_checkAllowMailing,signUp_checkAllowNotification,signUp_checkAcceptPP;
    ImageButton dateButton;
    Button signUp_singUpButton;
    TextView signUp_DOBET;

    TextView privacyPolicyTV,personalDataProtectionNoticeTV;

    private DatePickerDialog.OnDateSetListener mDateListener;


    Calendar myCalendar = Calendar.getInstance();
    int id_lang=1,mailing=1,pushnotification=1;
    String cartID, TokenID,LanguageID="1";
    String email,name,surname,password,dob,SelectedShopID="1";

    String dobForApi;

    int SelectedMonth = 0,parsedcartID;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SelectedMonth = monthOfYear;
            System.out.println("SelectedMonth is" + SelectedMonth);

            updateLabel();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        Insider.Instance.tagEvent("register_form_viewed").addParameterWithBoolean("needRegister",true).build();

        ((MainActivity) getActivity()).changeToolBarText("Sign Up Account");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        LanguageID = pref.getString("LanguageID", "1");
        cartID = pref.getString("CartID", "");
        TokenID = pref.getString("TokenID", "");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        //
        signUp_nameTV = (TextView)view.findViewById(R.id.signUp_nameTV);
        signUp_surnameTV = (TextView)view.findViewById(R.id.signUp_surnameTV);
        signUp_DOBTV = (TextView)view.findViewById(R.id.signUp_DOBTV);
        signUp_emailTV = (TextView)view.findViewById(R.id.signUp_emailTV);
        signUp_reEmailTV = (TextView)view.findViewById(R.id.signUp_reEmailTV);
        signUp_passTV = (TextView)view.findViewById(R.id.signUp_passTV);
        signUp_rePassTV = (TextView)view.findViewById(R.id.signUp_rePassTV);


        signUp_nameET = (EditText)view.findViewById(R.id.signUp_nameET);
        signUp_surnameET = (EditText)view.findViewById(R.id.signUp_surnameET);
        signUp_DOBET = (TextView)view.findViewById(R.id.signUp_DOBET);
        signUp_emailET = (EditText)view.findViewById(R.id.signUp_emailET);
        signUp_reEmailET = (EditText)view.findViewById(R.id.signUp_reEmailET);
        signUp_passET = (EditText)view.findViewById(R.id.signUp_passET);
        signUp_rePassET = (EditText)view.findViewById(R.id.signUp_rePassET);

        privacyPolicyTV = (TextView)view.findViewById(R.id.privacyPolicyTV);
        personalDataProtectionNoticeTV = (TextView)view.findViewById(R.id.personalDataProtectionNoticeTV);

        final SpannableStringBuilder str = new SpannableStringBuilder(privacyPolicyTV.getText().toString());
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 64, 78, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacyPolicyTV.setText(str);

        final SpannableStringBuilder str2 = new SpannableStringBuilder(personalDataProtectionNoticeTV.getText().toString());
        str2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        personalDataProtectionNoticeTV.setText(str2);

        privacyPolicyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalDataProtectionNoticeTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        privacyPolicyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PrivacyPolicyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromSignUp", "Yeah");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        personalDataProtectionNoticeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PersonalDataProtectionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        signUp_checkAllowMailing = (CheckBox)view.findViewById(R.id.signUp_checkAllowMailing);
        signUp_checkAllowNotification = (CheckBox)view.findViewById(R.id.signUp_checkAllowNotification);
        signUp_checkAcceptPP = (CheckBox)view.findViewById(R.id.signUp_checkAcceptPP);

        signUp_singUpButton = (Button)view.findViewById(R.id.signUp_singUpButton);

        signUp_nameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_surnameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_DOBTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_emailTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_reEmailTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_passTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_rePassTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        signUp_nameET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_surnameET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_DOBET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_emailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_reEmailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_passET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_rePassET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        signUp_checkAllowMailing.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_checkAllowNotification.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        signUp_checkAcceptPP.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        signUp_singUpButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        signUp_passET.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        signUp_rePassET.setTransformationMethod(new AsteriskPasswordTransformationMethod());


//        String hyperlink=signUp_checkAcceptPP.getText().toString();


        dateButton = (ImageButton)view.findViewById(R.id.signUp_dateButton);


        signUp_DOBET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth, mDateListener, year, day, month);

                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

            }
        });


        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, mDateListener, year, day, month);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;

                String date = month + "/" + dayOfMonth + "/" + year;

                dobForApi = year + "-" + month + "-" + dayOfMonth;

                signUp_DOBET.setText(date);
                signUp_DOBET.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));

                System.out.println("get birthday " + dobForApi);

            }
        };


        signUp_singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean GotProblemOccured = false;

                email = signUp_emailET.getText().toString();
                String retypeEmail = signUp_reEmailET.getText().toString();
                name = signUp_nameET.getText().toString();
                surname = signUp_surnameET.getText().toString();
                password = signUp_passET.getText().toString();
                String retypePassword = signUp_rePassET.getText().toString();
                dob = dobForApi;

                if(dob == null || dob.length()==0)
                {
                    GotProblemOccured = true;

                    Toast toast = Toast.makeText(getActivity(),
                            "Please enter your birthday.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.show();

                }

                if(email.length() == 0 || retypeEmail.length() == 0 || name.length() == 0 || surname.length() == 0 || password.length() == 0 || retypePassword.length() == 0)
                {
                    GotProblemOccured = true;
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please do not leave compulsory field empty")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                if(!email.equals(retypeEmail))
                {
                    GotProblemOccured = true;
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please make sure the Email address is matched")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                if(!password.equals(retypePassword))
                {
                    GotProblemOccured = true;
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please make sure the password is matched")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                if(signUp_checkAllowMailing.isChecked()){
                    mailing = 1;
                }
                else if(!signUp_checkAllowMailing.isChecked()){
                    mailing = 0;
                }


                if(signUp_checkAllowNotification.isChecked()){
                    pushnotification = 1;
                }
                else if(!signUp_checkAllowNotification.isChecked()){
                    pushnotification = 0;
                }

//                if(SelectedMonth > 0)
//                {
//                    String inputPattern = "dd MMMM yyyy";
//                    String outputPattern = "dd";
//                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
//                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);
//
//                    String inputPattern2 = "dd MMMM yyyy";
//                    String outputPattern2 = "yyyy";
//                    SimpleDateFormat inputFormat2 = new SimpleDateFormat(inputPattern2, Locale.ENGLISH);
//                    SimpleDateFormat outputFormat2 = new SimpleDateFormat(outputPattern2, Locale.ENGLISH);

//                    Date date = null;
//                    String str = null;
//                    Date date2 = null;
//                    String str2 = null;

//                    try {
//                        date = inputFormat.parse(dob);
//                        str = outputFormat.format(date);
//                        date2 = inputFormat2.parse(dob);
//                        str2 = outputFormat2.format(date2);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    dob = str2 + "-" + String.valueOf(SelectedMonth+1) + "-" + str;

//                }

                if(!signUp_checkAcceptPP.isChecked())
                {
                    GotProblemOccured = true;
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please tick the checkbox to confirm that you have read and accepted our Privacy Policy and Personal Data Protection Notice")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                System.out.println("emaiiillll:" + email);
                System.out.println("name:" + name);
                System.out.println("surname:" + surname);
                System.out.println("pass:" + password);
                System.out.println("dob:" + dob);
                System.out.println("mailing:" + mailing);
                System.out.println("push:" + pushnotification);
                System.out.println("Get DOB:" + dob);


                if(!GotProblemOccured)
                {
                    signUp();
                }


            }

        });

        return view;
    }
    private void updateLabel() {

        GregorianCalendar c = new GregorianCalendar(myCalendar
                .get(Calendar.YEAR),  myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");


    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    private void signUp(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="index.php/Customers/registration";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_shop",SelectedShopID)
                .add("id_lang", LanguageID)
                .add("email", email)
                .add("firstname", name)
                .add("lastname", surname)
                .add("password", password)
                .add("birthday", dob)
                .add("newsletter", String.valueOf(mailing))
                .add("optin", "0")
                .add("id_cart", cartID)
                .add("device_type", "Android")
                .add("token_id", TokenID)
                .add("enable_push_notification", String.valueOf(pushnotification))
                .build();
        WebServiceAccessPut callws = new WebServiceAccessPut(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){

            try {
                if (result.getBoolean("status"))
                {
                    System.out.println("hahaaaaaaab");
                    if (result.getString("action").equals("Customers_registration"))
                    {

                        System.out.println("get JSON Sign Up = " + result);

                        JSONObject data=result.getJSONObject("data");
                        String id_customer = data.getString("id");
                        String id_cart = data.getString("id_cart");
                        String name = data.getString("firstname");
                        String lastname = data.getString("lastname");
                        String email = data.getString("email");
                        String newsletter = data.getString("newsletter");

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("UserID", id_customer);
                        editor.putString("CartID", id_cart);
                        editor.putString("LanguageID", "1");
                        editor.putString("Name", name);
                        editor.putString("LastName", lastname);
                        editor.putString("Email", email);
                        editor.putString("Newsletter", newsletter);
                        editor.apply();

                        InsiderIdentifiers identifiers = new InsiderIdentifiers();
                        identifiers.addEmail(email);
                        identifiers.addUserID(id_customer);
                        Insider.Instance.getCurrentUser().login(identifiers);

                        Toast toast = Toast.makeText(getActivity(),
                                "Registered Successfully", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

                        Insider.Instance.tagEvent("register_form_viewed").addParameterWithBoolean("needRegister",false).build();

                        try{
                            if(getArguments().getString("fromGuestCheckOut").equals("1"))
                            {
                                 pref = getActivity().getSharedPreferences("MyPref", 0);
                                 editor = pref.edit();

                                editor.putString("NeedToGoBackToCart", "1");
//                                ((MainActivity)getActivity()).getSideMenuList();

                                editor.commit();

                                Fragment fragment = new AddAdressFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("COME_FROM_WHERE", 2);
                                bundle.putBoolean("EDIT_ADDRESS", false);
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }else{

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

                        }catch (Exception e){
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

                }else{
                        String message = result.getString("message");
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Message")
                                .setMessage(message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();


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