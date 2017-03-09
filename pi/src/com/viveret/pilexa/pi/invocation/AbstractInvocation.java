package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viveret on 1/26/17.
 */
public class AbstractInvocation implements Invocation {
    private Map<String, InvocationToken> myTokens;
    private InvocationPattern myPattern;
    private double myConfidence;

    public AbstractInvocation(InvocationPattern thePattern, Map<String, InvocationToken> theTokens, double theConfidence) {
        myPattern = thePattern;
        myTokens = new HashMap<>(theTokens);
        if (theConfidence < 0)
            myConfidence = 0;
        else if (theConfidence > 1)
            myConfidence = 1;
        else
            myConfidence = theConfidence;
    }

    @Override
    public List<CoreLabel> getValue(String key) {
        if (myTokens.containsKey(key)) {
            return myTokens.get(key).getData();
        } else {
            return null;
        }
    }

    @Override
    public String getType(String key) {
        return myTokens.get(key).getType();
    }

    @Override
    public boolean hasValue(String key) {
        return myTokens.containsKey(key);
    }

    @Override
    public double getConfidence() {
        return myConfidence;
    }

    @Override
    public InvocationPattern getPattern() {
        return myPattern;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[conf = " + getConfidence());
        sb.append(", ");
        sb.append(myTokens.toString());
        sb.append("]");

        return sb.toString();
    }
}