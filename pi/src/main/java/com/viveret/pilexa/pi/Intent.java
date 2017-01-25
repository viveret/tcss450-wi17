package com.viveret.pilexa.pi;

import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public interface Intent {
    String getIntentVerb();

    List<String> getIntentVerbAdjectives();

    String getIntentSubject();
}
