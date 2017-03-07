package com.viveret.pilexa.pi.user;

import com.viveret.pilexa.pi.util.ConfigFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.Serializable;
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
    }

    public List<UserDevice> getDevices() {
        if (myDevices == null) {
            myDevices = new ArrayList<>();
            JSONArray devices = (JSONArray) get("devices");
            if (devices != null) {
                for (int i = 0; i < devices.length(); i++) {
                    myDevices.add(new UserDevice(devices.getJSONObject(i)));
                }
            } else {
                return null;
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
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hashStr = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hashStr.append(String.format("%02X", b));
            }

            set("password", hashStr.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void setMac(String mac) {
        set("mac", mac);
    }

    public static class UserDevice {
        private final String myMacAddress;
        private String myName;

        public UserDevice(JSONObject json) {
            this.myMacAddress = json.getString("mac-address");
            this.myName = json.getString("name");
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
    }
}
