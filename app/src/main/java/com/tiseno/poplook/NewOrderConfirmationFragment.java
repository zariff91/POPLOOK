package com.tiseno.poplook;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atome.sdk.AtomeSDK;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.GiftWrapAdapter;
import com.tiseno.poplook.functions.SimpleDividerItemDecoration;
import com.tiseno.poplook.functions.addressItem;
import com.tiseno.poplook.functions.orderConfirmationBagAdapter;
import com.tiseno.poplook.functions.shoppingBagItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPost;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NewOrderConfirmationFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    protected RecyclerView bagRV,giftWrapRV;
    protected RecyclerView.LayoutManager rvLayoutManager,giftRVLayoutManager;
    protected RecyclerView.Adapter adapter,giftAdapter;

    boolean fromAtome = false;

    private String m_Text = "";
    String appVersion="1.0.0";

    ProgressDialog progressDialog ;

    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<>();
    ArrayList<shoppingBagItem> listArray_giftWrap = new ArrayList<>();
    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();
    ArrayList<voucherItem> listArray_storeCredit = new ArrayList<voucherItem>();

    String addressObj;
    String forBilling;
    String carrierID_forAPI = "";
    String UserID, CartID, OrderID,OrderIDWithTimeStamp;
    String SelectedShopID;
    String couponCodeForInsider;

    String[] onlineBankingArray;
    String[] onlineBankingArrayValue;

    String[] EWalletArray;
    String[] EWalletArrayValue;

    EditText voucherET,giftET,leaveET;

    RadioGroup paymentRG;
    RadioButton ccButton,eWalletBtn,onlineBankingBtn,enetsorpaypal,atomeBtn;
    ;

    TextView saleTop,addressName,addressLbl,addressPhone,addressTitleLabel,shippingLabel,shippingMethod,expectedShipDate,paymentLabel, applyVoucherLbl,addedVoucherLbl,creditLbl,totalCreditLbl,giftOptionTitle,giftOptionText,shoppingLbl,totalItemInCart,retailPriceTxt,totalRetailTxt,subtotalTxt,totalSubTotalTxt,shippingFeeTxt,shippingFeeTotalTxt,storeCreditBottomTitle,storeCreditTotalBottom,totalBottomText,totalPriceBottomTxt,placeOrderTxt,leaveMessageText;
    TextView eWalletTxt,onlineBankTxt,addVoucher;
    TextView appliedVoucherTitle,termsTV,privacyTV,andTV,belowTermsTV;

    ImageView addStoreCredit,addressBtn,messageBtn,tickIconShippimg,removeAppliedVoucher,removeCreditText;
    ImageView paymentTermsCheckIV;
    boolean TermCondChecked = false;
    boolean containGift = false;
    String paymentFromGift;

    String totalPrice;



    RelativeLayout addressView,addedVoucherRL,storeCreditRL,giftView,messageView,giftETView,leaveETView;

    addressItem deliveryAddressObj;

    JSONObject cartResultJObj,cartResultJObj1;

    String skipTimer="0",extra_cart="0";

    int selectedEWallet;
    String eWalletType = "";
    String eWalletID = "";

    int selectedBanking;
    String PaymentIDForIPay88 = "";

    int PAYMENT_METHOD;
    final int CREDIT_CARD_PAYMENT = 0;
    final int ONLINE_BANKING_PAYMENT = 1;
    final int PAYPAL_PAYMENT = 2;
    final int ENETS_PAYMENT = 3;
    final int TNG_EWALLET = 4;
    final int ATOME_PAYMENT = 5;


    int SelectedAddress = 0;
    String voucherCode2;
    String storeCreditCode;

    String voucherCodeName;
    String storeCreditCodeName;

    String voucherTotal  = "0.00";
    String storeCreditAmount = "0.00";

    boolean openGift = false;
    boolean openLeave = false;

    ArrayList<addressItem> listArray_address_new = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "");
        SelectedShopID  = pref.getString("SelectedShopID", "1");

        ((MainActivity) getActivity()).changeToolBarText("Order Confirmation");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(false);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).showBottomBar(false);
        View contentView = inflater.inflate(R.layout.new_order_layout, container, false);

        saleTop = (TextView) contentView.findViewById(R.id.topSaleLabel);
        saleTop.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        addressName = (TextView) contentView.findViewById(R.id.address_name);
        addressName.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        addressLbl = (TextView) contentView.findViewById(R.id.address_text);
        addressLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        addressPhone = (TextView) contentView.findViewById(R.id.address_phoneNum);
        addressPhone.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        addressTitleLabel = (TextView) contentView.findViewById(R.id.addressLabel);
        addressTitleLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        shippingLabel = (TextView) contentView.findViewById(R.id.shippingLabel);
        shippingLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        shippingMethod = (TextView) contentView.findViewById(R.id.shippingMthd);
        shippingMethod.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        expectedShipDate = (TextView) contentView.findViewById(R.id.expectedOrderTV);
        expectedShipDate.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        paymentLabel = (TextView) contentView.findViewById(R.id.paymentLabel);
        paymentLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        applyVoucherLbl = (TextView) contentView.findViewById(R.id.applyVoucherLabel);
        applyVoucherLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        appliedVoucherTitle = (TextView) contentView.findViewById(R.id.voucherAppliedTitle);
        appliedVoucherTitle.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        removeAppliedVoucher = (ImageView) contentView.findViewById(R.id.removeAppliedVoucher);
        addedVoucherLbl = (TextView) contentView.findViewById(R.id.addedVooucherLabel);
        addedVoucherLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        creditLbl = (TextView) contentView.findViewById(R.id.storeCreditLabel);
        creditLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        totalCreditLbl = (TextView) contentView.findViewById(R.id.totalStoreCreditLabel);
        totalCreditLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        removeCreditText = (ImageView) contentView.findViewById(R.id.removeStoreCredit);
        giftOptionTitle = (TextView) contentView.findViewById(R.id.giftOptionTitle);
        giftOptionTitle.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        giftOptionText = (TextView) contentView.findViewById(R.id.giftOptionText);
        giftOptionText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        shoppingLbl = (TextView) contentView.findViewById(R.id.cartLabel);
        shoppingLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalItemInCart = (TextView) contentView.findViewById(R.id.totalItemCartTV);
        totalItemInCart.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        retailPriceTxt = (TextView) contentView.findViewById(R.id.retailTextTV);
        retailPriceTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        totalRetailTxt = (TextView) contentView.findViewById(R.id.retailTotalTV);
        totalRetailTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        subtotalTxt = (TextView) contentView.findViewById(R.id.subTotalTextTV);
        subtotalTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        totalSubTotalTxt = (TextView) contentView.findViewById(R.id.subTotalTV);
        totalSubTotalTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        storeCreditBottomTitle = (TextView) contentView.findViewById(R.id.storeCreditBottom);
        storeCreditBottomTitle.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        storeCreditTotalBottom = (TextView) contentView.findViewById(R.id.storeCreditBottomTotalTV);
        storeCreditTotalBottom.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        shippingFeeTxt = (TextView) contentView.findViewById(R.id.shipFeeTextTV);
        shippingFeeTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        shippingFeeTotalTxt = (TextView) contentView.findViewById(R.id.shipFeeTotalTV);
        shippingFeeTotalTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        totalBottomText = (TextView) contentView.findViewById(R.id.totalBottomViewText);
        totalBottomText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        totalPriceBottomTxt = (TextView) contentView.findViewById(R.id.totalBottomViewPrice);
        totalPriceBottomTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        placeOrderTxt = (TextView) contentView.findViewById(R.id.placeOrderTV);
        placeOrderTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        addVoucher = (TextView) contentView.findViewById(R.id.addVoucher);
        addVoucher.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        leaveMessageText = (TextView) contentView.findViewById(R.id.leaveMessageTV);
        leaveMessageText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        termsTV = (TextView) contentView.findViewById(R.id.termsServiceNew);
        termsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        privacyTV = (TextView) contentView.findViewById(R.id.privacyPolicyNew);
        privacyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

        andTV = (TextView) contentView.findViewById(R.id.andTVNew);
        andTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        belowTermsTV = (TextView) contentView.findViewById(R.id.textAfterNew);
        belowTermsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        SpannableString content = new SpannableString("Terms Of Service");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        termsTV.setText(content);

        SpannableString content2 = new SpannableString("Privacy Policy");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        privacyTV.setText(content2);

        paymentTermsCheckIV = (ImageView)contentView.findViewById(R.id.paymentTSCheckIVNew);
        paymentTermsCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TermCondChecked)
                {
                    paymentTermsCheckIV.setImageResource(R.drawable.btn_check_active);
                    TermCondChecked = true;
                }
                else
                {
                    paymentTermsCheckIV.setImageResource(R.drawable.btn_check);
                    TermCondChecked = false;
                }
            }
        });

        addressView = (RelativeLayout)contentView.findViewById(R.id.addressRV);
        storeCreditRL = (RelativeLayout)contentView.findViewById(R.id.storeCreditView);
        addedVoucherRL = (RelativeLayout)contentView.findViewById(R.id.appliedVoucherView);


        giftView = (RelativeLayout)contentView.findViewById(R.id.giftVoucherView);
        messageView = (RelativeLayout)contentView.findViewById(R.id.leaveMessageView);

        giftETView =(RelativeLayout)contentView.findViewById(R.id.editTextGiftView);
        leaveETView =(RelativeLayout)contentView.findViewById(R.id.editTextLeaveView);


        voucherET = (EditText)contentView.findViewById(R.id.codeInputText);
        voucherET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        giftET = (EditText)contentView.findViewById(R.id.editTextGift);
        giftET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        leaveET = (EditText)contentView.findViewById(R.id.editTextLeaveMessage);
        leaveET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        eWalletTxt = (TextView) contentView.findViewById(R.id.EWalletTxtViewNew);
        eWalletTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        onlineBankTxt = (TextView) contentView.findViewById(R.id.OnlineBankingTxtViewNew);
        onlineBankTxt.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        paymentRG
                = (RadioGroup)contentView.findViewById(R.id.NewPaymentGroup);
        ccButton = (RadioButton)contentView.findViewById(R.id.CreditCardBtnNew);
        ccButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        eWalletBtn = (RadioButton)contentView.findViewById(R.id.EWalletBtnNew);
        eWalletBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        onlineBankingBtn = (RadioButton)contentView.findViewById(R.id.onlineBankingBtnNew);
        onlineBankingBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        atomeBtn = (RadioButton)contentView.findViewById(R.id.atomePayment);
        atomeBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        enetsorpaypal = (RadioButton)contentView.findViewById(R.id.enetspaypal);
        enetsorpaypal.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TermsCondFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromPayment", "Yeah");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        privacyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PrivacyPolicyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromSignUp", "Nope");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        paymentRG.setOnCheckedChangeListener((radioGroup, i) -> {

            checkButton(i);

        });

        eWalletTxt.setOnClickListener(view -> {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("E-Wallet");
            builder.setSingleChoiceItems(EWalletArray, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    selectedEWallet = i;

                }
            });

            builder.setPositiveButton("Done",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            eWalletID = EWalletArrayValue[selectedEWallet];
                            eWalletType = EWalletArray[selectedEWallet];
                            eWalletTxt.setText(EWalletArray[selectedEWallet]);

                        }
                    });


            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.show();

        });

        onlineBankTxt.setOnClickListener(view -> {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("Online Banking");
            builder.setSingleChoiceItems(onlineBankingArray, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    selectedBanking = i;

                }
            });

            builder.setPositiveButton("Done",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            onlineBankTxt.setText(onlineBankingArray[selectedBanking]);
                            PaymentIDForIPay88 = onlineBankingArrayValue[selectedBanking];

                        }
                    });


            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.show();

        });

        if(SelectedShopID.equals("1")){

            enetsorpaypal.setVisibility(View.GONE);
            onlineBankingBtn.setVisibility(View.VISIBLE);
            eWalletBtn.setVisibility(View.VISIBLE);

        }
        else if (SelectedShopID.equals("2"))
        {
            enetsorpaypal.setVisibility(View.VISIBLE);
            enetsorpaypal.setText("Enets");
            onlineBankingBtn.setVisibility(View.GONE);
            eWalletBtn.setVisibility(View.GONE);

        }

        else {
            enetsorpaypal.setVisibility(View.VISIBLE);
            enetsorpaypal.setText("PayPal");
            onlineBankingBtn.setVisibility(View.GONE);
            eWalletBtn.setVisibility(View.GONE);
        }




        placeOrderTxt.setOnClickListener(view -> {

            if(TermCondChecked) {

                int radioBtnID = paymentRG.getCheckedRadioButtonId();
                switch (radioBtnID) {
                    case R.id.CreditCardBtnNew:

                        PAYMENT_METHOD = CREDIT_CARD_PAYMENT;

                        if (SelectedShopID.equals("1")) {
                            OpenPaymentPage("myr_cc");
                            paymentFromGift = "myr_cc";
                        } else if (SelectedShopID.equals("2")) {
                            OpenPaymentPage("sgd_cc");
                            paymentFromGift = "sgd_cc";

                        } else {
                            OpenPaymentPage("usd_cc");
                            paymentFromGift = "usd_cc";
                        }

                        break;
                    case R.id.EWalletBtnNew:

                        PAYMENT_METHOD = TNG_EWALLET;

                        if (eWalletID.length() > 0) {
                            if (eWalletType.equals("Touch 'n Go")) {
                                eWalletType = "tng";
                            } else if (eWalletType.equals("Boost")) {

                                eWalletType = "boost";
                            } else {

                            }

                            OpenPaymentPage(eWalletType);
                            paymentFromGift = eWalletType;


                        } else {

                            Toast.makeText(getActivity(), "Please select payment type!", Toast.LENGTH_LONG).show();


                        }

                        break;
                    case R.id.onlineBankingBtnNew:

                        PAYMENT_METHOD = ONLINE_BANKING_PAYMENT;

                        if (PaymentIDForIPay88.length() > 0) {
                            OpenPaymentPage("ipay88");
                            paymentFromGift = "ipay88";

                        } else {
                            Toast.makeText(getActivity(), "Please select online banking!", Toast.LENGTH_LONG).show();
                        }

                        break;
                    case R.id.enetspaypal:

                        if (SelectedShopID.equals("2")) {
                            PAYMENT_METHOD = ENETS_PAYMENT;
                            OpenPaymentPage("enets");
                            paymentFromGift = "enets";

                        } else {
                            PAYMENT_METHOD = PAYPAL_PAYMENT;
                            OpenPaymentPage("paypal");
                            paymentFromGift = "paypal";

                        }

                        break;

                    case R.id.atomePayment:

                        PAYMENT_METHOD = ATOME_PAYMENT;

                            OpenPaymentPage("atome");

                        break;
                    default:
                        Toast.makeText(getActivity(), "Please select payment type", Toast.LENGTH_SHORT).show();
                        break;
//                        }

                }

            }
            else {
                new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                        .setTitle("Message")
                        .setMessage("You must agree to the Terms of Service and Privacy Policy before continuing")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }

        });


        bagRV = (RecyclerView) contentView.findViewById(R.id.totalItemRV);
//        giftWrapRV = (RecyclerView) contentView.findViewById(R.id.giftWrapRV);

        rvLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        bagRV.setLayoutManager(rvLayoutManager);
        bagRV.setItemAnimator(new DefaultItemAnimator());
        bagRV.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        giftRVLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

//        giftWrapRV.setLayoutManager(giftRVLayoutManager);
//        giftWrapRV.setItemAnimator(new DefaultItemAnimator());
//        giftWrapRV.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        addVoucher = (TextView) contentView.findViewById(R.id.addVoucher);
        addStoreCredit = (ImageView)contentView.findViewById(R.id.nextIcon);
        addressBtn = (ImageView)contentView.findViewById(R.id.listAddressBtn);
        messageBtn = (ImageView)contentView.findViewById(R.id.nextIconMessage);
        tickIconShippimg = (ImageView)contentView.findViewById(R.id.tickShipping);

        addressObj = getArguments().getString("cartResultJObj");

        addVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(voucherET.getText().toString().equals("")){
                        Toast.makeText(getActivity(), "You must enter a voucher code", Toast.LENGTH_LONG).show();
                    }else{
                        applyVoucher(voucherET.getText().toString(), UserID, CartID);
                    }

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        removeAppliedVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Remove Voucher Code?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteVoucherFromCart(voucherCode2, CartID);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .show();

            }
        });

        removeCreditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteVoucherFromCart(storeCreditCode, CartID);

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        giftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(openGift){
                    giftETView.setVisibility(View.GONE);
                    openGift = false;
                    giftET.setText("");
                    addStoreCredit.setImageResource(R.drawable.btn_add_2);
                }
                else {
                    giftETView.setVisibility(View.VISIBLE);
                    openGift = true;
                    addStoreCredit.setImageResource(R.drawable.btn_clear);
                }

            }
        });

        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ChangeEditAddressList();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ChangeEditAddressList");
                fragmentTransaction.addToBackStack(null);

                Bundle args = new Bundle();
                String cartResultJObjString = addressObj;
                System.out.println("cartResultObj" + cartResultJObjString);
                args.putString("cartResultJObj", cartResultJObjString);
                args.putString("FOR_BILLING","0");

                args.putBoolean("changeDeliveryAdd",true);
                args.putInt("selectedBilling",0);


                fragment.setArguments(args);

                fragmentTransaction.commit();


            }
        });

        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openLeave){
                    leaveETView.setVisibility(View.GONE);
                    leaveET.setText("");
                    openLeave = false;
                    messageBtn.setImageResource(R.drawable.btn_add_2);
                }
                else {
                    leaveETView.setVisibility(View.VISIBLE);
                    openLeave = true;
                    messageBtn.setImageResource(R.drawable.btn_clear);

                }
            }
        });

        getCartDetailList();

        return contentView;
    }

    public void updateAddressConfirmationPage(Integer delivery, Integer billing) {

        SelectedAddress = delivery;
//        getCartDetailList();

    }

    public void getCartDetailList() {

        progressDialog = ProgressDialog.show(getActivity(), null, null, true, false);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.getWindow().setDimAmount(0);

        String cartResultJObjString = getArguments().getString("cartResultJObj");
        try {
            cartResultJObj = new JSONObject(cartResultJObjString);
            String action = cartResultJObj.getString("action");

            if (action.equals("editShippingAddress") || action.equals("addShippingAddress")) {

            } else if (action.equals("Carts_OrderStep1")) {
                if (cartResultJObj.getBoolean("status")) {

                    JSONObject data = cartResultJObj.getJSONObject("data");
                    JSONArray jsonArr = new JSONArray();

//                    JSONObject giftWrapObject = data.getJSONObject("gift_wrap");
//                    JSONObject inner = giftWrapObject.getJSONObject("22610");
//
//                    listArray_giftWrap.clear();
//
//
//                    String productID = inner.getString("id_product");
//                    String productName = inner.getString("name");
//                    String imageURL = inner.getString("image_url_tumb");
//
//                    System.out.println("macib = " + productID);
//                    System.out.println("macib 2 = " + productName);
//                    System.out.println("macib 3 = " + imageURL);
//
//
//                    listArray_giftWrap.add(new shoppingBagItem("", "", productID, imageURL, productName, "", "", "", "", "", "", 0));
//
//                    giftAdapter = new GiftWrapAdapter(getActivity(),listArray_giftWrap);
//                    giftWrapRV.setAdapter(giftAdapter);

                    String stateID = "", address2 = "", company = "", addressState = "";

                    try {
                        JSONArray jsonArr1 = new JSONArray();
                        jsonArr1 = data.getJSONArray("address_list");

                        ArrayList<String> addressArrayList = new ArrayList<String>();


                        for (int i = 0; i < jsonArr1.length(); i++) {
                            JSONObject jObj3 = jsonArr1.getJSONObject(i);
                            String addressID = jObj3.getString("id_address");
                            String addressFirstName = jObj3.getString("firstname");
                            String addressLastName = jObj3.getString("lastname");
                            String id_gender = jObj3.getString("id_gender");
                            if (jObj3.has("company")) {
                                company = jObj3.getString("company");
                            }
                            String address1 = jObj3.getString("address1");
                            address1 = address1.replace("\\", "");
                            if (jObj3.has("address2")) {
                                address2 = jObj3.getString("address2");
                            }
                            address2 = address2.replace("\\", "");
                            String addressPostCode = jObj3.getString("postcode");
                            String addressCity = jObj3.getString("city");
                            String countryID = jObj3.getString("id_country");
                            String addressCountry = jObj3.getString("country_name");
                            if (jObj3.has("id_state")) {
                                stateID = jObj3.getString("id_state");
                            }
                            if (jObj3.has("state_name")) {
                                addressState = jObj3.getString("state_name");
                            }
                            String addressPhone = jObj3.getString("phone");


                            System.out.println("country issss " + jObj3.getString("country_name"));

                            listArray_address_new.add(new addressItem(addressID, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone, company, id_gender));


                            addressArrayList.add(address1);

                        }

                        deliveryAddressObj = listArray_address_new.get(SelectedAddress);

                        String deliveryAddFirstName = deliveryAddressObj.getaddressFirstName();
                        String deliveryAddLastName = deliveryAddressObj.getaddressLastName();
                        String companyName = deliveryAddressObj.getCompany();
                        String deliveryAdd1 = deliveryAddressObj.getaddress1();
                        String deliveryAdd2 = deliveryAddressObj.getaddress2();
                        String deliveryAddPostCode = deliveryAddressObj.getaddressPostCode();
                        String deliveryAddCity = deliveryAddressObj.getaddressCity();
                        String deliveryAddCountry = deliveryAddressObj.getaddressCountry();
                        String deliveryAddState = deliveryAddressObj.getaddressState();
                        String deliveryAddPhone = deliveryAddressObj.getaddressPhone();

                        System.out.println("sdasdasda = " + deliveryAddPhone);

                        addressPhone.setText(deliveryAddPhone);
                        addressName.setText(deliveryAddFirstName + " " + deliveryAddLastName);
                        addressLbl.setText(companyName + "\n" + deliveryAdd1 + " " + deliveryAdd2 + "\n" + deliveryAddPostCode + " " + deliveryAddCity + "\n" + deliveryAddState + "\n" + deliveryAddCountry);



                    } catch (Exception e) {
                    }

                    if(containGift)
                    {
                        OpenPaymentPage(paymentFromGift);
                    }
                    else {

                        GoToNextStepWS(SelectedAddress, SelectedAddress);
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    private void GoToNextStepWS(int SelectedDeliveryAddress, int SelectedBillingAddress) {
        addressItem deliveryItem = listArray_address_new.get(SelectedDeliveryAddress);
        addressItem billingItem = listArray_address_new.get(SelectedBillingAddress);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey = pref.getString("apikey", "");

        String deliverID = deliveryItem.getaddressID();
        String billingID = billingItem.getaddressID();



        String action = "Carts/OrderStep2?apikey=" + apikey + "&id_cart=" + CartID + "&id_address_delivery=" + deliverID + "&id_address_billing=" + billingID + "";
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {

        if(result!=null){
            try{
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                if(result.has("action")) {

                    String action = result.getString("action");

                    if (action.equals("Carts_OrderStep1")) {
                        if (result.getBoolean("status")) {

                            JSONObject data = result.getJSONObject("data");
                            JSONArray jsonArr = new JSONArray();

                            String stateID = "", address2 = "", company = "", addressState = "";

                            try {
                                JSONArray jsonArr1 = new JSONArray();
                                jsonArr1 = data.getJSONArray("address_list");

                                ArrayList<String> addressArrayList = new ArrayList<String>();


                                for (int i = 0; i < jsonArr1.length(); i++) {
                                    JSONObject jObj3 = jsonArr1.getJSONObject(i);
                                    String addressID = jObj3.getString("id_address");
                                    String addressFirstName = jObj3.getString("firstname");
                                    String addressLastName = jObj3.getString("lastname");
                                    String id_gender = jObj3.getString("id_gender");
                                    if (jObj3.has("company")) {
                                        company = jObj3.getString("company");
                                    }
                                    String address1 = jObj3.getString("address1");
                                    address1 = address1.replace("\\", "");
                                    if (jObj3.has("address2")) {
                                        address2 = jObj3.getString("address2");
                                    }
                                    address2 = address2.replace("\\", "");
                                    String addressPostCode = jObj3.getString("postcode");
                                    String addressCity = jObj3.getString("city");
                                    String countryID = jObj3.getString("id_country");
                                    String addressCountry = jObj3.getString("country_name");
                                    if (jObj3.has("id_state")) {
                                        stateID = jObj3.getString("id_state");
                                    }
                                    if (jObj3.has("state_name")) {
                                        addressState = jObj3.getString("state_name");
                                    }
                                    String addressPhone = jObj3.getString("phone");


                                    System.out.println("country issss " + jObj3.getString("country_name"));

                                    listArray_address_new.add(new addressItem(addressID, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, countryID, addressCountry, stateID, addressState, addressPhone, company, id_gender));


                                    addressArrayList.add(address1);

                                }

                                deliveryAddressObj = listArray_address_new.get(SelectedAddress);

                                String deliveryAddFirstName = deliveryAddressObj.getaddressFirstName();
                                String deliveryAddLastName = deliveryAddressObj.getaddressLastName();
                                String deliveryAdd1 = deliveryAddressObj.getaddress1();
                                String deliveryAdd2 = deliveryAddressObj.getaddress2();
                                String deliveryAddPostCode = deliveryAddressObj.getaddressPostCode();
                                String deliveryAddCity = deliveryAddressObj.getaddressCity();
                                String deliveryAddCountry = deliveryAddressObj.getaddressCountry();
                                String deliveryAddState = deliveryAddressObj.getaddressState();
                                String deliveryAddPhone = deliveryAddressObj.getaddressPhone();

                                System.out.println("sdasdasda = " + deliveryAddPhone);

                                addressPhone.setText(deliveryAddPhone);
                                addressName.setText(deliveryAddFirstName + " " + deliveryAddLastName);
                                addressLbl.setText(deliveryAdd1 + " " + deliveryAdd2 + "\n" + deliveryAddPostCode + " " + deliveryAddCity + "\n" + deliveryAddState + "\n" + deliveryAddCountry);


                            } catch (Exception e) {
                            }

                            if (containGift) {
                                OpenPaymentPageFromGift(paymentFromGift);
                            } else {
                                GoToNextStepWS(SelectedAddress, SelectedAddress);
                            }
                        }
                    } else if (action.equals("Carts_OrderStep2")) {
                        if (result.getBoolean("status")) {
                            cartResultJObj1 = result;
                            String nextPage = result.getJSONObject("data").getString("next_page");

                            if (nextPage.equals("shippingMethod")) {
                                JSONArray jsonArr = new JSONArray();
                                jsonArr = result.getJSONObject("data").getJSONArray("carrier_list");

                                for (int i = 0; i < jsonArr.length(); i++) {
                                    JSONObject jObj = jsonArr.getJSONObject(i);
                                    String carrierID = jObj.getString("id_carrier");
                                    String carrierName = jObj.getString("name");
                                    String carrierPrice = jObj.getString("price");

                                    shippingMethod.setText(carrierName);
                                    if (SelectedShopID.equals("1")) {
                                        expectedShipDate.setText("RM " + carrierPrice + "");
                                        shippingFeeTotalTxt.setText("RM " + carrierPrice + "");

                                    } else if (SelectedShopID.equals("2")) {
                                        expectedShipDate.setText("SGD " + carrierPrice + "");
                                        shippingFeeTotalTxt.setText("SGD " + carrierPrice + "");


                                    } else {
                                        expectedShipDate.setText("USD " + carrierPrice + "");
                                        shippingFeeTotalTxt.setText("USD " + carrierPrice + "");
                                    }
                                    carrierID_forAPI = carrierID;
                                    tickIconShippimg.setVisibility(View.VISIBLE);
                                }

                                String appVersion = "1.0.0";

                                progressDialog.dismiss();

                                String apikey = pref.getString("apikey", "");
                                String shippingAPI = "Carts/OrderStep3?apikey=" + apikey + "&id_cart=" + CartID + "&id_address_delivery=" + deliveryAddressObj.getaddressID() + "&id_carrier=" + carrierID_forAPI + "&device_type=android&app_version=" + appVersion;

                                WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

                                callws.execute(shippingAPI);

                            } else if (nextPage.equals("cart")) {
                                getActivity().onBackPressed();
                            }

                        } else {
                            String message = result.getString("message");
                            new android.app.AlertDialog.Builder(getActivity())
                                    .setTitle("Poplook")
                                    .setMessage(message)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
//                                        Fragment fragment = new ShoppingBagFragment();
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "ShoppingBagFragment");
//                                        fragmentTransaction.addToBackStack(null);
//                                        fragmentTransaction.commit();
                                        }
                                    }).show();
                        }
                    } else if (action.equals("Carts_OrderStep3")) {

                        skipTimer = "0";
                        extra_cart = "0";

                        progressDialog.dismiss();
                        listArray_shoppingBag.clear();

                        String nextPage = result.getJSONObject("data").getString("next_page");
                        JSONObject paymentData = result.getJSONObject("data");

                        if (nextPage.equals("paymentSelectionPage")) {

                            JSONArray paymentArray = paymentData.getJSONArray("payment_list");
                            for (int i = 0; i < paymentArray.length(); i++) {
                                String paymentType;

                                JSONObject paymentObj = paymentArray.getJSONObject(i);
                                paymentType = paymentObj.getString("name");

                                if (paymentType.equals("E-Wallet")) {

                                    JSONArray jsonEwallet = new JSONArray();
                                    jsonEwallet = paymentArray.getJSONObject(i).getJSONArray("options");

                                    EWalletArray = new String[jsonEwallet.length()];
                                    EWalletArrayValue = new String[jsonEwallet.length()];

                                    for (int x = 0; x < jsonEwallet.length(); x++) {
                                        JSONObject jObj = jsonEwallet.getJSONObject(x);
                                        EWalletArray[x] = jObj.getString("name");
                                        EWalletArrayValue[x] = jObj.getString("value");
                                    }
                                }

                                if (paymentType.equals("Credit Card")) {

//                                creditCardExist = true;

                                }

                                if (paymentType.equals("Online Banking")) {

//                                onlineBankingExist = true;

                                    JSONArray jsonArrOnlineBanking = new JSONArray();
                                    jsonArrOnlineBanking = paymentArray.getJSONObject(i).getJSONArray("options");


                                    onlineBankingArray = new String[jsonArrOnlineBanking.length()];
                                    onlineBankingArrayValue = new String[jsonArrOnlineBanking.length()];

                                    for (int x = 0; x < jsonArrOnlineBanking.length(); x++) {
                                        JSONObject jObj = jsonArrOnlineBanking.getJSONObject(x);
                                        onlineBankingArray[x] = jObj.getString("name");
                                        onlineBankingArrayValue[x] = jObj.getString("value");

                                    }

                                }
                            }

                            if (paymentData.has("voucher_list")) {

                                listArray_voucher.clear();

                                try {
                                    JSONArray voucherArray = paymentData.getJSONArray("voucher_list");
                                    for (int j = 0; j < voucherArray.length(); j++) {
                                        JSONObject jObj2 = voucherArray.getJSONObject(j);
                                        String voucherID = jObj2.getString("id_discount");
                                        String voucher_name = jObj2.getString("name");
                                        String voucherCode = jObj2.getString("code");
                                        String voucherPercentage = jObj2.getString("reduction_percent");
                                        String voucherAmount = jObj2.getString("value_tax_exc");

//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                                        voucherTotal = voucherAmount;
                                        listArray_voucher.add(new voucherItem(voucherID, voucher_name, voucherCode, voucherAmount));

                                        for (int i = 0; i < listArray_voucher.size(); i++) {

                                            addedVoucherLbl.setText(listArray_voucher.get(i).getvoucherName());
                                            addedVoucherRL.setVisibility(View.VISIBLE);

                                            voucherCode2 = listArray_voucher.get(i).getvoucherID();
                                            voucherCodeName = listArray_voucher.get(i).getvoucherName();

                                        }
                                    }
                                } catch (Exception e) {
                                    addedVoucherLbl.setText("");
                                    addedVoucherRL.setVisibility(View.GONE);
                                    voucherET.setHint("Enter code");
                                    voucherCodeName = "";
                                    voucherCode2 = "";
                                    voucherTotal = "";

                                }

                            }

                            if (paymentData.has("store_credit_list")) {

                                listArray_storeCredit.clear();

                                try {
                                    JSONArray storeCreditArray = paymentData.getJSONArray("store_credit_list");
                                    for (int j = 0; j < storeCreditArray.length(); j++) {
                                        JSONObject jObj2 = storeCreditArray.getJSONObject(j);
                                        String voucherID = jObj2.getString("id_discount");
                                        String voucher_name = jObj2.getString("name");
                                        String voucherCode = jObj2.getString("code");
                                        String voucherPercentage = jObj2.getString("reduction_percent");
                                        String voucherAmount = jObj2.getString("value_tax_exc");

                                        storeCreditAmount = voucherAmount;
//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                                        listArray_storeCredit.add(new voucherItem(voucherID, voucher_name, voucherCode, voucherAmount));

                                        for (int i = 0; i < listArray_storeCredit.size(); i++) {

                                            storeCreditRL.setVisibility(View.VISIBLE);
                                            totalCreditLbl.setText(listArray_storeCredit.get(i).getvoucherName());
                                            storeCreditCode = listArray_storeCredit.get(i).getvoucherID();
                                            storeCreditCodeName = listArray_storeCredit.get(i).getvoucherName();


                                        }
                                    }
                                } catch (Exception e) {

                                    storeCreditRL.setVisibility(View.GONE);
                                    totalCreditLbl.setText("");
                                    storeCreditAmount = "";
                                    storeCreditCode = "";
                                    storeCreditCodeName = "";
                                }

                            }


                            JSONArray itemArray = paymentData.getJSONArray("product_list");
                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject jObj = itemArray.getJSONObject(i);
                                String productAttributeID = jObj.getString("id_product_attribute");
                                String productID = jObj.getString("id_product");
                                String productName = jObj.getString("name");
                                String productSize = jObj.getString("attributes_small");
                                if (productSize.equals("")) {
                                    productSize = "-";
                                }
                                String price = jObj.getString("price");
                                String discountedPrice = jObj.getString("price");
                                String imageURL = jObj.getString("image_url");
                                String timeEnd = jObj.getString("time_end");
                                String timeRem = jObj.getString("time_remainder");
                                String productRef = jObj.getString("reference");
                                int quantiti = jObj.getInt("quantity");
                                String quantity = String.valueOf(quantiti);
                                String item_total = jObj.getString("total");
                                Integer quantity_available = jObj.getInt("quantity_available");
//

                                price = String.format("%.2f", Float.parseFloat(price));

                                listArray_shoppingBag.add(new shoppingBagItem(CartID, productAttributeID, productID, imageURL, productName, productSize, productRef, price, item_total, quantity, timeRem, quantity_available));

                            }

                            totalItemInCart.setText(+listArray_shoppingBag.size() + " items");

                            adapter = new orderConfirmationBagAdapter(getActivity(), listArray_shoppingBag);
                            bagRV.setAdapter(adapter);

                            if (SelectedShopID.equals("1")) {
                                totalRetailTxt.setText("RM " + paymentData.getString("totalProducts"));
                                totalSubTotalTxt.setText("-" + voucherTotal);
                                subtotalTxt.setText("Discount : " + voucherCodeName);

                                storeCreditBottomTitle.setText("Store Credit : " + storeCreditCodeName);
                                storeCreditTotalBottom.setText("-" + storeCreditAmount);

                                totalPrice = paymentData.getString("totalPriceWt");

                                totalPriceBottomTxt.setText("RM " + paymentData.getString("totalPriceWt"));
                            } else if (SelectedShopID.equals("2")) {
                                totalRetailTxt.setText("SGD " + paymentData.getString("totalProducts"));
                                totalSubTotalTxt.setText("-" + voucherTotal);
                                subtotalTxt.setText("Discount : " + voucherCodeName);

                                storeCreditBottomTitle.setText("Store Credit : " + storeCreditCodeName);
                                storeCreditTotalBottom.setText("-" + storeCreditAmount);

                                totalPriceBottomTxt.setText("SGD " + paymentData.getString("totalPriceWt"));
                            } else {
                                totalRetailTxt.setText("USD " + paymentData.getString("totalProducts"));
                                totalSubTotalTxt.setText("-" + voucherTotal);
                                subtotalTxt.setText("Discount : " + voucherCodeName);

                                storeCreditBottomTitle.setText("Store Credit : " + storeCreditCodeName);
                                storeCreditTotalBottom.setText("-" + storeCreditAmount);

                                totalPriceBottomTxt.setText("USD " + paymentData.getString("totalPriceWt"));
                            }

                        }
                    } else if (action.equals("Carts_OrderStep4")) {
                        if (result.getJSONObject("data").getString("next_page").equals("callPaymentGateway")) {


                            JSONObject data = result.getJSONObject("data");
                            CartID = data.getString("id_cart");
                            editor.putString("giftMessage", "");
                            editor.putString("LeaveMessage", "");
                            editor.putString("CartID", CartID);
                            editor.apply();

                            String orderID = data.getString("id_order");
                            OrderID = data.getString("id_order");

                            String totalPrice = data.getString("totalPrice");

                            if (PAYMENT_METHOD == CREDIT_CARD_PAYMENT) {

                                if (!SelectedShopID.equals("2")) {

                                    System.out.println("here for myr/usd payment");

                                    Intent ipay88Intent = new Intent(getActivity(), IPay88PaymentActivity.class);
                                    ipay88Intent.putExtra("CREDITCARD_PAYMENT", "1");
                                    ipay88Intent.putExtra("ORDER_ID", orderID);
                                    ipay88Intent.putExtra("CART_ID", CartID);
                                    ipay88Intent.putExtra("ITEM_PRICE", totalPrice);
                                    startActivityForResult(ipay88Intent, 1);
                                    getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
                                } else {

                                    System.out.println("here for sgd payment");


                                    Intent intent = new Intent(getActivity(), ENetsPaymentActivity.class);
                                    intent.putExtra("CREDITCARD_PAYMENT", "1");
                                    intent.putExtra("ITEM_DETAIL", "POPLOOK purchase");
                                    intent.putExtra("ITEM_PRICE", totalPrice);
                                    intent.putExtra("ORDER_ID", orderID);
                                    intent.putExtra("CART_ID", CartID);

                                    System.out.println("ENETS input 1 = " + totalPrice);
                                    System.out.println("ENETS input 2 = " + orderID);
                                    System.out.println("ENETS input 3 = " + CartID);


                                    if (data.has("payment_voucher")) {

                                        String paymentVoucher = data.getString("payment_voucher");
//
                                        System.out.println("voucher code 1 = " + paymentVoucher);

                                        intent.putExtra("PAYMENT_VOUCHER", paymentVoucher);

                                    } else {
                                        intent.putExtra("PAYMENT_VOUCHER", "0");

                                    }
                                    startActivityForResult(intent, 1);
                                    getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
                                }
                            } else if (PAYMENT_METHOD == TNG_EWALLET) {

                                Intent ipay88Intent = new Intent(getActivity(), IPay88PaymentActivity.class);
                                ipay88Intent.putExtra("CREDITCARD_PAYMENT", "4");
                                ipay88Intent.putExtra("eWallet", eWalletID);
                                ipay88Intent.putExtra("ORDER_ID", orderID);
                                ipay88Intent.putExtra("CART_ID", CartID);
                                ipay88Intent.putExtra("ITEM_PRICE", totalPrice);
                                startActivityForResult(ipay88Intent, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);

                            } else if (PAYMENT_METHOD == ONLINE_BANKING_PAYMENT) {
                                Intent ipay88Intent2 = new Intent(getActivity(), IPay88PaymentActivity.class);
                                ipay88Intent2.putExtra("CREDITCARD_PAYMENT", "0");
                                ipay88Intent2.putExtra("ITEM_PRICE", totalPrice);
                                ipay88Intent2.putExtra("PAYMENT_ID", PaymentIDForIPay88);
                                ipay88Intent2.putExtra("ORDER_ID", orderID);
                                ipay88Intent2.putExtra("CART_ID", CartID);
                                startActivityForResult(ipay88Intent2, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);

                            } else if (PAYMENT_METHOD == PAYPAL_PAYMENT) {
                                Intent intent = new Intent(getActivity(), PaypalPaymentActivity.class);
                                intent.putExtra("ITEM_DETAIL", "POPLOOK purchase");
                                intent.putExtra("ITEM_PRICE", totalPrice);
                                intent.putExtra("ORDER_ID", orderID);
                                intent.putExtra("CART_ID", CartID);
                                startActivityForResult(intent, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);

                            } else if (PAYMENT_METHOD == ENETS_PAYMENT) {
                                Intent intent = new Intent(getActivity(), ENetsPaymentActivity.class);
                                intent.putExtra("CREDITCARD_PAYMENT", "0");
                                intent.putExtra("ITEM_DETAIL", "POPLOOK purchases");
                                intent.putExtra("ITEM_PRICE", totalPrice);
                                intent.putExtra("ORDER_ID", orderID);
                                intent.putExtra("CART_ID", CartID);
                                startActivityForResult(intent, 1);
                                getActivity().overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);
                            } else if (PAYMENT_METHOD == ATOME_PAYMENT) {

                                String userName = pref.getString("Name", "") + " " + pref.getString("LastName", "");
                                String email = pref.getString("Email", "");

                                JSONObject addressInvoice = data.getJSONObject("address_invoice");

                                String urlAtome = "https://api.apaylater.com/v2/payments";

                                JSONObject customerInfo = new JSONObject();
                                try {
                                    customerInfo.put("mobileNumber", addressInvoice.getString("phone"));
                                    customerInfo.put("fullName", userName);
                                    customerInfo.put("email", email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JSONObject shippingInfo = new JSONObject();
                                try {

                                    JSONArray address = new JSONArray();
                                    address.put(addressInvoice.getString("address1"));
                                    address.put(addressInvoice.getString("address2"));
                                    address.put(addressInvoice.getString("postcode"));

                                    String SelectedCountryIsoCode = pref.getString("SelectedCountryIsoCode", "MY");

                                    shippingInfo.put("countryCode", SelectedCountryIsoCode);
                                    shippingInfo.put("lines", address);
                                    shippingInfo.put("postCode", addressInvoice.getString("postcode"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray itemArray = new JSONArray();
                                JSONObject itemList = new JSONObject();

                                for(int x=0;x<listArray_shoppingBag.size();x++){

                                    final shoppingBagItem item = listArray_shoppingBag.get(x);


                                    try {
                                        itemList.put("itemId", item.getproductID());
                                        itemList.put("name", item.getproductName());

                                        String itemPrice = item.getproductTotalPrice();
                                        double price = Double.parseDouble(itemPrice);
                                        double convertPrice = price*100;

                                        String priceStr = String.valueOf(convertPrice);

                                        itemList.put("price", priceStr);
                                        itemList.put("quantity", item.getproductQuantity());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    itemArray.put(itemList);
                                }

                                Long tsLong = System.currentTimeMillis()/1000;
                                String ts = tsLong.toString();

                                OrderIDWithTimeStamp = orderID+ts;

                                JSONObject atomeBody = new JSONObject();
                                try {
                                    atomeBody.put("referenceId", OrderIDWithTimeStamp);
                                    atomeBody.put("currency", data.getString("currency"));
                                    double price = Double.parseDouble(totalPrice);
                                    double convertPrice = price*100;
                                    String priceStr = String.valueOf(convertPrice);

                                    atomeBody.put("amount", priceStr);
                                    atomeBody.put("callbackUrl", "https://poplook.com/modules/atome_payment/atome_payment_get.php?referenceid=" + OrderIDWithTimeStamp + "&cart_id=" + CartID);
                                    atomeBody.put("paymentResultUrl", "https://dev3.poplook.com");
                                    atomeBody.put("paymentCancelUrl", "https://dev3.poplook.com");

                                    atomeBody.put("merchantReferenceId", CartID);

                                    atomeBody.put("taxAmount", 0);

                                    atomeBody.put("customerInfo", customerInfo);
                                    atomeBody.put("shippingAddress", shippingInfo);
                                    atomeBody.put("items", itemArray);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody formBody = RequestBody.create(JSON, atomeBody.toString());

                                WebServiceAccessPost callws = new WebServiceAccessPost(getActivity(), this);
                                callws.setAction(urlAtome);
                                callws.execute(formBody);
                            }
                            Insider.Instance.tagEvent("checkout_visited").build();

                        }

                    }
                    else if (action.equals("Carts_OrderStep5")) {

                        if (result.getBoolean("status")) {
                            editor.putString("order_success_id", OrderID);
                            editor.apply();

                            Fragment fragment = new OrderConfirmationFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Payment Failed", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, 50);
                            toast.show();

                            Fragment fragment = new OrderHistoryFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    }
                    else if (action.equals("Vouchers_validate")) {
                        if (result.getBoolean("status")) {
                            String message = result.getString("message");

                            if (!message.equals("This voucher does not exists")) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                Insider.Instance.tagEvent("coupon_used").addParameterWithString("coupon_code", couponCodeForInsider).build();

                                String apikey = pref.getString("apikey", "");
//                            editor.putString("LeaveMessage", leaveMessage);
//                            editor.putString("giftMessage", giftMessage);
                                editor.apply();

                                String actions = "Carts/OrderStep1?apikey=" + apikey + "&id_cart=" + CartID + "&gift_message=" + giftET.getText().toString();

                                WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

                                callws.execute(actions);

                            } else {
                                Toast.makeText(getActivity(), "Invalid Voucher Code", Toast.LENGTH_LONG).show();
//                            new AlertDialog.Builder(getActivity())
//                                    .setTitle("Message")
//                                    .setMessage("Invalid Voucher Code")
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    }).show();
                            }
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        } else {
                            String message = result.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }

                    } else if (action.equals("Carts_removeVoucher")) {
                        if (result.getBoolean("status")) {
                            String message = result.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                            String apikey = pref.getString("apikey", "");
                            String actions = "Carts/OrderStep1?apikey=" + apikey + "&id_cart=" + CartID + "&gift_message=" + "";

                            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                            callws.execute(actions);
                        } else {

                            Toast.makeText(getActivity(), "Unable to remove voucher please try again", Toast.LENGTH_LONG).show();

                        }

                    }

                }
                else {
                    String paymentStatus = result.getString("status");
                    int totalAmount = result.getInt("amount");
                    if (paymentStatus.equals("PAID")) {

                        PackageInfo pInfo = null;

                        try {
                            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        //get the app version Name for display
                        appVersion = pInfo.versionName;

                        System.out.println("atome payment result 7 = " + totalAmount);


                        double finalAmount = totalAmount / 100;

                        System.out.println("atome payment result 6 = " + finalAmount);

                        String totalAmountStr = String.valueOf(finalAmount);

                        System.out.println("atome payment result 5 = " + totalAmountStr);

                        JSONObject paymentDetail = result.getJSONObject("paymentTransaction");
                        String transactionID = paymentDetail.getString("transactionId");

                        String apikey = pref.getString("apikey", "");
                        String action = "Carts/OrderStep5?apikey="+apikey +"&device_type=android&id_order="+OrderID+"&transaction_status=1&payment_type=atome&total_paid=" + totalAmountStr + "&transaction_id=" + transactionID + "&app_version=" + appVersion;
                        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                        callws.execute(action);
                    }
                    else if(paymentStatus.equals("PROCESSING")){
                        String paymentUrl = result.getString("appPaymentUrl");
//                    Fragment fragment = new AtomeFragment();

                        if(fromAtome){
                            PackageInfo pInfo = null;

                            try {
                                pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            //get the app version Name for display
                            appVersion = pInfo.versionName;

                            double finalAmount = totalAmount / 100;
                            String totalAmountStr = String.valueOf(finalAmount);

                            String apikey = pref.getString("apikey", "");
                            String action = "Carts/OrderStep5?apikey=" + apikey + "&device_type=android&id_order=" + OrderID + "&transaction_status=0&payment_type=atome&total_paid=" + totalAmountStr + "&transaction_id&app_version=" + appVersion;
                            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                            callws.execute(action);
                        }
                        else {
                            fromAtome = true;
                            AtomeSDK.INSTANCE.setPaymentUrl(paymentUrl);
                        }
                    }

                    else {
                        PackageInfo pInfo = null;

                        try {
                            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        //get the app version Name for display
                        appVersion = pInfo.versionName;

                        double finalAmount = totalAmount / 100;
                        String totalAmountStr = String.valueOf(finalAmount);

                        String apikey = pref.getString("apikey", "");
                        String action = "Carts/OrderStep5?apikey=" + apikey + "&device_type=android&id_order=" + OrderID + "&transaction_status=0&payment_type=atome&total_paid=" + totalAmountStr + "&transaction_id&app_version=" + appVersion;
                        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
                        callws.execute(action);
                    }

//                    Bundle bundle = new Bundle();
//                    bundle.putString("paymentURL", paymentUrl);
//                    bundle.putString("referenceID", OrderID);
//
//                    fragment.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                }

            }
            catch (Exception e){
            }

        }
        else{

            new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("Message")
                    .setMessage("Please connect to the Internet")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();

        }

    }

    private void checkButton(int id){

        switch (id){

            case R.id.CreditCardBtnNew:

                eWalletTxt.setVisibility(View.GONE);
                onlineBankTxt.setVisibility(View.GONE);

                break;
            case R.id.EWalletBtnNew:

                eWalletTxt.setVisibility(View.VISIBLE);
                onlineBankTxt.setVisibility(View.GONE);

                break;
            case R.id.onlineBankingBtnNew:

                eWalletTxt.setVisibility(View.GONE);
                onlineBankTxt.setVisibility(View.VISIBLE);

                break;


        }


    }

    private void OpenPaymentPage(String paymentType)
    {
        String action;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String leaveMessage = pref.getString("LeaveMessage", "");
        String apikey =pref.getString("apikey","");
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PaymentType", paymentType);
        editor.apply();

        if(giftET.getText().toString().equals("")){

            containGift=false;
            if (paymentType.equals("sgd_cc") || paymentType.equals("enets")) {
                action = "Carts/OrderStep4?apikey=" + apikey + "&id_cart=" + CartID + "&payment=" + paymentType + "&message=" + leaveET.getText().toString() + "&skip_timer=" + skipTimer + "&extra_cart=" + extra_cart;
            } else {
                action = "Carts/OrderStep4?apikey=" + apikey + "&id_cart=" + CartID + "&payment=" + paymentType + "&message=" + leaveET.getText().toString() + "&skip_timer=" + skipTimer;
            }
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(action);

        }

        else {

            containGift = true;
            String actions="Carts/OrderStep1?apikey="+apikey+"&id_cart="+CartID+"&gift_message="+giftET.getText().toString();
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(actions);
        }
    }

    private void OpenPaymentPageFromGift(String paymentType)
    {
        String action;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String leaveMessage = pref.getString("LeaveMessage", "");
        String apikey =pref.getString("apikey","");
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PaymentType", paymentType);
        editor.apply();


            if (paymentType.equals("sgd_cc") || paymentType.equals("enets")) {
                action = "Carts/OrderStep4?apikey=" + apikey + "&id_cart=" + CartID + "&payment=" + paymentType + "&message=" + leaveET.getText().toString() + "&skip_timer=" + skipTimer + "&extra_cart=" + extra_cart;
            } else {
                action = "Carts/OrderStep4?apikey=" + apikey + "&id_cart=" + CartID + "&payment=" + paymentType + "&message=" + leaveET.getText().toString() + "&skip_timer=" + skipTimer;
            }
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(action);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                String paymentSuccess = pref.getString("payment_success","0");

                if(paymentSuccess.equals("1"))
                {

                    Fragment fragment = new OrderConfirmationFragment();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

                else
                {

                    Fragment fragment = new MyAccountFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment, "MyAccountFragment");
                    fragmentTransaction.commit();
                    editor.putString("CartID", "");
                    editor.putString("cartItem", "0");
                    editor.putString("PaymentDone", "1");
                    editor.apply();

                    ((MainActivity) getActivity()).changeToolBarBagNotiText("0");

                }

            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Fragment fragment = new MyAccountFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "MyAccountFragment");
                fragmentTransaction.commit();

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("CartID", "");
                editor.putString("cartItem", "0");
                editor.putString("PaymentDone", "1");
                editor.apply();

                ((MainActivity) getActivity()).changeToolBarBagNotiText("0");
            }
            else
            {
                Toast.makeText(getActivity(),"Some problems occured. Please try again.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void applyVoucher(String voucherCode, String userID, String cartID){

        couponCodeForInsider = voucherCode;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID+"&mobile=1";
//        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void deleteVoucherFromCart(String voucherID,String cartID){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Carts/removeVoucher";
        RequestBody formBody = new FormBody.Builder()
                .add("apikey",apikey)
                .add("id_cart",cartID)
                .add("id_cart_rule",voucherID)
                .build();
        WebServiceAccessDelete callws = new WebServiceAccessDelete(getActivity(), this);
        callws.setAction(action);
        callws.execute(formBody);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(fromAtome) {
            String urlAtome = "https://api.apaylater.com/v2/payments/"+OrderIDWithTimeStamp;
            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
            callws.execute(urlAtome);
        }
    }

}
