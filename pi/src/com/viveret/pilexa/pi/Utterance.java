package com.viveret.pilexa.pi;

import java.util.List;

/**
 * Created by viveret on 1/25/17.
 */
public interface Utterance {
    String getReducedVerb();

    String getRealVerb();

    List<String> getVerbAdjectives();

    String getReducedSubject();
}
