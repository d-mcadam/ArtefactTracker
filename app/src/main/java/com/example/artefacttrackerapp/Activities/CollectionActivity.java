package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.artefacttrackerapp.R;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        init();
    }

    private void init(){

    }

    public void OpenCollectors(View v){
        Intent intent = new Intent(getBaseContext(), CollectorActivity.class);
        startActivity(intent);
    }

    public void OpenCollections(View v){

    }

    public void OpenRewards(View v){

    }
}
