package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;

import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public interface Intent extends InvocationProcessor {
    Skill getAssociatedSkill();

    String getDisplayName();

    String getShortDescription();

    String getDescription();

    List<InvocationPattern> getInvocationPatterns();
}
