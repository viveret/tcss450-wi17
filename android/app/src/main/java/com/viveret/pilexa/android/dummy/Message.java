package com.viveret.pilexa.android.dummy;

/**
 * A dummy item representing a piece of content.
 */
public class Message {
    public final String id;
    public final String content;
    public final String details;

    public final boolean isFromUser;

    public Message(String id, String content, String details, boolean isFromUser) {
        this.id = id;
        this.content = content;
        this.details = details;
        this.isFromUser = isFromUser;
    }

    @Override
    public String toString() {
        return content;
    }
}
