package com.viveret.pilexa.pi.util;

import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by viveret on 2/21/17.
 */
public interface ConfigTransactionLayer {
    JSONObject getRoot();

    int getInt(String key);

    int getInt(String key, int theDefault);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean theDefault);

    String getString(String key);

    String getString(String key, String theDefault);

    List<String> getStringArray(String key);

    Object get(String key);

    Object get(String key, Object theDefault);

    void set(String key, Object value);
}
