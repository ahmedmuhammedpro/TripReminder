package com.example.tripreminder.view.fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.view.adapters.MainAdapter;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.view.adapters.SwipeToDeleteCallBack;
import com.example.tripreminder.viewmodel.MainViewModel;
import com.example.tripreminder.viewmodel.MainViewModelInterface;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    private RecyclerView recyclerView;
    private LinearLayout noTripsLayout;
    private MainViewModelInterface viewModel;
    MainAdapter adapter;
    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel();
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
                if (trips != null && !trips.isEmpty()) {
                    adapter = new MainAdapter(getActivity(), trips);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(VISIBLE);
                    noTripsLayout.setVisibility(INVISIBLE);
                    enableSwipeToDeleteAndUndo();
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
                        .make(getView(), "Item was removed from the list.", Snackbar.LENGTH_INDEFINITE);
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
}
