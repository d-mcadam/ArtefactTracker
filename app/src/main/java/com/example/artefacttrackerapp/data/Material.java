package com.example.artefacttrackerapp.data;

import java.util.ArrayList;
import java.util.Collections;

public class Material {

    public final String title;
    public int quantity = 0;
    private final ArrayList<String> locations = new ArrayList<>();

    public Material(String title){
        this.title = title;
    }

    public final String Title(){ return this.title; }

    public ArrayList<String> getLocations(){ return this.locations; }
    public boolean addLocation(String location){
        if (this.locations.add(location)){
            Collections.sort(this.locations);
            return true;
        }
        return false;
    }
    public boolean deleteLocation(String location){ return this.locations.remove(location); }

}
