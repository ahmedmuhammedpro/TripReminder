package com.example.tripreminder.utils;

public interface LocationCommunicator {
    void onLocationReceivedAction(double longitude, double latitude, String locationInfo);
}
