package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.Collection;
import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.utilities.CollectionAdapter;
import com.example.artefacttrackerapp.utilities.SelectArtefactAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

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

        //<editor-fold defaultstate="collapsed" desc="this context variable and check collector count">
        final Context thisContext = this;

        if (storage.Collectors().size() < 1){
            Toast.makeText(this, "No collectors in storage", Toast.LENGTH_LONG).show();
            return;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Declare dialog and view and reference activity components">
        AlertDialog.Builder collectionDialog = new AlertDialog.Builder(this);
        collectionDialog.setTitle("Create a Collection");

        View collectionDialogView = getLayoutInflater().inflate(R.layout.dialog_create_collection, null);
        final Spinner inputCollectorSpinner = collectionDialogView.findViewById(R.id.spinnerHolderCreateCollectionCollector);
        final EditText inputNameField = collectionDialogView.findViewById(R.id.editTextHolderCreateCollectionName);
        final Spinner inputCategorySpinner = collectionDialogView.findViewById(R.id.spinnerHolderCreateCollectionArtefactCategory);
        final Spinner inputRewardSpinner = collectionDialogView.findViewById(R.id.spinnerHolderCreateCollectionReward);
        final EditText inputQtyField = collectionDialogView.findViewById(R.id.editTextHolderCreateCollectionRewardQuantity);
        //</editor-fold>

        inputNameField.setText(collectionSearchField.getText().toString().trim());

        //<editor-fold defaultstate="collapsed" desc="Populate collector list">
        ArrayList<String> collectorValues = new ArrayList<>();
        storage.Collectors().forEach(c -> collectorValues.add(c.name));
        ArrayAdapter<String> collectorAdapter = new ArrayAdapter<>(thisContext, R.layout.support_simple_spinner_dropdown_item, collectorValues);
        collectorAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        inputCollectorSpinner.setAdapter(collectorAdapter);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Populate category and reward spinners from resource">
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.artefact_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputCategorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> rewardAdapter = ArrayAdapter.createFromResource(this, R.array.collection_rewards, android.R.layout.simple_spinner_item);
        rewardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputRewardSpinner.setAdapter(rewardAdapter);
        //</editor-fold>

        collectionDialog.setView(collectionDialogView)
            .setPositiveButton("Add", (dialogInterface, i) -> {

                //<editor-fold defaultstate-"collapsed" desc="Check details filled in & get data">
                if (inputNameField.getText().toString().trim().equals("") || inputQtyField.getText().toString().trim().equals("")){
                    Toast.makeText(thisContext, "Fill in all the fields", Toast.LENGTH_LONG).show();
                    return;
                }

                final String inputCollector = inputCollectorSpinner.getSelectedItem().toString();
                final String inputName = inputNameField.getText().toString().trim();
                final String inputCategory = inputCategorySpinner.getSelectedItem().toString();
                final String inputRewardType = inputRewardSpinner.getSelectedItem().toString();
                final int inputRewardQty = Integer.parseInt(inputQtyField.getText().toString().trim());
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Declare collection object, dialog and view">
                Collection collection = new Collection(inputName, inputCollector, inputCategory, inputRewardType, inputRewardQty);

                AlertDialog.Builder listDialog = new AlertDialog.Builder(thisContext);
                listDialog.setTitle("Select the Artefacts");

                View listDialogView = getLayoutInflater().inflate(R.layout.dialog_collection_list, null);
                //</editor-fold>

                //==========================================================
                //DECLARATION OF THESE EVENTS AND VARIABLES MUST BE IN ORDER
                //==========================================================

                //<editor-fold defaultstate="collapsed" desc="Declare recycler view components">
                final ArrayList<GameArtefact> dialogDisplayList = new ArrayList<>();

                final RecyclerView inputRecyclerView = listDialogView.findViewById(R.id.recyclerViewHolderCollectionLogList);
                inputRecyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

                RecyclerView.Adapter inputRecyclerViewAdapter = new SelectArtefactAdapter(thisContext, dialogDisplayList);
                inputRecyclerView.setAdapter(inputRecyclerViewAdapter);
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Declare search field and function">
                final EditText inputSearchField = listDialogView.findViewById(R.id.editTextHolderCollectionListSearch);
                inputSearchField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshDialogList(); }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshDialogList(); }
                    @Override
                    public void afterTextChanged(Editable editable) { RefreshDialogList(); }
                    private void RefreshDialogList(){
                        dialogDisplayList.clear();
                        String searchText = inputSearchField.getText().toString().trim();
                        storage.Artefacts().stream().filter(a ->
                                searchText.length() < 1 || a.title.contains(searchText)
                        ).forEach(dialogDisplayList::add);
                        ((SelectArtefactAdapter)inputRecyclerViewAdapter).selectedPosition = -1;
                        inputRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
                inputSearchField.setText("");
                //</editor-fold>

                listDialog.setView(listDialogView)
                    .setPositiveButton("Save", (dialogInterface1, i1) -> {

                        ((SelectArtefactAdapter)inputRecyclerViewAdapter).selectedData.forEach(a -> collection.artefacts.add(a.title));

                        storage.AddCollection(collection);
                        ((CollectionAdapter)collectionAdapter).selectedPosition = -1;
                        collectionAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Added Collection: " + inputName, Toast.LENGTH_LONG).show();
                        collectionSearchField.setText("");

                }).setNegativeButton("Cancel", (dialogInterface1, i1) -> Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG).show()).create().show();

        }).setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG).show()).create().show();
    }
}
