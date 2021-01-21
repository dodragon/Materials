package com.dod.materialsmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dod.materialsmanagement.R;
import com.dod.materialsmanagement.data.Materials;

import java.util.List;

public class StatusMaterialsAdapter extends RecyclerView.Adapter<StatusMaterialsAdapter.ItemViewHolder>{

    List<Materials> list;
    Context context;

    public StatusMaterialsAdapter(List<Materials> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.materials_status_materials_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Materials vo = list.get(position);

        holder.name.setText(vo.getName());
        holder.ea.setText(String.valueOf(vo.getEa()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView ea;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            ea = itemView.findViewById(R.id.ea);
        }
    }
}
