package com.tiseno.poplook.functions;

/**
 * Created by rahn on 9/21/15.
 */
public class carrierItem {

    String carrierID, carrierName, carrierPrice;

    public carrierItem(String carrierID, String carrierName, String carrierPrice) {
        super();
        this.carrierID = carrierID;
        this.carrierName = carrierName;
        this.carrierPrice = carrierPrice;
    }

    public String getcarrierID() {
        return carrierID;
    }

    public void setcarrierID(String carrierID) {
        this.carrierID = carrierID;
    }


    public String getcarrierName() {
        return carrierName;
    }

    public void setcarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getcarrierPrice() {
        return carrierPrice;
    }

    public void setcarrierPrice(String carrierPrice) {
        this.carrierPrice = carrierPrice;
    }
}

