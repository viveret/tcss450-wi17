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
 * Created by viveret on 3/7/17.
 */
public class UserAccountFactory {
    private static final String LOGTAG = "UserAccountFactory";

    private final PiLexaProxyConnection myConnection;

    public UserAccountFactory(PiLexaProxyConnection myConnection) {
        this.myConnection = myConnection;
    }

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

    public static boolean hasCachedUser(SharedPreferences prefs) {
        try {
            return getCachedUser(prefs) != null;
        } catch (Exception e) {
            return false;
        }
    }

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

        JSONObject res = myConnection.sendRequest(params);

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

    private UserAccount createAccountFromJson(JSONObject user) throws JSONException {
        int userId = user.getInt("id");
        String name = user.getString("name");
        String username = user.getString("username");
        String language = user.getString("lang");
        String authToken = user.getString("authToken");
        UserAccount acct = new UserAccount(userId, name, username, language, authToken);

        JSONArray ar = user.getJSONArray("devices");
        for (int i = 0; i < ar.length(); i++) {
            acct.addDevice(new UserAccount.UserDevice(ar.getString(i)));
        }

        return acct;
    }
}
