package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.util.ConfigFile;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 2/5/17.
 */
public class ManifestSkill extends ConfigFile implements Skill {
    private String myClassPath;

    private List<Intent> myIntents = new ArrayList<>();

    public ManifestSkill(final String classPath) throws FileNotFoundException {
        super("skills/" + String.join("/", classPath.split("\\.")) + "/manifest");
        myClassPath = classPath;
        getJsonIntents();
    }

    private void getJsonIntents() {
        final Logger log = Logger.getRootLogger();
        JSONArray intents = (JSONArray) getRoot().get("intents");
        for (int i = 0; i < intents.length(); i++) {
            try {
                myIntents.add(new ManifestIntent(intents.getJSONObject(i), this));
            } catch (ClassNotFoundException e) {
                log.error("Skill not found: " + intents.get(i).toString());
            }
        }
    }

    @Override
    public String getDisplayName() {
        return (String) getRoot().get("name");
    }

    @Override
    public String getShortDescription() {
        return (String) getRoot().get("shortDesc");
    }

    @Override
    public String getDescription() {
        return (String) getRoot().get("longDesc");
    }

    @Override
    public String getPublisher() {
        return (String) getRoot().get("publisher");
    }

    @Override
    public String getPackageName() {
        return myClassPath;
    }

    @Override
    public String getVersionString() {
        return (String) getRoot().get("versionStr");
    }

    @Override
    public int getVersionNumber() {
        return (int) ((Long) getRoot().get("version")).longValue();
    }

    @Override
    public String getIconUrl() {
        return (String) getRoot().get("iconUrl");
    }

    @Override
    public List<Intent> getIntents() {
        return myIntents;
    }
}
