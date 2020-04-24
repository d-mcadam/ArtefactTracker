package com.example.artefacttrackerapp.data;

import java.util.ArrayList;

public class Collection {

    public final String title;
    public final String collector;
    public final ArrayList<String> artefacts = new ArrayList<>();
    public final String category;
    public final String reward;
    public final int rewardQuantity;
    private Boolean completedOnce = false;

    public Collection(String title, String collector, String category, String reward, int qty){
        this.title = title;
        this.collector = collector;
        this.category = category;
        this.reward = reward;
        this.rewardQuantity = qty;
    }

    public boolean isCompleted(){ return this.completedOnce; }
    public void Completed(){ this.completedOnce = true; }

    public final String Title(){ return this.title; }

}
