package com.viveret.pilexa.pi;

import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationPattern;
import com.viveret.pilexa.pi.util.SimpleTuple;
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
///usr/lib/jvm/default-java/bin/java -Didea.launcher.port=7532 -Didea.launcher.bin.path=/home/viveret/bin/idea-IC-163.11103.6/bin -Dfile.encoding=UTF-8 -classpath /usr/lib/jvm/default-java/jre/lib/charsets.jar:/usr/lib/jvm/default-java/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/default-java/jre/lib/ext/dnsns.jar:/usr/lib/jvm/default-java/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/default-java/jre/lib/ext/jaccess.jar:/usr/lib/jvm/default-java/jre/lib/ext/localedata.jar:/usr/lib/jvm/default-java/jre/lib/ext/nashorn.jar:/usr/lib/jvm/default-java/jre/lib/ext/sunec.jar:/usr/lib/jvm/default-java/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/default-java/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/default-java/jre/lib/ext/zipfs.jar:/usr/lib/jvm/default-java/jre/lib/jce.jar:/usr/lib/jvm/default-java/jre/lib/jsse.jar:/usr/lib/jvm/default-java/jre/lib/management-agent.jar:/usr/lib/jvm/default-java/jre/lib/resources.jar:/usr/lib/jvm/default-java/jre/lib/rt.jar:/home/viveret/School/UW_Tacoma/TCSS_450/project.git/pi/build/classes/main:/home/viveret/School/UW_Tacoma/TCSS_450/project.git/pi/build/resources/main:/home/viveret/.gradle/caches/modules-2/files-2.1/joda-time/joda-time/2.9/e8a58b7f5853b693b8c4795a714fe77c266c3acc/joda-time-2.9.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/com.googlecode.efficient-java-matrix-library/ejml/0.23/fb9a880674f0d241d727ee2bc5e6a839d3007fe8/ejml-0.23.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-log4j12/1.7.0/8837262ff98225a81608b8c11aea642d9449228b/slf4j-log4j12-1.7.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/com.apple/AppleJavaExtensions/1.4/c586cd0b44cae8c0239a977277f99d08d751a482/AppleJavaExtensions-1.4.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/de.jollyday/jollyday/0.4.9/f29c859f06027772ea70a8412db7322eba8930cb/jollyday-0.4.9.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-lang3/3.3.1/6738a2da2202ce360f0af90aba005c1e05a2c4cd/commons-lang3-3.3.1.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.apache.lucene/lucene-queryparser/4.10.3/e7a0384c99257136e7295efd69516615b19d92bc/lucene-queryparser-4.10.3.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/javax.xml.bind/jaxb-api/2.2.7/2f51c4bb4724ea408096ee9100ff2827e07e5b7c/jaxb-api-2.2.7.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.apache.lucene/lucene-queries/4.10.3/40a4d378a857ec0c7cc32d3122a36f22beae2fd7/lucene-queries-4.10.3.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.apache.lucene/lucene-core/4.10.3/ea0344b37d8668e6beb21ec41c67c9ca2df9f2a4/lucene-core-4.10.3.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/javax.servlet/javax.servlet-api/3.0.1/6bf0ebb7efd993e222fc1112377b5e92a13b38dd/javax.servlet-api-3.0.1.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/de.sciss/sphinx4-core/1.0.0/3368c008971280ded53cc40165492162d037ffe6/sphinx4-core-1.0.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/de.sciss/sphinx4-data/1.0.0/19af6ae5c722e9f0db7d287e604abbe4b3aa955a/sphinx4-data-1.0.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.apache.lucene/lucene-analyzers-common/4.10.3/2fc01f6bfc26d282a96da36a367755f61b4074a8/lucene-analyzers-common-4.10.3.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.glassfish/javax.json/1.0.4/3178f73569fd7a1e5ffc464e680f7a8cc784b85a/javax.json-1.0.4.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-api/1.7.12/8e20852d05222dc286bf1c71d78d0531e177c317/slf4j-api-1.7.12.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/com.google.protobuf/protobuf-java/3.1.0/e13484d9da178399d32d2d27ee21a77cfb4b7873/protobuf-java-3.1.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-math3/3.6/825c90790e83af784be1de0a3f3a9f066cf884b9/commons-math3-3.6.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/log4j/log4j/1.2.17/5af35056b4d257e4b64b9e8069c0746e8b08629f/log4j-1.2.17.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/edu.stanford.nlp/stanford-corenlp/3.7.0/10fa3c86f50edc2b7765b2abeff92b6529acb7a6/stanford-corenlp-3.7.0-models.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.apache.lucene/lucene-sandbox/4.10.3/39ecd581b636adc15379dbd53878928e0f05a89c/lucene-sandbox-4.10.3.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/xml-apis/xml-apis/2.0.2/3136ca936f64c9d68529f048c2618bd356bf85c9/xml-apis-2.0.2.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/edu.stanford.nlp/stanford-corenlp/3.7.0/c5bc610f2014ffbd7686f0d97212b846717412b0/stanford-corenlp-3.7.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/org.slf4j/jcl-over-slf4j/1.7.0/cf900853d28acf86082cda1004729cf6a5dd9aae/jcl-over-slf4j-1.7.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/xerces/xercesImpl/2.8.0/cfd3ebe2f8034e660344f9108c3e2daf78c29cc3/xercesImpl-2.8.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/com.io7m.xom/xom/1.2.10/4165e25bef19aad134f6498cc277110b9bc5e52b/xom-1.2.10.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/edu.stanford.nlp/stanford-parser/3.7.0/90a1bac53827d541e2a9f3743404b5de0729b8cb/stanford-parser-3.7.0.jar:/home/viveret/.gradle/caches/modules-2/files-2.1/xalan/xalan/2.7.0/a33c0097f1c70b20fa7ded220ea317eb3500515e/xalan-2.7.0.jar:/home/viveret/bin/idea-IC-163.11103.6/lib/idea_rt.jar com.intellij.rt.execution.application.AppMain RepeatBackToMeSkill

/**
 * Created by viveret on 1/24/17.
 */
public class ConcretePiLexaService implements PiLexaService {
    private static final List<Skill> mySkills = new ArrayList<>();
    private Logger log;
    private StanfordCoreNLP pipeline;

    public static final void registerSkill(Skill s) {
        mySkills.add(s);
    }

    @Override
    public void connect() {
        BasicConfigurator.configure();

        initCoreNLP();

        log = Logger.getRootLogger();// Logger.getLogger(getClass().getName());
        log.info("Connected to PiLexa Service.");
    }

    private void initCoreNLP() {
        // build pipeline
        pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,ner,parse,natlog",
                        "ssplit.isOneSentence", "true",
                        "parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
                        "tokenize.language", "en"));
    }

    @Override
    public void disconnect() {
        log.info("Disconnected from PiLexa Service.");
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public int interpret(String str) {
        log.info("Interpreting \"" + str + "\"");

        Annotation document = new Annotation(str);
        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            List<SimpleTuple<Invocation, Intent>> invocs = new ArrayList<>();

            for (Skill s : mySkills) {
                for (SimpleTuple<InvocationPattern, Intent> patt : s.getPossibleIntents()) {
                    invocs.add(new SimpleTuple<Invocation, Intent>(patt.a.parse(sentence), patt.b));
                }
            }

            Collections.sort(invocs, (a, b) -> {
                double diff = a.a.getConfidence() - b.a.getConfidence();
                if (diff < 0)
                    return -1;
                else if (diff > 0)
                    return 1;
                else {
                    return 0;
                }
            });

            List<SimpleTuple<Invocation, Invocation>> confusedInvocs = new ArrayList<>();

            for (int i = 0; i < invocs.size(); i++) {
                Invocation invc = invocs.get(i).a;

                if (invc.getConfidence() < 0.5) {
                    log.warn("Low confidence for " + invc.toString() + ". Removing.");
                    invocs.remove(i);
                    i--;
                } else if (i + 1 < invocs.size() && invc.getConfidence() - invocs.get(i + 1).a.getConfidence() < 0.1) {
                    Invocation next = invocs.get(i + 1).a;
                    StringBuilder sbLog = new StringBuilder();
                    sbLog.append("Confused between invocations: ");
                    sbLog.append(invc.toString());
                    sbLog.append(" and ");
                    sbLog.append(next.toString());
                    sbLog.append(". Moving to confused list.");
                    log.info(sbLog.toString());

                    confusedInvocs.add(new SimpleTuple<>(invc, next));
                    invocs.remove(i);
                    invocs.remove(i + 1);
                    i -= 2;
                } else {
                    log.debug("Acceptable invocation " + invc.toString() + " kept.");
                }
            }

            if (invocs.size() > 0) {
                invocs.get(0).b.processInvocation(invocs.get(0).a);
            }
        }

        return 0;
    }

    @Override
    public Logger getLog() {
        return log;
    }
}
