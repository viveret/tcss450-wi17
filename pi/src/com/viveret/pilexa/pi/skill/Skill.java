package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.util.SimpleTuple;

import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public interface Skill {
    String getDisplayName();

    String getShortDescription();

    String getDescription();

    String getPublisher();

    String getPackageName();

    String getVersionString();

    int getVersionNumber();

    String getIconUrl();

    List<Intent> getIntents();
}
