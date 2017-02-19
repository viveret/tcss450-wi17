package com.viveret.pilexa.pi.inputmethods;

import com.viveret.pilexa.pi.AbstractPiLexaServiceProxy;
import com.viveret.pilexa.pi.InputSource;
import com.viveret.pilexa.pi.PiLexaService;
import com.viveret.pilexa.pi.util.Config;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viveret on 2/4/17.
 */
public class ServerPiLexaProxy extends AbstractPiLexaServiceProxy implements InputSource {
    private PiLexaService inst;
    private Thread myThread;
    private ServerSocket mySocket;
    private Map<Socket, Thread> clients = new HashMap<>();

    @Override
    public void run() {
        do {
            try {
                getLog().info("v");
                final Socket clientSocket = mySocket.accept();
                getLog().info("w");
                final Thread t = new Thread(() -> {
                    getLog().info("Client " + clientSocket.getInetAddress().getCanonicalHostName() + " connected");
                    getLog().info("a");
                    try {
                        getLog().info("b");
                        //BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        InputStream is = clientSocket.getInputStream();
                        getLog().info("c");
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        getLog().info("d");
                        String in;
                        getLog().info("e");
                        getLog().info("Pre read");

                        byte[] buf = new byte[1000];
                        int sz;
                        in = "";
                        while ((sz = is.read(buf)) > 0) {
                            getLog().info(in += new String(buf));
                            if (in.contains("\n"))
                                break;
                        }
                        is.close();

                        /*while ((*///in = bf.readLine();/*) != null) {*/
                        if (in == null) {
                            throw new IOException("No input");
                        }
                        getLog().info("f");
                        String responseBack = inst.interpret(in);
                        getLog().info("g");
                        JSONObject response = new JSONObject();
                        getLog().info("h");
                        response.put("msg", responseBack);
                        response.put("status", 0);

                        getLog().info("i");
                        out.write(response.toString());
                        getLog().info("j");
                        out.write("\n");
                        getLog().info("k");
                        out.flush();
                        getLog().info("l");
                        //}
                        getLog().info("m");
                    } catch (IOException e) {
                        getLog().error("Get input", e);
                    } finally {
                        if (clientSocket.isConnected()) {
                            getLog().info("Client " + clientSocket.getInetAddress().getHostName() + " disconnected");
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                inst.getLog().error("Socket close", e);
                            }
                        }
                        getLog().info("n");
                        clients.remove(clientSocket);
                    }
                    getLog().info("o");
                });
                getLog().info("p");
                clients.put(clientSocket, t);
                getLog().info("q");
                t.start();
                getLog().info("r");
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
            for (Map.Entry<Socket, Thread> e : clients.entrySet()) {
                e.getValue().join(1000);
                if (e.getValue().isAlive()) {
                    e.getValue().interrupt();
                }
                if (e.getKey().isConnected()) {
                    e.getKey().close();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public String getInput() throws IOException {
        throw new IOException("NotImplemented");
    }
}
