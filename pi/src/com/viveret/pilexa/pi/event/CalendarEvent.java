package com.viveret.pilexa.pi.event;

import org.json.JSONObject;

/**
 * Created by viveret on 3/7/17.
 */
public class CalendarEvent extends Event {
    private final String myMessage;
    private final long myLength;

    public CalendarEvent(String myMessage, long myLength) {
        this.myMessage = myMessage;
        this.myLength = myLength;
    }

    @Override
    public String getType() {
        return "setTimer";
    }

    @Override
    public JSONObject toJson() {
        JSONObject ret = super.toJson();
        ret.put("timerMsg", myMessage);
        ret.put("length", myLength);
        return ret;
    }
}
