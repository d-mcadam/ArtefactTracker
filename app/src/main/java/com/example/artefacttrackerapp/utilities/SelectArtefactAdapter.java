package com.example.artefacttrackerapp.utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.GameArtefact;

import java.util.ArrayList;

public class SelectArtefactAdapter extends RecyclerView.Adapter<SelectArtefactAdapter.SelectorViewHolder> {

    private final Context context;
    private final ArrayList<GameArtefact> selectorDataSet;
    protected final ArrayList<GameArtefact> selectedData = new ArrayList<>();

    public int selectedPosition = -1;
    private static final int viewHolderHeight = 129;

    public SelectArtefactAdapter(Context context, ArrayList<GameArtefact> selectorDataSet){
        this.context = context;
        this.selectorDataSet = selectorDataSet;
    }

    @NonNull
    @Override
    public SelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_select_artefact, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new SelectorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectorViewHolder holder, int thisViewsPosition) {

        final GameArtefact artefact = selectorDataSet.get(thisViewsPosition);

        holder.checkBox.setOnClickListener(view -> {
            notifyDataSetChanged();

            boolean r = selectedData.contains(artefact) ?
                    selectedData.remove(artefact) : selectedData.add(artefact);

            notifyDataSetChanged();
        });

        boolean contained = selectedData.contains(artefact);
        holder.checkBox.setBackgroundColor(contained ? context.getColor(R.color.colourRecyclerViewSelected) : Color.TRANSPARENT);
        holder.checkBox.setChecked(contained);
        holder.checkBox.setText(artefact.title);

    }

    @Override
    public int getItemCount() { return this.selectorDataSet.size(); }

    class SelectorViewHolder extends RecyclerView.ViewHolder{

        private final CheckBox checkBox;

        private SelectorViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxSelectArtefactDetails);
            checkBox.setHeight(viewHolderHeight);
        }
    }
}
