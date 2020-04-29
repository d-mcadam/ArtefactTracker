package com.example.artefacttrackerapp.data;

import java.util.ArrayList;
import java.util.Collections;

public class LevelInfo {

    public final String rubbleName;
    public final String digsite;
    public final int level;
    private final ArrayList<String> materials = new ArrayList<>();
    private final ArrayList<String> artefacts = new ArrayList<>();

    public LevelInfo(String name, String site, int level){
        this.rubbleName = name;
        this.digsite = site;
        this.level = level;
    }

    public ArrayList<String> getArtefacts(){ return this.artefacts; }
    public boolean addArtefact(String artefact){
        if (this.artefacts.add(artefact)){
            Collections.sort(this.artefacts);
            return true;
        }
        return false;
    }
    public boolean deleteArtefact(String artefact){ return this.artefacts.remove(artefact); }

    public ArrayList<String> getMaterials(){ return this.materials; }
    public boolean addMaterial(String material){
        if (this.materials.add(material)){
            Collections.sort(this.materials);
            return true;
        }
        return false;
    }
    public boolean deleteMaterial(String material){ return this.materials.remove(material); }

}
