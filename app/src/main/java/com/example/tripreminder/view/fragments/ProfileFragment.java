package com.example.tripreminder.view.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.SharedPreferencesHandler;
import com.example.tripreminder.view.activities.AuthenticationActivity;
import com.example.tripreminder.viewmodel.ProfileViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    ProfileViewModel profileViewModel;
    ShapeableImageView profileImageView;
    TextView emailTextView,nameTextView;
    Button signOutButton;
    String email="",username="",imageUrl="";
    boolean loggedIn=false;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        readFromSharedPreferences();

        profileViewModel =  ViewModelProviders.of(this).get(ProfileViewModel.class);

        emailTextView = view.findViewById(R.id.emailTextViewProfile);
        nameTextView = view.findViewById(R.id.nameTextViewProfile);
        signOutButton = view.findViewById(R.id.signOutButton);

        profileImageView = view.findViewById(R.id.profileImageView);


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });



        return view;
    }

    public void signOut(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.lOGGED_IN_KEY,false);
        editor.commit();
        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
        startActivity(intent);
        /*
        SharedPreferencesHandler.getInstance().getUserInfoLiveData().observe(this, new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                profileViewModel.signout().observe(ProfileFragment.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {


                    }
                });
            }
        });
*/

    }

    private void readFromSharedPreferences(){
            SharedPreferencesHandler.getInstance().getUserInfoLiveData().observe(this, new Observer<HashMap<String, Object>>() {
                @Override
                public void onChanged(HashMap<String, Object> userInfoHashMap) {

                    loggedIn = (boolean)userInfoHashMap.get(Constants.lOGGED_IN_KEY);
                    if(loggedIn) {
                        email = String.valueOf(userInfoHashMap.get(Constants.EMAIL_KEY));
                        imageUrl = String.valueOf(userInfoHashMap.get(Constants.IMAGE_URL));
                        username = String.valueOf(userInfoHashMap.get(Constants.USERNAME_KEY));

                        emailTextView.setText(email);
                        nameTextView.setText(username);

                        if(imageUrl!=null &&!imageUrl.equals("") && !imageUrl.equals("empty")) {
                            Picasso.get()
                                    .load(imageUrl)
                                    .into(profileImageView);
                        }
                    }
                }
            });
        }


}
