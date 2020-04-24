package com.example.artefacttrackerapp.data;

import java.util.ArrayList;

public class Collector {

    public final String name;
    public final String location;
    public final ArrayList<String> collections = new ArrayList<>();

    public Collector(String name, String location){
        this.name = name;
        this.location = location;
    }

    public final String Name(){ return this.name; }

}
