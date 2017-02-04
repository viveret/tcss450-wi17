package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Created by viveret on 1/26/17.
 */
public class PhraseToken {
    private TokenType myType;
    private String myLabel;
    private String myContent;

    public PhraseToken(TokenType theType, String theLabel, String theContent) {
        myType = theType;
        myLabel = theLabel;
        myContent = theContent;
    }

    public String getLabel() {
        return myLabel;
    }

    public String getContent() {
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
                switch (getContent()) {
                    case "string":
                        return new MatchResult(.3, true);
                    case "int":
                        return new MatchResult(.75, "NUMBER".equals(ne));
                }
                break;
            case MATCH:
                return new MatchResult(1, getContent().toLowerCase().equals(text.toLowerCase()));
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
        sb.append("content = \"" + myContent + "\"");
        sb.append("]");
        return sb.toString();
    }

    public enum TokenType {
        IGNORE,
        ARGUMENT,
        MATCH
    }
}