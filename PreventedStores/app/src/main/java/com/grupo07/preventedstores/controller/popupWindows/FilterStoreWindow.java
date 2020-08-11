package com.grupo07.preventedstores.controller.popupWindows;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupo07.preventedstores.R;
import com.grupo07.preventedstores.view.activities.MapsActivity;
import com.grupo07.preventedstores.model.filterStrategy.Filter;
import com.grupo07.preventedstores.model.filterStrategy.FilterByCategory;
import com.grupo07.preventedstores.model.filterStrategy.FilterByName;
import com.grupo07.preventedstores.model.database.Store;

import java.util.ArrayList;

/**
 * FilterStore is a Search fragment to filter by name or category
 */
public class FilterStoreWindow extends AppCompatDialogFragment {

    private final AppCompatDialogFragment dialog = this;
    private MapsActivity mapsActivity;
    private ArrayList<Store> stores;
    private ArrayList<Store> filteredStores;
    private ListView storesListView;
    private final String[] categories = {"all","grocery", "food", "shop", "services"};
    private Spinner categoryDropdown;
    private String category= "all";
    private EditText name;
    private Button searchButton;
    private Button sortName;
    private Button sortCategory;

    public FilterStoreWindow() {

    }

    /**
     * Constructor
     * @param mapsActivity context for toasts and show stores
     * @param stores list of stores to filter and search
     */
    public FilterStoreWindow(MapsActivity mapsActivity, ArrayList<Store> stores) {
        this.mapsActivity = mapsActivity;
        this.stores = stores;
        filteredStores = stores;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_store, container, false);
        storesListView = (ListView) v.findViewById(R.id.StoresListView);
        setAdapter();
        storesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                Store clickedStore = getStore(parent.getItemAtPosition(position).toString());
                mapsActivity.centerMapOnLocation(clickedStore.getLatitude(), clickedStore.getLongitude());
                ShowStoreWindow showStoreDialog = new ShowStoreWindow(mapsActivity);
                showStoreDialog.setStore(clickedStore);
                showStoreDialog.show(mapsActivity.getSupportFragmentManager(), "");
            }
        });
        categoryDropdown = (Spinner) v.findViewById(R.id.CategorySpinner);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(mapsActivity, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.categoryDropdown.setAdapter(categoriesAdapter);
        this.categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);

                if(!category.contains("all")){
                    Filter filter = new FilterByCategory();
                    filteredStores = filter.filter(stores,category);
                    checkEmpy();
                }else{
                    filteredStores = stores;
                }
                setAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        name = (EditText) v.findViewById(R.id.nameET);
        searchButton = (Button) v.findViewById(R.id.SearchB);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter filter = new FilterByName();
                filteredStores = filter.filter(filteredStores,name.getText().toString());
                checkEmpy();
                setAdapter();
            }
        });

        sortName = (Button) v.findViewById(R.id.nameSB);
        sortName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter filter = new FilterByName();
                filteredStores = filter.sort(filteredStores);
                setAdapter();
            }
        });
        sortCategory = (Button) v.findViewById(R.id.categorySB);
        sortCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter filter = new FilterByCategory();
                filteredStores = filter.sort(filteredStores);
                setAdapter();
            }
        });
        return v;
    }

    /**
     * Update ListView
     */
    private void setAdapter(){
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(mapsActivity,android.R.layout.simple_list_item_1,getNameList());
        storesListView.setAdapter(namesAdapter);
    }

    /**
     * Search store by selected name in ListView
     * @param name name of store
     * @return
     */
    private Store getStore(String name) {
        for(Store store:filteredStores){
            if(store.getName().equalsIgnoreCase(name)){
                return store;
            }
        }
        return null;
    }

    /**
     * Get names of stores
     * @return Arraylist of names of stores
     */
    private ArrayList<String> getNameList() {
        ArrayList<String> result = new ArrayList<String>();
        if(filteredStores.size()<1){
            filteredStores = stores;
        }
        for(Store store:filteredStores){
            result.add(store.getName());
        }
        return result;
    }

    /**
     * if filtered stores list is empy, make a Toast and reset filtered stores
     */
    private void checkEmpy(){
        if(filteredStores.size() < 1){
            filteredStores = stores;
            Toast toast = Toast.makeText(getContext(), "0 matches", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}