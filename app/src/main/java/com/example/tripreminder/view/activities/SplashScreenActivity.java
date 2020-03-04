package com.example.tripreminder.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.R;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.SharedPreferencesHandler;
import com.example.tripreminder.viewmodel.LoginViewModel;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {


    boolean loggedIn=false;

    private static final int RC_SIGN_IN = 1;
    LoginViewModel loginViewModel;
    String email="",username="",userID="",imageUrl="";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        readFromSharedPreferences();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(loggedIn) {
                    Toast.makeText(SplashScreenActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.putExtra(Constants.USER_ID_TAG, userID);
                    intent.putExtra(Constants.USERNAME_KEY,username);
                    intent.putExtra(Constants.IMAGE_URL,imageUrl);
                    intent.putExtra(Constants.EMAIL_KEY,email);
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private void readFromSharedPreferences(){
        SharedPreferencesHandler.getInstance().readFromSharedPreferences(this);
        SharedPreferencesHandler.getInstance().getUserInfoLiveData().observe(this, new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> userInfoHashMap) {

                loggedIn = (boolean)userInfoHashMap.get(Constants.lOGGED_IN_KEY);
                if(loggedIn) {
                    email = String.valueOf(userInfoHashMap.get(Constants.EMAIL_KEY));
                    userID = String.valueOf(userInfoHashMap.get(Constants.USER_ID_TAG));
                    imageUrl = String.valueOf(userInfoHashMap.get(Constants.IMAGE_URL));
                    username = String.valueOf(userInfoHashMap.get(Constants.USERNAME_KEY));
                }
                handler.sendMessage(new Message());
            }
        });
    }
}
