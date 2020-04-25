package com.example.artefacttrackerapp.utilities;

import android.content.Context;
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
import com.example.artefacttrackerapp.data.Collection;
import com.example.artefacttrackerapp.data.Collector;
import com.example.artefacttrackerapp.data.GameArtefact;

import java.util.ArrayList;

import static com.example.artefacttrackerapp.activities.MainActivity.storage;

public class UtilityMethods {

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

                                ((SelectArtefactAdapter)inputRecyclerViewAdapter).selectedData.forEach(a -> collection.artefacts.add(a.title));

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

}
