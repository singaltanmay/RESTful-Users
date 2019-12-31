package com.example.restfulusers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserListFragment.OnFragmentInteractionListener, UserModifyFragment.OnModificationDoneListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String KEY_TRANSACTION_STACK_FRAGMENT_USERLIST = "ZJV8y2rhdy";

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_parent, new UserListFragment(), KEY_TRANSACTION_STACK_FRAGMENT_USERLIST);
        transaction.addToBackStack(KEY_TRANSACTION_STACK_FRAGMENT_USERLIST);
        transaction.commit();

    }

    @Override
    public void onUserClicked(@NotNull UUID uuid) {

        if (manager == null) {
            manager = getSupportFragmentManager();
        }

        FragmentTransaction transaction = manager.beginTransaction();

        UserModifyFragment fragment = new UserModifyFragment(uuid);
        transaction.replace(R.id.fragment_parent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onNewUserFabClicked() {
        if (manager == null) manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_parent, new UserModifyFragment(null));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onModificationDone() {
        if (manager == null) manager = getSupportFragmentManager();

        manager.popBackStack(KEY_TRANSACTION_STACK_FRAGMENT_USERLIST, 0);
        Log.v(LOG_TAG, "Fragment Transaction stack popped");
    }

    private void deleteAllUsers() {

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAPIClient()
                .deleteAllUsers();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(KEY_TRANSACTION_STACK_FRAGMENT_USERLIST);
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
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(KEY_TRANSACTION_STACK_FRAGMENT_USERLIST);
            if (fragment instanceof UserListFragment) {
                ((UserListFragment) fragment).loadAllUsers();
            }
            Toast.makeText(this, "Reloaded", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /**
         *  Prevent popping of {@link UserListFragment()}
         **/
        if (manager.getBackStackEntryCount() == 1) finish();
        else super.onBackPressed();
    }
}
