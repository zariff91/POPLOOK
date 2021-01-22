package com.tiseno.poplook;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.useinsider.insider.Insider;
import com.useinsider.insider.InsiderIdentifiers;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    public Button loginBtn, login_signup_btn;
    EditText emailET,passwordET;
    TextView forgotPasswordTV,shippingTOTV;
    String email,pass;
    int langID = 1;
    String TokenID,CartID="";
    String Cart="",SelectedShopID="1",SelectedCountryName="Malaysia (RM)";
    String fromLoyalty;
    boolean lowerlolipop=false;


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

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        ((MainActivity) getActivity()).changeToolBarText("Log In");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        CartID = pref.getString("CartID", "");
        TokenID = pref.getString("TokenID", "");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryName = pref.getString("SelectedCountryName", "");

        fromLoyalty = pref.getString("fromLoyaltyPage","");

        //
        loginBtn=(Button)view.findViewById(R.id.login_submit_btn);
        loginBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        login_signup_btn = (Button)view.findViewById(R.id.login_signup_btn);
        login_signup_btn.setTypeface(FontUtil.getTypeface(getActivity(),FontUtil.FontType.AVENIR_ROMAN_FONT));
        forgotPasswordTV = (TextView) view.findViewById(R.id.forgot_passwordTV);
        forgotPasswordTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        passwordET = (EditText) view.findViewById(R.id.login_passwordET);
        passwordET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        emailET=(EditText) view.findViewById(R.id.login_userNameET);
        emailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        passwordET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        passwordET.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        shippingTOTV = (TextView) view.findViewById(R.id.shippingTOTV);
        shippingTOTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        shippingTOTV.setText(Html.fromHtml("Shipping To "+SelectedCountryName+ " <u>Change</u>"));

        shippingTOTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    lowerlolipop=true;
                }else{
                    lowerlolipop=false;
                }
                Intent i = new Intent(getActivity(), ChooseCountryActivity2.class);
                startActivityForResult(i, 1);
                getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);


            }

        });

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment fragment = new ForgotPasswordFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        login_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignUpFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean GotProblemOccured = false;

                email = emailET.getText().toString();
                pass = passwordET.getText().toString();

                if(email.length() == 0 && pass.length() == 0)
                {
                    GotProblemOccured = true;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please do not leave any field empty")
                            .setPositiveButton("OK", null)
                            .show();
                }
                if(email.length() == 0 && pass.length() != 0)
                { GotProblemOccured =true;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please enter email address")
                            .setPositiveButton("OK", null)
                            .show();
                }
                if(pass.length() == 0 && email.length() != 0)
                { GotProblemOccured =true;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please enter password")
                            .setPositiveButton("OK", null)
                            .show();
                }


                if(!GotProblemOccured)
                {
                    userLogin(email, pass);
                }

            }

        });
        return view;
    }


    private void userLogin(String email, String pass){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String passwordMD5=md5(pass);
        String action="UserAuth/login?apikey="+apikey+"&id_cart="+CartID+"&shop="+SelectedShopID+"&token="+TokenID+"&password="+passwordMD5+"&email="+email+"&id_lang=1&device_type=Android";
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("Get JSON Response : " + result);

        if(result!=null){

            try {
                if (result.getBoolean("status"))
                {
                    System.out.println("hahaaaaaaab");
                    if (result.getString("action").equals("UserAuth_login"))
                    {
                        JSONObject data=result.getJSONObject("data");
                        String id_customer = data.getString("id_customer");
                        String id_cart = data.getString("id_cart");
                        String name = data.getString("name");
                        String lastname = data.getString("lastname");
                        String email = data.getString("email");
                        String newsletter = data.getString("newsletter");
                        String id_wishlist = data.getString("id_wishlist");
                        String totalItemInCart = data.getString("totalItemInCart");
                        String loyalty = data.getString("loyalty");
                        String id_entity = data.getString("id_entity");


                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        InsiderIdentifiers identifiers = new InsiderIdentifiers();
                        identifiers.addEmail(email);
                        identifiers.addUserID(id_customer);
                        Insider.Instance.getCurrentUser().login(identifiers);
                        Insider.Instance.getCurrentUser().setName(name);
                        Insider.Instance.getCurrentUser().setCustomAttributeWithString("name",name);
                        Insider.Instance.getCurrentUser().setCustomAttributeWithString("surname",lastname);
                        Insider.Instance.getCurrentUser().setSurname(lastname);


                        if(data.isNull("popup_tier"))
                        {

                            editor.putString("popup_show", "false");

                        }
                        else
                        {
                            JSONObject popupTierObj = data.getJSONObject("popup_tier");

                            String enddate = data.getString("popup_tier_enddate");
                            editor.putString("levelup_enddate", enddate);

                            String popup_id = popupTierObj.getString("id");
                            String popup_tier = popupTierObj.getString("popped");

                            System.out.println("dapatpopup " + popup_tier);



                            if(popupTierObj.isNull("popped"))
                            {

                                editor.putString("popup_show", "true");

                                String getTier = String.valueOf(popupTierObj.get("tier"));
                                editor.putString("popup_tier", getTier);


                            }

                            else {

                                editor.putString("popup_show", "false");

                            }


                            editor.putString("popup_id", popup_id);
//                            editor.putString("popup_tier", popup_tier);

                        }

                        System.out.println("TOTAL ITEM IN CART LOGIN "+ totalItemInCart);

                        editor.putString("UserID", id_customer);
                        editor.putString("CartID", id_cart);
                        editor.putString("LanguageID", "1");
                        editor.putString("Name", name);
                        editor.putString("LastName", lastname);
                        editor.putString("Email", email);
                        editor.putString("Newsletter", newsletter);
                        editor.putString("WishlistID", id_wishlist);
//                        editor.putString("SelectedShopID", id_shop);
                        editor.putString("cartItem", totalItemInCart);
                        editor.putString("loyalty_id", loyalty);
                        editor.putString("entity_id", id_entity);

                        editor.apply();

                        Toast toast = Toast.makeText(getActivity(),
                                "Login Successfully", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

                        Insider.Instance.tagEvent("login").build();

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

//                        ((MainActivity)getActivity()).getSideMenuList();

                        System.out.println("id customeeerrrr :"+id_customer);
                        System.out.println("id caaaarrrttttt :"+id_cart);

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
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryName = pref.getString("SelectedCountryName", "");
        shippingTOTV.setText(Html.fromHtml("Shipping To "+SelectedCountryName+ " <font color=#70BF00><u>Change</u></font>"));
        ((MainActivity) getActivity()).getSideMenuList();

    }

    @Override
    public void onDetach() {
        super.onDetach();

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fromLoyaltyPage", "0");
        editor.apply();

    }
}