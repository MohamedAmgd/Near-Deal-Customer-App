package com.mohamed_amgd.ayzeh.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseClient {
    private static final String TAG = "FirebaseClient :";
    private static FirebaseClient mInstance;

    private final FirebaseAuth mAuth;

    private FirebaseClient() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseClient getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseClient();
        }
        return mInstance;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public MutableLiveData<Boolean> createNewUser(String email, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            result.setValue(true);
        }).addOnFailureListener(e -> {
            result.setValue(false);
        });
        return result;
    }

    public MutableLiveData<Boolean> signInUser(String email, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            result.setValue(true);
        }).addOnFailureListener(e -> {
            result.setValue(false);
        });
        return result;
    }

    public MutableLiveData<Boolean> changeUserEmail(String newEmail){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mAuth.getCurrentUser().updateEmail(newEmail).addOnSuccessListener(authResult -> {
            Log.i(TAG, "changeUserEmail true");
            result.setValue(true);
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Log.i(TAG, "changeUserEmail false"+e.getMessage());
            result.setValue(false);
        });
        return result;
    }
    public MutableLiveData<Boolean> changeUserPassword(String newPassword){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mAuth.getCurrentUser().updatePassword(newPassword).addOnSuccessListener(authResult -> {
            result.setValue(true);
        }).addOnFailureListener(e -> {
            result.setValue(false);
        });
        return result;
    }

    public void signOutUser() {
        mAuth.signOut();
    }
}
