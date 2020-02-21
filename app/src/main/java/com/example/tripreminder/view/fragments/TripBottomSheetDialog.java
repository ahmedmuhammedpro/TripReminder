package com.example.tripreminder.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tripreminder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TripBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "TripBSD";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
        return view;
    }
}
