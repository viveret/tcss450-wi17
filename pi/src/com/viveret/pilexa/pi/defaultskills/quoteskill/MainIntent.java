package com.viveret.pilexa.pi.defaultskills.quoteskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.JsonQueryIntent;
import org.json.JSONObject;

/**
 * Created by Daniel on 3/6/17.
 */
public class MainIntent extends JsonQueryIntent {
    @Override
    public Sayable processInvocation(Invocation i) {
        Object tmp = getJson();

        if (tmp instanceof Sayable) {
            return (Sayable) tmp;
        } else {
            JSONObject quoteJSON = ((JSONObject) tmp);
            String quoteText = ((String) quoteJSON.get("quoteText"));
            String authorAuthor = ((String) quoteJSON.get("quoteAuthor"));

//            getConnectedPilexaService().DispatchIntent(null);

            return new Phrase(new StringBuilder().append(quoteText).append(" by ").append(authorAuthor).toString());
        }
    }



    @Override
    public String getUrl() {
        return "http://api.forismatic.com/api/1.0/?method=getQuote&key=457653&format=json&lang=en";
    }
}


