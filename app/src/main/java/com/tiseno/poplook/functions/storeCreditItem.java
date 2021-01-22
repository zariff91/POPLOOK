package com.tiseno.poplook.functions;

/**
 * Created by rahnrazamai on 18/10/2016.
 */

public class storeCreditItem {
    String voucherDate, voucherCode, voucherPrice, originalAmount;

    public storeCreditItem(String voucherCode,String voucherDate, String voucherPrice, String originalAmount) {
        super();
        this.voucherCode = voucherCode;
        this.voucherDate = voucherDate;
        this.voucherPrice = voucherPrice;
        this.originalAmount = originalAmount;

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

    public String getOriAmount() {
        return originalAmount;
    }
    public void setvoucherPrice(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }


}
