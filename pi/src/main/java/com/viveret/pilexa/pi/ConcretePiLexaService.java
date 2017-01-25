package com.viveret.pilexa.pi;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Created by viveret on 1/24/17.
 */
public class ConcretePiLexaService implements PiLexaService {
    private Logger log;

    @Override
    public void connect() {
        BasicConfigurator.configure();
        log = Logger.getRootLogger();// Logger.getLogger(getClass().getName());
        log.info("Connected to PiLexa Service.");
    }

    @Override
    public void disconnect() {
        log.info("Disconnecting from PiLexa Service.");
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public int interpretUtterance(String str) {
        log.info("Interpreting utterance \"" + str + "\"");
        return 0;
    }
}
