package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by viveret on 2/3/17.
 */
public interface Invocation {
    List<CoreLabel> getValue(String key);

    String getType(String key);

    double getConfidence();

    InvocationPattern getPattern();
}
