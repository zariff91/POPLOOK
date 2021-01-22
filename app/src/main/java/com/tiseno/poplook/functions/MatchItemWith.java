package com.tiseno.poplook.functions;

public class MatchItemWith {

    String matchItemWithID,matchItemName;

    public MatchItemWith(String matchItemWithID, String matchItemName) {
        super();
        this.matchItemWithID = matchItemWithID;
        this.matchItemName = matchItemName;

    }

    public String matchItemWithID() {
        return matchItemWithID;
    }

    public String matchItemName() {
        return matchItemName;
    }
}
