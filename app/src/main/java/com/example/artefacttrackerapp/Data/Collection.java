package com.example.artefacttrackerapp.Data;

import java.util.List;

public class Collection {

    public final String title;
    public final String collector;
    public final List<String> artefacts;
    public final String category;
    public Boolean completedOnce = false;

    public Collection(String title, String collector, List<String> artefacts, @GameArtefactCategory.Category String category){
        this.title = title;
        this.collector = collector;
        this.artefacts = artefacts;
        this.category = category;
    }

    public void Completed(){ this.completedOnce = true; }

}
