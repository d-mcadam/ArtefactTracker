package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.artefacttrackerapp.Data.Storage;
import com.example.artefacttrackerapp.R;

public class MainActivity extends AppCompatActivity {

    public static Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){

        storage = new Storage();

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
