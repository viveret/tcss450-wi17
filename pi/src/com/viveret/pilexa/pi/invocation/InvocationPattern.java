package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viveret on 1/26/17.
 */
public class InvocationPattern {
    private static final String escapeChar = "%";

    private List<PhraseToken> myTokens;
    private String myPhrase;

    private InvocationPattern(String str, List<PhraseToken> theTokens) {
        myPhrase = str;
        myTokens = theTokens;
    }

    public static InvocationPattern parse(String s) {
        Logger log = Logger.getRootLogger();
        List<PhraseToken> tmp = new ArrayList<>();

        String[] tokenStrs = s.split("(\\s)");

        for (String e : tokenStrs) {
            PhraseToken t = null;
            if (e.equals(escapeChar + escapeChar)) {
                t = new PhraseToken(PhraseToken.TokenType.IGNORE, null, e);
            } else if (e.startsWith(escapeChar) && e.endsWith(escapeChar) && e.length() > 2) {
                String[] labelTypePair = e.substring(1, e.length() - 1).split(":");
                if (labelTypePair.length != 2) {
                    throw new IllegalArgumentException("Placeholder \"" + e + "\" must contain a label:type tag.");
                }
                t = new PhraseToken(PhraseToken.TokenType.ARGUMENT, labelTypePair[0], labelTypePair[1]);
            } else {
                t = new PhraseToken(PhraseToken.TokenType.MATCH, null, e);
            }
            tmp.add(t);
        }

        log.debug("Parsed phrase pattern: " + tmp.toString());

        return new InvocationPattern(s, tmp);
    }

    public Invocation parse(CoreMap sentence) {
        Logger log = Logger.getRootLogger();

        Map<String, InvocationToken> args = new HashMap<>();
        double confidence = 0;

        // traversing the words in the current sentence
        // a CoreLabel is a CoreMap with additional token-specific methods
        List<CoreLabel> words = sentence.get(CoreAnnotations.TokensAnnotation.class);
        List<CoreLabel> wordsIncluded = null;

        int i = 0, j = 0;
        for (; i < myTokens.size() && j < words.size(); ) {
            PhraseToken patt = myTokens.get(i);
            CoreLabel word = words.get(j);

            switch (patt.getType()) {
                case IGNORE:
                    if (i + 1 < myTokens.size()) {
                        i++;
                        PhraseToken delimPatt = myTokens.get(i);
                        while (j < words.size()) {
                            word = words.get(j);
                            MatchResult r = delimPatt.matches(word);
                            if (r.didMatch()) {
                                break;
                            } else {
                                confidence += r.getConfidence();
                                j++;
                            }
                        }
                    }
                    break;
                case MATCH:
                    MatchResult r = patt.matches(word);
                    if (r.didMatch()) {
                        j++;
                        confidence += r.getConfidence();
                    } else {
                        i++;
                    }
                    break;
                case ARGUMENT:
                    boolean isAtEnd = (j + 1 == words.size());
                    boolean nextIsTrue = false;
                    if (!isAtEnd && i + 1 < myTokens.size()) {
                        MatchResult r2 = myTokens.get(i + 1).matches(words.get(j));
                        confidence += r2.getConfidence();
                        nextIsTrue = r2.didMatch();
                    }

                    MatchResult matches = patt.matches(word);
                    if ((matches.didMatch() && !nextIsTrue) || isAtEnd) {
                        if (wordsIncluded == null) {
                            wordsIncluded = new ArrayList<>();
                        }

                        wordsIncluded.add(word);
                        confidence += matches.getConfidence();
                        j++;
                    }

                    if (!matches.didMatch() || isAtEnd || nextIsTrue) {
                        args.put(patt.getLabel(),
                                new InvocationToken(patt.getContent(), wordsIncluded));
                        wordsIncluded = null;
                        i++;
                    }
                    break;
            }
        }

        confidence /= words.size();
        return new AbstractInvocation(this, args, confidence);
    }
}
