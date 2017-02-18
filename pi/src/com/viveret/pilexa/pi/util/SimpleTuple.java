package com.viveret.pilexa.pi.util;

/**
 * Created by viveret on 2/3/17.
 */
public class SimpleTuple<E1, E2> {
    public E1 a;
    public E2 b;

    public SimpleTuple(E1 a, E2 b) {
        this.a = a;
        this.b = b;
    }

    public SimpleTuple() {
        this.a = null;
        this.b = null;
    }
}
