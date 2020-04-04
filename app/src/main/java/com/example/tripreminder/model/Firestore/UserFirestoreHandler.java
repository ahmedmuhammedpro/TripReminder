package com.example.tripreminder.model.Firestore;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;


public class UserFirestoreHandler {

    FirebaseAuth auth= FirebaseAuth.getInstance();;
    private final String USERNAME_KEY="username",PASSWORD_KEY="password",EMAIL_KEY="email";
    private final String TAG="fireBase";
    private FirebaseFirestore dbFirestoreInstance = FirebaseFirestore.getInstance();

    private static UserFirestoreHandler userFirestoreHandler =null;
    private UserFirestoreHandler(){
    }

    public static UserFirestoreHandler getInstance(){
        if(userFirestoreHandler == null)
            userFirestoreHandler = new UserFirestoreHandler();
        return userFirestoreHandler;
    }

    public MutableLiveData<User> register(User user){

        MutableLiveData<User> userRegistered = new MutableLiveData<>();

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(authTask->{
            if(authTask.isSuccessful()){
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if(firebaseUser!=null){
                    user.setUserId(firebaseUser.getUid());
                    userRegistered.postValue(user);
                    dbFirestoreInstance.collection("users").document(user.getUserId()).set(user);
                }
            }else{
                //logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
                user.setUserId("-1");
                user.setUsername(authTask.getException().getMessage());
                userRegistered.postValue(user);
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
                    firebaseUser.getDisplayName();
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



    public  MutableLiveData<User> registerGoogleUSer(User user ,GoogleSignInAccount account){
        MutableLiveData<User> userRegistered = new MutableLiveData<>();
        //check if user is registered before
        dbFirestoreInstance.collection("users").whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    firebaseAuthWithGoogle(account);
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

    public MutableLiveData<User>  resetPassword(String email){
        MutableLiveData<User> forgotPasswordEmailSent = new MutableLiveData<>();
        User user = new User();
        auth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("test", "Email sent.");
                            user.setEmail(email);
                            forgotPasswordEmailSent.postValue(user);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                user.setEmail("-1");
                //return error message in username
                user.setUsername(e.getMessage());
                forgotPasswordEmailSent.postValue(user);
            }
        });
        return forgotPasswordEmailSent;
    }

    public MutableLiveData<User> getUserData(String userId){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        User user = new User();
        dbFirestoreInstance.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    user.setUsername(String.valueOf(task.getResult().get(USERNAME_KEY)));
                    user.setUserId(userId);
                    user.setEmail(String.valueOf(task.getResult().get(EMAIL_KEY)));
                    userMutableLiveData.postValue(user);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                user.setUserId("-1");
                //get error message in username
                user.setUsername(e.getMessage());
                userMutableLiveData.postValue(user);
            }
        });

        return userMutableLiveData;
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(command -> {
                    Log.d(TAG, "firebaseAuthWithGoogle: success" + acct.getId());
                });
    }

    public MutableLiveData<User> signout() {
        auth.signOut();
        User user1 = new User();
        user1.setUserId("1");
        MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(user1);
        return mutableLiveData;
    }
}
