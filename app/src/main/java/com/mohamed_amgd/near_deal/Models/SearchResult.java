package com.mohamed_amgd.near_deal.Models;

import java.util.ArrayList;

public class SearchResult {
    private ArrayList<Product> mResultsLiveData;
    private float mMinPrice;
    private float mMaxPrice;

    public SearchResult(ArrayList<Product> mResultsLiveData, float mMinPrice, float mMaxPrice) {
        this.mResultsLiveData = mResultsLiveData;
        this.mMinPrice = mMinPrice;
        this.mMaxPrice = mMaxPrice;
    }

    public ArrayList<Product> getResults() {
        return mResultsLiveData;
    }

    public void setResults(ArrayList<Product> mResults) {
        this.mResultsLiveData = mResults;
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
