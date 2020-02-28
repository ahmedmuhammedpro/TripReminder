package com.example.tripreminder.model.Firestore;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class UserFirestoreHandler {

    FirebaseAuth auth= FirebaseAuth.getInstance();;
    private final String USERNAME_KEY="username",PASSWORD_KEY="password",EMAIL_KEY="email";
    private final String TAG="fireBase";
    MutableLiveData<List<User>> userLiveDataList = new MutableLiveData<>();
    private FirebaseFirestore dbFirestoreInstance = FirebaseFirestore.getInstance();

    public LiveData<List<User>> getUsersList(){
        dbFirestoreInstance.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                List<User>usersList = new ArrayList<>();
                for (QueryDocumentSnapshot document : documentSnapshot) {
                   /* User user = document.toObject(User.class);
                    usersList.add(user);*/

                   String username = document.getId();
                   String password = String.valueOf(document.getData().get(PASSWORD_KEY));
                    User user = new User(username,password);
                    usersList.add(user);
                }
                userLiveDataList.postValue(usersList);
            }

        });

        return userLiveDataList;
    }

    public void deleteUser(String username){

        dbFirestoreInstance.collection("users").document(username).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.i(TAG, "deleted successfully");
                else
                    Log.i(TAG, "error in deleting");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "error in deleting");
            }
        });
    }


    public void updateUser(String username,String password){

        HashMap<String,Object> newNode = new HashMap<>();
        newNode.put(PASSWORD_KEY,password);

        dbFirestoreInstance.collection("users").document(username).set(newNode).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "error in updating");
            }
        });
    }

    public MutableLiveData<User> register(User user){

        MutableLiveData<User> userRegistered = new MutableLiveData<>();
        if(isUserRegisteredBefore(user)){
            user.setUserId("-1");
            userRegistered.postValue(user);
            return userRegistered;
        }
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(authTask->{
            if(authTask.isSuccessful()){
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if(firebaseUser!=null){
                    user.setUserId(firebaseUser.getUid());
                    userRegistered.postValue(user);
                    dbFirestoreInstance.collection("users").document(user.getUserId()).set(user);
                }
            }else{
                logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
            }
        });
        return userRegistered;
    }

    public MutableLiveData<User> login(User user){
        MutableLiveData<User> userLoggedIn = new MutableLiveData<>();
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(authTask->{
            if(authTask.isSuccessful()){
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if(firebaseUser!=null){
                    user.setUserId(firebaseUser.getUid());

                }
            }else{
                user.setUserId("-1");
                logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
            }
            userLoggedIn.postValue(user);
        });
        return userLoggedIn;
    }
    private void logErrorMessage(String error){
        Log.d("Error", error);
    }


    private boolean isUserRegisteredBefore(User user){
        final boolean[] result = {false};

        return result[0];
    }

    public  MutableLiveData<User> registerGoogleUSer(User user){
        MutableLiveData<User> userRegistered = new MutableLiveData<>();
        //check if user is registered before
        dbFirestoreInstance.collection("users").whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getDocuments().size() == 0) {
                        //if not registered
                        HashMap<String, Object> newNode = new HashMap<>();
                        newNode.put(EMAIL_KEY, user.getEmail());
                        newNode.put(USERNAME_KEY, user.getUsername());
                        dbFirestoreInstance.collection("users").document(user.getUserId()).set(newNode).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i(TAG, "Added successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "error in Adding");
                                user.setUserId("-1");
                            }
                        });
                    }
                    else {
                        Log.i("Error", "Already registered");
                    }
                }
            }
        });

        return userRegistered;
    }
}
