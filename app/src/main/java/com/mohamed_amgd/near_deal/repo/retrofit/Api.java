package com.mohamed_amgd.near_deal.repo.retrofit;

import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @Multipart
    @POST("upload_image")
    Call<JsonObject> uploadImage(@Query("id") String id
            , @Query("type") String type
            , @Part MultipartBody.Part image);

    @GET("nearby_shops")
    Call<JsonObject> getNearbyShops(@Query("lat") double lat
            , @Query("lon") double lan
            , @Query("range") int range);

    @GET("search_nearby_shops_by_name")
    Call<JsonObject> searchNearbyShopsByName(@Query("lat") double lat
            , @Query("lon") double lan
            , @Query("range") int range
            , @Query("input_name") String name);

    @GET("shops")
    Call<JsonObject> getShop(@Query("shop_id") String id);

    @GET("products")
    Call<JsonObject> getProduct(@Query("product_id") String id);

    @GET("products")
    Call<JsonObject> getShopProducts(@Query("shop_id") String shopId);

    @GET("offers")
    Call<JsonObject> getProductOffers(@Query("product_id") String shopId);

    @GET("hot_deals")
    Call<JsonObject> getHotDeals(@Query("range") int range);

    @GET("hot_deals")
    Call<JsonObject> getHotDeals(@Query("lat") double lat
            , @Query("lon") double lan
            , @Query("range") int range);

    @GET("search_hot_deals")
    Call<JsonObject> searchHotDeals(@Query("range") int range
            , @Query("input_name") String name
            , @Query("category") String category
            , @Query("price_max") String price_max
            , @Query("price_min") String price_min);

    @GET("search_hot_deals")
    Call<JsonObject> searchHotDeals(@Query("lat") double lat
            , @Query("lon") double lon
            , @Query("range") int range
            , @Query("input_name") String name
            , @Query("category") String category
            , @Query("price_max") String price_max
            , @Query("price_min") String price_min);

    @GET("search_nearby_products_by_name")
    Call<JsonObject> searchNearbyProductsByName(@Query("lat") double lat
            , @Query("lon") double lan
            , @Query("range") int range
            , @Query("input_name") String name
            , @Query("category") String category
            , @Query("price_max") String price_max
            , @Query("price_min") String price_min);

    @GET("search_nearby_products_by_name")
    Call<JsonObject> searchNearbyProductsByName(@Query("range") int range
            , @Query("input_name") String name
            , @Query("category") String category
            , @Query("price_max") String price_max
            , @Query("price_min") String price_min);


}
