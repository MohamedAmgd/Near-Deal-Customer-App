package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Fragments.SignInFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.UserInfoFragment;
import com.mohamed_amgd.ayzeh.repo.Repository;
import com.mohamed_amgd.ayzeh.repo.Util;

public class SignUpViewModel extends AndroidViewModel {
    private FragmentManager mFragmentManager;

    public SignUpViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
    }

    public void setSignInAction() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, new SignInFragment());
        transaction.commit();

    }

    public void setSignUpAction(EditText emailEditText
            , EditText usernameEditText
            , EditText passwordEditText
            , EditText confirmPasswordEditText
            , EditText birthdateEditText) {

        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String birthdate = birthdateEditText.getText().toString();
        if (!Util.getInstance().isEmailValid(email)) {
            // TODO: 5/26/2021  show email error
        }
        if (!Util.getInstance().isUsernameValid(username)) {
            // TODO: 5/26/2021  show username error
        }
        if (!Util.getInstance().isPasswordValid(password)) {
            // TODO: 5/26/2021  show password error
        }
        if (!Util.getInstance().isConfirmPasswordValid(password, confirmPassword)) {
            // TODO: 5/26/2021  show confirm password error
        }
        if (!Util.getInstance().isBirthdateValid(birthdate)) {
            // TODO: 5/26/2021  show birthdate error
        }
        MutableLiveData<Boolean> status =
                Repository.getInstance().createUser(email, username, password, birthdate);
        status.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_layout, new UserInfoFragment());
                    transaction.commit();
                }else {
                    // TODO: 5/26/2021  show cannot create user error
                }
            }
        });
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        @NonNull
        private final FragmentManager mFragmentManager;


        public Factory(@NonNull Application application, @NonNull FragmentManager fragmentManager) {
            mApplication = application;
            mFragmentManager = fragmentManager;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SignUpViewModel(mApplication, mFragmentManager);
        }
    }
}