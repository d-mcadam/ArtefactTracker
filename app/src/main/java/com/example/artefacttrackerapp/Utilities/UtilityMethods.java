package com.example.artefacttrackerapp.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artefacttrackerapp.R;
import com.example.artefacttrackerapp.activities.MaterialOptionsActivity;
import com.example.artefacttrackerapp.data.Collection;
import com.example.artefacttrackerapp.data.Collector;
import com.example.artefacttrackerapp.data.GameArtefact;
import com.example.artefacttrackerapp.data.Material;
import com.example.artefacttrackerapp.data.Storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class UtilityMethods {

    //<editor-fold defaultstate="collapsed" desc="Collection Dialog Generator">

    /**
     * for collector adapter with NO initial spinner selection
     * @param context               the parent context
     * @param searchField          the initial string entry from the search box to default the title to
     * @param collectorViewAdapter  the collector adapter for the recycler view
     */
    public static void CreateCollectionDialogGenerator(final Context context, EditText searchField, CollectorAdapter collectorViewAdapter){
        CreateCollectionDialogGenerator(context, searchField, 0, 0, null, collectorViewAdapter, null);
    }

    public static void CreateCollectionDialogGenerator(final Context context, EditText searchField, CollectorAdapter collectorViewAdapter, Collector collector){
        CreateCollectionDialogGenerator(context, searchField, 0, 0, null, collectorViewAdapter, collector);
    }

    /**
     * for collector adapter with initial spinner selection
     * @param context               the parent context
     * @param searchField          the initial string entry from the search box to default the title to
     * @param collectorViewAdapter  the collector adapter for the recycler view
     * @param categoryPos           the position of the category spinner
     * @param rewardPos             the position of the reward spinner
     */
    public static void CreateCollectionDialogGenerator(final Context context, EditText searchField, CollectorAdapter collectorViewAdapter, int categoryPos, int rewardPos){
        CreateCollectionDialogGenerator(context, searchField, categoryPos, rewardPos, null, collectorViewAdapter, null);
    }

    /**
     * for collection adapter with NO initial spinner selection
     * @param context               the parent context
     * @param searchField          the initial string entry from the search box to default the title to
     * @param collectionViewAdapter the collection adapter for the recycler view
     */
    public static void CreateCollectionDialogGenerator(final Context context, EditText searchField, CollectionAdapter collectionViewAdapter){
        CreateCollectionDialogGenerator(context, searchField, 0, 0, collectionViewAdapter, null, null);
    }

    /**
     * for collection adapter with initial spinner selection
     * @param context               the parent context
     * @param searchField          the initial string entry from the search box to default the title to
     * @param collectionViewAdapter the collection adapter for the recycler view
     * @param categoryPos           the position of the category spinner
     * @param rewardPos             the position of the reward spinner
     */
    public static void CreateCollectionDialogGenerator(final Context context, EditText searchField, CollectionAdapter collectionViewAdapter, int categoryPos, int rewardPos){
        CreateCollectionDialogGenerator(context, searchField, categoryPos, rewardPos, collectionViewAdapter, null, null);
    }

    private static void CreateCollectionDialogGenerator(final Context context, EditText searchField, int categoryPos, int rewardPos,
                                                        CollectionAdapter collectionViewAdapter, CollectorAdapter collectorViewAdapter, Collector collector){

        //<editor-fold defaultstate="collapsed" desc="check collector count">
        if (storage.Collectors().size() < 1){
            Toast.makeText(context, "No collectors in storage", Toast.LENGTH_LONG).show();
            return;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Declare dialog and view and reference activity components">
        AlertDialog.Builder collectionDialog = new AlertDialog.Builder(context);
        collectionDialog.setTitle("Create a Collection");

        View collectionDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_collection, null);
        final Spinner inputCollectorSpinner = collectionDialogView.findViewById(R.id.spinnerHolderCreateCollectionCollector);
        final EditText inputNameField = collectionDialogView.findViewById(R.id.editTextHolderCreateCollectionName);
        final Spinner inputCategorySpinner = collectionDialogView.findViewById(R.id.spinnerHolderCreateCollectionArtefactCategory);
        final Spinner inputRewardSpinner = collectionDialogView.findViewById(R.id.spinnerHolderCreateCollectionReward);
        final EditText inputQtyField = collectionDialogView.findViewById(R.id.editTextHolderCreateCollectionRewardQuantity);
        //</editor-fold>

        inputNameField.setText(searchField.getText().toString().trim());

        //<editor-fold defaultstate="collapsed" desc="Populate collector list">
        ArrayList<String> collectorValues = new ArrayList<>();
        storage.Collectors().forEach(c -> collectorValues.add(c.name));
        ArrayAdapter<String> collectorAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, collectorValues);
        collectorAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        inputCollectorSpinner.setAdapter(collectorAdapter);
        if (collector != null)
            inputCollectorSpinner.setSelection(collectorAdapter.getPosition(collector.name));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Populate category and reward spinners from resource">
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(context, R.array.artefact_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputCategorySpinner.setAdapter(categoryAdapter);
        inputCategorySpinner.setSelection(categoryPos/*categorySpinner.getSelectedItemPosition()*/);

        ArrayAdapter<CharSequence> rewardAdapter = ArrayAdapter.createFromResource(context, R.array.collection_rewards, android.R.layout.simple_spinner_item);
        rewardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputRewardSpinner.setAdapter(rewardAdapter);
        inputRewardSpinner.setSelection(rewardPos/*rewardSpinner.getSelectedItemPosition()*/);
        //</editor-fold>

        collectionDialog.setView(collectionDialogView)
                .setPositiveButton("Add", (dialogInterface, i) -> {

                    //<editor-fold defaultstate="collapsed" desc="Check details filled in & get data">
                    if (inputNameField.getText().toString().trim().equals("") || inputQtyField.getText().toString().trim().equals("")){
                        Toast.makeText(context, "Fill in all the fields", Toast.LENGTH_LONG).show();
                        return;
                    }

                    final String inputCollector = inputCollectorSpinner.getSelectedItem().toString();
                    final String inputName = inputNameField.getText().toString().trim();
                    final String inputCategory = inputCategorySpinner.getSelectedItem().toString();
                    final String inputRewardType = inputRewardSpinner.getSelectedItem().toString();
                    final int inputRewardQty = Integer.parseInt(inputQtyField.getText().toString().trim());
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="Declare collection object, dialog and view">
                    Collection collection = new Collection(inputName, inputCollector, inputCategory, inputRewardType, inputRewardQty);

                    AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
                    listDialog.setTitle("Select the Artefacts");

                    View listDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_collection_list, null);
                    //</editor-fold>

                    //==========================================================
                    //DECLARATION OF THESE EVENTS AND VARIABLES MUST BE IN ORDER
                    //==========================================================

                    //<editor-fold defaultstate="collapsed" desc="Declare recycler view components">
                    final ArrayList<GameArtefact> dialogDisplayList = new ArrayList<>();

                    final RecyclerView inputRecyclerView = listDialogView.findViewById(R.id.recyclerViewHolderCollectionLogList);
                    inputRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                    RecyclerView.Adapter inputRecyclerViewAdapter = new SelectArtefactAdapter(context, dialogDisplayList);
                    inputRecyclerView.setAdapter(inputRecyclerViewAdapter);
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="Declare search field and function">
                    final EditText inputSearchField = listDialogView.findViewById(R.id.editTextHolderCollectionListSearch);
                    inputSearchField.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshDialogList(); }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { RefreshDialogList(); }
                        @Override
                        public void afterTextChanged(Editable editable) { RefreshDialogList(); }
                        private void RefreshDialogList(){
                            dialogDisplayList.clear();
                            String searchText = inputSearchField.getText().toString().trim();
                            storage.Artefacts().stream().filter(a ->
                                    searchText.length() < 1 || a.title.contains(searchText)
                            ).forEach(dialogDisplayList::add);
                            ((SelectArtefactAdapter)inputRecyclerViewAdapter).selectedPosition = -1;
                            inputRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                    inputSearchField.setText("");
                    //</editor-fold>

                    listDialog.setView(listDialogView)
                            .setPositiveButton("Save", (dialogInterface1, i1) -> {

                                if (((SelectArtefactAdapter)inputRecyclerViewAdapter).selectedData.size() < 1){
                                    Toast.makeText(context, "Need to select some artefacts", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                ((SelectArtefactAdapter)inputRecyclerViewAdapter).selectedData.forEach(a -> {
                                    boolean r = collection.addArtefact(a.title);
                                });

                                storage.AddCollection(collection);
                                if (collectionViewAdapter != null) {
                                    collectionViewAdapter.selectedPosition = -1;
                                    collectionViewAdapter.notifyDataSetChanged();
                                }else{
                                    collectorViewAdapter.selectedPosition = -1;
                                    collectorViewAdapter.notifyDataSetChanged();
                                }
                                Toast.makeText(context, "Added Collection: " + inputName, Toast.LENGTH_LONG).show();
                                searchField.setText("");

                            }).setNegativeButton("Cancel", (dialogInterface1, i1) -> Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()).create().show();

                }).setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()).create().show();

    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Material quantity modification">

    public static Handler HANDLER = new Handler();

    public static boolean AUTO_INCREMENTING = false;
    public static boolean AUTO_DECREMENTING = false;

    private final static int INITIAL_REPEAT_DELAY = 150;
    private static int PRESS_STEP_COUNT = 0;
    public static void resetModifyDelay(){
        PRESS_STEP_COUNT = 0;
    }
    public static int getRepeatDelay(){
        PRESS_STEP_COUNT++;
        return PRESS_STEP_COUNT > 50 ? 0 : PRESS_STEP_COUNT > 25 ? 75 : PRESS_STEP_COUNT > 10 ? 125 : INITIAL_REPEAT_DELAY;
    }

    public static boolean incrementMaterialQuantity(Material material, MaterialAdapter adapter){
        material.quantity++;
        adapter.notifyDataSetChanged();
        return false;
    }

    public static boolean decrementMaterialQuantity(Context context, Material material, MaterialAdapter adapter){
        if (material.quantity < 1){

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Warning");
            dialog.setMessage("You are about to delete the Material entry entirely");

            dialog.setPositiveButton("Continue", (dialogInterface, i) -> {
                storage.DeleteMaterial(material);
                ((MaterialOptionsActivity)context).RefreshList();
            }).setNegativeButton("Cancel", null).create().show();

        }else{
            material.quantity--;
        }

        adapter.notifyDataSetChanged();//RefreshList();
        return false;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="App Data">

    public static boolean USING_LIVE_DATA = true;

    public static void saveDatabaseOption(Context context){
        class SaveDatabaseOption extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                try (FileOutputStream fileOutputStream = context.openFileOutput(context.getResources().getString(R.string.runescape_artefact_tracker_database_option_data),Context.MODE_PRIVATE)){
                    fileOutputStream.write(String.valueOf(USING_LIVE_DATA).getBytes());
                } catch (Exception e) { e.printStackTrace(); }
        return null; }} new SaveDatabaseOption().execute();
    }

    public static boolean loadDatabaseOptions(Context context){
        try {
            FileInputStream fileInputStream = context.openFileInput(context.getResources().getString(R.string.runescape_artefact_tracker_database_option_data));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)){
                String line = reader.readLine();
                if (line != null)
                    return Boolean.valueOf(line.trim());
            } catch (IOException e) { e.printStackTrace(); }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        return false;
    }

    public static void saveAppData(Context context, Storage storage){
        if (!USING_LIVE_DATA) return;
        class SaveAppData extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput(context.getResources().getString(R.string.runescape_artefact_tracker_mobile_app_data),Context.MODE_PRIVATE))){
                    objectOutputStream.writeObject(storage);
                } catch (Exception e) { e.printStackTrace(); }
        return null; }} new SaveAppData().execute();
    }

    public static Storage loadAppData(Context context){
        if (USING_LIVE_DATA) {
            try (ObjectInputStream ois = new ObjectInputStream(context.openFileInput(context.getResources().getString(R.string.runescape_artefact_tracker_mobile_app_data)))) {
                Storage s = (Storage) ois.readObject();
                Toast.makeText(context, "Loaded Saved Data", Toast.LENGTH_SHORT).show();
                return s;
            } catch (Exception e) { e.printStackTrace(); }}//planned to throw exceptions under certain circumstances
        return new Storage(context);
    }

    //</editor-fold>

    public static GameArtefact findGameArtefactByTitle(String title) {
        Optional<GameArtefact> artefact = storage.Artefacts().stream().filter(a -> a.title.equals(title)).findFirst();
        return artefact.orElse(null);
    }
    private static Collection findCollectionByTitle(String title){
        Optional<Collection> collection = storage.Collections().stream().filter(c -> c.title.equals(title)).findFirst();
        return collection.orElse(null);
    }

    //<editor-fold defaultstate="collapsed" desc="Calculate activity display values">
    /**
     * checking that the number of artefacts above 0
     * is equal to the size of the collection
     * @param c
     * @return
     */
    private static boolean collectionCanBeCompleted(Collection c){
        return collectionCanBeCompletedXTimes(c, 0);
    }
    /**
     * checking that the number of artefacts above x
     * is equal to the size of the collection
     * @param collection
     * @param x
     * @return
     */
    private static boolean collectionCanBeCompletedXTimes(Collection collection, int x){
        return collectionCanBeCompletedXTimes(storage, collection, x);
    }
    /**
     * checking that the number of artefacts above x
     * is equal to the size of the collection IN THE DEFINED STORAGE MEDIUM
     * @param s
     * @param c
     * @param x
     * @return
     */
    private static boolean collectionCanBeCompletedXTimes(Storage s, Collection c, int x){
        return s.Artefacts().stream().filter(a -> c.getArtefacts().contains(a.title))
                .mapToInt(a -> a.quantity > x ? 1 : 0).reduce(0, Integer::sum)
                == c.getArtefacts().size();
    }

    private static boolean collectionCanBeCompletedXTimesAndModifyDataMethod
            (Storage s, CombinationItem cmb){
        if (collectionCanBeCompletedXTimes(s, findCollectionByTitle(cmb.collection), cmb.x)) {
            modifyTemporaryDataSet(s, cmb);
            return true;
        }
        return false;
    }

    private static boolean comboListCanBeCompleted(ArrayList<Collection> collections, ArrayList<GameArtefact> artefacts, ArrayList<CombinationItem> comboList){
        Storage copy = new Storage(null);
        copy.SET_ARTEFACTS_USE_WITH_CAUTION(artefacts);
        copy.SET_COLLECTIONS_USE_WITH_CAUTION(collections);
        return comboList.stream().allMatch(cmb ->
                collectionCanBeCompletedXTimesAndModifyDataMethod(copy, cmb));
    }

    private static void modifyTemporaryDataSet(Storage dataSet, CombinationItem combo){
        Collection c = findCollectionByTitle(combo.collection);
        dataSet.Artefacts().stream().filter(a -> c.getArtefacts().contains(a.title))
                .forEach(a -> a.quantity -= combo.x);
    }

    private static CombinationItem getMaximum(ArrayList<Collection> collections, ArrayList<GameArtefact> artefacts, Collection collection){
        Storage copy = new Storage(null);
        copy.SET_ARTEFACTS_USE_WITH_CAUTION(artefacts);
        copy.SET_COLLECTIONS_USE_WITH_CAUTION(collections);

        int x = 0;
        while (collectionCanBeCompletedXTimes(copy, collection, x + 1)) x++;

        return new CombinationItem(collection.title, x);
    }

    private static ArrayList<ArrayList<CombinationItem>> getAllAvailableCombinations() {

        //create deep copies of the original data set
        ArrayList<Collection> collectionsCopy = new ArrayList<>();
        ArrayList<GameArtefact> artefactsCopy = new ArrayList<>();
        storage.Collections().stream().filter(UtilityMethods::collectionCanBeCompleted)
                .forEach(c -> collectionsCopy.add((Collection)c.clone()));
        collectionsCopy.forEach(c -> c.getArtefacts()
                .forEach(a -> artefactsCopy.add((GameArtefact)findGameArtefactByTitle(a).clone())));

        for(int i = 0; i < collectionsCopy.size(); i++){
            ArrayList<CombinationItem> comboList = new ArrayList<>();
            Collection c = collectionsCopy.get(i);

            int x = 0;
            do{
                x++;
                int d = 0;
            }while(comboListCanBeCompleted(collectionsCopy, artefactsCopy, comboList));

        }

        //declare a master list of combinations
        ArrayList<ArrayList<CombinationItem>> masterList = new ArrayList<>();

        //return the master list of combinations that can be completed.
        return masterList;
    }

    /**
     * getting the count of collections that have not been marked as completed once before
     * @return
     */
    public static long getUniqueCollectionRemainingCount(){
        return storage.Collections().stream().filter(c -> !c.isCompleted()).count();
    }
    /**
     * returns 1 for each collection that we have enough artefacts to complete.
     * sums up the count of each collection that we have enough artefacts for.
     * @return
     */
    public static int getUniqueCollectibleCount(){
        return storage.Collections().stream().mapToInt(c -> collectionCanBeCompleted(c) ? 1 : 0).reduce(0, Integer::sum);
    }
    /**
     * summing the quantity owned of each artefact
     * @return
     */
    public static int getOwnedArtefactCountValue(){
        return storage.Artefacts().stream().map(a -> a.quantity).reduce(0, Integer::sum);
    }
    /**
     * count the material qty required for each artefacts multiplied by how many of
     * that artefact is owned
     * @return
     */
    public static int getMaterialRequirementsAsIfArtefactsAllBroken(){
        return storage.Artefacts().stream().mapToInt(                                           //mapping a int value
                artefact -> artefact.getRequirements().stream().mapToInt(                       //streaming all requirements on the artefact
                        materialRequirement -> artefact.quantity * materialRequirement.quantity //mapping matReq = owned artefact quantity * mat req quantity
                ).reduce(0, Integer::sum)
        ).reduce(0, Integer::sum);
    }



    //</editor-fold>

}
