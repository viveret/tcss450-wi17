package com.viveret.pilexa.pi.skill;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 2/5/17.
 */
public class ManifestSkill implements Skill {
    private String myDisplayName, myShortDesc, myDesc, myPublisher, myVersionStr, myIconUrl, myClassPath;
    private int myVersionNumber;

    private List<Intent> myIntents = new ArrayList<>();

    public ManifestSkill(final String classPath) {
        myClassPath = classPath;
        ClassLoader classLoader = getClass().getClassLoader();
        String finalUrl = "skills/" + String.join("/", classPath.split("\\.")) + "/manifest.json";
        URL tmpUrl = classLoader.getResource(finalUrl);
        String tmpFile = tmpUrl.getFile();
        File file = new File(tmpFile);

        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(file));
            getSkillInfo(root);
            getJsonIntents(root);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getJsonIntents(JSONObject root) {
        final Logger log = Logger.getRootLogger();
        JSONArray intents = (JSONArray) root.get("intents");
        for (int i = 0; i < intents.size(); i++) {
            try {
                myIntents.add(new ManifestIntent((JSONObject) intents.get(i), this));
            } catch (ClassNotFoundException e) {
                log.error("Skill not found: " + intents.get(i).toString());
            }
        }
    }

    private void getSkillInfo(JSONObject root) {
        myDisplayName = (String) root.get("name");
        myShortDesc = (String) root.get("shortDesc");
        myDesc = (String) root.get("longDesc");
        myPublisher = (String) root.get("publisher");
        myVersionStr = (String) root.get("versionStr");
        myVersionNumber = (int) ((Long) root.get("version")).longValue();
        myVersionStr = (String) root.get("iconUrl");
        myIconUrl = (String) root.get("iconUrl");
    }

    @Override
    public String getDisplayName() {
        return myDisplayName;
    }

    @Override
    public String getShortDescription() {
        return myShortDesc;
    }

    @Override
    public String getDescription() {
        return myDesc;
    }

    @Override
    public String getPublisher() {
        return myPublisher;
    }

    @Override
    public String getPackageName() {
        return myClassPath;
    }

    @Override
    public String getVersionString() {
        return myVersionStr;
    }

    @Override
    public int getVersionNumber() {
        return myVersionNumber;
    }

    @Override
    public String getIconUrl() {
        return myIconUrl;
    }

    @Override
    public List<Intent> getIntents() {
        return myIntents;
    }
}
