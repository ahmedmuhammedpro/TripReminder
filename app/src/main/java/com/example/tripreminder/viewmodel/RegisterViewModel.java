package com.example.tripreminder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.repositories.UserRepositoryImp;

public class RegisterViewModel extends ViewModel {

    UserRepositoryImp userRepositoryImp = new UserRepositoryImp();

    public MutableLiveData<User> registerUser(User user){
        return userRepositoryImp.registerUser(user);
    }
}

