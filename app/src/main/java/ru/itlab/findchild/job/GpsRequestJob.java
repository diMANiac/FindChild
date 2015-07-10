package ru.itlab.findchild.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.io.DataOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import ru.itlab.findchild.BuildConfig;


/**
 * @author Павел Аннин, 09.07.2015.
 */
public class GpsRequestJob extends Job {

    public static final int SERVER_CONNECT_TIMEOUT = 5000;
    public static final int SERVER_READ_TIMEOUT = 20000;

    private Param mParam;

    public GpsRequestJob(Param mParam) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("gps"));
        this.mParam = mParam;
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRun() throws Throwable {

        long startTime = System.currentTimeMillis();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BuildConfig.URL_REQUEST_GPS);

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(SERVER_CONNECT_TIMEOUT);
            connection.setReadTimeout(SERVER_READ_TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            if (BuildConfig.DEBUG) Log.d(BuildConfig.TAG, "Передаются параметры: " + mParam.toString());
            dataOutputStream.writeBytes(mParam.toString());
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            if (BuildConfig.DEBUG) Log.d(BuildConfig.TAG, "Ответ сервера: " + responseCode);
            if (BuildConfig.DEBUG) Log.d(BuildConfig.TAG, "Запрос завершена за " + ((System.currentTimeMillis() - startTime) / 1000) + " секунд");

        }  finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    @Override
    protected void onCancel() {}

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        if (BuildConfig.DEBUG) Log.w(BuildConfig.TAG, "Ошибка при запросе", throwable);
        return false;
    }

    public static class Param implements Serializable {

        private String mUserCode;
        private double mLatitude;
        private double mLongitude;
        private double mAltitude;

        public Param(String mUserCode, double mLatitude, double mLongitude, double mAltitude) {
            this.mUserCode = mUserCode;
            this.mLatitude = mLatitude;
            this.mLongitude = mLongitude;
            this.mAltitude = mAltitude;
        }

        @Override
        public String toString() {
            return String.format(
                    Locale.ENGLISH,
                    "position[user_id]=%s&position[x]=%f&position[y]=%f&position[z]=%f",
                    mUserCode,
                    mLatitude,
                    mLongitude,
                    mAltitude
            );
        }

        private String encodeUTF(String s) {
            String encode = "";
            try {
                encode = URLEncoder.encode(s, "utf-8").replace("\\+", "%20");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return encode;
        }
    }
}
