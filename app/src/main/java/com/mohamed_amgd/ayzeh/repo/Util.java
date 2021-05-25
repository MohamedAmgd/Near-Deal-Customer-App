package com.mohamed_amgd.ayzeh.repo;

public class Util {
    private static Util mInstance;

    private Util() {
    }

    public static Util getInstance() {
        if (mInstance == null){
            mInstance = new Util();
        }
        return mInstance;
    }

    public boolean isEmailValid(String email){
        return true;
    }

    public boolean isUsernameValid(String username) {
        return true;
    }

    public boolean isPasswordValid(String password) {
        return true;
    }

    public boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return true;
    }

    public boolean isBirthdateValid(String birthdate) {
        return true;
    }
}
