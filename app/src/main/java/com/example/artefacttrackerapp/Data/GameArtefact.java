package com.example.artefacttrackerapp.Data;

public class GameArtefact {

    public final String title;
    public final int quantity;
    public final String category;

    public GameArtefact(String title, int quantity, @GameArtefactCategory.Category String category){
        this.title = title;
        this.quantity = quantity;
        this.category = category;
    }

}
