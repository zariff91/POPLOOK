package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/11/15.
 */
public class loyaltyPointsOrderItem {
    String orderID, orderDate, orderStatus, loyaltyPoints, AvailableForConvert;

    public loyaltyPointsOrderItem(String orderID,String orderDate, String orderStatus, String loyaltyPoints, String AvailableForConvert) {
        super();
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.loyaltyPoints = loyaltyPoints;
        this.AvailableForConvert = AvailableForConvert;
    }

    public String getorderID()
    {
        return orderID;
    }

    public void setorderID(String orderID)
    {
        this.orderID = orderID;
    }


    public String getorderDate() {
        return orderDate;
    }
    public void setorderDate(String orderDate) {
        this.orderDate = orderDate;
    }


    public String getorderStatus() {
        return orderStatus;
    }
    public void setorderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }



    public String getloyaltyPoints()
    {
        return loyaltyPoints;
    }

    public void setloyaltyPoints(String loyaltyPoints)
    {
        this.loyaltyPoints = loyaltyPoints;
    }


    public String getAvailableForConvert()
    {
        return AvailableForConvert;
    }

    public void setAvailableForConvert(String AvailableForConvert)
    {
        this.AvailableForConvert = AvailableForConvert;
    }

}
