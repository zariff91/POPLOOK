package com.tiseno.poplook;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tiseno.poplook.functions.CustomGridWishlist;
import com.tiseno.poplook.functions.GridBookends;
import com.tiseno.poplook.functions.EndlessScrollListenerLinearLayout;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.MyLinearLayoutManager;
import com.tiseno.poplook.functions.savedItemsAdapter;
import com.tiseno.poplook.functions.savedItemsItem;
import com.tiseno.poplook.functions.shoppingBagAdapter;
import com.tiseno.poplook.functions.shoppingBagItem;
import com.tiseno.poplook.functions.voucherItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessDelete;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;
import com.useinsider.insider.Insider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class NewCartAndWishlistFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    String SelectedShopID,SelectedCountryCurrency;

    String UserID, CartID, LanguageID;

    TextView emptyTV;

    RecyclerView mRecyclerView;
    GridLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    RecyclerView mRecyclerViewCart;
    RecyclerView.LayoutManager mLayoutManagerCart;
    RecyclerView.Adapter sbAdapter;

    Button cartBtn,wishlistBtn;
    TextView closeBtn;

    ArrayList<savedItemsItem> listArray = new ArrayList<savedItemsItem>();

    JSONObject cartResultJObj,cartResultJObj1;

    String shippingPrice,totalItemInBag;
    String totalPrice="",taxAmount,totalPriceWt;
    String oriGiftOptionsText = "";
    String oriLeaveMessageText = "";
    Boolean haveOutOfStock = false;


    ImageView giftOptionsNOIV,giftOptionsYESIV, leaveMsgNOIV,leaveMsgYESIV, giftOptionsPopOutCloseIV, giftOptionsIV;
    TextView giftOptionsNOTV, giftOptionsYESTV, giftOptionsNoOfCharTV, leaveMsgNOTV, leaveMsgYESTV;
    EditText giftOptionsET, leaveMsgET;

    TextView codeTV,codeApplyTV,giftOptionsTV,leaveMsgTV,shoppingBarNextBtnTV;
    TextView totalPriceTV,totalPayableTV,totalPayableRMTV,shippingTVCart,shippingRMTVCart;
    TextView shoppingBagEmptyTV,additionalTopViewText,additionalTopViewText2,additionalTopViewText3,totalPriceRMTV;

    RelativeLayout giftBarRL, leaveMsgBarRL, giftOptionsPopOutRL;


    ImageButton shoppingBarNextBtnIB, codeApplyIB;

    EditText codeET;

    Boolean IsGiftOptionsEnabled = false;
    Boolean IsLeaveMSGEnabled = false;

    float totalAllProductPrice;
    float totalVoucherPrice = 0;

    int pos=0;

    String openWishlist = "0";


    ArrayList<shoppingBagItem> listArray_shoppingBag = new ArrayList<shoppingBagItem>();
    ArrayList<JSONObject> listArray_outofstockitem = new ArrayList<JSONObject>();
    ArrayList<voucherItem> listArray_voucher = new ArrayList<voucherItem>();
    ArrayList<voucherItem> listArray_store_credit = new ArrayList<voucherItem>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.new_cart_layout, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        Bundle bundle = this.getArguments();
        openWishlist = bundle.getString("forWishlist", "0");

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID", "");
        CartID = pref.getString("CartID", "0");
        LanguageID = pref.getString("LanguageID", "");

        SelectedShopID  = pref.getString("SelectedShopID", "1");
        SelectedCountryCurrency  = pref.getString("SelectedCountryCurrency", "RM");

        //Branch created

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.itemListRV);

        mRecyclerViewCart = (RecyclerView) contentView.findViewById(R.id.itemListcartRV);
        mLayoutManagerCart = new LinearLayoutManager(getActivity());
        mRecyclerViewCart.setLayoutManager(mLayoutManagerCart);

        cartBtn = (Button) contentView.findViewById(R.id.cartButton);
        wishlistBtn = (Button) contentView.findViewById(R.id.wishlistBtn);
        closeBtn = (TextView) contentView.findViewById(R.id.closeText);
        emptyTV = (TextView) contentView.findViewById(R.id.emptyText);

        emptyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));


        cartBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        wishlistBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        closeBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getActivity().onBackPressed();
            }
        });


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUserCartList();

                wishlistBtn.setBackgroundColor(Color.WHITE);
                wishlistBtn.setTextColor(Color.BLACK);

                cartBtn.setBackgroundColor(Color.BLACK);
                cartBtn.setTextColor(Color.WHITE);

            }
        });

        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSavedItems();

                wishlistBtn.setBackgroundColor(Color.BLACK);
                wishlistBtn.setTextColor(Color.WHITE);

                cartBtn.setBackgroundColor(Color.WHITE);
                cartBtn.setTextColor(Color.BLACK);

            }
        });

        if(openWishlist.equals("1"))
        {
            wishlistBtn.setBackgroundColor(Color.BLACK);
            wishlistBtn.setTextColor(Color.WHITE);

            cartBtn.setBackgroundColor(Color.WHITE);
            cartBtn.setTextColor(Color.BLACK);

            getSavedItems();
        }

        else {
            getUserCartList();
        }



        return contentView;
    }

    public void getSavedItems()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Wishlists/list/customer/"+UserID+"/lang/"+LanguageID+"/sort_options/"+1+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }


    public void getUserCartList(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String loggedin= "0";
        if(UserID.equalsIgnoreCase("")){
            loggedin = "0";
        }
        else{
            loggedin = "1";
        }
        System.out.println("LOGGEDIN " +loggedin);
        String apikey =pref.getString("apikey","");
        String cartID="0";
        if (CartID.equalsIgnoreCase("")){
            cartID="0";
        }else{
            cartID=CartID;
        }
        String userID="0";
        if (UserID.equalsIgnoreCase("")){
            userID="0";
        }else{
            userID=UserID;
        }
        String action="Carts/cart/id/"+cartID+"/customer/"+userID+"/login/"+loggedin+"?apikey="+apikey;

        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("stephere 1 = " + result);

        if(result!=null) {
            try {

                if (result.getString("action").equals("Wishlists_list")) {

                    System.out.println("stephere 2 = " + result);

                    if (result.getBoolean("status")) {

                        listArray.clear();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject = result.getJSONObject("data");
                        String wishlistID = jsonObject.getString("id_wishlist");

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("WishlistID", wishlistID);
                        editor.apply();

                        JSONArray jsonArr = new JSONArray();
                        jsonArr = jsonObject.getJSONArray("product_list");

                        try{
                            if(jsonArr.length()==0){
                                emptyTV.setText("No item in your wishlist");emptyTV.setVisibility(View.VISIBLE);
                                emptyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                                emptyTV.setVisibility(View.VISIBLE);

                                mRecyclerView.setVisibility(View.INVISIBLE);
                                mRecyclerViewCart.setVisibility(View.INVISIBLE);

                            }else{
                                emptyTV.setVisibility(View.INVISIBLE);
                                emptyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));

                                mRecyclerView.setVisibility(View.VISIBLE);
                                mRecyclerViewCart.setVisibility(View.INVISIBLE);

                            }
                            editor.putString("wishlistItem", String.valueOf(jsonArr.length()));
                            editor.apply();
                            ((MainActivity) getActivity()).changeToolBarWishNotiText(String.valueOf(jsonArr.length()));
                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject jObj = jsonArr.getJSONObject(i);
                                String productID = jObj.getString("id_product");
                                String productName = jObj.getString("name");
                                String productSize = jObj.getString("attributes_small");
                                String productPrice = jObj.getString("price");
                                String productAttributeID = jObj.getString("id_product_attribute");
                                int productAvailableQuantity = jObj.getInt("available_quantity");
                                String productExpires = jObj.getString("no_of_days");
                                String productImage = jObj.getString("image_url");

                                listArray.add(new savedItemsItem(productID, productName, productSize, productPrice, productImage, productAttributeID, wishlistID, String.valueOf(productAvailableQuantity), productExpires));
                            }

//                            RVAdapter = new savedItemsAdapter(getActivity(),SavedItemsFragment.this, listArray);
//                            RVAdapter = new savedItemsAdapter(getActivity(),SavedItemsFragment.this, listArray);

                            mLayoutManager = new GridLayoutManager(getActivity(), 2);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            mAdapter = new CustomGridWishlist(getActivity(),this,listArray);
                            mAdapter.notifyDataSetChanged();
                            final GridBookends mBookends = new GridBookends(mAdapter, mLayoutManager);
                            mRecyclerView.setAdapter(mBookends);
                            mRecyclerView.setVisibility(View.VISIBLE);

                        }catch (Exception e){
//                            emptyRL.setVisibility(View.VISIBLE);
//                            textViewSaja.setText("Your saved list is empty");
//                            textViewSaja.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                        }
                    }

                    else {

                        emptyTV.setText("No item in your wishlist");emptyTV.setVisibility(View.VISIBLE);
                        emptyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
                        emptyTV.setVisibility(View.VISIBLE);

                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mRecyclerViewCart.setVisibility(View.INVISIBLE);

                    }
                }





                if (result.getString("action").equals("Carts_cart")) {

                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    listArray_shoppingBag.clear();
                    totalAllProductPrice = 0;
                    int totalquantity=0;

                    if(result.getBoolean("status"))
                    {
                        cartResultJObj = result;

                        System.out.println("stephere 3 = " + result);


                        JSONObject data = new JSONObject();
                        data=result.getJSONObject("data");
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = data.getJSONArray("product_list");
                        CartID = data.getString("id_cart");

                        if(jsonArr.length() == 0)
                        {
                            emptyTV.setText("No item in your cart.");
                            emptyTV.setVisibility(View.VISIBLE);

                            mRecyclerView.setVisibility(View.INVISIBLE);
                            mRecyclerViewCart.setVisibility(View.INVISIBLE);
                        }

                        else {
                            emptyTV.setVisibility(View.INVISIBLE);

                            mRecyclerView.setVisibility(View.INVISIBLE);
                            mRecyclerViewCart.setVisibility(View.VISIBLE);


                        }

//                        if(data.has("bottom_message"))
//                        {
//                            bottomText = data.getString("bottom_message");
//                        }
//
//                        else
//                        {
//                            bottomText = "1";
//                        }
//
//
//                        CartMessages = data.getString("message_type");
//
//                        if(CartMessages.equals("alert-success"))
//                        {
//
//                            if(data.getString("cart_messages").equals(""))
//                            {
//                            }
//                            else {
//
//                                cartTopMessage = data.getJSONArray("cart_messages");
//
//                            }
//
//                        }

                        totalItemInBag = data.getString("totalItemInCart");
                        shippingPrice = data.getString("shipping_price");

                        editor.putString("CartID", CartID);
                        totalPriceWt = data.getString("totalProductsWt");
                        totalPrice = data.getString("totalPrice");
                        taxAmount = data.getString("taxCost");

                        System.out.println("stephere 4 = " + result);

                        for(int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jObj = jsonArr.getJSONObject(i);
                            String productAttributeID = jObj.getString("id_product_attribute");
                            String productID = jObj.getString("id_product");
                            String productName = jObj.getString("name");
                            String productSize = jObj.getString("attributes_small");
                            if(productSize.equals("")){
                                productSize="-";
                            }
                            String price = jObj.getString("price");
                            String discountedPrice = jObj.getString("price");
                            String imageURL = jObj.getString("image_url");
                            String timeEnd = jObj.getString("time_end");
                            String timeRem = jObj.getString("time_remainder");
                            String productRef = jObj.getString("reference");
                            String quantity = jObj.getString("quantity");
                            String item_total = jObj.getString("total");
                            Integer quantity_available = jObj.getInt("quantity_available");
//
                            if(quantity_available < 1)
                            {
                                haveOutOfStock = true;
                                listArray_outofstockitem.add(jObj);
                            }

                            int tot=Integer.parseInt(quantity);
                            totalquantity+=tot;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            Date d1 = format.parse(current_date);
                            Date d2 = format.parse(timeEnd);
                            System.out.println("current date is " + current_date);
                            System.out.println("current timeEnd is " + timeEnd);


                            System.out.println("current timeRem is " + timeRem);
                            //in milliseconds
                            long diff = d2.getTime() - d1.getTime();
                            System.out.println("current diff is " + diff);

//                            if(diff > 0)
//                            {
                            if(!discountedPrice.equals("0"))
                            {
                                price = discountedPrice;
                            }

                            price = String.format("%.2f", Float.parseFloat(price));


                            String totalPrice = String.valueOf(Float.parseFloat(price) * Integer.parseInt(quantity));

                            totalAllProductPrice += Float.parseFloat(price) * Integer.parseInt(quantity);


                            listArray_shoppingBag.add(new shoppingBagItem(CartID,productAttributeID,productID,imageURL,productName,productSize,productRef,price,item_total,quantity,timeRem,quantity_available));

                            System.out.println("stepheree = " + result);

//                            }


                        }
                        ((MainActivity) getActivity()).changeToolBarBagNotiText(String.valueOf(totalquantity));

                        editor.putString("cartItem", String.valueOf(totalquantity));
                        editor.apply();
                        listArray_voucher.clear();



                        try {
                            JSONArray voucherArray = data.getJSONArray("voucher_list");
                            for (int j = 0; j < voucherArray.length(); j++) {
                                JSONObject jObj2 = voucherArray.getJSONObject(j);
                                String voucherID = jObj2.getString("id_discount");
                                String voucher_name= jObj2.getString("name");
                                String voucherCode = jObj2.getString("code");
                                String voucherPercentage = jObj2.getString("reduction_percent");
                                String voucherAmount = jObj2.getString("value_tax_exc");

//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                                listArray_voucher.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                            }
                        }catch (Exception e)
                        {

                        }

                        listArray_store_credit.clear();

                        try {
                            JSONArray voucherArray1 = data.getJSONArray("store_credit_list");
                            for (int j = 0; j < voucherArray1.length(); j++) {
                                JSONObject jObj2 = voucherArray1.getJSONObject(j);
                                String voucherID = jObj2.getString("id_discount");
                                String voucher_name= jObj2.getString("name");
                                String voucherCode = jObj2.getString("code");
                                String voucherPercentage = jObj2.getString("reduction_percent");
                                String voucherAmount = jObj2.getString("value_real");

//                                if (!voucherPercentage.equals("0.00")) {
//                                    float Percentage = Float.parseFloat(voucherPercentage);
//                                    float amount = totalAllProductPrice * (Percentage / 100);
//                                    voucherAmount=String.format("%.2f", amount);
//                                }

                                listArray_store_credit.add(new voucherItem(voucherID,voucher_name,voucherCode, voucherAmount));
                            }
                        }catch (Exception e)
                        {

                        }
                    }
                    else
                    {
                        ((MainActivity) getActivity()).changeToolBarBagNotiText("0");


                        totalItemInBag = "0";

                        editor.putString("cartItem", "0");
                        editor.apply();
                    }

                    refreshAll();


                }

                if(result.getString("action").equals("Carts_OrderStep1"))
                {
                    if(result.getBoolean("status"))
                    {

                        ((MainActivity) getActivity()).getSupportActionBar().hide();

                        cartResultJObj1 = result;
                        JSONObject data = new JSONObject();
                        data=result.getJSONObject("data");
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = data.getJSONArray("product_list");
                        totalPriceWt = data.getString("totalProductsWt");
                        totalPrice = data.getString("totalPrice");
                        taxAmount = data.getString("taxCost");


                        String nextPage = data.getString("next_page");

                        if(nextPage.equals("addressPage"))
                        {

                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();
//
//                            Fragment fragment = new AddressBillingFragment();
//                            FragmentManager fragmentManager = getActivity().getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                            fragmentTransaction.replace(R.id.fragmentContainer, fragment,"AddressBillingFragment");
//                            fragmentTransaction.addToBackStack(null);
//
//                            Bundle args=new Bundle();
//                            String cartResultJObjString=cartResultJObj1.toString();
//                            System.out.println("cartResultObj" + cartResultJObjString);
//                            args.putString("cartResultJObj", cartResultJObjString);
//                            fragment.setArguments(args);
//
//                            fragmentTransaction.commit();
//                            editor = pref.edit();
//                            editor.putString("NeedToGoBackToCart", "0");
//                            editor.apply();

                            Fragment fragment = new NewAddressBillingFragment();
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment,"NewAddressBillingFragment");
                            fragmentTransaction.addToBackStack(null);

                            Bundle args=new Bundle();
                            String cartResultJObjString=cartResultJObj1.toString();
                            System.out.println("cartResultObj" + cartResultJObjString);
                            args.putString("cartResultJObj", cartResultJObjString);
                            args.putString("FOR_BILLING","0");
                            fragment.setArguments(args);

                            fragmentTransaction.commit();

                            editor = pref.edit();
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
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
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
                    else
                    {
                        String message = result.getString("message");
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Message")
                                .setMessage("An item in your cart is no longer available in this quantity. You cannot proceed with your order until the quantity is adjusted")
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
                                        getUserCartList();
                                        getUserCartList();

                                    }
                                }).show();
                    }
                }




            }
            catch (Exception e)
            {

            }
        }
    }

    public void refreshAll()
    {

        if(totalItemInBag.equals("0"))
        {

            mRecyclerViewCart.setVisibility(View.INVISIBLE);

            System.out.println("stephere 7 = ");


            refreshRecyclerView();


        }
        else
        {
            mRecyclerViewCart.setVisibility(View.VISIBLE);
            refreshRecyclerView();

        }


    }













    public void refreshRecyclerView()
    {
        // Make Bookends

//        sbAdapter = new shoppingBagAdapter(getActivity(),listArray_shoppingBag,this, true);
//        final Bookends<RecyclerView.Adapter> mBookends = new Bookends<>(sbAdapter);
//
//        // Inflate footer view
//        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
//
//        RelativeLayout footerEarlyView = (RelativeLayout) inflater1.inflate(R.layout.shopping_bar_footer_first_view, mRecyclerViewCart, false);
////        final RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view, mRecyclerView, false);
//        RelativeLayout footer = (RelativeLayout) inflater1.inflate(R.layout.shopping_bar_footer_view, mRecyclerViewCart, false);
//
//        LinearLayout header = (LinearLayout) inflater1.inflate(R.layout.shopping_bar_header, mRecyclerViewCart, false);
//
////        additionalTopViewText = (TextView) header.findViewById(R.id.additionalTopViewTextheader);
////        additionalTopViewText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
////        additionalTopViewText2 = (TextView) header.findViewById(R.id.additionalTopViewText2header);
////        additionalTopViewText2.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
////        additionalTopViewText3 = (TextView) header.findViewById(R.id.additionalTopViewText3header);
////        additionalTopViewText3.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//
//        totalPriceRMTV = (TextView)footerEarlyView.findViewById(R.id.totalPriceRMTV);
////        totalPriceRM1TV = (TextView)footerEarlyView.findViewById(R.id.totalPriceRM1TV);
//        totalPriceTV = (TextView)footerEarlyView.findViewById(R.id.totalPriceTV);
//        shippingRMTVCart = (TextView)footerEarlyView.findViewById(R.id.shippingMethodTVlblCart);
//        shippingTVCart = (TextView)footerEarlyView.findViewById(R.id.shippingMethodTVCart);
//
//        totalPayableTV = (TextView)footer.findViewById(R.id.totalPayableTVCart);
//        totalPayableRMTV = (TextView)footer.findViewById(R.id.totalPayableRMTVCart);
//
//        totalPayableTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//        totalPayableRMTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//
//
////        bottomMessage = (TextView)footer.findViewById(R.id.bottomMessage);
//        giftBarRL = (RelativeLayout)footer.findViewById(R.id.giftBarRL);
//        giftOptionsNOIV = (ImageView)footer.findViewById(R.id.giftOptionsNOIV);
//        giftOptionsYESIV = (ImageView)footer.findViewById(R.id.giftOptionsYESIV);
//        giftOptionsNOTV = (TextView)footer.findViewById(R.id.giftOptionsNOTV);
//        giftOptionsYESTV = (TextView)footer.findViewById(R.id.giftOptionsYESTV);
//        giftOptionsET = (EditText)footer.findViewById(R.id.giftOptionsET);
//        giftOptionsNoOfCharTV = (TextView)footer.findViewById(R.id.giftOptionsNoOfCharTV);
//
//        leaveMsgBarRL = (RelativeLayout)footer.findViewById(R.id.leaveMsgRL);
//        leaveMsgNOIV = (ImageView)footer.findViewById(R.id.leaveMsgNOIV);
//        leaveMsgYESIV = (ImageView)footer.findViewById(R.id.leaveMsgYESIV);
//        leaveMsgNOTV = (TextView)footer.findViewById(R.id.leaveMsgNOTV);
//        leaveMsgYESTV = (TextView)footer.findViewById(R.id.leaveMsgYESTV);
//        leaveMsgET = (EditText)footer.findViewById(R.id.leaveMsgET);
//
//        giftOptionsIV = (ImageView)footer.findViewById(R.id.giftOptionsIV);
//        giftOptionsPopOutRL = (RelativeLayout) footer.findViewById(R.id.giftOptionsPopOutRL);
//        giftOptionsPopOutCloseIV = (ImageView) footer.findViewById(R.id.giftOptionsPopOutCloseIV);
//
//        shoppingBarNextBtnIB = (ImageButton) footer.findViewById(R.id.shoppingBarNextBtnIB);
//
//        codeApplyIB = (ImageButton) footer.findViewById(R.id.codeApplyIB);
//
//        codeTV = (TextView) footer.findViewById(R.id.codeTV);
//        codeApplyTV = (TextView) footer.findViewById(R.id.codeApplyTV);
//        giftOptionsTV = (TextView) footer.findViewById(R.id.giftOptionsTV);
//        leaveMsgTV = (TextView) footer.findViewById(R.id.leaveMsgTV);
//        shoppingBarNextBtnTV = (TextView) footer.findViewById(R.id.shoppingBarNextBtnTV);
//
//        codeET = (EditText) footer.findViewById(R.id.codeET);
//
//        totalPriceRMTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
////        bottomMessage.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//
//        totalPriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//
//        shippingTVCart.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//        shippingRMTVCart.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
//
////        if(CartMessages.equalsIgnoreCase("alert-success"))
////        {
////
////            try {
////
////                if(cartTopMessage.length() == 1)
////                {
////
////                    String firstText = cartTopMessage.getString(0);
////                    additionalTopViewText.setVisibility(View.VISIBLE);
////                    additionalTopViewText.setText(firstText);
////
////                    additionalTopViewText2.setVisibility(View.GONE);
////                    additionalTopViewText3.setVisibility(View.GONE);
////                }
////
////                if(cartTopMessage.length() == 2)
////                {
////
////                    String firstText = cartTopMessage.getString(0);
////                    String secondText = cartTopMessage.getString(1);
////
////                    additionalTopViewText.setVisibility(View.VISIBLE);
////                    additionalTopViewText.setText(firstText);
////
////                    additionalTopViewText2.setVisibility(View.VISIBLE);
////                    additionalTopViewText2.setText(secondText);
////
////                    additionalTopViewText3.setVisibility(View.GONE);
////                }
////
////                if(cartTopMessage.length() == 3)
////                {
////
////                    String firstText = cartTopMessage.getString(0);
////                    String scnText = cartTopMessage.getString(1);
////                    String thirdText = cartTopMessage.getString(2);
////
////
////                    additionalTopViewText.setVisibility(View.VISIBLE);
////                    additionalTopViewText.setText(firstText);
////                    additionalTopViewText2.setVisibility(View.VISIBLE);
////                    additionalTopViewText2.setText(scnText);
////                    additionalTopViewText3.setVisibility(View.VISIBLE);
////                    additionalTopViewText3.setText(thirdText);
////
////
////                }
////
////            }
////            catch (Exception e)
////            {
////
////            }
////
//////            String cartMessage = CartMessageContent.replace("[", "").replace("\"", "").replace("]", "");
//////            additionalTopViewText.setVisibility(View.VISIBLE);
//////            additionalTopViewText.setText(cartMessage);
////
////        }
//
//
//        mBookends.addHeader(header);
//
//        if(SelectedShopID.equals("1")){
//            totalPriceRMTV.setText("Subtotal");
//            totalPriceTV.setText(": RM " + totalPriceWt);
//        }else{
//            totalPriceRMTV.setText("Subtotal");
//            totalPriceTV.setText(": " +SelectedCountryCurrency+" "+ totalPriceWt);
//        }
//
//        if(shippingPrice.equals("0.00")){
//            shippingTVCart.setText("Free Shipping");
//
//        }else{
//            shippingTVCart.setText(": " +SelectedCountryCurrency+" "+ shippingPrice);
//        }
//
//
//        if (totalPrice.equalsIgnoreCase("0.00")){
//            totalPayableTV.setText(": " +SelectedCountryCurrency+" "+ "0.00");
//        }else if(!totalPrice.contains(".")) {
//            totalPayableTV.setText(": " +SelectedCountryCurrency+" " +totalPrice+".00");
//
//            float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
//            System.out.print("GST" + rounded);
//        }else if(Float.parseFloat(totalPrice)<0.00){
//            totalPayableTV.setText(": " +SelectedCountryCurrency+" "+ "0.00");
//        }else{
//            totalPrice=String.format("%.2f", Double.parseDouble(totalPrice));
//            totalPayableTV.setText(": " +SelectedCountryCurrency+" "+ totalPrice);
//        }
//
////        if(bottomText.equals("1"))
////        {
////            bottomMessage.setVisibility(View.GONE);
////        }
////
////        else {
////
////            bottomMessage.setText(bottomText);
////            bottomMessage.setVisibility(View.VISIBLE);
////        }
//
//        giftOptionsNoOfCharTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        codeTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        codeApplyTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        giftOptionsTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        leaveMsgTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        shoppingBarNextBtnTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
//        codeET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        giftOptionsET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//        leaveMsgET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//
//        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
//        String leaveMessage = pref.getString("LeaveMessage", "");
//        String giftMessage = pref.getString("giftMessage", "");
//
//        if(!leaveMessage.equals("")){
//            IsLeaveMSGEnabled = true;
//            leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//            leaveMsgNOIV.setImageResource(R.drawable.btn_check);
//            leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
//            leaveMsgET.setVisibility(View.VISIBLE);
//            leaveMsgET.setText(leaveMessage);
//        }else{
//            IsLeaveMSGEnabled = false;
//            leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
//            leaveMsgNOIV.setImageResource(R.drawable.btn_check_active);
//            leaveMsgYESIV.setImageResource(R.drawable.btn_check);
//            leaveMsgET.setVisibility(View.GONE);
//        }
//
//
//
//
//        giftOptionsNOTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsGiftOptionsEnabled = false;
//                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
//                giftOptionsNOIV.setImageResource(R.drawable.btn_check_active);
//                giftOptionsYESIV.setImageResource(R.drawable.btn_check);
//                giftOptionsET.setVisibility(View.GONE);
//                giftOptionsNoOfCharTV.setVisibility(View.GONE);
//            }
//        });
//
//        giftOptionsYESTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsGiftOptionsEnabled = true;
//                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//                giftOptionsNOIV.setImageResource(R.drawable.btn_check);
//                giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
//                giftOptionsET.setVisibility(View.VISIBLE);
//                giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);
//            }
//        });
//
//        giftOptionsNOIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsGiftOptionsEnabled = false;
//                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
//                giftOptionsNOIV.setImageResource(R.drawable.btn_check_active);
//                giftOptionsYESIV.setImageResource(R.drawable.btn_check);
//                giftOptionsET.setVisibility(View.GONE);
//                giftOptionsNoOfCharTV.setVisibility(View.GONE);
//            }
//        });
//
//        giftOptionsYESIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsGiftOptionsEnabled = true;
//                giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//                giftOptionsNOIV.setImageResource(R.drawable.btn_check);
//                giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
//                giftOptionsET.setVisibility(View.VISIBLE);
//                giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);
//            }
//        });
//
//        if(!giftMessage.equals("")){
//            IsGiftOptionsEnabled = true;
//            giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//            giftOptionsNOIV.setImageResource(R.drawable.btn_check);
//            giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
//            giftOptionsET.setVisibility(View.VISIBLE);
//            giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);
//            giftOptionsET.setText(giftMessage);
//        }else{
//            IsGiftOptionsEnabled = false;
//            giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
//            giftOptionsNOIV.setImageResource(R.drawable.btn_check_active);
//            giftOptionsYESIV.setImageResource(R.drawable.btn_check);
//            giftOptionsET.setVisibility(View.GONE);
//            giftOptionsNoOfCharTV.setVisibility(View.GONE);
//        }
//
//
//        leaveMsgNOTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsLeaveMSGEnabled = false;
//                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
//                leaveMsgNOIV.setImageResource(R.drawable.btn_check_active);
//                leaveMsgYESIV.setImageResource(R.drawable.btn_check);
//                leaveMsgET.setVisibility(View.GONE);
//            }
//        });
//
//        leaveMsgYESTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsLeaveMSGEnabled = true;
//                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//                leaveMsgNOIV.setImageResource(R.drawable.btn_check);
//                leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
//                leaveMsgET.setVisibility(View.VISIBLE);
//            }
//        });
//
//        leaveMsgNOIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsLeaveMSGEnabled = false;
//                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1));
//                leaveMsgNOIV.setImageResource(R.drawable.btn_check_active);
//                leaveMsgYESIV.setImageResource(R.drawable.btn_check);
//                leaveMsgET.setVisibility(View.GONE);
//            }
//        });
//
//        leaveMsgYESIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IsLeaveMSGEnabled = true;
//                leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//                leaveMsgNOIV.setImageResource(R.drawable.btn_check);
//                leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
//                leaveMsgET.setVisibility(View.VISIBLE);
//            }
//        });
//
//        giftOptionsIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                giftOptionsPopOutRL.setVisibility(View.VISIBLE);
//            }
//        });
//
//        giftOptionsPopOutCloseIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                giftOptionsPopOutRL.setVisibility(View.GONE);
//            }
//        });
//        String NeedToGoBackToCart = pref.getString("NeedToGoBackToCart","0");
//
//        shoppingBarNextBtnIB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                goToNextStepWS(giftOptionsET.getText().toString(),leaveMsgET.getText().toString());
//
//            }
//        });
//
//        // Attach footer view
//        mBookends.addFooter(footerEarlyView);
//
//        for(int i = 0; i <listArray_voucher.size(); i++)
//        {   pos=i;
//            final voucherItem item=listArray_voucher.get(i);
//            RelativeLayout footerVoucherCode = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view_shoppingbag, mRecyclerViewCart, false);
//
//            TextView voucherCodeNameTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV);
//            TextView voucherCodeNameTV1 = (TextView) footerVoucherCode.findViewById(R.id.voucherCodeNameTV1);
//            TextView voucherCodePriceLblTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceLblTV);
//            TextView voucherCodePriceTV = (TextView) footerVoucherCode.findViewById(R.id.voucherCodePriceTV);
//            ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode.findViewById(R.id.voucherCodeRemove);
//
//            voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodeNameTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodeRemove.setVisibility(View.VISIBLE);
//
//            voucherCodePriceLblTV.setText(SelectedCountryCurrency);
//            voucherCodeNameTV.setText("Code - " + listArray_voucher.get(pos).getvoucherCode());
//            voucherCodeNameTV1.setText(listArray_voucher.get(pos).getvoucherName());
//            voucherCodePriceTV.setText(listArray_voucher.get(pos).getvoucherReduceAmount());
//
//            voucherCodeRemove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(getActivity())
//                            .setTitle("Message")
//                            .setMessage("Remove Voucher Code?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    deleteVoucherFromCart(item.getvoucherID(), CartID);
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            })
//                            .show();
//
//
//                }
//            });
//            mBookends.addFooter(footerVoucherCode);
//        }
//
//        for(int i = 0; i <listArray_store_credit.size(); i++)
//        {   pos=i;
//            final voucherItem item=listArray_store_credit.get(i);
//            RelativeLayout footerVoucherCode1 = (RelativeLayout) inflater1.inflate(R.layout.voucher_code_footer_view_shoppingbag, mRecyclerViewCart, false);
//
//            TextView voucherCodeNameTV = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodeNameTV);
//            TextView voucherCodeNameTV1 = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodeNameTV1);
//            TextView voucherCodePriceLblTV = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodePriceLblTV);
//            TextView voucherCodePriceTV = (TextView) footerVoucherCode1.findViewById(R.id.voucherCodePriceTV);
//            ImageButton voucherCodeRemove = (ImageButton) footerVoucherCode1.findViewById(R.id.voucherCodeRemove);
//
//
////            TextView totalPayableTVsc = (TextView)footerVoucherCode1.findViewById(R.id.totalPayableTVCart);
////            TextView totalPayableRMTVsc = (TextView)footerVoucherCode1.findViewById(R.id.totalPayableRMTVCart);
//
////            totalPayableTVsc.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
////            totalPayableRMTVsc.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
////
//            voucherCodeNameTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodeNameTV1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodePriceLblTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodePriceTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
//            voucherCodeRemove.setVisibility(View.VISIBLE);
//
//            voucherCodePriceLblTV.setText(SelectedCountryCurrency);
//            voucherCodeNameTV.setText(listArray_store_credit.get(pos).getvoucherCode());
//            voucherCodeNameTV1.setText(listArray_store_credit.get(pos).getvoucherName());
//            voucherCodePriceTV.setText(listArray_store_credit.get(pos).getvoucherReduceAmount());
////
////            if (totalPrice.equalsIgnoreCase("0.00")){
////                totalPayableTVsc.setText(": RM 0.00");
////            }else if(!totalPrice.contains(".")) {
////                totalPayableTVsc.setText(": RM " +totalPrice+".00");
////
////                float rounded=(int)Math.round((totalAllProductPrice + (totalAllProductPrice * 0.06) - totalVoucherPrice) * 100) / 100;
////                System.out.print("GST" + rounded);
////            }else if(Float.parseFloat(totalPrice)<0.00){
////                totalPayableTVsc.setText(": RM 0.00");
////            }else{
////                totalPrice=String.format("%.2f", Double.parseDouble(totalPrice));
////                totalPayableTVsc.setText(": RM " + totalPrice);
////            }
//
//            voucherCodeRemove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(getActivity())
//                            .setTitle("Message")
//                            .setMessage("Remove Store Credit?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    deleteVoucherFromCart(item.getvoucherID(), CartID);
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            })
//                            .show();
//
//
//                }
//            });
//
//
//            mBookends.addFooter(footerVoucherCode1);
//        }
//
//        mBookends.addFooter(footer);
//
//        if(oriGiftOptionsText.length() > 0)
//        {
//            IsGiftOptionsEnabled = true;
//            giftBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//            giftOptionsNOIV.setImageResource(R.drawable.btn_check);
//            giftOptionsYESIV.setImageResource(R.drawable.btn_check_active);
//            giftOptionsET.setVisibility(View.VISIBLE);
//            giftOptionsNoOfCharTV.setVisibility(View.VISIBLE);
//
//            giftOptionsET.setText(oriGiftOptionsText);
//        }
//
//        if(oriLeaveMessageText.length() > 0)
//        {
//            IsLeaveMSGEnabled = true;
//            leaveMsgBarRL.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_extra_plain_1_active));
//            leaveMsgNOIV.setImageResource(R.drawable.btn_check);
//            leaveMsgYESIV.setImageResource(R.drawable.btn_check_active);
//            leaveMsgET.setVisibility(View.VISIBLE);
//
//            leaveMsgET.setText(oriLeaveMessageText);
//        }
//
//        mRecyclerViewCart.setAdapter(mBookends);
//
//        codeApplyIB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(totalPrice.equals("0.00")){
//                    Toast.makeText(getActivity(), "Your order has been fully paid", Toast.LENGTH_LONG).show();
//                }else if(codeET.getText().toString().equals("")){
//                    Toast.makeText(getActivity(), "You must enter a voucher code", Toast.LENGTH_LONG).show();
//                }else{
//                    applyVoucherWS(codeET.getText().toString(), UserID, CartID);
//                }
//
//
//                InputMethodManager inputManager = (InputMethodManager)
//                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
////                refreshAll();
//
////                oriGiftOptionsText = giftOptionsET.getText().toString();
////                oriLeaveMessageText = leaveMsgET.getText().toString();
////
//                refreshRecyclerView();
//
//            }
//        });
//
//        leaveMsgET.setMovementMethod(new ScrollingMovementMethod());
//
//        leaveMsgET.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                leaveMsgET.getParent().requestDisallowInterceptTouchEvent(true);
//
//                return false;
//            }
//        });
//
//        giftOptionsET.setMovementMethod(new ScrollingMovementMethod());
//
//        mRecyclerViewCart.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                giftOptionsET.getParent().requestDisallowInterceptTouchEvent(false);
//
//                return false;
//            }
//        });
//
//        giftOptionsET.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                giftOptionsET.getParent().requestDisallowInterceptTouchEvent(true);
//
//                return false;
//            }
//        });
//
//        giftOptionsET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                giftOptionsNoOfCharTV.setText(giftOptionsET.getText().length() + "/350");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        Insider.Instance.tagEvent("cart_visited");
    }



    private void goToNextStepWS(String giftMessage, String leaveMessage)
    {

        final String leaveMessageFinal = leaveMessage;
        final String giftMessageFinal = giftMessage;

        if(haveOutOfStock)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setTitle("Out of stock");
            builder1.setMessage("Some of the items have sold out. These items will be removed from cart.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Proceed",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            removeOutOfStockItem(giftMessageFinal, leaveMessageFinal);


                        }
                    });

            builder1.setNegativeButton(
                    "Dismiss",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
        else {

            proceedToNextStep(giftMessageFinal, leaveMessageFinal);

        }

    }

    private void proceedToNextStep(String giftMessage, String leaveMessage)
    {

        if(totalItemInBag.equals("0"))
        {

            System.out.println("Stay in page");

        }

        else {

            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
            String apikey =pref.getString("apikey","");
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("LeaveMessage", leaveMessage);
            editor.putString("giftMessage", giftMessage);
            editor.apply();

            String action="Carts/OrderStep1?apikey="+apikey+"&id_cart="+CartID+"&gift_message="+giftMessage;

            WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);

            callws.execute(action);

        }
    }

    private void removeOutOfStockItem(String giftMessage, String leaveMessage)
    {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");

        for(int i=0;i<listArray_outofstockitem.size();i++)
        {

            try {
                JSONObject obj = listArray_outofstockitem.get(i);

                String productAttributeID = obj.getString("id_product_attribute");
                String productID = obj.getString("id_product");

                String action="Carts/removeProduct";
                RequestBody formBody = new FormBody.Builder()
                        .add("apikey",apikey)
                        .add("id_cart",CartID)
                        .add("id_product", productID)
                        .add("id_product_attribute",productAttributeID)
                        .build();
                WebServiceAccessDelete callws = new WebServiceAccessDelete(getActivity(), this);
                callws.setAction(action);
                callws.execute(formBody);

            } catch (Exception e) {
                Log.e("Error", "unexpected JSON exception", e);
                // Do something to recover ... or kill the app.
            }



        }

        getUserCartList();

        proceedToNextStep(giftMessage, leaveMessage);


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

    private void applyVoucherWS(String voucherCode, String userID, String cartID){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID+"&mobile=1";
//        String action="Vouchers/validate?apikey="+apikey+"&cart="+cartID+"&code="+voucherCode+"&shop="+SelectedShopID;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

}
