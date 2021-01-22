package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/18/15.
 */
public class voucherItem {

    String voucherCode, voucherName,voucherReduceAmount,voucherID;


    public voucherItem(String voucherID,String voucherName,String voucherCode, String reduction_amount) {
        super();
        this.voucherID=voucherID;
        this.voucherName=voucherName;
        this.voucherCode = voucherCode;
        this.voucherReduceAmount = reduction_amount;

    }

    public String getvoucherID() {
        return voucherID;
    }

    public void setvoucherID(String voucherID) {
        this.voucherID = voucherID;
    }

    public String getvoucherName() {
        return voucherName;
    }

    public void setvoucherName(String voucherName) {
        this.voucherName = voucherName;
    }


    public String getvoucherCode() {
        return voucherCode;
    }

    public void setvoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }



    public String getvoucherReduceAmount() {
        return voucherReduceAmount;
    }

    public void setvoucherReduceAmount(String voucherReduceAmount) {
        this.voucherReduceAmount = voucherReduceAmount;
    }



}

