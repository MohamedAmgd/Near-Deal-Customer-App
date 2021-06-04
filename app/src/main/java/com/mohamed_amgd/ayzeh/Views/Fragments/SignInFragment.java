package com.mohamed_amgd.ayzeh.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.ViewModels.SignInViewModel;

public class SignInFragment extends Fragment {

    public static final String CLASS_NAME = "SignInFragment";

    private SignInViewModel mViewModel;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignInButton;
    private ConstraintLayout mSignUpLayout;


    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignInViewModel.Factory factory = new SignInViewModel.Factory(getActivity().getApplication(), getFragmentManager());
        mViewModel = new ViewModelProvider(this, factory).get(SignInViewModel.class);
        mEmailEditText = view.findViewById(R.id.email_edit_text);
        mPasswordEditText = view.findViewById(R.id.password_edit_text);
        mSignInButton = view.findViewById(R.id.sign_in_button);
        mSignUpLayout = view.findViewById(R.id.sign_up_layout);

        mSignUpLayout.setOnClickListener(v -> {
            mViewModel.signUpAction();
        });

        mSignInButton.setOnClickListener(v -> {
            mViewModel.signInAction(mEmailEditText, mPasswordEditText);
        });

        mViewModel.mError.observe(getViewLifecycleOwner(), error -> {
            mViewModel.showError(view, error);
        });
    }
}