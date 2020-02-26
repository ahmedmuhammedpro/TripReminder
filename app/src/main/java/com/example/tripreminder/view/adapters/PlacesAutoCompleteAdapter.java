package com.example.tripreminder.view.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.tripreminder.model.PlaceApi;
import java.util.ArrayList;
import java.util.HashMap;


public class PlacesAutoCompleteAdapter extends ArrayAdapter implements Filterable {

   private ArrayList<String> placesDescriptions;
   private ArrayList<String> placesIds;
   private HashMap<Integer,ArrayList<String>> placesData = new HashMap<>();
   private int resource;
   private Context context;
   private double lat ,lng;
   private PlaceApi placeApi = new PlaceApi();

    public PlacesAutoCompleteAdapter(Context context, int resId) {
        super(context, resId);
        this.context = context;
        this.resource = resId;
    }

    @Override
    public int getCount() {
        return placesDescriptions.size();
    }

    @Override
    public String getItem(int pos) {
        return placesDescriptions.get(pos);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    placesData = placeApi.autoComplete(constraint.toString());
                    placesDescriptions = placesData.get(0);
                    placesIds = placesData.get(1);
                    filterResults.values = placesDescriptions;
                    filterResults.count = placesDescriptions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        };
        return filter;
    }
   public ArrayList<String> getplacesIds(){
        return placesIds;
   }
}