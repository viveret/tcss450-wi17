package com.viveret.pilexa.pi.event;

import org.json.JSONObject;

/**
 * Created by viveret on 3/7/17.
 */
public abstract class Event {
    public abstract String getType();

    public JSONObject toJson() {
        JSONObject ret = new JSONObject();
        ret.put("type", getType());
        return ret;
    }
}
