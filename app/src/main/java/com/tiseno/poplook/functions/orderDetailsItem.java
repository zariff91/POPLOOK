package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/10/15.
 */
public class orderDetailsItem {

    String productID, productImg, productName, productSize, productRefNo, productUnitPrice, productTotalPrice, productQuantity;

    public orderDetailsItem(String productID, String productImg, String productName, String productSize, String productRefNo, String productUnitPrice, String productTotalPrice, String productQuantity) {
        super();
        this.productID = productID;
        this.productImg = productImg;
        this.productName = productName;
        this.productSize = productSize;
        this.productRefNo = productRefNo;
        this.productUnitPrice = productUnitPrice;
        this.productTotalPrice = productTotalPrice;
        this.productQuantity = productQuantity;

    }

    public String getproductID() {
        return productID;
    }

    public void setproductID(String productID) {
        this.productID = productID;
    }


    public String getproductImg() {
        return productImg;
    }

    public void setproductImg(String productImg) {
        this.productImg = productImg;
    }


    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }


    public String getproductSize() {
        return productSize;
    }

    public void setproductSize(String productSize) {
        this.productSize = productSize;
    }


    public String getproductRefNo() {
        return productRefNo;
    }

    public void setproductRefNo(String productRefNo) {
        this.productRefNo = productRefNo;
    }


    public String getproductUnitPrice() {
        return productUnitPrice;
    }

    public void setproductUnitPrice(String productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }


    public String getproductTotalPrice() {
        return productTotalPrice;
    }

    public void setproductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }


    public String getproductQuantity() {
        return productQuantity;
    }

    public void setproductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }
}

