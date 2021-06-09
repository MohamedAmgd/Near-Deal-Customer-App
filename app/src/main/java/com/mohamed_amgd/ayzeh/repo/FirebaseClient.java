package com.mohamed_amgd.ayzeh.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

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

    public RepositoryResult<Boolean> createNewUser(String email, String password) {
        RepositoryResult<Boolean> result = new RepositoryResult<>(new MutableLiveData<>());
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            if (authResult.getUser() != null) {
                result.setFinishedSuccessfully(true);
            } else {
                result.setFinishedWithError(ErrorHandler.UNKNOWN_SIGN_USER_ERROR);
            }
        }).addOnFailureListener(e -> {
            result.setFinishedWithError(ErrorHandler.FAILED_CREATE_USER_ERROR);
            reportNonFetalCrash(e);
            Log.i(TAG, "createNewUser: " + e.getMessage());
            Log.i(TAG, "createNewUser: " + e);
        });
        return result;
    }

    public RepositoryResult<Boolean> signInUser(String email, String password) {
        RepositoryResult<Boolean> result = new RepositoryResult<>(new MutableLiveData<>());
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            if (authResult.getUser() != null) {
                result.setFinishedSuccessfully(true);
            } else {
                result.setFinishedWithError(ErrorHandler.UNKNOWN_SIGN_USER_ERROR);
            }
        }).addOnFailureListener(e -> {
            result.setFinishedWithError(ErrorHandler.FAILED_SIGN_IN_ERROR);
            reportNonFetalCrash(e);
            Log.i(TAG, "createNewUser: " + e.getMessage());
        });
        return result;
    }

    public RepositoryResult<Boolean> changeUserEmail(String newEmail) {
        RepositoryResult<Boolean> result = new RepositoryResult<>(new MutableLiveData<>());
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().updateEmail(newEmail).addOnSuccessListener((voidItem) -> {
                result.setFinishedSuccessfully(true);
            }).addOnFailureListener(e -> {
                result.setFinishedWithError(ErrorHandler.FAILED_UPDATE_EMAIL_ERROR);
                reportNonFetalCrash(e);
                Log.i(TAG, "createNewUser: " + e.getMessage());
            });
        } else {
            result.setFinishedWithError(ErrorHandler.NEED_SIGN_IN_ERROR);
        }
        return result;
    }

    public RepositoryResult<Boolean> changeUserPassword(String newPassword) {
        RepositoryResult<Boolean> result = new RepositoryResult<>(new MutableLiveData<>());
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().updatePassword(newPassword).addOnSuccessListener((voidItem) -> {
                result.setFinishedSuccessfully(true);
                Log.i(TAG, "updatePassword: " + true);
            }).addOnFailureListener(e -> {
                result.setFinishedWithError(ErrorHandler.FAILED_UPDATE_PASSWORD_ERROR);
                reportNonFetalCrash(e);
                Log.i(TAG, "updatePassword: " + e.getMessage());
            });
        } else {
            result.setFinishedWithError(ErrorHandler.NEED_SIGN_IN_ERROR);
        }
        return result;
    }

    public void signOutUser() {
        mAuth.signOut();
    }

    public void reportNonFetalCrash(Throwable throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable);
    }
}
