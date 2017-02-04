package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import org.apache.log4j.Logger;

import java.util.*;

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
        int matchStartIndex = -1;

        for (int i = 0; i < tokenStrs.length; i++) {
            String e = tokenStrs[i];
            PhraseToken t = null;
            boolean matchEnd = false;

            if (e.equals(escapeChar + escapeChar)) {
                matchEnd = true;
                t = new PhraseToken(PhraseToken.TokenType.IGNORE, null, new String[]{e});

            } else if (e.startsWith(escapeChar) && e.endsWith(escapeChar) && e.length() > 2) {
                matchEnd = true;

                String[] labelTypePair = e.substring(1, e.length() - 1).split(":");
                if (labelTypePair.length != 2) {
                    throw new IllegalArgumentException("Placeholder \"" + e + "\" must contain a label:type tag.");
                }
                String[] types = labelTypePair[1].split("\\|");
                t = new PhraseToken(PhraseToken.TokenType.ARGUMENT, labelTypePair[0], types);

            } else if (matchStartIndex < 0) {
                matchStartIndex = i;
            }

            if (matchStartIndex >= 0) { //  && matchEnd
                tmp.add(new PhraseToken(PhraseToken.TokenType.MATCH, null,
                        Arrays.copyOfRange(tokenStrs, matchStartIndex, i + 1)));
                matchStartIndex = -1;
            }

            if (t != null) {
                tmp.add(t);
            }
        }

        if (matchStartIndex >= 0) {
            tmp.add(new PhraseToken(PhraseToken.TokenType.MATCH, null,
                    Arrays.copyOfRange(tokenStrs, matchStartIndex, tokenStrs.length)));
        }

        log.debug("Parsed phrase pattern: " + tmp.toString());

        return new InvocationPattern(s, tmp);
    }

    public Invocation parse(CoreMap sentence) {
        Logger log = Logger.getRootLogger();

        Map<String, InvocationToken> args = new HashMap<>();
        double confidence = 0;
        int numDidMatch = 0, totalMatches = 0;

        // traversing the words in the current sentence
        // a CoreLabel is a CoreMap with additional token-specific methods
        List<CoreLabel> words = sentence.get(CoreAnnotations.TokensAnnotation.class);
        List<CoreLabel> wordsIncluded = null;

        int i = 0, j = 0, matchStreak = -1;
        for (; i < myTokens.size() && j < words.size(); ) {
            PhraseToken patt = myTokens.get(i);
            CoreLabel word = words.get(j);

            // printInfoAboutWord(word);

            switch (patt.getType()) {
                case IGNORE:
                    matchStreak = -1;
                    if (i + 1 < myTokens.size()) {
                        i++;
                        PhraseToken delimPatt = myTokens.get(i);
                        while (j < words.size()) {
                            word = words.get(j);
                            MatchResult r = delimPatt.searchForMatch(word);
                            if (r.meetsCriteria()) {
                                break;
                            } else {
                                confidence += r.getConfidence();
                                j++;
                            }
                        }
                    }
                    break;
                case MATCH:
                    MatchResult r = patt.searchForMatch(word);
                    if (r.meetsCriteria()) {
                        numDidMatch++;
                        j++;
                        if (matchStreak < 0)
                            matchStreak = 0;

                        matchStreak ++;
                        double v = 1; //percDiff(matchStreak, words.size() / 3);
                        //(2 - percDiff(matchStreak, words.size() / 3)) / 2.0;
                        // v = .25 + .75 * v;
                        confidence += r.getConfidence() * v;
                        // log.debug("v = " + v);
                    } else {
                        i++;
                        totalMatches++;
                    }
                    break;
                case ARGUMENT:
                    matchStreak = -1;
                    boolean isAtEnd = (j + 1 == words.size());
                    boolean nextIsTrue = false;
                    if (!isAtEnd && i + 1 < myTokens.size()) {
                        MatchResult r2 = myTokens.get(i + 1).searchForMatch(words.get(j));
                        confidence += r2.getConfidence();
                        nextIsTrue = r2.meetsCriteria();
                    }

                    MatchResult matches = patt.searchForMatch(word);
                    if (!nextIsTrue || isAtEnd) {
                        if (matches.meetsCriteria()) {
                            if (wordsIncluded == null) {
                                wordsIncluded = new ArrayList<>();
                            }

                            wordsIncluded.add(word);
                            confidence += matches.getConfidence();
                        }
                        j++;
                    }

                    if ((!matches.meetsCriteria() || isAtEnd || nextIsTrue)) {
                        if (wordsIncluded != null) {
                            args.put(patt.getLabel(),
                                    new InvocationToken(patt.getContent()[(Integer) matches.getExt("which")],
                                            wordsIncluded));
                            wordsIncluded = null;
                        }
                        i++;
                    }
                    break;
            }
        }

        confidence /= words.size();
        confidence = confidence *.90 + (1 - percDiff(words.size(), myTokens.size())) * .10;
        confidence *= (double)(numDidMatch) / totalMatches;
        return new AbstractInvocation(this, args, confidence);
    }

    private double percDiff(int a, int b) {
        return  Math.abs(a - b) / (.5 * (a + b));
    }

    @Override
    public String toString() {
        return myTokens.toString();
    }


    public static void printInfoAboutWord(CoreLabel w) {
        String text = w.get(CoreAnnotations.TextAnnotation.class);
        // this is the POS tag of the token
        String pos = w.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        // this is the NER label of the token
        String ne = w.get(CoreAnnotations.NamedEntityTagAnnotation.class);

        String isMonth = w.get(CoreAnnotations.MonthAnnotation.class);
        String scat1 = w.get(CoreAnnotations.CategoryAnnotation.class);
        String scat2 = w.get(CoreAnnotations.CommonWordsAnnotation.class);
        String scat3 = w.get(CoreAnnotations.DayAnnotation.class);
        String scat4 = w.get(CoreAnnotations.FeaturesAnnotation.class);
        String scat5 = w.get(CoreAnnotations.GoldAnswerAnnotation.class);
        String scat6 = w.get(CoreAnnotations.IsURLAnnotation.class);
        String scat7 = w.get(CoreAnnotations.LabelAnnotation.class);
        String scat8 = w.get(CoreAnnotations.MonthAnnotation.class);
        String scat9 = w.get(CoreAnnotations.WordSenseAnnotation.class);
        //String scat9 = w.get(CoreAnnotations.PercentAnnotation.class);
        //String scat9 = w.get(CoreAnnotations.PhraseWordsAnnotation.class);

        Logger log = Logger.getRootLogger();
        StringBuilder sb = new StringBuilder();

        sb.append("Word \"" + text + "\" ");
        sb.append("pos = " + pos + ", ");
        sb.append("ne = " + ne + ", ");
        sb.append("month = " + isMonth + ", ");
        sb.append("scat1 = " + scat1 + ", ");
        sb.append("scat2 = " + scat2 + ", ");
        sb.append("scat3 = " + scat3 + ", ");
        sb.append("scat4 = " + scat4 + ", ");
        sb.append("scat5 = " + scat5 + ", ");
        sb.append("scat6 = " + scat6 + ", ");
        sb.append("scat7 = " + scat7 + ", ");
        sb.append("scat8 = " + scat8 + ", ");
        sb.append("scat9 = " + scat9 + "");

        log.debug(sb.toString());
    }
}
