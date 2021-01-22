package com.tiseno.poplook.functions;

/**
 * Created by rahn on 9/18/15.
 */
public class addressItem {
    String id_gender,company,addressID, addressFirstName, addressLastName, address1, address2, addressPostCode, addressCity, addressCountryID, addressCountry, addressStateID, addressState, addressPhone;

    public addressItem(String addressID,String addressFirstName, String addressLastName, String address1, String address2, String addressPostCode, String addressCity, String addressCountryID, String addressCountry, String addressStateID, String addressState, String addressPhone,String company, String id_gender) {
        super();
        this.addressID = addressID;
        this.addressFirstName = addressFirstName;
        this.addressLastName = addressLastName;
        this.address1 = address1;
        this.address2 = address2;
        this.addressPostCode = addressPostCode;
        this.addressCity = addressCity;
        this.addressCountryID = addressCountryID;
        this.addressCountry = addressCountry;
        this.addressStateID = addressStateID;
        this.addressState = addressState;
        this.addressPhone = addressPhone;
        this.company = company;
        this.id_gender = id_gender;

    }

    public String getaddressID()
    {
        return addressID;
    }

    public void setaddressID(String addressID)
    {
        this.addressID = addressID;
    }


    public String getaddressFirstName() {
        return addressFirstName;
    }

    public void setaddressFirstName(String addressFirstName) {
        this.addressFirstName = addressFirstName;
    }


    public String getaddressLastName() {
        return addressLastName;
    }

    public void setaddressLastName(String addressLastName) {
        this.addressLastName = addressLastName;
    }



    public String getaddress1()
    {
        return address1;
    }

    public void setaddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getaddress2()
    {
        return address2;
    }

    public void setaddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getaddressPostCode()
    {
        return addressPostCode;
    }

    public void setaddressPostCode(String addressPostCode)
    {
        this.addressPostCode = addressPostCode;
    }
    public String getaddressCity()
    {
        return addressCity;
    }

    public void setaddressCity(String addressCity)
    {
        this.addressCity = addressCity;
    }
    public String getaddressCountry()
    {
        return addressCountry;
    }

    public void setaddressCountry(String addressCountry)
    {
        this.addressCountry = addressCountry;
    }
    public String getaddressState()
    {
        return addressState;
    }

    public void setaddressState(String addressState)
    {
        this.addressState = addressState;
    }
    public String getaddressPhone()
    {
        return addressPhone;
    }

    public void setaddressPhone(String addressPhone)
    {
        this.addressPhone = addressPhone;
    }
    //////


    public String getaddressCountryID()
    {
        return addressCountryID;
    }

    public void setaddressCountryID(String addressCountryID)
    {
        this.addressCountryID = addressCountryID;
    }
    public String getaddressStateID()
    {
        return addressStateID;
    }

    public void setaddressStateID(String addressStateID)
    {
        this.addressStateID = addressStateID;
    }

    public String getId_gender()
    {
        return id_gender;
    }

    public void setId_gender(String id_gender)
    {
        this.id_gender = id_gender;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

}
