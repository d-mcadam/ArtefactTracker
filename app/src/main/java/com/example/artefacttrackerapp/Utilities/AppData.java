package com.example.artefacttrackerapp.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.data.Storage;

import java.io.ObjectInputStream;

public class AppData {

    public static void saveAppData(Context context, Storage storage){
        new SaveAppData().execute(context, storage);
    }

    public static Storage loadAppData(Context context){
        try (ObjectInputStream ois = new ObjectInputStream(context.openFileInput(context.getResources().getString(R.string.runescape_artefact_tracker_mobile_app_data)))) {
            Storage s = (Storage) ois.readObject();
            Toast.makeText(context, "Loaded Storage", Toast.LENGTH_SHORT).show();
            return s;
        } catch (Exception e){
            Toast.makeText(context, "Initialising storage", Toast.LENGTH_LONG).show();
            e.printStackTrace();//uncomment line when debugging
            return new Storage(context);
        }
    }

}
