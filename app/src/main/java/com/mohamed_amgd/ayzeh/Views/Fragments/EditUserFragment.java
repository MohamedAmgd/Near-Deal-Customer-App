package com.mohamed_amgd.ayzeh.Views.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.ViewModels.EditUserViewModel;
import com.mohamed_amgd.ayzeh.repo.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class EditUserFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public static final String CLASS_NAME = "EditUserFragment";
    private static final int CHOOSE_IMAGE_REQUEST = 1;

    final Calendar myCalendar = Calendar.getInstance();

    private EditUserViewModel mViewModel;
    private TextView mConfirm;
    private CircleImageView mUserImage;
    private Chip mChangePhotoChip;
    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private TextView mBirthdateTextView;
    private ConstraintLayout mBirthdateLayout;


    public static EditUserFragment newInstance() {
        return new EditUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_user_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditUserViewModel.Factory factory = new EditUserViewModel.Factory(getActivity().getApplication(), getFragmentManager());
        mViewModel = new ViewModelProvider(this, factory).get(EditUserViewModel.class);
        mConfirm = view.findViewById(R.id.confirm_text_view);
        mUserImage = view.findViewById(R.id.user_image);
        mChangePhotoChip = view.findViewById(R.id.change_photo_chip);
        mEmailEditText = view.findViewById(R.id.email_edit_text);
        mUsernameEditText = view.findViewById(R.id.username_edit_text);
        mPasswordEditText = view.findViewById(R.id.password_edit_text);
        mConfirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        mBirthdateTextView = view.findViewById(R.id.birthday_edit_text);
        mBirthdateLayout = view.findViewById(R.id.date_picker_layout);

        mViewModel.mUser.observe(getViewLifecycleOwner(), user -> {
            mViewModel.initUserImage(mUserImage);
            mViewModel.initEmail(mEmailEditText);
            mViewModel.initUsername(mUsernameEditText);
            mViewModel.initBirthdate(mBirthdateTextView);
        });
        mConfirm.setOnClickListener(this);
        mChangePhotoChip.setOnClickListener(this);
        mBirthdateLayout.setOnClickListener(this);
        mBirthdateTextView.setOnClickListener(this);
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
        } else if (v.getId() == R.id.change_photo_chip) {
            openImageChooser();
        } else if (v.getId() == R.id.confirm_text_view) {
            mViewModel.confirmAction(mEmailEditText
                    , mUsernameEditText
                    , mPasswordEditText
                    , mConfirmPasswordEditText
                    , mBirthdateTextView);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Glide.with(this).load(data.getData()).into(mUserImage);
            mViewModel.changePhotoAction(data.getData());
        }
    }


}