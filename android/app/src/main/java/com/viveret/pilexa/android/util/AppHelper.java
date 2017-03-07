package com.viveret.pilexa.android.util;

import android.content.SharedPreferences;

import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;

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
        return mSharedPreferences.getString("pilexaHost", null) != null;
    }

    public void saveConnection(PiLexaProxyConnection conn) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("pilexaHost", conn.getHost());
        editor.putInt("pilexaPort", conn.getPort());
        editor.commit();
    }
}
