package com.viveret.pilexa.pi.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by viveret on 3/7/17.
 */
public class UserManager {
    private final Map<Integer, String> myUsers = new HashMap<>();

    public UserManager() {
        getUserIdsAndUsernames();
    }

    private void getUserIdsAndUsernames() {
        for (File dir : new File("users/").listFiles(File::isDirectory)) {
            try {
                UserAccount user = new UserAccount(Integer.parseInt(dir.getName()));
                myUsers.put(new Integer(user.getId()), user.getUsername());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Integer> getUserIdList() {
        return new ArrayList<>(myUsers.keySet());
    }

    public boolean userIdExists(int id) {
        return myUsers.containsKey(new Integer(id));
    }

    public boolean usernameExists(String username) {
        return myUsers.containsValue(username);
    }

    public boolean isValidUsername(String username) {
        Pattern p = Pattern.compile("(a-zA-Z0-9_-\\.)");
        return p.matcher(username).matches();
    }
}
