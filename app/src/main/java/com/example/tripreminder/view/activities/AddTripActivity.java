package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tripreminder.R;
import com.example.tripreminder.view.fragments.AddTripFragment1;
import com.example.tripreminder.view.fragments.AddTripFragment2;

public class AddTripActivity extends AppCompatActivity {

    Button closeBtn,nextBtn,prevBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setup();

    }
    private void setup(){

        //buttons views
        closeBtn = findViewById(R.id.closeBtn);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        prevBtn.setEnabled(false);
        //button listners
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 nextBtn.setEnabled(false);
                 prevBtn.setEnabled(true);
                 AddTripFragment2 ftwo = new AddTripFragment2();
                 FragmentManager manager = getSupportFragmentManager();
                // manager.popBackStack();
                 manager.beginTransaction().replace(R.id.tripInformationLayout,ftwo,"addTripFragment2").commit();
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(false);
                FragmentManager manager = getSupportFragmentManager();
                AddTripFragment1 fone = new AddTripFragment1();
                manager.beginTransaction().replace(R.id.tripInformationLayout,fone,"addTripFragment1").commit();

            }
        });
        FragmentManager manager = getSupportFragmentManager();
        AddTripFragment1 fone = new AddTripFragment1();
        manager.beginTransaction().replace(R.id.tripInformationLayout,fone,"addTripFragment1").commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("res" , "");
    }
}
