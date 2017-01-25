package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.AbstractSkill;
import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.Intent;
import com.viveret.pilexa.pi.PiLexaService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public class RepeatBackToMeSkill extends AbstractSkill {
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

        pilexa.interpretUtterance("repeat back to me I am a good Alexa clone");

        pilexa.disconnect();
    }

    @Override
    public boolean understandsIntent(Intent i) {
        List<String> verbs = Arrays.asList("repeat", "say", "tell");
        boolean rightVerbs = verbs.contains(i.getIntentVerb());
        // boolean rightSubject =
        return false;
    }

    @Override
    public int processIntent(Intent i) {
        return 0;
    }
}
