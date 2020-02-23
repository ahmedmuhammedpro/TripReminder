package com.example.tripreminder.model.Firestore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripFirestoreHandler {

    public static final String TRIP_NAME_KEY="tripName",USER_ID_KEY="userId",DATE_KEY="tripDate",START_LOCATION="startLocation";
    public static final String END_LOCATION = "endLocation";
    private FirebaseFirestore dbFirestoreInstance = FirebaseFirestore.getInstance();

    public MutableLiveData<Trip> addTrip(Trip trip){

        HashMap<String,Object> tripValues = new HashMap<>();
        tripValues.put(TRIP_NAME_KEY,trip.getTripName());
        tripValues.put(USER_ID_KEY,trip.getUserID());
        //tripValues.put(DATE_KEY,trip.getTripDate());
        tripValues.put(START_LOCATION,trip.getStartLocation().getLocationName());
        tripValues.put(END_LOCATION,trip.getEndLocation().getLocationName());
        MutableLiveData<Trip> tripAdded = new MutableLiveData<>();

        dbFirestoreInstance.collection("trips").add(tripValues);

        tripAdded.postValue(trip);

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
}
