package com.viveret.pilexa.android.util;

import android.util.Log;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by viveret on 3/7/17.
 */
public class UserAccountFactory {
    private static final String LOGTAG = "UserAccountFactory";

    private final PiLexaProxyConnection myConnection;

    public UserAccountFactory(PiLexaProxyConnection myConnection) {
        this.myConnection = myConnection;
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

    public UserAccount loginWithUsernameAndPassword(String username, String password) {
        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("password", password);
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return null;
        }

        JSONObject res = myConnection.sendRequest(params);

        try {
            return res != null && res.has("status") && res.getInt("status") == 0;
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return false;
        }
    }
}
