package com.viveret.pilexa.pi;

/**
 * Created by viveret on 1/24/17.
 */
public abstract class AbstractSkill implements Skill {
    private String myDisplayName, myShortDesc, myDesc, myPublisher, myVersionStr, myIconUrl;
    private int myVersionNumber;

    public AbstractSkill(String theDisplayName, String theShortDesc, String theDesc, String thePublisher,
                         String theVersionStr, int theVersionNum, String theIconUrl) {
        myDisplayName = theDisplayName;
        myShortDesc = theShortDesc;
        myDesc = theDesc;
        myPublisher = thePublisher;
        myVersionStr = theVersionStr;
        myVersionNumber = theVersionNum;
        myIconUrl = theIconUrl;
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
}
