package com.example.artefacttrackerapp.data;

import android.content.res.Resources;

import com.example.artefacttrackerapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

public class Storage {

    //<editor-fold defaultstate="collapsed" desc="Game Artefacts">
    private final ArrayList<GameArtefact> artefacts;
    public ArrayList<GameArtefact> Artefacts(){ return this.artefacts; }
    public boolean AddArtefact(GameArtefact artefact){
        if (this.artefacts.add(artefact)) {
            Collections.sort(this.artefacts, Comparator.comparing(GameArtefact::Title));
            return true;
        }
        return false;
    }
    public boolean DeleteArtefact(GameArtefact artefact){ return this.artefacts.remove(artefact); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collectors">
    private final ArrayList<Collector> collectors;
    public ArrayList<Collector> Collectors(){ return this.collectors; }
    public boolean AddCollector(Collector collector){
        if (this.collectors.add(collector)){
            Collections.sort(this.collectors, Comparator.comparing(Collector::Name));
            return true;
        }
        return false;
    }
    public boolean DeleteCollector(Collector collector){ return this.collectors.remove(collector); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collections">
    private final ArrayList<Collection> collections;
    public ArrayList<Collection> Collections(){ return this.collections; }
    public boolean AddCollection(Collection collection){
        if (this.collections.add(collection)){
            Collections.sort(this.collections, Comparator.comparing(Collection::Title));
            collectors.stream()
                    .filter(collector -> collector.name.equals(collection.collector))
                    .forEach(collector -> {
                        boolean r = collector.addCollection(collection.title);
                    });
            return true;
        }
        return false;
    }
    public boolean DeleteCollection(Collection collection){ return this.collections.remove(collection); }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Materials">
    private final ArrayList<Material> materials;
    public ArrayList<Material> Materials(){ return this.materials; }
    public boolean AddMaterial(Material material){
        if (this.materials.add(material)){
            Collections.sort(this.materials, Comparator.comparing(Material::Title));
            return true;
        }
        return false;
    }
    public boolean DeleteMaterial(Material material){ return this.materials.remove(material); }
    //</editor-fold>

    private final Resources cRes;
    public Storage(Resources resources){
        cRes = resources;
        this.artefacts = new ArrayList<>();
        this.collectors = new ArrayList<>();
        this.collections = new ArrayList<>();
        this.materials = new ArrayList<>();
        createTestData();
    }

    private String rndStr(int l){
        String alphaStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l; i++)
            sb.append(alphaStr.charAt(new Random().nextInt(alphaStr.length())));
        return sb.toString();
    }
    private int rndNum(int max){
        return rndNum(0, max);
    }
    private int rndNum(int min, int max){
        return new Random().nextInt(max - min) + min;
    }
    private String rndCategory(){
        String[] categories = cRes.getStringArray(R.array.artefact_categories);
        return categories[rndNum(categories.length)];
    }
    private String rndReward(){
        String[] rewards = cRes.getStringArray(R.array.collection_rewards);
        return rewards[rndNum(rewards.length)];
    }
    private GameArtefact genRndGa(){
        GameArtefact ga = new GameArtefact(
                rndStr(rndNum(4, 10)),
                rndCategory()
        );
        ga.quantity = rndNum(10);
        int r = rndNum(3, 5);
        for (int i = 0; i < r; i++)
            ga.addRequirement(new MaterialRequirement(this.materials.get(rndNum(this.materials.size())).title, rndNum(5, 15)));

        return ga;
    }
    private void createTestData(){

        Material m1 = new Material(rndStr(rndNum(4, 10)));
        Material m2 = new Material(rndStr(rndNum(4, 10)));
        Material m3 = new Material(rndStr(rndNum(4, 10)));
        Material m4 = new Material(rndStr(rndNum(4, 10)));
        Material m5 = new Material(rndStr(rndNum(4, 10)));
        ArrayList<Material> ma = new ArrayList<Material>() {{ add(m1); add(m2); add(m3); add(m4); add(m5); }};
        ma.forEach(material -> {
            material.quantity = rndNum(5, 200);
            for (int i = 0; i < 5; i++)
                material.addLocation(rndStr(rndNum(8, 10)));
        });
        this.materials.addAll(ma);

        GameArtefact a1 = genRndGa();
        GameArtefact a2 = genRndGa();
        GameArtefact a3 = genRndGa();
        GameArtefact a4 = genRndGa();
        GameArtefact a5 = genRndGa();
        ArrayList<GameArtefact> aa = new ArrayList<GameArtefact>() {{ add(a1); add(a2); add(a3); add(a4); add(a5); }};
        this.artefacts.addAll(aa);

        for (int i = 0; i < 5; i++)
            this.collectors.add(new Collector(rndStr(rndNum(4, 10)), rndStr(rndNum(4, 10))));

        for (int i = 0; i < 5; i++){

            Collection collection = new Collection(
                    rndStr(rndNum(4, 10)),
                    collectors.get(rndNum(collectors.size())).name,
                    rndCategory(),
                    rndReward(),
                    rndNum(1000)
            );

            if (new Random().nextBoolean())
                collection.hasBeenCompleted();

            int r = rndNum(2, 4);
            for (int j = 0; j < r; j++)
                collection.addArtefact(artefacts.get(rndNum(artefacts.size())).title);

            AddCollection(collection);

        }

    }

    public GameArtefact findGameArtefactByTitle(String title){
        Optional<GameArtefact> artefact = artefacts.stream().filter(a -> a.title.equals(title)).findFirst();
        return artefact.isPresent() ? artefact.get() : null;
    }

}
