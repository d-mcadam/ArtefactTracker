package com.example.artefacttrackerapp.utilities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.activities.MaterialOptionsActivity;
import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.Material;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final Context context;
    private final ArrayList<Material> materialDataSet;

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public MaterialAdapter(Context context, ArrayList<Material> materialDataSet){
        this.context = context;
        this.materialDataSet = materialDataSet;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_material, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new MaterialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int thisViewsPosition) {

        final Material material = materialDataSet.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.itemView.setOnLongClickListener(view -> ((MaterialOptionsActivity)context).GenerateLocationInputDialog(material));

        holder.viewIsSelected = selectedPosition == thisViewsPosition;
        if (holder.viewIsSelected)
            ((MaterialOptionsActivity)context).SetSelectedMaterialDetails(material);
        else if (selectedPosition == -1)
            ((MaterialOptionsActivity)context).SetSelectedMaterialDetails(null);

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getResources().getColor(R.color.colourRecyclerViewSelectedGrey, null) : Color.TRANSPARENT);
        holder.detailView.setText(context.getString(R.string.place_holder_title, material.title));
        holder.qtyView.setText(context.getString(R.string.place_holder_quantity, material.quantity));

        ((MaterialOptionsActivity)context).plusMaterialButton.setEnabled(selectedPosition > -1);
        ((MaterialOptionsActivity)context).minusMaterialButton.setEnabled(selectedPosition > -1);

    }

    @Override
    public int getItemCount() { return materialDataSet.size(); }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {

        private boolean viewIsSelected = false;

        private final TextView detailView;
        private final TextView qtyView;

        private MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderMaterial);
            detailView.setHeight(viewHolderHeight);

            qtyView = itemView.findViewById(R.id.textViewHolderMaterialDisplayQuantity);
            qtyView.setHeight(viewHolderHeight);
        }

    }
}
