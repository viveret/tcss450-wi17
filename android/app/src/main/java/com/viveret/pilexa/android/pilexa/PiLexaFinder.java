package com.viveret.pilexa.android.pilexa;

import android.content.Context;
import android.util.Log;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Locate running PiLexa instances on the local network by pinging each possible address on the local network.
 */
public class PiLexaFinder {
    private static final String LOGTAG = "Pilexa service finder";
    private static final int NUM_THREADS = 4 * 2;

    private int myPort;
    private String myLocalAddress, myPrefix;
    private Map<Thread, PingServerTask> myThreadPool = new HashMap<>(NUM_THREADS);
    private OnPilexaServiceFinderListener myListener;

    /**
     * Create a new PiLexaFinder from a port, localAdddress that will be the prefix, and listener for events.
     * @param port the port the PiLexa service is running on, default is 11283
     * @param localAddress the local address of the device, used to prefix attempted addresses
     * @param listener the listener for events the finder fires
     */
    public PiLexaFinder(int port, String localAddress, Context c, OnPilexaServiceFinderListener listener) {
        myPort = port;
        myLocalAddress = localAddress;
        myPrefix = myLocalAddress.substring(0, myLocalAddress.lastIndexOf('.') + 1);
        myListener = listener;
    }

    /**
     * Starts a parallel search for running PiLexa instances.
     * Calls the listener's methods if anything is found or happens.
     */
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

    /**
     * Halts the search if a search is still happening.
     */
    public void stopSearch() {
        for (Map.Entry<Thread, PingServerTask> e : myThreadPool.entrySet()) {
            e.getValue().myPorts.clear();
        }
        myThreadPool.clear();;
    }

    /**
     * Used to communicate to things that need to know if an instance is found or when the search has finished.
     */
    public interface OnPilexaServiceFinderListener {
        /**
         * Called when a PiLexa instance is found.
         * @param conn the pilexa connection object
         */
        void onPilexaServiceFound(PiLexaProxyConnection conn);

        /**
         * Called when all addresses have been tried.
         */
        void onPilexaFinderDoneSearching();
    }

    /**
     * Thread to process a list of suffix ports for addresses to try. Stops when there are no more ports to check.
     */
    private class PingServerTask implements Runnable {
        private List<Integer> myPorts;

        /**
         * Create a new thread for processing potential addresses.
         * @param thePorts
         */
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
