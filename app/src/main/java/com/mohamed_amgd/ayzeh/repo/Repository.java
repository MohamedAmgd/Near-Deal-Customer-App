package com.mohamed_amgd.ayzeh.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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

    public RepositoryResult<SearchResult> getHotDeals() {
        return mRetrofitClient.getHotDeals(2);
    }

    public RepositoryResult<ArrayList<Offer>> getProductOffers(String productId) {
        return mRetrofitClient.getProductOffers(productId);
    }

    public RepositoryResult<Product> getProduct(String productId) {
        return mRetrofitClient.getProduct(productId);
    }

    public RepositoryResult<SearchResult> searchProducts(double lat, double lon, String query, Filter filter) {
        String category = null, priceMax = null, priceMin = null;
        if (!filter.getCategoryName().equals(Filter.NO_CATEGORY)) {
            category = filter.getCategoryName();
        }
        if (filter.getPriceMax() != Filter.NO_PRICE) {
            priceMax = filter.getPriceMax() + "";
        }
        if (filter.getPriceMax() != Filter.NO_PRICE) {
            priceMin = filter.getPriceMax() + "";
        }

        return mRetrofitClient.searchNearbyProductsByName(lat, lon, 5, query, category, priceMax, priceMin);
    }

    public RepositoryResult<SearchResult> searchProducts(String query, Filter filter) {
        String category = null, priceMax = null, priceMin = null;
        if (!filter.getCategoryName().equals(Filter.NO_CATEGORY)) {
            category = filter.getCategoryName();
        }
        if (filter.getPriceMax() != Filter.NO_PRICE) {
            priceMax = filter.getPriceMax() + "";
        }
        if (filter.getPriceMax() != Filter.NO_PRICE) {
            priceMin = filter.getPriceMax() + "";
        }
        return mRetrofitClient.searchNearbyProductsByName(5, query, category, priceMax, priceMin);
    }

    public RepositoryResult<Shop> getShopById(String shopId) {
        return mRetrofitClient.getShop(shopId);
    }

    public RepositoryResult<ArrayList<Product>> getShopProductsByShopId(String shopId) {
        return mRetrofitClient.getShopProducts(shopId);
    }

    public RepositoryResult<ArrayList<Shop>> getNearbyShops(double userLat, double userLon) {
        return mRetrofitClient.getNearbyShops(userLat, userLon, 50);
    }

    public RepositoryResult<ArrayList<Shop>> getNearbyShops(double userLat, double userLon, String query) {
        return mRetrofitClient.searchNearbyShopsByName(userLat, userLon, 50, query);
    }

    public RepositoryResult<SearchResult> getHotDealsSearchResult(String query, Filter filter) {
        // TODO: 5/18/2021 use retrofit client to get hot deals search result of query with filter
        return new RepositoryResult<>(new MutableLiveData<>());
    }

    public RepositoryResult<User> getUser() {
        RepositoryResult<User> result;
        if (mFirebaseClient.getCurrentUser() == null) {
            result = new RepositoryResult<>(new MutableLiveData<>());
            result.setFinishedWithError(ErrorHandler.NEED_SIGN_IN_ERROR);
        } else {
            String email = mFirebaseClient.getCurrentUser().getEmail();
            String uid = mFirebaseClient.getCurrentUser().getUid();
            result = mRetrofitClient.getUserData(uid);
            result.getIsLoadingLiveData().observeForever(isLoading -> {
                if (result.isFinishedSuccessfully()) {
                    result.getData().getValue().setEmail(email);
                }
            });
        }
        return result;
    }

    public RepositoryResult<Boolean> createUser(String email, String username, String password, String birthdate) {
        final RepositoryResult<Boolean> result = mFirebaseClient.createNewUser(email, password);

        result.getIsLoadingLiveData().observeForever(isLoading -> {
            if (result.isFinishedSuccessfully()) {
                String uid = mFirebaseClient.getCurrentUser().getUid();
                User data = new User(uid, email, username, birthdate);
                RepositoryResult<Boolean> insertUserDataResult
                        = mRetrofitClient.insertUserData(uid, data);
                insertUserDataResult.getIsLoadingLiveData().observeForever(aBoolean -> {
                    if (insertUserDataResult.isFinishedWithError()) {
                        // updating the main result
                        result.setFinishedWithError(insertUserDataResult.getErrorCode());
                    }
                });
            }
        });
        return result;
    }

    public RepositoryResult<Boolean> signInUser(String email, String password) {
        return mFirebaseClient.signInUser(email, password);
    }

    public RepositoryResult<Boolean> updateUser(String email, String username, String password, String birthdate) {
        String oldEmail = mFirebaseClient.getCurrentUser().getEmail();
        final RepositoryResult<Boolean> result = mFirebaseClient.signInUser(oldEmail, password);
        result.getIsLoadingLiveData().observeForever((isLoading) -> {
            if (result.isFinishedSuccessfully()) {
                Log.i(TAG, "updateUser: signedIn");
                String uid = mFirebaseClient.getCurrentUser().getUid();
                RepositoryResult<Boolean> changeEmailResult = mFirebaseClient.changeUserEmail(email);
                changeEmailResult.getIsLoadingLiveData().observeForever(isLoadingChangeEmail -> {
                    if (changeEmailResult.isFinishedSuccessfully()) {
                        Log.i(TAG, "updateUser: emailChanged");
                        RepositoryResult<Boolean> changePasswordResult = mFirebaseClient.changeUserPassword(password);
                        changePasswordResult.getIsLoadingLiveData().observeForever(isLoadingChangePassword -> {
                            if (changePasswordResult.isFinishedSuccessfully()) {
                                Log.i(TAG, "updateUser: passwordChanged");
                                User data = new User(uid, email, username, birthdate);
                                RepositoryResult<Boolean> updateUserDataResult
                                        = mRetrofitClient.updateUserData(uid, data);
                                updateUserDataResult.getIsLoadingLiveData().observeForever(isLoadingUpdateUserData -> {
                                    if (updateUserDataResult.isFinishedWithError()) {
                                        result.setFinishedWithError(updateUserDataResult.getErrorCode());
                                    }
                                });
                            } else if (changePasswordResult.isFinishedWithError()) {
                                result.setFinishedWithError(changePasswordResult.getErrorCode());
                            }
                        });
                    } else if (changeEmailResult.isFinishedWithError()) {
                        result.setFinishedWithError(changeEmailResult.getErrorCode());
                    }
                });
            }
        });


        return result;
    }

    public RepositoryResult<Boolean> updateUserImage(Context context, String userImagePath) {
        String uid = mFirebaseClient.getCurrentUser().getUid();
        String type = RetrofitClient.UPLOAD_USER_IMAGE;
        File compressedImage = Util.getInstance().getCompressedImageFile(context, userImagePath);
        return mRetrofitClient.uploadImage(uid, type, compressedImage);
    }

    public void logoutUser() {
        mFirebaseClient.signOutUser();
    }
}
