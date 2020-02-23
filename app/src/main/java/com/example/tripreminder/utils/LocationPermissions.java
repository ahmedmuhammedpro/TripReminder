package com.example.tripreminder.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

public class LocationPermissions {


    private static LocationPermissions locationPermissionsObject = null;
    private WeakReference<Activity> context;
    int PERMISSION_ID = 44;
    private LocationPermissions(){
    }

    public static LocationPermissions getInstance(Activity context){
        if(locationPermissionsObject == null)
            locationPermissionsObject = new LocationPermissions();
        locationPermissionsObject.context = new WeakReference<>(context);
        return locationPermissionsObject;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(
                context.get(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager)   context.get().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


}
