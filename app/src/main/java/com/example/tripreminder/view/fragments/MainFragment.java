package com.example.tripreminder.view.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.example.tripreminder.viewmodel.MainViewModel;
import com.example.tripreminder.viewmodel.MainViewModelInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    private RecyclerView recyclerView;
    private List<Trip> tripList;
    private MainViewModelInterface viewModel;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripList = new ArrayList<>();
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));
        tripList.add(new Trip("1", 1, "ismalia",
                new TripLocation(10.0, 15.01, "iti ismalia"),
                new TripLocation(10.0, 15.01, "iti smart"), null,
                new Date()));

        viewModel = new MainViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = v.findViewById(R.id.upcoming_trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        return v;
    }

}
