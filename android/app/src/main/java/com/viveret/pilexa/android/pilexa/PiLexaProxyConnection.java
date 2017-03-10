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
 * Proxy for the Daemon input interface for a PiLexa service.
 * Also a helper class to perform common actions.
 */
public class PiLexaProxyConnection implements Serializable {
    private static final String LOGTAG = "PiLexaProxy";
    private static final int DEFAULT_TIMEOUT = 20000; // 20 seconds

    private transient final Map<String, Object> myConfigCache = new HashMap<>();
    private transient int myTimeout = DEFAULT_TIMEOUT;
    private final URL myUrl;
    private final String myHost;
    private final int myPort;

    /**
     * Create a new connection to a protocol specific host.
     * @param theHost the host location to connect to
     * @throws MalformedURLException if theHost is not a valid URL
     */
    private PiLexaProxyConnection(URL theHost) throws MalformedURLException {
        myUrl = theHost;
        myHost = myUrl.toExternalForm();
        myPort = myUrl.getPort();
    }

    /**
     * Create a new connection to a PiLexa socket location.
     * @param host the ip address or host to connect to.
     * @param port the port to connect to, default is 11283
     * @throws MalformedURLException if the host or port are invalid
     */
    private PiLexaProxyConnection(String host, int port) throws MalformedURLException  {
        myUrl = null;
        myHost = host;
        myPort = port;
    }

    /**
     * Attach the host url to a new PiLexa instance that is running.
     * @param url the host location to connect to
     * @return a PiLexa connection that is connected
     * @throws MalformedURLException if the host is not a valid URL.
     */
    public static PiLexaProxyConnection attachTo(URL url) throws MalformedURLException {
        PiLexaProxyConnection ret = new PiLexaProxyConnection(url);
        if (ret.canConnect()) {
            return ret;
        } else {
            return null;
        }
    }

    /**
     * Attach the host url to a new PiLexa instance that is running.
     * @param host the host location to connect to
     * @param port the port to connect to, default is 11283
     * @return a PiLexa connection that is connected
     * @throws MalformedURLException if the host is not a valid URL.
     */
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

    /**
     * Returns the host of the PiLexa connection.
     * @return the host of the PiLexa connection.
     */
    public String getHost() {
        return myHost;
    }

    /**
     * Returns the port of the PiLexa connection.
     * @return the port of the PiLexa connection.
     */
    public int getPort() {
        return myPort;
    }

    /**
     * Returns if the connection is through HTTP.
     * @return if the connection is through HTTP.
     */
    public boolean isHttp() { return myUrl != null; }

    /**
     * Sets a configuration option in the PiLexa instance.
     * @param key the key of the variable
     * @param val the new value for the variable
     * @throws Exception if an issue occurred while connecting or sending or receiving data
     */
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

    /**
     * Gets a configuration option in the PiLexa instance.
     * @param key the key of the variable
     * @return a configuration option in the PiLexa instance.
     * @throws Exception if an issue occurred while connecting or sending or receiving data
     */
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

    /**
     * Gets the entire configuration collection from the PiLexa instance.
     * @return the entire configuration collection from the PiLexa instance.
     * @throws Exception if an issue occurred while connecting or sending or receiving data
     */
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

    /**
     * Polls waiting events from the PiLexa event queue.
     * @param processor the processor to add the events that need processing to
     * @throws Exception if an issue occurred while connecting or sending or receiving data
     */
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

    /**
     * Returns if the proxy connection can successfully communicate with the host PiLexa instance.
     * @return if the proxy connection can successfully communicate with the host PiLexa instance.
     */
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

    /**
     * Sends a request to the host of the PiLexa instance.
     * @param params the parameters to send (through the specified connection protocol)
     * @return the received message from the PiLexa instance regarding the parameters sent.
     */
    public JSONObject sendRequest(JSONObject params) {
        if (isHttp()) {
            return sendRequestHttp(params);
        } else {
            return sendRequestPilexaProtocol(params);
        }
    }

    /**
     * Sends a request to the host of the PiLexa instance through HTTP.
     * @param params the parameters to send
     * @return the received message from the PiLexa instance regarding the parameters sent.
     */
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

    /**
     * Sends a request to the host of the PiLexa instance using the PiLexa protocol.
     * @param params the parameters to send
     * @return the received message from the PiLexa instance regarding the parameters sent.
     */
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

    /**
     * Send a message to the PiLexa that should be interpreted.
     * @param msg the message to tell the PiLexa
     * @return what the PiLexa responded back with
     * @throws Exception if an issue occurred while connecting or sending or receiving data
     */
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

    /**
     * Interface for Objects to get instances of PiLexa proxy connections.
     */
    public interface PiLexaProxyConnectionHolder {
        /**
         * Returns the current PiLexa connection the implementing Object has.
         * @return the current PiLexa connection the implementing Object has.
         */
        PiLexaProxyConnection getPilexa();
    }
}
