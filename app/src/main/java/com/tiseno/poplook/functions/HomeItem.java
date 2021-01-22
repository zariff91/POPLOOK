package com.tiseno.poplook.functions;

/**
 * Created by rahn on 9/15/15.
 */
public class HomeItem {

    String categoryID;
    String link,href,catName;
    String position;

    public HomeItem(String categoryID, String catName, String link, String href, String position) {
        super();
        this.categoryID = categoryID;
        this.catName = catName;
        this.link = link;
        this.href = href;
        this.position = position;


    }

    public String getcategoryID() {
        return categoryID;
    }

    public void setcategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getcatName() {
        return catName;
    }

    public void setcatName(String catName) {
        this.catName = catName;
    }

    public String getlink() {
        return link;
    }

    public void setlink(String link) {
        this.link = link;
    }

    public String gethref() {
        return href;
    }

    public void sethref(String href) {
        this.href = href;
    }

    public String getposition(){return  this.position = position;}

    public void setposition(String position) { this.position = position; }


}
