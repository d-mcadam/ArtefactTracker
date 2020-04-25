package com.example.artefacttrackerapp.data;

import com.example.artefacttrackerapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

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
            collectors.stream()
                    .filter(c -> c.name.equals(collection.collector))
                    .forEach(c -> c.collections.add(collection.title));
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
        createData();
    }

    private void createData(){

        for (int i = 1; i < 36; i++)
            this.materials.add("Material " + i);

        for (int i = 1; i < 101; i++)
            this.artefacts.add(new GameArtefact("Artefact " + i, "All"));

        for (int i = 1; i < 21; i++)
            this.collectors.add(new Collector("Collector " + i, "Location " + i));

        for (int i = 1; i < 51; i++)
            this.AddCollection(new Collection("Collection " + i, "Collector " + i, "All", "Any", 2));

        for (Collection collection : collections)
            collection.Completed();
    }

    public GameArtefact findGameArtefactByTitle(String title){
        Optional<GameArtefact> artefact = artefacts.stream().filter(a -> a.title.equals(title)).findFirst();
        return artefact.isPresent() ? artefact.get() : null;
    }

}
