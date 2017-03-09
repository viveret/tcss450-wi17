package com.viveret.pilexa.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.viveret.pilexa.android.util.AppHelper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppHelper appHelper = new AppHelper(PreferenceManager.getDefaultSharedPreferences(this));

        Intent i;
        if (appHelper.hasSavedConnection()) {
            if (appHelper.hasSavedUser()) {
                i = new Intent(this, HomeActivity.class);
            } else {
                i = new Intent(this, LoginOrRegisterActivity.class);
            }
        } else {
            i = new Intent(this, SetupWizardActivity.class);
        }
        startActivity(i);
        finish();
    }
}
