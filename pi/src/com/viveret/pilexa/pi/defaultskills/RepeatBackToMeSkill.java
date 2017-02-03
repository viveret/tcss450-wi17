package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.*;
import com.viveret.pilexa.pi.invocation.*;
import com.viveret.pilexa.pi.util.SimpleTuple;

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

        ret.add(new SimpleTuple<>(InvocationPattern.parse("repeat back to me %phrase:string%"), new MainIntent()));

        return ret;
    }

    private class MainIntent implements Intent {
        @Override
        public void processInvocation(Invocation i) {
            pilexa.getLog().debug("Going to repeat back \"" + i.getValue("phrase") + "\"");

        }
    }
}
