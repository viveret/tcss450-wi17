package com.viveret.pilexa.pi;

import com.viveret.pilexa.pi.inputmethods.InputMethodManager;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.skill.Intent;
import com.viveret.pilexa.pi.skill.Skill;
import com.viveret.pilexa.pi.skill.SkillManager;
import com.viveret.pilexa.pi.user.UserManager;
import com.viveret.pilexa.pi.util.Config;
import com.viveret.pilexa.pi.util.ConfigTransactionLayer;
import com.viveret.pilexa.pi.util.SimpleTuple;
import com.viveret.pilexa.pi.util.ToPhoneIntent;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by viveret on 1/24/17.
 */
public class ConcretePiLexaService implements PiLexaService {
    private static final double CONFUSE_THRESHOLD = 0.01;

    private static PiLexaService myInst = null;
    private Logger log;
    private StanfordCoreNLP pipeline;
    private boolean isRunning = false;

    private ConcretePiLexaService() {
        BasicConfigurator.configure();
    }

    private void init() {
        boolean multiThreaded = true;

        Thread t = new Thread(
                this::initCoreNLP
        );
        Thread t2 = new Thread(
                this::initPiLexa
        );
        Thread t3 = new Thread(
                Sayable::init
        );

        try {
            if (multiThreaded) {
                t.start();
                t2.start();
                t3.start();

                t.join();
                t2.join();
                t3.join();
            } else {
                t.start();
                t.join();

                t2.start();
                t2.join();

                t3.start();
                t3.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }

    public static PiLexaService inst() {
        if (myInst == null) {
            myInst = new ConcretePiLexaService();
            ((ConcretePiLexaService) myInst).init();
        }
        return myInst;
    }

    @Override
    public void connect() {
        InputMethodManager.inst().start(this);
        isRunning = true;
    }

    @Override
    public void run() {
        connect();
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        } while (isRunning);
        disconnect();
    }

    private void initCoreNLP() {
        // build pipeline
        pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", String.join(",", Config.inst().getStringArray("corenlp.annotators")),
                        "ssplit.isOneSentence", "true",
                        "parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
                        "tokenize.language", "en"));
    }

    private void initPiLexa() {
        log = Logger.getRootLogger();// Logger.getLogger(getClass().getName());
        log.info("Connected to PiLexa Service.");

        SkillManager.inst().getSkills();
        InputMethodManager.inst().getInputMethods();
        UserManager.inst();
    }

    @Override
    public void disconnect() {
        InputMethodManager.inst().stop();
        log.info("Disconnected from PiLexa Service.");
        isRunning = false;
    }

    @Override
    public boolean isConnected() {
        return isRunning;
    }

    @Override
    public String interpret(String str) {
        log.info("Interpreting \"" + str + "\"");

        Annotation document = new Annotation(str);
        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            List<SimpleTuple<Invocation, Intent>> invocs = new ArrayList<>();

            for (Skill s : SkillManager.inst().getSkills()) {
                for (Intent i : s.getIntents()) {
                    for (InvocationPattern pattern : i.getInvocationPatterns()) {
                        invocs.add(new SimpleTuple<>(pattern.parse(sentence), i));
                    }
                }
            }

            Collections.sort(invocs, (a, b) -> {
                double diff = a.a.getConfidence() - b.a.getConfidence();
                diff = -diff;
                if (diff < 0)
                    return -1;
                else if (diff > 0)
                    return 1;
                else {
                    return 0;
                }
            });

            List<SimpleTuple<Invocation, Invocation>> confusedInvocs = new ArrayList<>();

            int i = 0;
            while (i < invocs.size()) {
                SimpleTuple<Invocation, Intent> tuple = invocs.get(i);
                Invocation invc = tuple.a;

                if (invc.getConfidence() < 0.5) {
                    log.warn("Low confidence for " + tuple.b.getDisplayName() + ", " + invc.getConfidence() +
                            " for " + invc.getPattern() + ". Removing.");
                    invocs.remove(i);
                } else if (i + 1 < invocs.size() &&
                        Math.abs(invc.getConfidence() - invocs.get(i + 1).a.getConfidence()) < CONFUSE_THRESHOLD) {
                    Invocation next = invocs.get(i + 1).a;
                    StringBuilder sbLog = new StringBuilder();
                    sbLog.append("Confused between invocations (");
                    sbLog.append(invc.getConfidence());
                    sbLog.append(", ");
                    sbLog.append(next.getConfidence());
                    sbLog.append("): ");
                    sbLog.append(invc.getPattern());
                    sbLog.append(" and ");
                    sbLog.append(next.getPattern());
                    sbLog.append(". Moving to confused list.");
                    log.warn(sbLog.toString());

                    confusedInvocs.add(new SimpleTuple<>(invc, next));
                    invocs.remove(i);
                    invocs.remove(i);
                } else {
                    log.debug("Acceptable invocation " + tuple.b.getDisplayName() + ", " +
                            invc.getConfidence() + " for " + invc.getPattern() + " kept.");
                    i++;
                }
            }

            if (invocs.size() > 0) {
                Sayable s = invocs.get(0).b.processInvocation(invocs.get(0).a);
                s.speak();
                return s.toString();
            } else {
                Sayable s = new Phrase("Sorry, I did not understand that. Could you say that again?");
                s.speak();
                return s.toString();
            }
        }

        return "Unknown error";
    }

    @Override
    public ConfigTransactionLayer getConfig() {
        return Config.inst();
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public Sayable DispatchIntent(ToPhoneIntent i) {
        return null;
    }
}
