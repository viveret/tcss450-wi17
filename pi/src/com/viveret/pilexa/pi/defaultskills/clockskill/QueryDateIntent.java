package com.viveret.pilexa.pi.defaultskills.clockskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.JsonFromFileIntent;
import com.viveret.pilexa.pi.util.Math;
import com.viveret.pilexa.pi.util.NLPHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.util.Iterator;

/**
 * Created by viveret on 2/5/17.
 */
public class QueryDateIntent extends JsonFromFileIntent {
    @Override
    public Sayable processInvocation(Invocation i) {
        Object tmp = null;
        try {
            tmp = getJson(getClass().getPackage().getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (tmp == null) {
            return new Phrase("Could not read from cache.");
        }

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
