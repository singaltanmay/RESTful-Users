package com.example.restfulusers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.restfulusers.API.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    ListView mListView;
    private ListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.users_list);
        mAdapter = new ListAdapter(this, R.layout.list_item, new ArrayList<>());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(
                (AdapterView<?> parent, View view, int position, long id) -> {
                    Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
                    intent.putExtra(UserDetailsActivity.KEY_INTENT_UUID, mAdapter.getUUIDAtIndex(position).toString());
                    startActivity(intent);
                }
        );

        loadAllUsers();

        FloatingActionButton fab = findViewById(R.id.user_add_fab);
        fab.setOnClickListener((View view) -> startActivity(new Intent(MainActivity.this, UserDetailsActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAllUsers();
    }

    private void loadAllUsers() {

        Call<List<User>> call = RetrofitClient.getInstance()
                .getAPIClient()
                .getAllUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                mAdapter.setData(users);
                mAdapter.notifyDataSetChanged();

                Log.v(LOG_TAG, "Call successful. Items received: " + users.size());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                call.cancel();
                Log.d(LOG_TAG, "Call failed :" + t.getMessage());
            }
        });

    }

    private void deleteAllUsers() {

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAPIClient()
                .deleteAllUsers();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadAllUsers();
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
            loadAllUsers();
            Toast.makeText(this, "Reloaded", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListAdapter extends ArrayAdapter<User> {

        private List<User> data;
        int resource;

        public ListAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
            super(context, resource, objects);
            this.data = objects;
            this.resource = resource;
        }

        public void setData(List<User> data) {
            this.data = data;
        }

        public UUID getUUIDAtIndex(int position) {
            return this.data.get(position).getUUID();
        }

        @Override
        public int getCount() {
            return this.data.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(resource, parent, false);
            }

            try {
                TextView name = convertView.findViewById(R.id.list_item_user_name);
                TextView phone = convertView.findViewById(R.id.list_item_user_phone);
                TextView uuid = convertView.findViewById(R.id.list_item_user_uuid);

                User user = data.get(position);
                name.setText(user.getFirstName() + " " + user.getLastName());
                phone.setText(user.getPhoneNumber());
                uuid.setText(user.getUUID().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }
}
