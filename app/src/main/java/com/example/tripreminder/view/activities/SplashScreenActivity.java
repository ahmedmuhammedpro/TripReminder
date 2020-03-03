package com.example.tripreminder.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.view.fragments.MainFragment;
import com.example.tripreminder.viewmodel.LoginViewModel;

public class SplashScreenActivity extends AppCompatActivity implements Runnable{


    boolean loggedIn=false;

    private static final int RC_SIGN_IN = 1;
    LoginViewModel loginViewModel;
    String email="",username="",userID="",imageUrl="";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread(this);
        thread.start();
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

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            readFromSharedPreferences();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void readFromSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Constants.lOGGED_IN_KEY,false);
        if(loggedIn) {
            email = sharedPreferences.getString(Constants.EMAIL_KEY, "empty");
            userID = sharedPreferences.getString(Constants.USER_ID_TAG, "empty");
            imageUrl = sharedPreferences.getString(Constants.IMAGE_URL, "empty");
            username = sharedPreferences.getString(Constants.USERNAME_KEY,"empty");
        }
        handler.sendMessage(new Message());
    }
}
