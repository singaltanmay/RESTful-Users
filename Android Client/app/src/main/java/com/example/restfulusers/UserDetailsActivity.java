package com.example.restfulusers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.restfulusers.API.QueryUtils;
import com.example.restfulusers.API.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends AppCompatActivity {

    private UUID uuid;
    public static String KEY_INTENT_UUID = "3289gh";
    private static String LOG_TAG = UserDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        try {
            uuid = UUID.fromString(getIntent().getStringExtra(KEY_INTENT_UUID));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (uuid == null) {
                uuid = UUID.randomUUID();
                setupUserInfo(new User(uuid, "John", "Doe", "13491780"));
            } else getUserByID();
        }


        FloatingActionButton floatingActionButton = findViewById(R.id.user_edit_fab);
        floatingActionButton.setOnClickListener((View view) ->{
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.ic_edit_to_done_anim);
            ((FloatingActionButton) view).setImageDrawable(drawable);
            drawable.start();
        });

    }

    private void setupUserInfo(User user) {
        ((TextView) findViewById(R.id.user_details_user_first_name)).setText(user.getFirstName());
        ((TextView) findViewById(R.id.user_details_user_last_name)).setText(user.getLastName());
        ((TextView) findViewById(R.id.user_details_user_phone)).setText(user.getPhoneNumber());
        ( (TextView) findViewById(R.id.user_details_user_uuid)).setText(user.getUUID().toString());
    }

    private void getUserByID() {

        Call<User> call = RetrofitClient.getInstance()
                .getAPIClient()
                .getUserByID(uuid);

        call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             User body = response.body();
                             if (body != null) {
                                 setupUserInfo(body);
                             }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             call.cancel();
                             Log.d(LOG_TAG, "Call failed :" + call.toString());
                         }
                     }

        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete_all_users && uuid != null) {
            QueryUtils.deleteUserByID(uuid);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
