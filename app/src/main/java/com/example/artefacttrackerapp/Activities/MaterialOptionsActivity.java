package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.Material;
import com.example.artefacttrackerapp.utilities.MaterialAdapter;
import com.example.artefacttrackerapp.utilities.RptUpdater;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.saveAppData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.AUTO_DECREMENTING;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.AUTO_INCREMENTING;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.decrementMaterialQuantity;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.HANDLER;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.incrementMaterialQuantity;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.resetModifyDelay;

public class MaterialOptionsActivity extends AppCompatActivity {

    private EditText materialSearchField;
    public ImageButton plusMaterialButton;
    public ImageButton minusMaterialButton;

    private TextView materialLabelField;
    private TextView materialArtefactOccurrenceCount;
    private TextView materialDemandCount;

    private RecyclerView.Adapter materialAdapter;

    private final ArrayList<Material> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_options);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){

        materialLabelField = findViewById(R.id.textViewSelectedMaterialTitle);
        materialArtefactOccurrenceCount = findViewById(R.id.textViewSelectedMaterialArtefactOccurrenceCount);
        materialDemandCount = findViewById(R.id.textViewSelectedMaterialDemandCount);

        //<editor-fold defaultstate="collapsed" desc="Search materials field">
        materialSearchField = findViewById(R.id.editTextSearchMaterials);
        materialSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void afterTextChanged(Editable editable) { RefreshList(); }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recycler view components">
        RecyclerView materialRecyclerView = findViewById(R.id.recyclerViewMaterialList);

        materialRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        materialAdapter = new MaterialAdapter(this, displayList);
        materialRecyclerView.setAdapter(materialAdapter);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="plus and minus buttons">

        plusMaterialButton = findViewById(R.id.imageButtonIncrementMaterialCount);
        plusMaterialButton.setOnClickListener(view ->
                incrementMaterialQuantity(
                        displayList.get(((MaterialAdapter)materialAdapter).selectedPosition),   //material
                        (MaterialAdapter)materialAdapter));                                     //adapter
        plusMaterialButton.setOnLongClickListener(view -> {
            AUTO_INCREMENTING = true;
            HANDLER.post(new RptUpdater(
                            this,
                            displayList.get(((MaterialAdapter)materialAdapter).selectedPosition),
                            (MaterialAdapter)materialAdapter));
            return false;
        });
        plusMaterialButton.setOnTouchListener((view, motionEvent) -> {
            if ((motionEvent.getAction() == MotionEvent.ACTION_UP ||
                    motionEvent.getAction() == MotionEvent.ACTION_CANCEL) &&
                    AUTO_INCREMENTING)
                AUTO_INCREMENTING = false;
            resetModifyDelay();
            return false;
        });

        minusMaterialButton = findViewById(R.id.imageButtonDecrementMaterialCount);
        minusMaterialButton.setOnClickListener(view ->
                decrementMaterialQuantity(
                    this,
                    displayList.get(((MaterialAdapter)materialAdapter).selectedPosition),
                    (MaterialAdapter)materialAdapter));
        minusMaterialButton.setOnLongClickListener(view -> {
            AUTO_DECREMENTING = true;
            HANDLER.post(new RptUpdater(
                            this,
                            displayList.get(((MaterialAdapter)materialAdapter).selectedPosition),
                            (MaterialAdapter)materialAdapter));
            return false;
        });
        minusMaterialButton.setOnTouchListener((view, motionEvent) -> {
            if ((motionEvent.getAction() == MotionEvent.ACTION_UP ||
                    motionEvent.getAction() == MotionEvent.ACTION_CANCEL) &&
                    AUTO_DECREMENTING)
                AUTO_DECREMENTING = false;
            resetModifyDelay();
            return false;
        });

        //</editor-fold>

        RefreshList();
    }

    @Override
    public void onPause(){
        saveAppData(this, storage);
        super.onPause();
    }

    public void SetSelectedMaterialDetails(Material material){
        if (material == null){
            String s = getString(R.string.material_opt_label_placeholder);
            materialLabelField.setText(s);
            materialArtefactOccurrenceCount.setText(s);
            materialDemandCount.setText(s);
        }else{
            materialLabelField.setText(material.title);

            int artefactOccurrenceCount = storage.Artefacts().stream()                   //for each artefact
                    .map(gameArtefact -> gameArtefact.getRequirements().stream()        //for each requirement on each artefact
                            .filter(mr -> mr.title.equals(material.title))              //filter requirements for the selected material
                        .map(mr -> 1).reduce(0, Integer::sum))                       //map each mr = to 1 and sum up total mr's
                    .reduce(0, Integer::sum);                                        //add up all the mr's on al the artefacts for the selected material
            materialArtefactOccurrenceCount.setText(String.valueOf(artefactOccurrenceCount));

            int demand = storage.Artefacts().stream().filter(artefact -> artefact.quantity > 0)                             //for each artefact, filter for artefacts with a quantity greater than zero
                    .map(artefact -> artefact.getRequirements().stream().filter(mr -> mr.title.equals(material.title))      //for each mr, filter for mr's with matching title to selected material
                            .map(mr -> mr.quantity * artefact.quantity).reduce(0, Integer::sum))                         //map each mr = to the mr quantity multiplied by the quantity of artefacts and sum up this total
                    .reduce(0, Integer::sum);                                                                            //sum up all mr quantities for all the artefact quantities
            materialDemandCount.setText(String.valueOf(demand));
        }
    }

    public void RefreshList(){

        displayList.clear();

        String searchText = materialSearchField.getText().toString().trim();

        storage.Materials().stream().filter(m ->
                searchText.length() < 1 || m.title.toLowerCase().contains(searchText.toLowerCase())
        ).forEach(displayList::add);

        ((MaterialAdapter)materialAdapter).selectedPosition = -1;
        materialAdapter.notifyDataSetChanged();

    }

    @SuppressLint("InflateParams")
    public boolean GenerateLocationInputDialog(Material material){
        final Context thisContext = this;

        AlertDialog.Builder dialog = new AlertDialog.Builder(thisContext);
        dialog.setTitle("Locations for " + material.title);

        View dialogView = LayoutInflater.from(thisContext).inflate(R.layout.dialog_material_location_display, null, false);
        final TextView textView = dialogView.findViewById(R.id.textViewHolderMaterialLocationDisplayMultiline);

        StringBuilder sb = new StringBuilder();
        material.getLocations().forEach(location -> sb.append(location).append("\n"));
        textView.setText(sb.toString().trim());

        dialog.setView(dialogView)
                .setPositiveButton("OK", null)
                .setNeutralButton("Add Location", (dialogInterface, i) -> {

                    android.app.AlertDialog.Builder locationInputDialog = new android.app.AlertDialog.Builder(thisContext);
                    locationInputDialog.setTitle("Add a location");

                    View locationDialogView = LayoutInflater.from(thisContext).inflate(R.layout.dialog_input_material_location, null, false);
                    final EditText editText = locationDialogView.findViewById(R.id.editTextInputMaterialLocation);

                    locationInputDialog.setView(locationDialogView)
                            .setPositiveButton("Add", (dialogInterface1, i1) -> {

                                final String inputLocation = editText.getText().toString().trim();
                                boolean r = material.addLocation(inputLocation);                    //add to logging system
                                GenerateLocationInputDialog(material);

                            }).setNegativeButton("Cancel", (dialogInterface1, i1) ->
                            Toast.makeText(thisContext, "Cancelled", Toast.LENGTH_LONG).show()).create().show();

                }).create().show();
        return true;
    }

    public void AddMaterial(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add a material");

        LayoutInflater inflater = getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_create_material, null);
        final EditText inputField = dialogView.findViewById(R.id.editTextInputMaterialName);
        inputField.setText(materialSearchField.getText().toString().trim());

        dialog.setView(dialogView)
            .setPositiveButton("Add", (dialogInterface, i) -> {

                final String inputText = inputField.getText().toString().trim();

                if (storage.Materials().stream().anyMatch(m -> m.title.equals(inputText))){
                    Toast.makeText(getBaseContext(), "Duplicate names detected.", Toast.LENGTH_LONG).show();
                    return;
                }

                storage.AddMaterial(new Material(inputText));
                ((MaterialAdapter)materialAdapter).selectedPosition = -1;
                materialAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Added material: " + inputText, Toast.LENGTH_LONG).show();
                materialSearchField.setText("");

            }).setNegativeButton("Cancel",  (dialogInterface, i) -> Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_LONG).show()).create().show();
    }
}
