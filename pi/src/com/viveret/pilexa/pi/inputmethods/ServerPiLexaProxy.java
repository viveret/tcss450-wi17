package com.viveret.pilexa.pi.inputmethods;

import com.viveret.pilexa.pi.AbstractPiLexaServiceProxy;
import com.viveret.pilexa.pi.InputSource;
import com.viveret.pilexa.pi.PiLexaService;
import com.viveret.pilexa.pi.util.Config;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by viveret on 2/4/17.
 */
public class ServerPiLexaProxy extends AbstractPiLexaServiceProxy implements InputSource {
    private PiLexaService inst;
    private Thread myThread;
    private ServerSocket mySocket;
    private Socket clientSocket;

    @Override
    public void run() {
        do {
            try {
                clientSocket = mySocket.accept();
                String responseBack = inst.interpret(getInput());
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                JSONObject response = new JSONObject();
                response.put("msg", responseBack);
                response.put("status", 0);
                out.write(response.toString());
                out.flush();
                out.close();
            } catch (IOException e) {
                inst.getLog().error("Get input", e);
                return;
            }

            // Close socket
            if (clientSocket.isConnected()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    inst.getLog().error("Socket close", e);
                }
                clientSocket = null;
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
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public String getInput() throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        return bf.readLine();
    }
}
