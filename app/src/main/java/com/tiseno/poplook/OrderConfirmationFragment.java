package com.tiseno.poplook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.shoppingBagAdapter;
import com.tiseno.poplook.functions.shoppingBagItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import hotchemi.android.rate.AppRate;

public class OrderConfirmationFragment extends Fragment  implements AsyncTaskCompleteListener<JSONObject> {

    RecyclerView orderSummaryRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<shoppingBagItem>();
    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();

    TextView orderDetailsTotalPayableTVorderConfirm;

    String id_cart,total_products, Order_id;

    String SelectedCountryCurrency;

    Button continueBtn,orderHistoryBtn;

    public OrderConfirmationFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_order_confirmation, container, false);

        orderSummaryRecyclerView = (RecyclerView) contentView.findViewById(R.id.orderConfirmationRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        orderSummaryRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");


        Order_id = pref.getString("order_success_id","");

        ((MainActivity) getActivity()).changeToolBarText("Order Confirmation");

        ((MainActivity) getActivity()).changeBtnSearchView(false);
        ((MainActivity) getActivity()).changeBtnBagView(false);
        ((MainActivity) getActivity()).changeBtnWishlistView(false);

        AppRate.with(getActivity().getApplicationContext()).showRateDialog(getActivity());

        getOrderDetails();

        Insider.Instance.tagEvent("confirmation_page_view").build();


        return contentView;

    }


//getArguments().getString("orderID")
    private void getOrderDetails()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Orders/order/id/"+Order_id+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }


    @Override
    public void onTaskComplete(JSONObject result) {
        if (result != null) {
            try {
                listArray_shoppingBag.clear();
                String action = result.getString("action");

                if (action.equals("Orders_order")) {
                    if (result.getBoolean("status")) {
                        JSONObject orderHistroyJObj = result.getJSONObject("data");

                        JSONObject orderDetailsObj = orderHistroyJObj.getJSONObject("order_details");
                        id_cart = orderDetailsObj.getString("id_cart");
                        total_products = orderDetailsObj.getString("total_paid");

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

                                listArray_shoppingBag.add(new shoppingBagItem(id_cart, id_product_attribute, prodID, image, productName, size, productRef, price, totalPrice, quantity, "", 1));

                                logPurchasedEvent(Integer.valueOf(quantity),"",prodID,SelectedCountryCurrency,Double.parseDouble(price));

                            }
                        }catch (Exception e){}

                        RVAdapter = new shoppingBagAdapter(getActivity(),listArray_shoppingBag,null, false);

                        Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(RVAdapter);

                        // Inflate footer view
                        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

                        RelativeLayout header = (RelativeLayout) inflater1.inflate(R.layout.fragment_order_confirmation_header, orderSummaryRecyclerView, false);
                        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.order_details_footer_view, orderSummaryRecyclerView, false);
                        orderDetailsTotalPayableTVorderConfirm = (TextView) footer.findViewById(R.id.orderDetailsTotalPayableTV);
                        orderDetailsTotalPayableTVorderConfirm.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));


                        if (total_products.equals("0.00")){
                            orderDetailsTotalPayableTVorderConfirm.setText("0.00");
                        }else {
                            orderDetailsTotalPayableTVorderConfirm.setText(total_products);
                        }

                        TextView firstLbl = (TextView) header.findViewById(R.id.firstLabel);
                        TextView secondLbl = (TextView) header.findViewById(R.id.secondLabel);
                        TextView thirdLbl = (TextView) header.findViewById(R.id.below_label);

                        continueBtn = (Button) header.findViewById(R.id.continueButton);
                        continueBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

                        continueBtn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Fragment fragment = new ProductListFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("prodID", "45");
                                bundle.putString("catName","What's New");
                                bundle.putString("fromHome", "Home");
                                fragment.setArguments(bundle);

                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                ((MainActivity) getActivity()).getSupportActionBar().show();

                            }

                        });

                        orderHistoryBtn = (Button) header.findViewById(R.id.orderHistoryButton);
                        orderHistoryBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));


                        orderHistoryBtn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Fragment fragment = new OrderHistoryFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "OrderHistoryFragment");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }

                        });

                        firstLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
                        secondLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                        thirdLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

                        secondLbl.setText("Your order #"+Order_id+" has been confirmed.");

                        mBookends.addHeader(header);
                        mBookends.addFooter(footer);

                        orderSummaryRecyclerView.setAdapter(mBookends);



                    }
                }
            }

            catch (Exception e){

            }
        }
    }

    public void logPurchasedEvent
            (int numItems, String contentType, String contentId, String currency, double price) {

        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, numItems);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logPurchase(BigDecimal.valueOf(price),Currency.getInstance(currency),params);
    }
}
