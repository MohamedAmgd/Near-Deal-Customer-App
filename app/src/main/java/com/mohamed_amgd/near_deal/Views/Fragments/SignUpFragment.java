package com.mohamed_amgd.near_deal.Views.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.ViewModels.SignUpViewModel;
import com.mohamed_amgd.near_deal.repo.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public static final String CLASS_NAME = "SignUpFragment";
    final Calendar myCalendar = Calendar.getInstance();

    private SignUpViewModel mViewModel;
    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private TextView mBirthdateTextView;
    private ConstraintLayout mBirthdateLayout;
    private Button mSignUpButton;
    private ConstraintLayout mSignInLayout;


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignUpViewModel.Factory factory = new SignUpViewModel.Factory(getActivity().getApplication(), getFragmentManager());
        mViewModel = new ViewModelProvider(this, factory).get(SignUpViewModel.class);
        mEmailEditText = view.findViewById(R.id.email_edit_text);
        mUsernameEditText = view.findViewById(R.id.username_edit_text);
        mPasswordEditText = view.findViewById(R.id.password_edit_text);
        mConfirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        mBirthdateTextView = view.findViewById(R.id.birthday_edit_text);
        mBirthdateLayout = view.findViewById(R.id.date_picker_layout);
        mSignUpButton = view.findViewById(R.id.sign_up_button);
        mSignInLayout = view.findViewById(R.id.sign_in_layout);

        mBirthdateLayout.setOnClickListener(this);
        mBirthdateTextView.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mSignInLayout.setOnClickListener(this);

        mViewModel.mError.observe(getViewLifecycleOwner(), error -> {
            mViewModel.showError(view, error);
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat(Util.DATE_FORMAT, Locale.US);
        mBirthdateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.date_picker_layout) {
            mBirthdateTextView.setError(null);
            new DatePickerDialog(getContext(), this, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v.getId() == R.id.sign_up_button) {
            mViewModel.setSignUpAction(mEmailEditText
                    , mUsernameEditText
                    , mPasswordEditText
                    , mConfirmPasswordEditText
                    , mBirthdateTextView);
        } else if (v.getId() == R.id.sign_in_layout) {
            mViewModel.setSignInAction();
        }
    }
}