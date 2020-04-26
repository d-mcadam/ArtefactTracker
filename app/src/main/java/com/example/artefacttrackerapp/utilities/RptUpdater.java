package com.example.artefacttrackerapp.utilities;

import android.content.Context;

import com.example.artefacttrackerapp.data.Material;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.autoDecrementing;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.autoIncrementing;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.decrementMaterialQuantity;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.handler;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.incrementMaterialQuantity;

public class RptUpdater implements Runnable {

    private final Context context;
    private final Material material;
    private final MaterialAdapter materialAdapter;

    public RptUpdater(Context context, Material material, MaterialAdapter materialAdapter) {
        this.context = context;
        this.material = material;
        this.materialAdapter = materialAdapter;
    }

    @Override
    public void run() {
        if (autoIncrementing){
            incrementMaterialQuantity(material, materialAdapter);
            handler.post(new RptUpdater(context, material, materialAdapter));
        }else if (autoDecrementing){
            decrementMaterialQuantity(context, material, materialAdapter);
            handler.post(new RptUpdater(context, material, materialAdapter));
        }
    }
}
