package com.viveret.pilexa.pi.defaultskills.salutationskill;

import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;

/**
 * Created by viveret on 2/5/17.
 */
public class DepartureIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
        ConcretePiLexaService.inst().disconnect();
        return new Phrase("bye bye!");
    }
}
