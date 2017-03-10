package com.viveret.pilexa.android.util;

/**
 * Represents a message that the user or PiLexa can send or receive through conversation.
 */
public class Message {
    public long id;
    public final String body;
    public final boolean isFromUser;

    /**
     * Creates a new Message from an unique id, body message, and if the message was from the user.
     * @param id the unique id of the message
     * @param body the message string
     * @param isFromUser if the message was from the user
     */
    public Message(long id, String body, boolean isFromUser) {
        this.id = id;
        this.body = body;
        this.isFromUser = isFromUser;
    }

    @Override
    public String toString() {
        return body;
    }

    /**
     * Gets the id of the message.
     * @return the id of the message.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of the message.
     * @param id the new id for the message.
     */
    public void setId(long id) {
        this.id = id;
    }
}
