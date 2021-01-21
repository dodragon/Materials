package com.dod.materialsmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dod.materialsmanagement.ProductsStatusDetail;
import com.dod.materialsmanagement.R;
import com.dod.materialsmanagement.data.Products;

import java.util.List;

public class StatusProductsAdapter extends RecyclerView.Adapter<StatusProductsAdapter.ItemViewHolder>{

    List<Products> list;
    Context context;

    public StatusProductsAdapter(List<Products> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.materials_status_products_list, parent, false);
        return new StatusProductsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Products vo = list.get(position);

        holder.name.setText(vo.getName());
        holder.ea.setText(String.valueOf(vo.getEa()));
        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductsStatusDetail.class);
            intent.putExtra("name", vo.getName());
            intent.putExtra("vo", vo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView ea;
        RelativeLayout layout;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            ea = itemView.findViewById(R.id.ea);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
