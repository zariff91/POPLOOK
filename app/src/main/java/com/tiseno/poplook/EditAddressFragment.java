package com.tiseno.poplook;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditAddressFragment extends Fragment {


    Button editAddress_countryButton,editAddress_stateButton,editAddressstateSelectButton,editAddress_dateButton;
    RelativeLayout editAddressstateNumberPickerLayout;
    ScrollView editAddressscrollviewBehind;
    NumberPicker editAddressstateNumberPicker;
    EditText editAddress_stateET;
    EditText editAddress_DOBET;
    String sort = "Kuala Lumpur";
    float dimBack= (float)0.1;

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
        View view = inflater.inflate(R.layout.fragment_edit_address, container, false);
        editAddress_countryButton = (Button)view.findViewById(R.id.editAddress_countryButton);
        editAddress_stateButton = (Button)view.findViewById(R.id.editAddress_stateButton);
        editAddressstateSelectButton = (Button)view.findViewById(R.id.editAddressstateSelectButton);
        editAddress_dateButton = (Button)view.findViewById(R.id.editAddress_dateButton);
        editAddressstateNumberPickerLayout = (RelativeLayout)view.findViewById(R.id.editAddressstateNumberPickerLayout);
        editAddressscrollviewBehind = (ScrollView)view.findViewById(R.id.editAddressscrollviewBehind);
        editAddressstateNumberPicker = (NumberPicker)view.findViewById(R.id.editAddressstateNumberPicker);
        editAddress_stateET = (EditText)view.findViewById(R.id.editAddress_stateET);
        editAddress_DOBET = (EditText)view.findViewById(R.id.editAddress_DOBET);
        editAddress_stateET.setEnabled(false);
        setDividerColor(editAddressstateNumberPicker, Color.LTGRAY);
        editAddress_countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), CountryPickerFragment.class);
                getActivity().startActivityForResult(i, 1);
                getActivity().overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
//                CountryPickerFragment countryPickerFragment= new CountryPickerFragment();
//                getActivity().getFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainer, countryPickerFragment)
////                                .addToBackStack(null)
//                        .commit();


            }

        });
        editAddress_stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editAddressstateNumberPickerLayout.setVisibility(View.VISIBLE);
                editAddressscrollviewBehind.setAlpha(dimBack);
                editAddressstateNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                editAddressstateNumberPicker.setMaxValue(3);
                editAddressstateNumberPicker.setDisplayedValues( new String[] { "Kuala Lumpur" , "Pahang" , "Selangor", "Johor" ,} );

                editAddressstateNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        // TODO Auto-generated method stub

                        String[] values = picker.getDisplayedValues();

                        sort = values[newVal];
                        editAddress_stateET.setText(sort);
                        editAddress_stateET.setTextColor(getResources().getColor(R.color.black));
                    }
                });
            }
        });

        editAddressstateSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editAddressstateNumberPickerLayout.setVisibility(View.GONE);
                editAddressscrollviewBehind.setAlpha(1);

            }

        });

        editAddress_dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;

    }
    private void updateLabel() {

        GregorianCalendar c = new GregorianCalendar(myCalendar
                .get(Calendar.YEAR),  myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        editAddress_DOBET.setText(sdf.format(c.getTime()));
        editAddress_DOBET.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));



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
}