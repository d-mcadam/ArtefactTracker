package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.artefacttrackerapp.R;

public class MaterialOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_options);
        init();
    }

    private void init(){

    }

    public void AddMaterial(View v){
        Intent intent = new Intent(getBaseContext(), AddMaterialActivity.class);
        startActivity(intent);
    }
}
