package com.tiseno.poplook.functions;

/**
 * Created by rahn on 9/18/15.
 */
public class StyleWithItem {


    String styleWithID,styleWithName;
    String styleWithPrice,styleWithDiscount,styleWithImage;


    public StyleWithItem(String styleWithID, String styleWithName, String styleWithPrice, String styleWithDiscount, String styleWithImage) {
        super();
        this.styleWithID = styleWithID;
        this.styleWithName = styleWithName;
        this.styleWithPrice = styleWithPrice;
        this.styleWithDiscount = styleWithDiscount;
        this.styleWithImage = styleWithImage;
    }

    public String getstyleWithID() {
        return styleWithID;
    }

    public void setstyleWithID(String styleWithID) {
        this.styleWithID = styleWithID;
    }


    public String getstyleWithName() {
        return styleWithName;
    }

    public void setstyleWithName(String styleWithName) {
        this.styleWithName = styleWithName;
    }

    public String getstyleWithPrice() {
        return styleWithPrice;
    }

    public void setstyleWithPrice(String styleWithPrice) {
        this.styleWithPrice = styleWithPrice;
    }

    public String getstyleWithDiscount() {
        return styleWithDiscount;
    }

    public void setstyleWithDiscount(String styleWithDiscount) {
        this.styleWithDiscount = styleWithDiscount;
    }

    public String getstyleWithImage() {
        return styleWithImage;
    }

    public void setstyleWithImage(String styleWithImage) {
        this.styleWithImage = styleWithImage;
    }



}




