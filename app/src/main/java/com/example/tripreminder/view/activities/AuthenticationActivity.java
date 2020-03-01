package com.example.tripreminder.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.tripreminder.R;
import com.example.tripreminder.view.fragments.LoginFragment;
import com.example.tripreminder.view.fragments.RegisterFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class AuthenticationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authenticationFrameLayout, new LoginFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //handleSignInResult(task);
            if (getSupportFragmentManager().getFragments().get(0) instanceof LoginFragment){
                ((LoginFragment)getSupportFragmentManager().getFragments().get(0)).handleSignInResult(task);
            }
        }
    }

    @Override
    public void onBackPressed() {
       /* if (getSupportFragmentManager().getFragments().get(0) instanceof LoginFragment){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fragment_enter_left_to_right,R.anim.fragment_exit_to_right)
                    .replace(R.id.authenticationFrameLayout, new RegisterFragment()).commit();
        }
        else */
       if (getSupportFragmentManager().getFragments().get(0) instanceof RegisterFragment){

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fragment_enter_left_to_right,R.anim.fragment_exit_to_right)
                    .replace(R.id.authenticationFrameLayout, new LoginFragment()).commit();
        }
    }
}
