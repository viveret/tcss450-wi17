package com.viveret.pilexa.pi.user;

import com.viveret.pilexa.pi.util.ConfigFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 3/7/17.
 */
public class UserAccount extends ConfigFile {
    private List<UserDevice> myDevices = null;

    public UserAccount(int userId) throws FileNotFoundException {
        super("users/" + userId + "/config");
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
        return getInt("id");
    }

    public String getLanguage() {
        return getString("lang");
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
