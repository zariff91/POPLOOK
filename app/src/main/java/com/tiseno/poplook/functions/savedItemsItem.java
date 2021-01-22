package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/9/15.
 */
public class savedItemsItem {
    String productID, productName, productSize, productPrice, productImage, productAttributeID, wishlistID, productAvailableQuantity, productExpiresIn;

    public savedItemsItem(String productID,String productName, String productSize, String productPrice, String productImage, String productAttributeID, String wishlistID, String productAvailableQuantity, String productExpiresIn) {
        super();
        this.productID = productID;
        this.productName = productName;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productAttributeID = productAttributeID;
        this.wishlistID = wishlistID;
        this.productAvailableQuantity = productAvailableQuantity;
        this.productExpiresIn = productExpiresIn;
    }

    public String getproductID()
    {
        return productID;
    }

    public void setproductID(String productID)
    {
        this.productID = productID;
    }

    public String getproductName()
    {
        return productName;
    }
    public void setproductName(String productName)
    {
        this.productName = productName;
    }


    public String getproductSize() {
        return productSize;
    }
    public void setproductSize(String productSize) {
        this.productSize = productSize;
    }


    public String getproductPrice() {
        return productPrice;
    }
    public void setproductPrice(String productPrice) {
        this.productPrice = productPrice;
    }



    public String getproductImage()
    {
        return productImage;
    }

    public void setproductImage(String productImage)
    {
        this.productImage = productImage;
    }

    public String getproductAttributeID()
    {
        return productAttributeID;
    }

    public void setproductAttributeID(String productAttributeID)
    {
        this.productAttributeID = productAttributeID;
    }

    public String getwishlistID()
    {
        return wishlistID;
    }

    public void setwishlistID(String wishlistID)
    {
        this.wishlistID = wishlistID;
    }

    public String getProductAvailableQuantity(){ return productAvailableQuantity; }

    public void setProductAvailableQuantity(String productAvailableQuantity){ this.productAvailableQuantity = productAvailableQuantity; }

    public String getProductExpiresIn(){ return productExpiresIn; }

    public void setProductExpiresIn(String productExpiresIn){ this.productExpiresIn = productExpiresIn; }

}
