package com.mohamed_amgd.ayzeh.Models;

import java.io.Serializable;

public class Filter implements Serializable {

    public static final String NO_CATEGORY = "";
    public static final float NO_PRICE = -1f;
    public static final String MEN_CATEGORY = "Men";
    public static final String WOMEN_CATEGORY = "Women";
    public static final String DEVICES_CATEGORY = "Devices";
    public static final String GADGETS_CATEGORY = "Gadgets";
    public static final String TOOLS_CATEGORY = "Tools";
    private String mCategoryName;
    private float mPriceMin;
    private float mPriceMax;

    public Filter() {
        mCategoryName = NO_CATEGORY;
        mPriceMin = NO_PRICE;
        mPriceMax = NO_PRICE;
    }
    public static Filter createFromAnotherFilter(Filter anotherFilter){
        Filter newFilter = new Filter();
        newFilter.setCategoryName(anotherFilter.getCategoryName());
        newFilter.setPriceMax(anotherFilter.getPriceMax());
        newFilter.setPriceMin(anotherFilter.getPriceMin());
        return newFilter;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public float getPriceMin() {
        return mPriceMin;
    }

    public void setPriceMin(float mPriceMin) {
        this.mPriceMin = mPriceMin;
    }

    public float getPriceMax() {
        return mPriceMax;
    }

    public void setPriceMax(float mPriceMax) {
        this.mPriceMax = mPriceMax;
    }
}
