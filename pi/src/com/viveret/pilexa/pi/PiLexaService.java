package com.viveret.pilexa.pi;

import org.apache.log4j.Logger;

/**
 * Created by viveret on 1/24/17.
 */
public interface PiLexaService extends Runnable {
    void connect();

    void disconnect();

    boolean isConnected();

    String interpret(String str);

    Logger getLog();
}
