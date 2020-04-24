package com.example.artefacttrackerapp.Activities;

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
import android.widget.Toast;

import com.example.artefacttrackerapp.Data.Collector;
import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.Utilities.CollectorAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.Activities.MainActivity.storage;

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

        if (collectorSearchField.getText().toString().trim().length() > 0){
            storage.Collectors().stream()
                    .filter(c ->
                            c.name.contains(collectorSearchField.getText().toString().trim()) ||
                            c.location.contains(collectorSearchField.getText().toString().trim())
                    ).forEach(displayList::add);
        }else{
            displayList.addAll(storage.Collectors());
        }

        ((CollectorAdapter)collectorAdapter).selectedPosition = -1;
        collectorAdapter.notifyDataSetChanged();

    }

    public void GenerateViewLogDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Collections on this Collector");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_show_collector_logs, null);

        dialog.setView(dialogView)
                .setPositiveButton("OK", null)
                .setNeutralButton("Create Collection", (dialogInterface, i) -> {



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
