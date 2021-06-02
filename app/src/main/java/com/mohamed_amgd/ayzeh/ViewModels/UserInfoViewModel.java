package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;
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
import com.google.android.material.chip.Chip;
import com.mohamed_amgd.ayzeh.Models.User;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Fragments.EditUserFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.SignUpFragment;
import com.mohamed_amgd.ayzeh.repo.ErrorHandler;
import com.mohamed_amgd.ayzeh.repo.Repository;
import com.mohamed_amgd.ayzeh.repo.RepositoryResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoViewModel extends AndroidViewModel {
    public MutableLiveData<User> mUser;
    private RepositoryResult<User> mRepositoryResult;
    private FragmentManager mFragmentManager;

    public UserInfoViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        mUser = new MutableLiveData<>();
        mFragmentManager = fragmentManager;
        mRepositoryResult = Repository.getInstance().getUser();
        mRepositoryResult.getIsLoadingLiveData().observeForever(isLoading -> {
            if (mRepositoryResult.isFinishedSuccessfully()) {
                mUser.setValue(mRepositoryResult.getData().getValue());
            } else if (mRepositoryResult.isFinishedWithError()) {
                if (mRepositoryResult.getErrorCode() == ErrorHandler.NEED_SIGN_IN_ERROR) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_layout, new SignUpFragment());
                    transaction.commit();
                } else {
                    // TODO: 6/2/2021 show the error
                    Toast.makeText(getApplication()
                            , "Error code:" + mRepositoryResult.getErrorCode(), Toast.LENGTH_LONG).show();
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

    public void setEditChipAction(Chip editChip) {
        editChip.setOnClickListener(v -> {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_layout, new EditUserFragment());
            transaction.addToBackStack(EditUserFragment.CLASS_NAME);
            transaction.commit();
        });
    }

    public void logoutAction() {
        Repository.getInstance().logoutUser();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, new SignUpFragment());
        transaction.commit();
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
            return (T) new UserInfoViewModel(mApplication, mFragmentManager);
        }
    }
}