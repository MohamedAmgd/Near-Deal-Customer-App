package com.mohamed_amgd.ayzeh.repo;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.mohamed_amgd.ayzeh.Models.Filter;
import com.mohamed_amgd.ayzeh.Models.Offer;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.Models.SearchResult;
import com.mohamed_amgd.ayzeh.Models.Shop;
import com.mohamed_amgd.ayzeh.Models.User;

import java.util.ArrayList;

public class Repository {
    static Repository mInstance;

    private Repository() {
    }

    public static Repository getInstance() {
        if (mInstance == null) {
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

    public SearchResult getSearchResult(String query, Filter filter) {
        // TODO: 5/9/2021 use retrofit client to get search result of query with filter
        return new SearchResult(new MutableLiveData<>(), 0f, 1000f);
    }

    public MutableLiveData<Shop> getShopById(String shopId) {
        // TODO: 5/12/2021 use retrofit client to get shop details using its shopId
        return new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Product>> getShopProductsByShopId(String shopId) {
        // TODO: 5/12/2021 use retrofit client to get shop's products using shopId
        return new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Shop>> getNearbyShops(double userLat, double userLon) {
        // TODO: 5/15/2021 use retrofit to get nearby shops to user's location by userLat,userLon
        return new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Shop>> getNearbyShops(double userLat, double userLon, String query) {
        // TODO: 5/18/2021 use retrofit to get nearby shops to user's location by userLat,userLon and query
        return new MutableLiveData<>();
    }

    public SearchResult getHotDealsSearchResult(String query, Filter filter) {
        // TODO: 5/18/2021 use retrofit client to get hot deals search result of query with filter
        if (query == null) {
            return new SearchResult(getHotDeals(), 0f, 1000f);
        } else {
            return new SearchResult(new MutableLiveData<>(), 0f, 1000f);
        }
    }

    public MutableLiveData<User> getUser() {
        // TODO: 5/19/2021 use firebase auth and retrofit to get user's info
        return new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> createUser(String email, String username, String password, String birthdate) {
        // TODO: 5/26/2021 use firebase auth and retrofit to create user
        return new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> signInUser(String email, String password) {
        // TODO: 5/26/2021 use firebase auth to sign in user
        return new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> updateUser(String email, String username, String password, String birthdate) {
        // TODO: 5/26/2021 use firebase auth and retrofit to update user info
        return new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> updateUserImage(Uri mUserImageUri) {
        // TODO: 5/26/2021 use firebase auth and retrofit to update user's image
        return new MutableLiveData<>();
    }
}
