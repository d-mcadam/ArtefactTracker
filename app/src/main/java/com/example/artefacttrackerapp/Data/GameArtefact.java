package com.example.artefacttrackerapp.Data;

import java.util.ArrayList;

public class GameArtefact {

    public final String title;
    public final int quantity;
    public final String category;
    public final ArrayList<MaterialRequirement> requirements;

    public GameArtefact(String title, int quantity, @GameArtefactCategory.Category String category){
        this.title = title;
        this.quantity = quantity;
        this.category = category;
        this.requirements = new ArrayList<>();
    }

}
