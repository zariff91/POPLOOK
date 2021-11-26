package com.tiseno.poplook.functions;

public class ChildSideMenuItem {

    String categoryID, title;
    boolean showNextBtn;


    public ChildSideMenuItem(String categoryID, String title, boolean showNext) {
        super();
        this.categoryID = categoryID;
        this.title = title;
        this.showNextBtn = showNext;

    }

    public String getcategoryID() {
        return categoryID;
    }
    public String getTitle(){return title;}
    public boolean getshowNextBtn(){return showNextBtn;}
}

