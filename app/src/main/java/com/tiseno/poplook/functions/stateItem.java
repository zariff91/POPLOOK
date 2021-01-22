package com.tiseno.poplook.functions;

/**
 * Created by billygoh on 9/22/15.
 */
public class stateItem {

    String StateID,StateName;

    public stateItem(String StateID, String StateName) {
        super();
        this.StateID = StateID;
        this.StateName = StateName;
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



}
