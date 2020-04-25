package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artefacttrackerapp.data.Collector;
import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.utilities.CollectorAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.CreateCollectionDialogGenerator;

public class CollectorActivity extends AppCompatActivity {

    private EditText collectorSearchField;

    private RecyclerView.Adapter collectorAdapter;

    private final ArrayList<Collector> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);
        init();
    }

    private void init(){

        //<editor-fold defaultstate="collapsed" desc="Search collectors field">
        collectorSearchField = findViewById(R.id.editTextSearchCollectors);
        collectorSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void afterTextChanged(Editable editable) { RefreshList(); }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recycler view components">
        RecyclerView collectorRecyclerView = findViewById(R.id.recyclerViewCollectorList);

        collectorRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        collectorAdapter = new CollectorAdapter(this, displayList);
        collectorRecyclerView.setAdapter(collectorAdapter);
        //</editor-fold>

        RefreshList();

    }

    public void RefreshList(){

        displayList.clear();

        String searchText = collectorSearchField.getText().toString().trim();

        storage.Collectors().stream().filter(c ->
                searchText.length() < 1 || c.name.contains(searchText) || c.location.contains(searchText)
        ).forEach(displayList::add);

        ((CollectorAdapter)collectorAdapter).selectedPosition = -1;
        collectorAdapter.notifyDataSetChanged();

    }

    public void GenerateViewLogDialog(Collector collector){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Collections for " + collector.name);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_show_collector_logs, null);
        final TextView textView = dialogView.findViewById(R.id.textViewHolderCollectorDisplayMultiline);

        StringBuilder sb = new StringBuilder();
        collector.collections.forEach(c -> {
            StringBuilder item = new StringBuilder();

            storage.Collections().stream()
                    .filter(c1 -> c1.title.equals(c))
                    .forEach(c1 -> {
                        if (c1.artefacts.size() > 0 && c1.artefacts.stream().noneMatch(artefactTitle -> storage.findGameArtefactByTitle(artefactTitle).quantity < 1))
                            item.append("\u2605 ");

                        item.append(c);

                        if (c1.isCompleted())
                            item.append(" \u2713");
                    });

            sb.append(item.toString().trim()).append("\n");
        });
        textView.setText(sb.toString().trim());

        dialog.setView(dialogView)
                .setPositiveButton("OK", null)
                .setNeutralButton("Create Collection", (dialogInterface, i) -> {

                    CreateCollectionDialogGenerator(
                            this,
                            collectorSearchField,
                            (CollectorAdapter) collectorAdapter,
                            collector);

                }).create().show();
    }

    public void AddCollector(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Create a Collector");

        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_create_collector, null);
        final EditText inputFieldName = dialogView.findViewById(R.id.editTextCreateCollectorName);
        final EditText inputFieldLocation = dialogView.findViewById(R.id.editTextCreateCollectorLocation);

        dialog.setView(dialogView)
            .setPositiveButton("Add", (dialogInterface, i) -> {

                final String inputName = inputFieldName.getText().toString().trim();
                final String inputLocation = inputFieldLocation.getText().toString().trim();

                if (storage.Collectors().stream().anyMatch(c -> c.name.equals(inputName))){
                    Toast.makeText(getBaseContext(), "Duplicate names detected", Toast.LENGTH_LONG).show();
                    return;
                }

                storage.AddCollector(new Collector(inputName, inputLocation));
                ((CollectorAdapter)collectorAdapter).selectedPosition = -1;
                collectorAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Added collector: " + inputName + " @ " + inputLocation, Toast.LENGTH_LONG).show();
                collectorSearchField.setText("");

        }).setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_LONG).show()).create().show();

    }

}
