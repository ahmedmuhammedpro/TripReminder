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

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String email = getActivity().getIntent().getStringExtra(Constants.EMAIL_KEY);
        String username = getActivity().getIntent().getStringExtra(Constants.USERNAME_KEY);

        String imageURL = getActivity().getIntent().getStringExtra(Constants.IMAGE_URL);

        emailTextView = view.findViewById(R.id.emailTextViewProfile);
        nameTextView = view.findViewById(R.id.nameTextViewProfile);
        signOutButton = view.findViewById(R.id.signOutButton);

        emailTextView.setText(email);
        nameTextView.setText(username);
        profileImageView = view.findViewById(R.id.profileImageView);

        if(profileImageView!=null) {
            Picasso.get()
                    .load(imageURL)
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

}
