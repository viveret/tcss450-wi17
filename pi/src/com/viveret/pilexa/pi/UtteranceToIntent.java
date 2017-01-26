package com.viveret.pilexa.pi;

/**
 * Created by viveret on 1/25/17.
 */
public interface UtteranceToIntent {
    boolean understandsUtterance(Utterance u);
    Intent fromUtterance(Utterance u);
}
