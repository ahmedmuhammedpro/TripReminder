package com.example.tripreminder.model.Firestore;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TripFirestoreHandler {

    private final String TRIP_NAME_KEY="tripName",USER_ID_KEY="userId",DATE_KEY="tripDate",START_LOCATION="startLocation";
    private final String END_LOCATION = "endLocation",TRIP_STATUS="tripStatus";
    private FirebaseFirestore dbFirestoreInstance = FirebaseFirestore.getInstance();

    public MutableLiveData<Trip> addTrip(Trip trip){

        HashMap<String,Object> tripValues = new HashMap<>();
        tripValues.put(TRIP_NAME_KEY,trip.getTripName());
        tripValues.put(USER_ID_KEY,trip.getUserID());
        tripValues.put(DATE_KEY,trip.getTripDate());
        tripValues.put(START_LOCATION,trip.getStartLocation());
        tripValues.put(END_LOCATION,trip.getEndLocation());
        tripValues.put(TRIP_STATUS,trip.getTripStatus());
        MutableLiveData<Trip> tripAdded = new MutableLiveData<>();

        dbFirestoreInstance.collection("trips").add(tripValues).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){

                    String documentID = task.getResult().getId();
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

        dbFirestoreInstance.collection("trips").whereEqualTo("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Trip>tripsList = new ArrayList<>();
                if(task.isComplete()){
                    QuerySnapshot querySnapshot = task.getResult();;
                    for (QueryDocumentSnapshot document : querySnapshot) {
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

                        trip.setStartLocation(new TripLocation(latitude,longitude,locationName ));

                        HashMap endLocation= (HashMap) document.getData().get("endLocation");
                        Double endLatitude = (Double) endLocation.get("latitude");
                        Double endLongitude = (Double) endLocation.get("longitude");
                        String endLocationName = (String) endLocation.get("locationName");

                        trip.setEndLocation(new TripLocation(endLatitude,endLongitude,endLocationName ));


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
        tripValues.put(USER_ID_KEY,trip.getUserID());
        tripValues.put(DATE_KEY,trip.getTripDate());
        tripValues.put(START_LOCATION,trip.getStartLocation());
        tripValues.put(END_LOCATION,trip.getEndLocation());

        dbFirestoreInstance.collection("trips").document(trip.getTripId()).set(tripValues).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    String documentID = trip.getTripId();
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
}
