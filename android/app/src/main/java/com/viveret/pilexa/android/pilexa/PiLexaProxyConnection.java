package com.viveret.pilexa.android.pilexa;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viveret on 2/15/17.
 */

public class PiLexaProxyConnection {
    private static final String LOGTAG = "PiLexaProxy";
    private final Map<String, Object> myConfigCache = new HashMap<>();

    private URL myUrl;

    private PiLexaProxyConnection(String theHost) throws MalformedURLException {
        myUrl = new URL(theHost);
    }

    public static PiLexaProxyConnection attachTo(String host) throws ConnectException, MalformedURLException {
        PiLexaProxyConnection ret = new PiLexaProxyConnection(host);
        if (ret.canConnect()) {
            return ret;
        } else {
            throw new ConnectException("Could not connect");
        }
    }

    public String getHost() {
        return myUrl.getHost();
    }

    public int getPort() {
        return myUrl.getPort();
    }

    public void setConfig(String key, String val) throws Exception {
        JSONObject j = null;
        try {
            JSONObject args = new JSONObject();
            args.put("op", "setConfig");
            args.put("key", key);
            args.put("val", val);

            myConfigCache.put(key, val);

            j = new JSONObject(getStringFromRequest(args));

            if (j.has("status") && j.getInt("status") == 0) {
            } else {
                throw new Exception(j.getString("msg"));
            }
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
        }
    }

    public String getConfigString(String key) throws Exception {
        if (myConfigCache.containsKey(key)) {
            return (String) myConfigCache.get(key);
        } else {
            try {
                JSONObject args = new JSONObject();
                args.put("op", "queryConfig");
                args.put("key", key);
                JSONObject j = new JSONObject(getStringFromRequest(args));

                if (j.has("status") && j.getInt("status") == 0) {
                    myConfigCache.put(key, j.getString("val"));
                    return j.getString("val");
                } else {
                    throw new Exception(j.getString("msg"));
                }
            } catch (JSONException e) {
                Log.e(LOGTAG, Log.getStackTraceString(e));
                return null;
            }
        }
    }

    public JSONObject getEntireConfig() throws Exception {
        try {
            JSONObject args = new JSONObject();
            args.put("op", "queryEntireConfig");
            JSONObject j = new JSONObject(getStringFromRequest(args));

            if (j.has("status") && j.getInt("status") == 0) {
                return j.getJSONObject("val");
            } else {
                throw new Exception(j.getString("msg"));
            }
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public boolean canConnect() {
        try {
            JSONObject args = new JSONObject();
            args.put("op", "canConnect");
            JSONObject j = new JSONObject(getStringFromRequest(args));

            return j.has("status") && j.getInt("status") == 0;
        } catch (Exception e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return false;
        }
    }

    public String getStringFromRequest(JSONObject params) {
        try {
            String content = params.toString();
            URL tmpUrl = new URL(myUrl.toExternalForm());
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setFixedLengthStreamingMode(content.getBytes().length);
            conn.connect();

            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(content.getBytes());
            os.flush();

            int code = conn.getResponseCode();
            if (code == 200) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = buffer.readLine()) != null) {
                    sb.append(s);
                }

                os.close();
                return sb.toString();
            } else {
                os.close();
                return "{\"msg\"=\"error code " + code + "\", \"status\"=" + code + "}";
            }
        } catch (Exception e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public String sendMessage(String msg) throws Exception {
        try {
            JSONObject args = new JSONObject();
            args.put("op", "interpret");
            args.put("val", msg);
            JSONObject j = new JSONObject(getStringFromRequest(args));

            if (j.has("status") && j.getInt("status") == 0) {
                Log.i(LOGTAG, "Message received: " + j.getString("msg"));
                return j.getString("msg");
            } else {
                throw new Exception(j.getString("msg"));
            }
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return null;
        }
    }


    public interface PiLexaProxyConnectionHolder {
        PiLexaProxyConnection getPilexa();
    }
}
