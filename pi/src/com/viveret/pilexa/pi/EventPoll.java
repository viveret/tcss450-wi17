package com.viveret.pilexa.pi;

import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by viveret on 3/7/17.
 */
public class EventPoll {
    private static EventPoll myInst = null;
    public static EventPoll inst() {
        if (myInst == null) {
            myInst = new EventPoll();
        }
        return myInst;
    }

    private final Deque<JSONObject> myQueue = new ArrayDeque<>();

    private EventPoll() {
    }

    public Deque<JSONObject> getQueue() {
        return myQueue;
    }

    public void addToQueue(JSONObject ev) {
        myQueue.addLast(ev);
    }

    public void addToQueue(Event ev) {
        myQueue.addLast(ev.toJson());
    }
}
