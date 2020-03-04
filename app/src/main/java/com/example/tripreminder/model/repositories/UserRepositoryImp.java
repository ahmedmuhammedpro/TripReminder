package com.example.tripreminder.model.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.User;
import com.example.tripreminder.model.Firestore.UserFirestoreHandler;


public class UserRepositoryImp {

UserFirestoreHandler userFirestoreHandler = UserFirestoreHandler.getInstance();



    private static UserRepositoryImp userRepositoryImp =null;
    private UserRepositoryImp(){
    }
    public static UserRepositoryImp getInstance(){
        if(userRepositoryImp == null)
            userRepositoryImp = new UserRepositoryImp();
        return userRepositoryImp;
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

    public MutableLiveData<User> getUserData(String userId){
        return userFirestoreHandler.getUserData(userId);
    }

}
