package com.example.artefacttrackerapp.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Storage {

    //<editor-fold defaultstate="collapsed" desc="Game Artefacts>
    private final ArrayList<GameArtefact> artefacts;
    public ArrayList<GameArtefact> Artefacts(){ return this.artefacts; }
    public boolean AddArtefact(GameArtefact artefact){
        if (this.artefacts.add(artefact)) {
            Collections.sort(this.artefacts, Comparator.comparing(GameArtefact::Title));
            return true;
        }
        return false;
    }
    public boolean DeleteArtefact(GameArtefact artefact){ return this.artefacts.remove(artefact); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collectors>
    private final ArrayList<Collector> collectors;
    public ArrayList<Collector> Collectors(){ return this.collectors; }
    public boolean AddCollector(Collector collector){
        if (this.collectors.add(collector)){
            Collections.sort(this.collectors, Comparator.comparing(Collector::Name));
            return true;
        }
        return false;
    }
    public boolean DeleteCollector(Collector collector){ return this.collectors.remove(collector); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collections>
    private final ArrayList<Collection> collections;
    public ArrayList<Collection> Collections(){ return this.collections; }
    public boolean AddCollection(Collection collection){
        if (this.collections.add(collection)){
            Collections.sort(this.collections, Comparator.comparing(Collection::Title));
            return true;
        }
        return false;
    }
    public boolean DeleteCollection(Collection collection){ return this.collections.remove(collection); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collections>
    private final ArrayList<String> materials;
    public ArrayList<String> Materials(){ return this.materials; }
    public boolean AddMaterial(String material){
        if (this.materials.add(material)){
            Collections.sort(this.materials);
            return true;
        }
        return false;
    }
    public boolean DeleteMaterial(String material){ return this.materials.remove(material); }
    //</editor-fold>

    public Storage(){
        this.artefacts = new ArrayList<>();
        this.collectors = new ArrayList<>();
        this.collections = new ArrayList<>();
        this.materials = new ArrayList<>();
    }

}
