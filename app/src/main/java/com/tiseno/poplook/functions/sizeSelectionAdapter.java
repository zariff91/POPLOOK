package com.tiseno.poplook.functions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiseno.poplook.R;
import com.tiseno.poplook.SelectSizeAndFilterFragment;


public class sizeSelectionAdapter extends RecyclerView.Adapter<sizeSelectionAdapter.ViewHolder> {

    SelectSizeAndFilterFragment sizeColorFragment;

    Context context;

    String origin;

    String[] myStrings = { "S", "M", "L" ,"XL","2XL","1 Year","2 Year","3 Year","4 Year","5 Year"};

    String[] myStringsColour = { "Black", "White", "Green" ,"Red","Yellow","Blue","Orange","Pink","Purple","Peach"};



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tableTV;

        public ViewHolder(View v) {
            super(v);

            tableTV = (TextView)v.findViewById(R.id.sizeRVTV);

        }

    }

    public sizeSelectionAdapter(Context context1 , SelectSizeAndFilterFragment scFragment, String from)
    {

        context = context1;
        sizeColorFragment = scFragment;
        origin = from;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_selection_table_layout, parent, false); //Inflating the layout

        ViewHolder view = new ViewHolder(v);

        return view;
    }

    @Override
    public void onBindViewHolder(final sizeSelectionAdapter.ViewHolder holder, final int position) {

        if(origin.equals("size"))
        {
            holder.tableTV.setText(myStrings[position]);

        }

        else
        {
            holder.tableTV.setText(myStringsColour[position]);

        }


        holder.tableTV.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_ROMAN_FONT));
        holder.tableTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                holder.tableTV.setBackgroundColor(Color.BLACK);

                Drawable img = context.getApplicationContext().getResources().getDrawable(R.drawable.ic_checkmark_2);
                img.setBounds( 0, 0, 50, 50 );
                holder.tableTV.setCompoundDrawables( null, null, img, null );

                if(origin.equals("size")) {

                    sizeColorFragment.selectedData(myStrings[position]);

                }

                else {

                    sizeColorFragment.selectedDataColor(myStringsColour[position]);

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return myStrings.length;
    }


}
