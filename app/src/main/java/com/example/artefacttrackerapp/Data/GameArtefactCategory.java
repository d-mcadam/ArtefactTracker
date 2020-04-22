package com.example.artefacttrackerapp.Data;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GameArtefactCategory {

    public static final String ALL = "All";
    public static final String AGNOSTIC = "Agnostic";
    public static final String ARMADYL = "Armadyl";
    public static final String BANDOS = "Bandos";
    public static final String SARADOMIN = "Saradomin";
    public static final String ZAMORAK = "Zamorak";
    public static final String ZAROS = "Zaros";

    public GameArtefactCategory(@Category int category){}

    @StringDef({ALL, AGNOSTIC, ARMADYL, BANDOS, SARADOMIN, ZAMORAK, ZAROS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category{}

}
