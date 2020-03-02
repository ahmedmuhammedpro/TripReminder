package com.example.tripreminder.view.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripreminder.R;
import com.example.tripreminder.utils.Constants;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    ImageView profileImageView;
    TextView emailTextView,nameTextView;


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

        emailTextView = view.findViewById(R.id.nameTextViewProfile);
        nameTextView = view.findViewById(R.id.emailTextViewProfile);

        emailTextView.setText(email);
        nameTextView.setText(username);
        profileImageView = view.findViewById(R.id.profileImageView);

        Picasso.get()
                .load(imageURL)
                .into(profileImageView);
        return view;
    }

}
