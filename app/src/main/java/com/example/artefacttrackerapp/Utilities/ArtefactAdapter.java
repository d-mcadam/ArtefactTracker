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

import com.example.artefacttrackerapp.Data.GameArtefact;
import com.example.artefacttrackerapp.R;

import java.util.ArrayList;

public class ArtefactAdapter extends RecyclerView.Adapter<ArtefactAdapter.ArtefactViewHolder> {

    private final Context context;
    private final ArrayList<GameArtefact> artefactDataSet;

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public ArtefactAdapter(Context context, ArrayList<GameArtefact> artefactDataSet){
        this.context = context;
        this.artefactDataSet = artefactDataSet;
    }

    @NonNull
    @Override
    public ArtefactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_artefact, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new ArtefactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtefactViewHolder holder, int thisViewsPosition) {

        final GameArtefact artefact = artefactDataSet.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.addQuantityButton.setOnClickListener(view -> {
            artefact.quantity++;
            notifyDataSetChanged();
        });

        holder.minusQuantityButton.setOnClickListener(view -> {
            artefact.quantity--;
            notifyDataSetChanged();
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colourRecyclerViewSelected) : Color.TRANSPARENT);

        holder.detailView.setText(context.getString(R.string.place_holder_title, artefact.title));
        holder.qtyView.setText(context.getString(R.string.place_holder_quantity, artefact.quantity));

        holder.addQuantityButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.addQuantityButton.setClickable(holder.viewIsSelected);
        holder.minusQuantityButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.minusQuantityButton.setClickable(holder.viewIsSelected);
    }

    @Override
    public int getItemCount() { return this.artefactDataSet.size(); }

    public class ArtefactViewHolder extends RecyclerView.ViewHolder {

        public boolean viewIsSelected = false;

        public final TextView detailView;
        public final TextView qtyView;
        public final ImageButton addQuantityButton;
        public final ImageButton minusQuantityButton;

        public ArtefactViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderArtefactName);
            detailView.setHeight(viewHolderHeight);

            qtyView = itemView.findViewById(R.id.textViewHolderArtefactQuantity);
            qtyView.setHeight(viewHolderHeight);

            addQuantityButton = itemView.findViewById(R.id.imageButtonHolderAddArtefact);
            addQuantityButton.getLayoutParams().height = viewHolderHeight;

            minusQuantityButton = itemView.findViewById(R.id.imageButtonHolderMinusArtefact);
            minusQuantityButton.getLayoutParams().height = viewHolderHeight;
        }
    }
}