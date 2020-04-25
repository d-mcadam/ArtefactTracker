package com.example.artefacttrackerapp.data;

import java.util.ArrayList;

public class Material {

    public final String title;
    public int quantity = 0;
    public final ArrayList<String> locations = new ArrayList<>();

    public Material(String title){
        this.title = title;
    }

    public final String Title(){ return this.title; }

}
