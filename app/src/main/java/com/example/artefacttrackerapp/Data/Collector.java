package com.example.artefacttrackerapp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Collector implements Serializable {

    public final String name;
    public final String location;
    private final ArrayList<String> collections = new ArrayList<>();

    public Collector(String name, String location){
        this.name = name;
        this.location = location;
    }

    public final String Name(){ return this.name; }

    public ArrayList<String> getCollections(){ return this.collections; }
    public boolean addCollection(String collection){
        if (this.collections.add(collection)){
            Collections.sort(this.collections);
            return true;
        }
        return false;
    }
    public boolean deleteCollection(String collection){ return this.collections.remove(collection); }

}
