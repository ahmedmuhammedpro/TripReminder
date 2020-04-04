package com.example.tripreminder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.repositories.UserRepositoryImp;

public class ProfileViewModel extends ViewModel {

    UserRepositoryImp userRepositoryImp = UserRepositoryImp.getInstance();

    public MutableLiveData<User> signout(){

        return userRepositoryImp.signout();
    }
}
