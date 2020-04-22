package com.example.artefacttrackerapp.Data;

import java.util.ArrayList;

public class Collector {

    public final String name;
    public final String location;
    public final ArrayList<String> collections;

    public Collector(String name, String location){
        this.name = name;
        this.location = location;
        this.collections = new ArrayList<>();
    }

}
