package com.example.tripreminder.view.fragments;


import android.os.Bundle;
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
import com.example.tripreminder.viewmodel.RegisterViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    EditText emailEditText,passwordEditText,nameEditText,confirmPasswordEditText;
    View view;
    Button registerButton,loginButton;

    RegisterViewModel registerViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        viewSetup();
        return view;
    }
    private void viewSetup(){
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        loginButton = view.findViewById(R.id.loginButton);
        emailEditText = view.findViewById(R.id.emailEditTextRegister);
        passwordEditText = view.findViewById(R.id.passwordEditTextRegister);
        nameEditText = view.findViewById(R.id.nameEditTextRegister);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditTextRegister);
        registerButton = view.findViewById(R.id.registerButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if(validateInput(email,name,password,confirmPassword)){
                    User user = new User();
                    user.setEmail(email);
                    user.setUsername(name);
                    user.setPassword(password);

                    //if inputs are valid then register user
                    registerUser(user);
                }


            }
        });

    }
    private boolean validateInput(String email,String name,String password,String confirmPassword){

        if(email.equals("") || password.equals("")||name.equals("")||confirmPassword.equals("")){
            Toast.makeText(getActivity(), "You must enter all fields first and shouldn't have spaces", Toast.LENGTH_SHORT).show();
        }
        else{
            if(confirmPassword.equals(password)){
                return true;
            }
            else{
                Toast.makeText(getActivity(), "Confirmation password and password are not matched", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private void registerUser(User user){
        registerViewModel.registerUser(user).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user.getUserId().equals("-1")){
                    //username here will be the message of an error occurred
                    Toast.makeText(getActivity(), user.getUsername(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "registered successfully", Toast.LENGTH_SHORT).show();
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                }
            }
        });
    }
}
