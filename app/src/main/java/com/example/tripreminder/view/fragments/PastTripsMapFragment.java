package com.example.tripreminder.view.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.map_directions.FetchURL;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.SharedPreferencesHandler;
import com.example.tripreminder.viewmodel.PastTripsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripsMapFragment extends Fragment implements OnMapReadyCallback {

    MarkerOptions place1,place2;
    Polyline currentPolyline;
    private PastTripsViewModel pastTripsViewModel;

    View view ;

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
        view = inflater.inflate(R.layout.fragment_past_trips_map, container, false);
        pastTripsViewModel = ViewModelProviders.of(this).get(PastTripsViewModel.class);

        setup();
        return view;
    }


    private void setup(){

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap=googleMap;


        MutableLiveData<HashMap<String, Object>> userInfoLiveData = SharedPreferencesHandler.getInstance().getUserInfoLiveData();

        pastTripsViewModel.getPastTrips(String.valueOf(userInfoLiveData.getValue().get(Constants.USER_ID_TAG))).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {

                for(int i=0;i<trips.size();i++){
                    Trip trip = trips.get(i);
                    if(trip.getTripStatus() == trip.DONE) {
                        LatLng fromPosition = new LatLng(trip.getStartLocation().getLatitude(), trip.getStartLocation().getLongitude());
                        LatLng toPosition = new LatLng(trip.getEndLocation().getLatitude(), trip.getEndLocation().getLongitude());

                        place1 = new MarkerOptions().position(fromPosition).title(trip.getStartLocation().getLocationName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        place2 = new MarkerOptions().position(toPosition).title(trip.getEndLocation().getLocationName());
                        googleMap.addMarker(place1);
                        googleMap.addMarker(place2);

                        String url = getUrl(fromPosition, toPosition, "driving");
                        new FetchURL(getContext()).execute(url, "driving");
                    }

                }


                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.063550, 31.027708), 10));


            }
        });
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
