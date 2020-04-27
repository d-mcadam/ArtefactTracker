package com.example.artefacttrackerapp.data;

import java.io.Serializable;

public class MaterialRequirement implements Serializable {

    public final String title;
    public final int quantity;

    public MaterialRequirement(String title, int quantity){
        this.title = title;
        this.quantity = quantity;
    }

    public final String Title(){ return this.title; }

}
