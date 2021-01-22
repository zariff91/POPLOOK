package com.tiseno.poplook.functions;

/**
 * Created by rahnrazamai on 24/02/2017.
 */

public class customerServiceItem
{

    String categoryID;
    String link;

    public customerServiceItem(String categoryID, String link) {
        super();
        this.categoryID = categoryID;
        this.link = link;

    }

    public String getcategoryID() {
        return categoryID;
    }

    public void setcategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getlink() {
        return link;
    }

    public void setlink(String link) {
        this.link = link;
    }

}

