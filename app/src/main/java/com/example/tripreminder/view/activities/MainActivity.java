package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.map_directions.TaskLoadedCallback;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.LocationCommunicator;
import com.example.tripreminder.view.fragments.AddTripFragment1;
import com.example.tripreminder.view.fragments.FeedbackFragment;
import com.example.tripreminder.view.fragments.MainFragment;
import com.example.tripreminder.view.fragments.PastTripsFragment;
import com.example.tripreminder.view.fragments.PastTripsMapFragment;
import com.example.tripreminder.view.fragments.ProfileFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements TaskLoadedCallback, LocationCommunicator {

    private boolean isBackPressedTwice;
    Trip currentTripForEdit;
    boolean isClicked;
    boolean isEditClicked = true;
    int previousFragmentNumber = 2;
    private Fragment selectedFragment;
    public static String userId = "";
    public static final String USER_ID_TAG = "userID";
    public Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(savedInstanceState != null){
//            selectedFragment = getSupportFragmentManager().getFragment(savedInstanceState, "AddTripFragment");
//
//        }

        setContentView(R.layout.activity_main);
        userId = getIntent().getStringExtra(USER_ID_TAG);
        setupBottomBar();
    }

    private void setupBottomBar() {
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        SaveAndTripInterface mInterface = new SaveAndTripInterface() {
            @Override
            public void isClicked() {
                isClicked = true;
                bottomNavigation.show(2, true);
            }
        };

        EditInterface editInterface = new EditInterface() {
            @Override
            public void isClick(Trip t) {
                currentTripForEdit = t;
                isEditClicked = true;
                bottomNavigation.show(3, true);
            }
        };

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
                    case 1:
                        selectedFragment = new PastTripsFragment();
                        break;
                    case 2:
                        selectedFragment = new MainFragment();
                        ((MainFragment) selectedFragment).setmInterface(editInterface);
                        Log.i("ahmed", editInterface + "");
                        break;
                    case 3:
                        selectedFragment = new AddTripFragment1();
                        ((AddTripFragment1) selectedFragment).setmInterface(mInterface);
                        break;
                    case 4:
                        selectedFragment = new ProfileFragment();
                        break;
                    case 5:
                        selectedFragment = new FeedbackFragment();
                        break;
                }
                if (item.getId() > previousFragmentNumber) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_enter_right_to_left, R.anim.fragment_exit_to_left)
                            .replace(R.id.container, selectedFragment).commit();
                } else if (item.getId() < previousFragmentNumber) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_enter_left_to_right, R.anim.fragment_exit_to_right)
                            .replace(R.id.container, selectedFragment).commit();
                }

                previousFragmentNumber = item.getId();
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
                if (isClicked && item.getId() == 2) {
                    Log.i("ahmed", "i am in onShowItem()");
                    selectedFragment = new MainFragment();
                    ((MainFragment) selectedFragment).setmInterface(editInterface);
                    if (item.getId() > previousFragmentNumber) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter_right_to_left, R.anim.fragment_exit_to_left)
                                .replace(R.id.container, selectedFragment).commit();
                    } else if (item.getId() < previousFragmentNumber) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter_left_to_right, R.anim.fragment_exit_to_right)
                                .replace(R.id.container, selectedFragment).commit();
                    }

                    previousFragmentNumber = item.getId();
                    isClicked = false;
                } else if (isEditClicked && item.getId() == 3) {
                    Log.i("ahmed", "edit frag");
                    selectedFragment = new AddTripFragment1();
                    ((AddTripFragment1) selectedFragment).setmInterface(mInterface);
                    if (currentTripForEdit != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(MainFragment.TRIP_Object_FROM_MAIN, currentTripForEdit);
                        selectedFragment.setArguments(bundle);
                    }
                    if (item.getId() > previousFragmentNumber) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter_right_to_left, R.anim.fragment_exit_to_left)
                                .replace(R.id.container, selectedFragment).commit();
                    } else if (item.getId() < previousFragmentNumber) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter_left_to_right, R.anim.fragment_exit_to_right)
                                .replace(R.id.container, selectedFragment).commit();
                    }

                    previousFragmentNumber = item.getId();
                    isEditClicked = false;
                }


            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        bottomNavigation.show(2, true);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setmInterface(editInterface);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MainFragment()).commit();
    }

    //for googleMap line drawing
    @Override
    public void onTaskDone(Object... values) {

        Random rnd = new Random();


        if (getSupportFragmentManager().getFragments().get(1) instanceof PastTripsMapFragment) {

            ((PastTripsMapFragment) (getSupportFragmentManager().getFragments().get(1))).googleMap.addPolyline((PolylineOptions) values[0]).setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

        }
    }

    @Override
    public void onLocationReceivedAction(double longitude, double latitude, String locationInfo) {

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(Uri.parse("http://maps.google.com/maps?" +
                "saddr=" + latitude + "," + longitude +
                "&daddr=" + trip.getEndLocation().getLatitude() + "," + trip.getEndLocation().getLongitude()));
        startActivity(mapIntent);
    }

    @Override
    public void onBackPressed() {
        if (isBackPressedTwice) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        isBackPressedTwice = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressedTwice = false;
            }
        }, 30000);
    }

    public interface SaveAndTripInterface {
        void isClicked();
    }

    public interface EditInterface {
        void isClick(Trip t);
    }

}
