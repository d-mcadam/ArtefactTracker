package com.example.artefacttrackerapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.artefacttrackerapp.Data.Collection;
import com.example.artefacttrackerapp.Data.GameArtefact;
import com.example.artefacttrackerapp.Data.MaterialRequirement;
import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.Utilities.MaterialRequirementAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.artefacttrackerapp.Activities.MainActivity.storage;

public class AddArtefactActivity extends AppCompatActivity {

    private Button saveButton;
    private Spinner categorySpinner;
    private EditText artefactNameField;

    private RecyclerView.Adapter matReqAdapter;

    public final ArrayList<MaterialRequirement> requirementArrayList = new ArrayList<>();

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

        Intent intent = getIntent();
        String inputName = intent.getStringExtra("STRING_INPUT");

        saveButton = findViewById(R.id.buttonSaveArtefact);

        //<editor-fold defaultstate="collapsed" desc="Category spinner">
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.artefact_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinnerAddArtefactCategory);
        categorySpinner.setAdapter(categoryAdapter);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Artefact name field">
        artefactNameField = findViewById(R.id.editTextInputArtefactName);
        artefactNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { CheckSaveEligibility(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { CheckSaveEligibility(); }
            @Override
            public void afterTextChanged(Editable editable) { CheckSaveEligibility(); }
        });
        artefactNameField.setText(inputName);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recycler view components">
        RecyclerView matReqRecyclerView = findViewById(R.id.recyclerViewRequirementList);

        matReqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        matReqAdapter = new MaterialRequirementAdapter(this, requirementArrayList);
        matReqRecyclerView.setAdapter(matReqAdapter);
        //</editor-fold>

    }

    public void CheckSaveEligibility(){
        boolean nameFilledIn = artefactNameField.getText().toString().trim().length() > 0;
        boolean requirementEntered = requirementArrayList.size() > 0;
        boolean uniqueTitle = storage.Artefacts().stream().noneMatch(a -> a.title.equals(artefactNameField.getText().toString().trim()));

        saveButton.setClickable(nameFilledIn && requirementEntered && uniqueTitle);
        saveButton.setTextColor(getColor(saveButton.isClickable() ? R.color.colourBlackText : R.color.colourButtonDisabledText));

        String tooltipText = !nameFilledIn ? "Name needs to be entered" : !requirementEntered ? "Needs at least one requirement entry." : !uniqueTitle ? "Title must be unique" : "Save artefact";
        saveButton.setTooltipText(tooltipText);
    }

    public void AddMaterialRequirement(View v){

        final Context thisContext = this;

        AlertDialog.Builder nameDialog = new AlertDialog.Builder(thisContext);
        nameDialog.setTitle("Material Name");

        final LayoutInflater inflater = getLayoutInflater();

        View nameDialogView = inflater.inflate(R.layout.dialog_material_name, null);

        ArrayList<String> spinnerValues = new ArrayList<>();
        storage.Materials().stream()
                .filter(m -> requirementArrayList.stream().noneMatch(i -> i.title.equals(m)))
                .forEach(spinnerValues::add);

        final Spinner nameDialogSpinner = nameDialogView.findViewById(R.id.spinnerMaterialNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(thisContext, R.layout.support_simple_spinner_dropdown_item, spinnerValues);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        nameDialogSpinner.setAdapter(adapter);

        nameDialog.setView(nameDialogView)
            .setPositiveButton("Next", (dialogInterface, i) -> {

                final String name = nameDialogSpinner.getSelectedItem().toString();

                AlertDialog.Builder qtyDialog = new AlertDialog.Builder(thisContext);
                qtyDialog.setTitle("Quantity Required");

                View qtyDialogView = inflater.inflate(R.layout.dialog_material_quantity, null);
                final EditText qtyDialogField = qtyDialogView.findViewById(R.id.editTextMaterialReqQuantity);

                qtyDialog.setView(qtyDialogView)
                    .setPositiveButton("Save", (dialogInterface12, i1) -> {

                        int quantity = Integer.parseInt(qtyDialogField.getText().toString().trim());

                        MaterialRequirement materialRequirement = new MaterialRequirement(name, quantity);

                        requirementArrayList.add(materialRequirement);
                        Collections.sort(requirementArrayList, Comparator.comparing(MaterialRequirement::Title));
                        ((MaterialRequirementAdapter)matReqAdapter).selectedPosition = -1;
                        matReqAdapter.notifyDataSetChanged();
                        CheckSaveEligibility();

                        Toast.makeText(thisContext, "Saved requirement", Toast.LENGTH_LONG).show();

                    }).setNegativeButton("Cancel", (dialogInterface1, i1) -> Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG)).create().show();

            }).setNeutralButton("Add Material", (dialogInterface, i) -> {

                AlertDialog.Builder addMaterialDialog = new AlertDialog.Builder(thisContext);
                addMaterialDialog.setTitle("Add Material");

                View addMaterialDialogView = inflater.inflate(R.layout.dialog_create_material, null);
                final EditText addMaterialDialogField = addMaterialDialogView.findViewById(R.id.editTextInputMaterialName);

                addMaterialDialog.setView(addMaterialDialogView)
                    .setPositiveButton("Save", (dialogInterface1, i1) -> {

                        String inputText = addMaterialDialogField.getText().toString().trim();

                        if (storage.Materials().stream().anyMatch(m -> m.equals(inputText))){
                            Toast.makeText(getBaseContext(), "Duplicate names detected.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        storage.AddMaterial(inputText);

                        AddMaterialRequirement(null);

                    }).setNegativeButton("Cancel", (dialogInterface1, i1) -> Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG)).create().show();

            }).setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG)).create().show();
    }

    public void SaveArtefact(View v){

        final String title = artefactNameField.getText().toString().trim();
        final String category = categorySpinner.getSelectedItem().toString();

        GameArtefact artefact = new GameArtefact(title, category);

        artefact.requirements.addAll(requirementArrayList);

        storage.AddArtefact(artefact);

        int reqSize = artefact.requirements.size();
        Toast.makeText(this, "Added Artefact with " + reqSize + " requirement" + (reqSize == 1 ? "" : "s"), Toast.LENGTH_LONG).show();
        goBack();

    }
}
