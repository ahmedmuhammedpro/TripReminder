package com.example.tripreminder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.repositories.UserRepositoryImp;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class LoginViewModel extends ViewModel {

    UserRepositoryImp userRepositoryImp = UserRepositoryImp.getInstance();

    public MutableLiveData<User> login(User user){

        return userRepositoryImp.login(user);
    }

    public void registerIfNewGoogleAccount(User user, GoogleSignInAccount account){
        userRepositoryImp.registerGoogleUSer(user,account);
    }

    public MutableLiveData<User>  resetPassword(String email){return userRepositoryImp.resetPassword(email);}

    public MutableLiveData<User> getUserData(String userId){
        return userRepositoryImp.getUserData(userId);
    }
}
