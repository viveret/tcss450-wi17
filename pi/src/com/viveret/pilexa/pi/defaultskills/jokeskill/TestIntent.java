package com.viveret.pilexa.pi.defaultskills.jokeskill;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.AndroidIntentIntent;
import com.viveret.pilexa.pi.skill.JsonQueryIntent;
import org.json.simple.JSONObject;

/**
 * Created by viveret on 2/5/17.
 */
public class TestIntent extends AndroidIntentIntent {
    @Override
    public String getIntentString() {
        return "android.intent.action.CAMERA_BUTTON";
    }
}
