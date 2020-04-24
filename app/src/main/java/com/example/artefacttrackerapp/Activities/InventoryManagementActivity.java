package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.utilities.ArtefactAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class InventoryManagementActivity extends AppCompatActivity {

    private EditText artefactSearchField;
    private Spinner categorySpinner;

    private RecyclerView.Adapter artefactAdapter;

    private final ArrayList<GameArtefact> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_management);
        init();
    }

    private void init(){

        //<editor-fold defaultstate="collapsed" desc="Search artefacts field">
        artefactSearchField = findViewById(R.id.editTextSearchArtefacts);
        artefactSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void afterTextChanged(Editable editable) { RefreshList(); }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recycler view components">
        RecyclerView artefactRecyclerView = findViewById(R.id.recyclerViewInventoryList);

        artefactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        artefactAdapter = new ArtefactAdapter(this, displayList);
        artefactRecyclerView.setAdapter(artefactAdapter);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Category spinner">
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.artefact_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinnerInventoryArtefactCategory);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { RefreshList(); }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { RefreshList(); }
        });
        //</editor-fold>

        RefreshList();

    }

    @Override
    public void onResume() {
        super.onResume();
        artefactSearchField.setText("");
        RefreshList();
    }

    public void RefreshList(){

        displayList.clear();

        String textSearch = artefactSearchField.getText().toString().trim();
        String categorySearch = categorySpinner.getSelectedItem().toString();

        storage.Artefacts().stream().filter(a ->
            (textSearch.length() < 1 || a.title.contains(textSearch)) &&
            (categorySpinner.getSelectedItemPosition() < 1 || a.category.equals(categorySearch))
        ).forEach(displayList::add);

        ((ArtefactAdapter)artefactAdapter).selectedPosition = -1;
        artefactAdapter.notifyDataSetChanged();

    }

    public void OpenAddArtefact(View v){
        Intent intent = new Intent(getBaseContext(), AddArtefactActivity.class);
        intent.putExtra("STRING_INPUT", artefactSearchField.getText().toString().trim());
        startActivity(intent);
    }
}
