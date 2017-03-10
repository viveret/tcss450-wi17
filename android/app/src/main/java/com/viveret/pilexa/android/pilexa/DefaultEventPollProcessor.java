package com.viveret.pilexa.android.pilexa;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import static java.net.Proxy.Type.HTTP;

/**
 * Implementation of an EvenPollProcessor that integrates the Android intents with events from the PiLexa.
 */
public class DefaultEventPollProcessor extends EventPollProcessor {
    private final Activity myActivity;

    /**
     * Create a new DefaultEventPollProcessor.
     * @param myActivity the activity this is being used in.
     */
    public DefaultEventPollProcessor(Activity myActivity) {
        this.myActivity = myActivity;
    }

    @Override
    public void process(JSONObject ev) {
        if (ev.has("type")) {
            try {
                switch (ev.getString("type")) {
                    case "androidIntent": {
                        //Intent.makeMainSelectorActivity();
                        Intent i = new Intent(ev.getString("name"));
                        myActivity.startActivity(i);
                    } break;
                    case "setTimer": {
                        Intent i = new Intent(AlarmClock.ACTION_SET_TIMER)
                                .putExtra(AlarmClock.EXTRA_MESSAGE, ev.getString("timerMsg"))
                                .putExtra(AlarmClock.EXTRA_LENGTH, ev.getInt("length"))
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        if (i.resolveActivity(myActivity.getPackageManager()) != null) {
                            myActivity.startActivity(i);
                        }
                    } break;
                    case "search": {
                        Intent i = new Intent(Intent.ACTION_WEB_SEARCH)
                                .putExtra(SearchManager.QUERY, ev.getString("query"));
                        if (i.resolveActivity(myActivity.getPackageManager()) != null) {
                            myActivity.startActivity(i);
                        }
                    } break;
                    case "text": {
                        Intent i = new Intent(Intent.ACTION_SENDTO);
                        i.setData(Uri.parse("smsto:" + ev.getString("number")));
                        i.putExtra("sms_body", ev.getString("message"));
                        if (i.resolveActivity(myActivity.getPackageManager()) != null) {
                            myActivity.startActivity(i);
                        }
                    } break;
                    case "music": {
                        Intent i = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                        i.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, "vnd.android.cursor.item/*");
                        i.putExtra(SearchManager.QUERY, ev.getString("query"));
                        if (i.resolveActivity(myActivity.getPackageManager()) != null) {
                            myActivity.startActivity(i);
                        }
                    } break;
                }
            } catch (JSONException e) {
                Log.e("Event poll", Log.getStackTraceString(e));
            }
        }
    }
}
