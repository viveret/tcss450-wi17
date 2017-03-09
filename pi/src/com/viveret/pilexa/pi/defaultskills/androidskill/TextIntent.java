package com.viveret.pilexa.pi.defaultskills.androidskill;

import com.viveret.pilexa.pi.event.EventPoll;
import com.viveret.pilexa.pi.event.TextEvent;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.util.NLPHelper;

/**
 * Created by viveret on 2/5/17.
 */
public class TextIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
        String number = NLPHelper.join(i.getValue("phone"));
        String msg = NLPHelper.join(i.getValue("msg"));

        EventPoll.inst().addToQueue(new TextEvent(number, msg));
        return new Phrase("Texting " + number + ": \"" + msg + "\".");
    }
}
