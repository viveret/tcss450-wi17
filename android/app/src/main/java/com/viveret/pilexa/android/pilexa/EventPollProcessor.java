package com.viveret.pilexa.android.pilexa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Gets events from a JSON queue and processes them.
 */
public abstract class EventPollProcessor {
    private Deque<JSONObject> myQueue = null;

    /**
     * Default empty constructor.
     */
    public EventPollProcessor() {
    }

    /**
     * Adds events to this processor that need to be processed.
     * @param evs the queue of events
     * @throws JSONException if an event cannot be decoded from JSON
     */
    public void addEvents(JSONArray evs) throws JSONException {
        myQueue = new ArrayDeque<>();
        for (int i = 0; i < evs.length(); i++) {
            myQueue.addLast(evs.getJSONObject(i));
        }
    }

    /**
     * Processes all events that are in the queue.
     */
    public void start() {
        if (myQueue == null) {
            throw new IllegalStateException("Must assign events before calling start");
        }

        while (!myQueue.isEmpty()) {
            process(myQueue.pollFirst());
        }

        myQueue = null;
    }

    /**
     * Process a single event.
     * @param ev the event to process.
     */
    public abstract void process(JSONObject ev);
}
