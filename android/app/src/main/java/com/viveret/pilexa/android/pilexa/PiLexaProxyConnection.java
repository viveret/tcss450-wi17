package com.viveret.pilexa.android.pilexa;

import java.io.Serializable;

/**
 * Created by viveret on 2/15/17.
 */

public class PiLexaProxyConnection {
    private String myHost;
    private short myPort;

    private PiLexaProxyConnection(String theHost, short thePort) {
        myHost = theHost;
        myPort = thePort;
    }

    public static PiLexaProxyConnection attachTo(String host, short port) {
        PiLexaProxyConnection ret = new PiLexaProxyConnection(host, port);
        if (ret.canConnect()) {
            return ret;
        } else {
            throw new
        }
    }

    void setConfig(String key, Serializable val) {

    }

    boolean canConnect() {
        return false;
    }
}
