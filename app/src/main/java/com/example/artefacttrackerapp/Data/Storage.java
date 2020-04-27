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
//        createTestData();
        createLiveData();
    }

    //<editor-fold defaultstate="collapsed" desc="Test data">
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
        ma.forEach(this::AddMaterial);

        GameArtefact a1 = genRndGa();
        GameArtefact a2 = genRndGa();
        GameArtefact a3 = genRndGa();
        GameArtefact a4 = genRndGa();
        GameArtefact a5 = genRndGa();
        ArrayList<GameArtefact> aa = new ArrayList<GameArtefact>() {{ add(a1); add(a2); add(a3); add(a4); add(a5); }};
        aa.forEach(this::AddArtefact);

        for (int i = 0; i < 5; i++)
            AddCollector(new Collector(rndStr(rndNum(4, 10)), rndStr(rndNum(4, 10))));

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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Live Data Reset">

    private void createLiveData(){
        //<editor-fold defaultstate="collapsed" desc="Materials">
        Material m1 = new Material("Aetherium alloy");
        m1.addLocation("Stormguard Citadel, Research & Development");
        m1.addLocation("Empyrean Citadel");

        Material m2 = new Material("Ancient vis");
        m2.addLocation("Kharid-et, Culinarum");
        m2.addLocation("Slayer Tower, Top Floor");

        Material m3 = new Material("Animal furs");
        m3.addLocation("Feldip");

        Material m4 = new Material("Armadylean yellow");
        m4.addLocation("Empyrean Citadel");

        Material m5 = new Material("Blood of Orcus");
        m5.addLocation("Kharid-et, Chapel");
        m5.addLocation("Slayer Tower, Top Floor");

        Material m6 = new Material("Cadmium red");
        m6.addLocation("Infernal Source");
        m6.addLocation("First Wizards Tower");

        Material m7 = new Material("Chaotic brimstone");
        m7.addLocation("Infernal Source, Vestibule of Futility");
        m7.addLocation("Daemonheim");

        Material m8 = new Material("Cobalt blue");
        m8.addLocation("Everlight, Amphitheatre");
        m8.addLocation("GWD, Saradomin");

        Material m9 = new Material("Demonhide");
        m9.addLocation("Infernal Source, The Harrowing");
        m9.addLocation("GWD, Zamorak");

        Material m10 = new Material("Everlight silvthril");
        m10.addLocation("Everlight, Dominion Stadium");
        m10.addLocation("Barrows");

        Material m11 = new Material("Eye of Dagon");
        m11.addLocation("Infernal Source, Dungeon of Disorder");
        m11.addLocation("Daemonheim");

        Material m12 = new Material("Fossilised bone");
        m12.addLocation("Ancient Cavern");
        m12.addLocation("Odd Old Man");

        Material m13 = new Material("Goldrune");
        m13.addLocation("Camdozaal");

        Material m14 = new Material("Hellfire metal");
        m14.addLocation("GWD, Zamorak");
        m14.addLocation("Infernal source");

        Material m15 = new Material("Imperial steel");
        m15.addLocation("Empty Throne Room");
        m15.addLocation("Kharid-et, Barracks");

        Material m16 = new Material("Keramos");
        m16.addLocation("GWD, Saradomin");
        m16.addLocation("Everlight");

        Material m17 = new Material("Leather scraps");
        m17.addLocation("Morytania");

        Material m18 = new Material("Malachite green");
        m18.addLocation("GWD, Bandos");
        m18.addLocation("Warforge");

        Material m19 = new Material("Mark of the Kyzaj");
        m19.addLocation("GWD, bandos");
        m19.addLocation("Warforge");

        Material m20 = new Material("Orthenglass");
        m20.addLocation("Anachronia");

        Material m21 = new Material("Quintessence");
        m21.addLocation("Empyrean Citadel");
        m21.addLocation("Stormguard Citadel");

        Material m22 = new Material("Samite silk");
        m22.addLocation("Al Kharid");
        m22.addLocation("Kharid-et, Exterior");

        Material m23 = new Material("Soapstone");
        m23.addLocation("Waiko");

        Material m24 = new Material("Star of Saradomin");
        m24.addLocation("Barrows");
        m24.addLocation("Everlight");

        Material m25 = new Material("Stormguard steel");
        m25.addLocation("GWD, Armadyl");
        m25.addLocation("Stormguard Citadel, Research & Development");

        Material m26 = new Material("Third Age iron");
        m26.addLocation("Arch Guild");

        Material m27 = new Material("Tyrian purple");
        m27.addLocation("Empty Throne Room");
        m27.addLocation("Kharid-et, Barracks");

        Material m28 = new Material("Vellum");
        m28.addLocation("First Wizards Tower");

        Material m29 = new Material("Vulcanised rubber");
        m29.addLocation("Feldip");
        m29.addLocation("Warforge");

        Material m30 = new Material("Warforged bronze");
        m30.addLocation("GWD, Bandos");
        m30.addLocation("Warforge");

        Material m31 = new Material("White marble");
        m31.addLocation("Everlight");
        m31.addLocation("First Wizards Tower");

        Material m32 = new Material("White oak");
        m32.addLocation("Ice Mountain");

        Material m33 = new Material("Wings of War");
        m33.addLocation("GWD, Armadyl");

        Material m34 = new Material("Yu'biusk clay");
        m34.addLocation("Feldip");
        m34.addLocation("Warforge");

        Material m35 = new Material("Zarosian insignia");
        m35.addLocation("Empyt Throne Room");
        m35.addLocation("Kharid-et Barracks");
        //</editor-fold>
        ArrayList<Material> liveMaterialArray = new ArrayList<Material>(){{
            add(m1);add(m2);add(m3);add(m4);add(m5);add(m6);add(m7);add(m8);add(m9);add(m10);
            add(m11);add(m12);add(m13);add(m14);add(m15);add(m16);add(m17);add(m18);add(m19);add(m20);
            add(m21);add(m22);add(m23);add(m24);add(m25);add(m26);add(m27);add(m28);add(m29);add(m30);
            add(m31);add(m32);add(m33);add(m34);add(m35);
        }};
        liveMaterialArray.forEach(this::AddMaterial);

        //<editor-fold defaultstate="collapsed" desc="Game Artefact objects">
        GameArtefact ga1 = new GameArtefact("'Animate dead' spell scroll", "Zaros");
        ga1.addRequirement(new MaterialRequirement("Vellum", 40));
        ga1.addRequirement(new MaterialRequirement("Ancient vis", 24));
        ga1.addRequirement(new MaterialRequirement("Blood or Orcus", 40));

        GameArtefact ga2 = new GameArtefact("'Consensus ad Idem' painting", "Zaros");
        ga2.addRequirement(new MaterialRequirement("White oak", 10));
        ga2.addRequirement(new MaterialRequirement("Samite silk", 10));
        ga2.addRequirement(new MaterialRequirement("Tyrian purple", 50));

        GameArtefact ga3 = new GameArtefact("'Da Boss Man' sculpture", "Bandos");
        ga3.addRequirement(new MaterialRequirement("Yu'biusk clay", 50));
        ga3.addRequirement(new MaterialRequirement("Malachite green", 44));
        ga3.addRequirement(new MaterialRequirement("Soapstone", 44));

        GameArtefact ga4 = new GameArtefact("'Disorder' painting", "Zamorak");
        ga4.addRequirement(new MaterialRequirement("Samite silk", 6));
        ga4.addRequirement(new MaterialRequirement("White oak", 6));
        ga4.addRequirement(new MaterialRequirement("Vellum", 6));
        ga4.addRequirement(new MaterialRequirement("Cadmium red", 14));

        GameArtefact ga5 = new GameArtefact("'Exsanguinate' spell scroll", "Zaros");
        ga5.addRequirement(new MaterialRequirement("Vellum", 40));
        ga5.addRequirement(new MaterialRequirement("Blood or Orcus", 36));

        GameArtefact ga6 = new GameArtefact("'Forged in War' sculpture", "Bandos");
        ga6.addRequirement(new MaterialRequirement("Warforged bronze", 50));
        ga6.addRequirement(new MaterialRequirement("Yu'biusk clay", 42));

        GameArtefact ga7 = new GameArtefact("'Frying pan'", "Saradomin");
        ga7.addRequirement(new MaterialRequirement("Third Age iron", 20));
        ga7.addRequirement(new MaterialRequirement("White marble", 24));

        GameArtefact ga8 = new GameArtefact("'Hallowed Be the Everlight' painting", "Saradomin");
        ga8.addRequirement(new MaterialRequirement("Cobalt blue", 52));
        ga8.addRequirement(new MaterialRequirement("White oak", 16));
        ga8.addRequirement(new MaterialRequirement("Samite silk", 16));
        ga8.addRequirement(new MaterialRequirement("Vellum", 16));

        GameArtefact ga9 = new GameArtefact("'Incite fear' spell scroll", "Zaros");
        ga9.addRequirement(new MaterialRequirement("Vellum", 20));
        ga9.addRequirement(new MaterialRequirement("Ancient vis", 18));
        ga9.addRequirement(new MaterialRequirement("Blood of Orcus", 18));

        GameArtefact ga10 = new GameArtefact("'Lust' metal sculpture", "Zamorak");
        ga10.addRequirement(new MaterialRequirement("Third Age iron", 16));
        ga10.addRequirement(new MaterialRequirement("Eye of Dagon", 24));
        ga10.addRequirement(new MaterialRequirement("Goldrune", 24));

        GameArtefact ga11 = new GameArtefact("'Nosorog!' sculpture", "Bandos");
        ga11.addRequirement(new MaterialRequirement("Yu'biusk clay", 30 ));
        ga11.addRequirement(new MaterialRequirement("Malachite green", 24 ));
        ga11.addRequirement(new MaterialRequirement("Warforged bronze", 30 ));

        GameArtefact ga12 = new GameArtefact("'Pandemonium' tapestry", "Zamorak");
        ga12.addRequirement(new MaterialRequirement("White oak", 12 ));
        ga12.addRequirement(new MaterialRequirement("Samite silk", 12 ));
        ga12.addRequirement(new MaterialRequirement("Vellum", 12 ));
        ga12.addRequirement(new MaterialRequirement("Cadmium red", 42 ));

        GameArtefact ga13 = new GameArtefact("'Possession' metal sculpture", "Zamorak");
        ga13.addRequirement(new MaterialRequirement("Third Age iron", 44));
        ga13.addRequirement(new MaterialRequirement("Eye of Dagon", 24));
        ga13.addRequirement(new MaterialRequirement("Chaotic brimstone", 30 ));

        GameArtefact ga14 = new GameArtefact("'Prima legio' painting", "Zaros");
        ga14.addRequirement(new MaterialRequirement("White oak", 20  ));
        ga14.addRequirement(new MaterialRequirement("Samite silk", 20  ));
        ga14.addRequirement(new MaterialRequirement("Tyrian purple", 74 ));
        ga14.addRequirement(new MaterialRequirement("Zarosian insignia", 20 ));

        GameArtefact ga15 = new GameArtefact("'Smoke cloud' spell scroll", "Zaros");
        ga15.addRequirement(new MaterialRequirement("Vellum", 40));
        ga15.addRequirement(new MaterialRequirement("Ancient vis", 20));
        ga15.addRequirement(new MaterialRequirement("Blood of Orcus", 32));

        GameArtefact ga16 = new GameArtefact("'Solem in Umbra' painting", "Zaros");
        ga16.addRequirement(new MaterialRequirement("White oak", 10 ));
        ga16.addRequirement(new MaterialRequirement("Samite silk", 8 ));
        ga16.addRequirement(new MaterialRequirement("Tyrian purple", 14 ));

        GameArtefact ga17 = new GameArtefact("'The Enlightened Soul' scroll", "Saradomin");
        ga17.addRequirement(new MaterialRequirement("Vellum", 60 ));
        ga17.addRequirement(new MaterialRequirement("Star of Saradomin", 50 ));

        GameArtefact ga18 = new GameArtefact("'The Eudoxian Elements' tablet", "Saradomin");
        ga18.addRequirement(new MaterialRequirement("White marble", 60 ));
        ga18.addRequirement(new MaterialRequirement("Gold rune", 50 ));

        GameArtefact ga19 = new GameArtefact("'The Lake of Fire' painting", "Zamorak");
        ga19.addRequirement(new MaterialRequirement("White oak", 10 ));
        ga19.addRequirement(new MaterialRequirement("Samite silk", 10 ));
        ga19.addRequirement(new MaterialRequirement("Vellum", 10 ));
        ga19.addRequirement(new MaterialRequirement("Cadmium red", 34 ));

        GameArtefact ga20 = new GameArtefact("'The Lord of Light' painting", "Saradomin");
        ga20.addRequirement(new MaterialRequirement("White oak", 16 ));
        ga20.addRequirement(new MaterialRequirement("Samite silk", 16 ));
        ga20.addRequirement(new MaterialRequirement("Vellum", 16 ));
        ga20.addRequirement(new MaterialRequirement("Cobalt blue", 52 ));

        GameArtefact ga21 = new GameArtefact("'The Pride of Padosan' painting", "Saradomin");
        ga21.addRequirement(new MaterialRequirement("White oak", 16 ));
        ga21.addRequirement(new MaterialRequirement("Samite silk", 16 ));
        ga21.addRequirement(new MaterialRequirement("Vellum", 16 ));
        ga21.addRequirement(new MaterialRequirement("Cobalt blue", 52 ));

        GameArtefact ga22 = new GameArtefact("'Torment' metal sculpture", "Zamorak");
        ga22.addRequirement(new MaterialRequirement("Third Age iron", 20));
        ga22.addRequirement(new MaterialRequirement("Eye of Dagon", 20));
        ga22.addRequirement(new MaterialRequirement("Hellfire metal", 38 ));

        GameArtefact ga23 = new GameArtefact("Amphora", "Saradomin");
        ga23.addRequirement(new MaterialRequirement("Everlight silvthril", 34));
        ga23.addRequirement(new MaterialRequirement("Keramos", 46 ));

        GameArtefact ga24 = new GameArtefact("Ancient globe", "Zaros");
        ga24.addRequirement(new MaterialRequirement("White oak", 20 ));
        ga24.addRequirement(new MaterialRequirement("Ancient vis", 60 ));
        ga24.addRequirement(new MaterialRequirement("Tyrian purple", 54 ));

        GameArtefact ga25 = new GameArtefact("Ancient magic tablet", "Zaros");
        ga25.addRequirement(new MaterialRequirement("Ancient vis", 40 ));
        ga25.addRequirement(new MaterialRequirement("Blood of Orcus", 64 ));

        GameArtefact ga26 = new GameArtefact("Ancient timepiece", "Zaros");
        ga26.addRequirement(new MaterialRequirement("Goldrune", 12 ));
        ga26.addRequirement(new MaterialRequirement("Imperial steel", 16 ));
        ga26.addRequirement(new MaterialRequirement("Ancient vis", 18 ));

        GameArtefact ga27 = new GameArtefact("Avian song-egg player", "Armadyl");
        ga27.addRequirement(new MaterialRequirement("Stormguard steel", 36 ));
        ga27.addRequirement(new MaterialRequirement("Armadylean yellow", 32 ));

        GameArtefact ga28 = new GameArtefact("Aviansie dreamcoat", "Armadyl");
        ga28.addRequirement(new MaterialRequirement("Armadylean yellow", 20 ));
        ga28.addRequirement(new MaterialRequirement("Samite silk", 30 ));
        ga28.addRequirement(new MaterialRequirement("Animal furs", 22 ));

        GameArtefact ga29 = new GameArtefact("Battle plans", "Zaros");
        ga29.addRequirement(new MaterialRequirement("Vellum", 40 ));
        ga29.addRequirement(new MaterialRequirement("Ancient vis", 34 ));
        ga29.addRequirement(new MaterialRequirement("Tyrian purple", 60 ));

        GameArtefact ga30 = new GameArtefact("Beastkeeper helm", "Bandos");
        ga30.addRequirement(new MaterialRequirement("Warforged bronze", 16 ));
        ga30.addRequirement(new MaterialRequirement("Vulcanised rubber", 24 ));
        ga30.addRequirement(new MaterialRequirement("Animal furs", 20 ));
        ga30.addRequirement(new MaterialRequirement("Fossilised bone", 24 ));

        GameArtefact ga31 = new GameArtefact("Blackfire lance", "Armadyl");
        ga31.addRequirement(new MaterialRequirement("Aetherium alloy", 50 ));
        ga31.addRequirement(new MaterialRequirement("Quintessence", 46 ));

        GameArtefact ga32 = new GameArtefact("Branding iron", "Zamorak");
        ga32.addRequirement(new MaterialRequirement("Third Age iron", 14 ));
        ga32.addRequirement(new MaterialRequirement("Eye of Dagon", 12 ));
        ga32.addRequirement(new MaterialRequirement("Hellfire metal", 20 ));

        GameArtefact ga33 = new GameArtefact("Bronze Dominion medal", "Saradomin");
        ga33.addRequirement(new MaterialRequirement("Everlight silvthril", 36 ));
        ga33.addRequirement(new MaterialRequirement("Star of Saradomin", 26 ));

        GameArtefact ga34 = new GameArtefact("Ceremonial mace", "Zaros");
        ga34.addRequirement(new MaterialRequirement("Third Age iron", 20 ));
        ga34.addRequirement(new MaterialRequirement("Imperial steel", 20 ));
        ga34.addRequirement(new MaterialRequirement("Goldrune", 28 ));

        GameArtefact ga35 = new GameArtefact("Ceremonial plume", "Armadyl");
        ga35.addRequirement(new MaterialRequirement("Armadylean yellow", 38 ));
        ga35.addRequirement(new MaterialRequirement("Goldrune", 34 ));

        GameArtefact ga36 = new GameArtefact("Ceremonial unicorn ornament", "Saradomin");
        ga36.addRequirement(new MaterialRequirement("Keramos", 26 ));
        ga36.addRequirement(new MaterialRequirement("Cobalt blue", 20 ));

        GameArtefact ga37 = new GameArtefact("Ceremonial unicorn saddle", "Saradomin");
        ga37.addRequirement(new MaterialRequirement("Leather scraps", 24 ));
        ga37.addRequirement(new MaterialRequirement("Cobalt blue", 22 ));

        GameArtefact ga38 = new GameArtefact("Chaos elemental trophy", "Zamorak");
        ga38.addRequirement(new MaterialRequirement("Chaotic brimstone", 52 ));
        ga38.addRequirement(new MaterialRequirement("White oak", 30 ));
        ga38.addRequirement(new MaterialRequirement("Hellfire metal", 30 ));

        GameArtefact ga39 = new GameArtefact("Chaos star", "Zamorak");
        ga39.addRequirement(new MaterialRequirement("Chaotic brimstone", 28 ));
        ga39.addRequirement(new MaterialRequirement("Hellfire metal", 36 ));

        GameArtefact ga40 = new GameArtefact("Chuluu stone", "Armadyl");
        ga40.addRequirement(new MaterialRequirement("Aetherium alloy", 40 ));
        ga40.addRequirement(new MaterialRequirement("Quintessence", 30 ));
        ga40.addRequirement(new MaterialRequirement("Soapstone", 40 ));
        ga40.addRequirement(new MaterialRequirement("Goldrune", 24 ));

        GameArtefact ga41 = new GameArtefact("Crest of Dagon", "Zamorak");
        ga41.addRequirement(new MaterialRequirement("Orthenglass", 18 ));
        ga41.addRequirement(new MaterialRequirement("Goldrune", 14 ));

        GameArtefact ga42 = new GameArtefact("Dayguard shield", "Armadyl");
        ga42.addRequirement(new MaterialRequirement("Stormguard steel", 36 ));
        ga42.addRequirement(new MaterialRequirement("White oak", 20 ));
        ga42.addRequirement(new MaterialRequirement("Wings of War", 28 ));

        GameArtefact ga43 = new GameArtefact("Decorate vase", "Saradomin");
        ga43.addRequirement(new MaterialRequirement("White marble", 36 ));
        ga43.addRequirement(new MaterialRequirement("Cobalt blue", 30 ));

        GameArtefact ga44 = new GameArtefact("Dominarian device", "Saradomin");
        ga44.addRequirement(new MaterialRequirement("Everlight silvthril", 30 ));
        ga44.addRequirement(new MaterialRequirement("Keramos", 22 ));
        ga44.addRequirement(new MaterialRequirement("Third Age iron", 22 ));

        GameArtefact ga45 = new GameArtefact("Dominion discus", "Saradomin");
        ga45.addRequirement(new MaterialRequirement("Star of Saradomin", 28 ));
        ga45.addRequirement(new MaterialRequirement("Keramos", 34 ));

        GameArtefact ga46 = new GameArtefact("Dominion javelin", "Saradomin");
        ga46.addRequirement(new MaterialRequirement("Third Age iron", 30 ));
        ga46.addRequirement(new MaterialRequirement("Keramos", 32 ));

        GameArtefact ga47 = new GameArtefact("Dominion pelte shield", "Saradomin");
        ga47.addRequirement(new MaterialRequirement("Star of Saradomin", 34 ));
        ga47.addRequirement(new MaterialRequirement("Samite silk", 28 ));

        GameArtefact ga48 = new GameArtefact("Dominion torch", "Saradomin");
        ga48.addRequirement(new MaterialRequirement("Goldrune", 12 ));
        ga48.addRequirement(new MaterialRequirement("Orthenglass", 12 ));
        ga48.addRequirement(new MaterialRequirement("Everlight silvthril", 20 ));
        ga48.addRequirement(new MaterialRequirement("Star of Saradomin", 18 ));

        GameArtefact ga49 = new GameArtefact("Dorgeshuun spear", "Bandos");
        ga49.addRequirement(new MaterialRequirement("Warforged bronze", 50 ));
        ga49.addRequirement(new MaterialRequirement("White oak", 42 ));

        GameArtefact ga50 = new GameArtefact("Doru spear", "Saradomin");
        ga50.addRequirement(new MaterialRequirement("Everlight silvthril", 70 ));
        ga50.addRequirement(new MaterialRequirement("White oak", 62 ));

        GameArtefact ga51 = new GameArtefact("Drogokishuun hook swords", "Bandos");
        ga51.addRequirement(new MaterialRequirement("Warforged bronze", 44 ));
        ga51.addRequirement(new MaterialRequirement("Malachite green", 36 ));
        ga51.addRequirement(new MaterialRequirement("Fossilised bone", 32 ));

        GameArtefact ga52 = new GameArtefact("Ekeleshuun blinder mask", "Bandos");
        ga52.addRequirement(new MaterialRequirement("Vulcanised rubber", 24 ));
        ga52.addRequirement(new MaterialRequirement("Malachite green", 20 ));
        ga52.addRequirement(new MaterialRequirement("Vellum", 24 ));

        GameArtefact ga53 = new GameArtefact("Everlight harp", "Saradomin");
        ga53.addRequirement(new MaterialRequirement("Everlight silvthril", 30 ));
        ga53.addRequirement(new MaterialRequirement("White oak", 22 ));

        GameArtefact ga54 = new GameArtefact("Everlight trumpet", "Saradomin");
        ga54.addRequirement(new MaterialRequirement("Everlight silvthril", 28 ));
        ga54.addRequirement(new MaterialRequirement("Goldrune", 24 ));

        GameArtefact ga55 = new GameArtefact("Everlight violin", "Saradomin");
        ga55.addRequirement(new MaterialRequirement("Star of Saradomin", 16 ));
        ga55.addRequirement(new MaterialRequirement("White oak", 20 ));
        ga55.addRequirement(new MaterialRequirement("Samite silk", 16 ));

        GameArtefact ga56 = new GameArtefact("Fishing trident", "Saradomin");
        ga56.addRequirement(new MaterialRequirement("Star of Saradomin", 22 ));
        ga56.addRequirement(new MaterialRequirement("Third Age iron", 30 ));
        ga56.addRequirement(new MaterialRequirement("Goldrune", 22 ));

        GameArtefact ga57 = new GameArtefact("Flat cap", "Armadyl");
        ga57.addRequirement(new MaterialRequirement("Armadylean yellow", 60 ));
        ga57.addRequirement(new MaterialRequirement("Samite silk", 54 ));

        GameArtefact ga58 = new GameArtefact("Folded-arm figurine (female)", "Saradomin");
        ga58.addRequirement(new MaterialRequirement("White marble", 30 ));
        ga58.addRequirement(new MaterialRequirement("Goldrune", 24 ));

        GameArtefact ga59 = new GameArtefact("Folded-arm figurine (male)", "Saradomin");
        ga59.addRequirement(new MaterialRequirement("White marble", 30 ));
        ga59.addRequirement(new MaterialRequirement("Goldrune", 24 ));

        GameArtefact ga60 = new GameArtefact("Garagorshuun anchor", "Bandos");
        ga60.addRequirement(new MaterialRequirement("Warforged bronze", 32 ));
        ga60.addRequirement(new MaterialRequirement("Mark of the Kyzaj", 26 ));
        ga60.addRequirement(new MaterialRequirement("Third Age iron", 30 ));

        GameArtefact ga61 = new GameArtefact("Golem heart", "Armadyl");
        ga61.addRequirement(new MaterialRequirement("Aetherium alloy", 34 ));
        ga61.addRequirement(new MaterialRequirement("Quintessence", 24 ));
        ga61.addRequirement(new MaterialRequirement("Soapstone", 16 ));
        ga61.addRequirement(new MaterialRequirement("Orthenglass", 16 ));

        GameArtefact ga62 = new GameArtefact("Golem instruction", "Armadyl");
        ga62.addRequirement(new MaterialRequirement("Vellum", 44 ));
        ga62.addRequirement(new MaterialRequirement("Quintessence", 46 ));

        GameArtefact ga63 = new GameArtefact("Greater demon mask", "Zamorak");
        ga63.addRequirement(new MaterialRequirement("Third Age iron", 6 ));
        ga63.addRequirement(new MaterialRequirement("Leather scraps", 6 ));
        ga63.addRequirement(new MaterialRequirement("Chaotic brimstone", 8 ));
        ga63.addRequirement(new MaterialRequirement("Demonhide", 12 ));

        GameArtefact ga64 = new GameArtefact("Hallowed lantern", "Saradomin");
        ga64.addRequirement(new MaterialRequirement("Third Age iron", 20 ));
        ga64.addRequirement(new MaterialRequirement("Keramos", 24 ));

        GameArtefact ga65 = new GameArtefact("Hawkeye lens multi-vision scope", "Armadyl");
        ga65.addRequirement(new MaterialRequirement("Stormguard steel", 40 ));
        ga65.addRequirement(new MaterialRequirement("Orthenglass", 34 ));

        GameArtefact ga66 = new GameArtefact("Hellfire haladie", "Zamorak");
        ga66.addRequirement(new MaterialRequirement("Third Age iron", 26 ));
        ga66.addRequirement(new MaterialRequirement("Leather scraps", 20 ));
        ga66.addRequirement(new MaterialRequirement("Hellfire metal", 44 ));

        GameArtefact ga67 = new GameArtefact("Hellfire katar", "Zamorak");
        ga67.addRequirement(new MaterialRequirement("Leather scraps", 40 ));
        ga67.addRequirement(new MaterialRequirement("Hellfire metal", 50 ));

        GameArtefact ga68 = new GameArtefact("Hellfire zaghnal", "Zamorak");
        ga68.addRequirement(new MaterialRequirement("White oak", 26 ));
        ga68.addRequirement(new MaterialRequirement("Orthenglass", 26));
        ga68.addRequirement(new MaterialRequirement("Hellfire metal", 38 ));

        GameArtefact ga69 = new GameArtefact("High priest crozier", "Bandos");
        ga69.addRequirement(new MaterialRequirement("Mark of the Kyzaj", 26 ));
        ga69.addRequirement(new MaterialRequirement("Malachite green", 24));
        ga69.addRequirement(new MaterialRequirement("Goldrune", 28 ));

        GameArtefact ga70 = new GameArtefact("High priest mitre", "Bandos");
        ga70.addRequirement(new MaterialRequirement("Mark of the Kyzaj", 26 ));
        ga70.addRequirement(new MaterialRequirement("Malachite green", 24));
        ga70.addRequirement(new MaterialRequirement("Samite silk", 28 ));

        GameArtefact ga71 = new GameArtefact("High priest orb", "Bandos");
        ga71.addRequirement(new MaterialRequirement("Mark of the Kyzaj", 26 ));
        ga71.addRequirement(new MaterialRequirement("Malachite green", 24));
        ga71.addRequirement(new MaterialRequirement("Goldrune", 28 ));

        GameArtefact ga72 = new GameArtefact("Hobgoblin mansticker", "Bandos");
        ga72.addRequirement(new MaterialRequirement("Warforged bronze", 66));
        ga72.addRequirement(new MaterialRequirement("Fossilised bone", 46 ));

        GameArtefact ga73 = new GameArtefact("Hookah pipe", "Zamorak");
        ga73.addRequirement(new MaterialRequirement("Third Age iron", 10 ));
        ga73.addRequirement(new MaterialRequirement("Orthenglass", 8));
        ga73.addRequirement(new MaterialRequirement("Goldrune", 12 ));

        GameArtefact ga74 = new GameArtefact("Horogothgar cooking pot", "Bandos");
        ga74.addRequirement(new MaterialRequirement("Yu'biusk clay", 60 ));
        ga74.addRequirement(new MaterialRequirement("Malachite green", 38));
        ga74.addRequirement(new MaterialRequirement("Soapstone", 40 ));

        GameArtefact ga75 = new GameArtefact("Huzamogaarb chaos crown", "Bandos");
        ga75.addRequirement(new MaterialRequirement("Warforged bronze", 44));
        ga75.addRequirement(new MaterialRequirement("Third Age iron", 34));
        ga75.addRequirement(new MaterialRequirement("Eye of Dagon", 20));

        GameArtefact ga76 = new GameArtefact("Idithuun horn rings", "Bandos");
        ga76.addRequirement(new MaterialRequirement("Yu'biusk clay", 40 ));
        ga76.addRequirement(new MaterialRequirement("Vulcanised rubber", 44));

        GameArtefact ga77 = new GameArtefact("Ikovian gerege", "Armadyl");
        ga77.addRequirement(new MaterialRequirement("Third Age iron", 36 ));
        ga77.addRequirement(new MaterialRequirement("Wings of War", 30));

        GameArtefact ga78 = new GameArtefact("Imp mask", "Zamorak");
        ga78.addRequirement(new MaterialRequirement("Leather scraps", 10));
        ga78.addRequirement(new MaterialRequirement("Chaotic brimstone", 10));
        ga78.addRequirement(new MaterialRequirement("Demonhide", 12));

        GameArtefact ga79 = new GameArtefact("Kal-i-kran chieftain crown", "Bandos");
        ga79.addRequirement(new MaterialRequirement("Yu'biusk clay", 66 ));
        ga76.addRequirement(new MaterialRequirement("Animal furs", 60));

        GameArtefact ga80 = new GameArtefact("Kal-i-kran mace", "Bandos");
        ga80.addRequirement(new MaterialRequirement("Vulcanised rubber", 42));
        ga80.addRequirement(new MaterialRequirement("Third Age iron", 44));
        ga80.addRequirement(new MaterialRequirement("Fossilised bone", 40));

        GameArtefact ga81 = new GameArtefact("Kal-i-kran warhorn", "Bandos");
        ga81.addRequirement(new MaterialRequirement("Vulcanised rubber", 44));
        ga81.addRequirement(new MaterialRequirement("Animal furs", 40));
        ga81.addRequirement(new MaterialRequirement("Fossilised bone", 42));

        GameArtefact ga82 = new GameArtefact("Kantharos cup", "Saradomin");
        ga82.addRequirement(new MaterialRequirement("Everlight silvthril", 30 ));
        ga82.addRequirement(new MaterialRequirement("Orthenglass", 36));

        GameArtefact ga83 = new GameArtefact("Keshik drum", "Armadyl");
        ga83.addRequirement(new MaterialRequirement("Wings of War", 16));
        ga83.addRequirement(new MaterialRequirement("Animal furs", 16));
        ga83.addRequirement(new MaterialRequirement("White oak", 20));
        ga83.addRequirement(new MaterialRequirement("Leather scraps", 16));

        GameArtefact ga84 = new GameArtefact("Kontos lance", "Saradomin");
        ga84.addRequirement(new MaterialRequirement("Everlight silvthril", 70 ));
        ga84.addRequirement(new MaterialRequirement("Samite silk", 62));

        GameArtefact ga85 = new GameArtefact("Kopis dagger", "Saradomin");
        ga85.addRequirement(new MaterialRequirement("Everlight silvthril", 50 ));
        ga85.addRequirement(new MaterialRequirement("Leather scraps", 42));

        GameArtefact ga86 = new GameArtefact("Larupia trophy", "Zamorak");
        ga86.addRequirement(new MaterialRequirement("Cadmium red", 18));
        ga86.addRequirement(new MaterialRequirement("Animal furs", 28 ));
        ga86.addRequirement(new MaterialRequirement("Orthenglass", 26 ));

        GameArtefact ga87 = new GameArtefact("Legatus maximus figurine", "Zaros");
        ga87.addRequirement(new MaterialRequirement("Goldrune", 8 ));
        ga87.addRequirement(new MaterialRequirement("Zarosian insignia", 14  ));
        ga87.addRequirement(new MaterialRequirement("Ancient vis", 10  ));

        GameArtefact ga88 = new GameArtefact("Legatus pendant", "Zaros");
        ga88.addRequirement(new MaterialRequirement("Goldrune", 18  ));
        ga88.addRequirement(new MaterialRequirement("Third Age iron",   16 ));
        ga88.addRequirement(new MaterialRequirement("Ancient vis", 12   ));

        GameArtefact ga89 = new GameArtefact("Legionary gladius", "Zaros");
        ga89.addRequirement(new MaterialRequirement("Zarosian insignia",  6  ));
        ga89.addRequirement(new MaterialRequirement("Third Age iron",   10  ));
        ga89.addRequirement(new MaterialRequirement("Imperial steel", 12    ));

        GameArtefact ga90 = new GameArtefact("Legionary square shield", "Zaros");
        ga89.addRequirement(new MaterialRequirement("Zarosian insignia",  8  ));
        ga89.addRequirement(new MaterialRequirement("Third Age iron",   8  ));
        ga89.addRequirement(new MaterialRequirement("Imperial steel", 12    ));

        GameArtefact ga91 = new GameArtefact("Lesser demon mask", "Zamorak");
        ga90.addRequirement(new MaterialRequirement("Cadmium red", 6));
        ga90.addRequirement(new MaterialRequirement("Demonhide", 12));
        ga90.addRequirement(new MaterialRequirement("Chaotic brimstone", 8));
        ga90.addRequirement(new MaterialRequirement("Leather scraps", 6));

        GameArtefact ga92 = new GameArtefact("Lion trophy", "Zamorak");
        ga92.addRequirement(new MaterialRequirement("Cadmium red", 18));
        ga92.addRequirement(new MaterialRequirement("Animal furs", 28 ));
        ga92.addRequirement(new MaterialRequirement("White oak", 26 ));

        GameArtefact ga93 = new GameArtefact("Manacles", "Zamorak");
        
        GameArtefact ga94 = new GameArtefact("Morin khuur", "Armadyl");
        GameArtefact ga95 = new GameArtefact("Narogoshuun 'Hob-da-Gob' ball", "Bandos");
        GameArtefact ga96 = new GameArtefact("Necromantic focus", "Zaros");
        GameArtefact ga97 = new GameArtefact("Night owl flying goggles", "Armadyl");
        GameArtefact ga98 = new GameArtefact("Nightguard shield", "Armadyl");
        GameArtefact ga99 = new GameArtefact("Ogre kyzaj axe", "Bandos");
        GameArtefact ga100 = new GameArtefact("Opulent wine goblet", "Zamorak");
        GameArtefact ga101 = new GameArtefact("Orc cleaver sword", "Bandos");
        GameArtefact ga102 = new GameArtefact("Order of Dis robes", "Zamorak");
        GameArtefact ga103 = new GameArtefact("Ourg megahitter", "Bandos");
        GameArtefact ga104 = new GameArtefact("Ourg tower/goblin cower shield", "Bandos");
        GameArtefact ga105 = new GameArtefact("Patera bowl", "Saradomin");
        GameArtefact ga106 = new GameArtefact("Peacocking parasol", "Armadyl");
        GameArtefact ga107 = new GameArtefact("Pontifex censer", "Zaros");
        GameArtefact ga108 = new GameArtefact("Pontifex crozier", "Zaros");
        GameArtefact ga109 = new GameArtefact("Pontifex maximus figurine", "Zaros");
        GameArtefact ga110 = new GameArtefact("Pontifex mitre", "Zaros");
        GameArtefact ga111 = new GameArtefact("Pontifex signet ring", "Zaros");
        GameArtefact ga112 = new GameArtefact("Portable phylactery", "Zaros");
        GameArtefact ga113 = new GameArtefact("Praetorian hood", "Zaros");
        GameArtefact ga114 = new GameArtefact("Praetorian robes", "Zaros");
        GameArtefact ga115 = new GameArtefact("Praetorian staff", "Zaros");
        GameArtefact ga116 = new GameArtefact("Primis Elementis standard", "Zaros");
        GameArtefact ga117 = new GameArtefact("Prototype godbow", "Armadyl");
        GameArtefact ga118 = new GameArtefact("Prototype godstaff", "Armadyl");
        GameArtefact ga119 = new GameArtefact("Prototype godsword", "Armadyl");
        GameArtefact ga120 = new GameArtefact("Prototype gravimeter", "Armadyl");
        GameArtefact ga121 = new GameArtefact("Quintessence counter", "Armadyl");
        GameArtefact ga122 = new GameArtefact("Rekeshuun war tether", "Bandos");
        GameArtefact ga123 = new GameArtefact("Ritual dagger", "Zamorak");
        GameArtefact ga124 = new GameArtefact("Rod of Asclepius", "Saradomin");
        GameArtefact ga125 = new GameArtefact("Saragorgak star crowns", "Bandos");
        GameArtefact ga126 = new GameArtefact("She-wolf trophy", "Zamorak");
        GameArtefact ga127 = new GameArtefact("Silver Dominion medal", "Saradomin");
        GameArtefact ga128 = new GameArtefact("Songbird recorder", "Armadyl");
        GameArtefact ga129 = new GameArtefact("Spherical astrolabe", "Armadyl");
        GameArtefact ga130 = new GameArtefact("Spiked dog collar", "Zamorak");
        GameArtefact ga131 = new GameArtefact("Stormguard gerege", "Armadyl");
        GameArtefact ga132 = new GameArtefact("Talon-3 razor wing", "Armadyl");
        GameArtefact ga133 = new GameArtefact("Thorobshuun battle standard", "Bandos");
        GameArtefact ga134 = new GameArtefact("Toy glider", "Armadyl");
        GameArtefact ga135 = new GameArtefact("Toy war golem", "Armadyl");
        GameArtefact ga136 = new GameArtefact("Trishula", "Zamorak");
        GameArtefact ga137 = new GameArtefact("Tsutsaroth helm", "Zamorak");
        GameArtefact ga138 = new GameArtefact("Tsutsaroth pauldron", "Zamorak");
        GameArtefact ga139 = new GameArtefact("Tsutsaroth piercing", "Zamorak");
        GameArtefact ga140 = new GameArtefact("Tsutsaroth urumi", "Zamorak");
        GameArtefact ga141 = new GameArtefact("Venator dagger", "Zaros");
        GameArtefact ga142 = new GameArtefact("Venator light crossbow", "Zaros");
        GameArtefact ga143 = new GameArtefact("Vigorem vial", "Zaros");
        GameArtefact ga144 = new GameArtefact("Virius trophy", "Zamorak");
        GameArtefact ga145 = new GameArtefact("Xiphos short sword", "Saradomin");
        GameArtefact ga146 = new GameArtefact("Yurkolgokh stink grenade", "Bandos");
        GameArtefact ga147 = new GameArtefact("Zarosian effigy", "Zaros");
        GameArtefact ga148 = new GameArtefact("Zarosian ewer", "Zaros");
        GameArtefact ga149 = new GameArtefact("Zarosian stein", "Zaros");
        GameArtefact ga150 = new GameArtefact("Zarosian training dummy", "Zaros");
        //</editor-fold>
        ArrayList<GameArtefact> liveArtefactArray = new ArrayList<GameArtefact>(){{
            add(ga1); add(ga2); add(ga3); add(ga4); add(ga5); add(ga6); add(ga7); add(ga8); add(ga9); add(ga10);
            add(ga11); add(ga12); add(ga13); add(ga14); add(ga15); add(ga16); add(ga17); add(ga18); add(ga19); add(ga20);
            add(ga21); add(ga22); add(ga23); add(ga24); add(ga25); add(ga26); add(ga27); add(ga28); add(ga29); add(ga30);
            add(ga31); add(ga32); add(ga33); add(ga34); add(ga35); add(ga36); add(ga37); add(ga38); add(ga39); add(ga40);
            add(ga41); add(ga42); add(ga43); add(ga44); add(ga45); add(ga46); add(ga47); add(ga48); add(ga49); add(ga50);
            add(ga51); add(ga52); add(ga53); add(ga54); add(ga55); add(ga56); add(ga57); add(ga58); add(ga59); add(ga60);
            add(ga61); add(ga62); add(ga63); add(ga64); add(ga65); add(ga66); add(ga67); add(ga68); add(ga69); add(ga70);
            add(ga71); add(ga72); add(ga73); add(ga74); add(ga75); add(ga76); add(ga77); add(ga78); add(ga79); add(ga80);
            add(ga81); add(ga82); add(ga83); add(ga84); add(ga85); add(ga86); add(ga87); add(ga88); add(ga89); add(ga90);
            add(ga91); add(ga92); add(ga93); add(ga94); add(ga95); add(ga96); add(ga97); add(ga98); add(ga99); add(ga100);
            add(ga101); add(ga102); add(ga103); add(ga104); add(ga105); add(ga106); add(ga107); add(ga108); add(ga109); add(ga110);
            add(ga111); add(ga112); add(ga113); add(ga114); add(ga115); add(ga116); add(ga117); add(ga118); add(ga119); add(ga120);
            add(ga121); add(ga122); add(ga123); add(ga124); add(ga125); add(ga126); add(ga127); add(ga128); add(ga129); add(ga130);
            add(ga131); add(ga132); add(ga133); add(ga134); add(ga135); add(ga136); add(ga137); add(ga138); add(ga139); add(ga140);
            add(ga141); add(ga142); add(ga143); add(ga144); add(ga145); add(ga146); add(ga147); add(ga148); add(ga149); add(ga150);
        }};
        liveArtefactArray.forEach(this::AddArtefact);

        ArrayList<Collector> liveCollectorArray = new ArrayList<Collector>(){{
            add(new Collector("Art Critic Jacques", "Varrock Museum top floor"));
            add(new Collector("Chief Tess", "Oo'glog'"));
            add(new Collector("General Bentnoze", "Goblin Village"));
            add(new Collector("General Wartface", "Goblin Village"));
            add(new Collector("Isaura", "Black Knights base, Taverley Dungeon"));
            add(new Collector("Lowse", "Armadyls Tower Falador clan camp"));
            add(new Collector("Sir Atcha", "White knights castle courtyard"));
            add(new Collector("Soran, Emissary of Zaros", "South of GE"));
            add(new Collector("Velucia", "Archaeology Guild"));
            add(new Collector("Wise Old Man", "Draynor Village"));
        }};
        liveCollectorArray.forEach(this::AddCollector);

        //<editor-fold defaultstate="collapsed" desc="Collection objects">
        Collection c1 = new Collection("Imperial impressionism", "Art Critic Jacques", "Zaros", "Crontoes", 2086, "Replica zarosian art");
        Collection c2 = new Collection("Radiant renaissance", "Art Critic Jacques", "Saradomin", "Crontoes", 2730, "Replica saradominist art");
        Collection c3 = new Collection("Anarchic abstraction", "Art Critic Jacques", "Zamorak", "Crontoes", 1574, "Replica zamorakian art");

        Collection c4 = new Collection("Smoky fings", "Chief Tess", "All", "Robust glass", 40, "Og'glog wellspring");
        Collection c5 = new Collection("Showy fings", "Chief Tess", "All", "Robust glass", 40);
        Collection c6 = new Collection("Blingy fings", "Chief Tess", "All", "Robust glass", 20);
        Collection c7 = new Collection("Hitty fings", "Chief Tess", "All", "Robust glass", 40);

        Collection c8 = new Collection("Red rum relics 1", "General Bentnoze", "Bandos", "Tetra pieces", 1);
        Collection c9 = new Collection("Red rum relics 2", "General Bentnoze", "Bandos", "Tetra pieces", 1);
        Collection c10 = new Collection("Red rum relics 3", "General Bentnoze", "Bandos", "Tetra pieces", 1, "Helm of terror inside");

        Collection c11 = new Collection("Green gobbo goodies 1", "General Wartface", "Bandos", "Tetra pieces", 1);
        Collection c12 = new Collection("Green gobbo goodies 2", "General Wartface", "Bandos", "Tetra pieces", 1);
        Collection c13 = new Collection("Green gobbo goodies 3", "General Wartface", "Bandos", "Tetra pieces", 1, "Helm of terror outside");

        Collection c14 = new Collection("Zamorakian I", "Isaura", "Zamorak", "Crontoes", 2594, "Abyssal Thread");
        Collection c15 = new Collection("Zamorakian II", "Isaura", "Zamorak", "Tetra pieces", 1, "Tetra piece");
        Collection c16 = new Collection("Zamorakian III", "Isaura", "Zamorak", "Tetra pieces", 1, "Tetra piece");
        Collection c17 = new Collection("Zamorakian IV", "Isaura", "Zamorak", "Tetra pieces", 1, "Ariadne's Diadem");

        Collection c18 = new Collection("Armadylean I", "Lowse", "Armadyl", "Stormguard blueprint fragments", 50, "King Oberon's moonshroom spores");
        Collection c19 = new Collection("Armadylean II", "Lowse", "Armadyl", "Stormguard blueprint fragments", 75, "75 Stormguard blueprint fragments");
        Collection c20 = new Collection("Armadylean III", "Lowse", "Armadyl", "Stormguard blueprint fragments", 150, "Howl's Thinking Cap");

        Collection c21 = new Collection("Saradominist I", "Sir Atcha", "Saradomin", "Crontoes", 3998, "Lock of hair");
        Collection c22 = new Collection("Saradominist II", "Sir Atcha", "Saradomin", "Tetra pieces", 1, "Tetracompass piece");
        Collection c23 = new Collection("Saradominist III", "Sir Atcha", "Saradomin", "Tetra pieces", 1, "Tetracompass piece");
        Collection c24 = new Collection("Saradominist IV", "Sir Atcha", "Saradomin", "Tetra pieces", 1, "Petasos");

        Collection c25 = new Collection("Zarosian I", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 20, "Seal of the Praefectus Praetorio");
        Collection c26 = new Collection("Zarosian II", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 50, "50 Kharid-et pylon batteries");
        Collection c27 = new Collection("Zarosian III", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 100, "100 Kharid-et pylon batteries");
        Collection c28 = new Collection("Zarosian IV", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 100, "Inquisitor staff piece");

        Collection c29 = new Collection("Wise Am the Music Man", "Wise Old Man", "All", "Crontoes", 4024, "Koschei's needle");
        Collection c30 = new Collection("Hat Hoarder", "Wise Old Man", "All", "Crontoes", 5995);
        Collection c31 = new Collection("Hat Problem", "Wise Old Man", "All", "Crontoes", 6775);
        Collection c32 = new Collection("Magic Man", "Wise Old Man", "All", "Crontoes", 8600);
        Collection c33 = new Collection("Knowledge is Power", "Wise Old Man", "All", "Crontoes", 5584, "Amascut's Enchanted Gem");

        Collection c34 = new Collection("Museum - Armadylean I", "Armadyl", "category", "Cronotes", 7110);
        Collection c35 = new Collection("Museum - Armadylean II", "Armadyl", "category", "Cronotes", 7585);
        Collection c36 = new Collection("Museum - Armadylean III", "Armadyl", "category", "Cronotes", 13085);
        Collection c37 = new Collection("Museum - Bandosian I", "Velucia", "Bandos", "Cronotes", 8445);
        Collection c38 = new Collection("Museum - Bandosian II", "Velucia", "Bandos", "Cronotes", 8257);
        Collection c39 = new Collection("Museum - Bandosian III", "Velucia", "Bandos", "Cronotes", 11725);
        Collection c40 = new Collection("Museum - Saradominist I", "Velucia", "Saradomin", "Cronotes", 4997);
        Collection c41 = new Collection("Museum - Saradominist II", "Velucia", "Saradomin", "Cronotes", 6052);
        Collection c42 = new Collection("Museum - Saradominist III", "Velucia", "Saradomin", "Cronotes", 5685);
        Collection c43 = new Collection("Museum - Saradominist IV", "Velucia", "Saradomin", "Cronotes", 8778);
        Collection c44 = new Collection("Museum - Zamorakian I", "Velucia", "Zamorak", "Cronotes", 3242);
        Collection c45 = new Collection("Museum - Zamorakian II", "Velucia", "Zamorak", "Cronotes", 6432);
        Collection c46 = new Collection("Museum - Zamorakian III", "Velucia", "Zamorak", "Cronotes", 7417);
        Collection c47 = new Collection("Museum - Zamorakian IV", "Velucia", "Zamorak", "Cronotes", 7635);
        Collection c48 = new Collection("Museum - Zarosian I", "Velucia", "Zaros", "Cronotes", 2635);
        Collection c49 = new Collection("Museum - Zarosian II", "Velucia", "Zaros", "Cronotes", 7165);
        Collection c50 = new Collection("Museum - Zarosian III", "Velucia", "Zaros", "Cronotes", 9302);
        Collection c51 = new Collection("Museum - Zarosian IV", "Velucia", "Zaros", "Cronotes", 8512);

        //</editor-fold>
        ArrayList<Collection> liveCollectionArray = new ArrayList<Collection>(){{
            add(c1); add(c2); add(c3); add(c4); add(c5); add(c6); add(c7); add(c8); add(c9); add(c10);
            add(c11); add(c12); add(c13); add(c14); add(c15); add(c16); add(c17); add(c18); add(c19); add(c20);
            add(c21); add(c22); add(c23); add(c24); add(c25); add(c26); add(c27); add(c28); add(c29); add(c30);
            add(c31); add(c32); add(c33); add(c34); add(c35); add(c36); add(c37); add(c38); add(c39); add(c40);
            add(c41); add(c42); add(c43); add(c44); add(c45); add(c46); add(c47); add(c48); add(c49); add(c50);
            add(c51);
        }};
        liveCollectionArray.forEach(this::AddCollection);
    }

    //</editor-fold>

    public GameArtefact findGameArtefactByTitle(String title){
        Optional<GameArtefact> artefact = artefacts.stream().filter(a -> a.title.equals(title)).findFirst();
        return artefact.orElse(null);
    }

}
