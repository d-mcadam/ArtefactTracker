package com.example.artefacttrackerapp.Utilities;

public enum RewardSearchOption {

    ANY,
    CRONOTES,
    TETRA_PIECES,
    ROBUST_GLASS,
    BLUEPRINT_FRAGMENTS,
    PYLON_BATTERIES;

    public String GetString(){
        switch (this){
            case ANY:
                return "Any";
            case BLUEPRINT_FRAGMENTS:
                return "Stormguard blueprint fragments";
            case CRONOTES:
                return "Cronotes";
            case PYLON_BATTERIES:
                return "Pylon batteries";
            case ROBUST_GLASS:
                return "Robust glass";
            case TETRA_PIECES:
                return "Tetra pieces";
            default:
                return "";
        }
    }

}
