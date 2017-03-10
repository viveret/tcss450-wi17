package com.viveret.pilexa.android.pilexa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the details of a user's account on a PiLexa instance.
 */
public class UserAccount implements Serializable {
    private final List<UserDevice> myDevices = new ArrayList<>(1);

    private int myUserId;
    private String myName;
    private String myUsername;
    private String myLanguage;
    private String myUserAuthenticationToken;

    /**
     * Create a new user account object.
     * @param theUserId the id of the user
     * @param theName the name of the user
     * @param theUsername the username of the user
     * @param theLanguage the language the user prefers
     * @param theAuthToken the authentication token for the user if they have recently logged in
     */
    public UserAccount(int theUserId, String theName, String theUsername, String theLanguage, String theAuthToken) {
        this.myUserId = theUserId;
        this.myName = theName;
        this.myUsername = theUsername;
        this.myLanguage = theLanguage;
        this.myUserAuthenticationToken = theAuthToken;
    }

    /**
     * Returns the id of the user.
     * @return the id of the user.
     */
    public int getUserId() {
        return myUserId;
    }

    /**
     * Returns the name of the user.
     * @return the name of the user.
     */
    public String getName() {
        return myName;
    }

    /**
     * Sets the name of the user.
     * @param myName the new name for the user
     */
    public void setName(String myName) {
        this.myName = myName;
    }

    /**
     * Returns the username for the user.
     * @return the username for the user.
     */
    public String getUsername() {
        return myUsername;
    }

    /**
     * Sets the username of the user.
     * @param myUsername the new username for the user
     */
    public void setUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    /**
     * Returns the language the user prefers.
     * @return the language the user prefers.
     */
    public String getLanguage() {
        return myLanguage;
    }

    /**
     * Sets the language the user prefers.
     * @param myLanguage the new language the user prefers
     */
    public void setLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    /**
     * Gets the user's authentication token if they have recently logged in.
     * @return the user's authentication token if they have recently logged in.
     */
    public String getUserAuthenticationToken() {
        return myUserAuthenticationToken;
    }

    /**
     * Sets the authentication for the user if the current token has expired or is no longer valid.
     * @param myUserAuthenticationToken the new auth token
     */
    public void setUserAuthenticationToken(String myUserAuthenticationToken) {
        this.myUserAuthenticationToken = myUserAuthenticationToken;
    }

    /**
     * Return if the user has an id, which indicates if it is a valid user.
     * @return if the user has an id, which indicates if it is a valid user.
     */
    public boolean hasUserId() {
        return myUserId >= 0;
    }

    /**
     * Returns if the user has an authentication token.
     * @return if the user has an authentication token.
     */
    public boolean hasAuthToken() {
        return getUserAuthenticationToken() != null;
    }

    /**
     * Adds a device to the user that the user has allowed to connect using their credentials.
     * @param userDevice the device to add.
     */
    public void addDevice(UserDevice userDevice) {
        myDevices.add(userDevice);
    }

    /**
     * Represents a device the user can connect with.
     */
    public static class UserDevice implements Serializable {
        private final String myMacAddress;

        /**
         * Create a new device using a mac address to identify it
         * @param theMacAddress the mac address of the device
         */
        public UserDevice(String theMacAddress) {
            this.myMacAddress = theMacAddress;
        }
    }
}
