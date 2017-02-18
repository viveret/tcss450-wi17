package com.viveret.pilexa.pi.inputmethods;

import com.viveret.pilexa.pi.AbstractPiLexaServiceProxy;
import com.viveret.pilexa.pi.sayable.Phrase;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.IOException;

/**
 * Created by viveret on 2/4/17.
 */
public class Sphinx4PiLexaProxy extends AbstractPiLexaServiceProxy {
    private LiveSpeechRecognizer recognizer;

    @Override
    public void connect() {
        super.connect();

        Configuration config = new Configuration();
        config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        config.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        config.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        try {
            recognizer = new LiveSpeechRecognizer(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        connect();

        do {
            getLog().info("Getting user input through voice");
            recognizer.startRecognition(true);

            SpeechResult result;
            while ((result = recognizer.getResult()) == null) {
                new Phrase("Sorry, I couldn't understand what you said. Please try again.").speak();
            }

            // Pause recognition process.
            recognizer.stopRecognition();

            if (result != null) {
                getLog().debug(result.getNbest(100));
                interpret(result.getHypothesis());
            }
        } while (isConnected());

        disconnect();
    }
}
