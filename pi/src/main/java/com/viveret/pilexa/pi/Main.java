package com.viveret.pilexa.pi;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.adaptation.Stats;
import edu.cmu.sphinx.decoder.adaptation.Transform;
import edu.cmu.sphinx.result.WordResult;

public class Main {
    public static void main(String [] args) {
        System.out.println("Inside main");
        try {
            TranscriberDemo.main(args);
        } catch (Exception e) {
            
        }
    }
}
