package com.viveret.pilexa.pi;

import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.util.ConfigTransactionLayer;
import com.viveret.pilexa.pi.util.ToPhoneIntent;
import org.apache.log4j.Logger;

/**
 * Created by viveret on 1/24/17.
 */
public interface PiLexaService extends Runnable {
    void connect();

    void disconnect();

    boolean isConnected();

    String interpret(String str);

    ConfigTransactionLayer getConfig();

    Logger getLog();

    Sayable DispatchIntent(ToPhoneIntent i);
}
