package com.tiseno.poplook.functions;

public class StoreListItem {

    String store_Title,store_Address;

    public StoreListItem(String store_Title, String store_Address)
    {
        super();
        this.store_Title = store_Title;
        this.store_Address = store_Address;

    }

    public String getstore_Title()
    {
        return store_Title;
    }
    public String getstore_Address()
    {
        return store_Address;
    }


}
