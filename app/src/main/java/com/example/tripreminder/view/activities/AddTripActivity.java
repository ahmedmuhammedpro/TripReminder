package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.tripreminder.R;
import com.example.tripreminder.view.fragments.AddTripFragment1;

public class AddTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        AddTripFragment1 fone = new AddTripFragment1();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.tripInformationLayout,fone,"addTripFragment1").commit();
    }
}
