package com.example.tripreminder.view.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.viewmodel.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    TextView newUserTextView;
    private final String SHARED_PREFERENCES_FILE_NAME="loggedInUserInfo",EMAIL_KEY="email",PASSWORD_KEY="password";
    private final String lOGGED_IN_KEY="loggedIn",USER_ID_KEY="UserId",USERNAME_KEY = "username";
    boolean loggedIn=false;
    public static final String USER_ID_TAG="userID";
    private static final int RC_SIGN_IN = 1;
    Button loginButton;
    SignInButton googleSignInButton;
    EditText emailEditText,passwordEditText;
    LoginViewModel loginViewModel;
    View view;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        setup();
        return view;
    }

    private void setup(){

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        newUserTextView = view.findViewById(R.id.newUserTextView);

        loginButton = view.findViewById(R.id.loginButton);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        googleSignInButton = view.findViewById(R.id.googleSignInButton);

        setClickListners();

    }

    private void setClickListners(){
        newUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter_right_to_left,R.anim.fragment_exit_to_left)
                        .replace(R.id.authenticationFrameLayout, new RegisterFragment()).commit();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(email.equals("") || password.equals(""))
                    Toast.makeText(getActivity(), "You must enter all fields first", Toast.LENGTH_SHORT).show();
                else
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
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            User user = new User();
            user.setEmail(account.getEmail());
            user.setUserId(account.getId());
            user.setUsername(account.getDisplayName());
            loginViewModel.registerIfNewGoogleAccount(user);

            updateUI(account,user);
        } catch (ApiException e) {
            updateUI(null,null);
        }
    }

    private void updateUI(GoogleSignInAccount account,User user) {

        if(account!=null) {
            writeInSharedPreferences(user);
            Intent intent = new Intent(getActivity(), MainActivity.class);
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
                        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                    }else {
                        if(!loggedIn) {
                            writeInSharedPreferences(loggedInUser);
                        }
                        Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra(USER_ID_TAG, loggedInUser.getUserId());
                        startActivity(intent);
                    }

                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Please enter email and password before login and remove spaces if found", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeInSharedPreferences(User loggedInUser) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_KEY,loggedInUser.getEmail());
        editor.putString(USERNAME_KEY,loggedInUser.getUsername());
        //editor.putString(PASSWORD_KEY,loggedInUser.getPassword());
        editor.putString(USER_ID_TAG,loggedInUser.getUserId());
        editor.putBoolean(lOGGED_IN_KEY,true);
        editor.commit();
    }

    private  void resetPassword(String email){
        loginViewModel.resetPassword(email).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(!user.getEmail().equals("-1")){
                    Toast.makeText(getActivity(), "Reset password email is sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    //print error message written in userEmail
                    Toast.makeText(getActivity(), user.getUsername(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
