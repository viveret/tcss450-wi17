package com.viveret.pilexa.pi.defaultskills.salutationskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;

/**
 * Created by viveret on 2/5/17.
 */
public class ArrivalIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
        return new Phrase("hey there!");
    }
}
