package com.viveret.pilexa.pi.util;

import java.io.FileNotFoundException;

/**
 * Created by viveret on 2/5/17.
 */
public class Config extends ConfigFile {
    private static Config myInst = null;

    public Config() throws FileNotFoundException {
        super("pilexa-config");
    }

    public static Config inst() {
        if (myInst == null) {
            try {
                myInst = new Config();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return myInst;
    }
}
