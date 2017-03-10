package com.viveret.pilexa.pi.event;

import org.json.JSONObject;

/**
 * Created by viveret on 3/7/17.
 */
public class MusicEvent extends Event {
    private final String myQuery;

    public MusicEvent(String theQuery) {
        this.myQuery = theQuery;
    }

    @Override
    public String getType() {
        return "music";
    }

    @Override
    public JSONObject toJson() {
        JSONObject ret = super.toJson();
        ret.put("query", myQuery);
        return ret;
    }
}
