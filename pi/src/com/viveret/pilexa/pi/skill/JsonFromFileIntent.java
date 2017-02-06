package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.sayable.Phrase;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by viveret on 2/4/17.
 */
public abstract class JsonFromFileIntent extends JsonIntent {
    public Object getJson(String packageName) {
        ClassLoader classLoader = getClass().getClassLoader();
        String finalUrl = "skills/" + packageName + "." + getUrl();
        File file = new File(classLoader.getResource(finalUrl).getFile());
        JSONParser parser = new JSONParser();

        try {
            return parser.parse(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I couldn't read from my sources.");
        } catch (ParseException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I couldn't read my writing sources.");
        }
    }
}
