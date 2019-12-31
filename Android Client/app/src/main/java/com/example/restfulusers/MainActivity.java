package com.example.restfulusers;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements UserListFragment.OnFragmentInteractionListener, UserModifyFragment.fragmentFinishListener {

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
    public void finishFragment() {
        if (manager == null) manager = getSupportFragmentManager();

        manager.popBackStack(KEY_TRANSACTION_STACK_FRAGMENT_USERLIST, 0);
        Log.v(LOG_TAG, "Fragment Transaction stack popped");
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
