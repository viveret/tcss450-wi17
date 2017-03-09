package com.viveret.pilexa.android.util;

/**
 * A dummy item representing a piece of body.
 */
public class Message {
    public long id;
    public final String body;
    public final boolean isFromUser;

    public Message(long id, String body, boolean isFromUser) {
        this.id = id;
        this.body = body;
        this.isFromUser = isFromUser;
    }

    @Override
    public String toString() {
        return body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
