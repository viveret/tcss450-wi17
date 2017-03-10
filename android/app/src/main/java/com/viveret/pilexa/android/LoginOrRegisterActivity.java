package com.viveret.pilexa.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.pilexa.UserAccount;
import com.viveret.pilexa.android.util.AppHelper;

import java.net.MalformedURLException;

public class LoginOrRegisterActivity extends Activity implements LoginOrCreateAcctFragment.OnLoginOrCreateAcctListener,
        PiLexaProxyConnection.PiLexaProxyConnectionHolder {

    private PiLexaProxyConnection myConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myConn = new AppHelper(PreferenceManager.getDefaultSharedPreferences(LoginOrRegisterActivity.this)).makeConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserLogin(UserAccount user) {
        AppHelper appHelper = new AppHelper(PreferenceManager.getDefaultSharedPreferences(this));
        appHelper.saveUser(user);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public PiLexaProxyConnection getPilexa() {
        return myConn;
    }
}
