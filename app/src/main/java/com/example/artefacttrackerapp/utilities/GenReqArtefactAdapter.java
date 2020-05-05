package com.example.artefacttrackerapp.utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.GameArtefact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class GenReqArtefactAdapter extends RecyclerView.Adapter<GenReqArtefactAdapter.GenReqViewHolder> {

    private final Context context;
    private final ArrayList<GameArtefact> displayList;
    public final ArrayList<GameArtefact> selectedList = new ArrayList<>();

    public int selectedPosition = -1;

    private final int viewHolderHeight = 129;

    public GenReqArtefactAdapter(Context context, ArrayList<GameArtefact> dataSet){
        this.context = context;
        this.displayList = dataSet;
    }

    @NonNull
    @Override
    public GenReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_artefact, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new GenReqViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenReqViewHolder holder, int thisViewsPosition) {

        final GameArtefact artefact = displayList.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colour_recycler_view_selected_grey) : Color.TRANSPARENT);

        holder.plusButton.setOnClickListener(view -> {
            selectedList.add(artefact);
            notifyDataSetChanged();
        });

        holder.minusButton.setOnClickListener(view -> {
            selectedList.remove(artefact);
            notifyDataSetChanged();
        });

        holder.detailView.setText(context.getString(R.string.place_holder_title, artefact.title));
        holder.qtyView.setText(String.valueOf(Collections.frequency(selectedList, artefact)));

        holder.plusButton.setVisibility(holder.viewIsSelected & Collections.frequency(selectedList, artefact) < artefact.quantity ? View.VISIBLE : View.INVISIBLE);
        holder.plusButton.setClickable(holder.viewIsSelected & Collections.frequency(selectedList, artefact) < artefact.quantity);
        holder.minusButton.setVisibility(holder.viewIsSelected & selectedList.contains(artefact) ? View.VISIBLE : View.INVISIBLE);
        holder.minusButton.setClickable(holder.viewIsSelected & selectedList.contains(artefact));
    }

    @Override
    public int getItemCount() { return this.displayList.size(); }

    public class GenReqViewHolder extends RecyclerView.ViewHolder {

        private boolean viewIsSelected = false;

        private final TextView detailView;
        private final TextView qtyView;
        private final ImageButton plusButton;
        private final ImageButton minusButton;

        public GenReqViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderArtefactName);
            detailView.setHeight(viewHolderHeight);

            qtyView = itemView.findViewById(R.id.textViewHolderArtefactQuantity);
            qtyView.setHeight(viewHolderHeight);

            plusButton = itemView.findViewById(R.id.imageButtonHolderAddArtefact);
            plusButton.getLayoutParams().height = viewHolderHeight;

            minusButton = itemView.findViewById(R.id.imageButtonHolderMinusArtefact);
            minusButton.getLayoutParams().height = viewHolderHeight;
        }
    }
}
