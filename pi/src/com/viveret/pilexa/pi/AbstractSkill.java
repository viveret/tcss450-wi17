package com.viveret.pilexa.pi;

import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public abstract class AbstractSkill implements Skill {
    private String myDisplayName, myShortDesc, myDesc, myPublisher, myVersionStr, myIconUrl;
    private int myVersionNumber;
    private UtteranceToIntent myUtteranceTranslator;

    public AbstractSkill(String theDisplayName, String theShortDesc, String theDesc, String thePublisher,
                         String theVersionStr, int theVersionNum, String theIconUrl,
                         UtteranceToIntent theUtteranceTranslator) {
        myDisplayName = theDisplayName;
        myShortDesc = theShortDesc;
        myDesc = theDesc;
        myPublisher = thePublisher;
        myVersionStr = theVersionStr;
        myVersionNumber = theVersionNum;
        myIconUrl = theIconUrl;
        myUtteranceTranslator = theUtteranceTranslator;
    }

    @Override
    public String getDisplayName() {
        return myDisplayName;
    }

    @Override
    public String getShortDescription() {
        return myShortDesc;
    }

    @Override
    public String getDescription() {
        return myDesc;
    }

    @Override
    public String getPublisher() {
        return myPublisher;
    }

    @Override
    public String getVersionString() {
        return myVersionStr;
    }

    @Override
    public int getVersionNumber() {
        return myVersionNumber;
    }

    @Override
    public String getIconUrl() {
        return myIconUrl;
    }

    @Override
    public UtteranceToIntent getUtteranceTranslator() {
        return myUtteranceTranslator;
    }
}
