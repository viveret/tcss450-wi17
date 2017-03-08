package com.viveret.pilexa.pi.inputmethods;

import com.viveret.pilexa.pi.AbstractPiLexaServiceProxy;
import com.viveret.pilexa.pi.event.EventPoll;
import com.viveret.pilexa.pi.InputSource;
import com.viveret.pilexa.pi.PiLexaService;
import com.viveret.pilexa.pi.user.UserAccount;
import com.viveret.pilexa.pi.user.UserManager;
import com.viveret.pilexa.pi.util.Config;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by viveret on 2/4/17.
 * Most important method is {DaemonProxy.DaemonEntryPoint#performRequest}
 */
public class DaemonProxy extends AbstractPiLexaServiceProxy implements InputSource {
    private PiLexaService inst;
    private Thread myThread;
    private ServerSocket mySocket;
    private List<DaemonEntryPoint> clients = new ArrayList<>();

    @Override
    public void run() {
        do {
            try {
                getLog().info("Waiting for client to connect...");
                DaemonEntryPoint client = new DaemonEntryPoint(mySocket.accept());
                inst.getLog().info("Client " + client.mySocket.getInetAddress().getCanonicalHostName() + " connected");
                clients.add(client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (isConnected());
    }

    @Override
    public void start(PiLexaService piLexaService) {
        try {
            mySocket = new ServerSocket(Config.inst().getInt("server.port"));
        } catch (IOException e) {
            piLexaService.getLog().error("Creating socket", e);
            return;
        }
        connect();
        inst = piLexaService;
        myThread = new Thread(this);
        myThread.start();
    }

    @Override
    public void stop() {
        try {
            mySocket.close();
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (DaemonEntryPoint e : clients) {
            e.stop();
        }
    }

    @Override
    public void shutdown() {
        stop();
    }

    @Override
    public String getInput() throws IOException {
        throw new IOException("NotImplemented");
    }

    /**
     * Created by viveret on 2/21/17.
     */
    public class DaemonEntryPoint implements Runnable {
        private final Socket mySocket;
        private final Thread myThread;

        public DaemonEntryPoint(Socket theSocket) {
            mySocket = theSocket;
            myThread = new Thread(this);
        }

        @Override
        public void run() {
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                OutputStream os = mySocket.getOutputStream();
                performRequest(bf, os);
            } catch (IOException e) {
                inst.getLog().error("Get input", e);
            } finally {
                if (mySocket.isConnected()) {
                    inst.getLog().info("Client " + mySocket.getInetAddress().getHostName() + " disconnected");
                    try {
                        mySocket.close();
                    } catch (IOException e) {
                        inst.getLog().error("Socket close", e);
                    }
                }
                clients.remove(this);
            }
        }

        private void performRequest(BufferedReader in, OutputStream out) throws IOException {
            boolean stillRequiresInput = false;
            do {
                JSONObject jin = new JSONObject(in.readLine());
                JSONObject jout = new JSONObject();
                String msg = "OK";
                int status = 0;

                if (!jin.has("op")) {
                    status = 1;
                    msg = "key 'op' is required, no operation specified.";
                } else {
                    switch (jin.getString("op")) {
                        case "ping": {
                        } break;
                        case "interpret":
                            if (jin.has("msg")) {
                                msg = inst.interpret(jin.getString("msg"));
                            } else {
                                msg = "Missing msg";
                                status = 1;
                            }
                            break;
                        case "queryConfig":
                            if (jin.has("key")) {
                                Object theVal = getConfig().get(jin.getString("key"));
                                if (theVal != null) {
                                    jout.put("val", theVal);
                                } else {
                                    msg = "Invalid key";
                                    status = 1;
                                }
                            } else {
                                msg = "Missing key";
                                status = 1;
                            }
                            break;
                        case "queryEntireConfig":
                            jout.put("val", getConfig().getRoot());
                            break;
                        case "setConfig":
                            if (jin.has("key")) {
                                if (jin.has("val")) {
                                    jout.put("val", getConfig().getRoot());
                                } else {
                                    msg = "Missing value";
                                    status = 1;
                                }
                            } else {
                                msg = "Missing key";
                                status = 1;
                            }
                            break;
                        case "login":
                            if (jin.has("username") && jin.has("password")) {
                                // Basic authentication
                                msg = "Not implemented";
                                status = 1;
                            }
                            break;
                        case "createAccount":
                            if (jin.has("mac")) {
                                if (jin.has("username") && jin.has("password")) {
                                    UserAccount user = UserManager.inst().createNewUser(
                                            jin.getString("username"),
                                            jin.getString("password"),
                                            jin.getString("mac"));
                                    if (user != null) {
                                        jout.put("user", user.getRoot());
                                    } else {
                                        msg = "User was null";
                                        status = 1;
                                    }
                                } else {
                                    msg = "Missing username and/or password";
                                    status = 1;
                                }
                            } else {
                                msg = "Missing mac address";
                                status = 1;
                            }
                            break;
                        case "pollForEvents":
                            Deque<JSONObject> queue = EventPoll.inst().getQueue();
                            JSONArray evs = new JSONArray();
                            while (!queue.isEmpty()) {
                                evs.put(queue.pollLast());
                            }
                            jout.put("events", evs);
                            break;
                        default:
                            msg = "Invalid operation";
                            status = 1;
                            break;
                    }
                }

                jout.put("msg", msg);
                jout.put("status", status);
                out.write(jout.toString().getBytes());
                out.write("\n".getBytes());

                if (status != 0) {
                    stillRequiresInput = false;
                }
            } while (stillRequiresInput && isConnected());
        }

        public void start() {
            myThread.start();
        }

        public void stop() {
            try {
                myThread.join(1000);
                if (myThread.isAlive()) {
                    myThread.interrupt();
                }
                if (mySocket.isConnected()) {
                    mySocket.close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
