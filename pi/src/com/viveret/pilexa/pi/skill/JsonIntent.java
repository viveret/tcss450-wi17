package com.viveret.pilexa.pi.skill;

/**
 * Created by viveret on 2/4/17.
 */
public abstract class JsonIntent implements Intent {
    public abstract String getUrl();

    public abstract Object getJson();
}
