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
        //only need the material names for now, will add quantities and locations on the fly
        ArrayList<Material> liveMaterialArray = new ArrayList<Material>(){{
            add(new Material("Aetherium alloy"));
            add(new Material("Ancient fis"));
            add(new Material("Animal furs"));
            add(new Material("Armadylean yellow"));
            add(new Material("Blood of Orcus"));
            add(new Material("Cadmium red"));
            add(new Material("Chaotic brimstone"));
            add(new Material("Cobalt blue"));
            add(new Material("Demonhide"));
            add(new Material("Everlight silvthril"));
            add(new Material("Eye of Dagon"));
            add(new Material("Fossilised bone"));
            add(new Material("Goldrune"));
            add(new Material("Hellfire metal"));
            add(new Material("Imperial steel"));
            add(new Material("Keramos"));
            add(new Material("Leather scraps"));
            add(new Material("Malachite green"));
            add(new Material("Mark of the Kyzaj"));
            add(new Material("Orthenglass"));
            add(new Material("Quintessence"));
            add(new Material("Samite silk"));
            add(new Material("Soapstone"));
            add(new Material("Star of Saradomin"));
            add(new Material("Stormguard steel"));
            add(new Material("Third Age iron"));
            add(new Material("Tyrian purple"));
            add(new Material("Vellum"));
            add(new Material("Vulcanised rubber"));
            add(new Material("Warforged bronze"));
            add(new Material("White marble"));
            add(new Material("White oak"));
            add(new Material("Wings of War"));
            add(new Material("Yu'biusk clay"));
            add(new Material("Zarosian insignia"));
        }};
        liveMaterialArray.forEach(this::AddMaterial);

        //<editor-fold defaultstate="collapsed" desc="Game Artefact objects">
        GameArtefact ga1 = new GameArtefact("'Animate dead' spell scroll", "Zaros");
        GameArtefact ga2 = new GameArtefact("'Consensus ad Idem' painting", "Zaros");
        GameArtefact ga3 = new GameArtefact("'Da Boss Man' sculpture", "Bandos");
        GameArtefact ga4 = new GameArtefact("'Disorder' painting", "Zamorak");
        GameArtefact ga5 = new GameArtefact("'Exsanguinate' spell scroll", "Zaros");
        GameArtefact ga6 = new GameArtefact("'Forged in War' sculpture", "Bandos");
        GameArtefact ga7 = new GameArtefact("'Frying pan'", "Saradomin");
        GameArtefact ga8 = new GameArtefact("'Hallowed Be the Everlight' painting", "Saradomin");
        GameArtefact ga9 = new GameArtefact("'Incite fear' spell scroll", "Zaros");
        GameArtefact ga10 = new GameArtefact("'Lust' metal sculpture", "Zamorak");
        GameArtefact ga11 = new GameArtefact("'Nosorog!' sculpture", "Bandos");
        GameArtefact ga12 = new GameArtefact("'Pandemonium' tapestry", "Zamorak");
        GameArtefact ga13 = new GameArtefact("'Possession' metal sculpture", "Zamorak");
        GameArtefact ga14 = new GameArtefact("'Prima legio' painting", "Zaros");
        GameArtefact ga15 = new GameArtefact("'Smoke cloud' spell scroll", "Zaros");
        GameArtefact ga16 = new GameArtefact("'Solem in Umbra' painting", "Zaros");
        GameArtefact ga17 = new GameArtefact("'The Enlightened Soul' scroll", "Saradomin");
        GameArtefact ga18 = new GameArtefact("'The Eudoxian Elements' tablet", "Saradomin");
        GameArtefact ga19 = new GameArtefact("'The Lake of Fire' painting", "Zamorak");
        GameArtefact ga20 = new GameArtefact("'The Lord of Light' painting", "Saradomin");
        GameArtefact ga21 = new GameArtefact("'The Pride of Padosan' painting", "Saradomin");
        GameArtefact ga22 = new GameArtefact("'Torment' metal sculpture", "Zamorak");
        GameArtefact ga23 = new GameArtefact("Amphora", "Saradomin");
        GameArtefact ga24 = new GameArtefact("Ancient globe", "Zaros");
        GameArtefact ga25 = new GameArtefact("Ancient magic tablet", "Zaros");
        GameArtefact ga26 = new GameArtefact("Ancient timepiece", "Zaros");
        GameArtefact ga27 = new GameArtefact("Avian song-egg player", "Armadyl");
        GameArtefact ga28 = new GameArtefact("Aviansie dreamcoat", "Armadyl");
        GameArtefact ga29 = new GameArtefact("Battle plans", "Zaros");
        GameArtefact ga30 = new GameArtefact("Beastkeeper helm", "Bandos");
        GameArtefact ga31 = new GameArtefact("Blackfire lance", "Armadyl");
        GameArtefact ga32 = new GameArtefact("Branding iron", "Zamorak");
        GameArtefact ga33 = new GameArtefact("Bronze Dominion medal", "Saradomin");
        GameArtefact ga34 = new GameArtefact("Ceremonial mace", "Zaros");
        GameArtefact ga35 = new GameArtefact("Ceremonial plume", "Armadyl");
        GameArtefact ga36 = new GameArtefact("Ceremonial unicorn ornament", "Saradomin");
        GameArtefact ga37 = new GameArtefact("Ceremonial unicorn saddle", "Saradomin");
        GameArtefact ga38 = new GameArtefact("Chaos elemental trophy", "Zamorak");
        GameArtefact ga39 = new GameArtefact("Chaos star", "Zamorak");
        GameArtefact ga40 = new GameArtefact("Chuluu stone", "Armadyl");
        GameArtefact ga41 = new GameArtefact("Crest of Dagon", "Zamorak");
        GameArtefact ga42 = new GameArtefact("Dayguard shield", "Armadyl");
        GameArtefact ga43 = new GameArtefact("Decorate vase", "Saradomin");
        GameArtefact ga44 = new GameArtefact("Dominarian device", "Saradomin");
        GameArtefact ga45 = new GameArtefact("Dominion discus", "Saradomin");
        GameArtefact ga46 = new GameArtefact("Dominion javelin", "Saradomin");
        GameArtefact ga47 = new GameArtefact("Dominion pelte shield", "Saradomin");
        GameArtefact ga48 = new GameArtefact("Dominion torch", "Saradomin");
        GameArtefact ga49 = new GameArtefact("Dorgeshuun spear", "Bandos");
        GameArtefact ga50 = new GameArtefact("Doru spear", "Saradomin");
        GameArtefact ga51 = new GameArtefact("Drogokishuun hook swords", "Bandos");
        GameArtefact ga52 = new GameArtefact("Ekeleshuun blinder mask", "Bandos");
        GameArtefact ga53 = new GameArtefact("Everlight harp", "Saradomin");
        GameArtefact ga54 = new GameArtefact("Everlight trumpet", "Saradomin");
        GameArtefact ga55 = new GameArtefact("Everlight violin", "Saradomin");
        GameArtefact ga56 = new GameArtefact("Fishing trident", "Saradomin");
        GameArtefact ga57 = new GameArtefact("Flat cap", "Armadyl");
        GameArtefact ga58 = new GameArtefact("Folded-arm figurine (female)", "Saradomin");
        GameArtefact ga59 = new GameArtefact("Folded-arm figurine (male)", "Saradomin");
        GameArtefact ga60 = new GameArtefact("Garagorshuun anchor", "Bandos");
        GameArtefact ga61 = new GameArtefact("Golem heart", "Armadyl");
        GameArtefact ga62 = new GameArtefact("Golem instruction", "Armadyl");
        GameArtefact ga63 = new GameArtefact("Greater demon mask", "Zamorak");
        GameArtefact ga64 = new GameArtefact("Hallowed lantern", "Saradomin");
        GameArtefact ga65 = new GameArtefact("Hawkeye lens multi-vision scope", "Armadyl");
        GameArtefact ga66 = new GameArtefact("Hellfire haladie", "Zamorak");
        GameArtefact ga67 = new GameArtefact("Hellfire katar", "Zamorak");
        GameArtefact ga68 = new GameArtefact("Hellfire zaghnal", "Zamorak");
        GameArtefact ga69 = new GameArtefact("High priest crozier", "Bandos");
        GameArtefact ga70 = new GameArtefact("High priest mitre", "Bandos");
        GameArtefact ga71 = new GameArtefact("High priest orb", "Bandos");
        GameArtefact ga72 = new GameArtefact("Hobgoblin mansticker", "Bandos");
        GameArtefact ga73 = new GameArtefact("Hookah pipe", "Zamorak");
        GameArtefact ga74 = new GameArtefact("Horogothgar cooking pot", "Bandos");
        GameArtefact ga75 = new GameArtefact("Huzamogaarb chaos crown", "Bandos");
        GameArtefact ga76 = new GameArtefact("Idithuun horn rings", "Bandos");
        GameArtefact ga77 = new GameArtefact("Ikovian gerege", "Armadyl");
        GameArtefact ga78 = new GameArtefact("Imp mask", "Zamorak");
        GameArtefact ga79 = new GameArtefact("Kal-i-kran chieftain crown", "Bandos");
        GameArtefact ga80 = new GameArtefact("Kal-i-kran mace", "Bandos");
        GameArtefact ga81 = new GameArtefact("Kal-i-kran warhorn", "Bandos");
        GameArtefact ga82 = new GameArtefact("Kantharos cup", "Saradomin");
        GameArtefact ga83 = new GameArtefact("Keshik drum", "Armadyl");
        GameArtefact ga84 = new GameArtefact("Kontos lance", "Saradomin");
        GameArtefact ga85 = new GameArtefact("Kopis dagger", "Saradomin");
        GameArtefact ga86 = new GameArtefact("Larupia trophy", "Zamorak");
        GameArtefact ga87 = new GameArtefact("Legatus maximus figurine", "Zaros");
        GameArtefact ga88 = new GameArtefact("Legatus pendant", "Zaros");
        GameArtefact ga89 = new GameArtefact("Legionary gladius", "Zaros");
        GameArtefact ga90 = new GameArtefact("Legionary square shield", "Zaros");
        GameArtefact ga91 = new GameArtefact("Lesser demon mask", "Zamorak");
        GameArtefact ga92 = new GameArtefact("Lion trophy", "Zamorak");
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
