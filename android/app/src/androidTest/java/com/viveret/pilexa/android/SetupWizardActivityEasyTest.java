package com.viveret.pilexa.android;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * Tests the setup wizard through the easy branch. If it fails, change the visibility of the progress spinner
 * in the "fragment_pilexaconn_list" layout (change to invisible). Also make sure to disable animations in the Developer
 * options.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SetupWizardActivityEasyTest {
    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test.
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<SetupWizardActivity> mActivityRule = new ActivityTestRule<>(
            SetupWizardActivity.class);

    @Before
    public void clearState() {
        try {
            onView(withId(R.id.action_forget_conn))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            // Dont do anything
        }
    }

    @Test
    public void testCanFindPiLexaService() {
        try {
            onView(withId(R.id.action_forget_conn))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            try {
                onView(withId(R.id.easyBtn))
                        .perform(click());
            } catch (NoMatchingViewException e2) {
                mActivityRule.getActivity().onBackPressed();
            }
        }

        for (int i = 0; i < 59; i++) {
            try {
                Thread.sleep(1000); // Wait a minute before selecting pilexa
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                onView(withText("PiLexa TCSS 450")).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException e) {
                // Do nothing while trying
            }
        }
        onView(withText("pilexa"))
                // .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterInvalidUsername() {
        // findAndConnectToPi();

        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText(" "));
        onView(withId(R.id.password))
                .perform(typeText("test"));
        onView(withId(R.id.registerBtn))
                .perform(click());

        onView(withText("Invalid username"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testRegisterInvalidPassword() {
        // findAndConnectToPi();

        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText("viveret2"));
        onView(withId(R.id.password))
                .perform(typeText(""));
        onView(withId(R.id.registerBtn))
                .perform(click());

        onView(withText("Invalid password"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testLoginInvalidUsername() {
        findAndConnectToPi();

        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText("viveret2"));
        onView(withId(R.id.password))
                .perform(typeText("awd"));
        onView(withId(R.id.loginBtn))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("User not found: viveret2"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testLoginInvalidPassword() {
        findAndConnectToPi();

        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText("viveret"));
        onView(withId(R.id.password))
                .perform(typeText("awd"));
        onView(withId(R.id.loginBtn))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Invalid password"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testCanLogin() {
        findAndConnectToPi();

        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText("viveret"));
        onView(withId(R.id.password))
                .perform(typeText("test"));
        onView(withId(R.id.loginBtn))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Done!"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCanRegister() {
        findAndConnectToPi();

        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText("viveret" + new Random(java.util.Calendar.getInstance().getTimeInMillis()).nextInt()));
        onView(withId(R.id.password))
                .perform(typeText("test"));
        onView(withId(R.id.registerBtn))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Done!"))
                .check(matches(isDisplayed()));
    }

    private void findAndConnectToPi() {
        try {
            onView(withId(R.id.easyBtn))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            try {
                onView(withId(R.id.action_forget_conn))
                        .perform(click());
                onView(withId(R.id.easyBtn))
                        .perform(click());
            } catch (NoMatchingViewException e2) {
                return;
            }
        }

        for (int i = 0; i < 59; i++) {
            try {
                Thread.sleep(1000); // Wait a minute before selecting pilexa
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                onView(withText("PiLexa TCSS 450")).perform(click());
                return;
            } catch (NoMatchingViewException e) {
                // Do nothing while trying
            }
        }
    }
}