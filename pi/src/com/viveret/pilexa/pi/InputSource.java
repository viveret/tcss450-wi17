package com.viveret.pilexa.pi;

/**
 * Created by viveret on 2/14/17.
 */
public interface InputSource {
    void start(PiLexaService piLexaService);

    void stop();

    void shutdown();

    String getInput();
}
