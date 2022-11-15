package com.tiseno.poplook;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessPost;
import com.tiseno.poplook.webservice.WebServiceAccessPut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInformationFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    TextView step1TV,step2TV,personalInformationChangeEmailLBl,personalInformationCurrentEmailLblTV, personalInformationNewEmailLblET,personalInformationConfirmEmailLblTV;
    TextView changeYourEmailSaveChangesTV,personalInformationChangePasswordLblTV,personalInformationCurrentPasswordLblTV,personalInformationNewPasswordLblTV,personalInformationConfirmPasswordLblTV;
    TextView changeYourPasswordSaveChangesTV,changeYourNameLblTV,personalInformationCurrentFirstNameLblTV,personalInformationNewFirstNameLblTV,personalInformationCurrentSurNameLblTV;
    TextView personalInformationNewSurNameLblTV,changeYourNameSaveChangesTV,personalInformationMailingLblTV,mailingListPreferencesSaveChangesTV,personalDataNoticeLblTV,personalDataNoticeAcceptedTV;

    TextView personalInformationCurrentEmailTV,personalInformationCurrentPasswordTV, personalInformationCurrentFirstNameTV, personalInformationCurrentSurNameTV;
    EditText personalInformationNewEmailET,personalInformationConfirmEmailET,personalInformationNewPasswordET,personalInformationConfirmPasswordET;
    EditText personalInformationNewFirstNameET,personalInformationNewSurNameET;

    CheckBox personalInformationMailingSubscribedCB;

    RelativeLayout changeYourEmailSaveChangesRL;

    ImageButton changeYourEmailSaveChangesBtnIV, changeYourPasswordSaveChangesBtnIV, changeYourNameSaveChangesBtnIV, mailingListPreferencesSaveChangesBtnIV;

    String UserID,CartID,LanguageID,Email,Name,LastName,Newsletter;

    String confirmfirstName,confirmlasttName,confirmconfirmEmail;

    int UPDATED_TYPE;
    final int UPDATED_EMAIL = 0;
    final int UPDATED_PASSWORD = 1;
    final int UPDATED_NAME = 2;
    final int UPDATED_MAILINGLIST = 3;

    public PersonalInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_personal_information, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Personal Information");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "");
        LanguageID = pref.getString("LanguageID", "1");
        Email = pref.getString("Email","");
        Name = pref.getString("Name","");
        LastName = pref.getString("LastName","");
        Newsletter = pref.getString("Newsletter","0");

        step1TV = (TextView) contentView.findViewById(R.id.step1TV);
        step2TV = (TextView) contentView.findViewById(R.id.step2TV);
        personalInformationChangeEmailLBl = (TextView) contentView.findViewById(R.id.personalInformationChangeEmailLBl);
        personalInformationCurrentEmailLblTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentEmailLblTV);
        personalInformationNewEmailLblET = (TextView) contentView.findViewById(R.id.personalInformationNewEmailLblET);
        personalInformationConfirmEmailLblTV = (TextView) contentView.findViewById(R.id.personalInformationConfirmEmailLblTV);
        changeYourEmailSaveChangesTV = (TextView) contentView.findViewById(R.id.changeYourEmailSaveChangesTV);
        personalInformationChangePasswordLblTV = (TextView) contentView.findViewById(R.id.personalInformationChangePasswordLblTV);
        personalInformationCurrentPasswordLblTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentPasswordLblTV);
        personalInformationNewPasswordLblTV = (TextView) contentView.findViewById(R.id.personalInformationNewPasswordLblTV);
        personalInformationConfirmPasswordLblTV = (TextView) contentView.findViewById(R.id.personalInformationConfirmPasswordLblTV);
        changeYourPasswordSaveChangesTV = (TextView) contentView.findViewById(R.id.changeYourPasswordSaveChangesTV);
        changeYourNameLblTV = (TextView) contentView.findViewById(R.id.changeYourNameLblTV);
        personalInformationCurrentFirstNameLblTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentFirstNameLblTV);
        personalInformationNewFirstNameLblTV = (TextView) contentView.findViewById(R.id.personalInformationNewFirstNameLblTV);
        personalInformationCurrentSurNameLblTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentSurNameLblTV);
        personalInformationNewSurNameLblTV = (TextView) contentView.findViewById(R.id.personalInformationNewSurNameLblTV);
        changeYourNameSaveChangesTV = (TextView) contentView.findViewById(R.id.changeYourNameSaveChangesTV);
        personalInformationMailingLblTV = (TextView) contentView.findViewById(R.id.personalInformationMailingLblTV);
        mailingListPreferencesSaveChangesTV = (TextView) contentView.findViewById(R.id.mailingListPreferencesSaveChangesTV);
        personalDataNoticeLblTV = (TextView) contentView.findViewById(R.id.personalDataNoticeLblTV);
        personalDataNoticeAcceptedTV = (TextView) contentView.findViewById(R.id.personalDataNoticeAcceptedTV);

        personalInformationCurrentEmailTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentEmailTV);
        personalInformationCurrentPasswordTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentPasswordTV);
        personalInformationCurrentFirstNameTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentFirstNameTV);
        personalInformationCurrentSurNameTV = (TextView) contentView.findViewById(R.id.personalInformationCurrentSurNameTV);


        step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationChangeEmailLBl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentEmailLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationNewEmailLblET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationConfirmEmailLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        changeYourEmailSaveChangesTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationChangePasswordLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentPasswordLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationNewPasswordLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationConfirmPasswordLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        changeYourPasswordSaveChangesTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        changeYourNameLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentFirstNameLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationNewFirstNameLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentSurNameLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationNewSurNameLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        changeYourNameSaveChangesTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationMailingLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        mailingListPreferencesSaveChangesTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalDataNoticeLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalDataNoticeAcceptedTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentEmailTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentPasswordTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentFirstNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationCurrentSurNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        personalInformationCurrentEmailTV.setText(Email);
        personalInformationCurrentPasswordTV.setText("******");
        personalInformationCurrentFirstNameTV.setText(Name);
        personalInformationCurrentSurNameTV.setText(LastName);

        personalInformationNewEmailET = (EditText) contentView.findViewById(R.id.personalInformationNewEmailET);
        personalInformationConfirmEmailET = (EditText) contentView.findViewById(R.id.personalInformationConfirmEmailET);
        personalInformationNewPasswordET = (EditText) contentView.findViewById(R.id.personalInformationNewPasswordET);
        personalInformationConfirmPasswordET = (EditText) contentView.findViewById(R.id.personalInformationConfirmPasswordET);
        personalInformationNewFirstNameET = (EditText) contentView.findViewById(R.id.personalInformationNewFirstNameET);
        personalInformationNewSurNameET = (EditText) contentView.findViewById(R.id.personalInformationNewSurNameET);

        personalInformationNewEmailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationConfirmEmailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationNewPasswordET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationConfirmPasswordET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationNewFirstNameET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        personalInformationNewSurNameET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        personalInformationMailingSubscribedCB = (CheckBox) contentView.findViewById(R.id.personalInformationMailingSubscribedCB);
        personalInformationMailingSubscribedCB.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        changeYourEmailSaveChangesRL = (RelativeLayout) contentView.findViewById(R.id.changeYourEmailSaveChangesRL);
        changeYourEmailSaveChangesBtnIV = (ImageButton) contentView.findViewById(R.id.changeYourEmailSaveChangesBtnIV);
        changeYourPasswordSaveChangesBtnIV = (ImageButton) contentView.findViewById(R.id.changeYourPasswordSaveChangesBtnIV);
        changeYourNameSaveChangesBtnIV = (ImageButton) contentView.findViewById(R.id.changeYourNameSaveChangesBtnIV);
        mailingListPreferencesSaveChangesBtnIV = (ImageButton) contentView.findViewById(R.id.mailingListPreferencesSaveChangesBtnIV);

        if(Newsletter.equals("0"))
        {
            personalInformationMailingSubscribedCB.setChecked(false);
        }
        else
        {
            personalInformationMailingSubscribedCB.setChecked(true);
        }

        changeYourEmailSaveChangesBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean gotProblemsOccured = false;

                String newEmail = personalInformationNewEmailET.getText().toString();
                String confirmEmail = personalInformationConfirmEmailET.getText().toString();

                if(!newEmail.equals(confirmEmail)) {
                    gotProblemsOccured = true;
                    Toast.makeText(getActivity(),"Email does not match. Pleace check again.",Toast.LENGTH_LONG).show();
                }
                else
                if(newEmail.length() == 0 || confirmEmail.length() == 0)
                {
                    gotProblemsOccured = true;
                    Toast.makeText(getActivity(),"Please do not leave compulsory field empty.",Toast.LENGTH_LONG).show();
                }

                if(!gotProblemsOccured)
                {
                    UPDATED_TYPE = UPDATED_EMAIL;
                    confirmconfirmEmail = confirmEmail;
                    editPersonalInformationeEmail("","",confirmEmail,"","");
                }
            }
        });

        changeYourPasswordSaveChangesBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean gotProblemsOccured = false;

                String newPassword = personalInformationNewPasswordET.getText().toString();
                String confirmPassword = personalInformationConfirmPasswordET.getText().toString();



                if(!newPassword.equals(confirmPassword)) {
                    gotProblemsOccured = true;
                    Toast.makeText(getActivity(),"Password does not match. Pleace check again.",Toast.LENGTH_LONG).show();
                }

                if(newPassword.length() < 6 && newPassword.length() > 0 || confirmPassword.length() < 6 && confirmPassword.length() > 0) {
                    gotProblemsOccured = true;
                    Toast.makeText(getActivity(),"Password must be more than 5 characters. Pleace check again.",Toast.LENGTH_LONG).show();
                }

                if(newPassword.length() == 0 && confirmPassword.length() == 0)
                {
                    gotProblemsOccured = true;
                    Toast.makeText(getActivity(),"Please do not leave compulsory field empty.",Toast.LENGTH_LONG).show();
                }


                if(!gotProblemsOccured)
                {
                    UPDATED_TYPE = UPDATED_PASSWORD;
                    editPersonalPassword("","","",confirmPassword,"");
                }
            }
        });

        changeYourNameSaveChangesBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean gotProblemsOccured = false;

                String firstName = personalInformationNewFirstNameET.getText().toString();
                String lasttName = personalInformationNewSurNameET.getText().toString();




                if(firstName.length() == 0 && lasttName.length() == 0)
                {
                    gotProblemsOccured = true;
                    Toast.makeText(getActivity(),"Please do not leave compulsory field empty.",Toast.LENGTH_LONG).show();
                }
                if(firstName.length() == 0 && lasttName.length() != 0){
                    gotProblemsOccured = true;
                    UPDATED_TYPE = UPDATED_NAME;
                    confirmfirstName = Name;
                    confirmlasttName = lasttName;
                    editPersonalInformationName(Name,lasttName, "", "", "");
                }
                if(firstName.length() != 0 && lasttName.length() == 0){
                    gotProblemsOccured = true;
                    UPDATED_TYPE = UPDATED_NAME;
                    confirmfirstName = firstName;
                    confirmlasttName = LastName;
                    editPersonalInformationName(firstName,LastName,"","","");
                }
                if(!gotProblemsOccured)
                {
                    UPDATED_TYPE = UPDATED_NAME;
                    confirmfirstName = firstName;
                    confirmlasttName = lasttName;
                    editPersonalInformationName(firstName,lasttName,"","","");
                }
            }
        });

        mailingListPreferencesSaveChangesBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(personalInformationMailingSubscribedCB.isChecked())
                {
                    Newsletter = "1";
                }
                else
                {
                    Newsletter = "0";
                }

                UPDATED_TYPE = UPDATED_MAILINGLIST;
                editPersonalNewsletter("","","","",Newsletter);
            }
        });

        return contentView;
    }

    private void editPersonalInformationeEmail(String firstName, String lastName, String email, String password, String newsletter)
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Customers/editProfile";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_customer",UserID)
                .add("id_lang", LanguageID)
                .add("email", email)
                .add("optin", "0")
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }
    private void editPersonalInformationName(String firstName, String lastName, String email, String password, String newsletter)
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Customers/editProfile";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_customer",UserID)
                .add("id_lang", LanguageID)
                .add("firstname", firstName)
                .add("lastname", lastName)
                .add("optin", "0")
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }
    private void editPersonalPassword(String firstName, String lastName, String email, String password, String newsletter)
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Customers/editProfile";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_customer",UserID)
                .add("id_lang", LanguageID)
                .add("password", password)
                .add("optin", "0")
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }
    private void editPersonalNewsletter(String firstName, String lastName, String email, String password, String newsletter)
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Customers/editProfile";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_customer",UserID)
                .add("id_lang", LanguageID)
                .add("newsletter", newsletter)
                .add("optin", "0")
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                String Action = result.getString("action");
                if(Action.equals("Customers_editProfile"))
                {
                    if(result.getBoolean("status"))
                    {
                        Toast.makeText(getActivity(),"Successfully updated personal information",Toast.LENGTH_LONG).show();

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        if(UPDATED_TYPE == UPDATED_EMAIL)
                        {
                            InputMethodManager inputManager = (InputMethodManager)
                                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            personalInformationCurrentEmailTV.setText(confirmconfirmEmail);
                            editor.putString("Email", confirmconfirmEmail);
                            editor.commit();
                        }
                        else if(UPDATED_TYPE == UPDATED_NAME)
                        {
                            InputMethodManager inputManager = (InputMethodManager)
                                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            personalInformationCurrentFirstNameTV.setText(confirmfirstName);
                            personalInformationCurrentSurNameTV.setText(confirmlasttName);
                            editor.putString("Name", confirmfirstName);
                            editor.putString("LastName", confirmlasttName);
                            editor.commit();
                        }
                        else if(UPDATED_TYPE == UPDATED_PASSWORD)
                        {
                            InputMethodManager inputManager = (InputMethodManager)
                                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        else if(UPDATED_TYPE == UPDATED_MAILINGLIST)
                        {
                            InputMethodManager inputManager = (InputMethodManager)
                                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            editor.putString("Newsletter", Newsletter);
                            editor.commit();
                        }



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

}
