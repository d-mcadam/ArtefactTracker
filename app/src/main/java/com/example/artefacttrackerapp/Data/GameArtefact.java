package com.example.artefacttrackerapp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameArtefact {

    public final String title;
    public int quantity = 0;
    public final String category;
    private final ArrayList<MaterialRequirement> requirements = new ArrayList<>();

    public GameArtefact(String title, String category){
        this.title = title;
        this.category = category;
    }

    public final String Title(){ return this.title; }

    public ArrayList<MaterialRequirement> getRequirements(){ return this.requirements; }
    public boolean addRequirement(MaterialRequirement materialRequirement){
        if (this.requirements.add(materialRequirement)){
            Collections.sort(this.requirements, Comparator.comparing(MaterialRequirement::Title));
            return true;
        }
        else return false;
    }
    public boolean deleteRequirement(MaterialRequirement materialRequirement){ return this.requirements.remove(materialRequirement); }

}
