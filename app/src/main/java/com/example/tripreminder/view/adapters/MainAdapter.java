package com.example.tripreminder.view.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.view.fragments.TripBottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private Context context;
    private List<Trip> tripList;
    private SimpleDateFormat format;

    public MainAdapter(Context context,  List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
        format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.US);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.upcoming_trip_item, parent, false);
        MainViewHolder mainViewHolder = new MainViewHolder(view);
        mainViewHolder.tripOptionsIV.setOnClickListener(v -> {
            TripBottomSheetDialog bottomSheetDialog = new TripBottomSheetDialog();
            bottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), TripBottomSheetDialog.TAG);
        });
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        //holder.tripDateTV.setText(format.format(tripList.get(position).getTripDate()).toString());
        holder.tripNameTV.setText(tripList.get(position).getTripName());
        //holder.tripStartLocationTV.setText("Start point: " + tripList.get(position).getStartLocation().getLocationName());
        //holder.tripEndLocationTV.setText("End point: " + tripList.get(position).getEndLocation().getLocationName());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView tripDateTV;
        TextView tripNameTV;
        TextView tripStatusTV;
        ImageView tripOptionsIV;
        TextView tripStartLocationTV;
        TextView tripEndLocationTV;
        Button startTripButton;
        ImageView notesIV;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tripDateTV = itemView.findViewById(R.id.trip_date);
            tripNameTV = itemView.findViewById(R.id.trip_name);
            tripStatusTV = itemView.findViewById(R.id.trip_status);
            tripOptionsIV = itemView.findViewById(R.id.trip_options);
            tripStartLocationTV = itemView.findViewById(R.id.trip_start_location);
            tripEndLocationTV = itemView.findViewById(R.id.trip_end_location);
            startTripButton = itemView.findViewById(R.id.start_trip_button);
            notesIV = itemView.findViewById(R.id.trip_notes);
        }
    }
}
