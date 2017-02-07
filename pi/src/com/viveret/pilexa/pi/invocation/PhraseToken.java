package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.viveret.pilexa.pi.util.Math;

/**
 * Created by viveret on 1/26/17.
 */
public class PhraseToken {
    private TokenType myType;
    private String myLabel;
    private String[] myContent;

    public PhraseToken(TokenType theType, String theLabel, String[] theContent) {
        myType = theType;
        myLabel = theLabel;
        myContent = theContent;
    }

    public String getLabel() {
        return myLabel;
    }

    public String[] getContent() {
        return myContent;
    }

    public TokenType getType() {
        return myType;
    }

    public MatchResult searchForMatch(CoreLabel w) {
        String text = w.get(CoreAnnotations.TextAnnotation.class);
        // this is the POS tag of the token
        String pos = w.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        // this is the NER label of the token
        String ne = w.get(CoreAnnotations.NamedEntityTagAnnotation.class);

        switch (getType()) {
            case IGNORE:
                return new MatchResult(0, true);
            case ARGUMENT:
                MatchResult[] closest = new MatchResult[myContent.length];
                int maxConfIndex = 0;

                for (int i = 0; i < myContent.length; i++) {
                    MatchResult tmpRes = null;
                    Map<String, Object> exts = new HashMap<>();
                    exts.put("which", i);

                    switch (myContent[i]) {
                        case "string":
                            tmpRes = new MatchResult(1, !pos.equals("."), exts);
                            break;
                        case "int":
                            tmpRes = new MatchResult(1, "NUMBER".equals(ne), exts);
                            break;
                        case "date":
                            tmpRes = new MatchResult(1, "DATE".equals(ne), exts);
                            break;
                    }

                    if (tmpRes != null) {
                        if (closest[maxConfIndex] != null && tmpRes != null &&
                                closest[maxConfIndex].getConfidence() < tmpRes.getConfidence())
                            maxConfIndex = i;
                        closest[i] = tmpRes;
                    }
                }

                if (maxConfIndex < 0) {
                    return new MatchResult(0, false);
                } else {
                    return closest[maxConfIndex];
                }
            case MATCH:
                double diff = Math.strDiff(myContent[0].toLowerCase(), text.toLowerCase());
                return new MatchResult(diff, diff >= 0.9);
        }

        return new MatchResult(0, false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (getLabel() != null) {
            sb.append("label = \"" + getLabel() + "\", ");
        }
        sb.append("type = " + myType.toString());
        sb.append(", ");
        sb.append("content = \"" + Arrays.toString(myContent) + "\"");
        sb.append("]");
        return sb.toString();
    }

    public enum TokenType {
        IGNORE,
        ARGUMENT,
        MATCH
    }
}