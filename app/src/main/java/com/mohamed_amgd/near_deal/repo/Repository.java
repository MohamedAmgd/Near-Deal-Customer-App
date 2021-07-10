package com.mohamed_amgd.near_deal.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mohamed_amgd.near_deal.Models.Filter;
import com.mohamed_amgd.near_deal.Models.Offer;
import com.mohamed_amgd.near_deal.Models.Product;
import com.mohamed_amgd.near_deal.Models.SearchResult;
import com.mohamed_amgd.near_deal.Models.Shop;
import com.mohamed_amgd.near_deal.Models.User;
import com.mohamed_amgd.near_deal.repo.retrofit.RetrofitClient;

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

    public RepositoryResult<SearchResult> getHotDeals(LocationUtil.UserLocation location) {
        int range = 2;
        if (location == null) {
            return mRetrofitClient.getHotDeals(range);
        } else {
            return mRetrofitClient.getHotDeals(location.getLat(), location.getLon(), range);
        }
    }

    public RepositoryResult<ArrayList<Offer>> getProductOffers(String productId) {
        return mRetrofitClient.getProductOffers(productId);
    }

    public RepositoryResult<Product> getProduct(String productId) {
        return mRetrofitClient.getProduct(productId);
    }

    public RepositoryResult<SearchResult> searchProducts(LocationUtil.UserLocation location, String query, Filter filter) {
        String category = null, priceMax = null, priceMin = null;
        if (!filter.getCategoryName().equals(Filter.NO_CATEGORY)) {
            category = filter.getCategoryName();
        }
        if (filter.getFilterPriceMax() != Filter.NO_PRICE) {
            priceMax = filter.getFilterPriceMax() + "";
        }
        if (filter.getFilterPriceMax() != Filter.NO_PRICE) {
            priceMin = filter.getFilterPriceMin() + "";
        }
        if (location != null) {
            return mRetrofitClient.searchNearbyProductsByName(location.getLat(), location.getLon(), 5, query, category, priceMax, priceMin);
        } else {
            return mRetrofitClient.searchNearbyProductsByName(5, query, category, priceMax, priceMin);
        }
    }

    public RepositoryResult<Shop> getShopById(String shopId) {
        return mRetrofitClient.getShop(shopId);
    }

    public RepositoryResult<ArrayList<Product>> getShopProductsByShopId(String shopId) {
        return mRetrofitClient.getShopProducts(shopId);
    }

    public RepositoryResult<ArrayList<Shop>> getNearbyShops(LocationUtil.UserLocation location) {
        return mRetrofitClient.getNearbyShops(location.getLat(), location.getLon(), 50);
    }

    public RepositoryResult<ArrayList<Shop>> getNearbyShops(LocationUtil.UserLocation location, String query) {
        return mRetrofitClient.searchNearbyShopsByName(location.getLat(), location.getLon(), 50, query);
    }

    public RepositoryResult<SearchResult> getHotDealsSearchResult(LocationUtil.UserLocation location, String query, Filter filter) {
        String category = null, priceMax = null, priceMin = null;
        if (!filter.getCategoryName().equals(Filter.NO_CATEGORY)) {
            category = filter.getCategoryName();
        }
        if (filter.getFilterPriceMax() != Filter.NO_PRICE) {
            priceMax = filter.getFilterPriceMax() + "";
        }
        if (filter.getFilterPriceMin() != Filter.NO_PRICE) {
            priceMin = filter.getFilterPriceMin() + "";
        }
        if (location != null) {
            return mRetrofitClient.getHotDeals(location.getLat(), location.getLon(), 5, query, category, priceMax, priceMin);
        } else {
            return mRetrofitClient.getHotDeals(5, query, category, priceMax, priceMin);
        }
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
