package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.data.MaterialRequirement;
import com.example.artefacttrackerapp.data.Storage;
import com.example.artefacttrackerapp.R;

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
        storage = new Storage(getResources());
        init();
    }

    private void init(){

//        uniqueRemainingCollectionsField = findViewById(R.id.textViewUniqueRemaining);
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

    private void RefreshData(){

        materialTypeCountField.setText(String.valueOf(storage.Materials().size()));
        ownedArtefactCountField.setText(String.valueOf(storage.Artefacts().stream().map(a -> a.quantity).reduce(0, Integer::sum)));

        int total = 0;
        for (GameArtefact artefact : storage.Artefacts())
            for (MaterialRequirement req : artefact.getRequirements())
                total += req.quantity * artefact.quantity;
        requiredMaterialCountField.setText(String.valueOf(total));

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
