package com.example.tripreminder.view.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripreminder.R;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.view.activities.AuthenticationActivity;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    ImageView profileImageView;
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

        emailTextView = view.findViewById(R.id.emailTextViewProfile);
        nameTextView = view.findViewById(R.id.nameTextViewProfile);
        signOutButton = view.findViewById(R.id.signOutButton);

        emailTextView.setText(email);
        nameTextView.setText(username);
        profileImageView = view.findViewById(R.id.profileImageView);

        if(imageUrl!=null &&!imageUrl.equals("") && !imageUrl.equals("empty")) {
            Picasso.get()
                    .load(imageUrl)
                    .into(profileImageView);
        }

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
    }

    private void readFromSharedPreferences(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Constants.lOGGED_IN_KEY,false);
        if(loggedIn) {
            email = sharedPreferences.getString(Constants.EMAIL_KEY, "empty");
            imageUrl = sharedPreferences.getString(Constants.IMAGE_URL, "empty");
            username = sharedPreferences.getString(Constants.USERNAME_KEY,"empty");
        }
    }

}
