package com.example.tripreminder.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaceApi {
    public static String API_KEY = "AIzaSyC1zsGsZjjuUCZKtyKwptzBHW5YI3PrCko";
    public HashMap<Integer,ArrayList<String>> autoComplete(String input){

        HashMap<Integer,ArrayList<String>> placesData = new HashMap<>();
        ArrayList<String> placesDescriptionList=new ArrayList();
        ArrayList<String> placesIdsList=new ArrayList();
        HttpURLConnection connection=null;
        StringBuilder jsonResult=new StringBuilder();
        try {
            StringBuilder sb=new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            sb.append("input="+input);
            sb.append("&key="+API_KEY);
            URL url=new URL(sb.toString());
            connection=(HttpURLConnection)url.openConnection();
            InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff=new char[1024];
            while ((read=inputStreamReader.read(buff))!=-1){
                jsonResult.append(buff,0,read);
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(connection!=null){
                connection.disconnect();
            }
        }

        try {
            JSONObject jsonObject=new JSONObject(jsonResult.toString());
            JSONArray prediction=jsonObject.getJSONArray("predictions");
            for(int i=0;i<prediction.length();i++){
                placesDescriptionList.add(prediction.getJSONObject(i).getString("description"));
                placesIdsList.add(prediction.getJSONObject(i).getString("place_id"));
            }
            placesData.put(0,placesDescriptionList);
            placesData.put(1,placesIdsList);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return placesData;
    }
}
