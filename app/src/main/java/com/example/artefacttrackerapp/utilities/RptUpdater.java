package com.example.artefacttrackerapp.utilities;

import android.content.Context;

import com.example.artefacttrackerapp.data.Material;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.AUTO_DECREMENTING;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.AUTO_INCREMENTING;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.decrementMaterialQuantity;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.HANDLER;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.getRepeatDelay;
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
        if (AUTO_INCREMENTING){
            incrementMaterialQuantity(material, materialAdapter);
            HANDLER.postDelayed(new RptUpdater(context, material, materialAdapter), getRepeatDelay());
        }else if (AUTO_DECREMENTING){
            decrementMaterialQuantity(context, material, materialAdapter);
            HANDLER.postDelayed(new RptUpdater(context, material, materialAdapter), getRepeatDelay());
        }
    }
}
