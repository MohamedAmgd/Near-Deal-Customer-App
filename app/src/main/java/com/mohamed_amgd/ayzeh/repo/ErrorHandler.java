package com.mohamed_amgd.ayzeh.repo;

public class ErrorHandler {
    // common errors
    public static final int NO_ERROR = 0;
    public static final int NO_INTERNET_CONNECTION_ERROR = 1;

    // api errors
    public static final int INPUT_ERROR = 10;
    public static final int SERVER_ERROR = 11;
    public static final int BAD_RESPONSE_ERROR = 12;

    // firebase errors
    public static final int UNKNOWN_SIGN_USER_ERROR = 20;
    public static final int NEED_SIGN_IN_ERROR = 21;
    public static final int FAILED_CREATE_USER_ERROR = 22;
    public static final int FAILED_SIGN_IN_ERROR = 23;
    public static final int FAILED_UPDATE_EMAIL_ERROR = 24;
    public static final int FAILED_UPDATE_PASSWORD_ERROR = 25;

}
