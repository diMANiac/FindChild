package ru.itlab.findchild;

import android.app.Application;

import ru.itlab.findchild.util.PrefsUtil;

/**
 * Created by Pavel on 08.07.2015.
 */
public class App extends Application {

    private static App mInstance;
    private PrefsUtil mPrefs;

    public App() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = new PrefsUtil(this);
    }

    public static App getInstance() {
        return mInstance;
    }

    public String getUserKey() {
        return mPrefs != null ? mPrefs.loadUserKey() : null;
    }

    public boolean setUserKey(String userKey) {
        if (mPrefs != null) {
            mPrefs.saveUserKey(userKey);
            return true;
        }
        return false;
    }


}
