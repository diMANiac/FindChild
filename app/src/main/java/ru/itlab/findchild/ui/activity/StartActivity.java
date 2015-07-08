package ru.itlab.findchild.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.itlab.findchild.App;
import ru.itlab.findchild.R;
import ru.itlab.findchild.ui.fragment.StartFragment;

/**
 * @author Павел Аннин, 08.07.2015.
 */
public class StartActivity extends AppCompatActivity implements StartFragment.OnInputUserKey {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initToolbar();
        initStartFragment();
    }

    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private StartFragment initStartFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(StartFragment.TAG);
        if (fragment == null) {
            fragment = StartFragment.getInstance(App.getInstance().getUserKey());
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, StartFragment.TAG)
                    .commit();
        }
        return (StartFragment) fragment;
    }

    @Override
    public void onInputUserKey(String userKey) {
        if (userKey != null && !userKey.isEmpty()) {
            App.getInstance().setUserKey(userKey);
        }
    }
}
