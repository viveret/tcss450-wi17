package com.viveret.pilexa.pi.util;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by viveret on 2/21/17.
 */
public class ConfigFile implements ConfigTransactionLayer {
    private static final Logger logger = Logger.getRootLogger();

    private String myLocation;
    private JSONObject myRoot;
    private boolean myIsDirty = false;

    public ConfigFile(String location) {
        myLocation = location + ".json";
        File fin = FileUtil.getFile(myLocation, getClass());

        if (fin != null && fin.exists()) {
            try {
                JSONParser parser = new JSONParser();
                myRoot = (JSONObject) parser.parse(new FileReader(fin));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            myRoot = new JSONObject();
            setDirty();
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
    public int getInt(String key, int theDefault) {
        Object ret = get(key);
        if (ret == null) {
            return theDefault;
        } else {
            return (int) ((Long) ret).longValue();
        }
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    @Override
    public boolean getBoolean(String key, boolean theDefault) {
        Object ret = get(key);
        if (ret == null) {
            return theDefault;
        } else {
            return (boolean) ret;
        }
    }

    @Override
    public String getString(String key) {
        return (String) get(key);
    }

    @Override
    public String getString(String key, String theDefault) {
        Object ret = get(key);
        if (ret == null) {
            return theDefault;
        } else {
            return ret.toString();
        }
    }

    @Override
    public List<String> getStringArray(String key) {
        List<String> tmp = new ArrayList<>();
        JSONArray ar = (JSONArray) get(key);
        for (Object anAr : ar) {
            tmp.add(anAr.toString());
        }

        return tmp;
    }

    @Override
    public Object get(String key, Object theDefault) {
        Object ret = get(key);
        if (ret == null) {
            return theDefault;
        } else {
            return ret;
        }
    }

    @Override
    public Object get(String key) {
        String[] keyCrumbs = key.split("\\.");
        JSONObject cur = followBreadcrumbs(keyCrumbs);

        Object r = cur.get(keyCrumbs[keyCrumbs.length - 1]);
        if (r == null) {
            throw new NoSuchElementException(key);
        } else {
            return r;
        }
    }

    @Override
    public void set(String key, Object value) {
        String[] keyCrumbs = key.split("\\.");
        JSONObject parent = followBreadcrumbs(keyCrumbs);

        parent.put(keyCrumbs[keyCrumbs.length - 1], value);
        setDirty();
    }

    private JSONObject followBreadcrumbs(String[] keyCrumbs) {
        JSONObject parent = myRoot;
        for (int i = 0; i < keyCrumbs.length - 1; i++) {
            parent = (JSONObject) parent.get(keyCrumbs[i]);
            if (parent == null) {
                StringBuilder trail = new StringBuilder();
                for (int j = 0; j <= i; j++) {
                    trail.append(keyCrumbs[j]);
                    trail.append('.');
                }
                trail.setLength(trail.length() - 1);
                throw new NoSuchElementException(trail.toString());
            }
        }

        return parent;
    }

    public void setDirty() {
        myIsDirty = true;
    }

    public boolean isDirty() {
        return myIsDirty;
    }

    public void save() throws IOException {
        File fout = FileUtil.getFile(myLocation, getClass());
        if (!fout.exists()) {
            fout.getParentFile().mkdirs();
            fout.createNewFile();
        }

        if (!fout.canWrite()) {
            logger.error("Cannot write to " + myLocation);
            throw new IOException("Cannot write to " + myLocation);
        }

        OutputStream os = new FileOutputStream(fout);
        os.write(myRoot.toString().getBytes());
        os.close();

        myIsDirty = false;
    }
}
