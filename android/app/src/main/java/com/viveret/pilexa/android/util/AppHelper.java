package com.viveret.pilexa.android.util;

import android.content.SharedPreferences;

/**
 * Created by viveret on 3/5/17.
 */
public class AppHelper {
    private static final String KEY_HAS_SAVED_CONNECTION = "hasSavedConnection";
    private final SharedPreferences mSharedPreferences;

    public AppHelper(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public boolean hasSavedConnection() {
        return mSharedPreferences.getBoolean(KEY_HAS_SAVED_CONNECTION, false);
    }

    public boolean setHasSavedConnection(boolean val) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(KEY_HAS_SAVED_CONNECTION, val);

        // Commit changes to SharedPreferences.
        return editor.commit();
    }
}
