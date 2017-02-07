package com.viveret.pilexa.pi;

import com.viveret.pilexa.pi.sayable.Phrase;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Created by viveret on 2/4/17.
 */
public class TextInterfacePiLexaProxy implements PiLexaService {
    private PiLexaService inst;

    @Override
    public void connect() {
        inst = ConcretePiLexaService.inst();
        while (!inst.isConnected()) ;
    }

    @Override
    public void disconnect() {
        inst = null;
    }

    @Override
    public boolean isConnected() {
        return inst != null && inst.isConnected();
    }

    @Override
    public int interpret(String str) {
        return inst.interpret(str);
    }

    @Override
    public Logger getLog() {
        return inst.getLog();
    }

    @Override
    public void run() {
        connect();

        new Phrase("Hello! I'm PiLexa. Type to talk to me.").speak();

        String sin;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.printf("> ");
            sin = scan.nextLine();
            if (sin.trim().length() > 0) {
                interpret(sin);
            }
        } while (isConnected());

        disconnect();
    }
}
