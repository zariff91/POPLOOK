package com.tiseno.poplook.functions;

/**
 * Created by rahn on 9/18/15.
 */
public class StatePickerItem {


    String id_country,state;

    public StatePickerItem(String id_country, String state) {
        super();
        this.id_country = id_country;
        this.state = state;
    }


    public String getid_country() {
        return id_country;
    }

    public void setid_country(String id_country) {
        this.id_country = id_country;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }

}
