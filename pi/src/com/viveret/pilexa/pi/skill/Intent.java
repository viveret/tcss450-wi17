package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.sayable.Sayable;

/**
 * Created by viveret on 1/24/17.
 */
public interface Intent {
    Skill getAssociatedSkill();

    String getDisplayName();

    String getShortDescription();

    String getDescription();

    Sayable processInvocation(Invocation i);
}
