package com.mohamed_amgd.ayzeh.repo.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mohamed_amgd.ayzeh.Models.Shop;
import com.mohamed_amgd.ayzeh.Models.User;
import com.mohamed_amgd.ayzeh.repo.Util;

import java.io.File;
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
    private static final int STATUS_CODE_SUCCESSFUL = 200;
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

    public MutableLiveData<User> getUserData(String userId) {
        MutableLiveData<User> resultLiveData = new MutableLiveData<>();
        Call<JsonObject> request = mApi.getUserData(userId);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    resultLiveData.setValue(convertResponseToUser(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                // TODO: 5/29/2021
            }
        });
        return resultLiveData;
    }

    public MutableLiveData<Boolean> insertUserData(String userId, User userData) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        HashMap<String, Object> requestBody = convertUserDataToRequestBody(userData);
        Call<JsonObject> request = mApi.insertUserData(userId, requestBody);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    resultLiveData.setValue(true);
                } else {
                    resultLiveData.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                resultLiveData.setValue(false);
            }
        });
        return resultLiveData;
    }

    public MutableLiveData<Boolean> updateUserData(String userId, User userData) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        HashMap<String, Object> requestBody = convertUserDataToRequestBody(userData);
        Call<JsonObject> request = mApi.updateUserData(userId, requestBody);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    resultLiveData.setValue(true);
                } else {
                    resultLiveData.setValue(false);
                }
                Log.i("Image", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.i("Retrofit", "onFailure: " + t.getMessage());
                resultLiveData.setValue(false);
            }
        });
        return resultLiveData;
    }

    public MutableLiveData<Boolean> deleteUserData(String userId) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        Call<JsonObject> request = mApi.deleteUserData(userId);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    resultLiveData.setValue(true);
                } else {
                    resultLiveData.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                resultLiveData.setValue(false);
            }
        });
        return resultLiveData;
    }

    public MutableLiveData<Boolean> uploadImage(String id, String type, File imageFile) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        Call<JsonObject> request = mApi.uploadImage(id, type, body);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    resultLiveData.setValue(true);
                } else {
                    resultLiveData.setValue(false);
                }
                Log.i("Image", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                resultLiveData.setValue(false);
                t.printStackTrace();
                Log.i("Retrofit", "onFailure: " + t.getMessage());
            }
        });
        return resultLiveData;
    }

    public MutableLiveData<ArrayList<Shop>> getNearbyShops(double lat, double lon, int range) {
        MutableLiveData<ArrayList<Shop>> shopsLiveData = new MutableLiveData<>();
        Call<JsonObject> request = mApi.getNearbyShops(lat, lon, range);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    shopsLiveData.setValue(convertResponseToShopsArrayList(response, lat, lon));
                    Log.i("Retrofit", "onSuccess: " + shopsLiveData.getValue());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                // TODO: 6/1/2021
                t.printStackTrace();
                Log.i("Retrofit", "onFailure: " + t.getMessage());
            }
        });
        return shopsLiveData;
    }

    public MutableLiveData<ArrayList<Shop>> searchNearbyShopsByName(double lat, double lon, int range, String name) {
        MutableLiveData<ArrayList<Shop>> shopsLiveData = new MutableLiveData<>();
        Call<JsonObject> request = mApi.searchNearbyShopsByName(lat, lon, range, name);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    shopsLiveData.setValue(convertResponseToShopsArrayList(response, lat, lon));
                    Log.i("Retrofit", "onSuccess: " + shopsLiveData.getValue());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                // TODO: 6/1/2021
                t.printStackTrace();
                Log.i("Retrofit", "onFailure: " + t.getMessage());
            }
        });
        return shopsLiveData;
    }

    public MutableLiveData<Shop> getShop(String id) {
        MutableLiveData<Shop> shopLiveData = new MutableLiveData<>();
        Call<JsonObject> request = mApi.getShop(id);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == STATUS_CODE_SUCCESSFUL) {
                    shopLiveData.setValue(convertResponseToShop(response, 0, 0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                // TODO: 5/29/2021
                t.printStackTrace();
            }
        });
        return shopLiveData;
    }

    private Shop convertResponseToShop(Response<JsonObject> response, double userLat, double userLon) {
        Shop shop = null;
        if (response.body() != null) {
            shop = convertJsonObjectToShop(response.body().getAsJsonObject("message"), userLat, userLon);
        }
        return shop;
    }

    private ArrayList<Shop> convertResponseToShopsArrayList(Response<JsonObject> response, double userLat, double userLon) {
        ArrayList<Shop> shops = new ArrayList<>();
        if (response.body() != null) {
            JsonArray elements = response.body().getAsJsonArray("message");
            for (JsonElement element :
                    elements) {
                Shop shop = convertJsonObjectToShop(element.getAsJsonObject(), userLat, userLon);
                shops.add(shop);
            }
        }
        return shops;
    }

    private Shop convertJsonObjectToShop(JsonObject jsonObject, double userLat, double userLon) {
        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        String image_url = jsonObject.get("image_url").getAsString();
        double shop_lat = jsonObject.get("lat").getAsDouble();
        double shop_lon = jsonObject.get("lon").getAsDouble();
        String distanceToUser
                = Util.getInstance().getDistanceBetweenUserAndShop(userLat, userLon, shop_lat, shop_lon);
        return new Shop(id, name, image_url, description, shop_lon, shop_lat, distanceToUser);
    }

    private User convertResponseToUser(Response<JsonObject> response) {
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
    }

    private HashMap<String, Object> convertUserDataToRequestBody(User userData) {
        HashMap<String, Object> requestBody = new HashMap<>();
        JsonObject userDataJsonObject = new JsonObject();
        userDataJsonObject.add("user_name"
                , new JsonPrimitive(userData.getUsername()));

        userDataJsonObject.add("birth_day"
                , new JsonPrimitive(userData.getBirthdate()));
        requestBody.put("user_data", userDataJsonObject);
        return requestBody;
    }
}
