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

import com.example.artefacttrackerapp.Activities.AddArtefactActivity;
import com.example.artefacttrackerapp.Data.MaterialRequirement;
import com.example.artefacttrackerapp.R;

import java.util.ArrayList;

public class MaterialRequirementAdapter extends RecyclerView.Adapter<MaterialRequirementAdapter.MaterialRequirementViewHolder> {

    private final Context context;
    private final ArrayList<MaterialRequirement> matReqDataSet;

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public MaterialRequirementAdapter(Context context, ArrayList<MaterialRequirement> matReqDataSet){
        this.context = context;
        this.matReqDataSet = matReqDataSet;
    }

    @NonNull
    @Override
    public MaterialRequirementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_material_requirement, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new MaterialRequirementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialRequirementViewHolder holder, int thisViewsPosition) {

        final MaterialRequirement matReq = matReqDataSet.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.deleteButton.setOnClickListener(view -> {
            this.selectedPosition = -1;
            ((AddArtefactActivity)context).requirementArrayList.remove(matReq);
            ((AddArtefactActivity)context).CheckSaveEligibility();
            notifyDataSetChanged();
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colourRecyclerViewSelected) : Color.TRANSPARENT);
        holder.detailView.setText(context.getString(R.string.material_requirement_holder_display, matReq.title, matReq.quantity));
        holder.deleteButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.deleteButton.setClickable(holder.viewIsSelected);
    }

    @Override
    public int getItemCount() { return this.matReqDataSet.size(); }

    public static class MaterialRequirementViewHolder extends RecyclerView.ViewHolder {

        public boolean viewIsSelected = false;

        public final TextView detailView;
        public final ImageButton deleteButton;

        public MaterialRequirementViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderMaterialRequirement);
            detailView.setHeight(viewHolderHeight);
            deleteButton = itemView.findViewById(R.id.imageButtonHolderDeleteMaterialRequirement);
        }
    }
}