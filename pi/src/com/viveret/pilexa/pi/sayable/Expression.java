package com.viveret.pilexa.pi.sayable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viveret on 2/3/17.
 */
public class Expression extends Sayable {
    private List<Sayable> myParts;

    public Expression(List<Sayable> theParts) {
        myParts = new ArrayList<>(theParts);
    }

    public Expression(Sayable[] theParts) {
        myParts = Arrays.asList(theParts);
    }

    @Override
    public void speak() {
        for (Sayable s : myParts) {
            s.speak();
        }
    }

    @Override
    public String toString() {
        return myParts.toString();
    }
}
