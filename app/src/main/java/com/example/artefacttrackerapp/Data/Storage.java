package com.example.artefacttrackerapp.data;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.example.artefacttrackerapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.USING_LIVE_DATA;

public class Storage implements Serializable {

    public Storage(){
        this.artefacts = new ArrayList<>();
        this.collectors = new ArrayList<>();
        this.collections = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.levelInfos = new ArrayList<>();
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
        if (this.artefacts.remove(artefact)){
            this.collections.forEach(collection -> collection.deleteArtefact(artefact.title));
            return true;
        }
        return false;
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
        if (this.collectors.remove(collector)){
            ArrayList<Collection> removing = new ArrayList<>();
            this.collections.stream().filter(collection ->
                    collection.collector.equals(collector.name)).forEach(removing::add);
            removing.forEach(this.collections::remove);
            return true;
        }
        return false;
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
        if (this.collections.remove(collection)){
            this.collectors.forEach(collector -> collector.deleteCollection(collection.title));
            return true;
        }
        return false;
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
    public boolean DeleteMaterial(final Material material){
        if (this.materials.remove(material)){
            this.artefacts.forEach(artefact -> {
                ArrayList<MaterialRequirement> removing = new ArrayList<>();
                artefact.getRequirements().stream().filter(materialRequirement ->
                        materialRequirement.title.equals(material.title)).forEach(removing::add);
                removing.forEach(artefact::deleteRequirement);
            });
            return true;
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Level info">
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
