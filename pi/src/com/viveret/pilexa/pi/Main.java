package com.viveret.pilexa.pi;

public class Main {
    public static void main(String [] args) {
        final PiLexaService pilexa = ConcretePiLexaService.inst();
        Thread t1 = new Thread(pilexa);
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }
}
