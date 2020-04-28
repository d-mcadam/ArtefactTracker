package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.artefacttrackerapp.data.Collection;
import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.data.MaterialRequirement;
import com.example.artefacttrackerapp.data.Storage;
import com.example.artefacttrackerapp.R;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.USING_LIVE_DATA;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.loadAppData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.loadDatabaseOptions;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.saveAppData;

public class MainActivity extends AppCompatActivity {

    public static Storage storage;

    private TextView uniqueRemainingCollectionsField;
    private TextView availableCollectibleField;
    private TextView availableCronotesField;
    private TextView availableTetraField;
    private TextView materialTypeCountField;
    private TextView ownedArtefactCountField;
    private TextView requiredMaterialCountField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){

        USING_LIVE_DATA = loadDatabaseOptions(getBaseContext());
        storage = loadAppData(getBaseContext());

        uniqueRemainingCollectionsField = findViewById(R.id.textViewUniqueRemaining);
//        availableCollectibleField = findViewById(R.id.textViewAvailableCount);
//        availableCronotesField = findViewById(R.id.textViewCronotesCount);
//        availableTetraField = findViewById(R.id.textViewTetraCount);
        materialTypeCountField = findViewById(R.id.textViewMaterialTypeCount);
        ownedArtefactCountField = findViewById(R.id.textViewArtefactCount);
        requiredMaterialCountField = findViewById(R.id.textViewMaterialRequirementCount);

        RefreshData();

    }

    @Override
    public void onResume(){
        super.onResume();
        RefreshData();
    }

    @Override
    public void onPause(){
        saveAppData(this, storage);
        super.onPause();
    }

    private void RefreshData(){

        uniqueRemainingCollectionsField.setText(String.valueOf(
                storage.Collections().stream().filter(c -> !c.isCompleted()).count()));

        materialTypeCountField.setText(String.valueOf(storage.Materials().size()));
        ownedArtefactCountField.setText(String.valueOf(
                storage.Artefacts().stream().map(a -> a.quantity).reduce(0, Integer::sum)));
        requiredMaterialCountField.setText(String.valueOf(
                storage.Artefacts().stream().map(artefact -> artefact.getRequirements().stream().map(
                        materialRequirement -> artefact.quantity * materialRequirement.quantity)
                        .reduce(0, Integer::sum)).reduce(0, Integer::sum)));

    }

    public void OpenDatabaseOptions(View v){
        Intent intent = new Intent(getBaseContext(), DatabaseOptionsActivity.class);
        startActivity(intent);
    }

    public void OpenMaterialOptions(View v){
        Intent intent = new Intent(getBaseContext(), MaterialOptionsActivity.class);
        startActivity(intent);
    }

    public void OpenInventoryManagement(View v){
        Intent intent = new Intent(getBaseContext(), InventoryManagementActivity.class);
        startActivity(intent);
    }

    public void OpenCollectionManagement(View v){
        Intent intent = new Intent(getBaseContext(), CollectionActivity.class);
        startActivity(intent);
    }

}
