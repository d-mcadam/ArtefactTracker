package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.Collection;
import com.example.artefacttrackerapp.utilities.CollectionAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.saveAppData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.CreateCollectionDialogGenerator;

public class CollectionLogActivity extends AppCompatActivity {

    private EditText collectionSearchField;
    private Spinner categorySpinner;
    private Spinner rewardSpinner;

    private RecyclerView.Adapter collectionAdapter;

    private final ArrayList<Collection> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_log);
        init();
    }

    private void init(){

        //<editor-fold defaultstate="collapsed" desc="Search collections field">
        collectionSearchField = findViewById(R.id.editTextSearchCollections);
        collectionSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void afterTextChanged(Editable editable) { RefreshList(); }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Category spinner">
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.artefact_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinnerCollectionsArtefactCategory);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { RefreshList(); }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { RefreshList(); }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Reward spinner">
        ArrayAdapter<CharSequence> rewardAdapter = ArrayAdapter.createFromResource(this, R.array.collection_rewards, android.R.layout.simple_spinner_item);
        rewardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rewardSpinner = findViewById(R.id.spinnerCollectionsRewardType);
        rewardSpinner.setAdapter(rewardAdapter);
        rewardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { RefreshList(); }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { RefreshList(); }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recycler view components">
        RecyclerView collectionRecyclerView = findViewById(R.id.recyclerViewCollectionList);

        collectionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        collectionAdapter = new CollectionAdapter(this, displayList);
        collectionRecyclerView.setAdapter(collectionAdapter);
        //</editor-fold

        RefreshList();

    }

    @Override
    public void onPause(){
        saveAppData(this, storage);
        super.onPause();
    }

    public void RefreshList(){

        displayList.clear();

        String searchText = collectionSearchField.getText().toString().trim();
        String categorySearch = categorySpinner.getSelectedItem().toString();
        String rewardSearch = rewardSpinner.getSelectedItem().toString();

        storage.Collections().stream().filter(c ->
            (searchText.length() < 1 || c.title.contains(searchText)) &&
            (categorySpinner.getSelectedItemPosition() < 1 || c.category.equals(categorySearch)) &&
            (rewardSpinner.getSelectedItemPosition() < 1 || c.reward.equals(rewardSearch))
        ).forEach(displayList::add);

        ((CollectionAdapter)collectionAdapter).selectedPosition = -1;
        collectionAdapter.notifyDataSetChanged();

    }

    public void AddCollectionLog(View v){

        CreateCollectionDialogGenerator(
                this,
                collectionSearchField,
                (CollectionAdapter) collectionAdapter,
                categorySpinner.getSelectedItemPosition(),
                rewardSpinner.getSelectedItemPosition());

    }
}
