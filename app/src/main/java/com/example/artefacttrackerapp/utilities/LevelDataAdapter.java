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

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.activities.LevelActivity;
import com.example.artefacttrackerapp.data.LevelInfo;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class LevelDataAdapter extends RecyclerView.Adapter<LevelDataAdapter.LevelDataViewHolder> {

    private final Context context;
    private final ArrayList<LevelInfo> levelInfoDataSet;

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public LevelDataAdapter(Context context, ArrayList<LevelInfo> levelInfoDataSet){
        this.context = context;
        this.levelInfoDataSet = levelInfoDataSet;
    }

    @NonNull
    @Override
    public LevelDataAdapter.LevelDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_level_data, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new LevelDataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelDataAdapter.LevelDataViewHolder holder, int thisViewsPosition) {

        final LevelInfo item = levelInfoDataSet.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.itemView.setOnLongClickListener(view -> {
            return false;
        });

        holder.deleteButton.setOnClickListener(view -> {
            storage.DeleteLevelInfo(item);
            ((LevelActivity)context).RefreshList();
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colour_recycler_view_selected_grey) : Color.TRANSPARENT);

        holder.nameView.setText(context.getString(R.string.place_holder_title, item.rubbleName));
        holder.levelView.setText(context.getString(R.string.place_holder_quantity, item.level));
        holder.siteView.setText(item.digsite);

        holder.deleteButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.deleteButton.setClickable(holder.viewIsSelected);
    }

    @Override
    public int getItemCount() { return this.levelInfoDataSet.size(); }

    public class LevelDataViewHolder extends RecyclerView.ViewHolder {

        private boolean viewIsSelected = false;

        private final TextView nameView;
        private final TextView levelView;
        private final TextView siteView;
        private final ImageButton deleteButton;

        public LevelDataViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textViewHolderLevelName);
            nameView.setHeight(viewHolderHeight);

            levelView = itemView.findViewById(R.id.textViewHolderLevelLevel);
            levelView.setHeight(viewHolderHeight);

            siteView = itemView.findViewById(R.id.textViewHolderLevelSite);
            siteView.setHeight(viewHolderHeight);

            deleteButton = itemView.findViewById(R.id.imageButtonDeleteLevelData);
            deleteButton.getLayoutParams().height = viewHolderHeight;
        }
    }
}
