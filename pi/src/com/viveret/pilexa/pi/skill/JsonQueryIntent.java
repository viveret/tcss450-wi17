package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.sayable.Phrase;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by viveret on 2/4/17.
 */
public abstract class JsonQueryIntent extends JsonIntent {
    public Object getJson() {
        try {
            URL url = new URL(getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json;");

            conn.connect();

            JSONParser parser = new JSONParser();
            return parser.parse(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I could not connect.");
        } catch (ParseException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I couldn't read from my sources.");
        } catch (Exception e) {
            e.printStackTrace();
            return new Phrase("Sorry, something went wrong.");
        }
    }
}
