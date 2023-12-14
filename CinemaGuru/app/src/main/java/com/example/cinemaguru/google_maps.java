package com.example.cinemaguru;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;


public class google_maps extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps); // XML file.

        mapView = findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
    }

    @Override //Reference: https://www.geeksforgeeks.org/how-to-add-multiple-markers-on-google-maps-in-android/
    public void onMapReady(GoogleMap googleMap) {
        // Add marker for a nearby store (sample location)
        LatLng storeLocation = new LatLng(53.345040, -6.412020);
        googleMap.addMarker(new MarkerOptions().position(storeLocation).title("Vue"));
        LatLng storeLocation2 = new LatLng(53.348510, -6.278880);
        googleMap.addMarker(new MarkerOptions().position(storeLocation2).title("The Lighthouse"));
        LatLng storeLocation3 = new LatLng(53.289170, -6.206130);
        googleMap.addMarker(new MarkerOptions().position(storeLocation3).title("Odeon"));
        LatLng storeLocation4 = new LatLng(53.3265199, -6.2648571);
        googleMap.addMarker(new MarkerOptions().position(storeLocation4).title("Stella")); // Default markers(4)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 12)); // Default level of zoom when user clicks into function.
    }
    // To help the smoothness of the map running and to actually have it show up.
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    } //End of reference
}
