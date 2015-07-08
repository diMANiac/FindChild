package ru.itlab.findchild.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import ru.itlab.findchild.BuildConfig;

public class GpsService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Инициализация сервиса");
        //jobManager = ((SeconApplication) getApplication()).getJobManager();
        gpsHandler.post(gpsTask);
    }

    @Override
    public void onDestroy() {
        gpsHandler.removeCallbacks(gpsTask);
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Остоновка сервиса");
        super.onDestroy();
    }

    private Handler gpsHandler = new Handler();
    private Runnable gpsTask = new Runnable() {
        @Override
        public void run() {
            if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Определение кооринатов GPS");
            //jobManager.addJobInBackground(new DownloadDataJob(TypeModelEnum.TRACKS));
            gpsHandler.postDelayed(gpsTask, BuildConfig.TIME_DELAY_GPS);
        }
    };
    }
