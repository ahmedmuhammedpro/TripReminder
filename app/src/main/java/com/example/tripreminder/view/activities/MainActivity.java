package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.map_directions.TaskLoadedCallback;
import com.example.tripreminder.view.fragments.AddTripFragment1;
import com.example.tripreminder.view.fragments.FeedbackFragment;
import com.example.tripreminder.view.fragments.MainFragment;
import com.example.tripreminder.view.fragments.PastTripsFragment;
import com.example.tripreminder.view.fragments.PastTripsMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity implements TaskLoadedCallback {

    int testMapCounter =1;
    int previousFragmentNumber=2;
    private Fragment selectedFragment;
    public static String userId="";
    Polyline currentPolyline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId = getIntent().getStringExtra(LoginActivity.USER_ID_TAG);
        setupBottomBar();
    }
    private void setupBottomBar (){
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_history_24px));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_event_24px));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_add_location_24px));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_person_outline_24px));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_help_outline_24px));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                switch (item.getId()) {
                    case 1: selectedFragment = new PastTripsFragment();break;
                    case 2: selectedFragment = new MainFragment();break;
                    case 3: selectedFragment = new AddTripFragment1();break;
                    case 5: selectedFragment = new FeedbackFragment(); break;
                }
                if(item.getId()>previousFragmentNumber) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_enter_right_to_left,R.anim.fragment_exit_to_left)
                            .replace(R.id.container, selectedFragment).commit();
                }else if (item.getId()<previousFragmentNumber) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_enter_left_to_right,R.anim.fragment_exit_to_right)
                            .replace(R.id.container, selectedFragment).commit();
                }
                previousFragmentNumber = item.getId();
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });



        //bottomNavigation.show(2, true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new MainFragment() ).commit();
        bottomNavigation.show(2, true);
    }

    //for googleMap line drawing
    @Override
    public void onTaskDone(Object... values) {
        //if (currentPolyline != null)
          //  currentPolyline.remove();

        if(getSupportFragmentManager().getFragments().get(1)instanceof PastTripsMapFragment) {
            if(testMapCounter ==1) {
                ((PastTripsMapFragment) (getSupportFragmentManager().getFragments().get(1))).googleMap.addPolyline((PolylineOptions) values[0]).setColor(Color.BLUE);
                testMapCounter++;
            }
            else{
                ((PastTripsMapFragment) (getSupportFragmentManager().getFragments().get(1))).googleMap.addPolyline((PolylineOptions) values[0]).setColor(Color.RED);
            }
        }
    }

    public void signOut(){

        SharedPreferences sharedPreferences = getSharedPreferences(SplashScreenActivity.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SplashScreenActivity.lOGGED_IN_KEY,false);
        editor.commit();
        Intent intent = new Intent(this,AuthenticationActivity.class);
        startActivity(intent);
    }

}
