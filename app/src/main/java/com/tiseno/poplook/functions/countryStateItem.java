package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/22/15.
 */
public class countryStateItem{

    String CountryID,CountryName,StateID,StateName;
    Boolean IsHeader;

    public countryStateItem(String CountryID, String CountryName, String StateID, String StateName, Boolean isHeader) {
        super();
        this.CountryID = CountryID;
        this.CountryName = CountryName;
        this.StateID = StateID;
        this.StateName = StateName;
        this.IsHeader = isHeader;
    }

    public String getcCountryID() {
        return CountryID;
    }

    public void setCountryID(String CountryID) {
        this.CountryID = CountryID;
    }


    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String StateID) {
        this.StateID = StateID;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

    public boolean getIsHeader() {
        return IsHeader;
    }

    public void setIsHeader(boolean IsHeader) {
        this.IsHeader = IsHeader;
    }

}
