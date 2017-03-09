package com.viveret.pilexa.pi.util;

import java.io.File;

/**
 * Created by viveret on 3/7/17.
 */
public class FileUtil {
    public static File getFile(String location, Class c) {
        // ClassLoader classLoader = c.getClassLoader();
        /*URL fileRes = null; // classLoader.getResource(location);
        File fin = null;

        if (fileRes != null) {
            try {
                String s = fileRes.getFile();
                if (s != null) {
                    fin = new File(s);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (fin == null) {
            fin = new File("res/" + location);
            if (fin == null) {
                fin = new File(location);
            }
        }

        return fin;
        */
        return new File("res/" + location);
    }
}
