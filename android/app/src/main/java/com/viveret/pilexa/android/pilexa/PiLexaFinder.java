package com.viveret.pilexa.android.pilexa;

import android.content.Context;
import android.util.Log;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viveret on 3/5/17.
 */
public class PiLexaFinder {
    private static final String LOGTAG = "Pilexa service finder";
    private static final int NUM_THREADS = 4;

    private int myPort;
    private String myLocalAddress, myPrefix;
    private Map<Thread, PingServerTask> myThreadPool = new HashMap<>(NUM_THREADS);
    private Context context;
    private OnPilexaServiceFinderListener myListener;

    public PiLexaFinder(int port, String localAddress, Context c, OnPilexaServiceFinderListener listener) {
        myPort = port;
        myLocalAddress = localAddress;
        myPrefix = myLocalAddress.substring(0, myLocalAddress.lastIndexOf('.') + 1);
        context = c;
        myListener = listener;
    }

    public void startSearch() {
        int startFromPort = Integer.parseInt(myLocalAddress.substring(myLocalAddress.lastIndexOf('.') + 1));
        Log.d(LOGTAG, "Starting search on " + myPrefix + "*:" + myPort);

        List<List<Integer>> dividedWork = new ArrayList<>(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            dividedWork.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < 255; i++) {
            for (int j : new int[]{-1, 1}) {
                int newPort = startFromPort + i * j;
                if (newPort > 0 && newPort < 256) {
                    dividedWork.get(newPort % dividedWork.size()).add(newPort);
                }
            }
        }

        for (int i = 0; i < dividedWork.size(); i++) {
            PingServerTask r = new PingServerTask(dividedWork.get(i));
            Thread t = new Thread(r);
            myThreadPool.put(t, r);
            t.start();
        }
    }

    public void stopSearch() {
        for (Map.Entry<Thread, PingServerTask> e : myThreadPool.entrySet()) {
            e.getValue().myPorts.clear();
        }
        myThreadPool.clear();;
    }

    public interface OnPilexaServiceFinderListener {
        void onPilexaServiceFound(PiLexaProxyConnection conn);
        void onPilexaFinderDoneSearching();
    }

    private class PingServerTask implements Runnable {
        private List<Integer> myPorts;

        public PingServerTask(List<Integer> thePorts) {
            this.myPorts = thePorts;
        }

        @Override
        public void run() {
            while (myPorts.size() > 0) {
                int newPort = myPorts.get(0);
                myPorts.remove(0);
                PiLexaProxyConnection testConn = null;
                try {
                    testConn = PiLexaProxyConnection.attachTo(myPrefix + newPort, myPort);
                    if (testConn != null) {
                        myListener.onPilexaServiceFound(testConn);
                    }
                } catch (MalformedURLException e) {
                    Log.e(LOGTAG, Log.getStackTraceString(e));
                }
            }
            myThreadPool.remove(Thread.currentThread());
        }
    }
}
