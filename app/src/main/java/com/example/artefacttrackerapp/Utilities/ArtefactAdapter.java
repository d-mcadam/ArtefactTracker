package com.example.artefacttrackerapp.utilities;

import android.annotation.SuppressLint;
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

import com.example.artefacttrackerapp.activities.InventoryManagementActivity;
import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.R;

import java.util.ArrayList;
import java.util.OptionalInt;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.artefactsLeftForUniqueCollections;

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

        holder.itemView.setOnLongClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            StringBuilder titleSb = new StringBuilder();
            titleSb.append(artefact.title);

            @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_material_requirement_display, null, false);
            final TextView textView = dialogView.findViewById(R.id.textViewHolderMatReqDisplayMultiline);

            ArrayList<Integer> identifiedMaterialCounts = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            artefact.getRequirements().forEach(mr -> {
                sb.append(mr.title).append(", x").append(mr.quantity);
                storage.Materials().stream()
                        .filter(m -> m.title.equals(mr.title))
                        .forEach(m -> {
                            if (m.quantity >= mr.quantity)
                                sb.append(" \u2713");
                            identifiedMaterialCounts.add(m.quantity / mr.quantity);
                        });
                sb.append("\n");
            });
            textView.setText(sb.toString().trim());

            titleSb.append(" (").append(
                    identifiedMaterialCounts.stream().mapToInt(i -> i).min().orElse(0)
            ).append(")\n").append(artefactsLeftForUniqueCollections(artefact.title));

            dialog.setTitle(titleSb.toString().trim());
            dialog.setView(dialogView).setPositiveButton("OK", null).create().show();
            return true;
        });

        holder.addQuantityButton.setOnClickListener(view -> {
            artefact.quantity++;
            notifyDataSetChanged();
        });

        holder.minusQuantityButton.setOnClickListener(view -> {
            if (artefact.quantity < 1){
                AlertDialog.Builder warningDialog = new AlertDialog.Builder(context);
                warningDialog.setTitle("Warning");
                warningDialog.setMessage("You are about to delete the Artefact entry entirely");

                warningDialog.setPositiveButton("Continue", (dialogInterface, i) -> {

                    storage.DeleteArtefact(artefact);
                    ((InventoryManagementActivity)context).RefreshList();

                }).setNegativeButton("Cancel", null).create().show();
            }else {
                artefact.quantity--;
                notifyDataSetChanged();
            }
        });

        holder.viewIsSelected = selectedPosition == thisViewsPosition;

        holder.itemView.setBackgroundColor(holder.viewIsSelected ? context.getColor(R.color.colour_recycler_view_selected_grey) : Color.TRANSPARENT);

        holder.detailView.setText(context.getString(R.string.place_holder_title, artefact.title));
        holder.qtyView.setText(context.getString(R.string.place_holder_quantity, artefact.quantity));

        holder.addQuantityButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.addQuantityButton.setClickable(holder.viewIsSelected);
        holder.minusQuantityButton.setVisibility(holder.viewIsSelected ? View.VISIBLE : View.INVISIBLE);
        holder.minusQuantityButton.setClickable(holder.viewIsSelected);
    }

    @Override
    public int getItemCount() { return this.artefactDataSet.size(); }

    class ArtefactViewHolder extends RecyclerView.ViewHolder {

        private boolean viewIsSelected = false;

        private final TextView detailView;
        private final TextView qtyView;
        private final ImageButton addQuantityButton;
        private final ImageButton minusQuantityButton;

        private ArtefactViewHolder(@NonNull View itemView) {
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
