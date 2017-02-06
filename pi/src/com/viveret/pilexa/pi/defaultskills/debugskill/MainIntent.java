package com.viveret.pilexa.pi.defaultskills.debugskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Created by viveret on 2/5/17.
 */
public class MainIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
        for (CoreLabel w : i.getValue("str")) {
            InvocationPattern.printInfoAboutWord(w);
        }
        return new Phrase("Done.");
    }
}
