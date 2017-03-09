package com.viveret.pilexa.android;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.viveret.pilexa.android.pilexa.*;
import com.viveret.pilexa.android.util.AppHelper;

import java.net.MalformedURLException;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PiLexaProxyConnection.PiLexaProxyConnectionHolder,
        SkillFragment.OnListFragmentInteractionListener {

    private PiLexaProxyConnection pilexa;
    private Thread myPollPilexaThread = null;
    private Fragment myCurrentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment frag = new HomeFragment();
        myCurrentFragment = frag;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            }
            case R.id.action_logout: {
                new AppHelper(PreferenceManager.getDefaultSharedPreferences(this)).logout();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            }
            case R.id.action_forget_conn: {
                new AppHelper(PreferenceManager.getDefaultSharedPreferences(this)).forgetConnection();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            }
            case R.id.action_clear_msgs: {
                if (myCurrentFragment instanceof HomeFragment) {
                    ((HomeFragment) myCurrentFragment).clearMessages();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.manage_skills) {
            SkillFragment frag = new SkillFragment();
            myCurrentFragment = frag;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, frag)
                    .commit();
        } else if (id == R.id.create_skills) {

        } else if (id == R.id.install_new_plugins) {

        } else if (id == R.id.view_installed_plugins) {

        } else if (id == R.id.wizard) {
            Intent i = new Intent(this, SetupWizardActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.manual) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pilexa = new AppHelper(PreferenceManager.getDefaultSharedPreferences(HomeActivity.this)).makeConnection();
                    myPollPilexaThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (HomeActivity.this != null && pilexa != null && pilexa.canConnect()) {
                                try {
                                    pilexa.processPollEvents(new DefaultEventPollProcessor(HomeActivity.this));
                                } catch (Exception e) {
                                    Log.e("Event poll", Log.getStackTraceString(e));
                                }
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    break;
                                }
                            }
                        }
                    });
                    myPollPilexaThread.start();
                } /*catch (ConnectException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Could not connect to pi", Toast.LENGTH_LONG);
                }*/ catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Bad url for pi", Toast.LENGTH_LONG);
                }
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        pilexa = null;
    }

    @Override
    public PiLexaProxyConnection getPilexa() {
        return pilexa;
    }

    @Override
    public void onListFragmentInteraction(Skill item) {

    }
}
