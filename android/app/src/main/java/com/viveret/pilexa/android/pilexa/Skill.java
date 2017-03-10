package com.viveret.pilexa.android.pilexa;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for individual skill data.
 * Definition of skill: Skills are collections of intents. They are essentially a feature set that PiLexa understands.
 */
public class Skill implements Serializable {
    public static final String NAME = "name", SHORT_DESC = "shortDesc",
            LONG_DESC = "longDesc", INTENTS = "intents",
            DESC = "desc", INVOCATIONS = "invocations";
    private static List<Skill> tempSkillList;
    String skillName;
    String shortDesciption;
    String longDescription;


    /**
     * Creates a new skill from a name, short description, and long description.
     * @param skillName the name of the skill
     * @param shortDesciption the short description of the skill
     * @param longDescription the long description of the skill
     */
    public Skill(String skillName, String shortDesciption, String longDescription) {
        this.longDescription = longDescription;
        this.skillName = skillName;
        this.shortDesciption = shortDesciption;
        tempSkillList = new ArrayList<Skill>();
    }

    /**
     *
     * @param skillDetailList
     * @param skillList
     * @return
     */
    public static String parseSkillJSON(List<String> skillDetailList, List<Skill> skillList) {
        String resultOfParse = "";
        for (String skillDetails : skillDetailList) {
            JSONObject newSkillObject = null;
            try {
                newSkillObject = new JSONObject(skillDetails);
                String name = newSkillObject.get(NAME).toString();
                String shortDesc = newSkillObject.get(SHORT_DESC).toString();
                String longDesc = newSkillObject.get(LONG_DESC).toString();
                if(!(name.compareTo("Debug") == 0)){
                    Skill newSkill = new Skill(name, shortDesc, longDesc);
                    skillList.add(newSkill);
                }

            } catch (JSONException e) {
                resultOfParse = "Failed to parse skill details, reason: " + e;
                e.printStackTrace();
            }
            resultOfParse = "success";
        }
        return resultOfParse;
    }

    public void addSkill(Skill skill) {
        tempSkillList.add(skill);
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getShortDesciption() {
        return shortDesciption;
    }

    public void setShortDesciption(String shortDesciption) {
        this.shortDesciption = shortDesciption;
    }


}