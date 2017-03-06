package com.viveret.pilexa.pi;

import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.util.ConfigTransactionLayer;
import com.viveret.pilexa.pi.util.ToPhoneIntent;
import org.apache.log4j.Logger;

/**
 * Created by viveret on 2/13/17.
 */
public abstract class AbstractPiLexaServiceProxy implements PiLexaService {
    private PiLexaService inst;

    @Override
    public void connect() {
        inst = ConcretePiLexaService.inst();
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
    public String interpret(String str) {
        return inst.interpret(str);
    }

    @Override
    public ConfigTransactionLayer getConfig() {
        return inst.getConfig();
    }

    @Override
    public Logger getLog() {
        return inst.getLog();
    }

    @Override
    public Sayable DispatchIntent(ToPhoneIntent i) {
        return inst.DispatchIntent(i);
    }
}
