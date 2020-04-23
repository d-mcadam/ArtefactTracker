package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artefacttrackerapp.Data.Storage;
import com.example.artefacttrackerapp.R;

import static com.example.artefacttrackerapp.Activities.MainActivity.storage;

public class MaterialOptionsActivity extends AppCompatActivity {

    private EditText materialSearchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_options);
        init();
    }

    private void init(){

        materialSearchField = findViewById(R.id.editTextSearchMaterials);

    }

    public void AddMaterial(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add a material");

        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_create_material, null);
        final EditText inputField = dialogView.findViewById(R.id.editTextInputMaterialName);
        inputField.setText(materialSearchField.getText().toString().trim());

        dialog.setView(dialogView)
            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String inputText = inputField.getText().toString().trim();

                if (storage.Materials().stream().anyMatch((m) -> m.equals(inputText))){
                    Toast.makeText(getBaseContext(), "Duplicate names detected.", Toast.LENGTH_LONG).show();
                    return;
                }

                storage.AddMaterial(inputText);
                Toast.makeText(getBaseContext(), "Added material: " + inputText, Toast.LENGTH_LONG).show();
                materialSearchField.setText("");
            }
        }).setNegativeButton("Cancel", null).create().show();
    }
}
