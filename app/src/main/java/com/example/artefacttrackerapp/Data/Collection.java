package com.example.artefacttrackerapp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class Collection implements Serializable {

    public final String title;
    public final String collector;
    private final ArrayList<String> artefacts = new ArrayList<>();
    public final String category;
    public final String reward;
    public final int rewardQuantity;
    public Boolean completedOnce = false;
    public final String oneTimeOnlyReward;

    public Collection(String title, String collector, String category, String reward, int qty){
        this.title = title;
        this.collector = collector;
        this.category = category;
        this.reward = reward;
        this.rewardQuantity = qty;
        oneTimeOnlyReward = "N/A";
    }

    public Collection(String title, String collector, String category, String reward, int qty, String oneTimeOnlyReward){
        this.title = title;
        this.collector = collector;
        this.category = category;
        this.reward = reward;
        this.rewardQuantity = qty;
        this.oneTimeOnlyReward = oneTimeOnlyReward;
    }

    public boolean isCompleted(){ return this.completedOnce; }
    public void completeSubmission(){
        this.artefacts.forEach(
            a -> storage.Artefacts().stream()
                .filter(ga -> ga.title.equals(a))
                .forEach(ga -> ga.quantity--));
        this.completedOnce = true;
    }

    public final String Title(){ return this.title; }
    public final int Reward(){ return this.rewardQuantity; }

    public ArrayList<String> getArtefacts(){ return this.artefacts; }
    public boolean addArtefact(String artefact){
        if (this.artefacts.add(artefact)) {
            Collections.sort(this.artefacts);
            return true;
        }
        return false;
    }
    public boolean deleteArtefact(String artefact){ return this.artefacts.remove(artefact); }

    @Override
    public Object clone(){
        Collection clone = null;
        try{
            clone = (Collection)super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
        return clone;
    }

}
