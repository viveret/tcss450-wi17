package com.viveret.pilexa.pi.inputmethods;

import com.viveret.pilexa.pi.AbstractPiLexaServiceProxy;
import com.viveret.pilexa.pi.InputSource;
import com.viveret.pilexa.pi.PiLexaService;

import java.util.Scanner;

/**
 * Created by viveret on 2/4/17.
 */
public class TextInterfacePiLexaProxy extends AbstractPiLexaServiceProxy implements InputSource {
    private PiLexaService inst;
    private Thread myThread;

    @Override
    public void run() {
        do {
            inst.interpret(getInput());
        } while (isConnected());
    }

    @Override
    public void start(PiLexaService piLexaService) {
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
    public String getInput() {
        Scanner scan = new Scanner(System.in);
        System.out.printf("> ");
        String sin = scan.nextLine();
        return sin.trim();
    }
}
