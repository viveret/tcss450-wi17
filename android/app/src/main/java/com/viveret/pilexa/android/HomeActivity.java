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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.pilexa.Skill;

import java.net.ConnectException;
import java.net.MalformedURLException;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PiLexaProxyConnection.PiLexaProxyConnectionHolder,
        SkillFragment.OnListFragmentInteractionListener {

    private PiLexaProxyConnection pilexa;

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
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
                    String host = prefs.getString("keystring", getString(R.string.pref_default_host));
                    pilexa = PiLexaProxyConnection.attachTo(host);
                } catch (ConnectException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Could not connect to pi", Toast.LENGTH_LONG);
                } catch (MalformedURLException e) {
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
