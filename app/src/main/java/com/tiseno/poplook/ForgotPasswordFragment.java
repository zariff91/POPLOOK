package com.tiseno.poplook;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswordFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject>{

    static FrameLayout llShowView;
    Button sendPassword;
    EditText destinationAddress;
    TextView forgotPass_detailsTV;
    String SelectedShopID="1";
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).changeToolBarText("Forget Password");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        View view =  inflater.inflate(R.layout.fragment_forgot_password, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        //
        llShowView=(FrameLayout) view.findViewById(R.id.lltry) ;
        sendPassword=(Button)view.findViewById(R.id.forgotPass_continue_btn);
        sendPassword.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        destinationAddress=(EditText)view.findViewById(R.id.forgotPass_emailET);
        destinationAddress.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        forgotPass_detailsTV=(TextView)view.findViewById(R.id.forgotPass_detailsTV);
        forgotPass_detailsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(destinationAddress.length() == 0)
                {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please enter email address")
                            .setPositiveButton("OK", null)
                            .show();
                }
                else
                {
                    callForgottenPasswordWS(destinationAddress.getText().toString());
                }
            }

        });

        return view;
    }

    private void callForgottenPasswordWS(String email)
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="UserAuth/forgetPassword?apikey="+apikey+"&email="+email+"&shop="+SelectedShopID+"";
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){
            try{

                if(result.getBoolean("status"))
                {
                    String action = result.getString("action");
                    if(action.equals("UserAuth_forgetPassword"))
                    {
                        String emailAdd=destinationAddress.getText().toString();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Message")
                                .setMessage("An email to "+emailAdd+" with a link to reset you password has been sent")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                   getActivity().getFragmentManager().popBackStack();

                                    }

                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                }else{
                    String message=result.getString("message");
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage(message)
                            .setNegativeButton("OK", null)
//                            .setNegativeButton("Sign Up", new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    Fragment fragment = new SignUpFragment();
//                                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                                    FragmentTransaction ft = fragmentManager.beginTransaction();
//                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                    ft.replace(R.id.fragmentContainer, fragment);
//
//                                    ft.commit();
//
//                                }
//
//                            })
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
}


