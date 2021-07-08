package com.mohamed_amgd.near_deal.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

public class RepositoryResult<T> {
    private MutableLiveData<T> data;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<Boolean> isThereAnErrorLiveData;
    private int errorCode;
    private String TAG = getClass().getName();

    public RepositoryResult(MutableLiveData<T> data) {
        this.data = data;
        isLoadingLiveData = new MutableLiveData<>();
        isLoadingLiveData.setValue(true);
        isThereAnErrorLiveData = new MutableLiveData<>();
        isThereAnErrorLiveData.setValue(false);
        errorCode = ErrorHandler.NO_ERROR;
    }

    public boolean isFinishedSuccessfully() {
        return !getIsLoading() && !getIsThereAnError();
    }

    public void setFinishedSuccessfully(T dataValue) {
        getData().setValue(dataValue);
        setIsThereAnError(false);
        setIsLoading(false);
        Log.i(TAG, "setFinishedSuccessfully: " + dataValue);
    }

    public boolean isFinishedWithError() {
        return !getIsLoading() && getIsThereAnError();
    }

    public void setFinishedWithError(int errorCode) {
        setIsThereAnError(true);
        setErrorCode(errorCode);
        setIsLoading(false);
    }

    public MutableLiveData<T> getData() {
        return data;
    }

    public void setData(MutableLiveData<T> data) {
        this.data = data;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public void setIsLoadingLiveData(MutableLiveData<Boolean> isLoadingLiveData) {
        this.isLoadingLiveData = isLoadingLiveData;
    }

    public Boolean getIsLoading() {
        return isLoadingLiveData.getValue();
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoadingLiveData.setValue(isLoading);
    }

    public MutableLiveData<Boolean> getIsThereAnErrorLiveData() {
        return isThereAnErrorLiveData;
    }

    public void setIsThereAnErrorLiveData(MutableLiveData<Boolean> isThereAnErrorLiveData) {
        this.isThereAnErrorLiveData = isThereAnErrorLiveData;
    }

    public Boolean getIsThereAnError() {
        return isThereAnErrorLiveData.getValue();
    }

    public void setIsThereAnError(Boolean isThereAnError) {
        this.isThereAnErrorLiveData.setValue(isThereAnError);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
