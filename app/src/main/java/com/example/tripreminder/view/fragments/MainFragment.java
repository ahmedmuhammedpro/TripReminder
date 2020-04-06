package com.example.tripreminder.view.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.services.FloatingBubbleService;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.LocationLocator;
import com.example.tripreminder.utils.LocationPermissions;
import com.example.tripreminder.utils.RequestPermissions;
import com.example.tripreminder.utils.SharedPreferencesHandler;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.view.adapters.MainAdapter;
import com.example.tripreminder.view.adapters.SwipeToDeleteCallBack;
import com.example.tripreminder.view.adapters.SwipeToEditCallBack;
import com.example.tripreminder.viewmodel.MainViewModel;
import com.example.tripreminder.viewmodel.MainViewModelInterface;
import com.example.tripreminder.viewmodel.workmanager.WorkManagerViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainFragment extends Fragment {
    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final String TAG = "MainFragment";
    public static final String TRIP_Object_FROM_MAIN = "tripMain";
    private RecyclerView recyclerView;
    private LinearLayout noTripsLayout;
    private MainViewModelInterface viewModel;
    private WorkManagerViewModel workManagerViewModel;
    private MainAdapter adapter;
    private Vector<String> allNotes;
    private List<Trip> tripList;
    private boolean isDeleteActionClicked;
    private boolean isEditActionClicked;
    private RequestPermissions permissionsHandler ;
    private String[] notes;

    private MainActivity.EditInterface mInterface;

    private RecyclerItemInterface itemInterface = (view, position) -> {
        Trip trip = tripList.get(position);
        trip.setTripDate(getCurrentDate());
        trip.setTripStatus(Trip.DONE);
        viewModel.updateTrip(trip);
        workManagerViewModel.deleteRequest(trip.getTripId());
        if (LocationPermissions.getInstance(getActivity()).checkPermissions()) {
            if (LocationPermissions.getInstance(getActivity()).isLocationEnabled()) {
                LocationLocator.getInstance(getActivity()).getLastLocation();
            } else {
                Toast.makeText(getActivity(), "Please Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
//            else {
//                LocationPermissions.getInstance(getActivity()).requestPermissions();
//            }

        ((MainActivity) getActivity()).trip = trip;
        //start bubble service
        if (trip.getNotes() != null) {
            notes = new String[trip.getNotes().size()];
            trip.getNotes().toArray(notes);
            initializeFloatingBubble(notes);
        }

    };

    public MainFragment() {
    }

    public void setmInterface(MainActivity.EditInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel();
        allNotes = new Vector<>();
        //readFromSharedPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        MutableLiveData<HashMap<String, Object>> userInfoLiveData = SharedPreferencesHandler.getInstance().getUserInfoLiveData();
        viewModel.getAllTrips(String.valueOf(userInfoLiveData.getValue().get(Constants.USER_ID_TAG))).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                if (trips != null && !trips.isEmpty()) {

                    tripList = trips;
                    for (int i = 0; i < tripList.size(); i++) {
                        Trip currentTrip = tripList.get(i);
                        Log.i("main2", "trip type" + currentTrip.getTripType());
                        viewModel.getTripNotes(currentTrip.getTripId()).observe(MainFragment.this, new Observer<Vector<String>>() {
                            @Override
                            public void onChanged(Vector<String> strings) {
                                currentTrip.setNotes(strings);
                            }
                        });
                    }
                    adapter = new MainAdapter(getActivity(), tripList, itemInterface);
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
        noTripsLayout.setVisibility(INVISIBLE);
        workManagerViewModel = new WorkManagerViewModel(getActivity());
        permissionsHandler = RequestPermissions.getInstance(getActivity());
        permissionsHandler.requestLocPermissionIfNecessary();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        permissionsHandler.requestOverlayPermissionIfNecessary();

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
                        isDeleteActionClicked = true;
                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
                snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (!isDeleteActionClicked) {
                            viewModel.deleteTrip(item.getTripId());
                            workManagerViewModel.deleteRequest(item.getTripId());
                            isDeleteActionClicked = false;
                        }
                    }

                    @Override
                    public void onShown(Snackbar transientBottomBar) {
                        super.onShown(transientBottomBar);
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
        SwipeToEditCallBack swipeToEditCallBack = new SwipeToEditCallBack(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();

                final Trip item = adapter.getData().get(position);
                Log.i("main", "type:" + item.getTripType());
                Snackbar snackbar = Snackbar
                        .make(getView(), "Are you sure you want to Edit it? ", Snackbar.LENGTH_LONG);
                snackbar.setAction("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isEditActionClicked = true;
                        mInterface.isClick(item);
                    }
                });

                snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (!isEditActionClicked) {
                            isEditActionClicked = false;
                        }
                    }

                    @Override
                    public void onShown(Snackbar transientBottomBar) {
                        super.onShown(transientBottomBar);
                        adapter.removeItem(position);
                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToEditCallBack);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public void initializeFloatingBubble(String[] notes) {
       // Log.i("bubble", "start service ");
        Intent intentBubble = new Intent(getActivity(), FloatingBubbleService.class);
        intentBubble.putExtra(Constants.TRIP_NOTES_KEY, notes);
        getActivity().startService(intentBubble);
    }

    public interface RecyclerItemInterface {
        void onItemClick(View view, int position);
    }

    private String getCurrentDate() {
        GregorianCalendar g = new GregorianCalendar();

        return g.get(GregorianCalendar.DAY_OF_MONTH) + "-" +
                g.get(GregorianCalendar.MONTH) + "-" +
                g.get(GregorianCalendar.YEAR) + "-" +
                g.get(GregorianCalendar.HOUR_OF_DAY) + "-" +
                g.get(GregorianCalendar.MINUTE);
    }

    private void readFromSharedPreferences() {
        SharedPreferencesHandler.getInstance().readFromSharedPreferences(getActivity());
    }

}

