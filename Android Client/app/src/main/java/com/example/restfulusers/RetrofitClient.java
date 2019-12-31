package com.example.restfulusers;

import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RetrofitClient {

    private static String BASE_URL = "http://10.0.2.2:8080";

    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public APIClient getAPIClient() {
        return retrofit.create(APIClient.class);
    }

    public interface APIClient {

        @GET("/api/user")
        Call<List<User>> getAllUsers();

        @GET("/api/user/{id}")
        Call<User> getUserByID(@Path("id") UUID uuid);

        @POST("/api/user/")
        Call<ResponseBody> insertUser(@Body User user);

        @PUT("/api/user/{id}")
        Call<ResponseBody> insertUserAtID(@Path("id") UUID uuid, @Body User user);

        @PATCH("/api/user/{id}")
        Call<ResponseBody> modifyUserAtID(@Path("id") UUID uuid, @Body User user);

        @DELETE("/api/user")
        Call<ResponseBody> deleteAllUsers();

        @DELETE("/api/user/{id}")
        Call<ResponseBody> deleteUserByID(@Path("id") UUID uuid);

    }

}