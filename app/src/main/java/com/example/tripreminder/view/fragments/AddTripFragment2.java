package com.example.tripreminder.view.fragments;


import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment2 extends Fragment {

     ChipGroup allNotes;
     Button addNoteBtn ,addTripBtn,cancelTripBtn;
     TextInputEditText noteTxt;
     TextInputLayout noteLayout;
     Vector<String> notes;
    public AddTripFragment2() {
        // Required empty public constructor
    }
    private void setup(View v){
        notes= new Vector<>();
        allNotes =  v.findViewById(R.id.chipGroupNotes);
        noteTxt = v.findViewById(R.id.noteTxt);
        addTripBtn = v.findViewById(R.id.addTripBtn);
        cancelTripBtn = v.findViewById(R.id.cancelTripBtn);
        addNoteBtn = v.findViewById(R.id.addNoteBtn);
        noteLayout = v.findViewById(R.id.noteTxtLayout);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteLayout.setBoxStrokeColor(getResources().getColor(R.color.colorPrimaryDark));
                noteTxt.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                if(!noteTxt.getText().toString().isEmpty()){
                    Chip chip = addNoteChip(allNotes,noteTxt.getText().toString());
                        allNotes.addView(chip);
                    Toast.makeText(AddTripFragment2.super.getContext(), "note number "+allNotes.getChildCount(), Toast.LENGTH_SHORT).show();

                }else{
                    noteLayout.setBoxStrokeColor(getResources().getColor(R.color.design_default_color_error));
                    noteTxt.setHintTextColor(getResources().getColor(R.color.design_default_color_error));


                }
            }
        });

      addTripBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              addChipsIntoVector(allNotes,notes);
          }
      });
    }
    private void addChipsIntoVector(ChipGroup allNotes,Vector<String> notes){
        int chipCount =  allNotes.getChildCount();
        for(int i=0;i<chipCount;i++){
            Chip chip = (Chip) allNotes.getChildAt(i);
            notes.add(i,chip.getText().toString());
        }
    }
    private Chip addNoteChip(final ChipGroup notes,String text){
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
        return view;
    }


}
