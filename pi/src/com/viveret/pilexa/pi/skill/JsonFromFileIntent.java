package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.sayable.Phrase;
import org.json.JSONObject;

import java.io.*;

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
        try {
            StringBuilder jsonStr = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String ln;
            while ((ln = reader.readLine()) != null) {
                jsonStr.append(ln);
            }
            reader.close();
            return new JSONObject(jsonStr.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new Phrase("Sorry, I couldn't read from my sources.");
        }
    }
}
