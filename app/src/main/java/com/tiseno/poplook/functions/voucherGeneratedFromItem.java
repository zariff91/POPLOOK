package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/28/15.
 */
public class voucherGeneratedFromItem {

    String orderID, points;


    public voucherGeneratedFromItem(String orderID, String points) {
        super();
        this.orderID = orderID;
        this.points = points;
    }

    public String getorderID() {
        return orderID;
    }

    public void setorderID(String orderID) {
        this.orderID = orderID;
    }



    public String getpoints() {
        return points;
    }

    public void setpoints(String points) {
        this.points = points;
    }
}

