package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.artefacttrackerapp.Data.MaterialRequirement;
import com.example.artefacttrackerapp.R;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.Activities.MainActivity.storage;

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

    @Override
    public void onBackPressed(){
        if (artefactNameField.getText().toString().trim().length() > 0 || requirementArrayList.size() > 0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(true);
            dialog.setTitle("Unsaved data");
            dialog.setMessage("You'll lose any unsaved data");
            dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    goBack();
                }
            });
            dialog.setNegativeButton("Cancel", null);
            dialog.create().show();
        }else{
            goBack();
        }
    }
    private void goBack(){ super.onBackPressed(); }

    private void init(){

        saveButton = findViewById(R.id.buttonSaveArtefact);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.artefact_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinnerAddArtefactCategory);
        categorySpinner.setAdapter(categoryAdapter);

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
                requirementArrayList.size() > 0
        );
    }

    public void AddMaterialRequirement(View v){

        final Context thisContext = this;

        AlertDialog.Builder nameDialog = new AlertDialog.Builder(thisContext);
        nameDialog.setTitle("Material Name");

        final LayoutInflater inflater = getLayoutInflater();

        View nameDialogView = inflater.inflate(R.layout.dialog_material_name, null);

        ArrayList<String> spinnerValues = new ArrayList<>();
        for (String m : storage.Materials()){
            if (!requirementArrayList.stream().anyMatch((i) -> i.title.equals(m))){
                spinnerValues.add(m);
            }
        }

        final Spinner nameDialogSpinner = nameDialogView.findViewById(R.id.spinnerMaterialNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(thisContext, R.layout.support_simple_spinner_dropdown_item, spinnerValues);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        nameDialogSpinner.setAdapter(adapter);

        nameDialog.setView(nameDialogView)
            .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    final String name = nameDialogSpinner.getSelectedItem().toString();

                    AlertDialog.Builder qtyDialog = new AlertDialog.Builder(thisContext);
                    qtyDialog.setTitle("Quantity Required");

                    View qtyDialogView = inflater.inflate(R.layout.dialog_material_quantity, null);
                    final EditText qtyDialogField = qtyDialogView.findViewById(R.id.editTextMaterialReqQuantity);

                    qtyDialog.setView(qtyDialogView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                int quantity = Integer.parseInt(qtyDialogField.getText().toString().trim());

                                MaterialRequirement materialRequirement = new MaterialRequirement(name, quantity);

                                requirementArrayList.add(materialRequirement);
                                CheckSaveEligibility();

                                Toast.makeText(thisContext, "Saved requirement", Toast.LENGTH_LONG).show();

                            }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG);
                        }
                    }).create().show();

                }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG);
            }
        }).create().show();

    }
}
