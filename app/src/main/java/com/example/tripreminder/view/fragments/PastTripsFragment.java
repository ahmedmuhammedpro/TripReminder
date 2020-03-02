package com.example.tripreminder.view.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.tripreminder.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripsFragment extends Fragment {

    TabLayout tabLayout;
    FrameLayout fragmentFrameLayout;
    View view ;
    public PastTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_past_trips, container, false);
        tabLayout = view.findViewById(R.id.pastTripsTabLayout);
        fragmentFrameLayout = view.findViewById(R.id.pastTripsFrameLayout);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.pastTripsFrameLayout, new PastTripsDataFragment()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(tab.getPosition()==0){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_enter_left_to_right,R.anim.fragment_exit_to_right)
                            .replace(R.id.pastTripsFrameLayout, new PastTripsDataFragment()).commit();
                }
                else if (tab.getPosition() == 1){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_enter_right_to_left,R.anim.fragment_exit_to_left)
                            .replace(R.id.pastTripsFrameLayout, new PastTripsMapFragment()).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
