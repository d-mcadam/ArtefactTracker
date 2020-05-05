package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.data.MaterialRequirement;
import com.example.artefacttrackerapp.utilities.GenReqArtefactAdapter;
import com.example.artefacttrackerapp.utilities.GenReqListAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class AnalysisActivity extends AppCompatActivity {

    private EditText searchField;

    private RecyclerView.Adapter adapter;

    private final ArrayList<GameArtefact> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        init();
    }

    private void init(){

        searchField = findViewById(R.id.editTextRequirementGenArtefactFilter);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void afterTextChanged(Editable editable) { RefreshList(); }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewGenReqArtefacts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GenReqArtefactAdapter(this, displayList);
        recyclerView.setAdapter(adapter);

        RefreshList();
    }

    public void RefreshList(){

        displayList.clear();

        String textSearch = searchField.getText().toString().trim();

        storage.Artefacts().stream().filter(a ->
                textSearch.length() < 1 || a.title.toLowerCase().contains(textSearch.toLowerCase())
        ).forEach(displayList::add);

        ((GenReqArtefactAdapter)adapter).selectedPosition = -1;
        adapter.notifyDataSetChanged();

    }

    public void GenerateList(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("# Required (# left to get)");

        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_generate_requirement_list, null);

        final ArrayList<MaterialRequirement> requirements = new ArrayList<>();
        ((GenReqArtefactAdapter)adapter).selectedList.forEach(a ->  {
            outerloop:
            for (MaterialRequirement r : a.getRequirements()) {
                for (MaterialRequirement mr : requirements) {
                    if (r.title.equals(mr.title)) {
                        mr.quantity += r.quantity;
                        continue outerloop;
                    }
                }
                MaterialRequirement newReq = new MaterialRequirement(r.title, 0);
                newReq.quantity += r.quantity;
                requirements.add(newReq);
            }
        });

        final RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewHolderMaterialRequirementGeneration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new GenReqListAdapter(this, requirements);
        recyclerView.setAdapter(adapter);

        dialog.setView(dialogView).setPositiveButton("OK", null).create().show();
    }

}
