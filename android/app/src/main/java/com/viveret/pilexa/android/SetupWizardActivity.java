package com.viveret.pilexa.android;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.pilexa.UserAccount;
import com.viveret.pilexa.android.setup.*;
import com.viveret.pilexa.android.util.AppHelper;

public class SetupWizardActivity extends Activity implements OnPilexaServiceSelected,
        WelcomeToTheWizardFragment.OnWelcomeInteractionListener, PiLexaProxyConnection.PiLexaProxyConnectionHolder {

    private int myStepAt;
    private boolean myIsManual = false;

    private PiLexaProxyConnection myPilexa;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wizard);

        myStepAt = 0;
        switchToStep();
    }

    @Override
    public void onBackPressed() {
        if (myStepAt > 0) {
            myStepAt--;
            switchToStep();
        } else {
            super.onBackPressed();
        }
    }

    public void nextStep() {
        myStepAt++;
        switchToStep();
    }

    private void switchToStep() {
        Fragment f = null;
        switch (myStepAt) {
            case 0:
                f = new WelcomeToTheWizardFragment();
                break;
            case 1:
                if (myIsManual) {
                    f = new ManualPilexaConnectionFragment();
                } else {
                    f = new FindPilexaServiceFragment();
                }
                break;
            case 2:
                f = new DoneFragment();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(SetupWizardActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                t.start();
                break;
            default:
                Toast.makeText(this, "Invalid step at " + myStepAt, Toast.LENGTH_LONG).show();
                break;
        }

        if (f != null) {
            currentFragment = f;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
        } else {
            getFragmentManager().beginTransaction().remove(currentFragment).commit();
        }
    }

    @Override
    public void onPilexaServiceSelected(final PiLexaProxyConnection conn) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(SetupWizardActivity.this, "Selected " + conn.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
        myPilexa = conn;
        AppHelper app = new AppHelper(PreferenceManager.getDefaultSharedPreferences(this));
        app.saveConnection(conn);
        nextStep();
    }

    @Override
    public void onManualWizardSelected() {
        myIsManual = true;
        nextStep();
    }

    @Override
    public void onEasyWizardSelected() {
        myIsManual = false;
        nextStep();
    }

    @Override
    public PiLexaProxyConnection getPilexa() {
        return myPilexa;
    }
}
