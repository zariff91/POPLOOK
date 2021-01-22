package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/14/15.
 */
public class sideMenuItem {

    String categoryID, title, textColor;
    boolean isHeader,follow,collection;

    public sideMenuItem(String categoryID, String title, String textColor, boolean isHeader,boolean follow, boolean collection) {
        super();
        this.categoryID = categoryID;
        this.title = title;
        this.textColor = textColor;
        this.isHeader = isHeader;
        this.follow = follow;
        this.collection =collection;

    }

    public String getcategoryID() {
        return categoryID;
    }

    public void setcategoryID(String categoryID) {
        this.categoryID = categoryID;
    }


    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public boolean getisHeader() {
        return isHeader;
    }

    public void setisHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }
    public boolean getFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
    public boolean getCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }
}

