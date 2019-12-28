package com.example.restfulusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.restfulusers.API.QueryUtils;

import java.util.UUID;

public class UserDetailsActivity extends AppCompatActivity {

    private UUID uuid;
    public static String KEY_INTENT_UUID = "3289gh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        try {
            uuid = UUID.fromString(getIntent().getStringExtra(KEY_INTENT_UUID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = new User(uuid == null ? UUID.randomUUID() : uuid, "fname", "lname", "238945221");

        ((TextView) findViewById(R.id.user_details_user_first_name)).setText(user.getFirstName());
        ((TextView) findViewById(R.id.user_details_user_last_name)).setText(user.getLastName());
        ((TextView) findViewById(R.id.user_details_user_phone)).setText(user.getPhoneNumber());
        ((TextView) findViewById(R.id.user_details_user_uuid)).setText(user.getUUID().toString());

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
