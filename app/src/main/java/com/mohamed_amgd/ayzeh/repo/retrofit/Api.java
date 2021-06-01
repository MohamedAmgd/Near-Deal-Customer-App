package com.mohamed_amgd.ayzeh.repo.retrofit;

import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @GET("user_data")
    Call<JsonObject> getUserData(@Query("uid") String uid);

    @POST("user_data")
    Call<JsonObject> insertUserData(@Query("uid") String uid, @Body() HashMap<String, Object> userData);

    @PUT("user_data")
    Call<JsonObject> updateUserData(@Query("uid") String uid, @Body() HashMap<String, Object> userData);

    @DELETE("user_data")
    Call<JsonObject> deleteUserData(@Query("uid") String uid);

    @Multipart
    @POST("upload_image")
    Call<JsonObject> uploadImage(@Query("id") String id
            , @Query("type") String type
            , @Part MultipartBody.Part image);

    @GET("nearby_shops")
    Call<JsonObject> getNearbyShops(@Query("lat") double lat
            , @Query("lon") double lan
            , @Query("range") int range);

}
