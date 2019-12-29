package com.example.restfulusers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.restfulusers.API.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserListFragment.OnFragmentInteractionListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String user_list_fragment = "user_list_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_parent, new UserListFragment(), user_list_fragment);
        transaction.addToBackStack(user_list_fragment);
        transaction.commit();

    }

    @Override
    public void onUserClicked(@NotNull UUID uuid) {
        Toast.makeText(this, uuid.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewUserFabClicked() {
        startActivity(new Intent(MainActivity.this, UserDetailsActivity.class));
    }

    private void deleteAllUsers() {

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAPIClient()
                .deleteAllUsers();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(user_list_fragment);
                if (fragment instanceof UserListFragment) {
                    ((UserListFragment) fragment).loadAllUsers();
                }
                Log.v(LOG_TAG, "Deleted all users");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Log.d(LOG_TAG, "Call failed :" + t.getMessage());
            }
        });

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

        if (id == R.id.action_delete_all_users) {
            deleteAllUsers();
            Toast.makeText(this, "Deleted All", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_refresh_users_list) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(user_list_fragment);
            if (fragment instanceof UserListFragment) {
                ((UserListFragment) fragment).loadAllUsers();
            }
            Toast.makeText(this, "Reloaded", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
