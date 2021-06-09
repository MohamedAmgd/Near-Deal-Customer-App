package com.mohamed_amgd.ayzeh.repo.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mohamed_amgd.ayzeh.Models.Offer;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.Models.SearchResult;
import com.mohamed_amgd.ayzeh.Models.Shop;
import com.mohamed_amgd.ayzeh.Models.User;
import com.mohamed_amgd.ayzeh.repo.ErrorHandler;
import com.mohamed_amgd.ayzeh.repo.RepositoryResult;
import com.mohamed_amgd.ayzeh.repo.Util;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String UPLOAD_USER_IMAGE = "user";
    private static final String BASE_URL = "https://ayz-eh.herokuapp.com/";
    //private static final String BASE_URL = "https://3ayez-eh.azurewebsites.net/";
    private static final int STATUS_CODE_SUCCESSFUL = 200;
    private static final int STATUS_CODE_CLIENT_INPUT_ERROR = 400;
    private static final int STATUS_CODE_CLIENT_EMPTY_IMAGE_ERROR = 403;
    private static final int STATUS_CODE_SERVER_ERROR = 500;
    private static final String TAG = "RetrofitClient";
    private static RetrofitClient mInstance;

    private Retrofit mRetrofit;
    private Api mApi;

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = mRetrofit.create(Api.class);
    }

    public static RetrofitClient getInstance() {
        if (mInstance == null) mInstance = new RetrofitClient();
        return mInstance;
    }

    public RepositoryResult<User> getUserData(String userId) {
        RepositoryResult<User> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getUserData(userId);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                User user = convertResponseToUser(response);
                int errorCode = handleSuccess(user, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(user);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<Boolean> insertUserData(String userId, User userData) {
        RepositoryResult<Boolean> result = new RepositoryResult<>(new MutableLiveData<>());
        HashMap<String, Object> requestBody = convertUserDataToRequestBody(userData);
        Call<JsonObject> request = mApi.insertUserData(userId, requestBody);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                int errorCode = handleSuccess(response, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(true);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<Boolean> updateUserData(String userId, User userData) {
        RepositoryResult<Boolean> result = new RepositoryResult<>(new MutableLiveData<>());
        HashMap<String, Object> requestBody = convertUserDataToRequestBody(userData);
        Call<JsonObject> request = mApi.updateUserData(userId, requestBody);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                int errorCode = handleSuccess(response, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(true);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<Boolean> uploadImage(String id, String type, File imageFile) {
        RepositoryResult<Boolean> result = new RepositoryResult<>(new MutableLiveData<>());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        Call<JsonObject> request = mApi.uploadImage(id, type, body);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                int errorCode = handleSuccess(response, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(true);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<ArrayList<Shop>> getNearbyShops(double lat, double lon, int range) {
        RepositoryResult<ArrayList<Shop>> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getNearbyShops(lat, lon, range);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                ArrayList<Shop> shops = convertResponseToShopsArrayList(response, lat, lon);
                int errorCode = handleSuccess(shops, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(shops);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<ArrayList<Shop>> searchNearbyShopsByName(double lat, double lon, int range, String name) {
        RepositoryResult<ArrayList<Shop>> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.searchNearbyShopsByName(lat, lon, range, name);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                ArrayList<Shop> shops = convertResponseToShopsArrayList(response, lat, lon);
                int errorCode = handleSuccess(shops, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(shops);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<Shop> getShop(String id) {
        RepositoryResult<Shop> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getShop(id);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Shop shop = convertResponseToShop(response);
                int errorCode = handleSuccess(shop, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(shop);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<ArrayList<Product>> getShopProducts(String shopId) {
        RepositoryResult<ArrayList<Product>> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getShopProducts(shopId);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                ArrayList<Product> products = convertResponseToProductArrayList(response);
                int errorCode = handleSuccess(products, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    Log.i(TAG, "onResponse: " + products);
                    result.setFinishedSuccessfully(products);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<Product> getProduct(String id) {
        RepositoryResult<Product> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getProduct(id);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Product product = convertResponseToProduct(response);
                int errorCode = handleSuccess(product, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(product);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<ArrayList<Offer>> getProductOffers(String productId) {
        RepositoryResult<ArrayList<Offer>> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getProductOffers(productId);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                ArrayList<Offer> offers = convertResponseToOfferArrayList(response);
                int errorCode = handleSuccess(offers, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(offers);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<SearchResult> getHotDeals(int range) {
        RepositoryResult<SearchResult> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getHotDeals(range);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                SearchResult searchResult = convertResponseToProductSearchResult(response);
                int errorCode = handleSuccess(searchResult, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(searchResult);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<SearchResult> getHotDeals(double lat, double lon, int range) {
        RepositoryResult<SearchResult> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.getHotDeals(lat, lon, range);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                SearchResult searchResult = convertResponseToProductSearchResult(response);
                int errorCode = handleSuccess(searchResult, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(searchResult);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<SearchResult> getHotDeals(int range
            , String name
            , String category
            , String price_max
            , String price_min) {
        RepositoryResult<SearchResult> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.searchHotDeals(range, name, category, price_max, price_min);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                SearchResult searchResult = convertResponseToProductSearchResult(response);
                int errorCode = handleSuccess(searchResult, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(searchResult);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<SearchResult> getHotDeals(double lat
            , double lon
            , int range
            , String name
            , String category
            , String price_max
            , String price_min) {
        RepositoryResult<SearchResult> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.searchHotDeals(lat, lon, range, name, category, price_max, price_min);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                SearchResult searchResult = convertResponseToProductSearchResult(response);
                int errorCode = handleSuccess(searchResult, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(searchResult);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }
    public RepositoryResult<SearchResult> searchNearbyProductsByName(double lat
            , double lon
            , int range
            , String name
            , String category
            , String priceMax
            , String priceMin) {
        RepositoryResult<SearchResult> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.searchNearbyProductsByName(lat, lon, range, name, category, priceMax, priceMin);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                SearchResult searchResult = convertResponseToProductSearchResult(response);
                int errorCode = handleSuccess(searchResult, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(searchResult);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    public RepositoryResult<SearchResult> searchNearbyProductsByName(int range
            , String name
            , String category
            , String priceMax
            , String priceMin) {
        RepositoryResult<SearchResult> result = new RepositoryResult<>(new MutableLiveData<>());
        Call<JsonObject> request = mApi.searchNearbyProductsByName(range, name, category, priceMax, priceMin);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                SearchResult searchResult = convertResponseToProductSearchResult(response);
                int errorCode = handleSuccess(searchResult, response);
                if (errorCode == ErrorHandler.NO_ERROR) {
                    result.setFinishedSuccessfully(searchResult);
                } else {
                    result.setFinishedWithError(errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                result.setFinishedWithError(handleFailure(t));
            }
        });
        return result;
    }

    private SearchResult convertResponseToProductSearchResult(Response<JsonObject> response) {
        if (response.body() != null) {
            ArrayList<Product> products = new ArrayList<>();
            try {
                Log.i(TAG, "convertResponseToProductSearchResult: " + response.body());
                JsonObject message = response.body().get("message").getAsJsonObject();
                JsonArray productsElements = message.get("products").getAsJsonArray();
                for (JsonElement element :
                        productsElements) {
                    Product product = convertJsonObjectToProduct(element.getAsJsonObject());
                    if (product != null) products.add(product);
                }
                if (productsElements.size() == 0) {
                    return new SearchResult(new ArrayList<>(), 0f, 100f);
                }
                if (products.size() != productsElements.size()) {
                    return null;
                }
                float minItemPrice = message.get("min_price").getAsFloat();
                float maxItemPrice = message.get("max_price").getAsFloat();
                return new SearchResult(products, minItemPrice, maxItemPrice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ArrayList<Offer> convertResponseToOfferArrayList(Response<JsonObject> response) {
        if (response.body() != null) {
            try {
                ArrayList<Offer> offers = new ArrayList<>();
                JsonArray elements = response.body().getAsJsonArray("message");
                for (JsonElement element :
                        elements) {
                    Offer offer = convertJsonObjectToOffer(element.getAsJsonObject());
                    if (offer != null) offers.add(offer);
                }
                if (offers.size() != elements.size()) {
                    return null;
                }
                return offers;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Offer convertJsonObjectToOffer(JsonObject jsonObject) {
        try {
            String id = jsonObject.get("id").getAsString();
            String shop_id = jsonObject.get("shop_id").getAsString();
            String product_id = jsonObject.get("product_id").getAsString();
            int amount = jsonObject.get("amount").getAsInt();
            float price = jsonObject.get("price").getAsFloat();
            String shop_name = jsonObject.get("shop_name").getAsString();
            String shop_image_url = jsonObject.get("shop_image_url").getAsString();
            return new Offer(id, product_id, shop_id, shop_name, shop_image_url, price, amount);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Product convertResponseToProduct(Response<JsonObject> response) {
        try {
            Product product = null;
            if (response.body() != null) {
                product = convertJsonObjectToProduct(response.body().getAsJsonObject("message"));
            }
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Product> convertResponseToProductArrayList(Response<JsonObject> response) {
        try {
            ArrayList<Product> products = new ArrayList<>();
            if (response.body() != null) {
                JsonArray elements = response.body().getAsJsonArray("message");
                for (JsonElement element :
                        elements) {
                    Product product = convertJsonObjectToProduct(element.getAsJsonObject());
                    if (product != null) products.add(product);
                }
                if (products.size() != elements.size()) {
                    return null;
                }
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Product convertJsonObjectToProduct(JsonObject jsonObject) {
        try {
            String id = jsonObject.get("id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String brand = jsonObject.get("brand").getAsString();
            int amount;
            if (jsonObject.get("offer_amount") != null) {
                amount = jsonObject.get("offer_amount").getAsInt();
            } else {
                amount = 0;
            }
            float price;
            if (jsonObject.get("offer_price") != null) {
                price = jsonObject.get("offer_price").getAsFloat();
            } else {
                price = 0;
            }
            String category = jsonObject.get("category").getAsString();
            String image_url = jsonObject.get("image_url").getAsString();
            return new Product(id, name, category, brand, price + "", amount, description, image_url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Shop convertResponseToShop(Response<JsonObject> response) {
        try {
            Shop shop = null;
            if (response.body() != null) {
                shop = convertJsonObjectToShop(response.body().getAsJsonObject("message"), 0, 0);
            }
            return shop;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Shop> convertResponseToShopsArrayList(Response<JsonObject> response, double userLat, double userLon) {
        try {
            ArrayList<Shop> shops = new ArrayList<>();
            if (response.body() != null) {
                JsonArray elements = response.body().getAsJsonArray("message");
                for (JsonElement element :
                        elements) {
                    Shop shop = convertJsonObjectToShop(element.getAsJsonObject(), userLat, userLon);
                    if (shop != null) shops.add(shop);
                }
                if (shops.size() != elements.size()) {
                    return null;
                }
            }
            return shops;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Shop convertJsonObjectToShop(JsonObject jsonObject, double userLat, double userLon) {
        try {
            String id = jsonObject.get("id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String image_url = jsonObject.get("image_url").getAsString();
            double shop_lat = jsonObject.get("lat").getAsDouble();
            double shop_lon = jsonObject.get("lon").getAsDouble();
            String distanceToUser
                    = Util.getInstance().getDistanceBetweenUserAndShop(userLat, userLon, shop_lat, shop_lon);
            return new Shop(id, name, image_url, description, shop_lon, shop_lat, distanceToUser);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private User convertResponseToUser(Response<JsonObject> response) {
        try {
            User user = null;
            if (response.body() != null) {
                String user_name = response.body().getAsJsonObject("message").get("user_name").getAsString();
                String birth_day = response.body().getAsJsonObject("message").get("birth_day").getAsString();
                user = new User("", "", user_name, birth_day);
                if (response.body().getAsJsonObject("message").get("image_url") != null) {
                    String image_url = response.body().getAsJsonObject("message").get("image_url").getAsString();
                    user.setImageUrl(image_url);
                }
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HashMap<String, Object> convertUserDataToRequestBody(User userData) {
        try {
            HashMap<String, Object> requestBody = new HashMap<>();
            JsonObject userDataJsonObject = new JsonObject();
            userDataJsonObject.add("user_name"
                    , new JsonPrimitive(userData.getUsername()));

            userDataJsonObject.add("birth_day"
                    , new JsonPrimitive(userData.getBirthdate()));
            requestBody.put("user_data", userDataJsonObject);
            return requestBody;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private int handleSuccess(Object o, Response<JsonObject> response) {
        if (response.code() == STATUS_CODE_SUCCESSFUL) {
            if (o == null) {
                return ErrorHandler.BAD_RESPONSE_ERROR;
            } else {
                return ErrorHandler.NO_ERROR;
            }
        } else if (response.code() == STATUS_CODE_CLIENT_INPUT_ERROR) {
            return ErrorHandler.INPUT_ERROR;
        } else if (response.code() == STATUS_CODE_SERVER_ERROR) {
            return ErrorHandler.SERVER_ERROR;
        } else {
            return ErrorHandler.UNKNOWN_SERVER_ERROR;
        }
    }

    private int handleFailure(Throwable t) {
        t.printStackTrace();
        Log.i(TAG, "onFailure: " + t.getMessage());
        if (t.getClass() == UnknownHostException.class) {
            return ErrorHandler.NO_INTERNET_CONNECTION_ERROR;
        } else if (t.getClass() == SocketTimeoutException.class) {
            return ErrorHandler.REQUEST_TIMED_OUT_ERROR;
        } else {
            return ErrorHandler.INTERNET_CONNECTION_UNSTABLE_ERROR;
        }
    }

}
