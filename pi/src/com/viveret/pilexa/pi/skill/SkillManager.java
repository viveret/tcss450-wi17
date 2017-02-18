package com.viveret.pilexa.pi.skill;

import com.viveret.pilexa.pi.invocation.PooledInvocationPattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 2/5/17.
 */
public class SkillManager {
    private static SkillManager myInst = null;
    public static SkillManager inst() {
        if (myInst == null) {
            myInst = new SkillManager();
        }
        return myInst;
    }


    private static final List<Skill> mySkills = new ArrayList<>();
    public static final void registerSkill(Skill s) {
        mySkills.add(s);
    }


    private SkillManager() {
        loadSkills();
    }

    private void loadSkills() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("pilexa-config.json").getFile());

        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(file));
            JSONArray skills = (JSONArray) root.get("skills");

            for (int i = 0; i < skills.size(); i++) {
                registerSkill(new ManifestSkill((String) skills.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PooledInvocationPattern.runPool();
    }

    public List<Skill> getSkills() {
        return new ArrayList<>(mySkills);
    }
}
