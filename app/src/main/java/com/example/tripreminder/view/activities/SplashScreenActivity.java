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
import com.example.tripreminder.view.fragments.MainFragment;
import com.example.tripreminder.viewmodel.LoginViewModel;

public class SplashScreenActivity extends AppCompatActivity implements Runnable{

    private final String SHARED_PREFERENCES_FILE_NAME="loggedInUserInfo",EMAIL_KEY="email",PASSWORD_KEY="password";
    private final String lOGGED_IN_KEY="loggedIn";
    boolean loggedIn=false;
    public static final String USER_ID_TAG="userID";
    private static final int RC_SIGN_IN = 1;
    LoginViewModel loginViewModel;
    String email="",password="",userID="";
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
                    intent.putExtra(USER_ID_TAG, userID);
                    startActivity(intent);
                    //login(email, password);
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

        /*SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(lOGGED_IN_KEY,false);
        if(loggedIn) {
            email = sharedPreferences.getString(EMAIL_KEY, "empty");
            password = sharedPreferences.getString(PASSWORD_KEY, "empty");
        }*/

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(lOGGED_IN_KEY,false);
        if(loggedIn) {
            email = sharedPreferences.getString(EMAIL_KEY, "empty");
            password = sharedPreferences.getString(PASSWORD_KEY, "empty");
            userID = sharedPreferences.getString(USER_ID_TAG, "empty");
            //login(email,password);
        /*Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(USER_ID_TAG, userID);
        startActivity(intent);*/
        }
        handler.sendMessage(new Message());
    }


   /* private void login(String email,String password){


        if(!email.trim().equals("") && !password.trim().equals("")) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            loginViewModel.login(user).observe(this, loggedInUser -> {

                if (loggedInUser != null) {
                    if(loggedInUser.getUserId().equals("-1")){
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra(USER_ID_TAG, loggedInUser.getUserId());
                        startActivity(intent);
                    }

                }
            });
        }
    }*/
}
