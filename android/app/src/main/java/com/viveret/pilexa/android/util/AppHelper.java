package com.viveret.pilexa.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import com.viveret.pilexa.android.HomeActivity;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.pilexa.UserAccount;

import java.net.MalformedURLException;

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

    public void forgetConnection() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("pilexaHost");
        editor.remove("pilexaPort");
        editor.commit();
        logout();
    }

    public static String getMacAddress(Context c) {
        WifiManager manager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    public void saveUser(UserAccount user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("userId", user.getUserId());
        editor.putString("userName", user.getName());
        editor.putString("userUsername", user.getUsername());
        editor.putString("userLang", user.getLanguage());
        editor.putString("userAuthToken", user.getUserAuthenticationToken());
        editor.commit();
    }

    public void logout() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("userId");
        editor.remove("userName");
        editor.remove("userUsername");
        editor.remove("userLang");
        editor.remove("userAuthToken");
        editor.commit();
    }

    public boolean hasSavedUser() {
        return mSharedPreferences.getString("userAuthToken", null) != null;
    }

    public PiLexaProxyConnection makeConnection() throws MalformedURLException {
        String host = mSharedPreferences.getString("pilexaHost", null);
        int port = mSharedPreferences.getInt("pilexaPort", -1);
        return PiLexaProxyConnection.attachTo(host, port);
    }
}
