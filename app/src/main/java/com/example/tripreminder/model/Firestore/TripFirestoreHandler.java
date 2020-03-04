package com.example.tripreminder.model.Firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.SharedPreferencesHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class TripFirestoreHandler {

    private final String TRIP_NAME_KEY="tripName",USER_ID_KEY="userId",DATE_KEY="tripDate",START_LOCATION="startLocation";
    private final String END_LOCATION = "endLocation",TRIP_STATUS="tripStatus",TRIP_TYPE="tripType";
    private FirebaseFirestore dbFirestoreInstance = FirebaseFirestore.getInstance();

    MutableLiveData<HashMap<String, Object>> userInfoLiveData = SharedPreferencesHandler.getInstance().getUserInfoLiveData();


    private static TripFirestoreHandler tripFirestoreHandler =null;
    private TripFirestoreHandler(){
    }

    public static TripFirestoreHandler getInstance(){
        if(tripFirestoreHandler == null)
            tripFirestoreHandler = new TripFirestoreHandler();
        return tripFirestoreHandler;
    }

    public MutableLiveData<Trip> addTrip(Trip trip){

        HashMap<String,Object> tripValues = new HashMap<>();
        tripValues.put(TRIP_NAME_KEY,trip.getTripName());
        tripValues.put(USER_ID_KEY,String.valueOf(userInfoLiveData.getValue().get(Constants.USER_ID_TAG)));
        tripValues.put(DATE_KEY,trip.getTripDate());
        tripValues.put(START_LOCATION,trip.getStartLocation());
        tripValues.put(END_LOCATION,trip.getEndLocation());
        tripValues.put(TRIP_STATUS,trip.getTripStatus());
        tripValues.put(TRIP_TYPE,trip.getTripType());

        MutableLiveData<Trip> tripAdded = new MutableLiveData<>();

        dbFirestoreInstance.collection("trips").add(tripValues).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){

                    String documentID = task.getResult().getId();
                    trip.setTripId(documentID);
                    int counter=1;
                    Vector<String> notes = trip.getNotes();
                    Iterator iterator = notes.iterator();
                    HashMap<String,Object> noteValues = new HashMap<>();
                    while(iterator.hasNext()){
                        noteValues.put(""+counter,String.valueOf(iterator.next()));
                        counter++;
                    }

                    dbFirestoreInstance.collection("notes").document(documentID).set(noteValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            tripAdded.postValue(trip);
                            Log.d("test", "notes added ");
                        }
                    });
                }
            }
        });

        return tripAdded;
    }

    public LiveData<List<Trip>> getUserTrips(String userId) {

        MutableLiveData<List<Trip>> tripsListLiveData = new MutableLiveData<>();

        dbFirestoreInstance.collection("trips").whereEqualTo("userId",userId).whereEqualTo("tripStatus",Trip.UPCOMING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Trip>tripsList = new ArrayList<>();
                if(task.isComplete()){
                    QuerySnapshot querySnapshot = task.getResult();;
                    for (QueryDocumentSnapshot document : querySnapshot) {

                       Trip trip = prepareTripObjectData(document);
                       tripsList.add(trip);

                    }
                    tripsListLiveData.postValue(tripsList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return tripsListLiveData;
    }

    public MutableLiveData<Trip> deleteTrip(String tripID) {
        MutableLiveData<Trip> deleteTripLiveData = new MutableLiveData<>();

        dbFirestoreInstance.collection("trips").document(tripID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {

                    dbFirestoreInstance.collection("notes").document(tripID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Trip trip = new Trip();
                            trip.setTripId(tripID);
                            deleteTripLiveData.postValue(trip);
                        }
                    });

                }
                else
                    Log.d("Error", "error in deleting");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("test", "error in deleting");
            }
        });
        return deleteTripLiveData;
    }

    public MutableLiveData<Trip> updateTrip(Trip trip) {
        MutableLiveData<Trip> updatedTripData = new MutableLiveData<>();

        HashMap<String,Object> tripValues = new HashMap<>();
        tripValues.put(TRIP_NAME_KEY,trip.getTripName());
        tripValues.put(DATE_KEY,trip.getTripDate());
        tripValues.put(START_LOCATION,trip.getStartLocation());
        tripValues.put(END_LOCATION,trip.getEndLocation());
        tripValues.put(TRIP_STATUS,trip.getTripStatus());
        tripValues.put(TRIP_TYPE,trip.getTripType());
        tripValues.put(USER_ID_KEY,String.valueOf(userInfoLiveData.getValue().get(Constants.USER_ID_TAG)));



        dbFirestoreInstance.collection("trips").document(trip.getTripId()).set(tripValues).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    String documentID = trip.getTripId();
                    int counter=1;
                    Vector<String> notes = trip.getNotes();
                    HashMap<String, Object> noteValues = new HashMap<>();
                    if(notes!=null) {
                        Iterator iterator = notes.iterator();

                        while (iterator.hasNext()) {
                            noteValues.put("" + counter, String.valueOf(iterator.next()));
                            counter++;
                        }
                    }

                    dbFirestoreInstance.collection("notes").document(documentID).set(noteValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updatedTripData.postValue(trip);
                            Log.d("test", "notes updated");
                        }
                    });
                }
            }
        });

        return updatedTripData;
    }

    public MutableLiveData<Vector<String>> getTripNotes(String tripId){

        MutableLiveData<Vector<String>> notesMutableLiveData = new MutableLiveData<>();

        Vector<String> notesVector= new Vector<>();
        dbFirestoreInstance.collection("notes").document(tripId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    HashMap notesHashMap = (HashMap) task.getResult().getData();
                    if(notesHashMap!= null && !notesHashMap.isEmpty()){
                        for(int i=0;i<notesHashMap.size();i++){
                            notesVector.add((String) notesHashMap.get(""+(i + 1)));
                        }
                        notesMutableLiveData.postValue(notesVector);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                notesMutableLiveData.postValue(null);
                Log.d("Error", "error in getting notes :"+e.getMessage());
            }
        });


        return notesMutableLiveData;


    }
    public MutableLiveData<Trip> updateTripStatus(String tripId,int tripStatus){

        MutableLiveData<Trip> updatedTripData = new MutableLiveData<>();

        Trip trip = new Trip();

        dbFirestoreInstance.collection("trips").document(tripId).update(TRIP_STATUS,tripStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    trip.setTripId(tripId);
                    updatedTripData.postValue(trip);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                trip.setTripId("-1");
                //return error message in
                trip.setTripName(e.getMessage());
                updatedTripData.postValue(trip);
                Log.d("Error", "error in update status :"+e.getMessage());
            }
        });


        return  updatedTripData;
    }
    public LiveData<List<Trip>> getPastTrips(String userId) {

        MutableLiveData<List<Trip>> tripsListLiveData = new MutableLiveData<>();

        dbFirestoreInstance.collection("trips").whereEqualTo("userId",userId).whereIn("tripStatus", Arrays.asList(Trip.DONE, Trip.CANCELED)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Trip>tripsList = new ArrayList<>();
                if(task.isComplete()){
                    QuerySnapshot querySnapshot = task.getResult();;
                    for (QueryDocumentSnapshot document : querySnapshot) {

                        Trip trip = prepareTripObjectData(document);

                        tripsList.add(trip);
                    }
                    tripsListLiveData.postValue(tripsList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return tripsListLiveData;
    }

    private Trip prepareTripObjectData(QueryDocumentSnapshot document){

        String tripId = document.getId();
        String tripName = String.valueOf(document.getData().get("tripName"));
        Trip trip = new Trip();
        trip.setTripName(tripName);
        trip.setTripId(tripId);
        trip.setTripDate((String)document.getData().get("tripDate"));

        HashMap location = (HashMap) document.getData().get("startLocation");
        Double latitude = (Double) location.get("latitude");
        Double longitude = (Double) location.get("longitude");
        String locationName = (String) location.get("locationName");

        int tripStatus = Integer.parseInt(String.valueOf(document.getData().get("tripStatus")));
        int tripType =  Integer.parseInt(String.valueOf(document.getData().get("tripType")));
        trip.setTripType(tripType);
        trip.setTripStatus(tripStatus);

        trip.setStartLocation(new TripLocation(latitude,longitude,locationName ));

        HashMap endLocation= (HashMap) document.getData().get("endLocation");
        Double endLatitude = (Double) endLocation.get("latitude");
        Double endLongitude = (Double) endLocation.get("longitude");
        String endLocationName = (String) endLocation.get("locationName");

        trip.setEndLocation(new TripLocation(endLatitude,endLongitude,endLocationName ));

        return trip;

    }


}
