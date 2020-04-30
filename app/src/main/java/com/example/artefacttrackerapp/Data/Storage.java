package com.example.artefacttrackerapp.data;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.example.artefacttrackerapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

import static com.example.artefacttrackerapp.utilities.UtilityMethods.USING_LIVE_DATA;

public class Storage implements Serializable {

    private transient Context context;
    private transient Resources cRes;

    public Storage(Context context){
        this.context = context;
        if (this.context != null)
            this.cRes = this.context.getResources();

        this.artefacts = new ArrayList<>();
        this.collectors = new ArrayList<>();
        this.collections = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.levelInfos = new ArrayList<>();

        if (!USING_LIVE_DATA) {
            Toast.makeText(context, "Creating test data", Toast.LENGTH_SHORT).show();
            createTestData();
        } else {
            Toast.makeText(context, "Initialising storage", Toast.LENGTH_SHORT).show();
            createLiveData();
        }
    }

//    public void SET_ARTEFACTS_USE_WITH_CAUTION(ArrayList<GameArtefact> artefacts){
//        this.artefacts = artefacts;
//    }
//
//    public void SET_COLLECTIONS_USE_WITH_CAUTION(ArrayList<Collection> collections){
//        this.collections = collections;
//    }


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
    public boolean DeleteArtefact(GameArtefact artefact){
        return this.artefacts.remove(artefact);
    }
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
    public boolean DeleteCollector(Collector collector){
        return this.collectors.remove(collector);
    }
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
    public boolean DeleteCollection(Collection collection){
        return this.collections.remove(collection);
    }
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
    public boolean DeleteMaterial(Material material){
        return this.materials.remove(material);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Materials">
    private final ArrayList<LevelInfo> levelInfos;
    public ArrayList<LevelInfo> LevelInfos(){ return this.levelInfos; }
    public boolean AddLevelInfo(LevelInfo levelInfo){
        if (this.levelInfos.add(levelInfo)){
            Collections.sort(this.levelInfos, Comparator.comparing(LevelInfo::Level));
            return true;
        }
        return false;
    }
    public boolean DeleteLevelInfo(LevelInfo levelInfo){
        return this.levelInfos.remove(levelInfo);
    }
    //</editor-fold>

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
                collection.completeSubmission();
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
        GameArtefact ga1 = new GameArtefact("'Animate Dead' spell scroll", "Zaros");
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

        GameArtefact ga9 = new GameArtefact("'Incite Fear' spell scroll", "Zaros");
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

        GameArtefact ga14 = new GameArtefact("'Prima Legio' painting", "Zaros");
        ga14.addRequirement(new MaterialRequirement("White oak", 20  ));
        ga14.addRequirement(new MaterialRequirement("Samite silk", 20  ));
        ga14.addRequirement(new MaterialRequirement("Tyrian purple", 74 ));
        ga14.addRequirement(new MaterialRequirement("Zarosian insignia", 20 ));

        GameArtefact ga15 = new GameArtefact("'Smoke Cloud' spell scroll", "Zaros");
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

        GameArtefact ga38 = new GameArtefact("Chaos Elemental trophy", "Zamorak");
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

        GameArtefact ga43 = new GameArtefact("Decorative vase", "Saradomin");
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

        GameArtefact ga51 = new GameArtefact("Drogokishuun hook sword", "Bandos");
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

        GameArtefact ga76 = new GameArtefact("Idithuun horn ring", "Bandos");
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

        GameArtefact ga87 = new GameArtefact("Legatus Maximus figurine", "Zaros");
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
        ga93.addRequirement(new MaterialRequirement("Third Age iron",   14  ));
        ga93.addRequirement(new MaterialRequirement("Chaotic brimstone", 18));
        ga93.addRequirement(new MaterialRequirement("Eye of Dagon",   14  ));

        GameArtefact ga94 = new GameArtefact("Morin khuur", "Armadyl");
        ga94.addRequirement(new MaterialRequirement("Armadylean yellow", 36 ));
        ga94.addRequirement(new MaterialRequirement("White oak",   32   ));

        GameArtefact ga95 = new GameArtefact("Narogoshuun 'Hob-da-Gob' ball", "Bandos");
        ga95.addRequirement(new MaterialRequirement("Vulcanised rubber", 36 ));
        ga95.addRequirement(new MaterialRequirement("Mark of the Kyzaj",   32   ));

        GameArtefact ga96 = new GameArtefact("Necromantic focus", "Zaros");
        ga96.addRequirement(new MaterialRequirement("Imperial steel", 20 ));
        ga96.addRequirement(new MaterialRequirement("Blood of Orcus", 26  ));
        ga96.addRequirement(new MaterialRequirement("Ancient vis", 30  ));

        GameArtefact ga97 = new GameArtefact("Night owl flight goggles", "Armadyl");
        ga97.addRequirement(new MaterialRequirement("Armadylean yellow", 44 ));
        ga97.addRequirement(new MaterialRequirement("Leather scraps", 40  ));
        ga97.addRequirement(new MaterialRequirement("Orthenglass", 30  ));

        GameArtefact ga98 = new GameArtefact("Nightguard shield", "Armadyl");
        ga98.addRequirement(new MaterialRequirement("Stormguard steel", 30 ));
        ga98.addRequirement(new MaterialRequirement("Wings of War", 36  ));
        ga98.addRequirement(new MaterialRequirement("White oak", 30  ));

        GameArtefact ga99 = new GameArtefact("Ogre Kyzaj axe", "Bandos");
        ga99.addRequirement(new MaterialRequirement("Warforged bronze", 28 ));
        ga99.addRequirement(new MaterialRequirement("Mark of the Kyzaj", 20  ));
        ga99.addRequirement(new MaterialRequirement("Fossilised bone", 24  ));

        GameArtefact ga100 = new GameArtefact("Opulent wine goblet", "Zamorak");
        ga100.addRequirement(new MaterialRequirement("Third Age iron", 14  ));
        ga100.addRequirement(new MaterialRequirement("Goldrune", 16  ));

        GameArtefact ga101 = new GameArtefact("Ork cleaver sword", "Bandos");
        ga101.addRequirement(new MaterialRequirement("Warforged bronze", 36 ));
        ga101.addRequirement(new MaterialRequirement("Fossilised bone", 36  ));

        GameArtefact ga102 = new GameArtefact("Order of Dis robes", "Zamorak");
        ga102.addRequirement(new MaterialRequirement("Samite silk", 16 ));
        ga102.addRequirement(new MaterialRequirement("Cadmium red", 10  ));
        ga102.addRequirement(new MaterialRequirement("Eye of Dagon", 14  ));

        GameArtefact ga103 = new GameArtefact("Ourg megahitter", "Bandos");
        ga103.addRequirement(new MaterialRequirement("White oak", 20  ));
        ga103.addRequirement(new MaterialRequirement("Leather scraps", 20  ));
        ga103.addRequirement(new MaterialRequirement("Orthenglass", 26  ));
        ga103.addRequirement(new MaterialRequirement("Malachite green", 22  ));

        GameArtefact ga104 = new GameArtefact("Ourg tower/goblin cower shield", "Bandos");
        ga104.addRequirement(new MaterialRequirement("Mark of the Kyzaj", 20  ));
        ga104.addRequirement(new MaterialRequirement("Third Age iron", 26  ));
        ga104.addRequirement(new MaterialRequirement("Leather scraps", 22  ));
        ga104.addRequirement(new MaterialRequirement("White oak", 20  ));

        GameArtefact ga105 = new GameArtefact("Patera bowl", "Saradomin");
        ga105.addRequirement(new MaterialRequirement("Keramos", 36 ));
        ga105.addRequirement(new MaterialRequirement("Goldrune", 30  ));

        GameArtefact ga106 = new GameArtefact("Peacocking parasol", "Armadyl");
        ga106.addRequirement(new MaterialRequirement("Armadylean yellow", 22 ));
        ga106.addRequirement(new MaterialRequirement("Samite silk", 30  ));
        ga106.addRequirement(new MaterialRequirement("White oak", 20  ));

        GameArtefact ga107 = new GameArtefact("Pontifex censer", "Zaros");
        ga107.addRequirement(new MaterialRequirement("Third Age iron", 20 ));
        ga107.addRequirement(new MaterialRequirement("Ancient vis", 20  ));
        ga107.addRequirement(new MaterialRequirement("Goldrune", 32  ));

        GameArtefact ga108 = new GameArtefact("Pontifex crozier", "Zaros");
        ga108.addRequirement(new MaterialRequirement("Imperial steel", 20 ));
        ga108.addRequirement(new MaterialRequirement("Zarosian insignia", 20  ));
        ga108.addRequirement(new MaterialRequirement("Goldrune", 32  ));

        GameArtefact ga109 = new GameArtefact("Pontifex Maximus figurine", "Zaros");
        ga109.addRequirement(new MaterialRequirement("Ancient vis", 24 ));
        ga109.addRequirement(new MaterialRequirement("Zarosian insignia", 16  ));
        ga109.addRequirement(new MaterialRequirement("Goldrune", 28  ));

        GameArtefact ga110 = new GameArtefact("Pontifex mitre", "Zaros");
        ga110.addRequirement(new MaterialRequirement("Ancient vis", 20 ));
        ga110.addRequirement(new MaterialRequirement("Zarosian insignia", 20  ));
        ga110.addRequirement(new MaterialRequirement("Samite silk", 32  ));

        GameArtefact ga111 = new GameArtefact("Pontifex signet ring", "Zaros");
        ga111.addRequirement(new MaterialRequirement("Ancient vis", 22 ));
        ga111.addRequirement(new MaterialRequirement("Goldrune", 18  ));
        ga111.addRequirement(new MaterialRequirement("Third Age iron", 16  ));

        GameArtefact ga112 = new GameArtefact("Portable phylactery", "Zaros");
        ga112.addRequirement(new MaterialRequirement("Imperial steel", 48 ));
        ga112.addRequirement(new MaterialRequirement("Blood of Orcus", 36  ));
        ga112.addRequirement(new MaterialRequirement("Ancient vis", 20  ));

        GameArtefact ga113 = new GameArtefact("Praetorian hood", "Zaros");
        ga113.addRequirement(new MaterialRequirement("Zarosian insignia", 40 ));
        ga113.addRequirement(new MaterialRequirement("Samite silk", 48  ));
        ga113.addRequirement(new MaterialRequirement("Ancient vis", 36  ));

        GameArtefact ga114 = new GameArtefact("Praetorian robes", "Zaros");
        ga114.addRequirement(new MaterialRequirement("Zarosian insignia", 40 ));
        ga114.addRequirement(new MaterialRequirement("Samite silk", 54  ));
        ga114.addRequirement(new MaterialRequirement("Ancient vis", 30  ));

        GameArtefact ga115 = new GameArtefact("Praetorian staff", "Zaros");
        ga115.addRequirement(new MaterialRequirement("Zarosian insignia", 30 ));
        ga115.addRequirement(new MaterialRequirement("Imperial steel", 36  ));
        ga115.addRequirement(new MaterialRequirement("Ancient vis", 58  ));

        GameArtefact ga116 = new GameArtefact("Primis Elementis standard", "Zaros");
        ga116.addRequirement(new MaterialRequirement("Samite silk", 16  ));
        ga116.addRequirement(new MaterialRequirement("Third Age iron", 12  ));

        GameArtefact ga117 = new GameArtefact("Prototype godbow", "Armadyl");
        ga117.addRequirement(new MaterialRequirement("Aetherium alloy", 50 ));
        ga117.addRequirement(new MaterialRequirement("Quintessence", 34  ));
        ga117.addRequirement(new MaterialRequirement("Wings of War", 34  ));

        GameArtefact ga118 = new GameArtefact("Prototype godstaff", "Armadyl");
        ga118.addRequirement(new MaterialRequirement("Aetherium alloy", 50 ));
        ga118.addRequirement(new MaterialRequirement("Quintessence", 34  ));
        ga118.addRequirement(new MaterialRequirement("Wings of War", 34  ));

        GameArtefact ga119 = new GameArtefact("Prototype godsword", "Armadyl");
        ga119.addRequirement(new MaterialRequirement("Aetherium alloy", 50 ));
        ga119.addRequirement(new MaterialRequirement("Goldrune", 34  ));
        ga119.addRequirement(new MaterialRequirement("Wings of War", 34  ));

        GameArtefact ga120 = new GameArtefact("Prototype gravimeter", "Armadyl");
        ga120.addRequirement(new MaterialRequirement("Quintessence", 34 ));
        ga120.addRequirement(new MaterialRequirement("Leather scraps", 20  ));
        ga120.addRequirement(new MaterialRequirement("Third Age iron", 26  ));

        GameArtefact ga121 = new GameArtefact("Quintessence counter", "Armadyl");
        ga121.addRequirement(new MaterialRequirement("Quintessence", 54 ));
        ga121.addRequirement(new MaterialRequirement("Stormguard steel", 40  ));
        ga121.addRequirement(new MaterialRequirement("White oak", 40  ));

        GameArtefact ga122 = new GameArtefact("Rekeshuun war tether", "Bandos");
        ga122.addRequirement(new MaterialRequirement("Warforged bronze", 20 ));
        ga122.addRequirement(new MaterialRequirement("Vulcanised rubber", 22  ));
        ga122.addRequirement(new MaterialRequirement("Leather scraps", 26  ));

        GameArtefact ga123 = new GameArtefact("Ritual dagger", "Zamorak");
        ga123.addRequirement(new MaterialRequirement("Goldrune", 16  ));
        ga123.addRequirement(new MaterialRequirement("Hellfire metal", 24  ));

        GameArtefact ga124 = new GameArtefact("Rod of Asclepius", "Saradomin");
        ga124.addRequirement(new MaterialRequirement("White marble", 30 ));
        ga124.addRequirement(new MaterialRequirement("Star of Saradomin", 24  ));
        ga124.addRequirement(new MaterialRequirement("Goldrune", 26  ));

        GameArtefact ga125 = new GameArtefact("Saragorgak star crown", "Bandos");
        ga125.addRequirement(new MaterialRequirement("Warforged bronze", 44 ));
        ga125.addRequirement(new MaterialRequirement("Third Age iron", 34  ));
        ga125.addRequirement(new MaterialRequirement("Star of Saradomin", 20  ));

        GameArtefact ga126 = new GameArtefact("She-wolf trophy", "Zamorak");
        ga126.addRequirement(new MaterialRequirement("Chaotic brimstone", 26 ));
        ga126.addRequirement(new MaterialRequirement("Cadmium red", 18  ));
        ga126.addRequirement(new MaterialRequirement("Animal furs", 28  ));

        GameArtefact ga127 = new GameArtefact("Silver Dominion medal", "Saradomin");
        ga127.addRequirement(new MaterialRequirement("Everlight silvthril", 36  ));
        ga127.addRequirement(new MaterialRequirement("Star of Saradomin", 26  ));

        GameArtefact ga128 = new GameArtefact("Songbird recorder", "Armadyl");
        ga128.addRequirement(new MaterialRequirement("Stormguard steel", 44  ));
        ga128.addRequirement(new MaterialRequirement("Orthenglass", 36  ));

        GameArtefact ga129 = new GameArtefact("Spherical astrolabe", "Armadyl");
        ga129.addRequirement(new MaterialRequirement("Aetherium alloy", 46 ));
        ga129.addRequirement(new MaterialRequirement("Armadylean yellow", 40  ));
        ga129.addRequirement(new MaterialRequirement("Orthenglass", 48  ));

        GameArtefact ga130 = new GameArtefact("Spiked dog collar", "Zamorak");
        ga130.addRequirement(new MaterialRequirement("Third Age iron", 24 ));
        ga130.addRequirement(new MaterialRequirement("Leather scraps", 24  ));
        ga130.addRequirement(new MaterialRequirement("Chaotic brimstone", 16  ));

        GameArtefact ga131 = new GameArtefact("Stormguard gerege", "Armadyl");
        ga131.addRequirement(new MaterialRequirement("Stormguard steel", 36 ));
        ga131.addRequirement(new MaterialRequirement("Wings of War", 28  ));
        ga131.addRequirement(new MaterialRequirement("Goldrune", 20  ));

        GameArtefact ga132 = new GameArtefact("Talon-3 razor wing", "Armadyl");
        ga132.addRequirement(new MaterialRequirement("Aetherium alloy", 40  ));
        ga132.addRequirement(new MaterialRequirement("Wings of War", 34  ));

        GameArtefact ga133 = new GameArtefact("Thorobshuun battle standard", "Bandos");
        ga133.addRequirement(new MaterialRequirement("Mark of the Kyzaj", 16  ));
        ga133.addRequirement(new MaterialRequirement("Malachite green", 22  ));
        ga133.addRequirement(new MaterialRequirement("White oak", 16  ));
        ga133.addRequirement(new MaterialRequirement("Samite silk", 20  ));

        GameArtefact ga134 = new GameArtefact("Toy glider", "Armadyl");
        ga134.addRequirement(new MaterialRequirement("Stormguard steel", 36  ));
        ga134.addRequirement(new MaterialRequirement("White oak", 30  ));

        GameArtefact ga135 = new GameArtefact("Toy war golem", "Armadyl");
        ga135.addRequirement(new MaterialRequirement("Third Age iron", 36  ));
        ga135.addRequirement(new MaterialRequirement("White oak", 30  ));

        GameArtefact ga136 = new GameArtefact("Trishula", "Zamorak");
        ga136.addRequirement(new MaterialRequirement("Hellfire metal", 48 ));
        ga136.addRequirement(new MaterialRequirement("Eye of Dagon", 30  ));
        ga136.addRequirement(new MaterialRequirement("Third Age iron", 20  ));

        GameArtefact ga137 = new GameArtefact("Tsutsaroth helm", "Zamorak");
        ga137.addRequirement(new MaterialRequirement("Hellfire metal", 50 ));
        ga137.addRequirement(new MaterialRequirement("Eye of Dagon", 40  ));
        ga137.addRequirement(new MaterialRequirement("Goldrune", 40  ));

        GameArtefact ga138 = new GameArtefact("Tsutsaroth pauldron", "Zamorak");
        ga138.addRequirement(new MaterialRequirement("Hellfire metal", 40 ));
        ga138.addRequirement(new MaterialRequirement("Eye of Dagon", 40  ));
        ga138.addRequirement(new MaterialRequirement("Goldrune", 50  ));

        GameArtefact ga139 = new GameArtefact("Tsutsaroth piercing", "Zamorak");
        ga139.addRequirement(new MaterialRequirement("Hellfire metal", 44 ));
        ga139.addRequirement(new MaterialRequirement("Chaotic brimstone", 30  ));
        ga139.addRequirement(new MaterialRequirement("Cadmium red", 24  ));

        GameArtefact ga140 = new GameArtefact("Tsutsaroth urumi", "Zamorak");
        ga140.addRequirement(new MaterialRequirement("Hellfire metal", 50 ));
        ga140.addRequirement(new MaterialRequirement("Eye of Dagon", 40  ));
        ga140.addRequirement(new MaterialRequirement("Third Age iron", 40  ));

        GameArtefact ga141 = new GameArtefact("Venator dagger", "Zaros");
        ga141.addRequirement(new MaterialRequirement("Zarosian insignia", 12  ));
        ga141.addRequirement(new MaterialRequirement("Third Age iron", 16  ));

        GameArtefact ga142 = new GameArtefact("Venator light crossbow", "Zaros");
        ga142.addRequirement(new MaterialRequirement("Zarosian insignia", 16  ));
        ga142.addRequirement(new MaterialRequirement("Third Age iron", 12  ));

        GameArtefact ga143 = new GameArtefact("Vigorem vial", "Zaros");
        ga143.addRequirement(new MaterialRequirement("Imperial steel", 54  ));
        ga143.addRequirement(new MaterialRequirement("Ancient vis", 38  ));

        GameArtefact ga144 = new GameArtefact("Virius trophy", "Zamorak");
        ga144.addRequirement(new MaterialRequirement("Demonhide", 44 ));
        ga144.addRequirement(new MaterialRequirement("White oak", 34  ));
        ga144.addRequirement(new MaterialRequirement("Orthenglass", 34  ));

        GameArtefact ga145 = new GameArtefact("Xiphos short sword", "Saradomin");
        ga145.addRequirement(new MaterialRequirement("Everlight silvthril", 46  ));
        ga145.addRequirement(new MaterialRequirement("Leather scraps", 46  ));

        GameArtefact ga146 = new GameArtefact("Yurkolgokh stink grenade", "Bandos");
        ga146.addRequirement(new MaterialRequirement("Yu'biusk clay", 38  ));
        ga146.addRequirement(new MaterialRequirement("Vulcanised rubber", 36  ));

        GameArtefact ga147 = new GameArtefact("Zaros effigy", "Zaros");
        ga147.addRequirement(new MaterialRequirement("Samite silk", 8 ));
        ga147.addRequirement(new MaterialRequirement("White oak", 10  ));
        ga147.addRequirement(new MaterialRequirement("Zarosian insignia", 12  ));

        GameArtefact ga148 = new GameArtefact("Zarosian ewer", "Zaros");
        ga148.addRequirement(new MaterialRequirement("Zarosian insignia", 30  ));
        ga148.addRequirement(new MaterialRequirement("Third Age iron", 52  ));

        GameArtefact ga149 = new GameArtefact("Zarosian stein", "Zaros");
        ga149.addRequirement(new MaterialRequirement("Zarosian insignia", 30  ));
        ga149.addRequirement(new MaterialRequirement("Third Age iron", 16  ));
        ga149.addRequirement(new MaterialRequirement("Imperial steel", 36  ));

        GameArtefact ga150 = new GameArtefact("Zarosian training dummy", "Zaros");
        ga150.addRequirement(new MaterialRequirement("White oak", 14  ));
        ga150.addRequirement(new MaterialRequirement("Third Age iron", 16  ));
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

        //<editor-fold defaultstate="collapsed" desc="Art Critic Jacques ">
        Collection c1 = new Collection("Imperial impressionism", "Art Critic Jacques", "Zaros", "Crontoes", 2086, "Replica zarosian art");
        c1.addArtefact("'Solem in Umbra' painting");
        c1.addArtefact("'Consensus ad Idem' painting");
        c1.addArtefact("'Prima Legio' painting");
        Collection c2 = new Collection("Radiant renaissance", "Art Critic Jacques", "Saradomin", "Crontoes", 2730, "Replica saradominist art");
        c2.addArtefact("'Hallowed Be the Everlight' painting");
        c2.addArtefact("'The Lord of Light' painting");
        c2.addArtefact("'The Pride of Padosan' painting");
        Collection c3 = new Collection("Anarchic abstraction", "Art Critic Jacques", "Zamorak", "Crontoes", 1574, "Replica zamorakian art");
        c3.addArtefact("'Disorder' painting");
        c3.addArtefact("'The Lake of Fire' painting");
        c3.addArtefact("'Pandemonium' tapestry");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Chief Tess ">
        Collection c4 = new Collection("Smoky fings", "Chief Tess", "All", "Robust glass", 40, "Og'glog wellspring");
        c4.addArtefact("Hookah pipe");
        c4.addArtefact("Opulent wine goblet");
        c4.addArtefact("Everlight trumpet");
        c4.addArtefact("Dominion torch");
        c4.addArtefact("Pontifex censer");
        Collection c5 = new Collection("Showy fings", "Chief Tess", "All", "Robust glass", 40);
        c5.addArtefact("Crest of Dagon");
        c5.addArtefact("Legatus Maximus figurine");
        c5.addArtefact("'Lust' metal sculpture");
        c5.addArtefact("Pontifex Maximus figurine");
        c5.addArtefact("Ceremonial plume");
        c5.addArtefact("Rod of Asclepius");
        Collection c6 = new Collection("Blingy fings", "Chief Tess", "All", "Robust glass", 20);
        c6.addArtefact("Ancient timepiece");
        c6.addArtefact("Legatus pendant");
        c6.addArtefact("Pontifex signet ring");
        c6.addArtefact("Bronze Dominion medal");
        c6.addArtefact("Silver Dominion medal");
        Collection c7 = new Collection("Hitty fings", "Chief Tess", "All", "Robust glass", 40);
        c7.addArtefact("Ceremonial mace");
        c7.addArtefact("Pontifex crozier");
        c7.addArtefact("Fishing trident");
        c7.addArtefact("High priest crozier");
        c7.addArtefact("High priest orb");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="General Bentnoze ">
        Collection c8 = new Collection("Red rum relics 1", "General Bentnoze", "Bandos", "Tetra pieces", 1);
        c8.addArtefact("Ork cleaver sword");
        c8.addArtefact("Ogre Kyzaj axe");
        c8.addArtefact("Beastkeeper helm");
        c8.addArtefact("'Nosorog!' sculpture");
        Collection c9 = new Collection("Red rum relics 2", "General Bentnoze", "Bandos", "Tetra pieces", 1);
        c9.addArtefact("Ourg megahitter");
        c9.addArtefact("Ourg tower/goblin cower shield");
        c9.addArtefact("'Forged in War' sculpture");
        c9.addArtefact("Hobgoblin mansticker");
        Collection c10 = new Collection("Red rum relics 3", "General Bentnoze", "Bandos", "Tetra pieces", 1, "Helm of terror inside");
        c10.addArtefact("Kal-i-kran chieftain crown");
        c10.addArtefact("Kal-i-kran mace");
        c10.addArtefact("Kal-i-kran warhorn");
        c10.addArtefact("'Da Boss Man' sculpture");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="General Wartface ">
        Collection c11 = new Collection("Green gobbo goodies 1", "General Wartface", "Bandos", "Tetra pieces", 1);
        c11.addArtefact("Ekeleshuun blinder mask");
        c11.addArtefact("Narogoshuun 'Hob-da-Gob' ball");
        c11.addArtefact("Rekeshuun war tether");
        c11.addArtefact("Thorobshuun battle standard");
        c11.addArtefact("Yurkolgokh stink grenade");
        Collection c12 = new Collection("Green gobbo goodies 2", "General Wartface", "Bandos", "Tetra pieces", 1);
        c12.addArtefact("High priest crozier");
        c12.addArtefact("High priest mitre");
        c12.addArtefact("High priest orb");
        c12.addArtefact("Idithuun horn ring");
        c12.addArtefact("Garagorshuun anchor");
        Collection c13 = new Collection("Green gobbo goodies 3", "General Wartface", "Bandos", "Tetra pieces", 1, "Helm of terror outside");
        c13.addArtefact("Dorgeshuun spear");
        c13.addArtefact("Huzamogaarb chaos crown");
        c13.addArtefact("Saragorgak star crown");
        c13.addArtefact("Drogokishuun hook sword");
        c13.addArtefact("Horogothgar cooking pot");
        c13.addArtefact("'Da Boss Man' sculpture");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Isaura">
        Collection c14 = new Collection("Zamorakian I", "Isaura", "Zamorak", "Crontoes", 2594, "Abyssal Thread");
        c14.addArtefact("Hookah pipe");
        c14.addArtefact("Opulent wine goblet");
        c14.addArtefact("Crest of Dagon");
        c14.addArtefact("'Disorder' painting");
        c14.addArtefact("Imp mask");
        c14.addArtefact("Lesser demon mask");
        c14.addArtefact("Greater demon mask");
        c14.addArtefact("Order of Dis robes");
        c14.addArtefact("Ritual dagger");
        Collection c15 = new Collection("Zamorakian II", "Isaura", "Zamorak", "Tetra pieces", 1, "Tetra piece");
        c15.addArtefact("Branding iron");
        c15.addArtefact("Manacles");
        c15.addArtefact("'The Lake of Fire' painting");
        c15.addArtefact("'Lust' metal sculpture");
        c15.addArtefact("Chaos star");
        c15.addArtefact("Spiked dog collar");
        c15.addArtefact("Larupia trophy");
        c15.addArtefact("Lion trophy");
        c15.addArtefact("She-wolf trophy");
        Collection c16 = new Collection("Zamorakian III", "Isaura", "Zamorak", "Tetra pieces", 1, "Tetra piece");
        c16.addArtefact("'Torment' metal sculpture");
        c16.addArtefact("'Pandemonium' tapestry");
        c16.addArtefact("Hellfire haladie");
        c16.addArtefact("Hellfire katar");
        c16.addArtefact("Hellfire zaghnal");
        c16.addArtefact("Chaos Elemental trophy");
        c16.addArtefact("Virius trophy");
        Collection c17 = new Collection("Zamorakian IV", "Isaura", "Zamorak", "Tetra pieces", 1, "Ariadne's Diadem");
        c17.addArtefact("'Possession' metal sculpture");
        c17.addArtefact("Trishula");
        c17.addArtefact("Tsutsaroth piercing");
        c17.addArtefact("Tsutsaroth helm");
        c17.addArtefact("Tsutsaroth pauldron");
        c17.addArtefact("Tsutsaroth urumi");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Lowse">
        Collection c18 = new Collection("Armadylean I", "Lowse", "Armadyl", "Stormguard blueprint fragments", 50, "King Oberon's moonshroom spores");
        c18.addArtefact("Ikovian gerege");
        c18.addArtefact("Toy glider");
        c18.addArtefact("Toy war golem");
        c18.addArtefact("Avian song-egg player");
        c18.addArtefact("Keshik drum");
        c18.addArtefact("Morin khuur");
        c18.addArtefact("Aviansie dreamcoat");
        c18.addArtefact("Ceremonial plume");
        c18.addArtefact("Peacocking parasol");
        Collection c19 = new Collection("Armadylean II", "Lowse", "Armadyl", "Stormguard blueprint fragments", 75, "75 Stormguard blueprint fragments");
        c19.addArtefact("Hawkeye lens multi-vision scope");
        c19.addArtefact("Talon-3 razor wing");
        c19.addArtefact("Prototype gravimeter");
        c19.addArtefact("Songbird recorder");
        c19.addArtefact("Dayguard shield");
        c19.addArtefact("Stormguard gerege");
        c19.addArtefact("Golem heart");
        c19.addArtefact("Golem instruction");
        Collection c20 = new Collection("Armadylean III", "Lowse", "Armadyl", "Stormguard blueprint fragments", 150, "Howl's Thinking Cap");
        c20.addArtefact("Blackfire lance");
        c20.addArtefact("Nightguard shield");
        c20.addArtefact("Flat cap");
        c20.addArtefact("Night owl flight goggles");
        c20.addArtefact("Prototype godbow");
        c20.addArtefact("Prototype godstaff");
        c20.addArtefact("Prototype godsword");
        c20.addArtefact("Chuluu stone");
        c20.addArtefact("Quintessence counter");
        c20.addArtefact("Spherical astrolabe");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sir Atcha">
        Collection c21 = new Collection("Saradominist I", "Sir Atcha", "Saradomin", "Crontoes", 3998, "Lock of hair");
        c21.addArtefact("'Frying pan'");
        c21.addArtefact("Hallowed lantern");
        c21.addArtefact("Ceremonial unicorn ornament");
        c21.addArtefact("Ceremonial unicorn saddle");
        c21.addArtefact("Everlight harp");
        c21.addArtefact("Everlight trumpet");
        c21.addArtefact("Everlight violin");
        c21.addArtefact("Folded-arm figurine (female)");
        c21.addArtefact("Folded-arm figurine (male)");
        Collection c22 = new Collection("Saradominist II", "Sir Atcha", "Saradomin", "Tetra pieces", 1, "Tetracompass piece");
        c22.addArtefact("Dominion discus");
        c22.addArtefact("Dominion javelin");
        c22.addArtefact("Dominion pelte shield");
        c22.addArtefact("Bronze Dominion medal");
        c22.addArtefact("Silver Dominion medal");
        c22.addArtefact("Dominion torch");
        c22.addArtefact("Decorative vase");
        c22.addArtefact("Kantharos cup");
        c22.addArtefact("Patera bowl");
        Collection c23 = new Collection("Saradominist III", "Sir Atcha", "Saradomin", "Tetra pieces", 1, "Tetracompass piece");
        c23.addArtefact("Dominarian device");
        c23.addArtefact("Fishing trident");
        c23.addArtefact("Amphora");
        c23.addArtefact("Rod of Asclepius");
        c23.addArtefact("Kopis dagger");
        c23.addArtefact("Xiphos short sword");
        Collection c24 = new Collection("Saradominist IV", "Sir Atcha", "Saradomin", "Tetra pieces", 1, "Petasos");
        c24.addArtefact("'The Pride of Padosan' painting");
        c24.addArtefact("'Hallowed Be the Everlight' painting");
        c24.addArtefact("'The Lord of Light' painting");
        c24.addArtefact("'The Enlightened Soul' scroll");
        c24.addArtefact("'The Eudoxian Elements' tablet");
        c24.addArtefact("Doru spear");
        c24.addArtefact("Kontos lance");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Soran, Emissary of Zaros">
        Collection c25 = new Collection("Zarosian I", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 20, "Seal of the Praefectus Praetorio");
        c25.addArtefact("Venator dagger");
        c25.addArtefact("Venator light crossbow");
        c25.addArtefact("Legionary gladius");
        c25.addArtefact("Legionary square shield");
        c25.addArtefact("Primis Elementis standard");
        c25.addArtefact("Zaros effigy");
        c25.addArtefact("Zarosian training dummy");
        c25.addArtefact("Legatus Maximus figurine");
        c25.addArtefact("'Solem in Umbra' painting");
        Collection c26 = new Collection("Zarosian II", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 50, "50 Kharid-et pylon batteries");
        c26.addArtefact("Ancient timepiece");
        c26.addArtefact("Legatus pendant");
        c26.addArtefact("'Incite Fear' spell scroll");
        c26.addArtefact("Pontifex signet ring");
        c26.addArtefact("Ceremonial mace");
        c26.addArtefact("Pontifex Maximus figurine");
        c26.addArtefact("'Consensus ad Idem' painting");
        c26.addArtefact("Pontifex censer");
        c26.addArtefact("Pontifex crozier");
        c26.addArtefact("Pontifex mitre");
        Collection c27 = new Collection("Zarosian III", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 100, "100 Kharid-et pylon batteries");
        c27.addArtefact("'Exsanguinate' spell scroll");
        c27.addArtefact("Necromantic focus");
        c27.addArtefact("Zarosian ewer");
        c27.addArtefact("Zarosian stein");
        c27.addArtefact("'Smoke Cloud' spell scroll");
        c27.addArtefact("Vigorem vial");
        c27.addArtefact("Ancient magic tablet");
        c27.addArtefact("'Animate Dead' spell scroll");
        c27.addArtefact("Portable phylactery");
        Collection c28 = new Collection("Zarosian IV", "Soran, Emissary of Zaros", "Zaros", "Pylon batteries", 100, "Inquisitor staff piece");
        c28.addArtefact("Praetorian hood");
        c28.addArtefact("Praetorian robes");
        c28.addArtefact("Praetorian staff");
        c28.addArtefact("Ancient globe");
        c28.addArtefact("Battle plans");
        c28.addArtefact("'Prima Legio' painting");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Wise Old Man">
        Collection c29 = new Collection("Wise Am the Music Man", "Wise Old Man", "All", "Crontoes", 4024, "Koschei's needle");
        c29.addArtefact("Everlight harp");
        c29.addArtefact("Everlight trumpet");
        c29.addArtefact("Everlight violin");
        c29.addArtefact("Avian song-egg player");
        c29.addArtefact("Keshik drum");
        c29.addArtefact("Morin khuur");
        c29.addArtefact("Songbird recorder");
        Collection c30 = new Collection("Hat Hoarder", "Wise Old Man", "All", "Crontoes", 5995);
        c30.addArtefact("Lesser demon mask");
        c30.addArtefact("Greater demon mask");
        c30.addArtefact("Ceremonial unicorn ornament");
        c30.addArtefact("Pontifex mitre");
        c30.addArtefact("Saragorgak star crown");
        c30.addArtefact("Kal-i-kran chieftain crown");
        c30.addArtefact("Tsutsaroth helm");
        Collection c31 = new Collection("Hat Problem", "Wise Old Man", "All", "Crontoes", 6775);
        c31.addArtefact("Imp mask");
        c31.addArtefact("Ekeleshuun blinder mask");
        c31.addArtefact("High priest mitre");
        c31.addArtefact("Beastkeeper helm");
        c31.addArtefact("Huzamogaarb chaos crown");
        c31.addArtefact("Flat cap");
        c31.addArtefact("Praetorian hood");
        Collection c32 = new Collection("Magic Man", "Wise Old Man", "All", "Crontoes", 8600);
        c32.addArtefact("Legatus Maximus figurine");
        c32.addArtefact("Ritual dagger");
        c32.addArtefact("Ancient timepiece");
        c32.addArtefact("'Incite Fear' spell scroll");
        c32.addArtefact("'Exsanguinate' spell scroll");
        c32.addArtefact("High priest mitre");
        c32.addArtefact("'Smoke Cloud' spell scroll");
        c32.addArtefact("'Animate Dead' spell scroll");
        c32.addArtefact("Portable phylactery");
        c32.addArtefact("Chuluu stone");
        Collection c33 = new Collection("Knowledge is Power", "Wise Old Man", "All", "Crontoes", 5584, "Amascut's Enchanted Gem");
        c33.addArtefact("Crest of Dagon");
        c33.addArtefact("Ikovian gerege");
        c33.addArtefact("Necromantic focus");
        c33.addArtefact("Golem instruction");
        c33.addArtefact("'The Enlightened Soul' scroll");
        c33.addArtefact("'The Eudoxian Elements' tablet");
        c33.addArtefact("'Da Boss Man' sculpture");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Velucia">
        //<editor-fold defaultstate="collapsed" desc="Armadylean">
        Collection c34 = new Collection("Museum - Armadylean I", "Velucia", "Armadyl", "Cronotes", 7110);
        c34.addArtefact("Ikovian gerege");
        c34.addArtefact("Toy glider");
        c34.addArtefact("Toy war golem");
        c34.addArtefact("Avian song-egg player");
        c34.addArtefact("Keshik drum");
        c34.addArtefact("Morin khuur");
        c34.addArtefact("Aviansie dreamcoat");
        c34.addArtefact("Ceremonial plume");
        c34.addArtefact("Peacocking parasol");
        Collection c35 = new Collection("Museum - Armadylean II", "Velucia", "Armadyl", "Cronotes", 7585);
        c35.addArtefact("Hawkeye lens multi-vision scope");
        c35.addArtefact("Talon-3 razor wing");
        c35.addArtefact("Prototype gravimeter");
        c35.addArtefact("Songbird recorder");
        c35.addArtefact("Dayguard shield");
        c35.addArtefact("Stormguard gerege");
        c35.addArtefact("Golem heart");
        c35.addArtefact("Golem instruction");
        Collection c36 = new Collection("Museum - Armadylean III", "Velucia", "Armadyl", "Cronotes", 13085);
        c36.addArtefact("Blackfire lance");
        c36.addArtefact("Nightguard shield");
        c36.addArtefact("Flat cap");
        c36.addArtefact("Night owl flight goggles");
        c36.addArtefact("Prototype godbow");
        c36.addArtefact("Prototype godstaff");
        c36.addArtefact("Prototype godsword");
        c36.addArtefact("Chuluu stone");
        c36.addArtefact("Quintessence counter");
        c36.addArtefact("Spherical astrolabe");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Bandosian">
        Collection c37 = new Collection("Museum - Bandosian I", "Velucia", "Bandos", "Cronotes", 8445);
        c37.addArtefact("Ekeleshuun blinder mask");
        c37.addArtefact("Narogoshuun 'Hob-da-Gob' ball");
        c37.addArtefact("Rekeshuun war tether");
        c37.addArtefact("Ogre Kyzaj axe");
        c37.addArtefact("Ork cleaver sword");
        c37.addArtefact("Thorobshuun battle standard");
        c37.addArtefact("Yurkolgokh stink grenade");
        c37.addArtefact("High priest crozier");
        c37.addArtefact("High priest mitre");
        c37.addArtefact("High priest orb");
        Collection c38 = new Collection("Museum - Bandosian II", "Velucia", "Bandos", "Cronotes", 8257);
        c38.addArtefact("Beastkeeper helm");
        c38.addArtefact("Idithuun horn ring");
        c38.addArtefact("'Nosorog!' sculpture");
        c38.addArtefact("Ourg megahitter");
        c38.addArtefact("Ourg tower/goblin cower shield");
        c38.addArtefact("Garagorshuun anchor");
        c38.addArtefact("'Forged in War' sculpture");
        c38.addArtefact("Dorgeshuun spear");
        Collection c39 = new Collection("Museum - Bandosian III", "Velucia", "Bandos", "Cronotes", 11725);
        c39.addArtefact("Saragorgak star crown");
        c39.addArtefact("Huzamogaarb chaos crown");
        c39.addArtefact("Hobgoblin mansticker");
        c39.addArtefact("Drogokishuun hook sword");
        c39.addArtefact("Kal-i-kran chieftain crown");
        c39.addArtefact("Kal-i-kran mace");
        c39.addArtefact("Kal-i-kran warhorn");
        c39.addArtefact("Horogothgar cooking pot");
        c39.addArtefact("'Da Boss Man' sculpture");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Saradominist">
        Collection c40 = new Collection("Museum - Saradominist I", "Velucia", "Saradomin", "Cronotes", 4997);
        c40.addArtefact("'Frying pan'");
        c40.addArtefact("Hallowed lantern");
        c40.addArtefact("Ceremonial unicorn ornament");
        c40.addArtefact("Ceremonial unicorn saddle");
        c40.addArtefact("Everlight harp");
        c40.addArtefact("Everlight trumpet");
        c40.addArtefact("Everlight violin");
        c40.addArtefact("Folded-arm figurine (female)");
        c40.addArtefact("Folded-arm figurine (male)");
        Collection c41 = new Collection("Museum - Saradominist II", "Velucia", "Saradomin", "Cronotes", 6052);
        c41.addArtefact("Dominion discus");
        c41.addArtefact("Dominion javelin");
        c41.addArtefact("Dominion pelte shield");
        c41.addArtefact("Bronze Dominion medal");
        c41.addArtefact("Silver Dominion medal");
        c41.addArtefact("Dominion torch");
        c41.addArtefact("Decorative vase");
        c41.addArtefact("Kantharos cup");
        c41.addArtefact("Patera bowl");
        Collection c42 = new Collection("Museum - Saradominist III", "Velucia", "Saradomin", "Cronotes", 5685);
        c42.addArtefact("Dominarian device");
        c42.addArtefact("Fishing trident");
        c42.addArtefact("Amphora");
        c42.addArtefact("Rod of Asclepius");
        c42.addArtefact("Kopis dagger");
        c42.addArtefact("Xiphos short sword");
        Collection c43 = new Collection("Museum - Saradominist IV", "Velucia", "Saradomin", "Cronotes", 8778);
        c43.addArtefact("'The Pride of Padosan' painting");
        c43.addArtefact("'Hallowed Be the Everlight' painting");
        c43.addArtefact("'The Lord of Light' painting");
        c43.addArtefact("'The Enlightened Soul' scroll");
        c43.addArtefact("'The Eudoxian Elements' tablet");
        c43.addArtefact("Doru spear");
        c43.addArtefact("Kontos lance");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Zamorakian">
        Collection c44 = new Collection("Museum - Zamorakian I", "Velucia", "Zamorak", "Cronotes", 3242);
        c44.addArtefact("Hookah pipe");
        c44.addArtefact("Opulent wine goblet");
        c44.addArtefact("Crest of Dagon");
        c44.addArtefact("'Disorder' painting");
        c44.addArtefact("Imp mask");
        c44.addArtefact("Lesser demon mask");
        c44.addArtefact("Greater demon mask");
        c44.addArtefact("Order of Dis robes");
        c44.addArtefact("Ritual dagger");
        Collection c45 = new Collection("Museum - Zamorakian II", "Velucia", "Zamorak", "Cronotes", 6432);
        c45.addArtefact("Branding iron");
        c45.addArtefact("Manacles");
        c45.addArtefact("'The Lake of Fire' painting");
        c45.addArtefact("'Lust' metal sculpture");
        c45.addArtefact("Chaos star");
        c45.addArtefact("Spiked dog collar");
        c45.addArtefact("Larupia trophy");
        c45.addArtefact("Lion trophy");
        c45.addArtefact("She-wolf trophy");
        Collection c46 = new Collection("Museum - Zamorakian III", "Velucia", "Zamorak", "Cronotes", 7417);
        c46.addArtefact("'Torment' metal sculpture");
        c46.addArtefact("'Pandemonium' tapestry");
        c46.addArtefact("Hellfire haladie");
        c46.addArtefact("Hellfire katar");
        c46.addArtefact("Hellfire zaghnal");
        c46.addArtefact("Chaos Elemental trophy");
        c46.addArtefact("Virius trophy");
        Collection c47 = new Collection("Museum - Zamorakian IV", "Velucia", "Zamorak", "Cronotes", 7635);
        c47.addArtefact("'Possession' metal sculpture");
        c47.addArtefact("Trishula");
        c47.addArtefact("Tsutsaroth piercing");
        c47.addArtefact("Tsutsaroth helm");
        c47.addArtefact("Tsutsaroth pauldron");
        c47.addArtefact("Tsutsaroth urumi");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Zarosian">
        Collection c48 = new Collection("Museum - Zarosian I", "Velucia", "Zaros", "Cronotes", 2635);
        c48.addArtefact("Venator dagger");
        c48.addArtefact("Venator light crossbow");
        c48.addArtefact("Legionary gladius");
        c48.addArtefact("Legionary square shield");
        c48.addArtefact("Primis Elementis standard");
        c48.addArtefact("Zaros effigy");
        c48.addArtefact("Zarosian training dummy");
        c48.addArtefact("Legatus Maximus figurine");
        c48.addArtefact("'Solem in Umbra' painting");
        Collection c49 = new Collection("Museum - Zarosian II", "Velucia", "Zaros", "Cronotes", 7165);
        c49.addArtefact("Ancient timepiece");
        c49.addArtefact("Legatus pendant");
        c49.addArtefact("'Incite Fear' spell scroll");
        c49.addArtefact("Pontifex signet ring");
        c49.addArtefact("Ceremonial mace");
        c49.addArtefact("Pontifex Maximus figurine");
        c49.addArtefact("'Consensus ad Idem' painting");
        c49.addArtefact("Pontifex censer");
        c49.addArtefact("Pontifex crozier");
        c49.addArtefact("Pontifex mitre");
        Collection c50 = new Collection("Museum - Zarosian III", "Velucia", "Zaros", "Cronotes", 9302);
        c50.addArtefact("'Exsanguinate' spell scroll");
        c50.addArtefact("Necromantic focus");
        c50.addArtefact("Zarosian ewer");
        c50.addArtefact("Zarosian stein");
        c50.addArtefact("'Smoke Cloud' spell scroll");
        c50.addArtefact("Vigorem vial");
        c50.addArtefact("Ancient magic tablet");
        c50.addArtefact("'Animate Dead' spell scroll");
        c50.addArtefact("Portable phylactery");
        Collection c51 = new Collection("Museum - Zarosian IV", "Velucia", "Zaros", "Cronotes", 8512);
        c51.addArtefact("Praetorian hood");
        c51.addArtefact("Praetorian robes");
        c51.addArtefact("Praetorian staff");
        c51.addArtefact("Ancient globe");
        c51.addArtefact("Battle plans");
        c51.addArtefact("'Prima Legio' painting");
        //</editor-fold>
        //</editor-fold>

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

        //<editor-fold defaultstate="collapsed" desc="level objects">

        LevelInfo i1 = new LevelInfo("Venator remains", "Kharid-et", 5);
        i1.addArtefact("Venator light crossbow");
        i1.addArtefact("Venator dagger");

        LevelInfo i2 = new LevelInfo("Legionary remains", "Kharid-et", 12);
        i2.addArtefact("Legionary gladius");
        i2.addArtefact("Primis Elementis standard");
        i2.addArtefact("Legionary square shield");

        LevelInfo i3 = new LevelInfo("Castra debris", "Kharid-et", 17);
        i3.addArtefact("Zaros effigy");
        i3.addArtefact("Zarosian training dummy");

        LevelInfo i4 = new LevelInfo("Lodge bar storage", "Infernal source", 20);
        i4.addArtefact("Opulent wine goblet");
        i4.addArtefact("Hookah pipe");

        LevelInfo i5 = new LevelInfo("Lodge art storage", "Infernal source", 24);
        i5.addArtefact("Crest of Dagon");
        i5.addArtefact("'Disorder' painting");

        LevelInfo i6 = new LevelInfo("Administratum debris", "Kharid-et", 25);
        i6.addArtefact("'Solem in Umbra' painting");
        i6.addArtefact("Legatus Maximus figurine");

        LevelInfo i7 = new LevelInfo("Cultist footlocker", "Infernal source", 29);
        i7.addArtefact("Greater demon mask");
        i7.addArtefact("Imp mask");
        i7.addArtefact("Lesser demon mask");

        LevelInfo i8 = new LevelInfo("Sacrificial altar", "Infernal source", 36);
        i8.addArtefact("Order of Dis robes");
        i8.addArtefact("Ritual dagger");

        LevelInfo i9 = new LevelInfo("Prodromoi remains", "Everlight", 42);
        i9.addArtefact("Hallowed lantern");
        i9.addArtefact("'Frying pan'");

        LevelInfo i10 = new LevelInfo("Dis dungeon debris", "Infernal source", 45);
        i10.addArtefact("Branding iron");
        i10.addArtefact("Manacles");

        LevelInfo i11 = new LevelInfo("Praesidio remains", "Kharid-et", 47);
        i11.addArtefact("Ancient timepiece");
        i11.addArtefact("Legatus pendant");

        LevelInfo i12 = new LevelInfo("Monoceros remains", "Everlight", 48);
        i12.addArtefact("Ceremonial unicorn ornament");
        i12.addArtefact("Ceremonial unicorn saddle");

        LevelInfo i13 = new LevelInfo("Amphitheatre debris", "Everlight", 51);
        i13.addArtefact("Everlight harp");
        i13.addArtefact("Everlight trumpet");
        i13.addArtefact("Everlight violin");

        LevelInfo i14 = new LevelInfo("Ceramics studio debris", "Everlight", 56);
        i14.addArtefact("Folded-arm figurine (female)");
        i14.addArtefact("Folded-arm figurine (male)");

        LevelInfo i15 = new LevelInfo("Carcerem debris", "Kharid-et", 58);
        i15.addArtefact("Pontifex signet ring");
        i15.addArtefact("'Incite Fear' spell scroll");

        LevelInfo i16 = new LevelInfo("Stadio debris", "Everlight", 61);
        i16.addArtefact("Dominion discus");
        i16.addArtefact("Dominion javelin");
        i16.addArtefact("Dominion pelte shield");

        LevelInfo i17 = new LevelInfo("Infernal art", "Infernal source", 65);
        i17.addArtefact("'Lust' metal sculpture");
        i17.addArtefact("'The Lake of Fire' painting");

        LevelInfo i18 = new LevelInfo("Shakroth remains", "Infernal source", 68);
        i18.addArtefact("Chaos star");
        i18.addArtefact("Spiked dog collar");

        LevelInfo i19 = new LevelInfo("Dominion games podium", "Everlight", 69);
        i19.addArtefact("Silver Dominion medal");
        i19.addArtefact("Bronze Dominion medal");
        i19.addArtefact("Dominion torch");

        LevelInfo i20 = new LevelInfo("Ikovian memorial", "Stormguard Citadel", 70);
        i20.addArtefact("Ikovian gerege");
        i20.addArtefact("Toy glider");
        i20.addArtefact("Toy war golem");

        LevelInfo i21 = new LevelInfo("Oikos studio debris", "Everlight", 72);
        i21.addArtefact("Decorate vase");
        i21.addArtefact("Patera bowl");
        i21.addArtefact("Kantharos cup");

        LevelInfo i22 = new LevelInfo("Kharid-et chapel debris", "Kharid-et", 74);
        i22.addArtefact("Ceremonial mace");
        i22.addArtefact("'Consensus ad Idem' painting");
        i22.addArtefact("Pontifex Maximus figurine");

        LevelInfo i23 = new LevelInfo("Keshik ger", "Stormguard Citadel", 76);
        i23.addArtefact("Avian song-egg player");
        i23.addArtefact("Keshik drum");
        i23.addArtefact("Morin khuur");

        LevelInfo i24 = new LevelInfo("Gladiatorial goblin remains", "Warforge", 76);
        i24.addArtefact("Ekeleshuun blinder mask");
        i24.addArtefact("Narogoshuun 'Hob-da-Gob' ball");
        i24.addArtefact("Rekeshuun war tether");

        LevelInfo i25 = new LevelInfo("Animal trophies", "Infernal source", 81);
        i25.addArtefact("Lion trophy");
        i25.addArtefact("Larupia trophy");
        i25.addArtefact("She-wolf trophy");

        LevelInfo i26 = new LevelInfo("Pontifex remains", "Kharid-et", 81);
        i26.addArtefact("Pontifex censer");
        i26.addArtefact("Pontifex crozier");
        i26.addArtefact("Pontifex mitre");

        LevelInfo i27 = new LevelInfo("Tailory debris", "Stormguard Citadel", 81);
        i27.addArtefact("Aviansie dreamcoat");
        i27.addArtefact("Ceremonial plume");
        i27.addArtefact("Peacocking parasol");

        LevelInfo i28 = new LevelInfo("Crucible stands debris", "Warforge", 81);
        i28.addArtefact("Ork cleaver sword");
        i28.addArtefact("Ogre kyzaj axe");

        LevelInfo i29 = new LevelInfo("Goblin dorm debris", "Warforge", 83);
        i29.addArtefact("Thorobshuun battle standard");
        i29.addArtefact("Yurkolgokh stink grenade");

        LevelInfo i30 = new LevelInfo("Oikos fishing hut remains", "Everlight", 84);
        i30.addArtefact("Dominarian device");
        i30.addArtefact("Fishing trident");

        LevelInfo i31 = new LevelInfo("Weapons research debris", "Stormguard Citadel", 85);
        i31.addArtefact("Hawkeye lens multi-vision scope");
        i31.addArtefact("Talon-3 razor wing");

        LevelInfo i32 = new LevelInfo("Orcus altar", "Kharid-et", 86);
        i32.addArtefact("'Exsanguinate' spell scroll");
        i32.addArtefact("Necromantic focus");

        LevelInfo i33 = new LevelInfo("Dis overspill", "Infernal source", 89);
        i33.addArtefact("Pandemonium' tapestry");
        i33.addArtefact("'Torment' metal sculpture");

        LevelInfo i34 = new LevelInfo("Big high war god shrine", "Warforge", 89);
        i34.addArtefact("High priest crozier");
        i34.addArtefact("High priest mitre");
        i34.addArtefact("High priest orb");

        LevelInfo i35 = new LevelInfo("Gravitron research debris", "Stormguard Citadel", 91);
        i35.addArtefact("Prototype gravimeter");
        i35.addArtefact("Songbird recorder");

        LevelInfo i36 = new LevelInfo("Acropolis debris", "Everlight", 92);
        i36.addArtefact("Amphora");
        i36.addArtefact("Rod of Asclepius");

        LevelInfo i37 = new LevelInfo("Armarium debris", "Kharid-et", 93);
        i37.addArtefact("Zarosian ewer");
        i37.addArtefact("Zarosian stein");

        LevelInfo i38 = new LevelInfo("Yubiusk animal pen", "Warforge", 94);
        i38.addArtefact("Beastkeeper helm");
        i38.addArtefact("'Nosorog!' sculpture");
        i38.addArtefact("Idithuun horn rings");

        LevelInfo i39 = new LevelInfo("Keshik tower debris", "Stormguard Citadel", 95);
        i39.addArtefact("Stormguard gerege");
        i39.addArtefact("Dayguard shield");

        LevelInfo i40 = new LevelInfo("Goblin trainee remains", "Warforge", 97);
        i40.addArtefact("Garagorshuun anchor");
        i40.addArtefact("Ourg megahitter");
        i40.addArtefact("Ourg tower/goblin cower shield");

        LevelInfo i41 = new LevelInfo("Byzroth remains", "Infernal source", 98);
        i41.addArtefact("Hellfire haladie");
        i41.addArtefact("Hellfire katar");
        i41.addArtefact("Hellfire zaghnal");

        LevelInfo i42 = new LevelInfo("Destroyed golem", "Stormguard Citadel", 98);
        i42.addArtefact("Golem heart");
        i42.addArtefact("Golem instruction");

        LevelInfo i43 = new LevelInfo("Icyene weapon rack", "Everlight", 100);
        i43.addArtefact("Kopis dagger");
        i43.addArtefact("Xiphos short sword");

        LevelInfo i44 = new LevelInfo("Culinarum debris", "Kharid-et", 100);
        i44.addArtefact("Vigorem vial");
        i44.addArtefact("'Smoke Cloud' spell scroll");

        LevelInfo i45 = new LevelInfo("Kyzaj champions boudoir", "Warforge", 100);
        i45.addArtefact("Dorgeshuun spear");
        i45.addArtefact("'Forged in War' sculpture");

        LevelInfo i46 = new LevelInfo("Keshik weapon rack", "Stormguard Citadel", 103);
        i46.addArtefact("Blackfire lance");
        i46.addArtefact("Nightguard shield");

        LevelInfo i47 = new LevelInfo("Hellfire forge", "Infernal source", 104);
        i47.addArtefact("'Possession' metal sculpture");
        i47.addArtefact("Trishula");
        i47.addArtefact("Tsutsaroth piercing");

        LevelInfo i48 = new LevelInfo("Warforge scrap pile", "Warforge", 104);
        i48.addArtefact("Huzamogaarb chaos crown");
        i48.addArtefact("Saragorgak star crowns");

        LevelInfo i49 = new LevelInfo("Stockpiled art", "Everlight", 105);
        i49.addArtefact("'The Pride of Padosan' painting");
        i49.addArtefact("'Hallowed Be the Everlight' painting");
        i49.addArtefact("'The Lord of Light' painting");

        LevelInfo i50 = new LevelInfo("Ancient magick munitions", "Kharid-et", 107);
        i50.addArtefact("Ancient magic tablet");
        i50.addArtefact("Portable phylactery");
        i50.addArtefact("'Animate Dead' spell scroll");

        LevelInfo i51 = new LevelInfo("Bibliotheke debris", "Everlight", 109);
        i51.addArtefact("'The Enlightened Soul' scroll");
        i51.addArtefact("'The Eudoxian Elements' tablet");

        LevelInfo i52 = new LevelInfo("Chthonian trophies", "Infernal source", 110);
        i52.addArtefact("Virius trophy");
        i52.addArtefact("Chaos elemental trophy");

        LevelInfo i53 = new LevelInfo("Warforge weapon rack", "Warforge", 110);
        i53.addArtefact("Hobgoblin mansticker");
        i53.addArtefact("Drogokishuun hook swords");

        LevelInfo i54 = new LevelInfo("Flight research debris", "Stormguard Citadel", 111);
        i54.addArtefact("Flat cap");
        i54.addArtefact("Night owl flying goggles");

        LevelInfo i55 = new LevelInfo("Aetherium forge", "Stormguard Citadel", 112);
        i55.addArtefact("Prototype godbow");
        i55.addArtefact("Prototype godstaff");
        i55.addArtefact("Prototype godsword");

        LevelInfo i56 = new LevelInfo("Praetorian remains", "Kharid-et", 114);
        i56.addArtefact("Praetorian hood");
        i56.addArtefact("Praetorian robes");
        i56.addArtefact("Praetorian staff");

        LevelInfo i57 = new LevelInfo("Bandos sanctum debris", "Warforge", 115);
        i57.addArtefact("Kal-i-kran chieftain crown");
        i57.addArtefact("Kal-i-kran mace");
        i57.addArtefact("Kal-i-kran warhorn");

        LevelInfo i58 = new LevelInfo("Tsutsaroth remains", "Infernal source", 116);
        i58.addArtefact("Tsutsaroth helm");
        i58.addArtefact("Tsutsaroth pauldron");
        i58.addArtefact("Tsutsaroth urumi");

        LevelInfo i59 = new LevelInfo("Optimatoi remains", "Everlight", 117);
        i59.addArtefact("Kontos lance");
        i59.addArtefact("Doru spear");

        LevelInfo i60 = new LevelInfo("War table debris", "Kharid-et", 118);
        i60.addArtefact("Ancient globe");
        i60.addArtefact("Battle plans");
        i60.addArtefact("'Prima Legio' painting");

        LevelInfo i61 = new LevelInfo("Howl's workshop debris", "Stormguard Citadel", 118);
        i61.addArtefact("Chuluu stone");
        i61.addArtefact("Quintessence counter");
        i61.addArtefact("Spherical astrolabe");

        LevelInfo i62 = new LevelInfo("Makeshift pie oven", "Warforge", 119);
        i62.addArtefact("Horogothgar cooking pot");
        i62.addArtefact("'Da Boss Man' sculpture");

        //</editor-fold>
        ArrayList<LevelInfo> liveLevelArray = new ArrayList<LevelInfo>(){{
            add(i1); add(i2); add(i3); add(i4); add(i5); add(i6); add(i7); add(i8); add(i9); add(i10);
            add(i11); add(i12); add(i13); add(i14); add(i15); add(i16); add(i17); add(i18); add(i19); add(i20);
            add(i21); add(i22); add(i23); add(i24); add(i25); add(i26); add(i27); add(i28); add(i29); add(i30);
            add(i31); add(i32); add(i33); add(i34); add(i35); add(i36); add(i37); add(i38); add(i39); add(i40);
            add(i41); add(i42); add(i43); add(i44); add(i45); add(i46); add(i47); add(i48); add(i49); add(i50);
            add(i51);add(i52);add(i53);add(i54);add(i55);add(i56);add(i57);add(i58);add(i59);add(i60);add(i61);add(i62);
        }};
        liveLevelArray.forEach(this::AddLevelInfo);
    }

    //</editor-fold>

}
