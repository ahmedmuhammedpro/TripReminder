package com.example.tripreminder.view.fragments;


import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.viewmodel.AddTripViewModel;
import com.example.tripreminder.viewmodel.workmanager.WorkManagerViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.spark.submitbutton.SubmitButton;


import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment2 extends Fragment {

    ChipGroup allNotes;
    Button addNoteBtn, cancelTripBtn, prevBtn;
    TextInputEditText noteTxt;
    TextInputLayout noteLayout;
    SubmitButton addTripBtn;
    AddTripViewModel addTripViewModel;
    Trip trip;
    String SAVE_CHANGES = "save changes";
    public AddTripFragment2() {
        // Required empty public constructor
    }

    private void setup(View v) {
        addTripViewModel = ViewModelProviders.of(this).get(AddTripViewModel.class);
        allNotes = v.findViewById(R.id.chipGroupNotes);
        noteTxt = v.findViewById(R.id.noteTxt);
        addTripBtn = v.findViewById(R.id.addTripBtn);
        cancelTripBtn = v.findViewById(R.id.cancelTripBtn);
        addNoteBtn = v.findViewById(R.id.addNoteBtn);
        prevBtn = v.findViewById(R.id.prevBtn);
        noteLayout = v.findViewById(R.id.noteTxtLayout);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteLayout.setBoxStrokeColor(getResources().getColor(R.color.colorPrimaryDark));
                noteTxt.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                if (!noteTxt.getText().toString().isEmpty()) {
                    Chip chip = addNoteChip(allNotes, noteTxt.getText().toString());
                    allNotes.addView(chip);
                    Toast.makeText(AddTripFragment2.super.getContext(), "note number " + allNotes.getChildCount(), Toast.LENGTH_SHORT).show();

                } else {
                    noteLayout.setBoxStrokeColor(getResources().getColor(R.color.design_default_color_error));
                    noteTxt.setHintTextColor(getResources().getColor(R.color.design_default_color_error));


                }
            }
        });

        addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vector<String> notes = addChipsIntoVector(allNotes);
                trip.setNotes(notes);
              if(addTripBtn.getText() != SAVE_CHANGES) {
                  addTripViewModel.addTrip(trip).observe(AddTripFragment2.this, new Observer<Trip>() {
                      @Override
                      public void onChanged(Trip trip) {
                          Toast.makeText(getActivity(), "Trip details:" + trip.getTripDate() + trip.getTripName(), Toast.LENGTH_SHORT).show();
                          WorkManagerViewModel workManagerViewModel = new WorkManagerViewModel(getActivity());
                          workManagerViewModel.addTripToWorkManager(trip);
                          MainFragment fmain = new MainFragment();
                          FragmentManager manager = AddTripFragment2.super.getActivity().getSupportFragmentManager();
                          manager.beginTransaction()
                                  .replace(R.id.container, fmain).commit();
                      }
                  });
              }else{
                  //TODO: update trip
              }
            }
        });
        cancelTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment fmain = new MainFragment();
                FragmentManager manager = AddTripFragment2.super.getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.container, fmain).commit();
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTripFragment1 fone = new AddTripFragment1();
                FragmentManager manager = AddTripFragment2.super.getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.container, fone, "addTripFragment1").commit();
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

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes.removeView(view);
            }
        });
        return chip;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip_fragment2, container, false);
        setup(view);
        editSetup();

        return view;
    }

  private void editSetup (){
      Bundle bundle = getArguments();
      trip = (Trip) bundle.getSerializable(AddTripFragment1.TRIP_Object);
      if(trip.getNotes() != null){
          for(String note : trip.getNotes()){
              Chip chip =  addNoteChip(allNotes,note);
              allNotes.addView(chip);
          }
          addTripBtn.setText(SAVE_CHANGES);
      }

   }
}
