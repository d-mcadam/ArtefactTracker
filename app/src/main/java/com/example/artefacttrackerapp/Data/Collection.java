package com.example.artefacttrackerapp.Data;

import java.util.ArrayList;

public class Collection {

    public final String title;
    public final String collector;
    public final ArrayList<String> artefacts;
    public final String category;
    public Boolean completedOnce = false;

    public Collection(String title, String collector, @GameArtefactCategory.Category String category){
        this.title = title;
        this.collector = collector;
        this.artefacts = new ArrayList<>();
        this.category = category;
    }

    public void Completed(){ this.completedOnce = true; }

}
