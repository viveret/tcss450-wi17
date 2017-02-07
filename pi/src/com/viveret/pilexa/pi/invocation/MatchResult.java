package com.viveret.pilexa.pi.invocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viveret on 2/3/17.
 */
public class MatchResult {
    private double myConfidence;
    private boolean myDidMatch;
    private Map<String, Object> myExts;

    public MatchResult(double theConfidence, boolean theDidMatch, Map<String, Object> theExts) {
        myConfidence = (theDidMatch) ? theConfidence : 0;
        myDidMatch = theDidMatch;
        if (theExts != null) {
            myExts = new HashMap<>(theExts);
        } else {
            myExts = null;
        }
    }

    public MatchResult(double theConfidence, boolean theDidMatch) {
        this(theConfidence, theDidMatch, null);
    }

    public double getConfidence() {
        return myConfidence;
    }

    public boolean meetsCriteria() {
        return myDidMatch;
    }

    public Object getExt(String key) {
        if (myExts == null)
            throw new IllegalStateException("Must have extended components to access " + key);
        return myExts.get(key);
    }
}