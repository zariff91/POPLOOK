package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/10/15.
 */
public class orderHistoryItem {
    String orderNo, orderDate, orderStatus, orderTrackingNo, orderDeliveryInfo, cartID, trackingNum, deliveryInfo;

    public orderHistoryItem(String orderNo,String orderDate, String orderStatus, String orderTrackingNo, String orderDeliveryInfo, String cartID, String trackingNum, String deliveryInfo) {
        super();
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderTrackingNo = orderTrackingNo;
        this.orderDeliveryInfo = orderDeliveryInfo;
        this.cartID = cartID;
        this.trackingNum = trackingNum;
        this.deliveryInfo = deliveryInfo;

    }

    public String getTrackingNo()
    {
        return trackingNum;
    }

    public String getDeliveyInfo()
    {
        return deliveryInfo;
    }

    public String getorderNo()
    {
        return orderNo;
    }

    public void setorderNo(String orderNo)
    {
        this.orderNo = orderNo;
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



    public String getorderTrackingNo()
    {
        return orderTrackingNo;
    }

    public void setorderTrackingNo(String orderTrackingNo)
    {
        this.orderTrackingNo = orderTrackingNo;
    }

    public String getorderDeliveryInfo()
    {
        return orderDeliveryInfo;
    }

    public void setorderDeliveryInfo(String orderDeliveryInfo)
    {
        this.orderDeliveryInfo = orderDeliveryInfo;
    }

    public String getcartID()
    {
        return cartID;
    }

    public void setcartID(String cartID)
    {
        this.cartID = cartID;
    }
}
