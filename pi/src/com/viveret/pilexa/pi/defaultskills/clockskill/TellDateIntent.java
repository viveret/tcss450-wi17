package com.viveret.pilexa.pi.defaultskills.clockskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;

import java.util.Date;

/**
 * Created by viveret on 2/5/17.
 */
public class TellDateIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
        String theTime = new Date().toString();
        return new Phrase("It is " + theTime + ".");
    }
}
