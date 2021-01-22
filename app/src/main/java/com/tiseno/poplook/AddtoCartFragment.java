package com.tiseno.poplook;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tiseno.poplook.functions.FontUtil;

import java.util.ArrayList;
import java.util.List;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;



public class AddtoCartFragment extends BottomSheetDialogFragment {


    public interface addToCartMethod{

        void sendInput(String data, String data2,  String data3);
        void sendInputSaveItem(String data, String data2, String data3);

    }

    public addToCartMethod addingMethod;

    TextView sizeTextView, quantityText, selectAmount;

    Button addToCartBtn, addQuantityBtn,minusQuantityBtn;

    String productID,CategoryName, CartID,LanguageID,SelectedShopID, SelectedQuantity, SelectedAttID, UserID, getPlatform;

    List<String> attributeIDList = new ArrayList<String>();

    ArrayList<String> sizze = new ArrayList<String>();
    ArrayList<String> listQua = new ArrayList<String>();

    String[] sizeAvailableArray;


    int minteger = 1;
    int quantityValue = 1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();
        productID = bundle.getString("prodID");
        CategoryName = bundle.getString("catName");
        sizze = bundle.getStringArrayList("sizes");
        listQua = bundle.getStringArrayList("listQuanity");
        attributeIDList = bundle.getStringArrayList("attID");
        getPlatform = bundle.getString("forWishlist");

        View v = inflater.inflate(R.layout.add_to_cart, container, false);

        sizeTextView = (TextView)v.findViewById(R.id.sizeText);
        quantityText = (TextView)v.findViewById(R.id.quantityText);
        addToCartBtn = (Button)v.findViewById(R.id.addToCartButton);
        addQuantityBtn = (Button)v.findViewById(R.id.addBtn);
        minusQuantityBtn = (Button)v.findViewById(R.id.minusBtn);
        selectAmount = (TextView)v.findViewById(R.id.selectedAmount);

        sizeTextView.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        quantityText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        addToCartBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        selectAmount.setText("" + quantityValue);

        System.out.println("size List" + sizze);
        System.out.println("quantity List" + listQua);

        if(sizze.size() == 0)
        {
            SelectedAttID = "";

            addQuantityBtn.setEnabled(true);
            minusQuantityBtn.setEnabled(true);

            String quantity = listQua.get(0);

            int convertedVal = Integer.parseInt(quantity);
            System.out.println("getSelectedIndex " + convertedVal);
            quantityValue = convertedVal;

        }

        else {

            addQuantityBtn.setEnabled(false);
            minusQuantityBtn.setEnabled(false);

        }


        if(getPlatform.equals("1"))
        {

            addToCartBtn.setText("Add To Wishlist");

        }
        else {

            addToCartBtn.setText("Add To Cart");


        }


        if(sizze.size() == 0)
        {

        }

        else {

            ChipCloud chipCloud = (ChipCloud) v.findViewById(R.id.chipSizeCloud);
            chipCloud.setMode(ChipCloud.Mode.SINGLE);

            sizeAvailableArray = new String[sizze.size() - 1];

            for (int i = 1; i <sizze.size(); i++)
            {

                String sizes;

                sizes = sizze.get(i);

                sizeAvailableArray[i-1] = sizes;

            }

            new ChipCloud.Configure()
                    .chipCloud(chipCloud)
                    .selectedColor(Color.parseColor("#000000"))
                    .selectedFontColor(Color.parseColor("#ffffff"))
                    .deselectedColor(Color.parseColor("#e1e1e1"))
                    .deselectedFontColor(Color.parseColor("#333333"))
                    .labels(sizeAvailableArray)
                    .gravity(ChipCloud.Gravity.LEFT)
                    .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                    .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                    .chipListener(new ChipListener() {
                        @Override
                        public void chipSelected(int index) {


                            String quantity = listQua.get(index);

                            int convertedVal = Integer.parseInt(quantity);

                            System.out.println("getSelectedIndex " + convertedVal);

                            SelectedAttID = attributeIDList.get(index);

                            System.out.println("getSelectedIndexSize " + SelectedAttID);

                            System.out.println("get selected size " + sizze.get(index+1));

                            if(sizze.get(index+1).contains("Sold Out"))
                            {

                                Toast toast = Toast.makeText(getActivity(),
                                        "Product is sold out", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 50);
                                toast.show();

                            }

                            else {

                                if (getPlatform.equals("1")) {

                                    addingMethod.sendInputSaveItem(SelectedAttID, "1", productID);

                                } else {

                                    addingMethod.sendInput(SelectedAttID, "1", productID);

                                }

                                dismiss();

//                            quantityValue = convertedVal;
//
//                            minteger = 1;
//                            selectAmount.setText("" + minteger);
//
//                            if(quantityValue == 0)
//                            {
//
//                                addQuantityBtn.setEnabled(false);
//                                minusQuantityBtn.setEnabled(false);
//
//                            }
//
//                            else
//                            {
//
//                                if(convertedVal < 5)
//                                {
//
//                                    Toast toast = Toast.makeText(getActivity(),
//                                            "Only " + quantityValue +" items left", Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.BOTTOM, 0, 25);
//                                    toast.show();
//
//                                }
//
//                                minteger = 1;
//                                selectAmount.setText("" + minteger);
//
//
//                                addQuantityBtn.setEnabled(true);
//                                minusQuantityBtn.setEnabled(true);
//
//                            }
                            }

                        }
                        @Override
                        public void chipDeselected(int index) {

                            SelectedAttID = null;
                            minteger = 1;
                            selectAmount.setText("" + minteger);
                            //...
                        }
                    })
                    .build();



        }

        addQuantityBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                minteger = minteger + 1;

                display(minteger);

                }

        });

        minusQuantityBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                minteger = minteger - 1;
                display(minteger);

            }

        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getPlatform.equals("1"))
                {

                    System.out.println("get selected attribute " + SelectedAttID);
                    System.out.println("get selected quantity " + SelectedQuantity);
                    System.out.println("get selected product ID " + productID);


                    SelectedQuantity = String.valueOf(1);
                    addingMethod.sendInputSaveItem(SelectedAttID, SelectedQuantity, productID);
                    SelectedQuantity = String.valueOf(1);

                    dismiss();
                }
                else {

                        if(quantityValue == 0)
                        {


                        }

                        else
                        {

                            if(SelectedAttID != null) {



                                SelectedQuantity = String.valueOf(minteger);
                                addingMethod.sendInput(SelectedAttID, SelectedQuantity , productID);


                            dismiss();
                            }

                            else {

                                Toast toast = Toast.makeText(getActivity(),
                                        "Please select size", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 50);
                                toast.show();


                            }
                        }
                    }
            }
        });


        return v;
    }

    private void display(int number) {

        if(number < 1)

        {

            minteger = 1;

        }

        else if (number < quantityValue)
        {

            minteger = number;

            if (number > 5)
            {

                minteger = 5;

            }

        }

        else {

            minteger = quantityValue;

        }


        selectAmount.setText("" + minteger);

    }



    @Override

    public void onAttach(Context context)
    {

        super.onAttach(context);

        try {

            addingMethod = (addToCartMethod)context;
        }

        catch (ClassCastException e){

            throw new ClassCastException(context.toString() + "must implement");

        }

    }



}
