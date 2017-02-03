package com.viveret.pilexa.pi.sayable;

import org.apache.log4j.Logger;

/**
 * Created by viveret on 2/3/17.
 */
public abstract class Sayable {
    protected static final Logger log = Logger.getRootLogger();// Logger.getLogger(getClass().getName());

    public abstract void speak();
}
