package com.tiseno.poplook.functions;

import org.json.JSONArray;

/**
 * Created by rahn on 9/17/15.
 */
public class ProductListItem {

    String productID,out_of_stock;
    String name,reference,image_url;
    String tax_rate,price_with_tax,price_with_discount,total_colours,id_product_attribute,price_Without_reduction,discount_text;
    JSONArray related_colour_data;
    String get_collection_name,online_exclusive,discount_label;
    public ProductListItem(String productID, String name, String reference, String image_url, String tax_rate,String price_with_tax,String price_with_discount, String out_of_stock, String total_colours,String id_product_attribute, JSONArray related_colour_data, String get_collection_name, String online_exclusive , String discount_label, String price_Without_reduction, String discount_text) {
        super();
        this.productID = productID;
        this.name = name;
        this.reference = reference;
        this.image_url = image_url;
        this.tax_rate = tax_rate;
        this.price_with_tax=price_with_tax;
        this.price_with_discount=price_with_discount;
        this.out_of_stock=out_of_stock;
        this.total_colours=total_colours;
        this.id_product_attribute=id_product_attribute;
        this.related_colour_data=related_colour_data;
        this.get_collection_name=get_collection_name;
        this.online_exclusive=online_exclusive;
        this.discount_label=discount_label;
        this.price_Without_reduction = price_Without_reduction;
        this.discount_text = discount_text;

    }

    public String getproductID() {
        return productID;
    }

    public void setproductID(String productID) {
        this.productID = productID;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getreference() {
        return reference;
    }

    public void setreference(String reference) {
        this.reference = reference;
    }

    public String getimage_url() {
        return image_url;
    }

    public void setimage_url(String image_url) {
        this.image_url = image_url;
    }

    public String gettax_rate() {
        return tax_rate;
    }

    public void settax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }


    public String getprice_with_tax() {
        return price_with_tax;
    }

    public void setprice_with_tax(String price_with_tax) {
        this.price_with_tax = price_with_tax;
    }

    public String getprice_with_discount() {
        return price_with_discount;
    }

    public void setprice_with_discount(String price_with_discount) {
        this.price_with_discount = price_with_discount;
    }

    public String getout_of_stock() {
        return out_of_stock;
    }

    public void setout_of_stock(String out_of_stock) {
        this.out_of_stock = out_of_stock;
    }

    public String getTotal_colours() {
        return total_colours;
    }

    public void setTotal_colours(String total_colours) {
        this.total_colours = total_colours;
    }

    public String getId_product_attribute() {
        return id_product_attribute;
    }

    public void setId_product_attribute(String id_product_attribute) {
        this.id_product_attribute = id_product_attribute;
    }

    public JSONArray getRelated_colour_data() {
        return related_colour_data;
    }

    public void setRelated_colour_data(JSONArray related_colour_data) {
        this.related_colour_data = related_colour_data;
    }

    public String getCollectionName() {
        return get_collection_name;
    }
    public String getOnline_exclusive() {
        return online_exclusive;
    }

    public String getDiscount() {
        return discount_label;
    }
    public String getPrice_Without_reduction() {
        return price_Without_reduction;
    }
    public String getDiscount_text() {
        return discount_text;
    }


}


