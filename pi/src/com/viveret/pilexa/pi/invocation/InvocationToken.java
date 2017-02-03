package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 2/3/17.
 */
public class InvocationToken {
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