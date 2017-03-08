package com.viveret.pilexa.pi;

import org.json.JSONObject;

/**
 * Created by viveret on 3/7/17.
 */
public class AndroidIntentEvent extends Event {
    private final String myAndroidIntent;

    public AndroidIntentEvent(String myAndroidIntent) {
        this.myAndroidIntent = myAndroidIntent;
    }


    @Override
    public String getType() {
        return "androidIntent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject ret = super.toJson();
        ret.put("name", myAndroidIntent);
        return ret;
    }
}
