package com.example.tripreminder.view.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tripreminder.R;
import com.example.tripreminder.databinding.FragmentAddTripFragment1Binding;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.example.tripreminder.model.PlaceApi;
import com.example.tripreminder.utils.Constants;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment1 extends Fragment {
    public static final String TRIP_Object = "trip";
    public static final String TIME_FORMAT_1 = "hh:mm a";
    public static final String DATE_FORMAT_1 = "dd-MMM-yyyy";

    private MainActivity.SaveAndTripInterface mInterface;
    private int mYear, mMonth, mDay, mHour, mMinute,tripType=1,countData=0;
    private  Trip trip , mainTrip;
    private String dateString,dateStringRound,timeAlone,dateAlone,timeAloneRound,dateAloneRound;
    private  boolean isWrongTime = false;
    private Calendar chosenTime = Calendar.getInstance();
    private Calendar chosenTimeRound =Calendar.getInstance();
    Bundle bundleMainFragment;
    FragmentAddTripFragment1Binding binding ;
    public AddTripFragment1() {
        // Required empty public constructor
    }

    public void setmInterface(MainActivity.SaveAndTripInterface mInterface) {
        this.mInterface = mInterface;
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

    private void setupView() {
        trip = new Trip();
        binding.dateTxt.setKeyListener(null);
        binding.timeTxt.setKeyListener(null);
        binding.dateTxtRound.setKeyListener(null);
        binding.timeTxtRound.setKeyListener(null);
    }
    private void setupAdapters(){
        PlacesAutoCompleteAdapter adapterStart = new PlacesAutoCompleteAdapter(AddTripFragment1.super.getContext(), android.R.layout.simple_list_item_1);
        binding.startPointTxt.setAdapter(adapterStart);
        binding.startPointTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setLocation(adapterStart,i,1);
                countData +=1;
            }
        });
        PlacesAutoCompleteAdapter adapterEnd = new PlacesAutoCompleteAdapter(AddTripFragment1.super.getContext(), android.R.layout.simple_list_item_1);
        binding.endPointTxt.setAdapter(adapterEnd);
        binding.endPointTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setLocation(adapterEnd,i,2);
                countData +=1;
            }
        });
    }
    private void setupListeners(){
        binding.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                c.setTime(getCurrentDate());
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            c.set(year, monthOfYear, dayOfMonth);
                            chosenTime.set(year,monthOfYear,dayOfMonth);
                            String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                            binding.dateTxt.setText(date);
                            binding.timeBtn.setEnabled(true);
                            monthOfYear += 1;
                           if(mainTrip == null) {
                               dateAlone = "" + dayOfMonth + "-" + monthOfYear + "-" + year;
                           }else{
                               String[] time = dateString.split("-");
                               dateString = dayOfMonth+"-"+monthOfYear+"-"+year+"-"+time[3]+"-"+time[4];
                           }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        binding.timeBtn.setOnClickListener(view -> {
            // Get Current Time
            if ( binding.dateTxt.getText().length() == 0) {
                Toast.makeText(getContext(), "you must set Date First", Toast.LENGTH_SHORT).show();
                binding.dateBtn.performClick();
            } else {
                final Calendar c = Calendar.getInstance();
                c.setTime(getCurrentTime());
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (view12, hourOfDay, minute) -> {
                            chosenTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            chosenTime.set(Calendar.MINUTE, minute);
                            if (c.getTimeInMillis() < chosenTime.getTimeInMillis()) {
                                binding.timeTxt.setText(hourOfDay + ":" + minute);
                                if (mainTrip == null) {
                                    timeAlone = "-" + hourOfDay + "-" + minute;
                                } else {
                                    String[] date = dateString.split("-");
                                    dateString = date[0] + "-" + date[1] + "-" + date[2] + "-" + hourOfDay + "-" + minute;
                                }
                            } else {
                                isWrongTime = true;
                                Toast.makeText(getContext(), "pick up time after now!", Toast.LENGTH_LONG).show();
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();

            }
        });
        binding.chipGroupTripTypes.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {
                // Handle the checked chip change.
                           if( binding.trip1.isChecked()) {
                               tripType = 1;
                           }
                           if( binding.trip2.isChecked()) {
                               tripType = 2;
                               binding.roundLayoutDate.setVisibility(View.VISIBLE);
                           }else {
                               binding.roundLayoutDate.setVisibility(View.GONE);
                           }
                            trip.setTripType(tripType);
                }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainTrip == null) {
                    //add new trip
                    if ((!TextUtils.isEmpty(binding.tripNameTxt.getText()) && !TextUtils.isEmpty(binding.timeTxt.getText()) && !TextUtils.isEmpty(binding.dateTxt.getText())  )&&
                            ( tripType == 1 || (tripType == 2 && !TextUtils.isEmpty(binding.timeTxtRound.getText()) && !TextUtils.isEmpty(binding.dateTxtRound.getText())  ))) {
                      if(countData >= 2) {
                          dateString = dateAlone + timeAlone;
                          trip.setTripStatus(Trip.UPCOMING);
                          trip.setTripName(binding.tripNameTxt.getText().toString());
                          trip.setTripDate(dateString);
                          trip.getStartLocation().setLocationName(binding.startPointTxt.getText().toString());
                          trip.getEndLocation().setLocationName(binding.endPointTxt.getText().toString());
                          AddTripFragment2 ftwo = new AddTripFragment2();
                          ftwo.setmInterface(mInterface);
                          FragmentManager manager = AddTripFragment1.super.getActivity().getSupportFragmentManager();
                          Bundle bundle = new Bundle();
                          bundle.putSerializable(TRIP_Object, trip);
                          if (dateAloneRound != null && timeAloneRound != null)
                              dateStringRound = dateAloneRound + timeAloneRound;
                          bundle.putString("tripRound", dateStringRound);
                          ftwo.setArguments(bundle);
                          manager.beginTransaction().replace(R.id.container, ftwo, "addTripFragment2").addToBackStack(null).commit();
                      }else{
                          Toast.makeText(getContext(), "You should pick up a Right Place from list!", Toast.LENGTH_SHORT).show();
                      }
                    }else{
                        Toast.makeText(getContext(), "You should fill all data first!", Toast.LENGTH_SHORT).show();
                    }
                    //edit trip
                }else{
                    AddTripFragment2 ftwo = new AddTripFragment2();
                    ftwo.setmInterface(mInterface);
                    FragmentManager manager = AddTripFragment1.super.getActivity().getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    mainTrip.setTripStatus(Trip.UPCOMING);
                    mainTrip.setTripName( binding.tripNameTxt.getText().toString());
                    mainTrip.setTripDate(dateString);
                    mainTrip.setTripType(mainTrip.getTripType());
                    mainTrip.getStartLocation().setLocationName( binding.startPointTxt.getText().toString());
                    mainTrip.getEndLocation().setLocationName( binding.endPointTxt.getText().toString());
                    bundle.putSerializable(TRIP_Object, mainTrip);
                    bundle.putString("tripRound",dateStringRound);
                    ftwo.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.container, ftwo, "addTripFragment2").addToBackStack(null).commit();
                }
            }
        });
        binding.dateBtnRound.setOnClickListener(view -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            if (dateAlone != null && timeAlone != null) {
                Date date = getDate(dateAlone+timeAlone);
                c.setTime(date);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH );
                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripFragment1.super.getContext(),
                        (view14, year, monthOfYear, dayOfMonth) -> {
                            c.set(year, monthOfYear, dayOfMonth);
                            chosenTimeRound.set(year, monthOfYear, dayOfMonth);
                            String date1 = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                            binding.dateTxtRound.setText(date1);
                            monthOfYear += 1;
                            if (mainTrip == null) {
                                dateAloneRound = "" + dayOfMonth + "-" + monthOfYear + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(date.getTime() );
                datePickerDialog.show();

            }else{
                Toast.makeText(getContext(), "you must set one way date first !", Toast.LENGTH_SHORT).show();
            }
        });
        binding.timeBtnRound.setOnClickListener(view -> {
            if ( binding.dateTxtRound.getText().length() == 0) {
                Toast.makeText(getContext(), "you must set Date First", Toast.LENGTH_SHORT).show();
                binding.dateBtnRound.performClick();
            } else {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripFragment1.super.getContext(),
                        (view13, hourOfDay, minute) -> {
                            chosenTimeRound.set(Calendar.HOUR_OF_DAY,hourOfDay);
                             chosenTimeRound.set(Calendar.MINUTE,minute);
                            if (chosenTime.getTimeInMillis() < chosenTimeRound.getTimeInMillis()) {
                                binding.timeTxtRound.setText(hourOfDay + ":" + minute);
                                if (mainTrip == null) {
                                    timeAloneRound = "-" + hourOfDay + "-" + minute;
                                }
                            }else{
                                Toast.makeText(getContext(), "you must set time after one way trip time!", Toast.LENGTH_SHORT).show();
                            }
                        }, chosenTime.get(Calendar.HOUR_OF_DAY),chosenTime.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        // Inflate the layout for this fragment
        binding = FragmentAddTripFragment1Binding.inflate(inflater);
        setupView();
        if(savedInstanceState != null){
            Trip trip = (Trip) savedInstanceState.getSerializable(TRIP_Object);
            if(trip != null)
               setUIWithTripData(trip);
        }

        bundleMainFragment = getArguments();
      if (bundleMainFragment != null) {
        boolean[] per = bundleMainFragment.getBooleanArray(Constants.PERMISSIONS_STATUS);
          //check permissions
        if(per != null) {
            boolean location = per[0];
            boolean overlay = bundleMainFragment.getBooleanArray(Constants.PERMISSIONS_STATUS)[1];
            if ((location && overlay) == false || (location == false && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("You should allow all permissions first, so you can add your first trip!");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mInterface.isClicked();
                    }
                });
                alertDialog.show();
            }
        }
          //if edit clicked
            mainTrip = (Trip) bundleMainFragment.getSerializable(MainFragment.TRIP_Object_FROM_MAIN);
            if (mainTrip != null) {
                setUIWithTripData(mainTrip);
                binding.chipGroupTripTypes.setEnabled(false);
                binding.chipGroupTripTypes.setActivated(false);
                binding.chipGroupTripTypes.setOnKeyListener(null);
                binding.trip1.setKeyListener(null);
                binding.trip2.setKeyListener(null);
                binding.nextBtn.setEnabled(true);
            }
      }
        setupAdapters();
        setupListeners();
        return binding.getRoot();
    }
    public void setLocation(PlacesAutoCompleteAdapter adapter , int pos, int type){
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
    // for edit trip
  private Date  getDate(String dateString){
      String[] strings = dateString.split("-");
      int day = Integer.parseInt(strings[0]);
      int month = Integer.parseInt(strings[1])-1;
      int year = Integer.parseInt(strings[2]);
      Calendar c = Calendar.getInstance();
      c.set(year, month, day);
      return c.getTime();

  }

    private String  getTime(String dateString){
        String[] strings = dateString.split("-");
        int hour = Integer.parseInt(strings[3]);
        int minute = Integer.parseInt(strings[4]);
        return hour+":"+minute;

    }
private void setUIWithTripData (Trip myTrip){
    binding.tripNameTxt.setText(myTrip.getTripName());
    binding.startPointTxt.setText(myTrip.getStartLocation().getLocationName());
    binding.endPointTxt.setText(myTrip.getEndLocation().getLocationName());
    binding.dateTxt.setText(DateFormat.getDateInstance(DateFormat.FULL).format(getDate(myTrip.getTripDate())));
    binding.timeTxt.setText(getTime(myTrip.getTripDate()));
    dateString = myTrip.getTripDate();
    int type = myTrip.getTripType() ;
    if(type == 1){
        binding.trip1.setChecked(true);
    }else if(type == 2){
        binding.trip2.setChecked(true);
    }
}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TRIP_Object, trip);
        outState.putString("tripRound",dateStringRound);
    }
}
