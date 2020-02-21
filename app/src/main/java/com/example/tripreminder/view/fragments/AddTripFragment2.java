package com.example.tripreminder.view.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;
import com.google.android.material.chip.ChipGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment2 extends Fragment {

     ChipGroup allNotes;

    public AddTripFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_trip_fragment2, container, false);
    }

}
