package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/9/15.
 */
public class myAddressItem {
    String addressType, addressID, address, addressContactNo;

    public myAddressItem(String addressID,String addressType, String address, String addressContactNo) {
        super();
        this.addressID = addressID;
        this.addressType = addressType;
        this.address = address;
        this.addressContactNo = addressContactNo;
    }

    public String getaddressID()
    {
        return addressID;
    }

    public void setaddressID(String addressID)
    {
        this.addressID = addressID;
    }


    public String getaddressType() {
        return addressType;
    }
    public void setaddressType(String addressType) {
        this.addressType = addressType;
    }


    public String getaddress() {
        return address;
    }
    public void setaddress(String address) {
        this.address = address;
    }



    public String getaddressContactNo()
    {
        return addressContactNo;
    }

    public void setaddressContactNo(String addressContactNo)
    {
        this.addressContactNo = addressContactNo;
    }


}
