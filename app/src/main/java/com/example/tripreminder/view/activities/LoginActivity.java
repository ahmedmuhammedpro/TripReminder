package com.example.tripreminder.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.viewmodel.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final String SHARED_PREFERENCES_FILE_NAME="loggedInUserInfo",EMAIL_KEY="email",PASSWORD_KEY="password";
    private final String lOGGED_IN_KEY="loggedIn";
    boolean loggedIn=false;
    public static final String USER_ID_TAG="userID";
    private static final int RC_SIGN_IN = 1;
    Button loginButton;
    SignInButton googleSignInButton;
    EditText emailEditText,passwordEditText;
    LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();
    }




    private void setup(){
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        readFromSharedPreferences();

        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        googleSignInButton = findViewById(R.id.googleSignInButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                login(email,password);
            }
        });
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

    }

    private void googleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            User user = new User();
            user.setEmail(account.getEmail());
            user.setUserId(account.getId());
            loginViewModel.registerIfNewGoogleAccount(user);
            updateUI(account,user);
        } catch (ApiException e) {
            updateUI(null,null);
        }
    }

    private void updateUI(GoogleSignInAccount account,User user) {

        if(account!=null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER_ID_TAG, user.getUserId());
            startActivity(intent);
        }
    }

    private void login(String email,String password){


        if(!email.trim().equals("") && !password.trim().equals("")) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            loginViewModel.login(user).observe(this, loggedInUser -> {

                if (loggedInUser != null) {
                    if(loggedInUser.getUserId().equals("-1")){
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();

                    }else {

                        if(!loggedIn) {
                            writeInSharedPreferences(loggedInUser);
                        }
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra(USER_ID_TAG, loggedInUser.getUserId());
                        startActivity(intent);
                    }

                }
            });
        }
        else{
            Toast.makeText(this, "Please enter email and password before login", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeInSharedPreferences(User loggedInUser) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_KEY,loggedInUser.getEmail());
        editor.putString(PASSWORD_KEY,loggedInUser.getPassword());
        editor.putBoolean(lOGGED_IN_KEY,true);
        editor.commit();
    }

    private void readFromSharedPreferences(){

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(lOGGED_IN_KEY,false);
        if(loggedIn) {
            String email = sharedPreferences.getString(EMAIL_KEY, "empty");
            String password = sharedPreferences.getString(PASSWORD_KEY, "empty");
            login(email,password);
        }
    }

}
