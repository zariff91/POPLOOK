package com.tiseno.poplook.functions;

/**
 * Created by rahnrazamai on 19/12/2016.
 */

public class coloursItem {
    String ColorProdID,ColorImg;


    public coloursItem(String ColorProdID, String ColorImg) {
        super();
        this.ColorProdID = ColorProdID;
        this.ColorImg = ColorImg;
    }

    public String getColorProdID() {
        return ColorProdID;
    }

    public void setColorProdID(String ColorProdID) {
        this.ColorProdID = ColorProdID;
    }


    public String getColorImg() {
        return ColorImg;
    }

    public void setColorImg(String ColorImg) {
        this.ColorImg = ColorImg;
    }


}

