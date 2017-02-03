package com.viveret.pilexa.pi.sayable;

/**
 * Created by viveret on 2/3/17.
 */
public class Phrase extends Sayable {
    private String myTextToSpeech;
    private double myPauseBefore, myPauseAfter;

    public Phrase(String theTextToSpeech) {
        myTextToSpeech = theTextToSpeech;
        myPauseBefore = 0;
        myPauseAfter = 0;

        if (theTextToSpeech.endsWith(".")) {
            myPauseAfter = .5;
        } else if (theTextToSpeech.endsWith(":")) {
            myPauseAfter = 2;
        } else if (theTextToSpeech.endsWith("?")) {
            myPauseAfter = 3;
        }
    }

    protected final String getTextToSpeech() {
        return myTextToSpeech;
    }

    @Override
    public void speak() {
        log.info(getTextToSpeech());
    }
}
