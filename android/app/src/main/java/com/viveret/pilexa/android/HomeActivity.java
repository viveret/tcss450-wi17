package com.viveret.pilexa.android;

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

import com.viveret.pilexa.android.pilexa.EventPollProcessor;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.pilexa.Skill;
import com.viveret.pilexa.android.pilexa.UserAccount;
import com.viveret.pilexa.android.util.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.MalformedURLException;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PiLexaProxyConnection.PiLexaProxyConnectionHolder,
        SkillFragment.OnListFragmentInteractionListener {

    private PiLexaProxyConnection pilexa;

    private Thread myPollPilexaThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "";
                        if (pilexa != null) {
                            try {
                                msg = pilexa.getConfigString("system.name");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            msg = "could not get system.name";
                        }

                        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }).start();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment frag = new HomeFragment();
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.manage_skills) {
            SkillFragment frag = new SkillFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, frag)
                    .commit();
        } else if (id == R.id.create_skills) {

        } else if (id == R.id.install_new_plugins) {

        } else if (id == R.id.view_installed_plugins) {

        } else if (id == R.id.wizard) {

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
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                    String host = prefs.getString("pilexaHost", null);
                    int port = prefs.getInt("pilexaPort", -1);
                    pilexa = PiLexaProxyConnection.attachTo(host, port);

                    myPollPilexaThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (pilexa != null && pilexa.canConnect()) {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    break;
                                }
                                try {
                                    pilexa.processPollEvents(new EventPollProcessor() {
                                        @Override
                                        public void process(JSONObject ev) {
                                            if (ev.has("type")) {
                                                try {
                                                    switch (ev.getString("type")) {
                                                        case "androidIntent":
                                                            //Intent.makeMainSelectorActivity();
                                                            Intent i = new Intent(ev.getString("name"));
                                                            startActivity(i);
                                                            break;
                                                    }
                                                } catch (JSONException e) {
                                                    Log.e("Event poll", Log.getStackTraceString(e));
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("Event poll", Log.getStackTraceString(e));
                                }
                            }
                        }
                    });
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

    public void handleSkill(String str) {

    }
}
