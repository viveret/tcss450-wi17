package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.AbstractSkill;
import com.viveret.pilexa.pi.Intent;

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

    @Override
    public boolean understandsIntent(Intent i) {
        List<String> verbs = Arrays.asList("set", "start");
        return verbs.contains(i.getIntentVerb());
    }

    @Override
    public int processIntent(Intent i) {
        return 0;
    }
}
