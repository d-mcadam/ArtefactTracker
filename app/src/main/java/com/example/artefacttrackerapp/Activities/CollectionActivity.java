package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.artefacttrackerapp.R;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.AppData.saveAppData;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        init();
    }

    private void init(){

    }

    @Override
    public void onPause(){
        saveAppData(this, storage);
        super.onPause();
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
