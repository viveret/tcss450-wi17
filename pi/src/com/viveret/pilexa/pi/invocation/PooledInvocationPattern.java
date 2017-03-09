package com.viveret.pilexa.pi.invocation;

import com.viveret.pilexa.pi.util.Config;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by viveret on 2/5/17.
 */
public class PooledInvocationPattern implements InvocationPattern, Runnable {
    public static final List<PooledInvocationPattern> myPool = new ArrayList<>();

    private Object self = null;

    public PooledInvocationPattern(String thePattern) {
        self = thePattern;
        myPool.add(this);
    }

    public static void runPool() {
        int numThreads = Config.inst().getInt("system.numThreadsPerCore");
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < myPool.size(); i++) {
            executor.execute(myPool.get(i));
        }
        executor.shutdown();
        while (!executor.isTerminated());
        myPool.clear();
    }

    @Override
    public Invocation parse(CoreMap sentence) {
        return ((InvocationPattern) self).parse(sentence);
    }

    @Override
    public void run() {
        self = new FormattedInvocationPattern((String) self);
    }
}
