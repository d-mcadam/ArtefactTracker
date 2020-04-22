package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.artefacttrackerapp.Data.MaterialRequirement;
import com.example.artefacttrackerapp.R;

import java.util.ArrayList;

public class AddArtefactActivity extends AppCompatActivity {

    private Button saveButton;
    private Spinner categorySpinner;
    private EditText artefactNameField;

    private final ArrayList<MaterialRequirement> requirementArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artefact);
        init();
    }

    private void init(){

        saveButton = findViewById(R.id.buttonSaveArtefact);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.artefact_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinnerAddArtefactCategory);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { CheckSaveEligibility(); }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { CheckSaveEligibility(); }
        });

        artefactNameField = findViewById(R.id.editTextInputArtefactName);
        artefactNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { CheckSaveEligibility(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { CheckSaveEligibility(); }
            @Override
            public void afterTextChanged(Editable editable) { CheckSaveEligibility(); }
        });

    }

    private void CheckSaveEligibility(){
        saveButton.setEnabled(
                artefactNameField.getText().toString().trim().length() > 0 &&
                categorySpinner.getSelectedItemPosition() > 0
        );
    }

    public void AddMaterialRequirement(View v){

    }
}
