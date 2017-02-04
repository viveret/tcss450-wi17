package com.viveret.pilexa.pi.invocation;

/**
 * Created by viveret on 2/3/17.
 */
public class MatchResult {
    private double myConfidence;
    private boolean myDidMatch;

    public MatchResult(double theConfidence, boolean theDidMatch) {
        myConfidence = (theDidMatch) ? theConfidence : 0;
        myDidMatch = theDidMatch;
    }

    public double getConfidence() {
        return myConfidence;
    }

    public boolean meetsCriteria() {
        return myDidMatch;
    }
}