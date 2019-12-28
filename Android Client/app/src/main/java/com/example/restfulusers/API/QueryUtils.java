package com.example.restfulusers.API;

import android.util.Log;

import com.example.restfulusers.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static User user;

    public static User getUserByID(UUID uuid) {

        Call<User> call = RetrofitClient.getInstance()
                .getAPIClient()
                .getUserByID(uuid);

        call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             user = response.body();
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             call.cancel();
                             Log.d(LOG_TAG, "Call failed :" + call.toString());
                         }
                     }

        );

        return user;

    }

    public static void insertUser(User user) {
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAPIClient()
                .insertUser(user);

        Log.v(LOG_TAG, "Call created :" + call.request().body().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v(LOG_TAG, "User insertion result: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Log.d(LOG_TAG, "Call failed :" + t.getMessage());
            }
        });

    }



    public static void deleteUserByID(UUID uuid){

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAPIClient()
                .deleteUserByID(uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v(LOG_TAG, "Deleted user having UUID: " + uuid.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Log.d(LOG_TAG, "Call failed :" + t.getMessage());
            }
        });

    }

    private QueryUtils() {
    }

}
