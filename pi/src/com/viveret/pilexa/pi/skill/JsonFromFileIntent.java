package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.sayable.Phrase;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by viveret on 2/4/17.
 */
public abstract class JsonFromFileIntent extends JsonIntent {
    public Object getJson(String packageName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        String finalUrl = "skills/" + packageName + "/" + getUrl();

        String resFile;
        try {
            resFile = classLoader.getResource(finalUrl).getFile();
        } catch (NullPointerException e) {
            throw new FileNotFoundException(finalUrl);
        }
        File file = new File(resFile);
        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader(file);
            Object ret = parser.parse(reader);
            reader.close();
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I couldn't read from my sources.");
        } catch (ParseException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I couldn't read my writing sources.");
        }
    }
}
