package com.tiseno.poplook;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessPost;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class MyPreferenceFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {
        Button currencySaveBtn,currencySpinnerBtn,languageSpinnerBtn,preferenceSelectButton,preferenceSelectButton2;
        EditText languageSpinner,currencySpinner;
        TextView languageTV,currencyTV;
        NumberPicker preferenceNumberPicker,preferenceNumberPicker2;
        RelativeLayout preferenceNumberPickerLayout,preferenceNumberPickerLayout2;
    LinearLayout linearlayoutPreferenceBehind;
    String [] currency=new String[] {"Malaysian Ringgit (MYR)","United States Dollar (USD)","Singapore Dollar (SGD)"};
    String [] language=new String[] {"English","Bahasa Malaysia"};
    String sortlanguage = "English";
    String sortCurrency = "Malaysian Ringgit (MYR)";
    String UserID="",CartID="0";
    float dimBack= (float)0.1;
    int lang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_preference, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "0");
        ((MainActivity) getActivity()).changeToolBarText("My Preferences");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        currencySaveBtn=(Button)view.findViewById(R.id.currencySaveBtn);
        currencySpinnerBtn=(Button)view.findViewById(R.id.currencySpinnerBtn);
        languageSpinnerBtn=(Button)view.findViewById(R.id.languageSpinnerBtn);
        preferenceNumberPicker = (NumberPicker)view.findViewById(R.id.preferenceNumberPicker);
        preferenceNumberPickerLayout = (RelativeLayout)view.findViewById(R.id.preferenceNumberPickerLayout);
        preferenceNumberPicker2 = (NumberPicker)view.findViewById(R.id.preferenceNumberPicker2);
        preferenceNumberPickerLayout2 = (RelativeLayout)view.findViewById(R.id.preferenceNumberPickerLayout2);
        languageSpinner=(EditText)view.findViewById(R.id.languageSpinner);
        currencySpinner=(EditText)view.findViewById(R.id.currencySpinner);
        linearlayoutPreferenceBehind = (LinearLayout)view.findViewById(R.id.linearlayoutPreferenceBehind);
        preferenceSelectButton=(Button)view.findViewById(R.id.preferenceSelectButton);
        preferenceSelectButton2=(Button)view.findViewById(R.id.preferenceSelectButton2);
        languageTV = (TextView)view.findViewById(R.id.preferenceLangTV);
        currencyTV = (TextView)view.findViewById(R.id.preferenceCurrTV);
        setDividerColor(preferenceNumberPicker, Color.LTGRAY);


        currencySaveBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        languageSpinner.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        currencySpinner.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        preferenceSelectButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        preferenceSelectButton2.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        currencySpinner.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        currencyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        languageTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        currencySpinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceNumberPicker.setMaxValue(currency.length-1);
                preferenceNumberPickerLayout.setVisibility(View.VISIBLE);
                linearlayoutPreferenceBehind.setAlpha(dimBack);
                preferenceNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


                preferenceNumberPicker.setDisplayedValues( currency );

                preferenceNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        // TODO Auto-generated method stub

                        String[] values = picker.getDisplayedValues();

                        sortCurrency = values[newVal];
                        currencySpinner.setText(sortCurrency);
                    }
                });
            }
        });


        languageSpinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceNumberPicker2.setMaxValue(language.length-1);
                preferenceNumberPickerLayout2.setVisibility(View.VISIBLE);
                linearlayoutPreferenceBehind.setAlpha(dimBack);
                preferenceNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                preferenceNumberPicker2.setDisplayedValues( language );

                preferenceNumberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        // TODO Auto-generated method stub

                        String[] values = picker.getDisplayedValues();

                        sortlanguage = values[newVal];
                        languageSpinner.setText(sortlanguage);
                        if (sortlanguage.equals("Bahasa Malaysia"))
                        {
                            lang=0;
                        }else
                        {
                            lang=1;
                        }
                    }
                });
            }
        });
        preferenceSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceNumberPicker.setMaxValue(0);
                preferenceNumberPickerLayout.setVisibility(View.GONE);
                linearlayoutPreferenceBehind.setAlpha(1);

            }

        });

        preferenceSelectButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceNumberPicker2.setMaxValue(0);
                preferenceNumberPickerLayout2.setVisibility(View.GONE);
                linearlayoutPreferenceBehind.setAlpha(1);

            }

        });

        currencySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserPreferences();

            }

        });


        return view;

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


    private void setUserPreferences(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Preferences/setting";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_lang", String.valueOf(lang))
                .add("id_customer", UserID)
                .add("id_cart", CartID)
                .build();
        WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);


    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){

            try {
                if (result.getString("result").equals("1"))
                {
                    System.out.println("hahaaaaaaab");
                    if (result.getString("action").equals("setUserPreferences"))
                    {
                        String message = result.getString("0");

                        Toast toast = Toast.makeText(getActivity(),
                                message, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();

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
