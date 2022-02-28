package com.tiseno.poplook.functions;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by billygoh on 9/14/15.
 */
public class sideMenuItem {

    String categoryID, title;
    JSONArray shopByArray,discoverArray;

    public sideMenuItem(String categoryID, String title, JSONArray shopByArray,JSONArray discoverArray) {
        super();
        this.categoryID = categoryID;
        this.title = title;
        this.shopByArray = shopByArray;
        this.discoverArray = discoverArray;
    }

    public String getcategoryID() {
        return categoryID;
    }
    public String gettitle() {
        return title;
    }
    public void settitle(String title) {
        this.title = title;
    }
    public JSONArray getShopByArray(){return shopByArray;}
    public JSONArray getDiscoverArray(){return discoverArray;}
}

