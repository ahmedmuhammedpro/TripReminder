package com.example.tripreminder.model.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.Firestore.UserFirestoreHandler;

import java.util.List;


public class UserRepositoryImp {

UserFirestoreHandler userFirestoreHandler = new UserFirestoreHandler();

    public LiveData<List<User>> getUsersList(){
        return userFirestoreHandler.getUsersList();
    }


    public void deleteUser(String username){
        userFirestoreHandler.deleteUser(username);
    }

    public void updateUser(String username,String password){
        userFirestoreHandler.updateUser(username,password);
    }

    public  MutableLiveData<User> registerGoogleUSer(User user){
        return userFirestoreHandler.registerGoogleUSer(user);
    }
    public MutableLiveData<User> registerUser(User user){
       return userFirestoreHandler.register(user);
    }

    public MutableLiveData<User> login(User user){
        return userFirestoreHandler.login(user);
    }

    public MutableLiveData<User>  resetPassword(String email){return userFirestoreHandler.resetPassword(email);}


}
