package com.tiseno.poplook.functions;

/**
 * Created by rahn on 1/19/16.
 */
public class voucherGeneratedFromRVItem {

    String id_order,points,price;


    public voucherGeneratedFromRVItem(String id_order, String points, String price) {
        super();
        this.id_order = id_order;
        this.points = points;
        this.price = price;

    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPrice1() {
        return price;
    }

    public void setPrice1(String price) {
        this.price = price;
    }
}
