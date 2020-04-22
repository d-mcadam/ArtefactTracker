package com.example.artefacttrackerapp.Utilities;

import android.content.Context;
import android.content.Intent;

import com.example.artefacttrackerapp.Data.Storage;
import com.example.artefacttrackerapp.R;

public class UtilityMethods {

    public static Storage GetStorageFromIntent(Context context, Intent intent){
        return (Storage) intent.getSerializableExtra(context.getResources().getResourceName(R.string.intent_key_storage));
    }

}
