package ru.itlab.findchild.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

import ru.itlab.findchild.BuildConfig;

public class GpsService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = BuildConfig.TIME_DELAY_GPS / 2;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Инициализация сервиса");
        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";
        //jobManager = ((SeconApplication) getApplication()).getJobManager();
        gpsHandler.post(gpsTask);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        changeGpsLocation();
    }

    @Override
    public void onDestroy() {
        gpsHandler.removeCallbacks(gpsTask);
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        mGoogleApiClient.disconnect();
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Остоновка сервиса");
        super.onDestroy();
    }

    private synchronized void buildGoogleApiClient() {
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(BuildConfig.TIME_DELAY_GPS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "StartLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "StopLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void changeGpsLocation() {
        if (mCurrentLocation != null) {
            if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, String.format(
                    "Координаты: Широта - %f, Долгота - %f, Время - %s",
                    mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude(),
                    mLastUpdateTime
            ));
        }
    }

    private Handler gpsHandler = new Handler();
    private Runnable gpsTask = new Runnable() {
        @Override
        public void run() {
            if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Определение кооринатов GPS");
            //jobManager.addJobInBackground(new DownloadDataJob(TypeModelEnum.TRACKS));
           // gpsHandler.postDelayed(gpsTask, BuildConfig.TIME_DELAY_GPS);
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            changeGpsLocation();
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        changeGpsLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (BuildConfig.DEBUG) Log.i(BuildConfig.TAG, "Connection failed: ConnectionResult.getErrorCode() = " +
                connectionResult.getErrorCode());
    }
}
