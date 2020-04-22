package com.example.artefacttrackerapp.Data;

import java.io.Serializable;
import java.util.ArrayList;

public class Collection implements Serializable {

    public final String title;
    public final String collector;
    public final ArrayList<String> artefacts = new ArrayList<>();
    public final String category;
    public Boolean completedOnce = false;

    public Collection(String title, String collector, String category){
        this.title = title;
        this.collector = collector;
        this.category = category;
    }

    public void Completed(){ this.completedOnce = true; }

}
