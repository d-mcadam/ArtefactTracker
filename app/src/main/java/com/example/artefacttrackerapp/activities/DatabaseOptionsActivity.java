package com.example.artefacttrackerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.artefacttrackerapp.R;

import java.io.File;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.USING_LIVE_DATA;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.loadAppData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.saveDatabaseOption;

public class DatabaseOptionsActivity extends AppCompatActivity {

    private RadioButton testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_options);
        init();
    }

    private void init(){
        testButton = findViewById(R.id.radioButtonTestData);
        testButton.setChecked(!USING_LIVE_DATA);
    }

    @Override
    public void onPause(){
        saveDatabaseOption(getBaseContext());
        super.onPause();
    }

    public void RadioButtonSwitch(View v) {
        USING_LIVE_DATA = v.getId() != R.id.radioButtonTestData;
        storage = loadAppData(getBaseContext());
    }

    public void ResetLiveData(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Warning").setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setMessage("You're about to reset all information you've modified in the database.\n\n" +
                "This will overwrite what is saved in the Internal Storage.");
        dialog.setPositiveButton("Continue", (dialogInterface, i) -> {
            boolean r = new File(getFilesDir(), getString(R.string.runescape_artefact_tracker_mobile_app_data)).delete();
            storage = loadAppData(getBaseContext());
        }).setNegativeButton("Cancel", null).create().show();
    }

}
