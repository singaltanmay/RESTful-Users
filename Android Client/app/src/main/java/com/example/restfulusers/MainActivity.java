package com.example.restfulusers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.restfulusers.API.QueryUtils;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        loadAllUsers();

        FloatingActionButton fab = findViewById(R.id.user_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserDetailsActivity.class));
            }
        });
    }

    void loadAllUsers() {

        Call<List<User>> call = RetrofitClient.getInstance()
                .getAPIClient()
                .getAllUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                mAdapter.setData(users);
                mAdapter.notifyDataSetChanged();

                Log.v(LOG_TAG,"Call successful. Items received: " + users.size());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all_users) {
            QueryUtils.deleteAllUsers();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*

    @NonNull
    @Override
    public Loader<List<User>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<User>>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Nullable
            @Override
            public List<User> loadInBackground() {
                return QueryUtils.getAllUsers();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<User>> loader, List<User> data) {
        if (data != null) {
            Log.v(LOG_TAG, "No of users received" + data.size());
            mAdapter.data = data;
            mAdapter.clear();
            mAdapter.addAll(data);
        }else Log.e(LOG_TAG, "Null list received");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<User>> loader) {
        mAdapter.data = new ArrayList<>();
        mAdapter.clear();
    }
*/

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
