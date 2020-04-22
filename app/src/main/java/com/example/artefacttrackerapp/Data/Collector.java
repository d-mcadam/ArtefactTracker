package com.example.artefacttrackerapp.Data;

import java.io.Serializable;
import java.util.ArrayList;

public class Collector implements Serializable {

    public final String name;
    public final String location;
    public final ArrayList<String> collections = new ArrayList<>();

    public Collector(String name, String location){
        this.name = name;
        this.location = location;
    }

}
