package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tripreminder.R;
import com.example.tripreminder.view.fragments.AddTripFragment1;
import com.example.tripreminder.view.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment selectedFragment;
    public static String userId="";
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
                    case 1: break;
                    case 2: selectedFragment = new MainFragment(); break;
                    case 3: selectedFragment = new AddTripFragment1(); break;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selectedFragment).commit();
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
    }
}
