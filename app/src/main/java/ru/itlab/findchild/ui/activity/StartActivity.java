package ru.itlab.findchild.ui.activity;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.itlab.findchild.App;
import ru.itlab.findchild.R;
import ru.itlab.findchild.service.GpsService;
import ru.itlab.findchild.ui.fragment.StartFragment;
import ru.itlab.findchild.ui.fragment.StopFragment;

/**
 * @author Павел Аннин, 08.07.2015.
 */
public class StartActivity extends AppCompatActivity
        implements StartFragment.OnStartService, StopFragment.OnStopService {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initToolbar();
        updateUI();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
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

    private StopFragment initStopFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(StopFragment.TAG);
        if (fragment == null) {
            fragment = StopFragment.getInstance(App.getInstance().getUserKey());
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, StopFragment.TAG)
                    .commit();
        }
        return (StopFragment) fragment;
    }

    private void updateUI() {
        if (isGpsServiceRunning(GpsService.class)) {
            initStopFragment();
        } else {
            initStartFragment();
        }
    }

    private boolean isGpsServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStartService(String userKey) {
        if (userKey != null && !userKey.isEmpty()) {
            App.getInstance().setUserKey(userKey);
            Intent svcIntent = new Intent(this, GpsService.class);
            startService(svcIntent);
        }
        updateUI();
    }

    @Override
    public void onStopService() {
        Intent svcIntent = new Intent(this, GpsService.class);
        stopService(svcIntent);
        updateUI();
    }
}
