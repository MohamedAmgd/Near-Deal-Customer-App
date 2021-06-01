package com.mohamed_amgd.ayzeh.repo;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.mohamed_amgd.ayzeh.Models.Filter;
import com.mohamed_amgd.ayzeh.Models.Offer;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.Models.SearchResult;
import com.mohamed_amgd.ayzeh.Models.Shop;
import com.mohamed_amgd.ayzeh.Models.User;
import com.mohamed_amgd.ayzeh.repo.retrofit.RetrofitClient;

import java.io.File;
import java.util.ArrayList;

public class Repository {

    private static final String TAG = "Repository :";
    private static Repository mInstance;
    private final FirebaseClient mFirebaseClient;
    private final RetrofitClient mRetrofitClient;

    private Repository() {
        mFirebaseClient = FirebaseClient.getInstance();
        mRetrofitClient = RetrofitClient.getInstance();
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
        return mRetrofitClient.getNearbyShops(userLat,userLon,50);
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
        if (mFirebaseClient.getCurrentUser() == null) {
            return new MutableLiveData<>();
        }
        MutableLiveData<User> result = new MutableLiveData<>();
        String email = mFirebaseClient.getCurrentUser().getEmail();
        String uid = mFirebaseClient.getCurrentUser().getUid();
        User currentUser = new User(uid, email, "", "");
        mRetrofitClient.getUserData(uid).observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser.setUsername(user.getUsername());
                currentUser.setBirthdate(user.getBirthdate());
                currentUser.setImageUrl(user.getImageUrl());
                result.setValue(currentUser);
            }
        });
        result.setValue(currentUser);
        return result;
    }

    public MutableLiveData<Boolean> createUser(String email, String username, String password, String birthdate) {
        MutableLiveData<Boolean> status = new MutableLiveData<>();
        MutableLiveData<Boolean> creationStatus =
                mFirebaseClient.createNewUser(email, password);
        creationStatus.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    String uid = mFirebaseClient.getCurrentUser().getUid();
                    User data = new User(uid, email, username, birthdate);
                    MutableLiveData<Boolean> userDataStatus
                            = mRetrofitClient.insertUserData(uid, data);
                    userDataStatus.observeForever(new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            status.setValue(aBoolean);
                        }
                    });
                } else {
                    status.setValue(false);
                }
            }
        });
        return status;
    }

    public MutableLiveData<Boolean> signInUser(String email, String password) {
        return mFirebaseClient.signInUser(email, password);
    }

    public MutableLiveData<Boolean> updateUser(String email, String username, String password, String birthdate) {
        MutableLiveData<Boolean> status = new MutableLiveData<>();
        String oldEmail = mFirebaseClient.getCurrentUser().getEmail();
        mFirebaseClient.signInUser(oldEmail, password).observeForever((signedIn) -> {
            String uid = mFirebaseClient.getCurrentUser().getUid();
            mFirebaseClient.changeUserEmail(email).observeForever(emailChanged -> {
                if (emailChanged) {
                    mFirebaseClient.changeUserPassword(password).observeForever(passwordChanged -> {
                        if (passwordChanged) {
                            User data = new User(uid, email, username, birthdate);
                            mRetrofitClient.updateUserData(uid, data).observeForever(dataChanged -> {
                                if (dataChanged) status.setValue(true);
                                else status.setValue(false);
                            });
                        } else {
                            status.setValue(false);
                        }
                    });
                } else {
                    status.setValue(false);
                }
            });
        });


        return status;
    }

    public MutableLiveData<Boolean> updateUserImage(Context context, String userImagePath) {
        String uid = mFirebaseClient.getCurrentUser().getUid();
        String type = RetrofitClient.UPLOAD_USER_IMAGE;
        File compressedImage = Util.getInstance().getCompressedImageFile(context, userImagePath);
        return mRetrofitClient.uploadImage(uid, type, compressedImage);
    }

    public void logoutUser() {
        mFirebaseClient.signOutUser();
    }
}
