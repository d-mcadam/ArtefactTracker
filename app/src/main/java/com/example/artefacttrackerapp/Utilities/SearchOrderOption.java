package com.example.artefacttrackerapp.utilities;

public enum SearchOrderOption {

    A_Z,
    Z_A,
    A_Z_COLLECT,
    Z_A_COLLECT,
    LOWEST_HIGHEST,
    HIGHEST_LOWEST,
    LOWEST_HIGHEST_COLLECT,
    HIGHEST_LOWEST_COLLECT,
    MOST_AVAILABLE;

    public String GetString(){
        switch (this){
            case A_Z:
                return "A > Z";
            case Z_A:
                return "Z > A";
            case A_Z_COLLECT:
                return "A > Z Collectible";
            case Z_A_COLLECT:
                return "Z > A Collectible";
            case LOWEST_HIGHEST:
                return "Lowest > Highest";
            case HIGHEST_LOWEST:
                return "Highest > Lowest";
            case LOWEST_HIGHEST_COLLECT:
                return "Lowest > Highest Collectible";
            case HIGHEST_LOWEST_COLLECT:
                return "Highest > Lowest Collectible";
            case MOST_AVAILABLE:
                return "Most Available To Collect";
            default:
                return "";
        }
    }

}
