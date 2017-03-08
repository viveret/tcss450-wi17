package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.event.AndroidIntentEvent;
import com.viveret.pilexa.pi.event.EventPoll;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;

/**
 * Created by viveret on 2/4/17.
 */
public abstract class AndroidIntentIntent implements InvocationProcessor {
    public abstract String getIntentString();

    @Override
    public Sayable processInvocation(Invocation i) {
        EventPoll.inst().addToQueue(new AndroidIntentEvent(getIntentString()));
        return new Phrase("done");
    }
}
