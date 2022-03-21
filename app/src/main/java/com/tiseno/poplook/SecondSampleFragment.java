package com.tiseno.poplook;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiseno.poplook.functions.GridViewProductAdapter;
import com.tiseno.poplook.functions.ProductListItem;
import com.tiseno.poplook.functions.attributeItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SecondSampleFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> ,GridViewProductAdapter.ItemClickListener{

    RecyclerView productListTable;
    GridLayoutManager mLayoutManager;
    GridViewProductAdapter mAdapter;

    ArrayList<ProductListItem> mProductObj = new ArrayList<ProductListItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_second_sample, container, false);
        productListTable = rootView.findViewById(R.id.secondFragmentRV);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        productListTable.setLayoutManager(mLayoutManager);

        getProductList(1);

        return rootView;
    }

    private void getProductList(int pageNum) {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey = pref.getString("apikey", "");
        //multishop
        //
        String action = "Products/category/id/295/shop/1/num_page/" + pageNum + "/sort_options/0?api_version=apps&apikey=" + apikey + "&tier=" + pref.getString("tier_level","") + "&user_id=";
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        if (result != null) {
            try {

                if (result.getBoolean("status")) {

                    if(result.getString("action").equals("Products_category")) {

                        JSONObject pagination = result.getJSONObject("pagination");
                        JSONArray productListArray = result.getJSONArray("data");

                        for (int j = 0; j < productListArray.length(); j++) {

                            JSONObject jObject = productListArray.getJSONObject(j);

                            String id_product = jObject.getString("id_product");
                            String name = jObject.getString("name");
                            String reference = jObject.getString("reference");
                            JSONArray image_url = jObject.getJSONArray("image_url");
                            String tax_rate = jObject.getString("tax_rate");
                            String price_with_tax = jObject.getString("price");
                            String reduction = jObject.getString("price_reduction");
                            String out_of_stock = jObject.getString("product_out_of_stock");
                            String total_colours = jObject.getString("total_colours");

                            String id_product_attribute = jObject.getString("id_product_attribute");
                            String get_coll_name = jObject.getString("collection_name");
                            String get_online_exclusive = jObject.getString("online_exclusive");
                            String get_discount = jObject.getString("discount_label");
                            String get_Price_withOut_Reduction = jObject.getString("price_without_reduction");
                            String get_discount_text = jObject.getString("discount_text");

                            Boolean isWishlist;

                            JSONArray related_colour_data = null;
                            try {
                                related_colour_data = jObject.getJSONArray("related_colour_data");

                            } catch (Exception e) {

                                System.out.println("false");

                            }


                            ArrayList<attributeItem> sizeArray = new ArrayList<>();

                            JSONArray size_array = null;
                            try {
                                size_array = jObject.getJSONArray("attribute_list");

                                for (int i = 0; i < size_array.length(); i++) {
                                    JSONObject attributeObj = size_array.getJSONObject(i);
                                    String attID = attributeObj.getString("id_product_attribute");
                                    String attName = attributeObj.getString("attribute_name");
                                    int attQuantity = attributeObj.getInt("quantity");

                                    sizeArray.add(new attributeItem(attID, attName, attQuantity));
                                }

                            } catch (Exception e) {

                                System.out.println("false");

                            }


                            mProductObj.add(new ProductListItem(id_product, name, reference, image_url.getString(0), tax_rate, price_with_tax, reduction, out_of_stock, total_colours, id_product_attribute, related_colour_data, get_coll_name, get_online_exclusive, get_discount, get_Price_withOut_Reduction, get_discount_text, sizeArray, false));
                        }

                        mAdapter = new GridViewProductAdapter(getActivity(), mProductObj, this,"RM");
                        productListTable.setAdapter(mAdapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWishlistClick(int position) {

    }
}