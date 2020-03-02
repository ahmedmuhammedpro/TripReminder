package com.example.tripreminder.view.fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Firestore.TripFirestoreHandler;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.view.adapters.MainAdapter;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.view.adapters.SwipeToDeleteCallBack;
import com.example.tripreminder.view.adapters.SwipeToEditCallBack;
import com.example.tripreminder.viewmodel.MainViewModel;
import com.example.tripreminder.viewmodel.MainViewModelInterface;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Vector;

import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";
    public static final String TRIP_Object_FROM_MAIN = "tripMain";
    private RecyclerView recyclerView;
    private LinearLayout noTripsLayout;
    private MainViewModelInterface viewModel;
    MainAdapter adapter;
    Vector<String> allNotes;
    private List<Trip> tripList;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel();
        allNotes = new Vector<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = v.findViewById(R.id.upcoming_trips_recycler_view);
        noTripsLayout = v.findViewById(R.id.no_trips_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(INVISIBLE);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getAllTrips(MainActivity.userId).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter = new MainAdapter(getActivity(), trips);
                if (trips != null && !trips.isEmpty()) {
                    tripList = trips;
                    for (int i = 0; i < tripList.size(); i++) {
                        Trip currentTrip = tripList.get(i);
                        viewModel.getTripNotes(currentTrip.getTripId()).observe(MainFragment.this, new Observer<Vector<String>>() {
                            @Override
                            public void onChanged(Vector<String> strings) {
                                currentTrip.setNotes(strings);
                            }
                        });
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(VISIBLE);
                    noTripsLayout.setVisibility(INVISIBLE);
                    enableSwipeToDeleteAndUndo();
                    enableSwipeToEdit();

                } else {
                    recyclerView.setVisibility(INVISIBLE);
                    noTripsLayout.setVisibility(VISIBLE);
                }
            }
        });

        return v;
    }

    //set swiper items

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Trip item = adapter.getData().get(position);

                adapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(getView(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    private void enableSwipeToEdit() {
        SwipeToEditCallBack swipeToDeleteCallback = new SwipeToEditCallBack(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Trip item = adapter.getData().get(position);

                Snackbar snackbar = Snackbar
                        .make(getView(), "Are you sure you want to Edit it? ", Snackbar.LENGTH_LONG);
                snackbar.setAction("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddTripFragment1 fone = new AddTripFragment1();
                        FragmentManager manager = MainFragment.super.getActivity().getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(TRIP_Object_FROM_MAIN, item);
                        fone.setArguments(bundle);
                        manager.beginTransaction()
                                .replace(R.id.container, fone).commit();
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
