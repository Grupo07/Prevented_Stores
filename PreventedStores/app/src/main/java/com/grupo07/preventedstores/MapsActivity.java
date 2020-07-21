package com.grupo07.preventedstores;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap map;
    private ShowStoreWindow showStoreDialog;
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

    private void showStoreInfo(Store store) {
        showStoreDialog =  new ShowStoreWindow(this);
        showStoreDialog.setStore(store);
        showStoreDialog.show(getSupportFragmentManager(), "");
    }

    private void createStoreLocation(LatLng latLng) {
        StoreFormWindow dialog = new StoreFormWindow(this);
        dialog.setLocation(latLng);
        dialog.show(getSupportFragmentManager(), "");

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        createStoreLocation(latLng);
    }

    public void updateStores() {
        StoreDatabase.getStores(this);
    }

    public void loadStoresMarkers(ArrayList<Store> stores) {
        this.stores = stores;
        map.clear();
        for(Store store : stores)
            addMapMarker(new LatLng(store.getLatitude(), store.getLongitude()));
    }

    private void addMapMarker(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng));
    }

    private Store findStore(double latitude, double longitude) {
        for(Store store : stores)
            if(store.getLatitude() == latitude && store.getLongitude() == longitude)
                return store;
        return null;
    }

    public void centerMapOnLocation(Double latitude, Double longitude) {
        LatLng userLocation = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
    }

}
