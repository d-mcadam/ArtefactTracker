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

import com.example.artefacttrackerapp.Data.Collector;
import com.example.artefacttrackerapp.R;

import java.util.ArrayList;

public class CollectorAdapter extends RecyclerView.Adapter<CollectorAdapter.CollectorViewHolder> {

    private final Context context;
    private final ArrayList<Collector> collectorDataSet;

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public CollectorAdapter(Context context, ArrayList<Collector> collectorDataSet){
        this.context = context;
        this.collectorDataSet = collectorDataSet;
    }

    @NonNull
    @Override
    public CollectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_collector, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new CollectorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectorViewHolder holder, int thisViewsPosition) {

        final Collector collector = collectorDataSet.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.viewLogButton.setOnClickListener(view -> {

        });

        holder.deleteButton.setOnClickListener(view -> {

        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colourRecyclerViewSelected) : Color.TRANSPARENT);

        holder.detailView.setText(context.getString(R.string.place_holder_title, collector.name));
        holder.logQtyView.setText(context.getString(R.string.place_holder_quantity, collector.collections.size()));

        holder.viewLogButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.viewLogButton.setClickable(holder.viewIsSelected);
        holder.deleteButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.deleteButton.setClickable(holder.viewIsSelected);
    }

    @Override
    public int getItemCount() { return this.collectorDataSet.size(); }

    public class CollectorViewHolder extends RecyclerView.ViewHolder{

        public boolean viewIsSelected = false;

        public final TextView detailView;
        public final TextView logQtyView;
        public final ImageButton viewLogButton;
        public final ImageButton deleteButton;

        public CollectorViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderCollectorNameLocation);
            detailView.setHeight(viewHolderHeight);

            logQtyView = itemView.findViewById(R.id.textViewHolderCollectorLogCount);
            logQtyView.setHeight(viewHolderHeight);

            viewLogButton = itemView.findViewById(R.id.imageButtonHolderViewCollectorLog);
            viewLogButton.getLayoutParams().height = viewHolderHeight;

            deleteButton = itemView.findViewById(R.id.imageButtonHolderDeleteCollector);
            deleteButton.getLayoutParams().height = viewHolderHeight;
        }
    }
}
