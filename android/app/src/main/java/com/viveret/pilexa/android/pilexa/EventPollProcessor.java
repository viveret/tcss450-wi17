package com.viveret.pilexa.android.pilexa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by viveret on 3/7/17.
 */

public abstract class EventPollProcessor {
    private Deque<JSONObject> myQueue = null;

    public EventPollProcessor(JSONArray evs) throws JSONException {
        addEvents(evs);
    }

    public EventPollProcessor() {
    }

    public void addEvents(JSONArray evs) throws JSONException {
        myQueue = new ArrayDeque<>();
        for (int i = 0; i < evs.length(); i++) {
            myQueue.addLast(evs.getJSONObject(i));
        }
    }

    public void start() {
        if (myQueue == null) {
            throw new IllegalStateException("Must assign events before calling start");
        }

        while (!myQueue.isEmpty()) {
            process(myQueue.pollFirst());
        }

        myQueue = null;
    }

    public abstract void process(JSONObject ev);
}
