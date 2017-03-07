package com.viveret.pilexa.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.setup.FindPilexaServiceFragment;
import com.viveret.pilexa.android.setup.WelcomeToTheWizardFragment;

public class SetupWizardActivity extends Activity implements FindPilexaServiceFragment.OnPilexaServiceSelected,
        WelcomeToTheWizardFragment.OnWelcomeInteractionListener {

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

                } else {
                    f = new FindPilexaServiceFragment();
                }
                break;
            default:
                Toast.makeText(this, "Invalid step at " + myStepAt, Toast.LENGTH_LONG);
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
    public void onPilexaServiceSelected(PiLexaProxyConnection conn) {
        Toast.makeText(this, "Selected " + conn.toString(), Toast.LENGTH_LONG);
        myPilexa = conn;
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
}
