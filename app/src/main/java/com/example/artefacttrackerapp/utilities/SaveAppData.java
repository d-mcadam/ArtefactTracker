package com.example.artefacttrackerapp.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.artefacttrackerapp.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveAppData extends AsyncTask<Object, Void, Context> {

    @Override
    protected Context doInBackground(Object... objects) {
        Context c = (Context)objects[0];
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(c.openFileOutput(c.getResources().getString(R.string.runescape_artefact_tracker_mobile_app_data),Context.MODE_PRIVATE))){
            objectOutputStream.writeObject(objects[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

}
