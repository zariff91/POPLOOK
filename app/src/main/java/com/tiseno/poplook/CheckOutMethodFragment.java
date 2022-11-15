package com.tiseno.poplook;


import android.app.Activity;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CheckOutMethodFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{
    EditText checkOut_emailET,checkOut_passET;
    TextView checkOut_signUpTV,checkOut_new,checkOut_registeredTV,checkOut_forgotPassTV;
    CheckBox checkOut_GuestCheckBox,checkOut_SignUpCheckBox;
    Button  btn_cont_checkout,btn_login_checkout;
//    RadioGroup radioGroup;
    RadioButton radioButton,radioButtonSignUp,radioButtonInstant;
    String radioChoose;
    JSONObject cartResultJObj1;
    String LanguageID, CartID, TokenID,SelectedShopID="1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_check_out_method, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Check Out");
        ((MainActivity) getActivity()).changeToolBarTextView(false);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(true);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        LanguageID = pref.getString("LanguageID", "");
        CartID = pref.getString("CartID", "");
        TokenID = pref.getString("TokenID", "");
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "");
        //
        checkOut_emailET = (EditText)view.findViewById(R.id.checkOut_emailET);
        checkOut_passET = (EditText)view.findViewById(R.id.checkOut_passET);

        checkOut_signUpTV = (TextView)view.findViewById(R.id.checkOut_signUpTV);
        checkOut_new = (TextView)view.findViewById(R.id.checkOut_new);
        checkOut_registeredTV = (TextView)view.findViewById(R.id.checkOut_registeredTV);
        checkOut_forgotPassTV = (TextView)view.findViewById(R.id.checkOut_forgotPassTV);

//        radioGroup = (RadioGroup)view.findViewById(R.id.checkOut_Radio);
//        radioButtonSignUp = (RadioButton)view.findViewById(R.id.checkOut_SignUpCheckBox);
//        radioButtonInstant = (RadioButton)view.findViewById(R.id.checkOut_GuestCheckBox);


        btn_cont_checkout = (Button)view.findViewById(R.id.btn_cont_checkout);
        btn_login_checkout = (Button)view.findViewById(R.id.btn_login_checkout);

//        radioButtonSignUp.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        radioButtonInstant.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        checkOut_emailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        checkOut_passET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        checkOut_signUpTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        checkOut_new.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        checkOut_registeredTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        checkOut_forgotPassTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        btn_cont_checkout.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        btn_login_checkout.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));



        btn_cont_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // get selected radio button from radioGroup
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//
//                // find the radiobutton by returned id
//                radioButton = (RadioButton) view.findViewById(selectedId);
//
//                radioButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//                radioChoose = radioButton.getText().toString();
//                System.out.println("SADASDSADASDASD" + selectedId);
//
//                if (selectedId == R.id.checkOut_SignUpCheckBox) {

                    Fragment fragment = new SignUpFragment();
                    Bundle args = new Bundle();

                    args.putString("fromGuestCheckOut","1");
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

//                } else {
//                    Fragment fragment = new InstantCheckOutFragment();
//
//                    Bundle args = new Bundle();
//
//                    args.putString("cartResultJObj", getArguments().getString("cartResultJObj"));
//                    fragment.setArguments(args);
//
//                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                }


            }

        });

        checkOut_forgotPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment fragment = new ForgotPasswordFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btn_login_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(checkOut_emailET.getText().toString(), checkOut_passET.getText().toString());
            }
        });


        return view;
    }

    private void userLogin(String email, String pass){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String passwordMD5=md5(pass);
        String action="UserAuth/login?apikey="+apikey+"&id_cart="+CartID+"&shop="+SelectedShopID+"&token="+TokenID+"&password="+passwordMD5+"&email="+email+"&id_lang=1&device_type=Android";
        WebServiceAccessGet callws = new WebServiceAccessGet
                (getActivity(), this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        if(result!=null){

            try {
                if (result.getBoolean("status"))
                {
                    if (result.getString("action").equals("UserAuth_login"))
                    {

                        JSONObject data=result.getJSONObject("data");
                        String id_customer = data.getString("id_customer");
                        String id_cart = data.getString("id_cart");
                        String name = data.getString("name");
                        String lastname = data.getString("lastname");
                        String email = data.getString("email");
                        String newsletter = data.getString("newsletter");
                        String id_shop = data.getString("id_shop");


                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("UserID", id_customer);
                        editor.putString("CartID", id_cart);
                        editor.putString("LanguageID", "1");
                        editor.putString("Name", name);
                        editor.putString("LastName", lastname);
                        editor.putString("Email", email);
                        editor.putString("Newsletter", newsletter);
                        editor.putString("NeedToGoBackToCart", "1");
                        editor.apply();
                        Toast toast = Toast.makeText(getActivity(),
                                "Login Successfully", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

//                        Insider.Instance.tagEvent(getActivity(),"login");
                        Insider.Instance.tagEvent("login").build();

                        ((MainActivity) getActivity()).getSideMenuList();
                        try{
                            InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);}catch (Exception e){}

                        //multishop
                        String apikey =pref.getString("apikey","");
                        String giftMessage  = pref.getString("giftMessage", "");
                        String action="Carts/OrderStep1?apikey="+apikey+"&id_cart="+CartID+"&gift_message="+giftMessage;

                        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

                        callws.execute(action);


                        System.out.println("id customeeerrrr :"+id_customer);
                        System.out.println("id caaaarrrttttt :"+id_cart);

                    }
                    else if(result.getString("action").equals("Carts_OrderStep1")) {
                        if(result.getBoolean("status"))
                        {
                            cartResultJObj1 = result;
                            JSONObject data = new JSONObject();
                            data=result.getJSONObject("data");



                            String nextPage = data.getString("next_page");

                            if(nextPage.equals("addressPage"))
                            {
                                Fragment fragment = new NewOrderConfirmationFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment,"NewOrderConfirmationFragment");
                                fragmentTransaction.addToBackStack(null);

                                Bundle args=new Bundle();
                                String cartResultJObjString=cartResultJObj1.toString();
                                System.out.println("cartResultObj" + cartResultJObjString);
                                args.putString("cartResultJObj", cartResultJObjString);
                                fragment.setArguments(args);

                                fragmentTransaction.commit();
                                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("NeedToGoBackToCart", "0");
                                editor.apply();
                            }
                            else if(nextPage.equals("addAddressPage"))
                            {
                                Fragment fragment = new AddAdressFragment();
                                Bundle bundle = new Bundle();
                                String cartResultJObjString=cartResultJObj1.toString();
                                bundle.putString("cartResultJObj", cartResultJObjString);
                                bundle.putInt("COME_FROM_WHERE", 3);
                                bundle.putBoolean("EDIT_ADDRESS", false);
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                            else if(nextPage.equals("cart"))
                            {
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
                    else
                    {
                        String message = result.getString("message");

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Message")
                                .setMessage(message)
                                .setPositiveButton("OK", null)
                                .show();
                    }



                }else{
                    String message = result.getString("message");

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage(message)
                            .setPositiveButton("OK", null)
                            .show();
                }

            }
            catch (Exception e){

            }

        }
        else{
            try {
                String message = result.getString("message");

                new AlertDialog.Builder(getActivity())
                        .setTitle("Message")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }catch (Exception e){}
        }
    }
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
