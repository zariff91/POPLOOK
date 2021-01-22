package com.tiseno.poplook.functions;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiseno.poplook.R;

import java.util.ArrayList;

/**
 * Created by billygoh on 9/10/15.
 */
public class orderDetailsAdapter extends RecyclerView.Adapter<orderDetailsAdapter.ViewHolder> {

    ArrayList<shoppingBagItem> data ;


    public static class ViewHolder extends RecyclerView.ViewHolder {

//        ImageView orderhistoryGoToIV;

        public ViewHolder(View v) {
            super(v);

//            orderhistoryGoToIV = (ImageView) v.findViewById(R.id.orderhistoryGoToIV);

//            productTimeLeftTV = (TextView) v.findViewById(R.id.productTimeLeftTV);
//            productQuantitySpinner = (Spinner) v.findViewById(R.id.productQuantitySpinner);



        }
    }

    public orderDetailsAdapter(ArrayList<shoppingBagItem> listArray) {

        data = listArray;
    }

    @Override
    public orderDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item_row, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view



        return vhItem; // Returning the created object
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        shoppingBagItem item = data.get(position);




//        holder.productNameTV.setText(item.getproductName());

//        List<String> list = new ArrayList<String>();
//        list.add("list 1");
//        list.add("list 2");
//        list.add("list 3");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(dataAdapter);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
