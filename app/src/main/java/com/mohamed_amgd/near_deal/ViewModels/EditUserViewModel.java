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

import com.bumptech.glide.Glide;
import com.mohamed_amgd.near_deal.Models.User;
import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.Views.Fragments.SignUpFragment;
import com.mohamed_amgd.near_deal.Views.Fragments.UserInfoFragment;
import com.mohamed_amgd.near_deal.repo.ErrorHandler;
import com.mohamed_amgd.near_deal.repo.Repository;
import com.mohamed_amgd.near_deal.repo.RepositoryResult;
import com.mohamed_amgd.near_deal.repo.Util;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserViewModel extends AndroidViewModel {
    public MutableLiveData<User> mUser;
    public MutableLiveData<ErrorHandler.Error> mError;

    private String mUserImagePath;
    private FragmentManager mFragmentManager;

    public EditUserViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
        mError = new MutableLiveData<>();
        mUserImagePath = null;
        mUser = new MutableLiveData<>();
        initUserInfoResult();
    }

    private void initUserInfoResult() {
        RepositoryResult<User> result = Repository.getInstance().getUser();
        result.getIsLoadingLiveData().observeForever(isLoading -> {
            if (result.isFinishedSuccessfully()) {
                mUser.setValue(result.getData().getValue());
            } else if (result.isFinishedWithError()) {
                if (result.getErrorCode() == ErrorHandler.NEED_SIGN_IN_ERROR) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_layout, new SignUpFragment());
                    transaction.commit();
                } else {
                    mError.setValue(new ErrorHandler.Error(result.getErrorCode()
                            , v -> {
                        initUserInfoResult();
                    }));
                }
            } else {
                // TODO: 6/2/2021 show loading
                Toast.makeText(getApplication()
                        , "Loading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initUserImage(CircleImageView userImageView) {
        if (mUser.getValue().getImageUrl() != null) {
            Glide.with(userImageView.getContext()).load(mUser.getValue().getImageUrl()).into(userImageView);
        }
    }

    public void initEmail(TextView email) {
        email.setText(mUser.getValue().getEmail());
    }

    public void initUsername(TextView username) {
        username.setText(mUser.getValue().getUsername());
    }

    public void initBirthdate(TextView birthdate) {
        birthdate.setText(mUser.getValue().getBirthdate());
    }

    public void changePhotoAction(String imagePath) {
        mUserImagePath = imagePath;
    }

    public void confirmAction(EditText emailEditText
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
                Repository.getInstance().updateUser(email, username, password, birthdate);
        result.getIsLoadingLiveData().observeForever(isLoading -> {
            if (result.isFinishedSuccessfully()) {
                if (mUserImagePath != null) {
                    updateUserImage();
                } else {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_layout, new UserInfoFragment());
                    transaction.commit();
                }

            } else if (result.isFinishedWithError()) {
                mError.setValue(new ErrorHandler.Error(result.getErrorCode()
                        , v -> {
                    confirmAction(emailEditText
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

    private void updateUserImage() {
        RepositoryResult<Boolean> updateUserImageResult =
                Repository.getInstance()
                        .updateUserImage(getApplication(), mUserImagePath);
        updateUserImageResult.getIsLoadingLiveData().observeForever(isLoadingUpdateUserImage -> {
            if (updateUserImageResult.isFinishedSuccessfully()) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, new UserInfoFragment());
                transaction.commit();
            } else if (updateUserImageResult.isFinishedWithError()) {
                mError.setValue(new ErrorHandler.Error(updateUserImageResult.getErrorCode()
                        , v -> {
                    updateUserImage();
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
            return (T) new EditUserViewModel(mApplication, mFragmentManager);
        }
    }
}