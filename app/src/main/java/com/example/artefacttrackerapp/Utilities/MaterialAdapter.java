package com.example.artefacttrackerapp.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.activities.MaterialOptionsActivity;
import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.Material;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private void incrementQuantity(Material material){
        material.quantity++;
        notifyDataSetChanged();
    }
    private void decrementQuantity(Material material){
        if (material.quantity < 1){

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Warning");
            dialog.setMessage("You are about to delete the Material entry entirely");

            dialog.setPositiveButton("Continue", (dialogInterface, i) -> {

                storage.DeleteMaterial(material);
                ((MaterialOptionsActivity)context).RefreshList();

            }).setNegativeButton("Cancel", null).create().show();

        }else{
            material.quantity--;
            notifyDataSetChanged();
        }
    }

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

        holder.plusButton.setOnClickListener(view -> incrementQuantity(material));

        holder.minusButton.setOnClickListener(view -> decrementQuantity(material));

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getResources().getColor(R.color.colourRecyclerViewSelected, null) : Color.TRANSPARENT);
        holder.detailView.setText(context.getString(R.string.place_holder_title, material.title));
        holder.qtyView.setText(context.getString(R.string.place_holder_quantity, material.quantity));
        holder.plusButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.plusButton.setClickable(holder.viewIsSelected);
        holder.minusButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.minusButton.setClickable(holder.viewIsSelected);

    }

    @Override
    public int getItemCount() { return materialDataSet.size(); }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {

        private boolean viewIsSelected = false;

        private final TextView detailView;
        private final TextView qtyView;
        private final ImageButton plusButton;
        private final ImageButton minusButton;

        private MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderMaterial);
            detailView.setHeight(viewHolderHeight);

            qtyView = itemView.findViewById(R.id.textViewHolderMaterialDisplayQuantity);
            qtyView.setHeight(viewHolderHeight);

            plusButton = itemView.findViewById(R.id.imageButtonHolderPlusMaterial);
            plusButton.getLayoutParams().height = viewHolderHeight;

            minusButton = itemView.findViewById(R.id.imageButtonHolderMinusMaterial);
            minusButton.getLayoutParams().height = viewHolderHeight;
        }

    }
}
