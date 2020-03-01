package com.example.tripreminder.view.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.tripreminder.R;
import com.example.tripreminder.model.map_directions.FetchURL;
import com.example.tripreminder.utils.LocationLocator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripsMapFragment extends Fragment implements OnMapReadyCallback {

    MarkerOptions place1,place2;
    Polyline currentPolyline;


    public GoogleMap googleMap;
    int PERMISSION_ID = 44;
    public PastTripsMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_trips_map, container, false);

    }

    private void loadMap(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                LocationLocator.getInstance(getActivity()).getLastLocation();

            }
            else {
                Toast.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
        else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap=googleMap;
        //longitude and latitude of egypt
       // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.8205528 , 30.8024979), 6));


        //LatLng fromPosition = new LatLng(26.6205528 , 30.6024979); // enter user location lat and long
        //LatLng toPosition = new LatLng(26.9205528, 30.9024979);// fixed location

        LatLng fromPosition = new LatLng(30.063550, 31.027708);
        LatLng toPosition = new LatLng(30.071296, 31.020731);


        LatLng fromPosition2 = new LatLng(30.060737, 31.031110);
        LatLng toPosition2 = new LatLng(30.069327, 31.030263);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.063550, 31.027708), 6));
        place1 = new MarkerOptions().position(fromPosition).title("location 1");
        place2 = new MarkerOptions().position(toPosition).title("location 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.addMarker(place1);
        googleMap.addMarker(place2);

        place1 = new MarkerOptions().position(fromPosition2).title("location 1");
        place2 = new MarkerOptions().position(toPosition2).title("location 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        googleMap.addMarker(place1);
        googleMap.addMarker(place2);

        String url = getUrl(fromPosition,toPosition,"driving");
        new FetchURL(getContext()).execute(url,"driving");

        String url2 = getUrl(fromPosition2,toPosition2,"driving");
        new FetchURL(getContext()).execute(url2,"driving");
    }



    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        String api_key="AIzaSyBxfnMwiZig4b4yJqDLkT7IPtN-ZhRzFlo";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + api_key;
        return url;
    }

}
