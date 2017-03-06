package com.viveret.pilexa.pi.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by viveret on 2/21/17.
 */
public class ConfigFile implements ConfigTransactionLayer {
    private String myLocation;
    private JSONObject myRoot;

    public ConfigFile(String location) throws FileNotFoundException {
        myLocation = location;
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileRes = classLoader.getResource(location + ".json");
        File fin = null;

        if (fileRes != null) {
            try {
                String s = fileRes.getFile();
                if (s != null) {
                    fin = new File(s);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (fin == null) {
            fin = new File("res/" + location + ".json");
        }

        if (fin != null) {
            try {
                JSONParser parser = new JSONParser();
                myRoot = (JSONObject) parser.parse(new FileReader(fin));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileNotFoundException(location + ".json");
        }
    }

    @Override
    public JSONObject getRoot() {
        return myRoot;
    }

    @Override
    public int getInt(String key) {
        return (int) ((Long) get(key)).longValue();
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    @Override
    public String getString(String key) {
        return (String) get(key);
    }

    @Override
    public List<String> getStringArray(String key) {
        List<String> tmp = new ArrayList<>();
        JSONArray ar = (JSONArray) get(key);
        for (int i = 0; i < ar.size(); i++) {
            tmp.add(ar.get(i).toString());
        }

        return tmp;
    }

    @Override
    public Object get(String key) {
        JSONObject cur = myRoot;
        String[] keyCrumbs = key.split("\\.");
        for (int i = 0; i < keyCrumbs.length - 1; i++) {
            cur = (JSONObject) cur.get(keyCrumbs[i]);
            if (cur == null) {
                StringBuilder trail = new StringBuilder();
                for (int j = 0; j <= i; j++) {
                    trail.append(keyCrumbs[j]);
                    trail.append('.');
                }
                trail.setLength(trail.length() - 1);
                throw new NoSuchElementException(trail.toString());
            }
        }

        Object r = cur.get(keyCrumbs[keyCrumbs.length - 1]);
        if (r == null) {
            throw new NoSuchElementException(key);
        } else {
            return r;
        }
    }
}
