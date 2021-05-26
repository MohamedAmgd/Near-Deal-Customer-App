package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;
import android.net.Uri;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mohamed_amgd.ayzeh.Models.User;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Fragments.SignUpFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.UserInfoFragment;
import com.mohamed_amgd.ayzeh.repo.Repository;
import com.mohamed_amgd.ayzeh.repo.Util;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserViewModel extends AndroidViewModel {
    public MutableLiveData<User> mUser;
    private Uri mUserImageUri;
    private FragmentManager mFragmentManager;

    public EditUserViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
        mUser = new MutableLiveData<>();
        mUser = Repository.getInstance().getUser();
        if (mUser.getValue() == null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_layout, new SignUpFragment());
            transaction.commit();
        }
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

    public void changePhotoAction(Uri data) {
        mUserImageUri = data;
    }

    public void confirmAction(EditText emailEditText, EditText usernameEditText, EditText passwordEditText, EditText confirmPasswordEditText, EditText birthdateEditText) {
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

        MutableLiveData<Boolean> userImageStatus =
                Repository.getInstance().updateUserImage(mUserImageUri);
        userImageStatus.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    MutableLiveData<Boolean> status =
                            Repository.getInstance().updateUser(email, username, password, birthdate);
                    status.observeForever(new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean) {
                                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                                transaction.replace(R.id.fragment_layout, new UserInfoFragment());
                                transaction.commit();
                            } else {
                                // TODO: 5/26/2021  show cannot update user error
                            }
                        }
                    });
                } else {
                    // TODO: 5/26/2021  show cannot update user's image error
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
            return (T) new EditUserViewModel(mApplication, mFragmentManager);
        }
    }
}