package com.viveret.pilexa.pi.sayable;

import com.viveret.pilexa.pi.util.Config;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;
import org.apache.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import java.util.Locale;

/**
 * Created by viveret on 2/3/17.
 */
public abstract class Sayable {
    protected static final Logger log = Logger.getRootLogger();// Logger.getLogger(getClass().getName());
    private static MaryInterface marytts;

    public static void init() {
        try {
            marytts = new LocalMaryInterface();
            log.info("I currently have " + marytts.getAvailableVoices() + " voices in "
                    + marytts.getAvailableLocales() + " languages available.");
            log.info("Out of these, " + marytts.getAvailableVoices(Locale.US) + " are for US English.");

            String theVoice = Config.inst().getString("system.voice");
            if ("default".equalsIgnoreCase(theVoice)) {
                String[] arTmp = new String[marytts.getAvailableVoices(Locale.US).size()];
                marytts.getAvailableVoices(Locale.US).toArray(arTmp);

                if (arTmp.length > 0) {
                    theVoice = arTmp[0];
                } else {
                    throw new IllegalStateException("Must have more than one voice");
                }
            }

            marytts.setVoice(theVoice);
        } catch (MaryConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    protected static void sayOutLoud(String s) {
        try {
            AudioPlayer ap = new AudioPlayer();
            AudioInputStream audio = marytts.generateAudio(s);
            ap.setAudio(audio);
            ap.start();
            ap.join();
        } catch (SynthesisException ex) {
            System.err.println("Error saying phrase.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void speak();
}
