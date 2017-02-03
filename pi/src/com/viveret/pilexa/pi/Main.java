package com.viveret.pilexa.pi;

public class Main {
    public static void main(String [] args) {
        final PiLexaService pilexa = new ConcretePiLexaService();
        pilexa.connect();

        //String str = "repeat back to me I am a good Alexa clone";
        String str = "tell me the time";
        pilexa.interpret(str);

        pilexa.disconnect();
    }
}
