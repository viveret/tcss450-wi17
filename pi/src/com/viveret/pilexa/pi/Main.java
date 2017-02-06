package com.viveret.pilexa.pi;

public class Main {
    public static void main(String [] args) {
        final PiLexaService pilexa = ConcretePiLexaService.inst();
        Thread t1 = new Thread(pilexa);
        Thread t2 = new Thread(new TextInterfacePiLexaProxy());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }
}
