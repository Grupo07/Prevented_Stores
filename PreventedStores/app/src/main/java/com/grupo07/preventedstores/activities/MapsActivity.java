package com.grupo07.preventedstores.activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.grupo07.preventedstores.R;
import com.grupo07.preventedstores.popupWindows.FilterStore;
import com.grupo07.preventedstores.popupWindows.ShowStoreWindow;
import com.grupo07.preventedstores.objects.Store;
import com.grupo07.preventedstores.popupWindows.StoreFormWindow;
import com.grupo07.preventedstores.database.StoreDatabase;

import java.util.ArrayList;


/**
 * Map window that shows stores as markers.
 * It allows to create, update or delete stores
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    // display map
    private GoogleMap map;

    // last update stores
    private ArrayList<Store> stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        configureMap();

        StoreDatabase.getStores(this);

    }

    /**
     * Sets up the map look and event listeners.
     * Marker click listener and map long click listener are set up
     */
    private void configureMap() {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng location = marker.getPosition();
                Store store = findStore(location.latitude, location.longitude);
                showStoreInfo(store);

                return false;
            }
        });
    }

    /**
     * Pops up a store information window
     * @param store store whose information is displayed in the window
     */
    private void showStoreInfo(Store store) {
        ShowStoreWindow showStoreDialog = new ShowStoreWindow(this);
        showStoreDialog.setStore(store);
        showStoreDialog.show(getSupportFragmentManager(), "");
    }

    public void showFilterPopup(View view){
        FilterStore filterStore = new FilterStore(this,stores);
        filterStore.show(getSupportFragmentManager(),"");
    }

    /**
     * Pops up the create store window with a given location
     * @param latLng store location
     */
    private void createStoreLocation(LatLng latLng) {
        StoreFormWindow dialog = new StoreFormWindow(this);
        dialog.setLocation(latLng);
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        createStoreLocation(latLng);
    }

    /**
     * Calls the database to update the current stores
     */
    public void updateStores() {
        StoreDatabase.getStores(this);
    }

    /**
     * Updates and displays the saved list of stores
     * @param stores updated list of stores
     */
    public void loadStoresMarkers(ArrayList<Store> stores) {
        this.stores = stores;
        map.clear();
        for(Store store : stores) {
            LatLng location = new LatLng(store.getLatitude(), store.getLongitude());
            float hueColor = getStoreHueColor(store);
            addMapMarker(location, hueColor);
        }
    }

    /**
     * Generates a store marker color based on the sanitary options fulfilled
     * @param store store to get the color
     * @return color as a hue value
     */
    private float getStoreHueColor(Store store) {
        String sanitaryOptions = store.getSanitaryOptions();
        int totalOptions = sanitaryOptions.length();

        int fulfilledOptions = 0;
        for (int i = 0; i < totalOptions; i++)
            if (sanitaryOptions.charAt(i) == '1')
                fulfilledOptions += 1;

        int rating = (int) (((float) fulfilledOptions / totalOptions) * 100);

        float storeColor;
        if (rating == 100) {
            storeColor = 115;
        } else if (rating > 40) {
            storeColor = 53f;
        } else if (rating > 0) {
            storeColor = 35f;
        } else {
            storeColor = 15f;
        }

        return storeColor;
    }

    /**
     * Adds a marker to the map in the given location
     * @param latLng marker location
     * @param hueColor marker hue color
     */
    private void addMapMarker(LatLng latLng, float hueColor) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(hueColor)));
    }

    /**
     * Finds a store in the store list by its location
     * @param latitude store latitude
     * @param longitude store longitude
     * @return the found store
     */
    private Store findStore(double latitude, double longitude) {
        for(Store store : stores)
            if(store.getLatitude() == latitude && store.getLongitude() == longitude)
                return store;
        return null;
    }

    /**
     * Centers the map on a given location
     * @param latitude location latitude
     * @param longitude location longitude
     */
    public void centerMapOnLocation(Double latitude, Double longitude) {
        LatLng userLocation = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
    }

    /**
     * Logs out the current user and goes to the login activity
     * @param view view widget that calls the method
     */
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

}
