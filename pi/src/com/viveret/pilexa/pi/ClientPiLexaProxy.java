package com.viveret.pilexa.pi;

import org.apache.log4j.Logger;

/**
 * Created by viveret on 2/4/17.
 */
public class ClientPiLexaProxy implements PiLexaService {
    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public int interpret(String str) {
        return 0;
    }

    @Override
    public Logger getLog() {
        return null;
    }
}
