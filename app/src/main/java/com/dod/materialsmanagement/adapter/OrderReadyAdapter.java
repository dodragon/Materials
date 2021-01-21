package com.dod.materialsmanagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dod.materialsmanagement.OrderStatus;
import com.dod.materialsmanagement.R;
import com.dod.materialsmanagement.data.Materials;
import com.dod.materialsmanagement.data.Products;
import com.dod.materialsmanagement.dialog.OrderReadyProduct;

import java.util.List;

public class OrderReadyAdapter extends RecyclerView.Adapter<OrderReadyAdapter.ItemViewHolder>{

    List<?> list;
    boolean isProduct;
    Context context;

    public OrderReadyAdapter(List<?> list, boolean isProduct, Context context) {
        this.list = list;
        this.isProduct = isProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_ready_list, parent, false);
        return new OrderReadyAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Object vo = list.get(position);
        String name;
        String ea;

        if(isProduct){
            name = ((Products)vo).getName();
            ea = String.valueOf(((Products)vo).getEa());
            holder.layout.setOnClickListener(v -> {
                OrderReadyProduct dialog = OrderReadyProduct.getInstance(
                        ((Products)vo)
                        , ((OrderStatus)OrderStatus.context).bluePrints.get(name)
                        , context
                );
                dialog.show(((OrderStatus)OrderStatus.context).getSupportFragmentManager()
                        , "ORDER_DIALOG");
            });
        }else {
            name = ((Materials)vo).getName();
            ea = String.valueOf(((Materials)vo).getEa());
            holder.img.setVisibility(View.GONE);
        }

        holder.name.setText(name);
        holder.ea.setText(ea);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView ea;
        RelativeLayout layout;
        ImageView img;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            ea = itemView.findViewById(R.id.ea);
            layout = itemView.findViewById(R.id.layout);
            img = itemView.findViewById(R.id.add_img);
        }
    }
}
