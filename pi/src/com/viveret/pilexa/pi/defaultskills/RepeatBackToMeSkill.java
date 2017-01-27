package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.AbstractSkill;
import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.Intent;
import com.viveret.pilexa.pi.PiLexaService;
import com.viveret.pilexa.pi.invocation.InvocationPattern;

import java.util.Arrays;
import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public class RepeatBackToMeSkill extends AbstractSkill {
    static {
        ConcretePiLexaService.registerSkill(new RepeatBackToMeSkill());
    }

    /*private static class MyUtteranceTranslator implements UtteranceToIntent {
        @Override
        public boolean understandsUtterance(Utterance u) {
            List<String> verbs = Arrays.asList("repeat", "say");
            boolean rightVerbs = verbs.contains(u.getReducedVerb());
            boolean rightSubject = u.getReducedSubject() == null || u.getReducedSubject().equals("me");
            return rightVerbs && rightSubject;
        }

        @Override
        public Intent fromUtterance(Utterance u) {
            return null;//u.getRealVerb();
        }
    }*/

    public RepeatBackToMeSkill() {
        super("Repeat Back To Me", "Repeat a phrase back to you",
                "Repeat a phrase back to you", "PiLexa", "0.0.0.0",
                0, null);
    }

    /**
     * Test method for Repeat back to me skill
     *
     * @param args main arguments
     */
    public static void main(String[] args) {
        PiLexaService pilexa = new ConcretePiLexaService();
        pilexa.connect();

        String str = "repeat back to me I am a good Alexa clone";
        pilexa.interpret(str);

        pilexa.disconnect();
    }

    @Override
    public int processIntent(Intent i) {
        return 0;
    }

    @Override
    public List<InvocationPattern> getInvocations() {
        InvocationPattern[] invos = {InvocationPattern.parse("repeat back to me %phrase:string%")};
        return Arrays.asList(invos);
    }
}
