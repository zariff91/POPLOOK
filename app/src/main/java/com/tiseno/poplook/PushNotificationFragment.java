package com.tiseno.poplook;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPost;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class PushNotificationFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    RadioGroup radioGroup;
    RadioButton radioButton,radioButton1,radioButton2;
    Button btnSavePush;
    TextView pushNotiTV;
    int enable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_push_notification, container, false);
        ((MainActivity) getActivity()).changeToolBarText("Push Notification");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        pushNotiTV = (TextView) view.findViewById(R.id.pushNotiTV);
        radioGroup = (RadioGroup)view.findViewById(R.id.pushNoti_radio);
        radioButton1 = (RadioButton)view.findViewById(R.id.pushNoti_radioYES);
        radioButton2 = (RadioButton)view.findViewById(R.id.pushNoti_radioNO);
        btnSavePush = (Button) view.findViewById(R.id.pushNotiSaveBtn);

        pushNotiTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        radioButton1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        radioButton2.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        btnSavePush.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        btnSavePush.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) view.findViewById(selectedId);
                radioButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                System.out.println("heyyy " + radioButton.getText());

                if (radioButton.getText().equals("YES")) {
                    enable = 1;
                    enablePushNotification();
                } else {
                    enable = 0;
                    enablePushNotification();
                }

            }

        });

        getPushNotificationSetting();

        return view;
    }

    private void getPushNotificationSetting()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String TokenID = pref.getString("TokenID", "");

        String action="PushNotification/info?token_id="+TokenID+"&apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    private void enablePushNotification(){

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String TokenID = pref.getString("TokenID", "");

        String action="index.php/PushNotification/enable";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("token_id",TokenID)
                .add("enable_push_notification", String.valueOf(enable))
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
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
                    if (result.getString("action").equals("PushNotification_enable"))
                    {
                        Toast toast = Toast.makeText(getActivity(),
                                "Push Notification settings updated", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

                    }
                    else if (result.getString("action").equals("PushNotification_info"))
                    {
                        JSONObject data= result.getJSONObject("data");
                        String enabled = data.getString("enable");

                        if(enabled.equals("1"))
                        {
                            enable = 1;
                            radioButton1.setChecked(true);
                            radioButton2.setChecked(false);
                        }
                        else if(enabled.equals("0"))
                        {
                            enable = 0;
                            radioButton1.setChecked(false);
                            radioButton2.setChecked(true);
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
