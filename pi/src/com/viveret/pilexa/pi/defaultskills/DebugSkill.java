package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.AbstractSkill;
import com.viveret.pilexa.pi.skill.Intent;
import com.viveret.pilexa.pi.skill.Skill;
import com.viveret.pilexa.pi.util.SimpleTuple;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public class DebugSkill extends AbstractSkill {
    static {
        ConcretePiLexaService.registerSkill(new DebugSkill());
    }

    public DebugSkill() {
        super("Debug", "Helps debugging",
                "Helps debugging", "PiLexa", "0.0.0.0",
                0, null);
    }

    @Override
    public String getPackageName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public List<SimpleTuple<InvocationPattern, Intent>> getPossibleIntents() {
        List<SimpleTuple<InvocationPattern, Intent>> ret = new ArrayList<>();

        Intent i = new MainIntent();

        ret.add(new SimpleTuple<>(InvocationPattern.parse("debug print nlp tags %str:string%"), i));

        return ret;
    }

    private class MainIntent implements Intent {
        @Override
        public Skill getAssociatedSkill() {
            return DebugSkill.this;
        }

        @Override
        public String getDisplayName() {
            return getAssociatedSkill().getDisplayName();
        }

        @Override
        public String getShortDescription() {
            return getAssociatedSkill().getShortDescription();
        }

        @Override
        public String getDescription() {
            return getAssociatedSkill().getDescription();
        }

        @Override
        public Sayable processInvocation(Invocation i) {
            for (CoreLabel w : i.getValue("str")) {
                InvocationPattern.printInfoAboutWord(w);
            }
            return new Phrase("Done.");
        }
    }
}
