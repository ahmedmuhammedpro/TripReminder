package com.example.tripreminder.view.adapters;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.view.fragments.TripBottomSheetDialog;
import com.example.tripreminder.viewmodel.MainViewModel;

import com.example.tripreminder.viewmodel.MainViewModelInterface;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private Context context;
    private List<Trip> tripList;
    private MainViewModelInterface viewModel;
    private Vector<String> allNotes;
    boolean isAdded = false;

    public MainAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
        allNotes = new Vector<>();
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.upcoming_trip_item, parent, false);
        MainViewHolder mainViewHolder = new MainViewHolder(view);
        mainViewHolder.tripNotesBtn.setOnClickListener(v -> {
            Trip currentTrip = tripList.get(mainViewHolder.getAdapterPosition());
            Log.i("adapter","trip type"+currentTrip.getTripType());
            TripBottomSheetDialog bottomSheetDialog = new TripBottomSheetDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TRIP_OB_KEY, currentTrip);
            bottomSheetDialog.setArguments(bundle);
            bottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), TripBottomSheetDialog.TAG);
        });

        mainViewHolder.startTripButton.setOnClickListener(v -> {
            Trip trip = tripList.get(mainViewHolder.getAdapterPosition());
            trip.setTripStatus(Trip.DONE);
            MainViewModel mainViewModel = new MainViewModel();
            mainViewModel.updateTripStatus(trip.getTripId(), trip.getTripStatus());
        });
        return mainViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.tripDateTV.setText(getDate(tripList.get(position).getTripDate()));
        holder.tripTimeTV.setText(getTime(tripList.get(position).getTripDate()));
        holder.tripNameTV.setText(tripList.get(position).getTripName());
        holder.tripNotesBtn.setOnClickListener(v -> {
            if (holder.notesLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(holder.cardTrip, new AutoTransition());
                holder.notesLayout.setVisibility(View.VISIBLE);
                holder.tripNotesBtn.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);
                allNotes = tripList.get(position).getNotes();
                Log.i("adapter2","trip type"+tripList.get(position).getTripType());
                if (!isAdded) {
                    if (allNotes != null && allNotes.size() != 0) {
                        for (String txt : allNotes) {
                            Chip chip = addNoteChip(txt);
                            holder.notesGroup.addView(chip);
                            isAdded = true;
                        }
                    }
                }
            } else {
                TransitionManager.beginDelayedTransition(holder.cardTrip, new AutoTransition());

                holder.notesLayout.setVisibility(View.GONE);
                holder.tripNotesBtn.setBackgroundResource(R.drawable.ic_arrow_downward_black_24dp);
            }
        });
        holder.tripStartLocationTV.setText(tripList.get(position).getStartLocation().getLocationName());
        holder.tripEndLocationTV.setText(tripList.get(position).getEndLocation().getLocationName());

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView tripDateTV;
        TextView tripTimeTV;
        TextView tripNameTV;
        TextView tripStatusTV;
        Button tripNotesBtn;
        TextView tripStartLocationTV;
        TextView tripEndLocationTV;
        Button startTripButton;
        ChipGroup notesGroup;
        ConstraintLayout notesLayout;
        CardView cardTrip;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tripDateTV = itemView.findViewById(R.id.trip_date);
            tripNameTV = itemView.findViewById(R.id.trip_name);
            tripStatusTV = itemView.findViewById(R.id.trip_status);
            tripNotesBtn = itemView.findViewById(R.id.trip_options);
            tripStartLocationTV = itemView.findViewById(R.id.trip_start_location);
            tripEndLocationTV = itemView.findViewById(R.id.trip_end_location);
            startTripButton = itemView.findViewById(R.id.start_trip_button);
            notesGroup = itemView.findViewById(R.id.notesGroupItem);
            notesLayout = itemView.findViewById(R.id.layout_note_expand);
            cardTrip = itemView.findViewById(R.id.cardTrip);
            tripTimeTV = itemView.findViewById(R.id.trip_hour);
        }
    }

    private Chip addNoteChip(String text) {
        Chip chip = (Chip) LayoutInflater.from(context).inflate(R.layout.note_item, null, false);
        chip.setText(text);
        chip.getCloseIcon().setVisible(false, false);
        chip.setTextIsSelectable(false);
        return chip;
    }

    //swiper delete Items setup
    public void removeItem(int position) {
        tripList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Trip trip, int position) {
        tripList.add(position, trip);
        notifyItemInserted(position);
    }

    public List<Trip> getData() {
        return tripList;
    }

    private String getDate(String dateString) {
        String[] strings = dateString.split("-");
        int day = Integer.parseInt(strings[0]);
        int month = Integer.parseInt(strings[1]);
        int year = Integer.parseInt(strings[2]);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

    }

    private String getTime(String dateString) {
        String[] strings = dateString.split("-");
        int hour = Integer.parseInt(strings[3]);
        int minute = Integer.parseInt(strings[4]);
        return hour + ":" + minute;

    }
}
