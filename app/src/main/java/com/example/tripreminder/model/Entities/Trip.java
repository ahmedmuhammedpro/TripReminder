package com.example.tripreminder.model.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Trip implements Serializable {

    public static final int UPCOMING = 1;
    public static final int DONE = 2;
    public static final int CANCELLED = 2;

    private String userID;
    private String tripId;
    private int tripStatus;
    private int tripType;
    private String tripName;
    private TripLocation startLocation;
    private TripLocation endLocation;
    private Vector<String> notes;
    private String tripDate;

    public Trip() {
        startLocation = new TripLocation(0, 0, "");
        endLocation = new TripLocation(0, 0, "");
    }

    public Trip(String tripId, int tripStatus, String tripName, TripLocation startLocation,
                TripLocation endLocation, Vector<String> notes, String tripDate, int tripType) {
        this.tripId = tripId;
        this.tripStatus = tripStatus;
        this.tripName = tripName;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.notes = notes;
        this.tripDate = tripDate;
        this.tripType = tripType;
    }

    public Trip(String tripId, int tripStatus, String tripName, TripLocation startLocation,
                TripLocation endLocation, Vector<String> notes, String tripDate){
        this.tripId = tripId;
        this.tripStatus = tripStatus;
        this.tripName = tripName;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.notes = notes;
        this.tripDate = tripDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public TripLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(TripLocation startLocation) {
        this.startLocation = startLocation;
    }

    public TripLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(TripLocation endLocation) {
        this.endLocation = endLocation;
    }

    public Vector<String> getNotes() {
        return notes;
    }

    public void setNotes(Vector<String> notes) {
        this.notes = notes;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public void setTripType(int tripType) {
        this.tripType = tripType;
    }

    public int getTripType() {
        return tripType;
    }
}
