package com.example.tripreminder.view.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.example.tripreminder.model.PlaceApi;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.view.adapters.PlacesAutoCompleteAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment1 extends Fragment {
    public static final String TRIP_Object = "trip";
    public static final String TIME_FORMAT_1 = "hh:mm a";
    public static final String DATE_FORMAT_1 = "dd-MMM-yyyy";
    private ChipGroup tripTypes;
    private Button setDate, setTime ,nextBtn;
    private TextView dateTxt, timeTxt;
    private AutoCompleteTextView startPointTxt, endPointTxt;
    private TextInputEditText nameTxt;
    private int mYear, mMonth, mDay, mHour, mMinute,tripType;
    private  Trip trip;
    String dateString;
    int countData=0;
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

    private void setupView(View view) {
        trip = new Trip();
        tripTypes = view.findViewById(R.id.chipGroupTripTypes);
        nextBtn = view.findViewById(R.id.nextBtn);
        nameTxt = view.findViewById(R.id.tripNameTxt);
        setDate = view.findViewById(R.id.dateBtn);
        setTime = view.findViewById(R.id.timeBtn);
        dateTxt = view.findViewById(R.id.dateTxt);
        timeTxt = view.findViewById(R.id.timeTxt);
        startPointTxt = view.findViewById(R.id.startPointTxt);
        endPointTxt = view.findViewById(R.id.endPointTxt);
        nextBtn.setEnabled(false);
    }
    private void setupAdapters(){
        PlacesAutoCompleteAdapter adapterStart = new PlacesAutoCompleteAdapter(AddTripFragment1.super.getContext(), android.R.layout.simple_list_item_1);
        startPointTxt.setAdapter(adapterStart);
        startPointTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setLocation(adapterStart,i,1);
                countData +=1;
                if ( countData>=5 && !nameTxt.getText().toString().isEmpty()) {
                    nextBtn.setEnabled(true);
                }
            }
        });
        PlacesAutoCompleteAdapter adapterEnd = new PlacesAutoCompleteAdapter(AddTripFragment1.super.getContext(), android.R.layout.simple_list_item_1);
        endPointTxt.setAdapter(adapterEnd);
        endPointTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setLocation(adapterEnd,i,2);
                countData +=1;
                if ( countData>=5 && !nameTxt.getText().toString().isEmpty()) {
                    nextBtn.setEnabled(true);
                }
            }
        });
    }
    private void setupListeners(){
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
                                c.set(year, monthOfYear, dayOfMonth);
                                String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                                dateTxt.setText(date);
                                monthOfYear += 1;
                                dateString = "" +dayOfMonth + "-" + monthOfYear + "-" + year ;
                                countData +=1;
                                if ( countData>=5 && !nameTxt.getText().toString().isEmpty()) {
                                    nextBtn.setEnabled(true);
                                }
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
                                dateString += "-" + hourOfDay+ "-" + minute;
                                timeTxt.setText(hourOfDay + ":" + minute);
                                countData +=1;
                                if ( countData>=5 && !nameTxt.getText().toString().isEmpty()) {
                                    nextBtn.setEnabled(true);
                                }
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        tripTypes.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {
                // Handle the checked chip change.
                int chipsCount = group.getChildCount();
                if (chipsCount > 0) {
                    int i = 0;
                    while (i < chipsCount) {
                        Chip chip = (Chip) group.getChildAt(i);
                        if (chip.isChecked()) {
                            tripType = i + 1;
                            countData +=1;

                            if ( countData>=5 && !nameTxt.getText().toString().isEmpty()) {
                                nextBtn.setEnabled(true);
                            }
                        }
                        i++;
                    }
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if(nextBtn.isEnabled())  {
                    trip.setTripType(tripType);
                    trip.setTripName(nameTxt.getText().toString());
                    trip.setUserID(MainActivity.userId);
                    trip.setTripDate(dateString);
                    trip.getStartLocation().setLocationName(startPointTxt.getText().toString());
                    trip.getEndLocation().setLocationName(endPointTxt.getText().toString());
                    AddTripFragment2 ftwo = new AddTripFragment2();
                    FragmentManager manager = AddTripFragment1.super.getActivity().getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TRIP_Object, trip);
                    ftwo.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.container, ftwo, "addTripFragment2").commit();
                }
            }
        });


    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip_fragment1, container, false);
        setupView(view);
        setupAdapters();
        setupListeners();
        return view;
    }
    public void setLocation(PlacesAutoCompleteAdapter adapter , int pos,int type){
        String placeId = adapter.getplacesIds().get(pos);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        Places.initialize(getContext(), PlaceApi.API_KEY);
        PlacesClient placesClient = Places.createClient(getContext());
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            LatLng l = place.getLatLng();
           if(type==1) {
               TripLocation loc = new TripLocation(l.latitude, l.longitude);
               trip.setStartLocation(loc);
           }else {
               TripLocation loc = new TripLocation(l.latitude, l.longitude);
               trip.setEndLocation(loc);
           }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                Log.e("place", "Place not found: " + exception.getMessage());
            }
        });
    }
}
