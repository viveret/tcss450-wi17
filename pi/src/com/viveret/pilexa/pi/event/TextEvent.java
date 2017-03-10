package com.viveret.pilexa.pi.event;

import org.json.JSONObject;

/**
 * Created by viveret on 3/7/17.
 */
public class TextEvent extends Event {
    private final String myNumber, myMessage;

    public TextEvent(String myNumber, String myMessage) {
        this.myNumber = myNumber;
        this.myMessage = myMessage;
    }

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public JSONObject toJson() {
        JSONObject ret = super.toJson();
        ret.put("number", myNumber);
        ret.put("message", myMessage);
        return ret;
    }
}
