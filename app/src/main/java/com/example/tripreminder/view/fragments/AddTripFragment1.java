package com.example.tripreminder.view.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.example.tripreminder.model.repositories.TripRepositoryImp;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.view.adapters.PlacesAutoCompleteAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.tripreminder.viewmodel.AddTripViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment1 extends Fragment {
    public static final String TRIP_Object = "trip";
    public static final String TIME_FORMAT_1 = "hh:mm a";
    public static final String DATE_FORMAT_1 = "dd-MMM-yyyy";
    private ChipGroup tripTypes;
    private  Button setDate , setTime;
    private  TextView dateTxt,timeTxt;
    private AutoCompleteTextView startPointTxt,endPointTxt;
    private TextInputEditText nameTxt;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int tripType;
    String dateString ;

    Button nextBtn;

    public AddTripFragment1() {
        // Required empty public constructor
    }

    public static Date getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date today = Calendar.getInstance().getTime();
        return today;
    }

    public static Date getCurrentTime() {
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_1);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date today = Calendar.getInstance().getTime();
            return today;
    }
    private void setup(View view,Bundle saveInstanseState){

        //set views
        tripTypes = view.findViewById(R.id.chipGroupTripTypes);
        nextBtn = view.findViewById(R.id.nextBtn);
        nameTxt = view.findViewById(R.id.tripNameTxt);
        setDate = view.findViewById(R.id.dateBtn);
        setTime = view.findViewById(R.id.timeBtn);
        dateTxt = view.findViewById(R.id.dateTxt);
        timeTxt = view.findViewById(R.id.timeTxt);
        startPointTxt = view.findViewById(R.id.startPointTxt);
        endPointTxt = view.findViewById(R.id.endPointTxt);
        //set listners
         setDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // Get Current Date
                 final Calendar c = Calendar.getInstance();
                 c.setTime(getCurrentDate());
                 mYear = c.get(Calendar.YEAR);
                 mMonth = c.get(Calendar.MONTH);
                 mDay = c.get(Calendar.DAY_OF_MONTH);

                 // Launch Date Picker Dialog
                 DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripFragment1.super.getContext(),
                         new DatePickerDialog.OnDateSetListener() {
                             @Override
                             public void onDateSet(DatePicker view, int year,
                                                   int monthOfYear, int dayOfMonth) {
                                 c.set(year,monthOfYear,dayOfMonth);
                                String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                               dateTxt.setText(date);
                                 monthOfYear +=1;
                                 dateString = ""+year+"-"+monthOfYear+"-"+dayOfMonth;
                             }
                         }, mYear, mMonth, mDay);
                 datePickerDialog.show();
             }
         });
         setTime.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // Get Current Time
                 final Calendar c = Calendar.getInstance();
                 c.setTime(getCurrentTime());
                 mHour = c.get(Calendar.HOUR_OF_DAY);
                 mMinute = c.get(Calendar.MINUTE);
                 // Launch Time Picker Dialog
                 TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripFragment1.super.getContext(),
                         new TimePickerDialog.OnTimeSetListener() {
                             @Override
                             public void onTimeSet(TimePicker view, int hourOfDay,
                                                   int minute) {
                                dateString +="-"+mHour+"-"+mMinute;
                                 timeTxt.setText(hourOfDay + ":" + minute);
                             }
                         }, mHour, mMinute, true);
                 timePickerDialog.show();
             }
         });

        tripTypes.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group,@IdRes int checkedId) {
                // Handle the checked chip change.
                int chipsCount = group.getChildCount();
                if (chipsCount > 0) {
                    int i = 0;
                    while (i < chipsCount) {
                        Chip chip = (Chip) group.getChildAt(i);
                        if (chip.isChecked()) {
                            tripType = i+1;
                        }
                        i++;
                    }
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextBtn.setEnabled(false);
                Trip trip = new Trip();
                trip.setTripType(tripType);
                trip.setTripName(nameTxt.getText().toString());
                trip.setUserID(MainActivity.userId);
                trip.setStartLocation(new TripLocation(1,1,startPointTxt.getText().toString()));
                trip.setEndLocation(new TripLocation(1,1,endPointTxt.getText().toString()));
                trip.setTripDate(dateString);
                AddTripFragment2 ftwo = new AddTripFragment2();
                FragmentManager manager = AddTripFragment1.super.getActivity().getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TRIP_Object, trip);
                ftwo.setArguments(bundle);
                manager.beginTransaction().replace(R.id.container,ftwo,"addTripFragment2").commit();
            }
        });

         //set Adapters
        startPointTxt.setAdapter(new PlacesAutoCompleteAdapter(AddTripFragment1.super.getContext(),android.R.layout.simple_list_item_1));
        endPointTxt.setAdapter(new PlacesAutoCompleteAdapter(AddTripFragment1.super.getContext(),android.R.layout.simple_list_item_1));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip_fragment1, container, false);
        setup(view,savedInstanceState);
        return view;
    }

}
