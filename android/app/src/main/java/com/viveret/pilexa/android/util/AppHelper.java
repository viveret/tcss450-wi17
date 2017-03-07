package com.viveret.pilexa.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

    public void logout() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("pilexaHost");
        editor.remove("pilexaPort");
        editor.commit();
    }

    public void saveConnection(PiLexaProxyConnection conn) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("pilexaHost", conn.getHost());
        editor.putInt("pilexaPort", conn.getPort());
        editor.commit();
    }

    public static String getMacAddress(Context c) {
        WifiManager manager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }
}
