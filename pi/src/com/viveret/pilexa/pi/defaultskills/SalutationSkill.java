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
public class SalutationSkill extends AbstractSkill {
    private static final String[] GREETINGS = {"hello", "hi", "hey", "sup", "whats up"};
    private static final String[] FAREWELLS = {"bye", "goodbye", "later", "talk to you later", "see you later"};

    static {
        ConcretePiLexaService.registerSkill(new SalutationSkill());
    }

    public SalutationSkill() {
        super("Salutation", "Greetings or acknowledgments of arrival or departure.",
                "Gives a gesture as a greeting or acknowledgment of your arrival or departure.",
                "PiLexa", "0.0.0.0", 0, null);
    }

    @Override
    public String getPackageName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public List<SimpleTuple<InvocationPattern, Intent>> getPossibleIntents() {
        List<SimpleTuple<InvocationPattern, Intent>> ret = new ArrayList<>();

        Intent arrivalIntent = new ArrivalIntent();
        for (String s : GREETINGS) {
            ret.add(new SimpleTuple<>(InvocationPattern.parse(s), arrivalIntent));
        }

        Intent departureIntent = new DepartureIntent();
        for (String s : FAREWELLS) {
            ret.add(new SimpleTuple<>(InvocationPattern.parse(s), departureIntent));
        }

        return ret;
    }

    private class ArrivalIntent implements Intent {
        @Override
        public Skill getAssociatedSkill() {
            return SalutationSkill.this;
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
            return new Phrase("hey there!");
        }
    }

    private class DepartureIntent implements Intent {
        @Override
        public Skill getAssociatedSkill() {
            return SalutationSkill.this;
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
            return new Phrase("bye bye!");
        }
    }
}
