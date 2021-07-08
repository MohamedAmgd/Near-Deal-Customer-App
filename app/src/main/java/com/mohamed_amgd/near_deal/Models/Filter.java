package com.mohamed_amgd.near_deal.Models;

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
    private float mFilterPriceMin;
    private float mFilterPriceMax;
    private float mOriginalPriceMin;
    private float mOriginalPriceMax;

    public Filter() {
        mCategoryName = NO_CATEGORY;
        mFilterPriceMin = NO_PRICE;
        mFilterPriceMax = NO_PRICE;
        mOriginalPriceMin = NO_PRICE;
        mOriginalPriceMax = NO_PRICE;
    }

    public static Filter createFromAnotherFilter(Filter anotherFilter) {
        Filter newFilter = new Filter();
        newFilter.setCategoryName(anotherFilter.getCategoryName());
        newFilter.setFilterPriceMax(anotherFilter.getFilterPriceMax());
        newFilter.setFilterPriceMin(anotherFilter.getFilterPriceMin());
        newFilter.setOriginalPriceMax(anotherFilter.getOriginalPriceMax());
        newFilter.setOriginalPriceMin(anotherFilter.getOriginalPriceMin());
        return newFilter;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public float getFilterPriceMin() {
        return mFilterPriceMin;
    }

    public void setFilterPriceMin(float mFilterPriceMin) {
        if (mFilterPriceMin > mOriginalPriceMin || this.mFilterPriceMin == NO_PRICE)
            this.mFilterPriceMin = mFilterPriceMin;
    }

    public float getFilterPriceMax() {
        return mFilterPriceMax;
    }

    public void setFilterPriceMax(float mFilterPriceMax) {
        if (mFilterPriceMax < mOriginalPriceMax || this.mFilterPriceMax == NO_PRICE)
            this.mFilterPriceMax = mFilterPriceMax;
    }

    public float getOriginalPriceMin() {
        return mOriginalPriceMin;
    }

    public void setOriginalPriceMin(float mOriginalPriceMin) {
        this.mOriginalPriceMin = mOriginalPriceMin;
        if (mOriginalPriceMin > mFilterPriceMin) mFilterPriceMin = mOriginalPriceMin;
    }

    public float getOriginalPriceMax() {
        return mOriginalPriceMax;
    }

    public void setOriginalPriceMax(float mOriginalPriceMax) {
        this.mOriginalPriceMax = mOriginalPriceMax;
        if (mOriginalPriceMax < mFilterPriceMax) mFilterPriceMax = mOriginalPriceMax;
    }
}
