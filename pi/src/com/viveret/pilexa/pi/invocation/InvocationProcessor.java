package com.viveret.pilexa.pi.invocation;

import com.viveret.pilexa.pi.sayable.Sayable;

/**
 * Created by viveret on 2/5/17.
 */
public interface InvocationProcessor {
    Sayable processInvocation(Invocation i);
}
