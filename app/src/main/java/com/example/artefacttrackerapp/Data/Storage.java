package com.example.artefacttrackerapp.data;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.example.artefacttrackerapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.USING_LIVE_DATA;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.createLiveData;
import static com.example.artefacttrackerapp.utilities.UtilityMethods.createTestData;

public class Storage implements Serializable {

    private transient Context context;

    public Storage(Context context){
        this.context = context;

        this.artefacts = new ArrayList<>();
        this.collectors = new ArrayList<>();
        this.collections = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.levelInfos = new ArrayList<>();

        if (!USING_LIVE_DATA) {
            Toast.makeText(context, "Creating test data", Toast.LENGTH_SHORT).show();
            createTestData();
        } else {
            Toast.makeText(context, "Initialising storage", Toast.LENGTH_SHORT).show();
            createLiveData();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Game Artefacts">
    private final ArrayList<GameArtefact> artefacts;
    public ArrayList<GameArtefact> Artefacts(){ return this.artefacts; }
    public boolean AddArtefact(GameArtefact artefact){
        if (this.artefacts.add(artefact)) {
            Collections.sort(this.artefacts, Comparator.comparing(GameArtefact::Title));
            return true;
        }
        return false;
    }
    public boolean DeleteArtefact(GameArtefact artefact){
        return this.artefacts.remove(artefact);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collectors">
    private final ArrayList<Collector> collectors;
    public ArrayList<Collector> Collectors(){ return this.collectors; }
    public boolean AddCollector(Collector collector){
        if (this.collectors.add(collector)){
            Collections.sort(this.collectors, Comparator.comparing(Collector::Name));
            return true;
        }
        return false;
    }
    public boolean DeleteCollector(Collector collector){
        return this.collectors.remove(collector);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collections">
    private final ArrayList<Collection> collections;
    public ArrayList<Collection> Collections(){ return this.collections; }
    public boolean AddCollection(Collection collection){
        if (this.collections.add(collection)){
            Collections.sort(this.collections, Comparator.comparing(Collection::Title));
            collectors.stream()
                    .filter(collector -> collector.name.equals(collection.collector))
                    .forEach(collector -> {
                        boolean r = collector.addCollection(collection.title);
                    });
            return true;
        }
        return false;
    }
    public boolean DeleteCollection(Collection collection){
        return this.collections.remove(collection);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Materials">
    private final ArrayList<Material> materials;
    public ArrayList<Material> Materials(){ return this.materials; }
    public boolean AddMaterial(Material material){
        if (this.materials.add(material)){
            Collections.sort(this.materials, Comparator.comparing(Material::Title));
            return true;
        }
        return false;
    }
    public boolean DeleteMaterial(Material material){
        return this.materials.remove(material);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="level info">
    private final ArrayList<LevelInfo> levelInfos;
    public ArrayList<LevelInfo> LevelInfos(){ return this.levelInfos; }
    public boolean AddLevelInfo(LevelInfo levelInfo){
        if (this.levelInfos.add(levelInfo)){
            Collections.sort(this.levelInfos, Comparator.comparing(LevelInfo::Level));
            return true;
        }
        return false;
    }
    public boolean DeleteLevelInfo(LevelInfo levelInfo){
        return this.levelInfos.remove(levelInfo);
    }
    //</editor-fold>

}
