package com.mohamed_amgd.ayzeh.repo;

import androidx.lifecycle.MutableLiveData;

import com.mohamed_amgd.ayzeh.Models.Offer;
import com.mohamed_amgd.ayzeh.Models.Product;

import java.util.ArrayList;

public class Repository {
    static Repository mInstance;

    private Repository() {
    }

    public static Repository getInstance() {
        if (mInstance == null){
            mInstance = new Repository();
        }
        return mInstance;
    }

    public MutableLiveData<ArrayList<Product>> getHotDeals() {
        // TODO: 5/8/2021 use retrofit client to get hot deals from api
        return new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Offer>> getProductOffers(String productId) {
        // TODO: 5/8/2021 use retrofit client to get offers of a product using its id from api
        return new MutableLiveData<>();
    }
}
