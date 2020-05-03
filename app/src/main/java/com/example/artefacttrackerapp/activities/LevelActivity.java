package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.LevelInfo;
import com.example.artefacttrackerapp.utilities.LevelDataAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.saveAppData;

public class LevelActivity extends AppCompatActivity {

    private EditText searchDataField;
    private Spinner spinner;

    private RecyclerView.Adapter dataAdapter;

    private final ArrayList<LevelInfo> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        init();
    }

    private void init(){

        searchDataField = findViewById(R.id.editTextSearchLevelData);
        searchDataField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshList(); }
            @Override
            public void afterTextChanged(Editable editable) { RefreshList(); }
        });

        RecyclerView dataRecyclerView = findViewById(R.id.recyclerViewLevelData);
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new LevelDataAdapter(this, displayList);
        dataRecyclerView.setAdapter(dataAdapter);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.level_order, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spinnerLevelInfoOrder);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { reorder(); }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { reorder(); }
        });

        RefreshList();

    }

    @Override
    public void onPause(){
        saveAppData(this, storage);
        super.onPause();
    }

    private void reorder(){
        switch (spinner.getSelectedItemPosition()){
            case 0:
                Collections.sort(displayList, Comparator.comparing(LevelInfo::Level).thenComparing(LevelInfo::Title).thenComparing(LevelInfo::Site));
                break;
            case 1:
                Collections.sort(displayList, Comparator.comparing(LevelInfo::Level).thenComparing(LevelInfo::Title).thenComparing(LevelInfo::Site).reversed());
                break;
            case 2:
                Collections.sort(displayList, Comparator.comparing(LevelInfo::Title).thenComparing(LevelInfo::Level).thenComparing(LevelInfo::Site));
                break;
            case 3:
                Collections.sort(displayList, Comparator.comparing(LevelInfo::Title).thenComparing(LevelInfo::Level).thenComparing(LevelInfo::Site).reversed());
                break;
            case 4:
                Collections.sort(displayList, Comparator.comparing(LevelInfo::Site).thenComparing(LevelInfo::Level).thenComparing(LevelInfo::Title));
                break;
            case 5:
                Collections.sort(displayList, Comparator.comparing(LevelInfo::Site).thenComparing(LevelInfo::Level).thenComparing(LevelInfo::Title).reversed());
                break;
            default:
                break;
        }
        dataAdapter.notifyDataSetChanged();
    }

    public void RefreshList(){

        displayList.clear();

        String textSearch = searchDataField.getText().toString().trim();

        storage.LevelInfos().stream()
                .filter(l -> (
                                textSearch.length() < 1 ||
                                l.rubbleName.toLowerCase().contains(textSearch.toLowerCase()) ||
                                l.digsite.toLowerCase().contains(textSearch.toLowerCase())
                            )
                ).forEach(displayList::add);

        ((LevelDataAdapter)dataAdapter).selectedPosition = -1;
        dataAdapter.notifyDataSetChanged();

    }
}
