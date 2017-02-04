package com.viveret.pilexa.pi.defaultskills;

import com.viveret.pilexa.pi.util.Math;
import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.*;
import com.viveret.pilexa.pi.util.NLPHelper;
import com.viveret.pilexa.pi.util.SimpleTuple;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by viveret on 1/24/17.
 */
public class ClockSkill extends AbstractSkill {
    static {
        ConcretePiLexaService.registerSkill(new ClockSkill());
    }

    public ClockSkill() {
        super("Clock", "Tells you the time or date",
                "Tells you the time, date, or holidays", "PiLexa", "0.0.0.0",
                0, null);
    }

    @Override
    public String getPackageName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public List<SimpleTuple<InvocationPattern, Intent>> getPossibleIntents() {
        List<SimpleTuple<InvocationPattern, Intent>> ret = new ArrayList<>();

        ret.add(new SimpleTuple<>(InvocationPattern.parse("tell me the time"), new TimeTellIntent()));
        ret.add(new SimpleTuple<>(InvocationPattern.parse("tell me the date"), new DateTellIntent()));

        Intent queryDateIntent = new QueryDateIntent();
        ret.add(new SimpleTuple<>(InvocationPattern.parse("when is %date:date|string%"), queryDateIntent));
        ret.add(new SimpleTuple<>(InvocationPattern.parse("what day is %date:date|string% on"), queryDateIntent));

        return ret;
    }

    private class TimeTellIntent implements Intent {
        @Override
        public Skill getAssociatedSkill() {
            return ClockSkill.this;
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
            return ClockSkill.this;
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

    private class QueryDateIntent extends JsonFromFileIntent {
        @Override
        public Skill getAssociatedSkill() {
            return ClockSkill.this;
        }

        @Override
        public String getDisplayName() {
            return "Tell the date of a holiday";
        }

        @Override
        public String getShortDescription() {
            return getDisplayName();
        }

        @Override
        public String getDescription() {
            return getDescription();
        }

        @Override
        public Sayable processInvocation(Invocation i) {
            Object tmp = getJson();

            if (tmp instanceof Sayable) {
                return (Sayable) tmp;
            } else {
                String holiday = NLPHelper.join(i.getValue("date"));
                JSONObject json = (JSONObject) tmp;

                long status = (Long) json.get("status");
                if (status == 200) {
                    JSONObject arRoot = (JSONObject) json.get("holidays");

                    JSONObject closestHoliday = null;
                    int closestHolidayDiff = Integer.MAX_VALUE;

                    for(Iterator iterator = arRoot.keySet().iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        JSONArray holidaysOnDate = (JSONArray) arRoot.get(key);

                        for (int j = 0; j < holidaysOnDate.size() && closestHolidayDiff != 0; j++) {
                            JSONObject tmpHoliday = (JSONObject) holidaysOnDate.get(j);
                            int tmpDist = Math.strRawDiff(((String) tmpHoliday.get("name")).toLowerCase(), holiday);

                            if (tmpDist < closestHolidayDiff) {
                                closestHoliday = tmpHoliday;
                                closestHolidayDiff = tmpDist;
                            }
                        }
                    }

                    if (closestHolidayDiff < 5) {
                        return new Phrase(holiday + " is on " + closestHoliday.get("date"));
                    } else {
                        return new Phrase("Sorry, I don't know when " + holiday + " is.");
                    }
                } else {
                    return new Phrase("Sorry, status was " + status + ".");
                }
            }
        }

        @Override
        public String getUrl() {
            /*StringBuilder url = new StringBuilder();
            url.append("https://holidayapi.com/v1/holidays?country=");
            url.append("US");
            url.append("&year=");
            url.append("2016");
//            url.append("&month=");
//            url.append("02");
            url.append("&key=");
            // url.append("dfdb2f65-9f36-4a59-9169-7cd9cd68c4bf"); // Test key
            url.append("3325d3fc-f212-4b91-9aa1-a57051900ea2"); // Live key
            return url.toString();*/
            return "holiday-cache.json";
        }
    }
}
