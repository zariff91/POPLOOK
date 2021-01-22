package com.tiseno.poplook.functions;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by billygoh on 9/11/15.
 */
public class loyaltyPointsVoucherItem {
    String voucherDate, voucherCode, voucherPrice, voucherAvailability;
    JSONArray voucherGeneratedFromJArray;

    public loyaltyPointsVoucherItem(String voucherCode,String voucherDate, String voucherPrice, String voucheravailability, JSONArray voucherGeneratedFromJArray) {
        super();
        this.voucherCode = voucherCode;
        this.voucherDate = voucherDate;
        this.voucherPrice = voucherPrice;
        this.voucherAvailability = voucheravailability;
        this.voucherGeneratedFromJArray = voucherGeneratedFromJArray;
    }

    public String getvoucherCode()
    {
        return voucherCode;
    }

    public void setvoucherCode(String voucherCode)
    {
        this.voucherCode = voucherCode;
    }


    public String getvoucherDate() {
        return voucherDate;
    }
    public void setvoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }


    public String getvoucherPrice() {
        return voucherPrice;
    }
    public void setvoucherPrice(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }


    public String getvoucherAvailability() {
        return voucherAvailability;
    }
    public void setvoucherAvailability(String voucherAvailability) {
        this.voucherAvailability = voucherAvailability;
    }


    public JSONArray getvoucherGeneratedFromJArray() {
        return voucherGeneratedFromJArray;
    }
    public void setvoucherGeneratedFromJArray(JSONArray voucherGeneratedFromJArray) {
        this.voucherGeneratedFromJArray = voucherGeneratedFromJArray;
    }
}
