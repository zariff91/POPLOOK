package com.tiseno.poplook.functions;

/**
 * Created by DEV POPLOOK 2 on 5/5/2016.
 */
public class countryShopChooserItem{

    String id_country,country_name,country_iso_code,id_shop,shop_url,currency_sign;
    Boolean IsHeader;

    public countryShopChooserItem(String id_country, String country_name, String country_iso_code,String id_shop,String shop_url,String currency_sign, Boolean isHeader) {
        super();
        this.id_country = id_country;
        this.country_name = country_name;
        this.country_iso_code = country_iso_code;
        this.id_shop = id_shop;
        this.shop_url = shop_url;
        this.currency_sign=currency_sign;
        this.IsHeader = isHeader;
    }

    public String getId_country() {
        return id_country;
    }

    public void setId_country(String id_country) {
        this.id_country = id_country;
    }


    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_iso_code() {
        return country_iso_code;
    }

    public void setCountry_iso_code(String country_iso_code) {
        this.country_iso_code = country_iso_code;
    }

    public String getId_shop() {
        return id_shop;
    }

    public void setId_shop(String id_shop) {
        this.id_shop = id_shop;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }

    public String getCurrency_sign() {
        return currency_sign;
    }

    public void setCurrency_sign(String currency_sign) {
        this.currency_sign = currency_sign;
    }

    public boolean getIsHeader() {
        return IsHeader;
    }

    public void setIsHeader(boolean IsHeader) {
        this.IsHeader = IsHeader;
    }

}