package com.viveret.pilexa.pi.inputmethods;

import com.viveret.pilexa.pi.ConcretePiLexaService;
import com.viveret.pilexa.pi.InputSource;
import com.viveret.pilexa.pi.PiLexaService;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 2/15/17.
 */
public class InputMethodManager {
    private static InputMethodManager myInst = null;
    private List<InputSource> myInputSrcs = new ArrayList<>();

    private InputMethodManager() {
        loadInputs();
    }

    public static InputMethodManager inst() {
        if (myInst == null) {
            myInst = new InputMethodManager();
        }

        return myInst;
    }

    public List<InputSource> getInputMethods() {
        return new ArrayList<>(myInputSrcs);
    }

    public void addInputSource(InputSource src) {
        myInputSrcs.add(src);
    }

    public void addInputSourceFromClass(String className) throws ClassNotFoundException {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor();
            addInputSource((InputSource) ctor.newInstance());
        } catch (Exception e) {
            throw new ClassNotFoundException(e.toString());
        }
    }

    public void start(PiLexaService pilexa) {
        for (InputSource src : myInputSrcs) {
            src.start(pilexa);
        }
        pilexa.getLog().info("Done starting inputs");
    }

    public void stop() {
        for (InputSource src : myInputSrcs) {
            src.stop();
            src.shutdown();
        }
    }

    private void loadInputs() {
        try {
            List<String> skills = ConcretePiLexaService.inst().getConfig().getStringArray("input-methods");
            for (int i = 0; i < skills.size(); i++) {
                addInputSourceFromClass(skills.get(i));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //PooledInvocationPattern.runPool();
    }
}
