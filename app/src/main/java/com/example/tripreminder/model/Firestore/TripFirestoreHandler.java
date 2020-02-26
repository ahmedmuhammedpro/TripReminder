package com.example.tripreminder.model.Firestore;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class TripFirestoreHandler {

    private final String TRIP_NAME_KEY="tripName",USER_ID_KEY="userId",DATE_KEY="tripDate",START_LOCATION="startLocation";
    private final String END_LOCATION = "endLocation",LONGITUDE="longitude",LATITUDE="latitude",LOCATION_NAME="locationName";
    private FirebaseFirestore dbFirestoreInstance = FirebaseFirestore.getInstance();

    public MutableLiveData<Trip> addTrip(Trip trip){

        HashMap<String,Object> tripValues = new HashMap<>();
        tripValues.put(TRIP_NAME_KEY,trip.getTripName());
        tripValues.put(USER_ID_KEY,trip.getUserID());
        tripValues.put(DATE_KEY,trip.getTripDate());
        tripValues.put(START_LOCATION,trip.getStartLocation());
        tripValues.put(END_LOCATION,trip.getEndLocation());
        //tripValues.put(START_LOCATION,trip.getStartLocation().getLocationName());
        //tripValues.put(END_LOCATION,trip.getEndLocation().getLocationName());



        MutableLiveData<Trip> tripAdded = new MutableLiveData<>();


        dbFirestoreInstance.collection("trips").add(tripValues).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){

                    String documentID = task.getResult().getId();

                  /*  HashMap<String,Object> startLocationValues = new HashMap<>();
                    startLocationValues.put(END_LOCATION,trip.getStartLocation());
                    startLocationValues.put(LATITUDE,String.valueOf(trip.getStartLocation().getLatitude()));
                    startLocationValues.put(LONGITUDE,String.valueOf(trip.getStartLocation().getLongitude()));
                    startLocationValues.put(LOCATION_NAME,String.valueOf(trip.getStartLocation().getLocationName()));


                    HashMap<String,Object> endLocationValues = new HashMap<>();
                    endLocationValues.put(END_LOCATION,trip.getEndLocation());
                    /*endLocationValues.put(LATITUDE,String.valueOf(trip.getEndLocation().getLatitude()));
                    endLocationValues.put(LONGITUDE,String.valueOf(trip.getEndLocation().getLongitude()));
                    endLocationValues.put(LOCATION_NAME,String.valueOf(trip.getEndLocation().getLocationName()));

                    dbFirestoreInstance.collection("trips").document(documentID).collection(START_LOCATION).add(startLocationValues);
                    dbFirestoreInstance.collection("trips").document(documentID).collection(END_LOCATION).add(endLocationValues);
*/
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
                        trip.setStartLocation(new TripLocation(1,1,String.valueOf(document.getData().get("startLocation"))));
                        trip.setEndLocation(new TripLocation(1,1,String.valueOf(document.getData().get("endLocation"))));

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
                    Log.d("test", "error in deleting");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("test", "error in deleting");
            }
        });
        return deleteTripLiveData;
    }
}
