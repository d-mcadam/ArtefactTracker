package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.LevelInfo;
import com.example.artefacttrackerapp.utilities.LevelDataAdapter;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class LevelActivity extends AppCompatActivity {

    private EditText searchDataField;

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



        RefreshList();

    }

    public void RefreshList(){

        displayList.clear();

        String textSearch = searchDataField.getText().toString().trim();

        storage.LevelInfos().stream()
                .filter(l -> textSearch.length() < 1 || l.rubbleName.toLowerCase().contains(textSearch.toLowerCase()))
                .forEach(displayList::add);

        ((LevelDataAdapter)dataAdapter).selectedPosition = -1;
        dataAdapter.notifyDataSetChanged();

    }
}
