package com.tiseno.poplook;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.shoppingBagAdapter;
import com.tiseno.poplook.functions.shoppingBagItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    TextView orderDetailsNotesOnOrderTV, orderDetailsNotesOnOrderLblTV;

    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<shoppingBagItem>();
    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();

    float totalAllProductPrice;
    float totalVoucherPrice;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 111;
    String orderDate;
    String orderID;
    String invoiceID, invoicePrefix;
    String orderCarrier;
    String orderPayment;
    String orderNotes, total_products, carrier_price, id_cart;

    String shipping_addressCompany = "", shipping_addressFirstName, shipping_addressLastName, shipping_address1, shipping_address2 = "", shipping_addressPostCode, shipping_addressCity, shipping_addressCountry, shipping_addressState = "", shipping_addressPhone;

    String billing_addressCompany, billing_addressFirstName, billing_addressLastName, billing_address1, billing_address2, billing_addressPostCode, billing_addressCity, billing_addressCountry, billing_addressState, billing_addressPhone;

    TextView orderDetailsTotalPayableTV, orderDetailsInvoiceNoTV, orderDetailsOrderNoTV, orderDetailsDateTV, orderDetailsCarrierTV, orderDetailsPaymentMethodTV;
    TextView shippingAddressTV, shippingAddressContactNoTV, billingAddressInfoTV, billingAddressContactNoTV, orderDetailstotalPayableRM1TV, shippingMethodTVlbl, shippingMethodTV, addressfooterRM1, shippingMethodTVbill1;
    TextView paymentMethodLabel, invoiceNoLabel, orderLabel, dateLabel, carrierLabel, downloadReturnFormLabel, shippingAddressLabel, shippingAddressContactNoLblTV, billingAddressLabel, billingAddressContactNoLblTV, descLabel;

    ImageButton orderDetailsDownloadReturnFormBtnIV;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    String SelectedShopID, SelectedCountryCurrency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_order_details, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        SelectedShopID = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency = pref.getString("SelectedCountryCurrency", "RM");
        //

        ((MainActivity) getActivity()).changeToolBarText("Order Details");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.orderDetailsRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getOrderDetails();


        return contentView;
    }

    private void getOrderDetails() {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        String action = "Orders/order/id/" + getArguments().getString("orderID") + "?apikey=" + apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }


    @Override
    public void onTaskComplete(JSONObject result) {
        if (result != null) {
            try {
                listArray_shoppingBag.clear();
                totalAllProductPrice = 0;
                String action = result.getString("action");

                if (action.equals("Orders_order")) {
                    if (result.getBoolean("status")) {
                        JSONObject orderHistroyJObj = result.getJSONObject("data");

                        orderDate = orderHistroyJObj.getString("date_add");
                        orderID = orderHistroyJObj.getString("id_order");
                        invoicePrefix = orderHistroyJObj.getString("invoice_prefix");
                        invoiceID = orderHistroyJObj.getString("invoice_number");
                        carrier_price = orderHistroyJObj.getString("carrier_price");
                        orderCarrier = orderHistroyJObj.getString("carrier");
                        orderPayment = orderHistroyJObj.getString("payment_method");
                        try {
                            JSONArray orderNotesJSON = orderHistroyJObj.getJSONArray("order_message");
                            orderNotes = orderNotesJSON.getJSONObject(0).getString("message");
                        } catch (Exception e) {
                            orderNotes = "";
                        }
                        JSONObject orderDetailsObj = orderHistroyJObj.getJSONObject("order_details");
                        total_products = orderDetailsObj.getString("total_paid");
                        id_cart = orderDetailsObj.getString("id_cart");


                        try {
                            JSONObject shippingObj = orderHistroyJObj.getJSONObject("shipping_details");
                            JSONObject billingObj = orderHistroyJObj.getJSONObject("billing_details");

                            shipping_addressCompany = shippingObj.getString("company");
                            shipping_addressFirstName = shippingObj.getString("firstname");
                            shipping_addressLastName = shippingObj.getString("lastname");
                            shipping_address1 = shippingObj.getString("address1");
                            shipping_address1 = shipping_address1.replace("\\", "");
                            shipping_address2 = shippingObj.getString("address2");
                            shipping_address2 = shipping_address2.replace("\\", "");
                            shipping_addressPostCode = shippingObj.getString("postcode");
                            shipping_addressCity = shippingObj.getString("city");
                            shipping_addressCountry = shippingObj.getString("country");
                            shipping_addressState = shippingObj.getString("state");
                            shipping_addressPhone = shippingObj.getString("phone");

                            billing_addressCompany = billingObj.getString("company");
                            billing_addressFirstName = billingObj.getString("firstname");
                            billing_addressLastName = billingObj.getString("lastname");
                            billing_address1 = billingObj.getString("address1");
                            billing_address1 = billing_address1.replace("\\", "");
                            billing_address2 = billingObj.getString("address2");
                            billing_address2 = billing_address2.replace("\\", "");
                            billing_addressPostCode = billingObj.getString("postcode");
                            billing_addressCity = billingObj.getString("city");
                            billing_addressCountry = billingObj.getString("country");
                            billing_addressState = billingObj.getString("state");
                            billing_addressPhone = billingObj.getString("phone");

                        } catch (Exception e) {

                        }

                        try {
                            JSONArray jsonArr = new JSONArray();
                            jsonArr = orderHistroyJObj.getJSONArray("product_details");
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jObj = jsonArr.getJSONObject(i);
                                String prodID = jObj.getString("id_product");
                                String productName = jObj.getString("name");
                                String productRef = jObj.getString("reference");
                                String price = jObj.getString("unit_price_wt");
                                String quantity = jObj.getString("quantity");
                                String image = jObj.getString("image_url");
                                String size = jObj.getString("attributes_small");
                                String id_product_attribute = jObj.getString("id_product_attribute");
                                String totalPrice = jObj.getString("total_wt");
//                                Integer quantity_available = jObj.getInt("quantity_available");


//                                price = String.format("%.2f", Float.parseFloat(price));
//
//
//                                String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));
//
//                                totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);

                                listArray_shoppingBag.add(new shoppingBagItem(id_cart, id_product_attribute, prodID, image, productName, size, productRef, price, totalPrice, quantity, "", 1));


                            }
                        } catch (Exception e) {
                        }

                        listArray_voucher.clear();

                        try {

                            JSONArray voucherArray = orderHistroyJObj.getJSONArray("discount_details");

                            totalVoucherPrice = 0;

                            for (int j = 0; j < voucherArray.length(); j++) {
                                JSONObject jObj2 = voucherArray.getJSONObject(j);
                                String voucherCode = jObj2.getString("code");
                                String voucherPercentage = jObj2.getString("reduction_percent");
                                String voucherAmount = jObj2.getString("value_tax_excl");

                                totalVoucherPrice += Float.parseFloat(voucherAmount);

                                listArray_voucher.add(new voucherItem("", "", voucherCode, voucherAmount));
                            }
                        } catch (Exception e) {
                        }
                    }


                    RVAdapter = new shoppingBagAdapter(getActivity(), listArray_shoppingBag, null, false);

                    // Make Bookends
                    Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

                    // Inflate footer view
                    LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                    RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.order_details_header_view, mRecyclerView, false);
                    RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.order_details_footer_view, mRecyclerView, false);
                    RelativeLayout footerShipping = (RelativeLayout) inflater1.inflate(R.layout.order_history_footer_shipping, mRecyclerView, false);


                    TextView step1TV = (TextView) header.findViewById(R.id.step1TV);
                    TextView step2TV = (TextView) header.findViewById(R.id.step2TV);
                    TextView step2toTV = (TextView) header.findViewById(R.id.step2toTV);
                    TextView step3TV = (TextView) header.findViewById(R.id.step3TV);

                    step1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    step2TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    step2toTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    step3TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


                    paymentMethodLabel = (TextView) header.findViewById(R.id.paymentMethodLabel);
                    invoiceNoLabel = (TextView) header.findViewById(R.id.invoiceNoLabel);
                    orderLabel = (TextView) header.findViewById(R.id.orderLabel);
                    dateLabel = (TextView) header.findViewById(R.id.dateLabel);
                    carrierLabel = (TextView) header.findViewById(R.id.carrierLabel);
                    downloadReturnFormLabel = (TextView) header.findViewById(R.id.downloadReturnFormLabel);

                    shippingAddressLabel = (TextView) header.findViewById(R.id.shippingAddressLabel);
                    shippingAddressContactNoLblTV = (TextView) header.findViewById(R.id.shippingAddressContactNoLblTV);
                    billingAddressLabel = (TextView) header.findViewById(R.id.billingAddressLabel);
                    billingAddressContactNoLblTV = (TextView) header.findViewById(R.id.billingAddressContactNoLblTV);
                    descLabel = (TextView) header.findViewById(R.id.descLabel);

                    orderDetailsInvoiceNoTV = (TextView) header.findViewById(R.id.orderDetailsInvoiceNoTV);
                    orderDetailsOrderNoTV = (TextView) header.findViewById(R.id.orderDetailsOrderNoTV);
                    orderDetailsDateTV = (TextView) header.findViewById(R.id.orderDetailsDateTV);
                    orderDetailsCarrierTV = (TextView) header.findViewById(R.id.orderDetailsCarrierTV);
                    orderDetailsPaymentMethodTV = (TextView) header.findViewById(R.id.orderDetailsPaymentMethodTV);
                    orderDetailsDownloadReturnFormBtnIV = (ImageButton) header.findViewById(R.id.orderDetailsDownloadReturnFormBtnIV);

                    shippingAddressTV = (TextView) header.findViewById(R.id.shippingAddressTV);
                    shippingAddressContactNoTV = (TextView) header.findViewById(R.id.shippingAddressContactNoTV);
                    billingAddressInfoTV = (TextView) header.findViewById(R.id.billingAddressInfoTV);
                    billingAddressContactNoTV = (TextView) header.findViewById(R.id.billingAddressContactNoTV);

                    shippingMethodTVlbl = (TextView) footerShipping.findViewById(R.id.shippingMethodTVlbl);
                    shippingMethodTV = (TextView) footerShipping.findViewById(R.id.shippingMethodTV);
                    addressfooterRM1 = (TextView) footerShipping.findViewById(R.id.addressfooterRM1);
                    shippingMethodTVbill1 = (TextView) footerShipping.findViewById(R.id.shippingMethodTVbill1);

                    paymentMethodLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    invoiceNoLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    orderLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    dateLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    carrierLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    downloadReturnFormLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                    shippingAddressLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    shippingAddressContactNoLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    billingAddressLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    billingAddressContactNoLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    descLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                    orderDetailsInvoiceNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    orderDetailsOrderNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    orderDetailsDateTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    orderDetailsCarrierTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    orderDetailsPaymentMethodTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


                    shippingAddressTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    shippingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    billingAddressInfoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    billingAddressContactNoTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


                    shippingMethodTVlbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    shippingMethodTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    addressfooterRM1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    shippingMethodTVbill1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

                    orderDetailsInvoiceNoTV.setText("# " + invoicePrefix + invoiceID);
                    orderDetailsOrderNoTV.setText("# " + orderID);
                    orderDetailsDateTV.setText(orderDate);
                    orderDetailsCarrierTV.setText(orderCarrier);
                    orderDetailsPaymentMethodTV.setText(orderPayment);

                    shippingAddressTV.setText(shipping_addressFirstName + " " + shipping_addressLastName + "\n" + shipping_addressCompany + "\n" + shipping_address1 + " " + shipping_address2 + "\n" + shipping_addressPostCode + " " + shipping_addressCity + "\n" + shipping_addressState + "\n" + shipping_addressCountry);
                    shippingAddressContactNoTV.setText(shipping_addressPhone);

                    billingAddressInfoTV.setText(billing_addressFirstName + " " + billing_addressLastName + "\n" + billing_addressCompany + "\n" + billing_address1 + " " + billing_address2 + "\n" + billing_addressPostCode + " " + billing_addressCity + "\n" + billing_addressState + "\n" + billing_addressCountry);
                    billingAddressContactNoTV.setText(billing_addressPhone);

                    orderDetailsTotalPayableTV = (TextView) footer.findViewById(R.id.orderDetailsTotalPayableTV);
                    orderDetailstotalPayableRM1TV = (TextView) footer.findViewById(R.id.orderDetailstotalPayableRM1TV);

                    orderDetailstotalPayableRM1TV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    orderDetailsTotalPayableTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    orderDetailstotalPayableRM1TV.setText("TOTAL " + SelectedCountryCurrency);


                    if (total_products.equals("0.00")) {
                        orderDetailsTotalPayableTV.setText("0.00");
                    } else {
                        orderDetailsTotalPayableTV.setText(total_products);
                    }
                    orderDetailsNotesOnOrderLblTV = (TextView) footer.findViewById(R.id.orderDetailsNotesOnOrderLblTV);
                    orderDetailsNotesOnOrderTV = (TextView) footer.findViewById(R.id.orderDetailsNotesOnOrderTV);

                    orderDetailsNotesOnOrderLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                    orderDetailsNotesOnOrderTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    if (orderNotes.isEmpty() || orderNotes.equalsIgnoreCase("") || orderNotes.equalsIgnoreCase(null) || orderNotes.equalsIgnoreCase("null")) {
                        orderDetailsNotesOnOrderTV.setText("");
                    } else {
                        orderDetailsNotesOnOrderTV.setText(orderNotes);
                    }
                    orderDetailsNotesOnOrderTV.setMovementMethod(new ScrollingMovementMethod());

                    mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            orderDetailsNotesOnOrderTV.getParent().requestDisallowInterceptTouchEvent(false);

                            return false;
                        }
                    });

                    orderDetailsNotesOnOrderTV.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            orderDetailsNotesOnOrderTV.getParent().requestDisallowInterceptTouchEvent(true);

                            return false;
                        }
                    });

                    orderDetailsDownloadReturnFormBtnIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                                        READ_EXTERNAL_STORAGE);
                                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                            final DownloadFileFromURL downloadTask = new DownloadFileFromURL(getActivity());
//                            downloadTask.execute("http://dev.poplook.com/themes/poplook/pdf/POPLOOK_generic_returnform.pdf");
                                    DownloadManager mManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                                    Uri downloadUri = Uri.parse("http://poplook.com/themes/poplook/pdf/POPLOOK_generic_returnform.pdf");
                                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                                    request.setTitle("POPLOOK");
                                    request.setDescription("Downloading Poplook Return Form");
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Poplook_Return_Form.pdf");
                                    mManager.enqueue(request);
//                                    Intent intent = new Intent();
//                                    intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
//                                    startActivity(intent);
                                    System.out.println("LALU1");
                                } else {
                                    if (shouldShowRequestPermissionRationale(
                                            READ_EXTERNAL_STORAGE)) {
                                        System.out.println("LALU2");

                                        // Show an explanation to the user *asynchronously* -- don't block
                                        // this thread waiting for the user's response! After the user
                                        // sees the explanation, try again to request the permission.
                                        Snackbar.make(v, "Write external storage permission is needed to proceed with download",
                                                Snackbar.LENGTH_INDEFINITE)
                                                .setAction("OK", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        requestPermissions(
                                                                new String[]{READ_EXTERNAL_STORAGE, ACCESS_NETWORK_STATE},
                                                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                                    }
                                                })
                                                .show();
                                    } else {
                                        System.out.println("LALU3");

                                        // No explanation needed, we can request the permission.
                                        requestPermissions(new String[]{READ_EXTERNAL_STORAGE, ACCESS_NETWORK_STATE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                        // app-defined int constant. The callback method gets the
                                        // result of the request.
                                    }
                                    System.out.println("LALU4");

                                }
                            } else {
                                DownloadManager mManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                                Uri downloadUri = Uri.parse("http://poplook.com/themes/poplook/pdf/POPLOOK_generic_returnform.pdf");
                                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                                request.setTitle("POPLOOK");
                                request.setDescription("Downloading Poplook Return Form");
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Poplook_Return_Form.pdf");
                                mManager.enqueue(request);
//                                Intent intent = new Intent();
//                                intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
//                                startActivity(intent);

                            }
                        }
                    });


                    mBookends.addHeader(header);

                    for (int i = 0; i < listArray_voucher.size(); i++) {
                        RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view, mRecyclerView, false);

                        TextView voucherCodeNameTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV);
                        TextView voucherCodePriceLblTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceLblTV);
                        TextView voucherCodePriceTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceTV);
                        ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode.findViewById(R.id.voucherCodeRemove);

                        voucherCodeRemove.setVisibility(View.GONE);

                        voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                        voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                        voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

                        voucherCodeNameTV.setText("Code - " + listArray_voucher.get(i).getvoucherCode());
                        voucherCodePriceTV.setText(listArray_voucher.get(i).getvoucherReduceAmount());
                        if (listArray_voucher.get(0).getvoucherCode().equals("") || listArray_voucher.get(0).getvoucherReduceAmount().equals("0.00")) {

                        } else {
                            mBookends.addFooter(footerVoucherCode);
                        }
                    }

                    if (carrier_price.equals("0.00")) {
                        mBookends.addFooter(footerShipping);
                    } else {
                        shippingMethodTVbill1.setVisibility(View.VISIBLE);
                        addressfooterRM1.setVisibility(View.VISIBLE);
                        shippingMethodTV.setVisibility(View.GONE);
                        shippingMethodTVbill1.setText(carrier_price);
                        mBookends.addFooter(footerShipping);

                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                        SelectedShopID = pref.getString("SelectedShopID", "1");

                        if (SelectedShopID.equalsIgnoreCase("1")) {
                            addressfooterRM1.setText("RM");
                        } else if (SelectedShopID.equalsIgnoreCase("2")) {
                            addressfooterRM1.setText("SGD");
                        } else if (SelectedShopID.equalsIgnoreCase("3")) {
                            addressfooterRM1.setText("USD");
                        }

                    }

                    mBookends.addFooter(footer);

                    mRecyclerView.setAdapter(mBookends);

                }


            } catch (Exception e) {

            }

        } else {

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    DownloadManager mManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                    Uri downloadUri = Uri.parse("http://poplook.com/themes/poplook/pdf/POPLOOK_generic_returnform.pdf");
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setTitle("POPLOOK");
                    request.setDescription("Downloading Poplook Return Form");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Poplook_Return_Form.pdf");
                    mManager.enqueue(request);

//                    Intent intent = new Intent();
//                    intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
//                    startActivity(intent);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(getView(), "Permission denied",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("Approve", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    requestPermissions(
                                            new String[]{READ_EXTERNAL_STORAGE, ACCESS_NETWORK_STATE},
                                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                }
                            })
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void logPurchasedEvent(int numItems, String contentType, String contentId, String currency, double price) {

        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, numItems);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logPurchase(BigDecimal.valueOf(price), Currency.getInstance(currency), params);
    }
}
