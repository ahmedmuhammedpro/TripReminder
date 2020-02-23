package com.example.tripreminder.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationLocator {

    private WeakReference<Activity> activityContext;
    private static LocationLocator locationLocatorObject = null;
    private WeakReference<LocationCommunicator> activityObject=null;
    FusedLocationProviderClient mFusedLocationClient;
    String longitude="",latitude="";

    private LocationLocator(Activity activity){
        activityContext = new WeakReference<>(activity);
        activityObject = new WeakReference<>((LocationCommunicator) activity);
    }

    public static LocationLocator getInstance(Activity activityContext){
        if(locationLocatorObject == null)
            locationLocatorObject = new LocationLocator(activityContext);
        return locationLocatorObject;
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        String[] result = new String[2];//longitude and latitude
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activityContext.get());
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude() + "";
                            longitude = location.getLongitude() + "";
                            String locationInfo= getLocationInfo();
                            activityObject.get().onLocationReceivedAction(Double.parseDouble(longitude),Double.parseDouble(latitude),locationInfo);
                        }
                    }
                }
        );
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activityContext.get());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude()+"";
            longitude = mLastLocation.getLongitude()+"";
            String locationInfo= getLocationInfo();
            activityObject.get().onLocationReceivedAction(Double.parseDouble(longitude),Double.parseDouble(latitude),locationInfo);
        }
    };


    public String getLocationInfo() {
        String locationInfo="";
        Geocoder geocoder = new Geocoder(activityContext.get(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude),
                    1);
        } catch (Exception ioException) {

            if (addresses == null || addresses.size() == 0) {
                return "not found";
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                String cityName = addresses.get(0).getAdminArea();
                String street = addresses.get(0).getSubAdminArea();
                String building = addresses.get(0).getPremises();
                locationInfo = cityName+" , "+street+" , "+building;
            }
        }
        return locationInfo;
    }
}
