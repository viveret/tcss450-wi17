package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.invocation.InvocationProcessor;

/**
 * Created by viveret on 2/4/17.
 */
public abstract class JsonIntent implements InvocationProcessor {
    public abstract String getUrl();
}
