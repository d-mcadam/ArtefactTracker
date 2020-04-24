package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.artefacttrackerapp.Data.GameArtefact;
import com.example.artefacttrackerapp.Data.MaterialRequirement;
import com.example.artefacttrackerapp.Data.Storage;
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
        init();
    }

    private void init(){

        storage = new Storage();

//        uniqueRemainingCollectionsField = findViewById(R.id.textViewUniqueRemaining);
//        availableCollectibleField = findViewById(R.id.textViewAvailableCount);
//        availableCronotesField = findViewById(R.id.textViewCronotesCount);
//        availableTetraField = findViewById(R.id.textViewTetraCount);
        materialTypeCountField = findViewById(R.id.textViewMaterialTypeCount);
        ownedArtefactCountField = findViewById(R.id.textViewArtefactCount);
        requiredMaterialCountField = findViewById(R.id.textViewMaterialRequirementCount);

    }

    @Override
    public void onResume(){
        super.onResume();

        materialTypeCountField.setText(String.valueOf(storage.Materials().size()));
        ownedArtefactCountField.setText(String.valueOf(storage.Artefacts().stream().map(a -> a.quantity).reduce(0, Integer::sum)));

        int total = 0;
        for (GameArtefact artefact : storage.Artefacts())
            for (MaterialRequirement req : artefact.requirements)
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
}
