package com.viveret.pilexa.pi.util;

import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by viveret on 2/21/17.
 */
public interface ConfigTransactionLayer {
    JSONObject getRoot();

    int getInt(String key);

    boolean getBoolean(String key);

    String getString(String key);

    List<String> getStringArray(String key);

    Object get(String key);
}
