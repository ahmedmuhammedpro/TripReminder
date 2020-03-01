package com.example.tripreminder.view.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.services.FloatingBubbleService;
import com.example.tripreminder.view.activities.MainActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    Button testbubble;
    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feedback_fragment, container, false);
        testbubble = v.findViewById(R.id.testBubble);
        testbubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                } else {
                    initializeFloatingBubble();
                }

            }
        });

        return v;
    }
    private void initializeFloatingBubble() {
        Log.i("bubble","start service ");
        getActivity().startService(new Intent(getActivity(),FloatingBubbleService.class));
        getActivity().finish();
    }


    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeFloatingBubble();
            } else { //Permission is not available
                Toast.makeText(getContext(), "Draw over other app permission not available. Closing the application", Toast.LENGTH_SHORT).show();
               // getActivity().finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
