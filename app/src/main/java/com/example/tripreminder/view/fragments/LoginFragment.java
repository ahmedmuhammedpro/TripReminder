package com.example.tripreminder.view.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.SharedPreferencesHandler;
import com.example.tripreminder.view.activities.MainActivity;
import com.example.tripreminder.viewmodel.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    boolean loggedIn=false;
    Button signUpButton;
    public static final String USER_ID_TAG="userID";
    private static final int RC_SIGN_IN = 1;
    Button loginButton;
    SignInButton googleSignInButton;
    EditText emailEditText,passwordEditText;
    LoginViewModel loginViewModel;
    Button forgotPasswordButton;
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
        signUpButton = view.findViewById(R.id.signUpButton);
        loginButton = view.findViewById(R.id.loginButton);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        googleSignInButton = view.findViewById(R.id.googleSignInButton);
        forgotPasswordButton = view.findViewById(R.id.forgetPasswordBtn);
        setClickListners();

    }

    private void setClickListners(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter_right_to_left,R.anim.fragment_exit_to_left)
                        .replace(R.id.authenticationFrameLayout, new RegisterFragment()).addToBackStack(null).commit();
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

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if(email.equals("")){
                    Toast.makeText(getActivity(), "Please enter email first", Toast.LENGTH_SHORT).show();
                }
                else{
                    resetPassword(email);
                }
            }
        });
    }
    private void googleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(String.valueOf(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //firebaseAuthWithGoogle(account);
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
           String test = account.getIdToken();
            String test2 = account.getId();
            // Signed in successfully, show authenticated UI.
            User user = new User();
            user.setEmail(account.getEmail());
            user.setUserId(account.getId());
            user.setUsername(account.getDisplayName());

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getId(), null);
            loginViewModel.registerIfNewGoogleAccount(user,account);
            updateUI(account,user);
        } catch (ApiException e) {
            updateUI(null,null);
        }
    }

    private void updateUI(GoogleSignInAccount account,User user) {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        if(account!=null) {
            if(account.getPhotoUrl()!=null){
                writeInSharedPreferences(user,account.getPhotoUrl().toString());
                intent.putExtra(Constants.IMAGE_URL,account.getPhotoUrl().toString());
            }else{
                writeInSharedPreferences(user,"");
            }


            SharedPreferencesHandler.getInstance().readFromSharedPreferences(getActivity());

            intent.putExtra(Constants.EMAIL_KEY, user.getEmail());
            intent.putExtra(Constants.USERNAME_KEY, user.getUsername());
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
                            getUserData(loggedInUser.getUserId());
                        }
                       else {
                            SharedPreferencesHandler.getInstance().readFromSharedPreferences(getActivity());
                            Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra(USER_ID_TAG, loggedInUser.getUserId());
                            startActivity(intent);
                        }
                    }
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Please enter email and password before login and remove spaces if found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserData(String userId) {
        loginViewModel.getUserData(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(!user.getUserId().equals("-1")) {
                    writeInSharedPreferences(user, "empty");
                    SharedPreferencesHandler.getInstance().readFromSharedPreferences(getActivity());
                    Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(USER_ID_TAG, user.getUserId());
                    startActivity(intent);
                }
            }
        });
    }

    private void writeInSharedPreferences(User loggedInUser,String imageUrl) {
        SharedPreferencesHandler.getInstance().writeInSharedPreferences(loggedInUser,imageUrl);
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
