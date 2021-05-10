package com.mohamed_amgd.ayzeh.Models;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class SearchResult {
    private MutableLiveData<ArrayList<Product>> mResultsLiveData;
    private float mMinPrice;
    private float mMaxPrice;

    public SearchResult(MutableLiveData<ArrayList<Product>> mResultsLiveData, float mMinPrice, float mMaxPrice) {
        this.mResultsLiveData = mResultsLiveData;
        this.mMinPrice = mMinPrice;
        this.mMaxPrice = mMaxPrice;
    }

    public MutableLiveData<ArrayList<Product>> getResultsLiveData() {
        return mResultsLiveData;
    }

    public void setResultsLiveData(MutableLiveData<ArrayList<Product>> mResultsLiveData) {
        this.mResultsLiveData = mResultsLiveData;
    }

    public float getMinPrice() {
        return mMinPrice;
    }

    public void setMinPrice(float mMinPrice) {
        this.mMinPrice = mMinPrice;
    }

    public float getMaxPrice() {
        return mMaxPrice;
    }

    public void setMaxPrice(float mMaxPrice) {
        this.mMaxPrice = mMaxPrice;
    }
}
