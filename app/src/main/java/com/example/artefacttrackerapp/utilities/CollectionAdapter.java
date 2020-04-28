package com.example.artefacttrackerapp.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.activities.CollectionLogActivity;
import com.example.artefacttrackerapp.data.Collection;
import com.example.artefacttrackerapp.data.GameArtefact;

import java.util.ArrayList;
import java.util.OptionalInt;

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
        final String rewardValue = collection.rewardQuantity + " " + collection.reward;

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
            StringBuilder titleSb = new StringBuilder();
            titleSb.append(collection.title).append(collection.isCompleted() ? " \u2713" : "");

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_collection_display, null, false);
            final TextView textView = dialogView.findViewById(R.id.textViewHolderCollectionDisplayMultiline);

            ArrayList<GameArtefact> identifiedArtefacts = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            collection.getArtefacts().forEach(a -> {

                sb.append(a).append(", x");

                storage.Artefacts().stream()
                        .filter(ga -> ga.title.equals(a))
                        .forEach(ga -> {
                            sb.append(ga.quantity);
                            identifiedArtefacts.add(ga);
                        });

                sb.append("\n");
            });

            textView.setText(sb.toString().trim());

            OptionalInt maxCollectCount = identifiedArtefacts.stream().mapToInt(artefact -> artefact.quantity).min();
            titleSb.append(" (").append(maxCollectCount.isPresent() ? maxCollectCount.getAsInt() : 0).append(")").append(" (").append(rewardValue).append(")");
            dialog.setTitle(titleSb.toString().trim());

            if (maxCollectCount.isPresent() && maxCollectCount.getAsInt() > 0)
                dialog.setNeutralButton("Collect", (dialogInterface, i) -> {
                    AlertDialog.Builder submitDialog = new AlertDialog.Builder(context);
                    submitDialog.setTitle("Submit " + collection.title + "?");
                    submitDialog.setMessage(sb.toString().trim());

                    submitDialog.setPositiveButton("YES", (dialogInterface1, i1) -> {

                        collection.completeSubmission();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Collected Reward", Toast.LENGTH_SHORT).show();

                    }).setNegativeButton("NO", null).create().show();
                });


            dialog.setView(dialogView).setPositiveButton("OK", null).create().show();
        });

        holder.deleteButton.setOnClickListener(view -> {
            storage.DeleteCollection(collection);
            ((CollectionLogActivity)context).RefreshList();
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colour_recycler_view_selected_grey) : Color.TRANSPARENT);

        holder.detailView.setText(context.getString(R.string.place_holder_title, (collection.isCompleted() ? "\u2713 " : "") + collection.title + "\n" + collection.rewardQuantity + " " + collection.reward));
        holder.qtyView.setText(context.getString(R.string.place_holder_quantity, collection.getArtefacts().size()));

        holder.viewButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.viewButton.setClickable(holder.viewIsSelected);
        holder.deleteButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.deleteButton.setClickable(holder.viewIsSelected);
    }

    @Override
    public int getItemCount() { return this.collectionDataSet.size(); }

    class CollectionViewHolder extends RecyclerView.ViewHolder {

        private boolean viewIsSelected = false;

        private final TextView detailView;
        private final TextView qtyView;
        private final ImageButton viewButton;
        private final ImageButton deleteButton;

        private CollectionViewHolder(@NonNull View itemView) {
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
