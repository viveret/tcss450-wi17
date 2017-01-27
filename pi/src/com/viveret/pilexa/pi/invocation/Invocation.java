package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viveret on 1/26/17.
 */
public class Invocation {
    private Map<String, InvocationToken> myTokens;
    private double myConfidence;

    public Invocation(Map<String, InvocationToken> theTokens, double theConfidence) {
        myTokens = new HashMap<>(theTokens);
        myConfidence = theConfidence;
    }

    public List<CoreLabel> getValue(String key) {
        return myTokens.get(key).getData();
    }

    public String getType(String key) {
        return myTokens.get(key).getType();
    }

    public double getConfidence() {
        return myConfidence;
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


    public static class InvocationToken {
        private String myType;
        private List<CoreLabel> myData;

        public InvocationToken(String theType, List<CoreLabel> theData) {
            myType = theType;
            myData = new ArrayList<>(theData);
        }

        public String getType() {
            return myType;
        }

        public List<CoreLabel> getData() {
            return new ArrayList<>(myData);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("type: " + myType + ", ");
            sb.append("data: " + myData.toString());
            sb.append("]");

            return sb.toString();
        }
    }
}