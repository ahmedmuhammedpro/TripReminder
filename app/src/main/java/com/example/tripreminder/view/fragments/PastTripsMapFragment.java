package com.example.tripreminder.view.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.utils.LocationCommunicator;
import com.example.tripreminder.utils.LocationLocator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripsMapFragment extends Fragment implements OnMapReadyCallback {


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

        //longitude and latitude of egypt
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.8205528 , 30.8024979), 6));


        LatLng fromPosition = new LatLng(26.6205528 , 30.6024979); // enter user location lat and long
        LatLng toPosition = new LatLng(26.9205528, 30.9024979);// fixed location

        new AsyncTask<String,Void,String>(){


            @Override
            protected String doInBackground(String... strings) {
                String responseString = "";
                try{
                    responseString = requestDirection(strings[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return  responseString;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //parse json
            }
        };
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString="";
        InputStream inputStream=null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line="";
            while ((line = bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null)
                inputStream.close();;
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    private String getRequestURL(LatLng origin , LatLng destination){
        String str_origin = "origin="+origin.latitude+"," + origin.longitude;
        String str_destination ="destination="+destination.latitude+","+destination.longitude;
        String sensor="sensor=false";
        String mode="mode=driving";
        String param = str_origin+"&"+str_destination+"&" +sensor+ "&"+mode;
        String output="json";
        String url ="https://maps.googleapis.com/api/directions/"+ output +"?"+param;
        return  url;
    }

}
