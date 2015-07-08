package ru.itlab.findchild.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.itlab.findchild.R;

/**
 * @author Павел Аннин, 08.07.2015.
 */
public class PrefsUtil {

    public final String KEY_USER_KEY;
    private SharedPreferences mShPrefs;

    public PrefsUtil(Context context) {
        this.KEY_USER_KEY = context.getString(R.string.pref_user_key);
        mShPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String loadUserKey() {
        return mShPrefs.getString(KEY_USER_KEY, null);
    }

    public void saveUserKey(String userKey) {
        SharedPreferences.Editor editor = mShPrefs.edit();
        editor.putString(KEY_USER_KEY, userKey);
        editor.apply();
    }
}
