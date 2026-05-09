package com.example.lostandfound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lostandfound.model.Item;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.util.LocationManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private GoogleMap googleMap;
    private ItemRepository itemRepository;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double userLatitude = 0;
    private double userLongitude = 0;
    private double radiusInKm = 50; // Default 50km search radius
    private boolean mapInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        Log.d(TAG, "MapActivity onCreate");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get radius from intent if provided
        if (getIntent().hasExtra("radius")) {
            radiusInKm = getIntent().getDoubleExtra("radius", 50);
            Log.d(TAG, "Radius: " + radiusInKm + "km");
        }

        itemRepository = new ItemRepository(this);
        locationManager = new LocationManager(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            Log.d(TAG, "Map fragment found, getting map async");
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "Map fragment not found!");
            Toast.makeText(this, "Error loading map", Toast.LENGTH_SHORT).show();
        }

        // Check and request permissions
        if (!hasLocationPermission()) {
            Log.d(TAG, "Requesting location permission");
            requestLocationPermission();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        Log.d(TAG, "onMapReady called");
        googleMap = map;
        mapInitialized = true;

        // Request location updates
        getCurrentLocationAndLoadItems();
    }

    private void getCurrentLocationAndLoadItems() {
        if (!hasLocationPermission()) {
            Log.d(TAG, "No location permission, requesting");
            requestLocationPermission();
            return;
        }

        Log.d(TAG, "Getting current location");

        try {
            // Create location request for high accuracy
            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setMinUpdateDistanceMeters(0)
                    .setMaxUpdateDelayMillis(10000)
                    .build();

            // Get last known location first
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            Log.d(TAG, "Got location: " + location.getLatitude() + ", " + location.getLongitude());
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();
                            displayMapWithItems();
                        } else {
                            Log.w(TAG, "Last location is null, requesting location updates");
                            Toast.makeText(this, "Getting current location...", Toast.LENGTH_SHORT).show();

                            // If last location is null, request location updates
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                    updatedLocation -> {
                                        if (updatedLocation != null) {
                                            userLatitude = updatedLocation.getLatitude();
                                            userLongitude = updatedLocation.getLongitude();
                                            Log.d(TAG, "Got location from update: " + userLatitude + ", " + userLongitude);
                                            displayMapWithItems();
                                        }
                                    }, getMainLooper());
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        Log.e(TAG, "Failed to get location", e);
                        Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        } catch (SecurityException e) {
            Log.e(TAG, "Security exception: ", e);
            Toast.makeText(this, "Location permission error", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayMapWithItems() {
        if (!mapInitialized || googleMap == null) {
            Log.e(TAG, "Map not initialized yet");
            return;
        }

        Log.d(TAG, "Displaying map with items");

        LatLng userLocation = new LatLng(userLatitude, userLongitude);

        // Move camera to user location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));

        // Add marker for user location
        googleMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Draw search radius circle
        googleMap.addCircle(new CircleOptions()
                .center(userLocation)
                .radius(radiusInKm * 1000) // Convert km to meters
                .strokeColor(0xFF0000FF)
                .fillColor(0x220000FF)
                .strokeWidth(2));

        // Load and display items within radius
        loadItemsOnMap();
    }

    private void loadItemsOnMap() {
        Log.d(TAG, "Loading items on map");

        try {
            List<Item> items = itemRepository.searchItemsByRadius(userLatitude, userLongitude, radiusInKm);
            Log.d(TAG, "Found " + items.size() + " items");

            int markerCount = 0;
            for (Item item : items) {
                if (item.getLatitude() != 0 && item.getLongitude() != 0) {
                    LatLng itemLocation = new LatLng(item.getLatitude(), item.getLongitude());

                    // Choose marker color based on status
                    float markerColor = "LOST".equals(item.getStatus())
                            ? BitmapDescriptorFactory.HUE_RED
                            : BitmapDescriptorFactory.HUE_GREEN;

                    googleMap.addMarker(new MarkerOptions()
                            .position(itemLocation)
                            .title(item.getTitle())
                            .snippet(item.getStatus() + " - " + item.getLocation())
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));

                    markerCount++;
                    Log.d(TAG, "Added marker for item: " + item.getTitle());
                }
            }

            String message = "Found " + markerCount + " items within " + radiusInKm + "km";
            Log.d(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Error loading items on map", e);
            e.printStackTrace();
            Toast.makeText(this, "Error loading items: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        if (!hasLocationPermission()) {
            Log.d(TAG, "Requesting location permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Location permission granted");
                if (mapInitialized) {
                    getCurrentLocationAndLoadItems();
                }
            } else {
                Log.w(TAG, "Location permission denied");
                Toast.makeText(this, "Location permission denied. Cannot show map.", Toast.LENGTH_SHORT).show();
                finish(); // Close map activity if permission denied
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (itemRepository != null) {
            itemRepository.close();
        }
    }
}
