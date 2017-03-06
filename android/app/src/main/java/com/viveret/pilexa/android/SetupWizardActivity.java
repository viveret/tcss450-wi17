package com.viveret.pilexa.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.setup.FindPilexaServiceFragment;

public class SetupWizardActivity extends Activity implements FindPilexaServiceFragment.OnPilexaServiceSelected {
    private int myStepAt;

    private Button prevBtn, nextBtn;
    private PiLexaProxyConnection myPilexa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wizard);

        prevBtn = (Button) findViewById(R.id.previousBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStepAt--;
                switchToStep();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStepAt++;
                switchToStep();
            }
        });

        myStepAt = 0;
        switchToStep();
    }

    private void switchToStep() {
        Fragment f = null;
        prevBtn.setEnabled(myStepAt > 0);

        switch (myStepAt) {
            case 0:
                f = new FindPilexaServiceFragment();
                break;
            default:
                Toast.makeText(this, "Invalid step at " + myStepAt, Toast.LENGTH_LONG);
                break;
        }

        if (f != null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
        }
    }

    @Override
    public void onPilexaServiceSelected(PiLexaProxyConnection conn) {
        Toast.makeText(this, "Selected " + conn.toString(), Toast.LENGTH_LONG);
        myPilexa = conn;
        nextBtn.callOnClick();
    }
}
