package com.viveret.pilexa.pi;

public class Main {
    public static void main(String [] args) {
        System.out.println("Testing Sphinx4..");
        try {
            TranscriberDemo.main(args);
        } catch (Exception e) {
            
        }
        
        System.out.println("Done! Testing Stanford CoreNLP...");

        try {
            SimpleExample.main(args);
        } catch (Exception e) {
            
        }

        System.out.println("Done!");
    }
}
