package com.viveret.pilexa.pi.invocation;

/**
 * Created by viveret on 2/3/17.
 */
public class MatchResult {
    private double myConfidence;
    private boolean myDidMatch;

    public MatchResult(double theConfidence, boolean theDidMatch) {
        myConfidence = theConfidence;
        myDidMatch = theDidMatch;
    }

    public double getConfidence() {
        return myConfidence;
    }

    public boolean didMatch() {
        return myDidMatch;
    }
}