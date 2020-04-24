package com.example.artefacttrackerapp.data;

import java.util.ArrayList;

public class GameArtefact {

    public final String title;
    public int quantity = 0;
    public final String category;
    public final ArrayList<MaterialRequirement> requirements = new ArrayList<>();

    public GameArtefact(String title, String category){
        this.title = title;
        this.category = category;
    }

    public final String Title(){ return this.title; }

}
