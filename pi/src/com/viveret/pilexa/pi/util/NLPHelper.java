package com.viveret.pilexa.pi.util;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by viveret on 2/4/17.
 */
public class NLPHelper {
    public static String join(List<CoreLabel> words) {
        StringBuilder sb = new StringBuilder();
        for (CoreLabel w : words) {
            sb.append(w.word() + " ");
        }
        if (words.size() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
}
