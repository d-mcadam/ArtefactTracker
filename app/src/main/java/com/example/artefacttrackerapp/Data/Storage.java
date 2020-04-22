package com.example.artefacttrackerapp.Data;

import java.util.ArrayList;

public class Storage {

    //<editor-fold defaultstate="collapsed" desc="Game Artefacts>
    private final ArrayList<GameArtefact> artefacts;
    public ArrayList<GameArtefact> Artefacts(){ return this.artefacts; }
    public boolean AddArtefact(GameArtefact artefact){ return this.artefacts.add(artefact); }
    public boolean DeleteArtefact(GameArtefact artefact){ return this.artefacts.remove(artefact); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collectors>
    private final ArrayList<Collector> collectors;
    public ArrayList<Collector> Collectors(){ return this.collectors; }
    public boolean AddCollector(Collector collector){ return this.collectors.add(collector); }
    public boolean DeleteCollector(Collector collector){ return this.collectors.remove(collector); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collections>
    private final ArrayList<Collection> collections;
    public ArrayList<Collection> Collections(){ return this.collections; }
    public boolean AddCollection(Collection collection){ return this.collections.add(collection); }
    public boolean DeleteCollection(Collection collection){ return this.collections.remove(collection); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collections>
    private final ArrayList<String> materials;
    public ArrayList<String> Materials(){ return this.materials; }
    public boolean AddMaterial(String material){ return this.materials.add(material); }
    public boolean DeleteMaterial(String material){ return this.materials.remove(material); }
    //</editor-fold>

    public Storage(){
        this.artefacts = new ArrayList<>();
        this.collectors = new ArrayList<>();
        this.collections = new ArrayList<>();
        this.materials = new ArrayList<>();
    }

}
