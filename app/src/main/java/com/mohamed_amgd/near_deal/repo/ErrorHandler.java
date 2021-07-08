package com.mohamed_amgd.near_deal.repo;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mohamed_amgd.near_deal.R;

public class ErrorHandler {
    // common errors
    public static final int NO_ERROR = 0;
    public static final int NO_INTERNET_CONNECTION_ERROR = 1;
    public static final int INTERNET_CONNECTION_UNSTABLE_ERROR = 2;
    public static final int REQUEST_TIMED_OUT_ERROR = 3;

    // api errors
    public static final int INPUT_ERROR = 10;
    public static final int SERVER_ERROR = 11;
    public static final int BAD_RESPONSE_ERROR = 12;
    public static final int UNKNOWN_SERVER_ERROR = 13;

    // firebase errors
    public static final int UNKNOWN_SIGN_USER_ERROR = 20;
    public static final int NEED_SIGN_IN_ERROR = 21;
    public static final int FAILED_CREATE_USER_ERROR = 22;
    public static final int FAILED_SIGN_IN_ERROR = 23;
    public static final int FAILED_UPDATE_EMAIL_ERROR = 24;
    public static final int FAILED_UPDATE_PASSWORD_ERROR = 25;

    private static ErrorHandler mInstance;

    private ErrorHandler() {
    }

    public static ErrorHandler getInstance() {
        if (mInstance == null) mInstance = new ErrorHandler();
        return mInstance;
    }

    public void showError(View view, Error error) {
        showError(view, error.getErrorCode(), error.getListener());
    }

    public void showError(View view, int errorCode, View.OnClickListener listener) {
        Context context = view.getContext();
        String errorMessage = "";
        switch (errorCode) {
            case NO_INTERNET_CONNECTION_ERROR:
                errorMessage = context.getString(R.string.no_internet_connection_error);
                break;
            case INTERNET_CONNECTION_UNSTABLE_ERROR:
                errorMessage = context.getString(R.string.internet_connection_unstable_error);
                break;
            case REQUEST_TIMED_OUT_ERROR:
                errorMessage = context.getString(R.string.request_timed_out_error);
                break;
            case INPUT_ERROR:
                errorMessage = context.getString(R.string.input_error);
                break;
            case SERVER_ERROR:
                errorMessage = context.getString(R.string.server_error);
                break;
            case BAD_RESPONSE_ERROR:
                errorMessage = context.getString(R.string.bad_response_error);
                break;
            case UNKNOWN_SIGN_USER_ERROR:
                errorMessage = context.getString(R.string.unknown_sign_user_error);
                break;
            case NEED_SIGN_IN_ERROR:
                errorMessage = context.getString(R.string.need_sign_in_error);
                break;
            case FAILED_CREATE_USER_ERROR:
                errorMessage = context.getString(R.string.failed_create_user_error);
                break;
            case FAILED_SIGN_IN_ERROR:
                errorMessage = context.getString(R.string.failed_sign_in_error);
                break;
            case FAILED_UPDATE_EMAIL_ERROR:
                errorMessage = context.getString(R.string.failed_update_email_error);
                break;
            case FAILED_UPDATE_PASSWORD_ERROR:
                errorMessage = context.getString(R.string.failed_update_password_error);
                break;
        }
        Snackbar snackbar = Snackbar.make(view, errorMessage, BaseTransientBottomBar.LENGTH_INDEFINITE);
        if (listener == null) {
            snackbar.setDuration(BaseTransientBottomBar.LENGTH_LONG);
        } else {
            snackbar.setAction(R.string.retry, listener);
        }
        snackbar.show();
    }

    public void reportNonFetalCrash(Throwable throwable) {
        FirebaseClient.getInstance().reportNonFetalCrash(throwable);
    }

    public static class Error {
        private int errorCode;
        private View.OnClickListener listener;

        public Error(int errorCode, View.OnClickListener listener) {
            this.errorCode = errorCode;
            this.listener = listener;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public View.OnClickListener getListener() {
            return listener;
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }
    }
}
