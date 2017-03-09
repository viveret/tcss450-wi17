package com.viveret.pilexa.pi.defaultskills.jokeskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.JsonQueryIntent;
import org.json.JSONObject;

/**
 * Created by viveret on 2/5/17.
 */
public class MainIntent extends JsonQueryIntent {
    @Override
    public Sayable processInvocation(Invocation i) {
        Object tmp = getJson();

        if (tmp instanceof Sayable) {
            return (Sayable) tmp;
        } else {
            return new Phrase((String) ((JSONObject) tmp).get("joke"));
        }
    }

    @Override
    public String getUrl() {
        return "http://tambal.azurewebsites.net/joke/random";
    }
}
