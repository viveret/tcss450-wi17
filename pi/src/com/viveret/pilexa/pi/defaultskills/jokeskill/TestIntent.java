package com.viveret.pilexa.pi.defaultskills.jokeskill;

import com.viveret.pilexa.pi.skill.AndroidIntentIntent;

/**
 * Created by viveret on 2/5/17.
 */
public class TestIntent extends AndroidIntentIntent {
    @Override
    public String getIntentString() {
        return "android.intent.action.CAMERA_BUTTON";
    }
}
