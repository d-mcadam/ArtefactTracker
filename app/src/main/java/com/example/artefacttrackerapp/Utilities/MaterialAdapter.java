package com.example.artefacttrackerapp.Utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.Activities.MaterialOptionsActivity;
import com.example.artefacttrackerapp.R;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.Activities.MainActivity.storage;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final Context context;
    private final ArrayList<String> materialDataSet;

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public MaterialAdapter(Context context, ArrayList<String> materialDataSet){
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

        final String material = materialDataSet.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.deleteButton.setOnClickListener(view -> {
            storage.DeleteMaterial(material);
            this.selectedPosition = -1;
            ((MaterialOptionsActivity)context).RefreshList();
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getResources().getColor(R.color.colourRecyclerViewSelected, null) : Color.TRANSPARENT);
        holder.detailView.setText(materialDataSet.get(thisViewsPosition));
        holder.deleteButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.deleteButton.setClickable(holder.viewIsSelected);

    }

    @Override
    public int getItemCount() { return materialDataSet.size(); }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {

        public boolean viewIsSelected = false;

        public final TextView detailView;
        public final ImageButton deleteButton;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderMaterial);
            detailView.setHeight(viewHolderHeight);
            deleteButton = itemView.findViewById(R.id.imageButtonHolderDeleteMaterial);
        }

    }
}