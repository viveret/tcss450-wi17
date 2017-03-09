package com.viveret.pilexa.pi.defaultskills.clockskill;

import com.viveret.pilexa.pi.event.EventPoll;
import com.viveret.pilexa.pi.event.TimerEvent;
import com.viveret.pilexa.pi.invocation.Invocation;
import com.viveret.pilexa.pi.invocation.InvocationProcessor;
import com.viveret.pilexa.pi.sayable.Phrase;
import com.viveret.pilexa.pi.sayable.Sayable;
import com.viveret.pilexa.pi.util.NLPHelper;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by viveret on 2/5/17.
 */
public class SetTimerIntent implements InvocationProcessor {
    @Override
    public Sayable processInvocation(Invocation i) {
//        int seconds = getSeconds(i);
//        int minutes = getMinutes(i);
//        int hours = getHours(i);

        if (i.hasValue("time")) {
            List<CoreLabel> words = i.getValue("time");
            long totalSeconds = 0;
            if (words.size() == 2) {
                totalSeconds = Integer.parseInt(words.get(0).word());
                String units = words.get(1).word();
                switch (units) {
                    case "hours":
                        totalSeconds *= 60;
                    case "minutes":
                        totalSeconds *= 60;
                    case "seconds":
                        break;
                    default:
                        return new Phrase("Invalid units " + units);
                }

                EventPoll.inst().addToQueue(new TimerEvent("Pilexa timer", totalSeconds));
            }
            StringBuilder sb = new StringBuilder("Setting timer for " + totalSeconds + " seconds");
//        if (hours > 0) {
//            sb.append(hours);
//            sb.append(" hours, ");
//        }
//        if (minutes > 0) {
//            sb.append(minutes);
//            sb.append(" minutes, ");
//        }
//        if (seconds > 0) {
//            if (minutes > 0 || hours > 0) {
//                sb.append("and ");
//            }
//            sb.append(seconds);
//            sb.append(" seconds.");
//        }

            return new Phrase(sb.toString());
        } else {
            return new Phrase("Error: no time");
        }
    }

    public int getSeconds(Invocation i) {
        if (i.hasValue("seconds")) {
            return Integer.parseInt(NLPHelper.join(i.getValue("seconds")));
        } else {
            return 0;
        }
    }

    public int getMinutes(Invocation i) {
        if (i.hasValue("minutes")) {
            return Integer.parseInt(NLPHelper.join(i.getValue("minutes")));
        } else {
            return 0;
        }
    }

    public int getHours(Invocation i) {
        if (i.hasValue("hours")) {
            return Integer.parseInt(NLPHelper.join(i.getValue("hours")));
        } else {
            return 0;
        }
    }
}
