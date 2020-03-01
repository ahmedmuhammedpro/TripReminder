package com.example.tripreminder.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.view.adapters.MainAdapter;
import com.example.tripreminder.viewmodel.BottomSheetViewModel;
import com.example.tripreminder.viewmodel.workmanager.WorkManagerViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TripBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "TripBSD";
    //String tripID="";

    BottomSheetViewModel bottomSheetViewModel;
    TextView editTextView,cancelTextView,deleteTextView,doneTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Trip trip = (Trip) getArguments().getSerializable(Constants.TRIP_OB_KEY);
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);

        bottomSheetViewModel = ViewModelProviders.of(getActivity()).get(BottomSheetViewModel.class);
        editTextView = view.findViewById(R.id.edit);
        cancelTextView = view.findViewById(R.id.cancel);
        deleteTextView = view.findViewById(R.id.delete);
        doneTextView = view.findViewById(R.id.done);

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetViewModel.deleteTrip(trip.getTripId()).observe(getActivity(), new Observer<Trip>() {
                    @Override
                    public void onChanged(Trip trip) {
                        Toast.makeText(getActivity(), "Trip Deleted", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        });

        editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManagerViewModel workManagerViewModel = new WorkManagerViewModel(getActivity());
                workManagerViewModel.editRequest(trip);
                dismiss();
            }
        });

        return view;
    }

}
