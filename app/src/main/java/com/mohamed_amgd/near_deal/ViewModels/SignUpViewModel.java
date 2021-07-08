package com.mohamed_amgd.near_deal.ViewModels;

import android.app.Application;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.Views.Fragments.SignInFragment;
import com.mohamed_amgd.near_deal.Views.Fragments.UserInfoFragment;
import com.mohamed_amgd.near_deal.repo.ErrorHandler;
import com.mohamed_amgd.near_deal.repo.Repository;
import com.mohamed_amgd.near_deal.repo.RepositoryResult;
import com.mohamed_amgd.near_deal.repo.Util;

public class SignUpViewModel extends AndroidViewModel {
    private FragmentManager mFragmentManager;
    public MutableLiveData<ErrorHandler.Error> mError;

    public SignUpViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
        mError = new MutableLiveData<>();
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
            , TextView birthdateEditText) {
        boolean inputError = false;

        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String birthdate = birthdateEditText.getText().toString();
        if (!Util.getInstance().isEmailValid(email)) {
            inputError = true;
            emailEditText.setError(getApplication().getString(R.string.email_input_error));
        }
        if (!Util.getInstance().isUsernameValid(username)) {
            inputError = true;
            usernameEditText.setError(getApplication().getString(R.string.username_input_error));
        }
        if (!Util.getInstance().isPasswordValid(password)) {
            inputError = true;
            passwordEditText.setError(getApplication().getString(R.string.password_input_error));
        }
        if (!Util.getInstance().isConfirmPasswordValid(password, confirmPassword)) {
            inputError = true;
            confirmPasswordEditText.setError(getApplication().getString(R.string.confirm_password_input_error));
        }
        if (!Util.getInstance().isBirthdateValid(birthdate)) {
            inputError = true;
            birthdateEditText.setError(getApplication().getString(R.string.birthdate_input_error));
        }
        if (inputError) return;

        RepositoryResult<Boolean> result =
                Repository.getInstance().createUser(email, username, password, birthdate);
        result.getIsLoadingLiveData().observeForever(aBoolean -> {
            if (result.isFinishedSuccessfully()) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, new UserInfoFragment());
                transaction.commit();
            } else if (result.isFinishedWithError()) {
                mError.setValue(new ErrorHandler.Error(result.getErrorCode()
                        , v -> {
                    setSignUpAction(emailEditText
                            , usernameEditText
                            , passwordEditText
                            , confirmPasswordEditText
                            , birthdateEditText);
                }));
            } else {
                // TODO: 6/2/2021 show loading
                Toast.makeText(getApplication()
                        , "Loading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showError(View view, ErrorHandler.Error error) {
        ErrorHandler.getInstance().showError(view, error);
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