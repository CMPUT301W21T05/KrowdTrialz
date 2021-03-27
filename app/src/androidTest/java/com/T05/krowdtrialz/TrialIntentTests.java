package com.T05.krowdtrialz;

import android.app.Activity;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.T05.krowdtrialz.ui.SplashActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for user stories related to actions on experiments
 * @author Furmaan Sekhon and Jacques Leong-Sit
 */
public class TrialIntentTests {
    private Solo solo;
    private String description = "Max jump height";
    private String minTrials = "16";
    private String region = "Canada";
    private String unit = "cm";
    private String searchTerm = "Max jump";
    private String value = "50";

    @Rule
    public ActivityTestRule<MainActivity> ruleMain =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @ClassRule
    public static ActivityTestRule<SplashActivity> ruleSplash =
            new ActivityTestRule<>(SplashActivity.class, true, true);

    @BeforeClass
    public static void beforeClass() {
        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), ruleSplash.getActivity());
        Activity activity = ruleSplash.getActivity();
        solo.waitForActivity(SplashActivity.class);
    }

    @Before
    public void before() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), ruleMain.getActivity());
        Activity activity = ruleMain.getActivity();
        solo.waitForActivity(MainActivity.class);
    }

    @Test
    /**
     * @alert Verification for US 01.05.01 and US 06.03.01
     */
    public void testAddTrialsAndLocationWarning(){
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click add trials button
        solo.waitForView(solo.getView(R.id.add_trials_experiment));
        solo.clickOnView(solo.getView(R.id.add_trials_experiment));

        //Make sure warning appears
        assertTrue(solo.waitForText("Warning! This experiment requires your location."));

        //Click through warning
        solo.clickOnText("Continue");

        //Click measurement value EditText
        solo.waitForView(solo.getView(R.id.submit_trial_button));
        EditText measureEditText = (EditText) solo.getView(R.id.measure_editText);
        solo.clickOnView(measureEditText);

        //Type a measurement value
        solo.typeText(measureEditText, value);

        //Click submit button
        solo.clickOnView(solo.getView(R.id.submit_trial_button));

        //Go back to subscribed page
        solo.waitForView(solo.getView(R.id.add_trials_experiment));
        solo.goBack();
        solo.hideSoftKeyboard();
        solo.goBack();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on stats tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("Stats");

        //Make sure trial appears in stats
        assertFalse(((TextView)solo.getView(R.id.q1)).getText().equals("NaN"));

        //Go back to subscribed page
        solo.goBack();
        solo.hideSoftKeyboard();
        solo.goBack();

        unpublish();
    }

    @Test
    /**
     * @alert Verification for US 01.08.01
     */
    public void testIgnoreResults(){
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click add trials button
        solo.waitForView(solo.getView(R.id.add_trials_experiment));
        solo.clickOnView(solo.getView(R.id.add_trials_experiment));

        //Make sure warning appears
        assertTrue(solo.waitForText("Warning! This experiment requires your location."));

        //Click through warning
        solo.clickOnText("Continue");

        //Click measurement value EditText
        solo.waitForView(solo.getView(R.id.submit_trial_button));
        EditText measureEditText = (EditText) solo.getView(R.id.measure_editText);
        solo.clickOnView(measureEditText);

        //Type a measurement value
        solo.typeText(measureEditText, value);

        //Click submit button
        solo.clickOnView(solo.getView(R.id.submit_trial_button));

        //Go back to subscribed page
        solo.waitForView(solo.getView(R.id.add_trials_experiment));
        solo.goBack();
        solo.hideSoftKeyboard();
        solo.goBack();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on stats tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("Stats");

        //Make sure trial appears in stats with the correct value
        assertTrue(((TextView)solo.getView(R.id.mean)).getText().equals("50.0"));

        //Click on more tab
        solo.clickOnText("More");

        //Click on view contributors button
        solo.clickOnView(solo.getView(R.id.view_contributors_button));

        //Check off the contributor
        solo.waitForView(solo.getView(R.id.contributors_title_textView));
        solo.clickOnView(solo.getView(R.id.ignore_contributor_checkbox));

        //Go back to subscribed page
        solo.goBack();
        solo.goBack();
        solo.hideSoftKeyboard();
        solo.goBack();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on stats tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("Stats");

        //Make sure no trial appears in the results
        assertTrue(((TextView)solo.getView(R.id.mean)).getText().equals("NaN"));

        //Click on more tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("More");

        //Click on unpublish button
        solo.clickOnView(solo.getView(R.id.unpublish_experiment_button));
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    private void search(){
        //Click on search icon
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        SearchView searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, searchTerm);
    }

    private void publish(){
        //Click on publish tab
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.navigation_publish));
        solo.waitForView(R.id.geo_location_toggle);

        //Click on measurement
        solo.clickOnView(solo.getView(R.id.measurement_experiment_radio));

        //Click on add a description editText
        EditText descriptionBox = (EditText) solo.getView(R.id.experiment_description_input);
        solo.clickOnView(descriptionBox);
        solo.sleep(1000);

        //Type in a description
        solo.typeText(descriptionBox, description);

        //Click on geo location required toggle
        solo.clickOnView(solo.getView(R.id.geo_location_toggle));

        //Click on min trials editText
        EditText minTrialsBox = (EditText) solo.getView(R.id.minimum_trials_input);
        solo.clickOnView(minTrialsBox);
        solo.sleep(500);

        //Type in minTrials
        solo.typeText(minTrialsBox, minTrials);

        //Click on experiment region
        EditText regionBox = (EditText) solo.getView(R.id.experiment_region_input);
        solo.clickOnView(regionBox);
        solo.sleep(500);

        //Type in a region
        solo.typeText(regionBox, region);

        //Click on unit
        EditText unitBox = (EditText) solo.getView(R.id.edit_username_text);
        solo.clickOnView(unitBox);
        solo.sleep(500);

        //Type in a unit
        solo.typeText(unitBox, unit);

        //Click on publish
        solo.clickOnView(solo.getView(R.id.publish_experiment_button));
    }

    private void unpublish(){
        //Click on search icon
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        SearchView searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, searchTerm);

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on more tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("More");

        //Click on unpublish button
        solo.clickOnView(solo.getView(R.id.unpublish_experiment_button));
    }
}
