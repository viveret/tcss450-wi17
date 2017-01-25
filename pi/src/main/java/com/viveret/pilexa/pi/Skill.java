package com.viveret.pilexa.pi;

/**
 * Created by viveret on 1/24/17.
 */
public interface Skill {
    String getDisplayName();

    String getShortDescription();

    String getDescription();

    String getPublisher();

    String getVersionString();

    int getVersionNumber();

    String getIconUrl();

    boolean understandsIntent(Intent i);

    int processIntent(Intent i);
}
