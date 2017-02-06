package com.viveret.pilexa.pi;

import java.util.Scanner;

public class Main {
    public static void main(String [] args) {
        final PiLexaService pilexa = new ConcretePiLexaService();
        pilexa.connect();
        //System.out.println("Hello! I'm PiLexa. Type to talk to me.");

        //String sin = null;
        //sin = "tell me a joke";
        //sin = "repeat back to me: I am Obama. It is december. I am going to a bank.";
        // sin = "when is may day?";

        // Uncomment to show NLP info for input string
        //sin = "debug print nlp tags " + sin;
//        Scanner scan = new Scanner(System.in);
//        do {
//            System.out.printf("> ");
//            sin = scan.nextLine();
//            pilexa.interpret(sin);
//            // sin = "bye";
//        } while (!sin.equals("bye"));

        pilexa.disconnect();
    }
}
