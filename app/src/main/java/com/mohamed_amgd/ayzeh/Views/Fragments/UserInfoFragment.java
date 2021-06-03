package com.mohamed_amgd.ayzeh.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.ViewModels.UserInfoViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoFragment extends Fragment {


    public static final String CLASS_NAME = "UserInfoFragment";
    private UserInfoViewModel mViewModel;
    private TextView mLogout;
    private CircleImageView mUserImageView;
    private Chip mEditChip;
    private TextView mEmail;
    private TextView mUsername;
    private TextView mBirthdate;


    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel.Factory factory = new UserInfoViewModel.Factory(getActivity().getApplication(), getFragmentManager());
        mViewModel = new ViewModelProvider(this, factory).get(UserInfoViewModel.class);
        mLogout = view.findViewById(R.id.logout_text_view);
        mUserImageView = view.findViewById(R.id.user_image);
        mEditChip = view.findViewById(R.id.edit_chip);
        mEmail = view.findViewById(R.id.email_text_view);
        mUsername = view.findViewById(R.id.username_text_view);
        mBirthdate = view.findViewById(R.id.birthday_text_view);

        mLogout.setOnClickListener(v -> {
            mViewModel.logoutAction();
        });

        mViewModel.mUser.observe(getViewLifecycleOwner(), user -> {
            mViewModel.initUserImage(mUserImageView);
            mViewModel.initEmail(mEmail);
            mViewModel.initUsername(mUsername);
            mViewModel.initBirthdate(mBirthdate);
        });
        mViewModel.setEditChipAction(mEditChip);

        mViewModel.mError.observe(getViewLifecycleOwner(), error -> {
            mViewModel.showError(view, error);
        });
    }
}