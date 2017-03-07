package com.viveret.pilexa.pi.coinskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.invocation.InvocationToken;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.JsonQueryIntent;
import org.json.simple.JSONObject;

/**
 * Created by Daniel on 3/07/17.
 */
public class MainIntent implements InvocationProcessor {

    @Override
    public Sayable processInvocation(Invocation i) {

        Phrase newPh = new Phrase("Flipping a coin.");
        newPh.speak();

        double rand = Math.random();
        StringBuilder resultOfCoinFlip = new StringBuilder().append("It is ");
        if(rand > 0.5) {
            resultOfCoinFlip.append("heads.");
        } else {
            resultOfCoinFlip.append("tails.");
        }

        return new Phrase(resultOfCoinFlip.toString());
    }
}
