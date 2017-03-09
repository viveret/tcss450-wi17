package com.viveret.pilexa.android.pilexa;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viveret on 2/15/17.
 */

public class PiLexaProxyConnection implements Serializable {
    private static final String LOGTAG = "PiLexaProxy";
    private static final int DEFAULT_TIMEOUT = 20000; // 20 seconds

    private transient final Map<String, Object> myConfigCache = new HashMap<>();
    private transient int myTimeout = DEFAULT_TIMEOUT;
    private final URL myUrl;
    private final String myHost;
    private final int myPort;

    private PiLexaProxyConnection(URL theHost) throws MalformedURLException {
        myUrl = theHost;
        myHost = myUrl.toExternalForm();
        myPort = myUrl.getPort();
    }

    public PiLexaProxyConnection(String host, int port) throws MalformedURLException  {
        myUrl = null;
        myHost = host;
        myPort = port;
    }

    public static PiLexaProxyConnection attachTo(URL url) throws MalformedURLException {
        PiLexaProxyConnection ret = new PiLexaProxyConnection(url);
        if (ret.canConnect()) {
            return ret;
        } else {
            return null;
        }
    }

    public static PiLexaProxyConnection attachTo(String host, int port) throws MalformedURLException  {
        PiLexaProxyConnection ret = new PiLexaProxyConnection(host, port);
        if (ret.canConnect()) {
            try {
                ret.getConfigString("system.name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        } else {
            return null;
        }
    }

    public String getHost() {
        return myHost;
    }

    public int getPort() {
        return myPort;
    }

    public boolean isHttp() { return myUrl != null; }

    public void setConfig(String key, String val) throws Exception {
        JSONObject j = null;
        try {
            JSONObject args = new JSONObject();
            args.put("op", "setConfig");
            args.put("key", key);
            args.put("val", val);

            myConfigCache.put(key, val);

            j = sendRequestHttp(args);

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
                JSONObject j = sendRequest(args);

                if (j.has("status") && j.getInt("status") == 0) {
                    myConfigCache.put(key, j.getString("val"));
                    return j.getString("val");
                } else {
                    throw new Exception(j.getString("msg"));
                }
            } catch (JSONException e) {
                Log.e(LOGTAG, Log.getStackTraceString(e));
                return "[ERROR]";
            }
        }
    }

    public JSONObject getEntireConfig() throws Exception {
        try {
            JSONObject args = new JSONObject();
            args.put("op", "queryEntireConfig");
            JSONObject j = sendRequest(args);

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

    public void processPollEvents(EventPollProcessor processor) throws Exception {
        try {
            JSONObject args = new JSONObject();
            args.put("op", "pollForEvents");
            JSONObject j = sendRequest(args);

            if (j.has("status") && j.getInt("status") == 0) {
                processor.addEvents(j.getJSONArray("events"));
                processor.start();
            } else {
                throw new Exception(j.getString("msg"));
            }
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
        }
    }

    public boolean canConnect() {
        try {
            JSONObject args = new JSONObject();
            args.put("op", "ping");
            JSONObject j = sendRequest(args);

            return j != null && j.has("status") && j.getInt("status") == 0;
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            return false;
        }
    }

    public JSONObject sendRequest(JSONObject params) {
        if (isHttp()) {
            return sendRequestHttp(params);
        } else {
            return sendRequestPilexaProtocol(params);
        }
    }

    private JSONObject sendRequestHttp(JSONObject params) {
        try {
            String content = params.toString();
            URL tmpUrl = new URL(myUrl.toExternalForm());
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(myTimeout);
            conn.setConnectTimeout(myTimeout);
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
                return new JSONObject(sb.toString());
            } else {
                os.close();
                return new JSONObject("{\"msg\"=\"error code " + code + "\", \"status\"=" + code + "}");
            }
        } catch (Exception e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            JSONObject ret = new JSONObject();
            try {
                ret.put("msg", e.getMessage());
                ret.put("status", -1);
            } catch (JSONException e1) {
                Log.e(LOGTAG, Log.getStackTraceString(e1));
            }
            return ret;
        }
    }

    private JSONObject sendRequestPilexaProtocol(JSONObject params) {
        try {
            Socket socket = new Socket(getHost(), getPort());
            socket.setSoTimeout(myTimeout);
            OutputStream os = new BufferedOutputStream(socket.getOutputStream());
            os.write((params.toString() + "\n").getBytes());
            os.flush();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JSONObject ret = new JSONObject(buffer.readLine());

            return ret;
        } catch (ConnectException e) {
            Log.e(LOGTAG, "Error connecting to " + getHost() + ": " + e.getMessage());
            JSONObject ret = new JSONObject();
            try {
                ret.put("msg", e.getMessage());
                ret.put("status", -1);
            } catch (JSONException e1) {
                Log.e(LOGTAG, Log.getStackTraceString(e1));
            }
            return ret;
        } catch (IOException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            JSONObject ret = new JSONObject();
            try {
                ret.put("msg", e.getMessage());
                ret.put("status", -1);
            } catch (JSONException e1) {
                Log.e(LOGTAG, Log.getStackTraceString(e1));
            }
            return ret;
        } catch (JSONException e) {
            Log.e(LOGTAG, Log.getStackTraceString(e));
            JSONObject ret = new JSONObject();
            try {
                ret.put("msg", e.getMessage());
                ret.put("status", -1);
            } catch (JSONException e1) {
                Log.e(LOGTAG, Log.getStackTraceString(e1));
            }
            return ret;
        }
    }

    public String sendMessage(String msg) throws Exception {
        try {
            JSONObject args = new JSONObject();
            args.put("op", "interpret");
            args.put("msg", msg);
            JSONObject j = sendRequest(args);

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
