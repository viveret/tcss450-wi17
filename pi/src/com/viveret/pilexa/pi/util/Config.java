package com.viveret.pilexa.pi.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 2/5/17.
 */
public class Config {
    private static Config myInst = null;
    private JSONObject myRoot;

    public Config() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("pilexa-config.json").getFile());
        JSONParser parser = new JSONParser();
        try {
            myRoot = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Config inst() {
        if (myInst == null) {
            myInst = new Config();
        }
        return myInst;
    }

    public int getInt(String key) {
        JSONObject cur = myRoot;
        String[] keyCrumbs = key.split("\\.");
        for (int i = 0; i < keyCrumbs.length - 1; i++) {
            cur = (JSONObject) cur.get(keyCrumbs[i]);
        }
        return (int) ((Long) cur.get(keyCrumbs[keyCrumbs.length - 1])).longValue();
    }

    public boolean getBoolean(String key) {
        JSONObject cur = myRoot;
        String[] keyCrumbs = key.split("\\.");
        for (int i = 0; i < keyCrumbs.length - 1; i++) {
            cur = (JSONObject) cur.get(keyCrumbs[i]);
        }
        return ((Boolean) cur.get(keyCrumbs[keyCrumbs.length - 1])).booleanValue();
    }

    public String getString(String key) {
        JSONObject cur = myRoot;
        String[] keyCrumbs = key.split("\\.");
        for (int i = 0; i < keyCrumbs.length - 1; i++) {
            cur = (JSONObject) cur.get(keyCrumbs[i]);
        }
        return (String) cur.get(keyCrumbs[keyCrumbs.length - 1]);
    }

    public List<String> getStringArray(String key) {
        JSONObject cur = myRoot;
        String[] keyCrumbs = key.split("\\.");
        for (int i = 0; i < keyCrumbs.length - 1; i++) {
            cur = (JSONObject) cur.get(keyCrumbs[i]);
        }

        List<String> tmp = new ArrayList<>();
        JSONArray ar = (JSONArray) cur.get(keyCrumbs[keyCrumbs.length - 1]);
        for (int i = 0; i < ar.size(); i++) {
            tmp.add(ar.get(i).toString());
        }

        return tmp;
    }
}
