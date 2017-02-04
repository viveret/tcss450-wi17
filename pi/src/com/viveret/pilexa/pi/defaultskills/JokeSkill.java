package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.AbstractSkill;
import com.viveret.pilexa.pi.skill.Intent;
import com.viveret.pilexa.pi.skill.JsonQueryIntent;
import com.viveret.pilexa.pi.skill.Skill;
import com.viveret.pilexa.pi.util.SimpleTuple;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public class JokeSkill extends AbstractSkill {
    static {
        ConcretePiLexaService.registerSkill(new JokeSkill());
    }

    @Override
    public String getPackageName() {
        return this.getClass().getCanonicalName();
    }

    public JokeSkill() {
        super("Joke", "Tells you a joke.",
                "Tells you a joke.", "PiLexa", "0.0.0.0",
                0, null);
    }

    @Override
    public List<SimpleTuple<InvocationPattern, Intent>> getPossibleIntents() {
        List<SimpleTuple<InvocationPattern, Intent>> ret = new ArrayList<>();

        ret.add(new SimpleTuple<>(InvocationPattern.parse("tell me a joke"), new MainIntent()));

        return ret;
    }

    private class MainIntent extends JsonQueryIntent {
        @Override
        public Skill getAssociatedSkill() {
            return JokeSkill.this;
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
}
