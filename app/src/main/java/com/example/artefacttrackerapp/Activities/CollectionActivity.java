package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.artefacttrackerapp.R;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.getUniqueCollectibleCount;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.getUniqueCollectionRemainingCount;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.saveAppData;

public class CollectionActivity extends AppCompatActivity {

    private TextView uniqueRemainingCollectionsField;
    private TextView availableCollectibleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        init();
    }

    private void init(){
        uniqueRemainingCollectionsField = findViewById(R.id.textViewLogsUniqueRemaining);
        availableCollectibleField = findViewById(R.id.textViewLogsAvailableCount);
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
    }

    public void OpenCollectors(View v){
        Intent intent = new Intent(getBaseContext(), CollectorActivity.class);
        startActivity(intent);
    }

    public void OpenCollections(View v){
        Intent intent = new Intent(getBaseContext(), CollectionLogActivity.class);
        startActivity(intent);
    }

    public void OpenRewards(View v){

    }
}
