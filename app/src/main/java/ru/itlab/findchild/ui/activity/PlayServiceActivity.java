package ru.itlab.findchild.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import ru.itlab.findchild.BuildConfig;

/**
 * @author Павел Аннин, 09.07.2015.
 */
public abstract class PlayServiceActivity extends AppCompatActivity {
    private final static int PLAY_SERVICES_REQUEST = 1;

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REQUEST).show();
            } else {
                if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Устройство не поддерживается библиотекой Google Play Services");
                finish();
            }
            return true;
        }
        return true;
    }
}
