package com.tiseno.poplook.functions;

public class Artist {
    String categoryID, title;

//    public String name;
//    public String sample;

    public  Artist(String categoryID, String title)
    {

        this.categoryID = categoryID;
        this.title = title;

    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
