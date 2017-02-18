package com.viveret.pilexa.pi.defaultskills.repeatbacktomeskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by viveret on 2/5/17.
 */
public class MainIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
        List<CoreLabel> toRepeat = i.getValue("phrase");
        StringBuilder sb = new StringBuilder();

        for (CoreLabel w : toRepeat) {
            sb.append(w.word());
            sb.append(" ");
        }
        sb.setLength(sb.length() - 1);
        return new Phrase(sb.toString());
    }
}
