package com.viveret.pilexa.pi;

import com.viveret.pilexa.pi.invocation.InvocationPattern;

import java.util.List;

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

    int processIntent(Intent i);

    List<InvocationPattern> getInvocations();
}
