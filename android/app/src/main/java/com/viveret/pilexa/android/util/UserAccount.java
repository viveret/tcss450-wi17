package com.viveret.pilexa.android.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 3/6/17.
 */
public class UserAccount implements Serializable {
    private final List<UserDevice> myDevices = new ArrayList<>(1);

    private int myUserId;
    private String myName;
    private String myUsername;
    private String myLanguage;
    private String myUserAuthenticationToken;

    public UserAccount(int theUserId, String theName, String theUsername, String theLanguage, String theAuthToken) {
        this.myUserId = theUserId;
        this.myName = theName;
        this.myUsername = theUsername;
        this.myLanguage = theLanguage;
        this.myUserAuthenticationToken = theAuthToken;
    }

    public UserAccount(String theName, String theUsername, String theLanguage) {
        this(-1, theName, theUsername, theLanguage, null);
    }

    public int getUserId() {
        return myUserId;
    }

    public String getName() {
        return myName;
    }

    public void setName(String myName) {
        this.myName = myName;
    }

    public String getUsername() {
        return myUsername;
    }

    public void setUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    public String getLanguage() {
        return myLanguage;
    }

    public void setLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    public String getUserAuthenticationToken() {
        return myUserAuthenticationToken;
    }

    public void setUserAuthenticationToken(String myUserAuthenticationToken) {
        this.myUserAuthenticationToken = myUserAuthenticationToken;
    }

    public boolean hasUserId() {
        return myUserId >= 0;
    }

    public boolean hasAuthToken() {
        return getUserAuthenticationToken() != null;
    }


    public static class UserDevice implements Serializable {
        private final String myMacAddress;

        public UserDevice(String theMacAddress) {
            this.myMacAddress = theMacAddress;
        }
    }
}
