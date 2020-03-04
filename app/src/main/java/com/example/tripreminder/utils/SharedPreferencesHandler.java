package com.example.tripreminder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.User;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SharedPreferencesHandler {

    private WeakReference<Activity> activityContext;

    private MutableLiveData<HashMap<String,Object>> userInfoLiveData = new MutableLiveData<>();


    public MutableLiveData<HashMap<String,Object>> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    private static SharedPreferencesHandler sharedPreferencesHandler =null;
    private SharedPreferencesHandler(){
    }

    public static SharedPreferencesHandler getInstance(){
        if(sharedPreferencesHandler == null)
            sharedPreferencesHandler = new SharedPreferencesHandler();
        return sharedPreferencesHandler;
    }

    public void readFromSharedPreferences(Activity activity){

        activityContext = new WeakReference<>(activity);
        SharedPreferences sharedPreferences = activityContext.get().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        HashMap<String,Object> userInfo = new HashMap<>();

        boolean loggedIn = sharedPreferences.getBoolean(Constants.lOGGED_IN_KEY,false);

        if(loggedIn) {
            userInfo.put(Constants.lOGGED_IN_KEY,sharedPreferences.getBoolean(Constants.lOGGED_IN_KEY,false));
            userInfo.put(Constants.EMAIL_KEY,sharedPreferences.getString(Constants.EMAIL_KEY, "empty"));
            userInfo.put(Constants.USER_ID_TAG, sharedPreferences.getString(Constants.USER_ID_TAG, "empty"));
            userInfo.put(Constants.IMAGE_URL,sharedPreferences.getString(Constants.IMAGE_URL, "empty"));
            userInfo.put(Constants.USERNAME_KEY,sharedPreferences.getString(Constants.USERNAME_KEY,"empty"));
        }
        else{
            userInfo.put(Constants.lOGGED_IN_KEY,sharedPreferences.getBoolean(Constants.lOGGED_IN_KEY,false));
        }

        userInfoLiveData.postValue(userInfo);
    }
    public void writeInSharedPreferences(User loggedInUser, String imageUrl) {
        SharedPreferences sharedPreferences = activityContext.get().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.IMAGE_URL,imageUrl);
        editor.putString(Constants.EMAIL_KEY,loggedInUser.getEmail());
        editor.putString(Constants.USERNAME_KEY,loggedInUser.getUsername());
        editor.putString(Constants.USER_ID_TAG,loggedInUser.getUserId());
        editor.putBoolean(Constants.lOGGED_IN_KEY,true);
        editor.commit();
    }


}
