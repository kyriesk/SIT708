package com.example.lostandfound.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationManager {
    private static final String TAG = "LocationManager";
    private final Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Geocoder geocoder;

    public LocationManager(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    /**
     * Get the last known location of the device
     */
    public Task<Location> getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permission not granted");
            return null;
        }

        return fusedLocationProviderClient.getLastLocation();
    }

    /**
     * Get coordinates from an address string using Geocoder
     */
    public double[] getCoordinatesFromAddress(String addressString) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new double[]{address.getLatitude(), address.getLongitude()};
            }
        } catch (IOException e) {
            Log.e(TAG, "Error geocoding address: " + addressString, e);
        }
        return null;
    }

    /**
     * Get address from coordinates using Geocoder
     */
    public String getAddressFromCoordinates(double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                if (address.getThoroughfare() != null) {
                    sb.append(address.getThoroughfare()).append(", ");
                }
                if (address.getLocality() != null) {
                    sb.append(address.getLocality());
                }
                return sb.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reverse geocoding coordinates", e);
        }
        return null;
    }

    /**
     * Calculate distance between two coordinates in kilometers
     */
    public static double calculateDistanceInKm(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] / 1000.0; // Convert meters to km
    }

    /**
     * Check if Geocoder is present on the device
     */
    public boolean isGeocodePresent() {
        return Geocoder.isPresent();
    }
}

