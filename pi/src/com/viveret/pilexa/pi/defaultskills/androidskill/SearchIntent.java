package com.viveret.pilexa.pi.defaultskills.androidskill;

import com.viveret.pilexa.pi.event.EventPoll;
import com.viveret.pilexa.pi.event.SearchEvent;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.util.NLPHelper;

/**
 * Created by viveret on 2/5/17.
 */
public class SearchIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
        String query = NLPHelper.join(i.getValue("query"));
        EventPoll.inst().addToQueue(new SearchEvent(query));
        return new Phrase("Searching for \"" + query + "\".");
    }
}
