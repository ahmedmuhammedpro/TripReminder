package com.example.tripreminder.view.fragments;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.R;
import com.example.tripreminder.databinding.FragmentAddTripFragment2Binding;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.RequestPermissions;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.viewmodel.AddTripViewModel;
import com.example.tripreminder.viewmodel.workmanager.WorkManagerViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment2 extends Fragment {
    FragmentAddTripFragment2Binding binding;
    MainActivity.SaveAndTripInterface mInterface;
    AddTripViewModel addTripViewModel;
    Trip trip ,trip2;
    String SAVE_CHANGES = "save changes";
    RequestPermissions handler ;
    public AddTripFragment2() {
        // Required empty public constructor
    }

    public void setmInterface(MainActivity.SaveAndTripInterface mInterface) {
        this.mInterface = mInterface;
    }

    private void setup() {
        addTripViewModel = ViewModelProviders.of(this).get(AddTripViewModel.class);
        //binding.prevBtn.setVisibility(View.GONE);
        binding.addNoteBtn.setOnClickListener(view -> {
            binding.noteTxtLayout.setBoxStrokeColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.noteTxt.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if (!binding.noteTxt.getText().toString().isEmpty()) {
                Chip chip = addNoteChip(binding.chipGroupNotes, binding.noteTxt.getText().toString());
                binding.chipGroupNotes.addView(chip);
                Toast.makeText(AddTripFragment2.super.getContext(), "note number " + binding.chipGroupNotes.getChildCount(), Toast.LENGTH_SHORT).show();

            } else {
                binding.noteTxtLayout.setBoxStrokeColor(getResources().getColor(R.color.design_default_color_error));
                binding.noteTxt.setHintTextColor(getResources().getColor(R.color.design_default_color_error));
            }
        });

        binding.addTripBtn.setOnClickListener(view -> {
            WorkManagerViewModel workManagerViewModel = new WorkManagerViewModel(getActivity());
            Vector<String> notes = addChipsIntoVector(binding.chipGroupNotes);
            trip.setNotes(notes);
            if (binding.addTripBtn.getText() != SAVE_CHANGES) {
                //add new trips
                    if (trip2 != null) {
                        trip2.setNotes(notes);
                        addTripViewModel.addTrip(trip2).observe(AddTripFragment2.this, trip -> workManagerViewModel.addTripToWorkManager(trip2));
                    }
                    addTripViewModel.addTrip(trip).observe(AddTripFragment2.this, trip -> {
                        workManagerViewModel.addTripToWorkManager(trip);
                        mInterface.isClicked();
                    });
            } else {
                //update old trip
                addTripViewModel.updateTrip(trip).observe(AddTripFragment2.this, trip -> {
                    workManagerViewModel.editRequest(trip);
                    mInterface.isClicked();
                });
            }
        });
        binding.cancelTripBtn.setOnClickListener(view -> {
            mInterface.isClicked();
        });
        binding.prevBtn.setOnClickListener(v -> {
            if (getFragmentManager().getBackStackEntryCount() != 0) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private Vector<String> addChipsIntoVector(ChipGroup allNotes) {
        Vector<String> notes = new Vector<>();
        int chipCount = allNotes.getChildCount();
        for (int i = 0; i < chipCount; i++) {
            Chip chip = (Chip) allNotes.getChildAt(i);
            notes.add(i, chip.getText().toString());
        }
        return notes;
    }

    private Chip addNoteChip(final ChipGroup notes, String text) {
        Chip chip = (Chip) LayoutInflater.from(AddTripFragment2.super.getContext()).inflate(R.layout.note_item, null, false);
        chip.setText(text);
        chip.setOnCloseIconClickListener(view -> notes.removeView(view));
        return chip;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTripFragment2Binding.inflate(inflater);
        setup();
        editSetup();
        return binding.getRoot();
    }


    private void editSetup (){
      Bundle bundle = getArguments();
      trip = (Trip) bundle.getSerializable(AddTripFragment1.TRIP_Object);
      String round = bundle.getString("tripRound");
      if(round != null){
          trip2 = new Trip();
          trip2.setUserID(trip.getUserID());
          trip2.setTripStatus(Trip.UPCOMING);
          trip2.setTripName(trip.getTripName()+" - (return)");
          trip2.setTripDate(round);
          trip2.setStartLocation(trip.getEndLocation());
          trip2.setEndLocation(trip.getStartLocation());
          trip2.setTripType(2);
      }
      if(trip.getNotes() != null) {
          for (String note : trip.getNotes()) {
              Chip chip = addNoteChip(binding.chipGroupNotes, note);
              binding.chipGroupNotes.addView(chip);
          }
          binding.addTripBtn.setText(SAVE_CHANGES);
      }
   }
}
