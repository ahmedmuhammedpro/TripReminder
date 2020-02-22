package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.viewmodel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_ID_TAG="userID";
    Button loginButton;
    EditText emailEditText,passwordEditText;
    LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();
    }

    private void setup(){
        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
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
        else{
            Toast.makeText(this, "Please enter email and password before login", Toast.LENGTH_SHORT).show();
        }
    }
}
