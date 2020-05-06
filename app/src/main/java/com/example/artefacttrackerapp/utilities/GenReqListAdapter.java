package com.example.artefacttrackerapp.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.MaterialRequirement;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.findMaterialByTitle;

public class GenReqListAdapter extends RecyclerView.Adapter<GenReqListAdapter.GenReqViewHolder> {

    private final ArrayList<MaterialRequirement> dataSet;

    private final int viewHolderHeight = 70;

    public GenReqListAdapter(ArrayList<MaterialRequirement> dataSet){
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public GenReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_show_collector_logs, parent, false);
        v.getLayoutParams().height = viewHolderHeight;
        return new GenReqViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenReqViewHolder holder, int thisViewsPosition) {
        DecimalFormat df = new DecimalFormat("#,###");
        final MaterialRequirement mr = dataSet.get(thisViewsPosition);
        int matsLeft = mr.quantity - findMaterialByTitle(mr.title).quantity;
        holder.textView.setText((mr.title + ", x" + df.format(mr.quantity) + " (" + (matsLeft < 0 ? 0 : df.format(matsLeft)) + ")").trim());
    }

    @Override
    public int getItemCount() { return this.dataSet.size(); }

    public class GenReqViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public GenReqViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewHolderCollectorDisplayMultiline);
            textView.setHeight(viewHolderHeight);
        }
    }
}
