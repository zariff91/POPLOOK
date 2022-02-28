package com.tiseno.poplook.functions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tiseno.poplook.R;

import java.util.ArrayList;
import java.util.List;

public class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuAdapter.ViewHolder> {

//    private List<String> mData;
    private onClickItem monClick;
    Context context;

    ArrayList<sideMenuItem>menuArray;

    // data is passed into the constructor
    public BottomMenuAdapter(ArrayList<sideMenuItem>data, onClickItem onClick, Context context) {
        this.menuArray = data;
        this.monClick = onClick;
        this.context = context;
    }
    @NonNull
    @Override
    public BottomMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_menu_row_adapter, parent, false); //Inflating the layout

        return new ViewHolder(v,monClick);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomMenuAdapter.ViewHolder holder, int position) {
            holder.textView.setText(menuArray.get(position).title);
            holder.textView.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));

        if(menuArray.get(position).getShopByArray() != null){
            holder.next.setVisibility(View.VISIBLE);

            if(menuArray.get(position).title.equals("Raya 2022")){

                holder.textView.setTextColor(context.getResources().getColor(R.color.raya_22));
                holder.textView.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
            }

            else {
                holder.textView.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
            }
        }
        else {
            holder.next.setVisibility(View.GONE);

            if(menuArray.get(position).title.equals("Visit Our Store") || menuArray.get(position).title.equals("Settings") || menuArray.get(position).title.equals("Follow Us") || menuArray.get(position).title.equals("My Account") || menuArray.get(position).title.equals("Log Out") || menuArray.get(position).title.equals("Home")|| menuArray.get(position).title.equals("POPLOOK Loyalty")|| menuArray.get(position).title.equals("My Member ID")|| menuArray.get(position).title.equals("Shop By")|| menuArray.get(position).title.equals("Discover")){

                holder.textView.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));

                if(menuArray.get(position).title.equals("Follow Us")){
               holder.line.setVisibility(View.VISIBLE);
                }
           else {
                    holder.line.setVisibility(View.GONE);
               }

            }

            else {

                    holder.textView.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_MEDIUM_FONT));
            }

        }

//
//        if(mData[position].equals("Settings") || mData[position].equals("My Account") || mData[position].equals("Order History")|| mData[position].equals("POPLOOK Loyalty")|| mData[position].equals("Log Out")){
//            holder.textView.setTypeface(FontUtil.getTypeface(context, FontUtil.FontType.AVENIR_BLACK_FONT));
//
//        }

        }

    @Override
    public int getItemCount() {

        return menuArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ImageView next;
        private View line;
        onClickItem itemClick;

        public ViewHolder(View view, onClickItem itemClick) {
            super(view);
            view.setOnClickListener(this);
            this.textView = view.findViewById(R.id.categoryTextSideMenu);
            this.next = view.findViewById(R.id.nextBtn);
            this.line = view.findViewById(R.id.view_line);

            this.itemClick = itemClick;
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(getAdapterPosition());
        }
    }

    public interface onClickItem{
        void onItemClick(int position);
    }
}
