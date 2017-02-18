package com.viveret.pilexa.pi.invocation;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import org.apache.log4j.Logger;

/**
 * Created by viveret on 2/5/17.
 */
public interface InvocationPattern {
    static void printInfoAboutWord(CoreLabel w) {
        String text = w.get(CoreAnnotations.TextAnnotation.class);
        // this is the POS tag of the token
        String pos = w.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        // this is the NER label of the token
        String ne = w.get(CoreAnnotations.NamedEntityTagAnnotation.class);

        String isMonth = w.get(CoreAnnotations.MonthAnnotation.class);
        String scat1 = w.get(CoreAnnotations.CategoryAnnotation.class);
        String scat2 = w.get(CoreAnnotations.CommonWordsAnnotation.class);
        String scat3 = w.get(CoreAnnotations.DayAnnotation.class);
        String scat4 = w.get(CoreAnnotations.FeaturesAnnotation.class);
        String scat5 = w.get(CoreAnnotations.GoldAnswerAnnotation.class);
        String scat6 = w.get(CoreAnnotations.IsURLAnnotation.class);
        String scat7 = w.get(CoreAnnotations.LabelAnnotation.class);
        String scat8 = w.get(CoreAnnotations.MonthAnnotation.class);
        String scat9 = w.get(CoreAnnotations.WordSenseAnnotation.class);
        //String scat9 = w.get(CoreAnnotations.PercentAnnotation.class);
        //String scat9 = w.get(CoreAnnotations.PhraseWordsAnnotation.class);

        Logger log = Logger.getRootLogger();
        StringBuilder sb = new StringBuilder();

        sb.append("Word \"" + text + "\" ");
        sb.append("pos = " + pos + ", ");
        sb.append("ne = " + ne + ", ");
        sb.append("month = " + isMonth + ", ");
        sb.append("scat1 = " + scat1 + ", ");
        sb.append("scat2 = " + scat2 + ", ");
        sb.append("scat3 = " + scat3 + ", ");
        sb.append("scat4 = " + scat4 + ", ");
        sb.append("scat5 = " + scat5 + ", ");
        sb.append("scat6 = " + scat6 + ", ");
        sb.append("scat7 = " + scat7 + ", ");
        sb.append("scat8 = " + scat8 + ", ");
        sb.append("scat9 = " + scat9 + "");

        log.debug(sb.toString());
    }

    Invocation parse(CoreMap sentence);

    @Override
    String toString();
}
