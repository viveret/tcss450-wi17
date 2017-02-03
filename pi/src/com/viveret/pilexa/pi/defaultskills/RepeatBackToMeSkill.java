package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.*;
import com.viveret.pilexa.pi.invocation.*;
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
public class RepeatBackToMeSkill extends AbstractSkill {
    static {
        ConcretePiLexaService.registerSkill(new RepeatBackToMeSkill());
    }

    public RepeatBackToMeSkill() {
        super("Repeat Back To Me", "Repeat a phrase back to you",
                "Repeat a phrase back to you", "PiLexa", "0.0.0.0",
                0, null);
    }

    private static final PiLexaService pilexa = new ConcretePiLexaService();

    /**
     * Test method for Repeat back to me skill
     *
     * @param args main arguments
     */
    public static void main(String[] args) {
        pilexa.connect();

        String str = "repeat back to me I am a good Alexa clone";
        pilexa.interpret(str);

        pilexa.disconnect();
    }

    @Override
    public List<SimpleTuple<InvocationPattern, Intent>> getPossibleIntents() {
        List<SimpleTuple<InvocationPattern, Intent>> ret = new ArrayList<>();

        Intent i = new MainIntent();

        ret.add(new SimpleTuple<>(InvocationPattern.parse("repeat back to me %phrase:string%"), i));
        ret.add(new SimpleTuple<>(InvocationPattern.parse("repeat %phrase:string%"), i));
        ret.add(new SimpleTuple<>(InvocationPattern.parse("tell me %phrase:string%"), i));
        ret.add(new SimpleTuple<>(InvocationPattern.parse("say %phrase:string%"), i));

        return ret;
    }

    private class MainIntent implements Intent {
        @Override
        public Skill getAssociatedSkill() {
            return RepeatBackToMeSkill.this;
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
            List<CoreLabel> toRepeat = i.getValue("phrase");
            StringBuilder sb = new StringBuilder();

            for (CoreLabel w : toRepeat) {
                sb.append(w.word());
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);
            return new Phrase(sb.toString());
        }
    }
}
