package com.example.artefacttrackerapp.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.R;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final ArrayList<String> materialDataSet;

    public MaterialAdapter(ArrayList<String> materialDataSet){
        this.materialDataSet = materialDataSet;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_material, parent, false);
        v.getLayoutParams().height = parent.getMeasuredHeight() / 12;
        return new MaterialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {

        holder.detailView.setText(materialDataSet.get(position));

    }

    @Override
    public int getItemCount() { return materialDataSet.size(); }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {

        public TextView detailView;
        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderMaterial);
        }
    }
}
