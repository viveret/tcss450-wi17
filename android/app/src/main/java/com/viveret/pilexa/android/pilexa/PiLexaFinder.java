package com.viveret.pilexa.android.pilexa;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 3/5/17.
 */
public class PiLexaFinder {
    private static final String LOGTAG = "Pilexa service finder";

    private int myPort;
    private String myLocalAddress;
    private List<PingServerTask> myServerTasks = new ArrayList<>();
    private Context context;
    private OnPilexaServiceFound myListener;

    public PiLexaFinder(int port, String localAddress, Context c, OnPilexaServiceFound listener) {
        myPort = port;
        myLocalAddress = localAddress;
        context = c;
        myListener = listener;
    }

    public void startSearch() {
        int startFromPort = Integer.parseInt(myLocalAddress.substring(myLocalAddress.lastIndexOf('.') + 1)) + 15;
        String prefix = myLocalAddress.substring(0, myLocalAddress.lastIndexOf('.') + 1);
        Log.d(LOGTAG, "Starting search on " + prefix + "*");
        for (int i = 0; i < 255; i ++) {
            for (int j : new int[]{-1, 1}) {
                int newPort = startFromPort + i * j;
                if (newPort > 0 && newPort < 256) {
                    String newAddress = prefix + newPort;
                    PingServerTask task = new PingServerTask();
                    myServerTasks.add(task);
                    task.execute(newAddress);
                }
            }
        }
    }

    public interface OnPilexaServiceFound {
        void onPilexaServiceFound(String address, int port);
    }

    private class PingServerTask extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... urls) {
            try {
                Log.d(LOGTAG, "Connecting to " + urls[0] + "...");
                JSONObject params = new JSONObject();
                params.put("op", "ping");

                String content = params.toString() + "\n";
                Socket socket = new Socket(urls[0], myPort);
                // socket.setSoTimeout(100);
                OutputStream os = new BufferedOutputStream(socket.getOutputStream());
                os.write(content.getBytes());
                os.flush();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                JSONObject ret = new JSONObject(buffer.readLine());
                Log.d(LOGTAG, urls[0] + ": " + ret.toString());

                if (ret.has("status")) {
                    if (ret.getInt("status") == 0) {
                        myListener.onPilexaServiceFound(urls[0], myPort);
                    } else {
                        Toast.makeText(context, "Error (" + ret.getInt("status") + "): " +
                                ret.getString("msg"), Toast.LENGTH_LONG);
                    }
                } else {
                    Log.e(LOGTAG, "Returned json does not have status field");
                }

                socket.close();
            } catch (Exception e) {
                Log.e(LOGTAG, Log.getStackTraceString(e));
            }
            myServerTasks.remove(this);
            return null;
        }
    }
}
