package com.example.artefacttrackerapp.Data;

import java.util.ArrayList;

public class Collection {

    public final String title;
    public final String collector;
    public final ArrayList<String> artefacts = new ArrayList<>();
    public final String category;
    public final String reward;
    public Boolean completedOnce = false;

    public Collection(String title, String collector, String category, String reward){
        this.title = title;
        this.collector = collector;
        this.category = category;
        this.reward = reward;
    }

    public void Completed(){ this.completedOnce = true; }

    public final String Title(){ return this.title; }

}
