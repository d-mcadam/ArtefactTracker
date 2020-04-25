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
import com.example.artefacttrackerapp.activities.CollectionLogActivity;
import com.example.artefacttrackerapp.data.Collection;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private final Context context;
    private final ArrayList<Collection> collectionDataSet;

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public CollectionAdapter(Context context, ArrayList<Collection> collectionDataSet){
        this.context = context;
        this.collectionDataSet = collectionDataSet;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_collection, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new CollectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int thisViewsPosition) {

        final Collection collection = collectionDataSet.get(thisViewsPosition);

        holder.itemView.setOnClickListener(view -> {
            notifyDataSetChanged();

            if (holder.viewIsSelected)
                selectedPosition = -1;
            else
                selectedPosition = thisViewsPosition;

            notifyDataSetChanged();
        });

        holder.viewButton.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(("Artefact list for " + collection.title + " " + (collection.isCompleted() ? "\u2713" : "")).trim());

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_collection_display, null, false);
            final TextView textView = dialogView.findViewById(R.id.textViewHolderCollectionDisplayMultiline);

            StringBuilder sb = new StringBuilder();
            collection.artefacts.forEach(a -> {

                sb.append(a).append(", x");

                storage.Artefacts().stream()
                        .filter(ga -> ga.title.equals(a))
                        .forEach(ga -> sb.append(ga.quantity));

                sb.append("\n");
            });

            textView.setText(sb.toString().trim());

            dialog.setView(dialogView).setPositiveButton("OK", null).create().show();
        });

        holder.deleteButton.setOnClickListener(view -> {
            storage.DeleteCollection(collection);
            ((CollectionLogActivity)context).RefreshList();
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colourRecyclerViewSelected) : Color.TRANSPARENT);

        holder.detailView.setText(context.getString(R.string.place_holder_title, collection.title));
        holder.qtyView.setText(context.getString(R.string.place_holder_quantity, collection.artefacts.size()));

        holder.viewButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.viewButton.setClickable(holder.viewIsSelected);
        holder.deleteButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.deleteButton.setClickable(holder.viewIsSelected);
    }

    @Override
    public int getItemCount() { return this.collectionDataSet.size(); }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        public boolean viewIsSelected = false;

        public final TextView detailView;
        public final TextView qtyView;
        public final ImageButton viewButton;
        public final ImageButton deleteButton;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            detailView = itemView.findViewById(R.id.textViewHolderCollectionName);
            detailView.setHeight(viewHolderHeight);

            qtyView = itemView.findViewById(R.id.textViewHolderCollectionArtefactCount);
            qtyView.setHeight(viewHolderHeight);

            viewButton = itemView.findViewById(R.id.imageButtonHolderViewCollection);
            viewButton.getLayoutParams().height = viewHolderHeight;

            deleteButton = itemView.findViewById(R.id.imageButtonHolderDeleteCollection);
            deleteButton.getLayoutParams().height = viewHolderHeight;
        }
    }
}
