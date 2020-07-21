package com.grupo07.preventedstores;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class StoreDatabase {

    public static void saveStore(Store store) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("stores/" + generateStoreId(store));
        database.setValue(store);
    }

    public static void getStores(final MapsActivity activity) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("stores");

        database.addValueEventListener(new ValueEventListener() {
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

    public static void deleteStore(Store store, final MapsActivity activity) {
        FirebaseDatabase.getInstance().getReference("stores/" + generateStoreId(store)).removeValue();
        getStores(activity);
    }

    private static String generateStoreId(Store store) {
        return (store.getLatitude().toString() + "+" + store.getLongitude().toString()).replace('.', '_');
    }
}
