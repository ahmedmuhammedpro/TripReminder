package com.example.tripreminder.view.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.view.adapters.MainAdapter;
import com.example.tripreminder.viewmodel.MainViewModel;
import com.example.tripreminder.viewmodel.PastTripsViewModel;

import java.util.List;
import java.util.Vector;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripsDataFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout noTripsLayout;
    private PastTripsViewModel pastTripsViewModel;
    MainAdapter adapter;
    private List<Trip> tripList;
    View v;
    public PastTripsDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_past_trips_data, container, false);
        setup();
        return v;
    }

    private void setup(){

        recyclerView = v.findViewById(R.id.upcoming_trips_recycler_view_pastTrips);
        noTripsLayout = v.findViewById(R.id.no_trips_layout_pastTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(INVISIBLE);
        pastTripsViewModel = ViewModelProviders.of(this).get(PastTripsViewModel.class);

        pastTripsViewModel.getPastTrips(MainActivity.userId).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter = new MainAdapter(getActivity(), trips);
                if (trips != null && !trips.isEmpty()) {
                    tripList = trips;
                    for (int i = 0; i < tripList.size(); i++) {
                        Trip currentTrip = tripList.get(i);
                        pastTripsViewModel.getTripNotes(currentTrip.getTripId()).observe(PastTripsDataFragment.this, new Observer<Vector<String>>() {
                            @Override
                            public void onChanged(Vector<String> strings) {
                                currentTrip.setNotes(strings);
                            }
                        });
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(VISIBLE);
                    noTripsLayout.setVisibility(INVISIBLE);
                } else {
                    recyclerView.setVisibility(INVISIBLE);
                    noTripsLayout.setVisibility(VISIBLE);
                }
            }
        });

    }
}
