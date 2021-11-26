package com.tiseno.poplook.functions;

/**
 * Created by rahn on 9/28/15.
 */
public class attributeItem {

    String attributeID, attributeName;
    int quantity;

    public attributeItem(String attributeID, String attributeName, int quantity) {
        super();
        this.attributeID = attributeID;
        this.attributeName = attributeName;
        this.quantity = quantity;
    }

    public String getattributeID() {
        return attributeID;
    }
    public void setattributeID(String attributeID) {
        this.attributeID = attributeID;
    }

    public String getattributeName() {
        return attributeName;
    }
    public void setattributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getQuantity(){ return quantity;}

}

