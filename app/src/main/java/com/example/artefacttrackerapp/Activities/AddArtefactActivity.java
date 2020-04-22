package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.artefacttrackerapp.R;

public class AddArtefactActivity extends AppCompatActivity {

    private Button buttonSave;
    private EditText inputArtefactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artefact);
        init();
    }

    private void init(){

        buttonSave = findViewById(R.id.buttonSaveArtefact);

        inputArtefactName = findViewById(R.id.editTextInputArtefactName);
        inputArtefactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { CheckSaveEligibility(); }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { CheckSaveEligibility(); }

            @Override
            public void afterTextChanged(Editable editable) { CheckSaveEligibility(); }
        });

    }

    private void CheckSaveEligibility(){
        buttonSave.setEnabled(inputArtefactName.getText().toString().trim().length() > 0);
    }
}
