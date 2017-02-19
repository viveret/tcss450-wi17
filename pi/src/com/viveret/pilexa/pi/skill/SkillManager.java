package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.invocation.PooledInvocationPattern;
import com.viveret.pilexa.pi.util.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 2/5/17.
 */
public class SkillManager {
    private static final List<Skill> mySkills = new ArrayList<>();
    private static SkillManager myInst = null;


    private SkillManager() {
        loadSkills();
    }

    public static SkillManager inst() {
        if (myInst == null) {
            myInst = new SkillManager();
        }
        return myInst;
    }

    public static final void registerSkill(Skill s) {
        mySkills.add(s);
    }

    private void loadSkills() {
        List<String> skills = Config.inst().getStringArray("skills");

        for (String s : skills) {
            registerSkill(new ManifestSkill(s));
        }

        PooledInvocationPattern.runPool();
    }

    public List<Skill> getSkills() {
        return new ArrayList<>(mySkills);
    }
}
