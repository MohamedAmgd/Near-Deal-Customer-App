package com.mohamed_amgd.ayzeh.repo;

import androidx.lifecycle.MutableLiveData;

import com.mohamed_amgd.ayzeh.Models.Filter;
import com.mohamed_amgd.ayzeh.Models.Offer;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.Models.SearchResult;
import com.mohamed_amgd.ayzeh.Models.Shop;

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

    public SearchResult getSearchResult(String query, Filter filter) {
        // TODO: 5/9/2021 use retrofit client to get search result of query with filter
        return new SearchResult(new MutableLiveData<>(),0f,1000f);
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
        // TODO: 5/9/2021 use retrofit client to get hot deals search result of query with filter
        if (query == null){
            return new SearchResult(getHotDeals(),0f,1000f);
        } else {
            return new SearchResult(new MutableLiveData<>(),0f,1000f);
        }
    }
}
