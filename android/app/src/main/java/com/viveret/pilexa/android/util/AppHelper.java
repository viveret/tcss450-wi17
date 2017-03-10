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
 * Helper class for application globals and commonly used utility methods.
 */
public class AppHelper {
    private static final String KEY_HAS_SAVED_CONNECTION = "hasSavedConnection";
    private final SharedPreferences mSharedPreferences;

    /**
     * Creates a new app helper from shared preferences where the data is stored.
     * @param mSharedPreferences the shared preferences
     */
    public AppHelper(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    /**
     * Returns if the app has a saved connection to a PiLexa instance.
     * @return if the app has a saved connection to a PiLexa instance.
     */
    public boolean hasSavedConnection() {
        return mSharedPreferences.getString("pilexaHost", null) != null;
    }

    /**
     * Saves a PiLexa connection to the app's cache.
     * @param conn the connection to store.
     */
    public void saveConnection(PiLexaProxyConnection conn) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("pilexaHost", conn.getHost());
        editor.putInt("pilexaPort", conn.getPort());
        editor.commit();
    }

    /**
     * Forgets a PiLexa connection if one exists.
     */
    public void forgetConnection() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("pilexaHost");
        editor.remove("pilexaPort");
        editor.commit();
        logout();
    }

    /**
     * Gets the mac address of the device.
     * @param c the context to get the mac address from
     * @return the mac address of the device.
     */
    public static String getMacAddress(Context c) {
        WifiManager manager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * Saves a user to the app's cache.
     * @param user the user to store.
     */
    public void saveUser(UserAccount user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("userId", user.getUserId());
        editor.putString("userName", user.getName());
        editor.putString("userUsername", user.getUsername());
        editor.putString("userLang", user.getLanguage());
        editor.putString("userAuthToken", user.getUserAuthenticationToken());
        editor.commit();
    }

    /**
     * Logs the user out, if they are logged in and deletes the data from the app's cache.
     */
    public void logout() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("userId");
        editor.remove("userName");
        editor.remove("userUsername");
        editor.remove("userLang");
        editor.remove("userAuthToken");
        editor.commit();
    }

    /**
     * Returns if the app has a saved user (already loggged in).
     */
    public boolean hasSavedUser() {
        return mSharedPreferences.getString("userAuthToken", null) != null;
    }

    /**
     * Gets the PiLexa proxy connection from the saved instance.
     * @return the PiLexa proxy connection from the saved instance.
     * @throws MalformedURLException if the saved host url is invalid
     */
    public PiLexaProxyConnection makeConnection() throws MalformedURLException {
        String host = mSharedPreferences.getString("pilexaHost", null);
        int port = mSharedPreferences.getInt("pilexaPort", -1);
        return PiLexaProxyConnection.attachTo(host, port);
    }
}
