package ru.itlab.findchild;

import android.app.Application;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

import ru.itlab.findchild.util.PrefsUtil;

/**
 * Created by Pavel on 08.07.2015.
 */
public class App extends Application {

    private static App mInstance;
    private PrefsUtil mPrefs;
    private JobManager mJobManager;

    public App() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = new PrefsUtil(this);
        initJobManager();
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

    private void initJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    @Override
                    public boolean isDebugEnabled() {
                        return BuildConfig.DEBUG;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(BuildConfig.TAG, String.format(text, args));
                    }
                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(BuildConfig.TAG, String.format(text, args), t);
                    }
                    @Override
                    public void e(String text, Object... args) {
                        Log.e(BuildConfig.TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .build();
        mJobManager = new JobManager(this, configuration);
    }

    public JobManager getJobManager() {
        return mJobManager;
    }


}
