package com.viveret.pilexa.pi;

/**
 * Created by viveret on 1/24/17.
 */
public interface PiLexaService {
    void connect();

    void disconnect();

    boolean isConnected();

    int interpretUtterance(String str);
}
