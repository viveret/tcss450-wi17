package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.sayable.Phrase;
import org.json.JSONObject;

import java.io.BufferedReader;
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
            StringBuilder jsonStr = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ln;
            while ((ln = reader.readLine()) != null) {
                jsonStr.append(ln);
            }
            return new JSONObject(jsonStr.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I could not connect.");
        } catch (Exception e) {
            e.printStackTrace();
            return new Phrase("Sorry, something went wrong.");
        }
    }
}
