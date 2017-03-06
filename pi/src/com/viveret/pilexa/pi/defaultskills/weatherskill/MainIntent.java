package com.viveret.pilexa.pi.defaultskills.weatherskill;

import com.viveret.pilexa.pi.PiLexaService;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.JsonQueryIntent;
import org.json.simple.JSONObject;

/**
 * Created by Daniel on 2/25/17.
 */
public class MainIntent extends JsonQueryIntent {
    @Override
    public Sayable processInvocation(Invocation i) {
        Object tmp = getJson();

        if (tmp instanceof Sayable) {
            return (Sayable) tmp;
        } else {
            JSONObject weatherJSON = ((JSONObject) tmp);
            JSONObject currentObservation = (JSONObject) weatherJSON.get("current_observation");
            String weatherLocation = ((JSONObject) currentObservation.get("display_location")).get("city").toString();
            String currentWeatherCondition = ((String) currentObservation.get("weather"));
            Double currentWeatherTemp = ((Double) currentObservation.get("temp_f"));

//            getConnectedPilexaService().DispatchIntent(null);

            return new Phrase(new StringBuilder().append("The weather in ").append(weatherLocation).append(" is ").append(currentWeatherCondition).append(" with a temperature of ").append(currentWeatherTemp).append(" degrees fahrenheit.").toString());
        }
    }



    @Override
    public String getUrl() {
        return "http://api.wunderground.com/api/3b19c6e0e0728084/conditions/q/WA/Tacoma.json";
    }
}

