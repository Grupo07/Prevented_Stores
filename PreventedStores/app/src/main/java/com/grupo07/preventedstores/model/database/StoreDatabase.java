package com.grupo07.preventedstores.model.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo07.preventedstores.view.activities.MapsActivity;

import java.util.ArrayList;
import androidx.annotation.NonNull;

/**
 * It allows to manipulate the store database, offering crud functionality
 */
public class StoreDatabase {

    /**
     * Saves / updates a new store to the database. Since the store id depends only on it's location
     * and the location can't be changed without deleting the store first, this function works
     * also to override (update) a store
     * @param store store to be saved
     */
    public static void saveStore(Store store) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("stores/" + generateStoreId(store));
        database.setValue(store);
    }

    /**
     * Gets all the stores from the databse and updates them in the given activity
     * @param activity activity to update the stores
     */
    public static void getStores(final MapsActivity activity) {
        StoreDatabaseConnection connection = StoreDatabaseConnection.getInstance();
        DatabaseReference databaseReference = connection.getReference("stores");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataGroup) {
                ArrayList<Store> stores = new ArrayList<>();
                for(DataSnapshot data : dataGroup.getChildren()) {
                    Store store = data.getValue(Store.class);
                    stores.add(store);
                }
                activity.loadStoresMarkers(stores);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    /**
     * Deletes a given store from the database and updates the given activity
     * @param store store to delete
     * @param activity activity to update the stores after the deletion
     */
    public static void deleteStore(Store store, final MapsActivity activity) {
        StoreDatabaseConnection connection = StoreDatabaseConnection.getInstance();
        DatabaseReference databaseReference = connection.getReference("stores/" + generateStoreId(store));
        databaseReference.removeValue();
        getStores(activity);
    }

    /**
     * Creates a unique id given a store
     * @param store store to create the id
     * @return store id
     */
    private static String generateStoreId(Store store) {
        return (store.getLatitude().toString() + "+" + store.getLongitude().toString()).replace('.', '_');
    }
}
