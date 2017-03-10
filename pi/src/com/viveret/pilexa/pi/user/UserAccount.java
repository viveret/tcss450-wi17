package com.viveret.pilexa.pi.user;

import com.viveret.pilexa.pi.util.ConfigFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 3/7/17.
 */
public class UserAccount extends ConfigFile {
    private List<UserDevice> myDevices = null;
    private int myId;

    public UserAccount(int userId) {
        super("users/" + userId + "/config");
        myId = userId;
        getRoot().put("id", userId);
        if (!getRoot().has("name")) {
            getRoot().put("name", "[no name]");
        }
        if (!getRoot().has("lang")) {
            getRoot().put("lang", "en");
        }
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<UserDevice> getDevices() {
        if (myDevices == null) {
            myDevices = new ArrayList<>();

            JSONArray devices;
            if (getRoot().has("devices")) {
                devices = (JSONArray) get("devices");
            } else {
                devices = new JSONArray();
                getRoot().put("devices", devices);
            }
            for (int i = 0; i < devices.length(); i++) {
                myDevices.add(new UserDevice(devices.getJSONObject(i)));
            }
        }
        return myDevices;
    }

    public String getName() {
        return getString("name");
    }

    public String getUsername() {
        return getString("username");
    }

    public int getId() {
        return myId;
    }

    public String getLanguage() {
        return getString("lang");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public void updatePassword(String password) {
        // Need to hash password
        set("password", hashPassword(password));
    }

    public void setMac(String mac) {
        set("mac", mac);
    }

    public void addDevice(UserDevice device) {
        if (myDevices == null) {
            getDevices();
        }
        myDevices.add(device);
        getRoot().getJSONArray("devices").put(device.toJson());
    }

    public boolean passwordMatches(String other) {
        return getString("password").equals(hashPassword(other));
    }

    private static String hashPassword(String thePassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(thePassword.getBytes("UTF-8"));
            StringBuilder hashStr = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hashStr.append(String.format("%02X", b));
            }

            return hashStr.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class UserDevice {
        private final String myMacAddress;
        private String myName;

        public UserDevice(JSONObject json) {
            this.myMacAddress = json.getString("mac-address");
            this.myName = json.getString("name");
        }

        public UserDevice(String mac, String name) {
            myMacAddress = mac;
            myName = name;
        }

        public String getMacAddress() {
            return myMacAddress;
        }

        public String getName() {
            return myName;
        }

        public void setName(String theName) {
            this.myName = theName;
        }

        public JSONObject toJson() {
            JSONObject ret = new JSONObject();
            ret.put("mac-address", myMacAddress);
            ret.put("name", myName);
            return ret;
        }
    }
}
