package com.example.artefacttrackerapp.Data;

public class MaterialRequirement {

    public final String title;
    public final int quantity;

    public MaterialRequirement(String title, int quantity){
        this.title = title;
        this.quantity = quantity;
    }

    public final String Title(){ return this.title; }

}
