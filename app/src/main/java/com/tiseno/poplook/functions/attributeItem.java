package com.tiseno.poplook.functions;

/**
 * Created by rahn on 9/28/15.
 */
public class attributeItem {

    String attributeID, attributeName;

    public attributeItem(String attributeID, String attributeName) {
        super();
        this.attributeID = attributeID;
        this.attributeName = attributeName;
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

}

