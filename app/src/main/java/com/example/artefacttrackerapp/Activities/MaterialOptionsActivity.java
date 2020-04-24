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
import android.widget.Toast;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.utilities.MaterialAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class MaterialOptionsActivity extends AppCompatActivity {

    private EditText materialSearchField;

    private RecyclerView.Adapter materialAdapter;

    private final ArrayList<String> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_options);
        init();
    }

    private void init(){

        //<editor-fold defaultstate="collapsed" desc="Search materials field">
        materialSearchField = findViewById(R.id.editTextSearchMaterials);
        materialSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void afterTextChanged(Editable editable) { RefreshList(); }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recycler view components">
        RecyclerView materialRecyclerView = findViewById(R.id.recyclerViewMaterialList);

        materialRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        materialAdapter = new MaterialAdapter(this, displayList);
        materialRecyclerView.setAdapter(materialAdapter);
        //</editor-fold>

        RefreshList();
    }

    public void RefreshList(){

        displayList.clear();

        String searchText = materialSearchField.getText().toString().trim();

        storage.Materials().stream().filter(m ->
                searchText.length() < 1 || m.contains(searchText)
        ).forEach(displayList::add);

        ((MaterialAdapter)materialAdapter).selectedPosition = -1;
        materialAdapter.notifyDataSetChanged();

    }

    public void AddMaterial(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add a material");

        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_create_material, null);
        final EditText inputField = dialogView.findViewById(R.id.editTextInputMaterialName);
        inputField.setText(materialSearchField.getText().toString().trim());

        dialog.setView(dialogView)
            .setPositiveButton("Add", (dialogInterface, i) -> {

                final String inputText = inputField.getText().toString().trim();

                if (storage.Materials().stream().anyMatch(m -> m.equals(inputText))){
                    Toast.makeText(getBaseContext(), "Duplicate names detected.", Toast.LENGTH_LONG).show();
                    return;
                }

                storage.AddMaterial(inputText);
                ((MaterialAdapter)materialAdapter).selectedPosition = -1;
                materialAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Added material: " + inputText, Toast.LENGTH_LONG).show();
                materialSearchField.setText("");

            }).setNegativeButton("Cancel",  (dialogInterface, i) -> Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_LONG).show()).create().show();
    }
}
