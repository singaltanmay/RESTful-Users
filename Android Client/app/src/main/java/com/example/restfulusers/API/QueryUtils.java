package com.example.restfulusers.API;

import android.util.Log;

import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

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
