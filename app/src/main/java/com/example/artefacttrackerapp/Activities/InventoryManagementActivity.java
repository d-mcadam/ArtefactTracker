package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.artefacttrackerapp.R;

public class InventoryManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_management);
        init();
    }

    private void init(){

    }

    public void OpenAddArtefact(View v){
        Intent intent = new Intent(getBaseContext(), AddArtefactActivity.class);
        startActivity(intent);
    }
}
