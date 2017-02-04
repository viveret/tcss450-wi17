package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.PiLexaService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public class CalendarSkill extends AbstractSkill {
    static {
        ConcretePiLexaService.registerSkill(new CalendarSkill());
    }

    public CalendarSkill() {
        super("Calendar", "Tells you the time or date",
                "Tells you the time, date, or holidays", "PiLexa", "0.0.0.0",
                0, null);
    }

    @Override
    public List<SimpleTuple<InvocationPattern, Intent>> getPossibleIntents() {
        List<SimpleTuple<InvocationPattern, Intent>> ret = new ArrayList<>();

        ret.add(new SimpleTuple<>(InvocationPattern.parse("tell me the time"), new TimeTellIntent()));
        ret.add(new SimpleTuple<>(InvocationPattern.parse("tell me the date"), new DateTellIntent()));

        return ret;
    }

    private class TimeTellIntent implements Intent {
        @Override
        public Skill getAssociatedSkill() {
            return CalendarSkill.this;
        }

        @Override
        public String getDisplayName() {
            return "Tell the time";
        }

        @Override
        public String getShortDescription() {
            return "Tells the time";
        }

        @Override
        public String getDescription() {
            return getDescription();
        }

        @Override
        public Sayable processInvocation(Invocation i) {
            String theTime = Calendar.getInstance().getTime().toString();
            return new Phrase("It is " + theTime + ".");
        }
    }

    private class DateTellIntent implements Intent {
        @Override
        public Skill getAssociatedSkill() {
            return CalendarSkill.this;
        }

        @Override
        public String getDisplayName() {
            return "Tell the date";
        }

        @Override
        public String getShortDescription() {
            return "Tells the date";
        }

        @Override
        public String getDescription() {
            return getDescription();
        }

        @Override
        public Sayable processInvocation(Invocation i) {
            String theTime = new Date().toString();
            return new Phrase("It is " + theTime + ".");
        }
    }
}
