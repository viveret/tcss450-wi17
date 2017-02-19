package com.viveret.pilexa.pi.inputmethods;

import com.viveret.pilexa.pi.AbstractPiLexaServiceProxy;
import com.viveret.pilexa.pi.InputSource;
import com.viveret.pilexa.pi.PiLexaService;
import com.viveret.pilexa.pi.util.Config;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
                final Socket clientSocket = mySocket.accept();
                final Thread t = new Thread(() -> {
                    getLog().info("Client " + clientSocket.getInetAddress().getCanonicalHostName() + " connected");
                    try {
                        BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String in = bf.readLine();
                        String responseBack = inst.interpret(in);

                        JSONObject response = new JSONObject();
                        response.put("msg", responseBack);
                        response.put("status", 0);

                        OutputStream os = clientSocket.getOutputStream();
                        os.write(response.toString().getBytes());
                        os.write("\n".getBytes());
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
                        clients.remove(clientSocket);
                    }
                });
                clients.put(clientSocket, t);
                t.start();
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
        stop();
    }

    @Override
    public String getInput() throws IOException {
        throw new IOException("NotImplemented");
    }
}
