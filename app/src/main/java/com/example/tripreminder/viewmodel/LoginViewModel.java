package com.example.tripreminder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.repositories.UserRepositoryImp;

public class LoginViewModel extends ViewModel {

    UserRepositoryImp userRepositoryImp = new UserRepositoryImp();

    public MutableLiveData<User> login(User user){

        return userRepositoryImp.login(user);
    }

    public void registerIfNewGoogleAccount(User user){
        userRepositoryImp.registerGoogleUSer(user);
    }
}
