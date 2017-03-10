package com.viveret.pilexa.android.pilexa;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.viveret.pilexa.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for UserAccounts to abstract getting a user from the devices cache or from logging in or
 * registering with the PiLexa instance.
 */
public class UserAccountFactory {
    private static final String LOGTAG = "UserAccountFactory";

    private final PiLexaProxyConnection myConnection;

    /**
     * Creates a new factory from an active PiLexa connection.
     * @param myConnection the connection for the PiLexa
     */
    public UserAccountFactory(PiLexaProxyConnection myConnection) {
        this.myConnection = myConnection;
    }

    /**
     * Gets a user from the devices cache.
     * @param prefs the shared preferences where the data is stored
     * @return  a user from the devices cache.
     * @throws Exception if the user does not exist in the cache.
     */
    public static UserAccount getCachedUser(SharedPreferences prefs) throws Exception {
        if (prefs.contains("userId")) {
            int userId = prefs.getInt("userId", -1);
            String name = prefs.getString("name", null);
            String username = prefs.getString("username", null);
            String language = prefs.getString("lang", null);
            String authToken = prefs.getString("authToken", null);
            return new UserAccount(userId, name, username, language, authToken);
        } else {
            throw new Exception("Cached user does not exist");
        }
    }

    /**
     * Returns if there is a saved instance of a user in the devices cache preferences.
     * @param prefs the shared preferences where the data is stored
     * @return if there is a saved instance of a user in the devices cache preferences.
     */
    public static boolean hasCachedUser(SharedPreferences prefs) {
        try {
            return getCachedUser(prefs) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Attempts to login using a user's credentials.
     * @param acct the user that has the credentials
     * @return if the login succeeded
     */
    public boolean tryLogin(UserAccount acct) {
        JSONObject params = new JSONObject();
        try {
            if (acct.hasAuthToken()) {
                params.put("authToken", acct.getUserAuthenticationToken());
            } else {
                throw new IllegalArgumentException("User must have authentication token to login");
            }

            if (acct.hasUserId()) {
                params.put("userId", acct.getUserId());
            } else {
                params.put("username", acct.getUsername());
            }
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return false;
        }

        JSONObject res = myConnection.sendRequest(params);

        try {
            return res != null && res.has("status") && res.getInt("status") == 0;
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return false;
        }
    }

    /**
     * Logs into and returns the user by logging into the PiLexa with a username, password, and device mac.
     * @param username the username of the user
     * @param password the password of the user
     * @param mac the mac address of the device
     * @return the user's info if successfully logged in, otherwise null
     * @throws Exception if there was an error while trying to perform the request
     */
    public UserAccount loginWithUsernameAndPassword(String username, String password, String mac) throws Exception {
        JSONObject params = new JSONObject();
        try {
            params.put("op", "login");
            params.put("username", username);
            params.put("password", password);
            params.put("mac", mac);
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return null;
        }

        return getAccountFromRes(myConnection.sendRequest(params));
    }

    /**
     * Registers and returns the user by registering the user on the PiLexa with a username, password, and device mac.
     * @param username the username of the user
     * @param password the password of the user
     * @param macAddress the mac address of the device
     * @return the user's info if successfully registered in, otherwise null
     * @throws Exception if there was an error while trying to perform the request
     */
    public UserAccount registerWithUsernameAndPassword(String username, String password, String macAddress) throws Exception {
        JSONObject params = new JSONObject();
        try {
            params.put("op", "createAccount");
            params.put("username", username);
            params.put("password", password);
            params.put("mac", macAddress);
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return null;
        }

        return getAccountFromRes(myConnection.sendRequest(params));
    }

    /**
     * Decodes a user account object from a JSON object
     * @param res the JSON object the data is contained in
     * @return a user account object from a JSON object
     * @throws Exception if there was an error while decoding
     */
    private UserAccount getAccountFromRes(JSONObject res) throws Exception {
        try {
            if (res != null ) {
                if (res.has("status") && res.getInt("status") == 0) {
                    return createAccountFromJson(res.getJSONObject("user"));
                } else {
                    throw new Exception(res.getString("msg"));
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return null;
        }
    }

    /**
     * Decodes a user account object from a JSON object
     * @param user the JSON object the data is contained in
     * @return a user account object from a JSON object
     * @throws Exception if there was an error while decoding
     */
    private UserAccount createAccountFromJson(JSONObject user) throws JSONException {
        int userId = user.getInt("id");
        String name = user.getString("name");
        String username = user.getString("username");
        String language = user.getString("lang");
        String authToken = "";//user.getString("authToken");
        UserAccount acct = new UserAccount(userId, name, username, language, authToken);

        JSONArray ar = user.getJSONArray("devices");
        for (int i = 0; i < ar.length(); i++) {
            acct.addDevice(new UserAccount.UserDevice(ar.getString(i)));
        }

        return acct;
    }
}
