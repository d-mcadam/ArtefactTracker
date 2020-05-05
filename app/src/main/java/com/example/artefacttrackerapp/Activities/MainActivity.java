package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.artefacttrackerapp.data.Storage;
import com.example.artefacttrackerapp.R;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.USING_LIVE_DATA;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.createLiveData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.createTestData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.getMaterialRequirementsAsIfArtefactsAllBroken;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.getOwnedArtefactCountValue;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.getUniqueCollectibleCount;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.getUniqueCollectionRemainingCount;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.loadAppData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.loadDatabaseOptions;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.saveAppData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.localContext;

public class MainActivity extends AppCompatActivity {

    public static Storage storage;

    private TextView uniqueRemainingCollectionsField;
    private TextView availableCollectibleField;
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

        localContext = this.getResources();

        USING_LIVE_DATA = loadDatabaseOptions(getBaseContext());
        loadAppData(getBaseContext());

        uniqueRemainingCollectionsField = findViewById(R.id.textViewUniqueRemaining);
        availableCollectibleField = findViewById(R.id.textViewAvailableCount);
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

        uniqueRemainingCollectionsField.setText(String.valueOf(getUniqueCollectionRemainingCount()));
        availableCollectibleField.setText(String.valueOf(getUniqueCollectibleCount()));

        materialTypeCountField.setText(String.valueOf(storage.Materials().size()));
        ownedArtefactCountField.setText(String.valueOf(getOwnedArtefactCountValue()));
        requiredMaterialCountField.setText(String.valueOf(getMaterialRequirementsAsIfArtefactsAllBroken()));

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

    public void OpenLevelData(View v){
        Intent intent = new Intent(getBaseContext(), LevelActivity.class);
        startActivity(intent);
    }

    public void OpenRequirements(View v){
        Intent intent = new Intent(getBaseContext(), AnalysisActivity.class);
        startActivity(intent);
    }

}
