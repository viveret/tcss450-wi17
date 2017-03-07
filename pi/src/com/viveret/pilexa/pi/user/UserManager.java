package com.viveret.pilexa.pi.user;

import com.viveret.pilexa.pi.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by viveret on 3/7/17.
 */
public class UserManager {
    private static UserManager myInst = null;
    public static UserManager inst() {
        if (myInst == null) {
            myInst = new UserManager();
        }
        return myInst;
    }


    private final Map<Integer, String> myUsers = new HashMap<>();
    private int myMaxId = 0;

    private UserManager() {
        getUserIdsAndUsernames();
    }

    private void getUserIdsAndUsernames() {
        FileUtil.getFile("users/", getClass()).mkdirs();
        for (File dir : FileUtil.getFile("users/", getClass()).listFiles(File::isDirectory)) {
            try {
                UserAccount user = new UserAccount(Integer.parseInt(dir.getName()));
                myUsers.put(user.getId(), user.getUsername());
                myMaxId = Math.max(myMaxId, user.getId());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Integer> getUserIdList() {
        return new ArrayList<>(myUsers.keySet());
    }

    public boolean userIdExists(int id) {
        return myUsers.containsKey(id);
    }

    public boolean usernameExists(String username) {
        return myUsers.containsValue(username);
    }

    public boolean isValidUsername(String username) {
        Pattern p = Pattern.compile("(a-zA-Z0-9_-\\.)");
        return p.matcher(username).matches();
    }

    public UserAccount createNewUser(String username, String password, String mac) {
        myMaxId++;
        UserAccount ret = new UserAccount(myMaxId);
        ret.setUsername(username);
        ret.updatePassword(password);
        ret.setMac(mac);
        try {
            ret.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
