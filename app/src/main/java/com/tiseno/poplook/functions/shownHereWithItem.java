package com.tiseno.poplook.functions;

public class shownHereWithItem {

    String shownWithID,shownWithName;

    public shownHereWithItem(String shownWithID, String shownWithName) {
        super();
        this.shownWithID = shownWithID;
        this.shownWithName = shownWithName;

    }

    public String shownWithID() {
        return shownWithID;
    }

    public String shownWithName() {
        return shownWithName;
    }
}
